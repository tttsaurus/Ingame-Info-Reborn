package com.tttsaurus.ingameinfo.common.core.gui.property.style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields annotated with {@link StyleProperty} must be <code>public</code>.
 * All annotated style properties will be collected during mod initialization.
 *
 * @see StylePropertyCallback
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StyleProperty
{
    /**
     * Fill the style property name here.
     * The actual field name will be used as the name for this style property if it returns an empty string.
     *
     * @return The name of this style property.
     */
    String name() default "";

    /**
     * Fill the style property setter's pre callback method name here.
     * The pre callback method signature can be
     * <pre><code>
     *     // where T is the type of that style property
     *     public void funcName(T arg0) { }
     * </code></pre>
     * or
     * <pre><code>
     *     // where T is the type of that style property
     *     public void funcName(T arg0, CallbackInfo arg1) { }
     * </code></pre>
     * You can abort the setting process, which is denying a value to be set, by using {@link CallbackInfo},
     * and this is why pre callback is special. You can use pre callback to do value validation.
     *
     * @see StylePropertyCallback
     * @see CallbackInfo
     * @return The method name of pre callback.
     */
    String setterCallbackPre() default "";

    /**
     * Fill the style property setter's post callback method name here.
     * The post callback method signature must be
     * <pre><code>
     *     public void funcName() { }
     * </code></pre>
     * Post callbacks happen when the value is already being set.
     *
     * @see StylePropertyCallback
     * @return The method name of post callback.
     */
    String setterCallbackPost() default "";
}
