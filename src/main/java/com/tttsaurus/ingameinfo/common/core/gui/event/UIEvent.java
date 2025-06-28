package com.tttsaurus.ingameinfo.common.core.gui.event;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.ElementAccessor;
import com.tttsaurus.ingameinfo.common.core.input.InputState;

public class UIEvent
{
    public final InputState input;
    public final ElementAccessor elementAccessor;

    private boolean consumed = false;
    public boolean isConsumed() { return consumed; }
    public void consume() { consumed = true; }

    public UIEvent(InputState input, Element element)
    {
        this.input = input;
        elementAccessor = new ElementAccessor(element);
    }

    public static class MouseEnter extends UIEvent { public MouseEnter(InputState input, Element element) { super(input, element); } }
    public static class MouseLeave extends UIEvent { public MouseLeave(InputState input, Element element) { super(input, element); } }
    public static class MousePress extends UIEvent { public MousePress(InputState input, Element element) { super(input, element); } }
    public static class MouseRelease extends UIEvent { public MouseRelease(InputState input, Element element) { super(input, element); } }
}
