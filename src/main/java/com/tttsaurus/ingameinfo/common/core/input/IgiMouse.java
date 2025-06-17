package com.tttsaurus.ingameinfo.common.core.input;

import com.tttsaurus.ingameinfo.common.core.commonutils.MouseUtils;

public class IgiMouse implements IMouse
{
    public static final IgiMouse INSTANCE = new IgiMouse();

    private boolean leftDown;
    private boolean leftPress;
    private boolean leftLeave;
    private boolean rightDown;
    private boolean rightPress;
    private boolean rightLeave;
    private float posX;
    private float posY;
    private float deltaPosX;
    private float deltaPosY;

    public void updateInput()
    {
        boolean ld = MouseUtils.isMouseDownLeft();
        boolean rd = MouseUtils.isMouseDownRight();
        leftPress = !leftDown && ld;
        rightPress = !rightDown && rd;
        leftLeave = leftDown && !ld;
        rightLeave = rightDown && !rd;
        leftDown = ld;
        rightDown = rd;
        float px = MouseUtils.getMouseX();
        float py = MouseUtils.getMouseY();
        deltaPosX = px - posX;
        deltaPosY = py - posY;
        posX = px;
        posY = py;
    }

    @Override
    public boolean isLeftDown() { return leftDown; }

    @Override
    public boolean isLeftPress() { return leftPress; }

    @Override
    public boolean isLeftLeave() { return leftLeave; }

    @Override
    public boolean isRightDown() { return rightDown; }

    @Override
    public boolean isRightPress() { return rightPress; }

    @Override
    public boolean isRightLeave() { return rightLeave; }

    @Override
    public float getPosX() { return posX; }

    @Override
    public float getPosY() { return posY; }

    @Override
    public float getDeltaPosX() { return deltaPosX; }

    @Override
    public float getDeltaPosY() { return deltaPosY; }
}
