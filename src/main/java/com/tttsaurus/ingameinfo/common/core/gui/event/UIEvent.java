package com.tttsaurus.ingameinfo.common.core.gui.event;

import com.tttsaurus.ingameinfo.common.core.input.InputState;

public class UIEvent
{
    public final InputState input;

    private boolean consumed = false;
    public boolean isConsumed() { return consumed; }
    public void consume() { consumed = true; }

    public UIEvent(InputState input)
    {
        this.input = input;
    }

    public static class MouseEnter extends UIEvent { public MouseEnter(InputState input) { super(input); } }
    public static class MouseLeave extends UIEvent { public MouseLeave(InputState input) { super(input); } }
    public static class MousePress extends UIEvent { public MousePress(InputState input) { super(input); } }
    public static class MouseRelease extends UIEvent { public MouseRelease(InputState input) { super(input); } }
    public static class MouseClick extends UIEvent { public MouseClick(InputState input) { super(input); } }
}
