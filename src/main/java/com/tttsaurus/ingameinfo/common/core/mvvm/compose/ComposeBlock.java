package com.tttsaurus.ingameinfo.common.core.mvvm.compose;

import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;

public abstract class ComposeBlock
{
    private final ElementGroup root;

    public ComposeBlock(ElementGroup root)
    {
        this.root = root;
    }
}
