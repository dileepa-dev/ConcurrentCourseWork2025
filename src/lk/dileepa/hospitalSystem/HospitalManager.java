package lk.dileepa.hospitalSystem;

import lombok.Getter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class HospitalManager {

    private final BlockingQueue<Patient>[] queues;
    private ExecutorService consultantPool;

    private final ScheduledExecutorService shiftScheduler =
            Executors.newSingleThreadScheduledExecutor();

    private final AtomicReference<Shift> currentShift =
            new AtomicReference<>(Shift.DAY);

    private AtomicBoolean shiftRunning; // NEW per-shift flag
    private static final int MAX_SHIFTS = 4;
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

        shiftScheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("\n--- SHIFT CHANGE ---\n");

                // Stop old shift
                shiftRunning.set(false);
                consultantPool.shutdown();
                consultantPool.awaitTermination(5, TimeUnit.SECONDS);

                int completedShifts = shiftCount.incrementAndGet();

                if (completedShifts >= MAX_SHIFTS) {

                    System.out.println("Simulation completed (2 days).");
                    simulationRunning.set(false); // stop arrivals

                    shiftRunning.set(false);      // allow consultants to exit AFTER draining
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