package com.tttsaurus.ingameinfo.common.core.gui.event;

public interface IUIEventListener<T extends UIEvent>
{
    void handle(T event);
}
