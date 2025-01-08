package com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify;

import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;

public class SpotifyView extends View
{
    @Override
    public String getDefaultIxml()
    {
        return
               """
               <Def debug = false>
               <VerticalGroup padding = {"top": 10, "left": 10} backgroundStyle = "roundedBoxWithOutline">
                   <HorizontalGroup padding = {"top": 10, "bottom": 5}>
                       <UrlImage uid = "albumImage" width = 40 height = 40 padding = {"left": 10, "right": 10}>
                   </Group>
                   <HorizontalGroup padding = {"bottom": 5}>
                       <SlidingText uid = "trackTitle" width = 40 spareWidth = 20 onDemandSliding = true padding = {"left": 10, "right": 10}>
                   </Group>
                   <HorizontalGroup padding = {"bottom": 10}>
                       <ProgressBar uid = "progressBar" width = 50 height = 3 padding = {"left": 5, "right": 5}>
                   </Group>
               </Group>
               """;
    }

    @Override
    public String getIxmlFileName()
    {
        return "spotify";
    }
}
