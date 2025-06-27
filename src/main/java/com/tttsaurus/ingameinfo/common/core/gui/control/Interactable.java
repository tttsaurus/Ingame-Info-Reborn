package com.tttsaurus.ingameinfo.common.core.gui.control;

import com.tttsaurus.ingameinfo.common.core.gui.event.UIEvent;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.input.InputState;

@RegisterElement(constructable = false)
public abstract class Interactable extends Sized
{
    protected boolean hover = false;
    protected boolean hold = false;

    @Override
    public void onPropagateInput(InputState inputState)
    {
        super.onPropagateInput(inputState);

        if (rect.contains(inputState.posX, inputState.posY))
        {
            inputState.consume();
            if (!hover)
            {
                hover = true;
                fireEvent(new UIEvent.MouseEnter(inputState));
            }
            if (inputState.leftDown)
            {
                if (!hold)
                {
                    hold = true;
                    fireEvent(new UIEvent.MousePress(inputState));
                }
            }
            else if (hold)
            {
                hover = false;
                hold = false;
                fireEvent(new UIEvent.MouseRelease(inputState));
            }
        }
        else if (hover)
        {
            hover = false;
            hold = false;
            fireEvent(new UIEvent.MouseLeave(inputState));
        }
    }
}
