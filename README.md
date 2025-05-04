# Unit Testing Framework (Assignment 1 - Problem 2)

A lightweight Java reflection-based test runner for `@Testable` methods annotated with `@Specification`. This utility dynamically loads classes, reads method-level specs, invokes methods, and reports results (WrongArgs, WrongResultType, TestFailed, TestSucceeded).

---

## 📂 Project Structure

```
Unit-testing/
├─ src/
│  ├─ unittesting/    ← framework code (annotations + runner)
│  │   ├─ Report.java
│  │   ├─ RunTests.java
│  │   ├─ Specification.java
│  │   └─ Testable.java
│  └─ test/            ← your test-case classes (same package)
│       ├─ MathOpsTests_simple.java
│       └─ MathOpsTests.java
```

- **src/unittesting/**: core test-runner and annotation definitions.  
- **src/test/**: your `@Testable` classes annotated under package `unittesting`.

---

## 🛠 Prerequisites

- **Java SE 8** or higher
- **Command-line** access or IDE (e.g., IntelliJ, Eclipse)

---

## ⚙️ Build & Run

1. **Clone the repo**
   ```sh
   git clone https://github.com/Kimmy7070/Unit-testing.git
   cd Unit-testing
   ```

2. **Compile** both framework and tests:
   ```sh
   mkdir -p bin
   javac -d bin src/unittesting/*.java src/test/*.java
   ```

3. **Run** the test runner on any test class:
   ```sh
   # Replace MathOpsTests_simple with your class name (without .java)
   java -cp bin unittesting.RunTests MathOpsTests_simple
   ```

---

## 📝 Writing Your Own Test Cases

1. **Create** a new Java file in `testclasses/unittesting/`, package `unittesting`.
2. **Annotate** methods you want to test with both `@Testable` and `@Specification`:
   ```java
   package unittesting;

   public class MyTests {

       @Testable
       @Specification(
           argTypes  = {"int","int"},
           argValues = {"3","4"},
           resType   = "int",
           resVal    = "7"
       )
       public int add(int a, int b) {
           return a + b;
       }

       // add more @Testable methods...
   }
   ```
3. **Re-compile** and **re-run** the framework:
   ```sh
   javac -d bin testclasses/unittesting/MyTests.java
   java -cp bin unittesting.RunTests MyTests
   ```

---

## 📰 Report Output

For each `@Testable` method, you’ll see a line:
```
Test: <methodName> | Result: <TEST_RESULT>
```
Where `<TEST_RESULT>` is one of:
- `WrongArgs` — argument count/type mismatch  
- `WrongResultType` — return type mismatch  
- `TestFailed` — returned value ≠ expected  
- `TestSucceeded` — all checks passed  

---

## 🤝 Contributing

Feel free to expand:
- Support richer types (floats, longs)  
- Enhanced arg parsing (arrays, objects)  
- Timeout handling for long-running tests  

Fork, push, and open a pull request!
