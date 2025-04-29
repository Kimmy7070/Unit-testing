package unittesting;

public class Report {
    public enum TEST_RESULT { WrongArgs, WrongResultType, TestFailed, TestSucceeded }

    public static void report(TEST_RESULT result, String methodName, Specification spec) {
        // Provided implementation (do not modify)
        System.out.println("Test: " + methodName + " | Result: " + result);
    }
}
