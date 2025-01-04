package com.tttsaurus.ingameinfo.common.api.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Map;

public final class AnnotationUtils
{
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A createAnnotation(Class<A> annotationClass, Map<String, Object> values)
    {
        return (A)Proxy.newProxyInstance(
                annotationClass.getClassLoader(),
                new Class[]{ annotationClass },
                (proxy, method, args) ->
                {
                    if (values.containsKey(method.getName()))
                        return values.get(method.getName());
                    else if (method.getDefaultValue() != null)
                        return method.getDefaultValue();
                    return null;
                }
        );
    }
}
