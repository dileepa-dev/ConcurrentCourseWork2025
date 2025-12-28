package lk.dileepa.hospitalSystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consultant implements Runnable {

    private final Shift shift;
    private final Speciality speciality;
    private final BlockingQueue<Patient> queue;
    private final AtomicBoolean shiftRunning;

    public Consultant(Shift shift,
                      Speciality speciality,
                      BlockingQueue<Patient> queue,
                      AtomicBoolean shiftRunning) {
        this.shift = shift;
        this.speciality = speciality;
        this.queue = queue;
        this.shiftRunning = shiftRunning;
    }

    @Override
    public void run() {
        try {
            while (shiftRunning.get()) {

                Patient patient = queue.poll(1, TimeUnit.SECONDS);
                if (patient == null) continue;

                System.out.println(
                        shift + " - SHIFT " + speciality +
                                " is treating patient " + patient.getPatientId()
                );

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}