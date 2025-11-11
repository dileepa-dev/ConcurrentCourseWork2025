package lk.dileepa.submissionSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UOWSubmissionSystem {
    private static final int STUDENT_COUNT = 10;
    private static final int MAX_THREADS = 3;

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║   UNIVERSITY OF WESTMINSTER SUBMISSION SYSTEM          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        SubmissionConfiguration stats = new SubmissionConfiguration();

        System.out.println("Submission Starts...\n");
        stats.startTimer();

        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        for (int i = 1; i <= STUDENT_COUNT; i++) {
            final String studentId = "IIT-"+i;

            executor.submit(() -> {
                try {
                    // Simulate processing delay (10–100 ms)
                    Thread.sleep(50);

                    // Success case
                    stats.recordSuccess();
                    System.out.println("Student " + studentId + " submission: SUCCESS ✅");
                } catch (Exception e) {
                    stats.recordFailure();
                    System.out.println("Student " + studentId + " submission: FAILED ❌ (" + e.getMessage() + ")");
                }
            });
        }

        // Gracefully shut down the executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        stats.endTimer();
        System.out.println("\nSubmission Ends...");
        stats.displayResults();
    }
}