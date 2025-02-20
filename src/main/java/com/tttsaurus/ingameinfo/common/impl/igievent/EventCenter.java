package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.impl.igievent.modcompat.*;

public final class EventCenter
{
    // vanilla
    public final static IgiGuiFpsEvent igiGuiFpsEvent = new IgiGuiFpsEvent();
    public final static IgiGuiFboRefreshRateEvent igiGuiFboRefreshRateEvent = new IgiGuiFboRefreshRateEvent();
    public final static GameFpsEvent gameFpsEvent = new GameFpsEvent();
    public final static GameMemoryEvent gameMemoryEvent = new GameMemoryEvent();
    public final static GameTpsMsptEvent gameTpsMsptEvent = new GameTpsMsptEvent();
    public final static EnterBiomeEvent enterBiomeEvent = new EnterBiomeEvent();

    // spotify
    public final static SpotifyOverlayEvent spotifyOverlayEvent = new SpotifyOverlayEvent();
    public final static SpotifyOverlayEditEvent spotifyOverlayEditEvent = new SpotifyOverlayEditEvent();

    //<editor-fold desc="mod compat">
    public final static BloodMagicEvent bloodMagicEvent = new BloodMagicEvent();
    public final static SereneSeasonsEvent sereneSeasonsEvent = new SereneSeasonsEvent();
    public final static ThaumcraftEvent thaumcraftEvent = new ThaumcraftEvent();
    public final static RfToolsDimEvent rftoolsdimEvent = new RfToolsDimEvent();
    public final static DeepResonanceEvent deepresonanceEvent = new DeepResonanceEvent();
    public final static ToughAsNailsEvent toughasnailsEvent = new ToughAsNailsEvent();

    public static void triggerModCompatEvents()
    {
        Helper.triggerModCompatEvents();
    }
    //</editor-fold>
}
