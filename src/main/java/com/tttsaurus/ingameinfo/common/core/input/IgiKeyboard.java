package com.tttsaurus.ingameinfo.common.core.input;

import org.lwjgl.input.Keyboard;
import java.util.HashSet;
import java.util.Set;

public class IgiKeyboard implements IKeyboard
{
    public static final IgiKeyboard INSTANCE = new IgiKeyboard();

    private final Set<Integer> heldKeys = new HashSet<>();
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Set<Integer> leftKeys = new HashSet<>();

    @Override
    public Set<Integer> getHeldKeys() { return heldKeys; }

    @Override
    public Set<Integer> getPressedKeys() { return pressedKeys; }

    @Override
    public Set<Integer> getLeftKeys() { return leftKeys; }

    public void updateInput()
    {
        Set<Integer> copy = new HashSet<>(heldKeys);
        heldKeys.clear();
        pressedKeys.clear();
        leftKeys.clear();
        for (int key = 1; key <= 223; key++)
        {
            boolean down = Keyboard.isKeyDown(key);
            if (down)
                heldKeys.add(key);
            if (!copy.contains(key) && down)
                pressedKeys.add(key);
            if (copy.contains(key) && !down)
                leftKeys.add(key);
        }
    }

    public boolean keyPress(Key key)
    {
        return pressedKeys.contains(key.keycode);
    }

    public boolean keyLeave(Key key)
    {
        return leftKeys.contains(key.keycode);
    }

    public boolean keyDown(Key key)
    {
        return heldKeys.contains(key.keycode);
    }
}
