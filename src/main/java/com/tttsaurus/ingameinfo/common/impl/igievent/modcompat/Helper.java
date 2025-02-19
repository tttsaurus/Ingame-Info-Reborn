package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

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
        if (InGameInfoReborn.thaumcraftLoaded)
        {
            IgiNetwork.requestThaumcraftNbt((nbt) ->
            {
                float localVis = nbt.getFloat("LocalVis");
                float localFlux = nbt.getFloat("LocalFlux");
                int localAuraBase = nbt.getInteger("LocalAuraBase");

                int permanentWarp = 0;
                int normalWarp = 0;
                int temporaryWarp = 0;
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                IPlayerWarp warp = null;
                if (player != null)
                    warp = player.getCapability(ThaumcraftCapabilities.WARP, null);
                if (warp != null)
                {
                    permanentWarp = warp.get(IPlayerWarp.EnumWarpType.PERMANENT);
                    normalWarp = warp.get(IPlayerWarp.EnumWarpType.NORMAL);
                    temporaryWarp = warp.get(IPlayerWarp.EnumWarpType.TEMPORARY);
                }

                EventCenter.thaumcraftEvent.trigger(new ThaumcraftEvent.ThaumcraftData(
                        localVis,
                        localFlux,
                        localAuraBase,
                        permanentWarp,
                        normalWarp,
                        temporaryWarp
                ));
            });
        }
    }
}
