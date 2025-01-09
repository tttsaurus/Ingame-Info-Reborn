package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.*;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.CallbackInfo;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.input.MouseUtils;
import java.util.ArrayList;
import java.util.List;

@RegisterElement(constructable = false)
public abstract class AbstractButton extends Sized
{
    private boolean hover = false;
    private boolean hold = false;

    @StylePropertyCallback
    public void addEnterListenerHelper(IMouseEnterButton value, CallbackInfo callbackInfo)
    {
        callbackInfo.cancel = true;
        addListener(value);
    }
    @StyleProperty(setterCallbackPre = "addEnterListenerHelper")
    public IMouseEnterButton addEnterListener;

    @StylePropertyCallback
    public void addLeaveListenerHelper(IMouseLeaveButton value, CallbackInfo callbackInfo)
    {
        callbackInfo.cancel = true;
        addListener(value);
    }
    @StyleProperty(setterCallbackPre = "addLeaveListenerHelper")
    public IMouseLeaveButton addLeaveListener;

    @StylePropertyCallback
    public void addPressListenerHelper(IMousePressButton value, CallbackInfo callbackInfo)
    {
        callbackInfo.cancel = true;
        addListener(value);
    }
    @StyleProperty(setterCallbackPre = "addPressListenerHelper")
    public IMousePressButton addPressListener;

    @StylePropertyCallback
    public void addReleaseListenerHelper(IMouseReleaseButton value, CallbackInfo callbackInfo)
    {
        callbackInfo.cancel = true;
        addListener(value);
    }
    @StyleProperty(setterCallbackPre = "addReleaseListenerHelper")
    public IMouseReleaseButton addReleaseListener;

    @StylePropertyCallback
    public void addClickListenerHelper(IMouseClickButton value, CallbackInfo callbackInfo)
    {
        callbackInfo.cancel = true;
        addListener(value);
    }
    @StyleProperty(setterCallbackPre = "addClickListenerHelper")
    public IMouseClickButton addClickListener;

    private final List<IMouseEnterButton> enter = new ArrayList<>();
    private final List<IMouseLeaveButton> leave = new ArrayList<>();
    private final List<IMousePressButton> press = new ArrayList<>();
    private final List<IMouseReleaseButton> release = new ArrayList<>();
    private final List<IMouseClickButton> click = new ArrayList<>();

    public AbstractButton addListener(IMouseEnterButton action) { enter.add(action); return this; }
    public AbstractButton addListener(IMouseLeaveButton action) { leave.add(action); return this; }
    public AbstractButton addListener(IMousePressButton action) { press.add(action); return this; }
    public AbstractButton addListener(IMouseReleaseButton action) { release.add(action); return this; }
    public AbstractButton addListener(IMouseClickButton action) { click.add(action); return this; }

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        if (!focused) return;

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
