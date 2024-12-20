package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Reactive
{
    String targetUid() default "";
    String property() default "";
    boolean passiveSync() default false;
    boolean initiativeSync() default false;
}
