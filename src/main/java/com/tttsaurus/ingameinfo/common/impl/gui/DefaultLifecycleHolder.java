package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.fluxloading.core.FluxLoadingAPI;
import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLifecycleHolder;
import com.tttsaurus.ingameinfo.common.core.input.IgiKeyboard;
import com.tttsaurus.ingameinfo.common.core.input.IgiMouse;
import com.tttsaurus.ingameinfo.common.core.input.InputFrameGenerator;

public final class DefaultLifecycleHolder extends GuiLifecycleHolder
{
    public static final String HOLDER_NAME = "official_igi_lifecycle_holder";

    public DefaultLifecycleHolder()
    {
        super(HOLDER_NAME, new InputFrameGenerator(new IgiKeyboard(), new IgiMouse()));
    }

    private boolean init = false;

    @Override
    public void update()
    {
        if (getLifecycleProvider() == null) return;

        // fluxloading compat
        if (InGameInfoReborn.fluxloadingLoaded)
        {
            if (!init)
            {
                init = true;
                getLifecycleProvider().update(inputGen.generate());
            }
            if (!FluxLoadingAPI.isDuringFadingOutPhase() && !FluxLoadingAPI.isFinishLoading()) return;
        }

        getLifecycleProvider().update(inputGen.generate());
    }
}
