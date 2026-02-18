package com.tttsaurus.ingameinfo.common.core.gui.event;

public interface UIEventListener<T extends UIEvent>
{
    void handle(T event);
    UIEventListenerType type();
}
