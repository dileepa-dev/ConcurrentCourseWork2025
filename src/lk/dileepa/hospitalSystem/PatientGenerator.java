package lk.dileepa.hospitalSystem;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Continuously generates patients with random specialities.
 * Acts as the producer in the producer–consumer model.
 */
public class PatientGenerator implements Runnable {

    private final BlockingQueue<Patient>[] queues;
    private final Random random = new Random();
    private int patientId = 1;
    private final HospitalStatus status;
    private final AtomicBoolean simulationRunning;

    public PatientGenerator(BlockingQueue<Patient>[] queues,
                            AtomicBoolean simulationRunning,
                            HospitalStatus status) {
        this.queues = queues;
        this.status = status;
        this.simulationRunning = simulationRunning;
    }

    @Override
    public void run() {
        try {
            while (simulationRunning.get()) {

                Speciality speciality =
                        Speciality.values()[random.nextInt(3)];

                Patient patient = new Patient(patientId++, speciality);
                status.recordArrival();

                System.out.println("Patient " + patient.getPatientId()
                        + " arrived for " + speciality);

                queues[speciality.ordinal()].put(patient);

                // Random arrival interval
                Thread.sleep(500 + random.nextInt(1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}