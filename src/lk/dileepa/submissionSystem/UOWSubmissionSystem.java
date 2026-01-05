package lk.dileepa.submissionSystem;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main class that simulates the University of Westminster
 * concurrent submission system.
 */
public class UOWSubmissionSystem {

    // Number of students submitting
    private static final int STUDENT_COUNT = 1000;

    // Thread pool size based on available CPU cores
    private static final int MAX_THREADS =
            Runtime.getRuntime().availableProcessors() * 4;

    public static void main(String[] args) {

        // System banner
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║   UNIVERSITY OF WESTMINSTER SUBMISSION SYSTEM          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        // Shared thread-safe statistics object
        SubmissionConfiguration stats = new SubmissionConfiguration();

        System.out.println("Submission Starts...\n");
        stats.startTimer();

        // Fixed-size thread pool for concurrent processing
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        Random random = new Random();

        // Submit tasks for each student
        for (int i = 1; i <= STUDENT_COUNT; i++) {
            final String studentId = "IIT-" + i;

            executor.submit(() -> {
                try {
                    // Simulated processing delay
                    Thread.sleep(50);

                    // Random failure simulation (5% chance)
                    int failRandom = random.nextInt(100);
                    if (failRandom < 5) {
                        stats.recordFailure();
                        System.out.println("Student " + studentId + " submission: FAILED  ❌");
                    } else {
                        stats.recordSuccess();
                        System.out.println("Student " + studentId + " submission: SUCCESS ✅");
                    }

                } catch (Exception e) {
                    stats.recordFailure();
                    System.out.println("Student " + studentId +
                            " submission: FAILED ❌ (" + e.getMessage() + ")");
                }
            });
        }

        // Gracefully shutdown executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Stop timer and display results
        stats.endTimer();
        System.out.println("\nSubmission Ends...");
        stats.displayResults();
    }
}