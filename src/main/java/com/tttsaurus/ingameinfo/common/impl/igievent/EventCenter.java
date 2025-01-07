package com.tttsaurus.ingameinfo.common.impl.igievent;

public final class EventCenter
{
    public final static IgiGuiFpsEvent igiGuiFpsEvent = new IgiGuiFpsEvent();
    public final static GameFpsEvent gameFpsEvent = new GameFpsEvent();
    public final static GameMemoryEvent gameMemoryEvent = new GameMemoryEvent();

    // spotify
    public final static SpotifyOverlayEvent spotifyOverlayEvent = new SpotifyOverlayEvent();
}
