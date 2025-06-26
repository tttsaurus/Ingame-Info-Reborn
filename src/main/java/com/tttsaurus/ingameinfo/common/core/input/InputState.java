package com.tttsaurus.ingameinfo.common.core.input;

import java.util.Objects;
import java.util.Set;

public class InputState
{
    public final Set<Integer> heldKeys;
    public final Set<Integer> pressedKeys;
    public final Set<Integer> leftKeys;
    public final boolean leftDown;
    public final boolean leftPress;
    public final boolean leftLeave;
    public final boolean rightDown;
    public final boolean rightPress;
    public final boolean rightLeave;
    public final float posX;
    public final float posY;
    public final float deltaPosX;
    public final float deltaPosY;

    private boolean consumed = false;
    public boolean isConsumed() { return consumed; }
    public void consume() { consumed = true; }

    public InputState(
            Set<Integer> heldKeys,
            Set<Integer> pressedKeys,
            Set<Integer> leftKeys,
            boolean leftDown,
            boolean leftPress,
            boolean leftLeave,
            boolean rightDown,
            boolean rightPress,
            boolean rightLeave,
            float posX,
            float posY,
            float deltaPosX,
            float deltaPosY)
    {
        this.heldKeys = heldKeys;
        this.pressedKeys = pressedKeys;
        this.leftKeys = leftKeys;
        this.leftDown = leftDown;
        this.leftPress = leftPress;
        this.leftLeave = leftLeave;
        this.rightDown = rightDown;
        this.rightPress = rightPress;
        this.rightLeave = rightLeave;
        this.posX = posX;
        this.posY = posY;
        this.deltaPosX = deltaPosX;
        this.deltaPosY = deltaPosY;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof InputState state)) return false;
        return leftDown == state.leftDown &&
                leftPress == state.leftPress &&
                leftLeave == state.leftLeave &&
                rightDown == state.rightDown &&
                rightPress == state.rightPress &&
                rightLeave == state.rightLeave &&
                Float.compare(posX, state.posX) == 0 &&
                Float.compare(posY, state.posY) == 0 &&
                Float.compare(deltaPosX, state.deltaPosX) == 0 &&
                Float.compare(deltaPosY, state.deltaPosY) == 0 &&
                Objects.equals(heldKeys, state.heldKeys) &&
                Objects.equals(pressedKeys, state.pressedKeys) &&
                Objects.equals(leftKeys, state.leftKeys);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(heldKeys, pressedKeys, leftKeys, leftDown, leftPress, leftLeave, rightDown, rightPress, rightLeave, posX, posY, deltaPosX, deltaPosY);
    }
}
