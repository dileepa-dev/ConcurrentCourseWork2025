package lk.dileepa.hospitalSystem;

import lombok.Getter;

@Getter
public class Patient {
    private final int patientId;
    private final Speciality speciality;
    private final long arrivalTime;

    public Patient(int patientId, Speciality speciality) {
        this.patientId = patientId;
        this.speciality = speciality;
        this.arrivalTime = System.currentTimeMillis();
    }
}
