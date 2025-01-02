package com.tttsaurus.ingameinfo.common.impl.mvvm.registry;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.IReactiveObjectGetter;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.registry.RegistryUtils;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
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
        IgiGuiContainer igiGuiContainer = igiGuiContainerCache.get(mvvmRegistryName);
        if (igiGuiContainer == null)
        {
            ViewModel<?> viewModel = newViewModel(mvvmRegistryName);
            if (viewModel == null) return null;
            GuiLayout guiLayout = InternalMethods.instance.ViewModel$init.invoke(viewModel);
            igiGuiContainer = InternalMethods.instance.GuiLayout$igiGuiContainer$getter.invoke(guiLayout);
            InternalMethods.instance.IgiGuiContainer$viewModel$setter.invoke(igiGuiContainer, viewModel);
            igiGuiContainerCache.put(mvvmRegistryName, igiGuiContainer);
        }
        return igiGuiContainer;
    }

    // key: view model class name
    private static final Map<String, Map<Reactive, IReactiveObjectGetter>> registeredReactiveObjects = new HashMap<>();

    public static Map<Reactive, IReactiveObjectGetter> getRegisteredReactiveObjects(Class<? extends ViewModel> clazz)
    {
        Map<Reactive, IReactiveObjectGetter> map = registeredReactiveObjects.get(clazz.getName());
        if (map == null) return new HashMap<>();
        return map;
    }

    private static final Map<String, Class<? extends ViewModel<?>>> viewModelClasses = new HashMap<>();

    public static boolean isMvvmRegistered(String mvvmRegistryName) { return viewModelClasses.containsKey(mvvmRegistryName); }

    @Nullable
    private static ViewModel<?> newViewModel(String mvvmRegistryName)
    {
        try { return viewModelClasses.get(mvvmRegistryName).newInstance(); }
        catch (Exception e) { return null; }
    }

    public static boolean register(String mvvmRegistryName, Class<? extends ViewModel<?>> viewModelClass)
    {
        if (viewModelClasses.containsKey(mvvmRegistryName)) return false;
        for (Map.Entry<String, Class<? extends ViewModel<?>>> entry: viewModelClasses.entrySet())
            if (entry.getValue().getName().equals(viewModelClass.getName())) return false;

        viewModelClasses.put(mvvmRegistryName, viewModelClass);
        registeredReactiveObjects.put(viewModelClass.getName(), RegistryUtils.findReactiveObjects(viewModelClass));

        // to init the cache
        getIgiGuiContainer(mvvmRegistryName);

        return true;
    }
}
