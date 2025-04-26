package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import com.tttsaurus.ingameinfo.common.api.gui.ElementAccessor;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;

public class ReactiveCollection
{
    protected ElementGroup group;

    public int size()
    {
        if (group == null)
            throw new IllegalStateException("Internal object is null. The binding probably failed.");
        return group.elements.size();
    }
    public ElementAccessor get(int index)
    {
        if (group == null)
            throw new IllegalStateException("Internal object is null. The binding probably failed.");
        if (index < 0 || index > group.elements.size() - 1)
            throw new IndexOutOfBoundsException("Index " + index + " is invalid as the length of this collection is " + group.elements.size());
        return new ElementAccessor(group.elements.get(index));
    }
}
