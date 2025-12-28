package lk.dileepa.hospitalSystem;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PatientGenerator implements Runnable {

    private final BlockingQueue<Patient>[] queues;
    private final Random random = new Random();
    private int patientId = 1;

    public PatientGenerator(BlockingQueue<Patient>[] queues) {
        this.queues = queues;
    }

    @Override
    public void run() {
        try {
            while (true) {

                Speciality speciality =
                        Speciality.values()[random.nextInt(3)];

                Patient patient = new Patient(patientId++, speciality);

                System.out.println(
                        "Patient " + patient.getPatientId() +
                                " arrived for " + speciality
                );

                queues[speciality.ordinal()].put(patient);

                Thread.sleep(500 + random.nextInt(1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
