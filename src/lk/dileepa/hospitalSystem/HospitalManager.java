package lk.dileepa.hospitalSystem;

import lombok.Getter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controls shift scheduling, consultant lifecycle,
 * and overall simulation management.
 */
@Getter
public class HospitalManager {

    // One queue per speciality
    private final BlockingQueue<Patient>[] queues;

    private ExecutorService consultantPool;

    // Handles automatic shift rotation
    private final ScheduledExecutorService shiftScheduler =
            Executors.newSingleThreadScheduledExecutor();

    private final AtomicReference<Shift> currentShift =
            new AtomicReference<>(Shift.DAY);

    private AtomicBoolean shiftRunning;
    private static final int MAX_SHIFTS = 4; // 2 days
    private final AtomicInteger shiftCount = new AtomicInteger(0);

    private final AtomicBoolean simulationRunning = new AtomicBoolean(true);
    private final HospitalStatus status = new HospitalStatus();

    public HospitalManager() {
        queues = new BlockingQueue[3];
        for (int i = 0; i < 3; i++) {
            queues[i] = new LinkedBlockingQueue<>();
        }
    }

    public void start() {
        startShift(currentShift.get());

        // Rotate shifts every 12 seconds (12 hours simulated)
        shiftScheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("\n--- SHIFT CHANGE ---\n");

                shiftRunning.set(false);
                consultantPool.shutdown();
                consultantPool.awaitTermination(5, TimeUnit.SECONDS);

                int completedShifts = shiftCount.incrementAndGet();

                // Stop simulation after 2 days
                if (completedShifts >= MAX_SHIFTS) {
                    System.out.println("Simulation completed (2 days).");
                    simulationRunning.set(false);

                    shiftRunning.set(false);
                    consultantPool.shutdown();
                    consultantPool.awaitTermination(10, TimeUnit.SECONDS);

                    status.printSummary();
                    shiftScheduler.shutdown();
                    return;
                }

                // Start next shift
                Shift next = currentShift.get().next();
                currentShift.set(next);
                startShift(next);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, 12, 12, TimeUnit.SECONDS);
    }

    private void startShift(Shift shift) {
        shiftRunning = new AtomicBoolean(true);
        consultantPool = Executors.newFixedThreadPool(3);

        // One consultant per speciality
        for (Speciality s : Speciality.values()) {
            consultantPool.submit(
                    new Consultant(
                            shift,
                            s,
                            queues[s.ordinal()],
                            shiftRunning,
                            status
                    )
            );
        }
    }
}