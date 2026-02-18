package com.tttsaurus.ingameinfo.common.core.igievent;

public interface Event<T>
{
    void addListener(T listener);
}
