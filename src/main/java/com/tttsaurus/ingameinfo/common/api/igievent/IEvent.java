package com.tttsaurus.ingameinfo.common.api.igievent;

public interface IEvent<T>
{
    void addListener(T listener);
}
