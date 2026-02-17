package com.tttsaurus.ingameinfo.common.core.mvvm.registry;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.IReactiveCollectionGetter;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.IReactiveObjectGetter;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ISlotAccessorGetter;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public final class MvvmRegistry
{
    // key: mvvm registry name
    private final Map<String, IgiGuiContainer> igiGuiContainerCache = new HashMap<>();

    @Nullable
    public IgiGuiContainer getIgiGuiContainer(String mvvmRegistryName)
    {
        if (!isMvvmRegistered(mvvmRegistryName)) return null;
        return igiGuiContainerCache.get(mvvmRegistryName);
    }
    public void cacheIgiGuiContainer(String mvvmRegistryName, @Nonnull ViewModel<?> viewModel)
    {
        GuiLayout guiLayout = InternalMethods.ViewModel$init(viewModel, mvvmRegistryName, this);
        IgiGuiContainer igiGuiContainer = InternalMethods.GuiLayout$igiGuiContainer$getter(guiLayout);
        InternalMethods.IgiGuiContainer$viewModel$setter(igiGuiContainer, viewModel);
        igiGuiContainerCache.put(mvvmRegistryName, igiGuiContainer);

        InGameInfoReborn.LOGGER.info("Generated and cached IgiGuiContainer for '" + mvvmRegistryName + "'.");
    }

    // key: mvvm registry name
    private final Map<String, Map<Reactive, IReactiveObjectGetter>> registeredReactiveObjects = new HashMap<>();
    private final Map<String, Map<Reactive, IReactiveCollectionGetter>> registeredReactiveCollections = new HashMap<>();
    private final Map<String, Map<Reactive, ISlotAccessorGetter>> registeredSlotAccessors = new HashMap<>();

    //<editor-fold desc="getters">
    public Map<Reactive, IReactiveObjectGetter> getRegisteredReactiveObjects(String mvvmRegistryName)
    {
        Map<Reactive, IReactiveObjectGetter> map = registeredReactiveObjects.get(mvvmRegistryName);
        if (map == null) return new HashMap<>();
        return map;
    }
    public Map<Reactive, IReactiveCollectionGetter> getRegisteredReactiveCollections(String mvvmRegistryName)
    {
        Map<Reactive, IReactiveCollectionGetter> map = registeredReactiveCollections.get(mvvmRegistryName);
        if (map == null) return new HashMap<>();
        return map;
    }
    public Map<Reactive, ISlotAccessorGetter> getRegisteredSlotAccessors(String mvvmRegistryName)
    {
        Map<Reactive, ISlotAccessorGetter> map = registeredSlotAccessors.get(mvvmRegistryName);
        if (map == null) return new HashMap<>();
        return map;
    }
    //</editor-fold>

    private final Map<String, Class<? extends ViewModel<?>>> viewModelClasses = new HashMap<>();

    public boolean isMvvmRegistered(String mvvmRegistryName) { return viewModelClasses.containsKey(mvvmRegistryName); }

    @Nullable
    public ViewModel<?> newViewModel(String mvvmRegistryName)
    {
        try { return viewModelClasses.get(mvvmRegistryName).newInstance(); }
        catch (Exception e) { return null; }
    }

    public boolean manualRegister(String mvvmRegistryName, Class<? extends ViewModel<?>> viewModelClass)
    {
        if (viewModelClasses.containsKey(mvvmRegistryName)) return false;

        InGameInfoReborn.LOGGER.info("Currently registering MVVM '" + mvvmRegistryName + "'.");

        viewModelClasses.put(mvvmRegistryName, viewModelClass);
        registeredReactiveObjects.put(mvvmRegistryName, RegistryUtils.findReactiveObjects(mvvmRegistryName, viewModelClass));
        registeredReactiveCollections.put(mvvmRegistryName, RegistryUtils.findReactiveCollections(mvvmRegistryName, viewModelClass));
        registeredSlotAccessors.put(mvvmRegistryName, RegistryUtils.findSlotAccessors(mvvmRegistryName, viewModelClass));

        return true;
    }

    public boolean autoRegister(String mvvmRegistryName, Class<? extends ViewModel<?>> viewModelClass)
    {
        if (!manualRegister(mvvmRegistryName, viewModelClass)) return false;
        cacheIgiGuiContainer(mvvmRegistryName, newViewModel(mvvmRegistryName));
        return true;
    }
}
