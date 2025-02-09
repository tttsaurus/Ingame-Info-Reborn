package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;

public final class Helper
{
    public static void triggerModCompatEvents()
    {
        if (InGameInfoReborn.bloodmagicLoaded)
        {
            IgiNetwork.requestBloodMagicNbt((nbt) ->
            {
                int lp = nbt.getInteger("CurrentEssence");
                int orbTier = nbt.getInteger("OrbTier");
                EventCenter.bloodMagicEvent.trigger(lp, orbTier);
            });
        }
        if (InGameInfoReborn.sereneseasonsLoaded)
        {
            ISeasonState state = SeasonHelper.dataProvider.getClientSeasonState();
            int dayDuration = state.getDayDuration();
            int subSeasonDuration = state.getSubSeasonDuration();
            int seasonDuration = state.getSeasonDuration();
            int cycleDuration = state.getCycleDuration();
            int seasonCycleTicks = state.getSeasonCycleTicks();
            int day = state.getDay();
            String seasonName = state.getSeason().name();
            String subSeasonName = state.getSubSeason().name();
            String tropicalSeasonName = state.getTropicalSeason().name();
            int seasonOrdinal = state.getSeason().ordinal();
            int subSeasonOrdinal = state.getSubSeason().ordinal();
            int tropicalSeasonOrdinal = state.getTropicalSeason().ordinal();
            int dayOfSeason = state.getDay() % (state.getSeasonDuration() / state.getDayDuration());
            EventCenter.sereneSeasonsEvent.trigger(new SereneSeasonsEvent.SereneSeasonsData(
                    dayDuration,
                    subSeasonDuration,
                    seasonDuration,
                    cycleDuration,
                    seasonCycleTicks,
                    day,
                    seasonName,
                    subSeasonName,
                    tropicalSeasonName,
                    seasonOrdinal,
                    subSeasonOrdinal,
                    tropicalSeasonOrdinal,
                    dayOfSeason
            ));
        }
    }
}
