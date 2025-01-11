package com.tttsaurus.ingameinfo.common.impl.mvvm;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;

public class TemplateViewModel extends ViewModel<TemplateView>
{
    @Reactive(targetUid = "biome", property = "text", initiativeSync = true)
    public ReactiveObject<String> biomeText = new ReactiveObject<>(){};

    @Override
    public void start()
    {
        EventCenter.enterBiomeEvent.addListener((biomeName, registryName) ->
        {
            biomeText.set("Biome: " + biomeName + ", " + registryName);
        });
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }
}
