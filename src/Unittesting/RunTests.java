package unittesting;

import java.lang.reflect.*;

public class RunTests {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java unittesting.RunTests <FullyQualifiedClassName>");
            return;
        }

        String className = args[0];
        try {
            // Load the target class
            Class<?> clazz = Class.forName(className);
            Object   instance = clazz.getDeclaredConstructor().newInstance();

            // Inspect and run all @Testable methods
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Testable.class)
                 && !Modifier.isPrivate(method.getModifiers())) {

                    Specification spec = method.getAnnotation(Specification.class);
                    Report.TEST_RESULT result =
                        validateAndInvoke(method, spec, instance);

                    Report.report(result, method.getName(), spec);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Report.TEST_RESULT validateAndInvoke(
            Method method, Specification spec, Object instance) {

        // 1) Check argument counts
        String[] argTypes  = spec.argTypes();
        String[] argValues = spec.argValues();
        if (argTypes.length != argValues.length
         || argTypes.length != method.getParameterCount()) {
            return Report.TEST_RESULT.WrongArgs;
        }

        // 2) Convert arguments
        Object[] convertedArgs = new Object[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            convertedArgs[i] = convertArgument(
                argTypes[i], argValues[i], method.getParameterTypes()[i]
            );
            if (convertedArgs[i] == null) {
                return Report.TEST_RESULT.WrongArgs;
            }
        }

        // 3) Check expected return type
        Class<?> returnType      = method.getReturnType();
        String   expectedResType = spec.resType().trim();
        if (!expectedResType.isEmpty()) {
            if (!returnType.getSimpleName()
                    .equalsIgnoreCase(expectedResType)) {
                return Report.TEST_RESULT.WrongResultType;
            }
        } else {
            if (!returnType.equals(void.class)) {
                return Report.TEST_RESULT.WrongResultType;
            }
        }

        // 4) Invoke the method
        Object resultValue;
        try {
            resultValue = method.invoke(instance, convertedArgs);
        } catch (Exception e) {
            return Report.TEST_RESULT.WrongArgs;
        }

        // 5) If void, it's a success
        if (expectedResType.isEmpty()) {
            return Report.TEST_RESULT.TestSucceeded;
        }

        // 6) Convert expected return value
        Object expectedValue = convertArgument(
            expectedResType, spec.resVal(), returnType
        );
        if (expectedValue == null) {
            return Report.TEST_RESULT.WrongResultType;
        }

        // 7) Compare actual vs expected
        if (!expectedValue.equals(resultValue)) {
            return Report.TEST_RESULT.TestFailed;
        }
        return Report.TEST_RESULT.TestSucceeded;
    }

    private static Object convertArgument(
            String argType, String argValue, Class<?> targetType) {
        try {
            switch (argType.toLowerCase()) {
                case "int":    return Integer.parseInt(argValue);
                case "double": return Double.parseDouble(argValue);
                case "bool":   return Boolean.parseBoolean(argValue);
                case "string": return argValue;
                default:       return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}