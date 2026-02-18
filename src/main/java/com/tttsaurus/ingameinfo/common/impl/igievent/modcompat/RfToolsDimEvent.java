package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.common.core.function.Action1Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

public final class RfToolsDimEvent extends EventBase<Action1Param<RfToolsDimEvent.RfToolsDimData>>
{
    @Override
    public void addListener(Action1Param<RfToolsDimData> listener)
    {
        addListenerInternal(listener);
    }

    @ZenRegister
    @ZenClass("mods.ingameinfo.compat.rftoolsdim.RfToolsDimData")
    public static class RfToolsDimData
    {
        private final boolean isRfToolsDim;
        private final long dimPower;
        private final int dimRfCost;
        private final String dimName;
        private final String dimOwnerName;

        public RfToolsDimData(
                boolean isRfToolsDim,
                long dimPower,
                int dimRfCost,
                String dimName,
                String dimOwnerName)
        {
            this.isRfToolsDim = isRfToolsDim;
            this.dimPower = dimPower;
            this.dimRfCost = dimRfCost;
            this.dimName = dimName;
            this.dimOwnerName = dimOwnerName;
        }

        @ZenMethod
        public boolean getIsRfToolsDim() { return isRfToolsDim; }
        @ZenMethod
        public long getDimPower() { return dimPower; }
        @ZenMethod
        public int getDimRfCost() { return dimRfCost; }
        @ZenMethod
        public String getDimName() { return dimName; }
        @ZenMethod
        public String getDimOwnerName() { return dimOwnerName; }
    }
}
