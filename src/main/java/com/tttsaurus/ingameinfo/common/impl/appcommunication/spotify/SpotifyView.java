package com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify;

import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;

public class SpotifyView extends View
{
    @Override
    public String getDefaultIxml()
    {
        return
               """
               <ImageUrl uid = "albumImage" width = 20 height = 20>
               """;
    }

    @Override
    public String getIxmlFileName()
    {
        return "spotify";
    }
}
