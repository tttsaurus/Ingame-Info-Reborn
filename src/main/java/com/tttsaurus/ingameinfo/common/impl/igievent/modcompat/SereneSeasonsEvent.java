package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

public final class SereneSeasonsEvent extends EventBase<IAction_1Param<SereneSeasonsEvent.SereneSeasonsData>>
{
    @Override
    public void addListener(IAction_1Param<SereneSeasonsData> listener)
    {
        addListenerInternal(listener);
    }

    @ZenRegister
    @ZenClass("mods.ingameinfo.compat.sereneseasons.SereneSeasonsData")
    public static class SereneSeasonsData
    {
        private final int dayDuration;
        private final int subSeasonDuration;
        private final int seasonDuration;
        private final int cycleDuration;
        private final int seasonCycleTicks;
        private final int day;
        private final String seasonName;
        private final String subSeasonName;
        private final String tropicalSeasonName;
        private final int seasonOrdinal;
        private final int subSeasonOrdinal;
        private final int tropicalSeasonOrdinal;
        private final int dayOfSeason;

        public SereneSeasonsData(
                int dayDuration,
                int subSeasonDuration,
                int seasonDuration,
                int cycleDuration,
                int seasonCycleTicks,
                int day,
                String seasonName,
                String subSeasonName,
                String tropicalSeasonName,
                int seasonOrdinal,
                int subSeasonOrdinal,
                int tropicalSeasonOrdinal,
                int dayOfSeason)
        {
            this.dayDuration = dayDuration;
            this.subSeasonDuration = subSeasonDuration;
            this.seasonDuration = seasonDuration;
            this.cycleDuration = cycleDuration;
            this.seasonCycleTicks = seasonCycleTicks;
            this.day = day;
            this.seasonName = seasonName;
            this.subSeasonName = subSeasonName;
            this.tropicalSeasonName = tropicalSeasonName;
            this.seasonOrdinal = seasonOrdinal;
            this.subSeasonOrdinal = subSeasonOrdinal;
            this.tropicalSeasonOrdinal = tropicalSeasonOrdinal;
            this.dayOfSeason = dayOfSeason;
        }

        @ZenMethod
        public int getDayDuration() { return dayDuration; }
        @ZenMethod
        public int getSubSeasonDuration() { return subSeasonDuration; }
        @ZenMethod
        public int getSeasonDuration() { return seasonDuration; }
        @ZenMethod
        public int getCycleDuration() { return cycleDuration; }
        @ZenMethod
        public int getSeasonCycleTicks() { return seasonCycleTicks; }
        @ZenMethod
        public int getDay() { return day; }
        @ZenMethod
        public String getSeasonName() { return seasonName; }
        @ZenMethod
        public String getSubSeasonName() { return subSeasonName; }
        @ZenMethod
        public String getTropicalSeasonName() { return tropicalSeasonName; }
        @ZenMethod
        public int getSeasonOrdinal() { return seasonOrdinal; }
        @ZenMethod
        public int getSubSeasonOrdinal() { return subSeasonOrdinal;}
        @ZenMethod
        public int getTropicalSeasonOrdinal() { return tropicalSeasonOrdinal; }
        @ZenMethod
        public int getDayOfSeason() { return dayOfSeason; }
    }
}
