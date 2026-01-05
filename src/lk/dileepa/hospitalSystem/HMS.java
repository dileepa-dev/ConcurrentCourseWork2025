package lk.dileepa.hospitalSystem;

public class HMS {
    public static void main(String[] args) {
        HospitalManager manager = new HospitalManager();
        manager.start();

        Thread generator = new Thread(
                new PatientGenerator(
                        manager.getQueues(),
                        manager.getSimulationRunning(),
                        manager.getStatus()
                )
        );
        generator.start();
    }
}