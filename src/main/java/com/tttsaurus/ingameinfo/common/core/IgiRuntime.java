package com.tttsaurus.ingameinfo.common.core;

import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiGuiLifecycleInitEvent;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLifecycleHolder;
import com.tttsaurus.ingameinfo.common.core.gui.screen.IGuiScreenDrawScreen;
import com.tttsaurus.ingameinfo.common.core.gui.screen.IGuiScreenKeyTyped;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        private final DefaultLifecycleHolder lifecycleHolder;
        private final List<String> guisToOpenWhenLifecycleInit0 = new ArrayList<>();

        private final List<GuiLifecycleHolder> externaLifecycleHolders;
        private final Map<GuiLifecycleHolder, List<String>> guisToOpenWhenLifecycleInit1 = new HashMap<>();

        private InitPhaseEntry(MvvmRegistry mvvmRegistry, DefaultLifecycleHolder lifecycleHolder, List<GuiLifecycleHolder> externaLifecycleHolders)
        {
            this.mvvmRegistry = mvvmRegistry;
            this.lifecycleHolder = lifecycleHolder;
            this.externaLifecycleHolders = externaLifecycleHolders;
        }

        public InitPhaseEntry registerMvvm(String mvvmRegistryName, Class<? extends ViewModel<?>> viewModelClass)
        {
            mvvmRegistry.autoRegister(mvvmRegistryName, viewModelClass);
            return this;
        }

        @ZenMethod
        public InitPhaseEntry openGuiOnStartup(String mvvmRegistryName)
        {
            if (!guisToOpenWhenLifecycleInit0.contains(mvvmRegistryName))
                guisToOpenWhenLifecycleInit0.add(mvvmRegistryName);
            return this;
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

        public InitPhaseEntry openGuiOnStartup(String holderName, String mvvmRegistryName)
        {
            GuiLifecycleHolder holder = null;
            if (holderName.equals(DefaultLifecycleHolder.HOLDER_NAME))
                holder = lifecycleHolder;
            else for (GuiLifecycleHolder otherHolder: externaLifecycleHolders)
                if (holderName.equals(otherHolder.getHolderName()))
                {
                    holder = otherHolder;
                    break;
                }

            if (holder == null) return this;

            List<String> list = guisToOpenWhenLifecycleInit1.computeIfAbsent(holder, k -> new ArrayList<>());
            if (!list.contains(mvvmRegistryName))
                list.add(mvvmRegistryName);
            return this;
        }
    }

    @ZenRegister
    @ZenClass("mods.ingameinfo.runtime.LivePhaseEntry")
    public static class LivePhaseEntry
    {
        private final MvvmRegistry mvvmRegistry;
        private final DefaultLifecycleHolder lifecycleHolder;
        private final List<GuiLifecycleHolder> externaLifecycleHolders;

        private LivePhaseEntry(MvvmRegistry mvvmRegistry, DefaultLifecycleHolder lifecycleHolder, List<GuiLifecycleHolder> externaLifecycleHolders)
        {
            this.mvvmRegistry = mvvmRegistry;
            this.lifecycleHolder = lifecycleHolder;
            this.externaLifecycleHolders = externaLifecycleHolders;
        }

        @ZenMethod
        public LivePhaseEntry openGui(String mvvmRegistryName)
        {
            lifecycleHolder.openGui(mvvmRegistryName, mvvmRegistry);
            // force update to refresh
            lifecycleHolder.update();
            return this;
        }

        @ZenMethod
        public LivePhaseEntry closeGui(String mvvmRegistryName)
        {
            lifecycleHolder.closeGui(mvvmRegistryName, mvvmRegistry);
            // force update to refresh
            lifecycleHolder.update();
            return this;
        }

        @ZenMethod
        public boolean isGuiOpen(String mvvmRegistryName)
        {
            return InternalMethods.GuiLifecycleProvider$openedGuiMap$getter(lifecycleHolder.getLifecycleProvider()).containsKey(mvvmRegistryName);
        }

        public LivePhaseEntry openGui(String holderName, String mvvmRegistryName)
        {
            GuiLifecycleHolder holder = null;
            if (holderName.equals(DefaultLifecycleHolder.HOLDER_NAME))
                holder = lifecycleHolder;
            else for (GuiLifecycleHolder otherHolder: externaLifecycleHolders)
                if (holderName.equals(otherHolder.getHolderName()))
                {
                    holder = otherHolder;
                    break;
                }

            if (holder == null) return this;

            holder.openGui(mvvmRegistryName, mvvmRegistry);
            // force update to refresh
            holder.update();
            return this;
        }

        public LivePhaseEntry closeGui(String holderName, String mvvmRegistryName)
        {
            GuiLifecycleHolder holder = null;
            if (holderName.equals(DefaultLifecycleHolder.HOLDER_NAME))
                holder = lifecycleHolder;
            else for (GuiLifecycleHolder otherHolder: externaLifecycleHolders)
                if (holderName.equals(otherHolder.getHolderName()))
                {
                    holder = otherHolder;
                    break;
                }

            if (holder == null) return this;

            holder.closeGui(mvvmRegistryName, mvvmRegistry);
            // force update to refresh
            holder.update();
            return this;
        }

        public boolean isGuiOpen(String holderName, String mvvmRegistryName)
        {
            GuiLifecycleHolder holder = null;
            if (holderName.equals(DefaultLifecycleHolder.HOLDER_NAME))
                holder = lifecycleHolder;
            else for (GuiLifecycleHolder otherHolder: externaLifecycleHolders)
                if (holderName.equals(otherHolder.getHolderName()))
                {
                    holder = otherHolder;
                    break;
                }

            if (holder == null) return false;

            return InternalMethods.GuiLifecycleProvider$openedGuiMap$getter(holder.getLifecycleProvider()).containsKey(mvvmRegistryName);
        }

        public boolean isOccupyingScreen()
        {
            if (lifecycleHolder.getLifecycleProvider().isUsingOtherScreen())
                return true;
            for (GuiLifecycleHolder holder: externaLifecycleHolders)
                if (holder.getLifecycleProvider().isUsingOtherScreen())
                    return true;
            return false;
        }

        public List<IGuiScreenDrawScreen> collectDrawScreenDelegatesIfScreenOccupied()
        {
            List<IGuiScreenDrawScreen> res = new ArrayList<>();
            if (lifecycleHolder.getLifecycleProvider().isUsingOtherScreen())
                res.add(lifecycleHolder.getLifecycleProvider().GUI_SCREEN_DRAW_SCREEN);
            for (GuiLifecycleHolder holder: externaLifecycleHolders)
                if (holder.getLifecycleProvider().isUsingOtherScreen())
                    res.add(holder.getLifecycleProvider().GUI_SCREEN_DRAW_SCREEN);
            return res;
        }

        public List<IGuiScreenKeyTyped> collectKeyTypedDelegatesIfScreenOccupied()
        {
            List<IGuiScreenKeyTyped> res = new ArrayList<>();
            if (lifecycleHolder.getLifecycleProvider().isUsingOtherScreen())
                res.add(lifecycleHolder.getLifecycleProvider().GUI_SCREEN_KEY_TYPED);
            for (GuiLifecycleHolder holder: externaLifecycleHolders)
                if (holder.getLifecycleProvider().isUsingOtherScreen())
                    res.add(holder.getLifecycleProvider().GUI_SCREEN_KEY_TYPED);
            return res;
        }
    }

    public static class GlobalEntry
    {
        public final MvvmRegistry mvvmRegistry;
        public final DefaultLifecycleHolder lifecycleHolder;

        private GlobalEntry(MvvmRegistry mvvmRegistry, DefaultLifecycleHolder lifecycleHolder)
        {
            this.mvvmRegistry = mvvmRegistry;
            this.lifecycleHolder = lifecycleHolder;
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

        initPhase = new InitPhaseEntry(mvvmRegistry, lifecycleHolder, externaLifecycleHolders);
        livePhase = new LivePhaseEntry(mvvmRegistry, lifecycleHolder, externaLifecycleHolders);
        global = new GlobalEntry(mvvmRegistry, lifecycleHolder);

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
            for (String mvvmRegistryName: initPhase.guisToOpenWhenLifecycleInit0)
                lifecycleHolder.openGui(mvvmRegistryName, mvvmRegistry);
            initPhase.guisToOpenWhenLifecycleInit0.clear();
        }

        for (Map.Entry<GuiLifecycleHolder, List<String>> entry: new ArrayList<>(initPhase.guisToOpenWhenLifecycleInit1.entrySet()))
        {
            if (event.lifecycleOwner.equals(entry.getKey().getHolderName()))
            {
                for (String mvvmRegistryName: entry.getValue())
                    entry.getKey().openGui(mvvmRegistryName, mvvmRegistry);
                initPhase.guisToOpenWhenLifecycleInit1.remove(entry.getKey());
            }
        }
    }
}
