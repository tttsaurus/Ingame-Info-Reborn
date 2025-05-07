package com.tttsaurus.ingameinfo.demo.eg1;

import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMouseClickButton;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveCollection;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;

public class Eg1ViewModel extends ViewModel<Eg1View>
{
    @Reactive(targetUid = "tps/mspt", property = "text", initiativeSync = true)
    public ReactiveObject<String> tpsMtpsText = new ReactiveObject<>(){};

    @Reactive(targetUid = "memoryBar", property = "percentage", initiativeSync = true)
    public ReactiveObject<Float> memoryBarPercentage = new ReactiveObject<>(){};

    @Reactive(targetUid = "memory", property = "text", initiativeSync = true)
    public ReactiveObject<String> memoryText = new ReactiveObject<>(){};

    @Reactive(targetUid = "switch", property = "addClickListener", initiativeSync = true)
    public ReactiveObject<IMouseClickButton> switchClick = new ReactiveObject<>(){};

    @Reactive(targetUid = "list")
    public ReactiveCollection list = new ReactiveCollection();

    private boolean flag = false;

    @Override
    public void start()
    {
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

        switchClick.set(new IMouseClickButton()
        {
            @Override
            public void click()
            {
                if (flag)
                {
                    list.get(0).set("enabled", false);
                    list.get(1).set("enabled", true);
                }
                else
                {
                    list.get(0).set("enabled", true);
                    list.get(1).set("enabled", false);
                }
                flag = !flag;
            }
        });
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }
}
