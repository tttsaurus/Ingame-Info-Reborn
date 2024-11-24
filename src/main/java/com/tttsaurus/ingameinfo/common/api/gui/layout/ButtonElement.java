package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.*;
import com.tttsaurus.ingameinfo.common.api.input.MouseUtils;
import java.util.ArrayList;
import java.util.List;

public abstract class ButtonElement extends SizedElement
{
    protected boolean hover = false;
    protected boolean hold = false;

    private final List<IMouseEnterButton> enter = new ArrayList<>();
    private final List<IMouseLeaveButton> leave = new ArrayList<>();
    private final List<IMousePressButton> press = new ArrayList<>();
    private final List<IMouseReleaseButton> release = new ArrayList<>();
    private final List<IMouseClickButton> click = new ArrayList<>();

    public ButtonElement addListener(IMouseEnterButton action) { enter.add(action); return this; }
    public ButtonElement addListener(IMouseLeaveButton action) { leave.add(action); return this; }
    public ButtonElement addListener(IMousePressButton action) { press.add(action); return this; }
    public ButtonElement addListener(IMouseReleaseButton action) { release.add(action); return this; }
    public ButtonElement addListener(IMouseClickButton action) { click.add(action); return this; }

    public ButtonElement(float width, float height)
    {
        super(width, height);
    }

    @Override
    protected void onFixedUpdate(double deltaTime)
    {

    }
    @Override
    protected void onRenderUpdate()
    {
        int x = MouseUtils.getMouseX();
        int y = MouseUtils.getMouseY();

        if (x >= rect.x && x <= (rect.x + rect.width) && y >= rect.y && y <= (rect.y + rect.height))
        {
            if (!hover)
            {
                hover = true;
                enter.forEach(IMouseEnterButton::enter);
            }
            if (MouseUtils.isMouseDownLeft())
            {
                if (!hold)
                {
                    hold = true;
                    press.forEach(IMousePressButton::press);
                }
            }
            else if (hold)
            {
                hover = false;
                hold = false;
                release.forEach(IMouseReleaseButton::release);
                click.forEach(IMouseClickButton::click);
            }
        }
        else if (hover)
        {
            hover = false;
            hold = false;
            leave.forEach(IMouseLeaveButton::leave);
        }
    }
}
