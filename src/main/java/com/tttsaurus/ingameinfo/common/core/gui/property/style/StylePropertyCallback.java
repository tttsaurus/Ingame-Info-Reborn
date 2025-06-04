package com.tttsaurus.ingameinfo.common.core.gui.property.style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with {@link StylePropertyCallback} must be <code>public</code>.
 * Callbacks related to setting style properties must be annotated with {@link StylePropertyCallback}.
 * The method name of the annotated method is also important.
 *
 * @see StyleProperty
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StylePropertyCallback { }
