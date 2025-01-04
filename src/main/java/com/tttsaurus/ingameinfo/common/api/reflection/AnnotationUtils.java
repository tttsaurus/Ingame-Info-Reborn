package com.tttsaurus.ingameinfo.common.api.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;

public final class AnnotationUtils
{
    private static <A extends Annotation> int computeHashCode(Class<A> annotationClass, Map<String, Object> values)
    {
        int result = 0;
        for (Method method : annotationClass.getDeclaredMethods())
        {
            Object value = values.getOrDefault(method.getName(), method.getDefaultValue());
            if (value == null)
                throw new IllegalStateException("No value provided for " + method.getName());
            result += (127 * method.getName().hashCode()) ^ value.hashCode();
        }
        return result;
    }

    private static boolean proxyEquals(Object proxy, Object other)
    {
        if (proxy == other) return true;
        if (!Proxy.isProxyClass(other.getClass())) return false;
        return Objects.equals(proxy.hashCode(), other.hashCode());
    }

    private static <A extends Annotation> String proxyToString(Class<A> annotationClass, Map<String, Object> values)
    {
        StringBuilder sb = new StringBuilder("@").append(annotationClass.getName()).append("(");
        values.forEach((key, value) -> sb.append(key).append("=").append(value).append(", "));
        if (!values.isEmpty()) sb.setLength(sb.length() - 2);
        sb.append(")");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A createAnnotation(Class<A> annotationClass, Map<String, Object> values)
    {
        return (A)Proxy.newProxyInstance(
                annotationClass.getClassLoader(),
                new Class[]{ annotationClass },
                (proxy, method, args) ->
                {
                    String methodName = method.getName();

                    switch (methodName)
                    {
                        case "hashCode" -> { return computeHashCode(annotationClass, values); }
                        case "equals" -> { return proxyEquals(proxy, args[0]); }
                        case "toString" -> { return proxyToString(annotationClass, values); }
                    }

                    if (values.containsKey(methodName))
                        return values.get(methodName);
                    else if (method.getDefaultValue() != null)
                        return method.getDefaultValue();

                    return null;
                }
        );
    }
}
