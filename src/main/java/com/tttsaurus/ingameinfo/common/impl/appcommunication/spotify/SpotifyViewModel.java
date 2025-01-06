package com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;

public class SpotifyViewModel extends ViewModel<SpotifyView>
{
    @Reactive(targetUid = "albumImage", property = "url", initiativeSync = true)
    public ReactiveObject<String> albumImageUrl = new ReactiveObject<>(){};

    @Override
    public void start()
    {
        albumImageUrl.set("https://media.forgecdn.net/avatars/thumbnails/1071/348/256/256/638606872011907048.png");
    }
}
