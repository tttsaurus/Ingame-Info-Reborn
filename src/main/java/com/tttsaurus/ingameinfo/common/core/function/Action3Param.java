package com.tttsaurus.ingameinfo.common.core.function;

/**
 * An interface of <code>void invoke(T0 arg0, T1 arg1, T2 arg2)</code>.
 */
@FunctionalInterface
public interface Action3Param<T0, T1, T2>
{
    void invoke(T0 arg0, T1 arg1, T2 arg2);
}
