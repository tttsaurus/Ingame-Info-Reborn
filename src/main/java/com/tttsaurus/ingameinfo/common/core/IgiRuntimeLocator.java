package com.tttsaurus.ingameinfo.common.core;

public final class IgiRuntimeLocator
{
    private IgiRuntimeLocator() { }

    public static IgiRuntime get()
    {
        return InternalMethods.IgiRuntime$instance$getter();
    }
}
