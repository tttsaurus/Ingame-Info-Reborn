package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.fluxloading.core.FluxLoadingAPI;
import com.tttsaurus.fluxloading.core.fsm.FluxLoadingPhase;
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
        if (InGameInfoReborn.isFluxLoadingLoaded().orElseThrow() && FluxLoadingAPI.isActive())
        {
            if (!init)
            {
                init = true;
                getLifecycleProvider().update(inputGen.generate());
            }
            if (FluxLoadingAPI.getPhase() != FluxLoadingPhase.FADING_OUT && FluxLoadingAPI.getPhase() != FluxLoadingPhase.FINISHED) return;
        }

        getLifecycleProvider().update(inputGen.generate());
    }
}
