package lk.dileepa.hospitalSystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Consultant thread that treats patients of a specific speciality
 * during a given shift.
 */
public class Consultant implements Runnable {

    private final Shift shift;
    private final Speciality speciality;
    private final BlockingQueue<Patient> queue;
    private final AtomicBoolean shiftRunning;
    private final HospitalStatus status;

    public Consultant(Shift shift,
                      Speciality speciality,
                      BlockingQueue<Patient> queue,
                      AtomicBoolean shiftRunning,
                      HospitalStatus status) {
        this.shift = shift;
        this.speciality = speciality;
        this.queue = queue;
        this.shiftRunning = shiftRunning;
        this.status = status;
    }

    @Override
    public void run() {
        try {
            // Continue while shift is active or patients remain in queue
            while (shiftRunning.get() || !queue.isEmpty()) {

                // Poll with timeout to avoid busy waiting
                Patient patient = queue.poll(1, TimeUnit.SECONDS);
                if (patient == null) continue;

                System.out.println(
                        shift + " SHIFT - " + speciality +
                                " treating patient " + patient.getPatientId()
                );

                status.recordTreated();
                Thread.sleep(1000); // Simulated treatment time
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}