package lk.dileepa.hospitalSystem;

public enum Shift {
    DAY,
    NIGHT;

    // Method to toggle shift
    public Shift next() {
        return this == DAY ? NIGHT : DAY;
    }
}