package com.tttsaurus.ingameinfo.common.core;

import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiGuiLifecycleInitEvent;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLifecycleHolder;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.gui.DefaultLifecycleHolder;
import com.tttsaurus.ingameinfo.config.IgiCommonConfig;
import crafttweaker.annotations.ZenRegister;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public final class IgiRuntime
{
    private static IgiRuntime instance;
    private static void init()
    {
        instance = new IgiRuntime();
    }

    @ZenRegister
    @ZenClass("mods.ingameinfo.runtime.InitPhaseEntry")
    public static class InitPhaseEntry
    {
        private final MvvmRegistry mvvmRegistry;
        private final List<String> guisToOpenWhenLifecycleInit = new ArrayList<>();

        private InitPhaseEntry(MvvmRegistry mvvmRegistry)
        {
            this.mvvmRegistry = mvvmRegistry;
        }

        public InitPhaseEntry registerMvvm(String mvvmRegistryName, Class<? extends ViewModel<?>> viewModelClass)
        {
            mvvmRegistry.autoRegister(mvvmRegistryName, viewModelClass);
            return this;
        }

        @ZenMethod
        public InitPhaseEntry openGuiOnStartup(String mvvmRegistryName)
        {
            if (!guisToOpenWhenLifecycleInit.contains(mvvmRegistryName))
                guisToOpenWhenLifecycleInit.add(mvvmRegistryName);
            return this;
        }
    }

    @ZenRegister
    @ZenClass("mods.ingameinfo.runtime.LivePhaseEntry")
    public static class LivePhaseEntry
    {
        private final MvvmRegistry mvvmRegistry;
        private final DefaultLifecycleHolder lifecycleHolder;

        private LivePhaseEntry(MvvmRegistry mvvmRegistry, DefaultLifecycleHolder lifecycleHolder)
        {
            this.mvvmRegistry = mvvmRegistry;
            this.lifecycleHolder = lifecycleHolder;
        }

        @ZenMethod
        public LivePhaseEntry openGui(String mvvmRegistryName)
        {
            lifecycleHolder.openGui(mvvmRegistryName, mvvmRegistry);
            return this;
        }

        @ZenMethod
        public LivePhaseEntry closeGui(String mvvmRegistryName)
        {
            lifecycleHolder.closeGui(mvvmRegistryName);
            return this;
        }
    }

    public static class GlobalEntry
    {
        public final MvvmRegistry mvvmRegistry;
        public final DefaultLifecycleHolder lifecycleHolder;
        private final List<GuiLifecycleHolder> externaLifecycleHolders;

        private GlobalEntry(MvvmRegistry mvvmRegistry, DefaultLifecycleHolder lifecycleHolder, List<GuiLifecycleHolder> externaLifecycleHolders)
        {
            this.mvvmRegistry = mvvmRegistry;
            this.lifecycleHolder = lifecycleHolder;
            this.externaLifecycleHolders = externaLifecycleHolders;
        }

        public GuiLifecycleHolder registerLifecycleHolder(Class<? extends GuiLifecycleHolder> holderClass)
        {
            if (holderClass.equals(DefaultLifecycleHolder.class)) return null;
            for (GuiLifecycleHolder otherHolder: externaLifecycleHolders)
                if (otherHolder.getClass().getName().equals(holderClass.getName())) return null;

            try
            {
                Constructor<? extends GuiLifecycleHolder> constructor = holderClass.getConstructor();
                constructor.setAccessible(true);
                GuiLifecycleHolder holder = constructor.newInstance();

                if (DefaultLifecycleHolder.HOLDER_NAME.equals(holder.getHolderName())) return null;
                for (GuiLifecycleHolder otherHolder: externaLifecycleHolders)
                    if (otherHolder.getHolderName().equals(holder.getHolderName())) return null;

                externaLifecycleHolders.add(holder);
                return holder;
            }
            catch (Throwable ignored) { }
            return null;
        }
    }

    private final MvvmRegistry mvvmRegistry;
    private final DefaultLifecycleHolder lifecycleHolder;
    private final List<GuiLifecycleHolder> externaLifecycleHolders;

    public final InitPhaseEntry initPhase;
    public final LivePhaseEntry livePhase;
    public final GlobalEntry global;

    private IgiRuntime()
    {
        mvvmRegistry = new MvvmRegistry();
        lifecycleHolder = new DefaultLifecycleHolder();
        lifecycleHolder.setLifecycleProvider(IgiCommonConfig.GUI_LIFECYCLE_PROVIDER);
        externaLifecycleHolders = new ArrayList<>();

        initPhase = new InitPhaseEntry(mvvmRegistry);
        livePhase = new LivePhaseEntry(mvvmRegistry, lifecycleHolder);
        global = new GlobalEntry(mvvmRegistry, lifecycleHolder, externaLifecycleHolders);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        lifecycleHolder.update();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onIgiGuiLifecycleInit(IgiGuiLifecycleInitEvent event)
    {
        if (event.lifecycleOwner.equals(DefaultLifecycleHolder.HOLDER_NAME))
        {
            for (String mvvmRegistryName: initPhase.guisToOpenWhenLifecycleInit)
            {
                lifecycleHolder.openGui(mvvmRegistryName, mvvmRegistry);
            }
            initPhase.guisToOpenWhenLifecycleInit.clear();
        }
    }
}
