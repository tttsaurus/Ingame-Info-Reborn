package com.tttsaurus.ingameinfo.common.core.function;

/**
 * An interface of <code>void invoke(T0 arg0, T1 arg1)</code>.
 */
@FunctionalInterface
public interface Action2Param<T0, T1>
{
    void invoke(T0 arg0, T1 arg1);
}
