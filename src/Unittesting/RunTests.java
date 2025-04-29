package unittesting;

import java.lang.reflect.*;
                if (convertedArgs[i] == null) {
                    return TEST_RESULT.WrongArgs;
                }
            }

            // Check return type
            Class<?> returnType = method.getReturnType();
            String expectedResType = spec.resType();
            if (!expectedResType.isEmpty() && !returnType.getSimpleName().equalsIgnoreCase(expectedResType)) {
                return TEST_RESULT.WrongResultType;
            }

            // Invoke method
            Object result = method.invoke(instance, convertedArgs);

            // Check result value
            if (expectedResType.isEmpty()) { // Void method
                return TEST_RESULT.TestSucceeded;
            } else {
                Object expectedValue = convertArgument(expectedResType, spec.resVal(), returnType);
                if (expectedValue == null || !result.equals(expectedValue)) {
                    return TEST_RESULT.TestFailed;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            return TEST_RESULT.WrongArgs;
        }
        return TEST_RESULT.TestSucceeded;
    }

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
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}