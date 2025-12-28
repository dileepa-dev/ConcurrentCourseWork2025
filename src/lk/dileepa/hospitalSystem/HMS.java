package lk.dileepa.hospitalSystem;

public class HMS {
    public static void main(String[] args) {

        // Start hospital system (shifts + consultants)
        HospitalManager manager = new HospitalManager();
        manager.start();

        // Start continuous patient arrival simulation
        Thread generator = new Thread(
                new PatientGenerator(
                        manager.getQueues()
                )
        );

        generator.start();
    }
}
