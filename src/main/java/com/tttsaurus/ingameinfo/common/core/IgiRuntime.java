package com.tttsaurus.ingameinfo.common.core;

import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiGuiLifecycleInitEvent;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLifecycleHolder;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.gui.DefaultLifecycleHolder;
import com.tttsaurus.ingameinfo.config.IgiCommonConfig;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.List;

public final class IgiRuntime
{
    private static IgiRuntime instance;
    private static void init()
    {
        instance = new IgiRuntime();
    }

    public static class UnifiedEntry
    {
        private final MvvmRegistry mvvmRegistry;
        private final GuiLifecycleHolder lifecycleHolder;
        private final List<String> guisToOpenWhenLifecycleInit = new ArrayList<>();

        private UnifiedEntry(MvvmRegistry mvvmRegistry, GuiLifecycleHolder lifecycleHolder)
        {
            this.mvvmRegistry = mvvmRegistry;
            this.lifecycleHolder = lifecycleHolder;
        }

        public UnifiedEntry registerMvvm(String mvvmRegistryName, Class<? extends ViewModel<?>> viewModelClass)
        {
            mvvmRegistry.autoRegister(mvvmRegistryName, viewModelClass);
            return this;
        }

        public UnifiedEntry openGuiOnStartup(String mvvmRegistryName)
        {
            if (!guisToOpenWhenLifecycleInit.contains(mvvmRegistryName))
                guisToOpenWhenLifecycleInit.add(mvvmRegistryName);
            return this;
        }

        public UnifiedEntry openGui(String mvvmRegistryName)
        {
            lifecycleHolder.openGui(mvvmRegistryName, mvvmRegistry);
            return this;
        }
    }

    public final MvvmRegistry mvvmRegistry;
    public final DefaultLifecycleHolder lifecycleHolder;
    public final UnifiedEntry unifiedEntry;

    private IgiRuntime()
    {
        mvvmRegistry = new MvvmRegistry();
        lifecycleHolder = new DefaultLifecycleHolder();
        lifecycleHolder.setLifecycleProvider(IgiCommonConfig.GUI_LIFECYCLE_PROVIDER);
        unifiedEntry = new UnifiedEntry(mvvmRegistry, lifecycleHolder);
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
            for (String mvvmRegistryName: unifiedEntry.guisToOpenWhenLifecycleInit)
            {
                lifecycleHolder.openGui(mvvmRegistryName, mvvmRegistry);
            }
            unifiedEntry.guisToOpenWhenLifecycleInit.clear();
        }
    }
}
