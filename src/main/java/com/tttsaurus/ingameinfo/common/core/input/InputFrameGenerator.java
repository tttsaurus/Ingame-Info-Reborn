package com.tttsaurus.ingameinfo.common.core.input;

public class InputFrameGenerator
{
    private final IKeyboard keyboard;
    private final IMouse mouse;

    public InputFrameGenerator(IKeyboard keyboard, IMouse mouse)
    {
        this.keyboard = keyboard;
        this.mouse = mouse;
    }

    public InputState generate()
    {
        keyboard.updateInput();
        mouse.updateInput();

        return new InputState(
                keyboard.getHeldKeys(),
                keyboard.getPressedKeys(),
                keyboard.getLeftKeys(),
                mouse.isLeftDown(),
                mouse.isLeftPress(),
                mouse.isLeftLeave(),
                mouse.isRightDown(),
                mouse.isRightPress(),
                mouse.isRightLeave(),
                mouse.getPosX(),
                mouse.getPosY(),
                mouse.getDeltaPosX(),
                mouse.getDeltaPosY());
    }
}
