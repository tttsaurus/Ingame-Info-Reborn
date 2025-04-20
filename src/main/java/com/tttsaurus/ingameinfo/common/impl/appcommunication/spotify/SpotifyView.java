package com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify;

import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;

public class SpotifyView extends View
{
    @Override
    public String getDefaultIxml()
    {
        return
               """
               <Def debug = false theme = "spotify">
               <VerticalGroup padding = {"top": 10, "bottom": 10, "left": 10, "right": 10} alignment = TOP_RIGHT pivot = TOP_RIGHT>
                   <VerticalGroup drawBackground = true>
                       <HorizontalGroup uid = "albumImageGroup" padding = {"top": 10, "bottom": 5}>
                           <UrlImage uid = "albumImage" rounded = true width = 40 height = 40 padding = {"left": 10, "right": 10}>
                           <VerticalGroup uid = "anotherTrackTitleGroup" padding = {"top": 10, "left": 5} enabled = false>
                               <SlidingText uid = "anotherTrackTitle" width = 40 spareWidth = 20 onDemandSliding = true>
                               <SlidingText uid = "anotherTrackAuthor" scale = 0.7f width = 30 spareWidth = 20 onDemandSliding = true padding = {"top": 5}>
                               <Text uid = "anotherTrackTimer" scale = 0.7f alignment = TOP_RIGHT pivot = TOP_RIGHT padding = {"top": 25}>
                           </Group>
                       </Group>
                       <HorizontalGroup uid = "trackTitleGroup" padding = {"bottom": 5}>
                           <SlidingText uid = "trackTitle" width = 40 spareWidth = 20 onDemandSliding = true padding = {"left": 10, "right": 10}>
                       </Group>
                       <HorizontalGroup uid = "progressBarGroup" padding = {"bottom": 10}>
                           <ProgressBar uid = "progressBar" width = 50 height = 3 padding = {"left": 5, "right": 5}>
                       </Group>
                   </Group>
                   <Button uid = "editButton" text = "Switch Layout" padding = {"top": 5} enabled = false>
               </Group>
               """;
    }

    @Override
    public String getIxmlFileName()
    {
        return "spotify";
    }
}
