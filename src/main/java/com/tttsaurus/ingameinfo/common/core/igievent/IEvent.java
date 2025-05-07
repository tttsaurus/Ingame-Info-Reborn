package com.tttsaurus.ingameinfo.common.core.igievent;

public interface IEvent<T>
{
    void addListener(T listener);
}
