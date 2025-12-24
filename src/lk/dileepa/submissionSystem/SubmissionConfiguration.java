package lk.dileepa.submissionSystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@AllArgsConstructor
public class SubmissionConfiguration {
    private final AtomicInteger totalStudents = new AtomicInteger(0);
    private final AtomicInteger successfulSubmissions = new AtomicInteger(0);
    private final AtomicInteger failedSubmissions = new AtomicInteger(0);

    private final AtomicLong startTime = new AtomicLong(0);
    private final AtomicLong endTime = new AtomicLong(0);

    public void startTimer() {
        startTime.set(System.currentTimeMillis());
    }

    public void endTimer() {
        endTime.set(System.currentTimeMillis());
    }

    public void recordSuccess() {
        totalStudents.incrementAndGet();
        successfulSubmissions.incrementAndGet();
    }

    public void recordFailure() {
        totalStudents.incrementAndGet();
        failedSubmissions.incrementAndGet();
    }

    public double getTotalTime() {
        return (endTime.get() - startTime.get()) / 1000.0;
    }

    public double getSuccessRate() {
        if (totalStudents.get() == 0) return 0.0;
        return (successfulSubmissions.get() * 100.0) / totalStudents.get();
    }

    public void displayResults() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("UOW SUBMISSION SYSTEM REPORT");
        System.out.println("=".repeat(35));
        System.out.println("Total time taken      : " + getTotalTime() + " s");
        System.out.println("Total submissions     : " + totalStudents.get());
        System.out.println("Successful submissions: " + successfulSubmissions.get());
        System.out.println("Failed submissions    : " + failedSubmissions.get());
        System.out.printf("Success rate          : %.2f%%\n", getSuccessRate());
        System.out.println("=".repeat(35) + "\n");
    }
}