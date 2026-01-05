package lk.dileepa.hospitalSystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
            while (shiftRunning.get() || !queue.isEmpty()) {
                Patient patient = queue.poll(1, TimeUnit.SECONDS);
                if (patient == null) continue;

                System.out.println(
                        shift + " - SHIFT " + speciality +
                                " is treating patient " + patient.getPatientId()
                );
                status.recordTreated();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}