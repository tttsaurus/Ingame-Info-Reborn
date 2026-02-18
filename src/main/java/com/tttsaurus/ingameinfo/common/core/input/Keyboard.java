package com.tttsaurus.ingameinfo.common.core.input;

import java.util.Set;

public interface Keyboard
{
    void updateInput();
    boolean keyPress(Key key);
    boolean keyLeave(Key key);
    boolean keyDown(Key key);
    Set<Integer> getHeldKeys();
    Set<Integer> getPressedKeys();
    Set<Integer> getLeftKeys();
}
