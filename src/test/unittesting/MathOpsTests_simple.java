package unittesting;

public class MathOpsTests_simple {

    @Testable
    @Specification(
        argTypes  = {"int", "int"},
        argValues = {"2", "3"},
        resType   = "int",
        resVal    = "5"
    )
    public int add(int a, int b) {
        return a + b;
    }

    @Testable
    @Specification(
        argTypes  = {"string","string"},
        argValues = {"Hello","World"},
        resType   = "string",
        resVal    = "HelloWorld"
    )
    public String concat(String s1, String s2) {
        return s1 + s2;
    }

    @Testable
    @Specification(
        argTypes  = {},
        argValues = {},
        resType   = "",
        resVal    = ""
    )
    public void doNothing() {
        // no-op; just testing void return
    }
}
