package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.event.IgiGuiInitEvent;
import com.tttsaurus.ingameinfo.common.core.event.RegainScreenFocusEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.time.StopWatch;
import org.lwjgl.opengl.Display;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class GuiLifecycleProvider
{
    protected final Map<String, IgiGuiContainer> openedGuiMap = new LinkedHashMap<>();

    public final void openIgiGui(String mvvmRegistryName, IgiGuiContainer guiContainer)
    {
        if (openedGuiMap.containsKey(mvvmRegistryName)) return;
        openedGuiMap.put(mvvmRegistryName, guiContainer);
    }
    public final void closeIgiGui(String mvvmRegistryName)
    {
        openedGuiMap.remove(mvvmRegistryName);
    }

    protected abstract void updateInternal();

    private boolean listenRegainScreenFocus = false;
    private boolean initFlag = true;
    public final void update()
    {
        if (initFlag)
        {
            initFlag = false;
            MinecraftForge.EVENT_BUS.post(new IgiGuiInitEvent());
        }

        if (!Display.isActive() && !listenRegainScreenFocus)
            listenRegainScreenFocus = true;
        if (Display.isActive() && listenRegainScreenFocus)
        {
            listenRegainScreenFocus = false;
            MinecraftForge.EVENT_BUS.post(new RegainScreenFocusEvent());
        }

        updateInternal();
    }

    //<editor-fold desc="fixed update">
    // units are all in second
    protected int maxFps_FixedUpdate = 125;
    protected double timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    public final void setMaxFps_FixedUpdate(int fps)
    {
        maxFps_FixedUpdate = fps;
        timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    }
    protected final StopWatch stopwatch_FixedUpdate = new StopWatch();

    protected abstract void onFixedUpdate();
    //</editor-fold>

    //<editor-fold desc="render update">
    // units are all in second
    protected int maxFps_RenderUpdate = 240;
    protected double timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    public final void setMaxFps_RenderUpdate(int fps)
    {
        maxFps_RenderUpdate = fps;
        timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    }
    protected final StopWatch stopwatch_RenderUpdate = new StopWatch();

    protected abstract void onRenderUpdate();
    //</editor-fold>
}
