package com.tttsaurus.ingameinfo.common.core.mvvm.registry;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.core.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.IReactiveCollectionGetter;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.IReactiveObjectGetter;
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
    private static final Map<String, IgiGuiContainer> igiGuiContainerCache = new HashMap<>();

    @Nullable
    public static IgiGuiContainer getIgiGuiContainer(String mvvmRegistryName)
    {
        if (!isMvvmRegistered(mvvmRegistryName)) return null;
        return igiGuiContainerCache.get(mvvmRegistryName);
    }
    public static void setIgiGuiContainer(String mvvmRegistryName, @Nonnull ViewModel<?> viewModel)
    {
        GuiLayout guiLayout = InternalMethods.instance.ViewModel$init.invoke(viewModel, mvvmRegistryName);
        IgiGuiContainer igiGuiContainer = InternalMethods.instance.GuiLayout$igiGuiContainer$getter.invoke(guiLayout);
        InternalMethods.instance.IgiGuiContainer$viewModel$setter.invoke(igiGuiContainer, viewModel);
        igiGuiContainerCache.put(mvvmRegistryName, igiGuiContainer);
    }

    // key: mvvm registry name
    private static final Map<String, Map<Reactive, IReactiveObjectGetter>> registeredReactiveObjects = new HashMap<>();
    private static final Map<String, Map<Reactive, IReactiveCollectionGetter>> registeredReactiveCollections = new HashMap<>();

    public static Map<Reactive, IReactiveObjectGetter> getRegisteredReactiveObjects(String mvvmRegistryName)
    {
        Map<Reactive, IReactiveObjectGetter> map = registeredReactiveObjects.get(mvvmRegistryName);
        if (map == null) return new HashMap<>();
        return map;
    }
    public static Map<Reactive, IReactiveCollectionGetter> getRegisteredReactiveCollections(String mvvmRegistryName)
    {
        Map<Reactive, IReactiveCollectionGetter> map = registeredReactiveCollections.get(mvvmRegistryName);
        if (map == null) return new HashMap<>();
        return map;
    }

    private static final Map<String, Class<? extends ViewModel<?>>> viewModelClasses = new HashMap<>();

    public static boolean isMvvmRegistered(String mvvmRegistryName) { return viewModelClasses.containsKey(mvvmRegistryName); }

    @Nullable
    public static ViewModel<?> newViewModel(String mvvmRegistryName)
    {
        try { return viewModelClasses.get(mvvmRegistryName).newInstance(); }
        catch (Exception e) { return null; }
    }

    // only to be called under MvvmRegisterEvent
    public static boolean manualRegister(String mvvmRegistryName, Class<? extends ViewModel<?>> viewModelClass)
    {
        if (viewModelClasses.containsKey(mvvmRegistryName)) return false;

        InGameInfoReborn.logger.info("Currently registering '" + mvvmRegistryName + "'.");

        viewModelClasses.put(mvvmRegistryName, viewModelClass);
        registeredReactiveObjects.put(mvvmRegistryName, RegistryUtils.findReactiveObjects(mvvmRegistryName, viewModelClass));
        registeredReactiveCollections.put(mvvmRegistryName, RegistryUtils.findReactiveCollections(mvvmRegistryName, viewModelClass));

        return true;
    }

    // only to be called under MvvmRegisterEvent
    public static boolean autoRegister(String mvvmRegistryName, Class<? extends ViewModel<?>> viewModelClass)
    {
        if (!manualRegister(mvvmRegistryName, viewModelClass)) return false;
        setIgiGuiContainer(mvvmRegistryName, newViewModel(mvvmRegistryName));
        return true;
    }
}
