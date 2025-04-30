package unittesting;

import java.lang.reflect.*;
import java.util.*;

public class RunTests {
    public static void main(String[] args) {
        // Check if a class name is provided as an argument
        if (args.length == 0) {
            System.out.println("Usage: java unittesting.RunTests <ClassName>");
            return;
        }

        String className = args[0];

        try {
            // Load the class using reflection
            Class<?> clazz = Class.forName(className);

            // Create an instance of the class
            Object instance = clazz.getDeclaredConstructor().newInstance();

            // Get all declared methods of the class
            Method[] methods = clazz.getDeclaredMethods();

            // Iterate through all methods
            for (Method method : methods) {
                // Check if the method is annotated with @Testable and is non-private
                if (method.isAnnotationPresent(Testable.class) && !Modifier.isPrivate(method.getModifiers())) {
                    // Get the @Specification annotation
                    Specification spec = method.getAnnotation(Specification.class);

                    // Validate arguments and invoke the method
                    TEST_RESULT result = validateAndInvoke(method, spec, instance);

                    // Report the result using the Report class
                    Report.report(result, method.getName(), spec);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates the method's arguments, converts them to the correct types,
     * invokes the method, and checks the result.
     */
    private static TEST_RESULT validateAndInvoke(Method method, Specification spec, Object instance) {
        try {
            // Extract argument types and values from the @Specification annotation
            String[] argTypes = spec.argTypes();
            String[] argValues = spec.argValues();

            // Check if the number of arguments matches the method's parameter count
            if (argTypes.length != argValues.length || argTypes.length != method.getParameterCount()) {
                return TEST_RESULT.WrongArgs;
            }

            // Convert arguments to their respective types
            Object[] convertedArgs = new Object[argTypes.length];
            for (int i = 0; i < argTypes.length; i++) {
                Class<?> paramType = method.getParameterTypes()[i];
                convertedArgs[i] = convertArgument(argTypes[i], argValues[i], paramType);
                if (convertedArgs[i] == null) {
                    return TEST_RESULT.WrongArgs;
                }
            }

            // Check if the expected return type matches the method's return type
            Class<?> returnType = method.getReturnType();
            String expectedResType = spec.resType();
            if (!expectedResType.isEmpty() && !returnType.getSimpleName().equalsIgnoreCase(expectedResType)) {
                return TEST_RESULT.WrongResultType;
            }

            // Invoke the method with the converted arguments
            Object result = method.invoke(instance, convertedArgs);

            // If the method is void, check that no result is expected
            if (expectedResType.isEmpty()) {
                if (!returnType.equals(void.class)) {
                    return TEST_RESULT.WrongResultType;
                }
                return TEST_RESULT.TestSucceeded;
            }

            // Convert the expected result value to the correct type
            Object expectedValue = convertArgument(expectedResType, spec.resVal(), returnType);
            if (expectedValue == null || !result.equals(expectedValue)) {
                return TEST_RESULT.TestFailed;
            }

            // If everything matches, the test succeeds
            return TEST_RESULT.TestSucceeded;
        } catch (IllegalAccessException | InvocationTargetException e) {
            return TEST_RESULT.WrongArgs;
        }
    }

    /**
     * Converts a string argument to the specified type.
     */
    private static Object convertArgument(String argType, String argValue, Class<?> targetType) {
        try {
            switch (argType.toLowerCase()) {
                case "int":
                    return Integer.parseInt(argValue);
                case "double":
                    return Double.parseDouble(argValue);
                case "bool":
                    return Boolean.parseBoolean(argValue);
                case "string":
                    return argValue;
                default:
                    return null; // Unsupported type
            }
        } catch (NumberFormatException e) {
            return null; // Invalid conversion
        }
    }
}