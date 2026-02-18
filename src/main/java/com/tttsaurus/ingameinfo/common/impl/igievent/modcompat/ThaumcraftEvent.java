package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.common.core.function.Action1Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

public final class ThaumcraftEvent extends EventBase<Action1Param<ThaumcraftEvent.ThaumcraftData>>
{
    @Override
    public void addListener(Action1Param<ThaumcraftData> listener)
    {
        addListenerInternal(listener);
    }

    @ZenRegister
    @ZenClass("mods.ingameinfo.compat.thaumcraft.ThaumcraftData")
    public static class ThaumcraftData
    {
        private final float localVis;
        private final float localFlux;
        private final int localAuraBase;
        private final int permanentWarp;
        private final int normalWarp;
        private final int temporaryWarp;

        public ThaumcraftData(
                float localVis,
                float localFlux,
                int localAuraBase,
                int permanentWarp,
                int normalWarp,
                int temporaryWarp)
        {
            this.localVis = localVis;
            this.localFlux = localFlux;
            this.localAuraBase = localAuraBase;
            this.permanentWarp = permanentWarp;
            this.normalWarp = normalWarp;
            this.temporaryWarp = temporaryWarp;
        }

        @ZenMethod
        public float getLocalVis() { return localVis; }
        @ZenMethod
        public float getLocalFlux() { return localFlux; }
        @ZenMethod
        public int getLocalAuraBase() { return localAuraBase; }
        @ZenMethod
        public int getPermanentWarp() { return permanentWarp; }
        @ZenMethod
        public int getNormalWarp() { return normalWarp; }
        @ZenMethod
        public int getTemporaryWarp() { return temporaryWarp; }
    }
}
