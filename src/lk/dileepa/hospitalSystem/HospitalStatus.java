package lk.dileepa.hospitalSystem;

import java.util.concurrent.atomic.AtomicInteger;

public class HospitalStatus {

    private final AtomicInteger arrived = new AtomicInteger();
    private final AtomicInteger treated = new AtomicInteger();

    public void recordArrival() {
        arrived.incrementAndGet();
    }

    public void recordTreated() {
        treated.incrementAndGet();
    }

    public int getArrived() {
        return arrived.get();
    }

    public int getTreated() {
        return treated.get();
    }

    public int getLost() {
        return arrived.get() - treated.get();
    }

    public void printSummary() {
        System.out.println("\n===== SIMULATION SUMMARY =====");
        System.out.println("Total patients arrived : " + getArrived());
        System.out.println("Total patients treated : " + getTreated());
        System.out.println("Total patients lost    : " + getLost());
        System.out.println("================================");
    }
}