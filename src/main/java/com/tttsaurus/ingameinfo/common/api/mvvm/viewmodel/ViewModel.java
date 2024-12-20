package com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.VVMBinding;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;

// only do one inheritance; don't do nested inheritance
// ofc T can't be View
public abstract class ViewModel<T extends View>
{
    private VVMBinding<T> binding = new VVMBinding<>();

    // entry point
    public GuiLayout init()
    {
        GuiLayout guiLayout = binding.init(this);

        return guiLayout;
    }
}
