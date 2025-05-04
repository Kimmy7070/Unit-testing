package unittesting;

public class MathOpsTests {

    // --- should succeed
    @Testable
    @Specification(argTypes  = {"int","int"}, argValues = {"7","8"}, resType = "int",    resVal = "15")
    public int addOK(int a, int b) {
        return a + b;
    }

    // --- should fail (wrong result)
    @Testable
    @Specification(argTypes  = {"int","int"}, argValues = {"4","5"}, resType = "int",    resVal = "100")
    public int addWrong(int a, int b) {
        return a + b;  // 4+5 = 9 != 100 → TestFailed
    }

    // --- wrong result type
    @Testable
    @Specification(argTypes  = {"int","int"}, argValues = {"10","2"}, resType = "double", resVal = "5.0")
    public int divideInt(int a, int b) {
        return a / b;  // return type is int, but spec expects double → WrongResultType
    }

    // --- wrong args
    @Testable
    @Specification(argTypes  = {"int","int","int"}, argValues = {"1","2","3"}, resType = "int", resVal = "6")
    public int sumThree(int a, int b) {
        return a + b;  // spec has 3 args but method only takes 2 → WrongArgs
    }
}
