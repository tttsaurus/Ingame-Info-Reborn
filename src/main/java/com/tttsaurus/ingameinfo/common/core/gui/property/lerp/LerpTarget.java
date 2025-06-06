package com.tttsaurus.ingameinfo.common.core.gui.property.lerp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LerpTarget
{
    // field name
    String value() default "";
    String inner0() default "";
    String inner1() default "";
}
