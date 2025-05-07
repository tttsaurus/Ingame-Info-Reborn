package com.tttsaurus.ingameinfo.common.core.gui.style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StyleProperty
{
    String name() default "";
    String setterCallbackPre() default "";
    String setterCallbackPost() default "";
}
