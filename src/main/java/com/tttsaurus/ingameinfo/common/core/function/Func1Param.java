package com.tttsaurus.ingameinfo.common.core.function;

/**
 * An interface of <code>TReturn invoke(T0 arg0)</code>.
 */
@FunctionalInterface
public interface Func1Param<TReturn, T0>
{
    TReturn invoke(T0 arg0);
}
