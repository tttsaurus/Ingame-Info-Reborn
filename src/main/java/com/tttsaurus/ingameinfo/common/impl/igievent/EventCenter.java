package com.tttsaurus.ingameinfo.common.impl.igievent;

public final class EventCenter
{
    public final static IgiGuiFpsEvent igiGuiFpsEvent = new IgiGuiFpsEvent();
    public final static IgiGuiFboRefreshRateEvent igiGuiFboRefreshRateEvent = new IgiGuiFboRefreshRateEvent();
    public final static GameFpsEvent gameFpsEvent = new GameFpsEvent();
    public final static GameMemoryEvent gameMemoryEvent = new GameMemoryEvent();
    public final static GameTpsMtpsEvent gameTpsMtpsEvent = new GameTpsMtpsEvent();
    public final static EnterBiomeEvent enterBiomeEvent = new EnterBiomeEvent();

    // spotify
    public final static SpotifyOverlayEvent spotifyOverlayEvent = new SpotifyOverlayEvent();
    public final static SpotifyOverlayEditEvent spotifyOverlayEditEvent = new SpotifyOverlayEditEvent();
}
