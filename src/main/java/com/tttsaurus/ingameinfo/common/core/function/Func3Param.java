package com.tttsaurus.ingameinfo.common.core.function;

/**
 * An interface of <code>TReturn invoke(T0 arg0, T1 arg1, T2 arg2)</code>.
 */
@FunctionalInterface
public interface Func3Param<TReturn, T0, T1, T2>
{
    TReturn invoke(T0 arg0, T1 arg1, T2 arg2);
}
