package com.tttsaurus.ingameinfo.common.impl.mvvm;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;

public class TemplateViewModel extends ViewModel<TemplateView>
{
    @Reactive(targetUid = "biome", property = "text", initiativeSync = true)
    public ReactiveObject<String> biomeText = new ReactiveObject<>(){};

    @Reactive(targetUid = "tps/mtps", property = "text", initiativeSync = true)
    public ReactiveObject<String> tpsMtpsText = new ReactiveObject<>(){};

    @Reactive(targetUid = "memoryBar", property = "percentage", initiativeSync = true)
    public ReactiveObject<Float> memoryBarPercentage = new ReactiveObject<>(){};

    @Reactive(targetUid = "memory", property = "text", initiativeSync = true)
    public ReactiveObject<String> memoryText = new ReactiveObject<>(){};

    @Reactive(targetUid = "fps", property = "text", initiativeSync = true)
    public ReactiveObject<String> fpsText = new ReactiveObject<>(){};

    @Reactive(targetUid = "igiFps", property = "text", initiativeSync = true)
    public ReactiveObject<String> igiFpsText = new ReactiveObject<>(){};

    @Reactive(targetUid = "igiFbo", property = "text", initiativeSync = true)
    public ReactiveObject<String> igiFboText = new ReactiveObject<>(){};

    @Override
    public void start()
    {
        EventCenter.enterBiomeEvent.addListener((biomeName, registryName) ->
        {
            biomeText.set("Biome: " + biomeName + ", " + registryName);
        });
        EventCenter.gameTpsMsptEvent.addListener((tps, mspt) ->
        {
            tpsMtpsText.set(String.format("TPS: %d, MSPT: %.3f ms/t", tps, mspt));
        });
        EventCenter.gameMemoryEvent.addListener((used, total) ->
        {
            int usedMb = (int)(used / 1024 / 1024);
            int totalMb = (int)(total / 1024 / 1024);
            memoryBarPercentage.set(((float)usedMb / (float)totalMb));
            memoryText.set(usedMb + " MB / " + totalMb + " MB");
        });
        EventCenter.gameFpsEvent.addListener((fps) ->
        {
            fpsText.set("FPS: " + fps);
        });
        EventCenter.igiGuiFpsEvent.addListener((fixedFps, renderFps) ->
        {
            igiFpsText.set("IGI GUI FPS: " + fixedFps + ", " + renderFps);
        });
        EventCenter.igiGuiFboRefreshRateEvent.addListener((rate) ->
        {
            igiFboText.set(String.format("FBO Refresh Rate: %.2f ", rate * 100f) + "%");
        });
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }
}
