package com.tttsaurus.ingameinfo.common.impl.mvvm.registry;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.IReactiveObjectGetter;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.registry.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.api.mvvm.registry.RegistryUtils;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class MvvmRegistry
{
    // key: view model class name
    private static final Map<String, Map<Reactive, IReactiveObjectGetter>> registeredReactiveObjects = new HashMap<>();

    public static Map<Reactive, IReactiveObjectGetter> getRegisteredReactiveObjects(Class<? extends ViewModel> clazz)
    {
        Map<Reactive, IReactiveObjectGetter> map = registeredReactiveObjects.get(clazz.getName());
        if (map == null) return new HashMap<>();
        return map;
    }

    public static final InternalMethods internalMethods = new InternalMethods();

    private static final Map<String, Class<? extends ViewModel<?>>> viewModelClasses = new HashMap<>();

    public static boolean isMvvmRegistered(String mvvmRegistryName) { return viewModelClasses.containsKey(mvvmRegistryName); }

    @Nullable
    public static ViewModel<?> newViewModel(String mvvmRegistryName)
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

        return true;
    }
}
