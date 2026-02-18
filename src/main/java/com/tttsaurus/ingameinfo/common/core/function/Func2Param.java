package com.tttsaurus.ingameinfo.common.core.function;

/**
 * An interface of <code>TReturn invoke(T0 arg0, T1 arg1)</code>.
 */
@FunctionalInterface
public interface Func2Param<TReturn, T0, T1>
{
    TReturn invoke(T0 arg0, T1 arg1);
}
