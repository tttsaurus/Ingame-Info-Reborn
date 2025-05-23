package com.tttsaurus.ingameinfo.common.core.mvvm.binding;

import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.mvvm.compose.ComposeBlock;
import java.lang.reflect.Constructor;

public class SlotAccessor
{
    // the slot group
    protected ElementGroup group;

    private ComposeBlock composeBlock = null;
    public ComposeBlock getComposeBlock() { return composeBlock; }

    public void initComposeBlock(Class<? extends ComposeBlock> clazz)
    {
        if (group == null)
            throw new IllegalStateException("Internal object is null. The binding probably failed.");

        try
        {
            Constructor<? extends ComposeBlock> constructor = clazz.getConstructor(ElementGroup.class);
            composeBlock = constructor.newInstance(group);
        }
        catch (Exception ignored)
        {
            throw new RuntimeException("Failed to initialize the compose block.");
        }
    }
}
