package com.tttsaurus.ingameinfo.common.core.input;

public interface Mouse
{
    void updateInput();
    boolean isLeftDown();
    boolean isLeftPress();
    boolean isLeftLeave();
    boolean isRightDown();
    boolean isRightPress();
    boolean isRightLeave();
    float getPosX();
    float getPosY();
    float getDeltaPosX();
    float getDeltaPosY();
}
