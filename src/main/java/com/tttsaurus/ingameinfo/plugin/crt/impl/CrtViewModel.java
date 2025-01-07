package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.api.reflection.AnnotationUtils;
import com.tttsaurus.ingameinfo.plugin.crt.api.TypesWrapper;
import com.tttsaurus.ingameinfo.plugin.crt.api.viewmodel.IViewModelStart;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.Tuple;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import java.util.HashMap;
import java.util.Map;

@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.ViewModel")
public final class CrtViewModel extends ViewModel<CrtView>
{
    public String runtimeMvvm;

    // key: mvvm registry name
    private static Map<String, IViewModelStart> startActions = new HashMap<>();

    @ZenMethod
    public static void setStartAction(IViewModelStart action) { startActions.put(CrtMvvm.currentMvvm, action); }

    // key: mvvm registry name
    public static final Map<String, Map<String, Tuple<Reactive, ReactiveObject<?>>>> reactiveObjectDefs = new HashMap<>();

    @ZenMethod
    public static ReactiveObjectWrapper registerReactiveObject(String fieldName, TypesWrapper typesWrapper, String targetUid, String property, @Optional boolean initiativeSync, @Optional boolean passiveSync)
    {
        Map<String, Tuple<Reactive, ReactiveObject<?>>> def = reactiveObjectDefs.computeIfAbsent(CrtMvvm.currentMvvm, k -> new HashMap<>());

        if (def.containsKey(fieldName)) return null;

        Map<String, Object> values = new HashMap<>();
        values.put("targetUid", targetUid);
        values.put("property", property);
        values.put("initiativeSync", initiativeSync);
        values.put("passiveSync", passiveSync);
        Reactive reactive = AnnotationUtils.createAnnotation(Reactive.class, values);

        ReactiveObjectWrapper wrapper = ReactiveObjectWrapper.instantiate(typesWrapper.types);

        def.put(fieldName, new Tuple<Reactive, ReactiveObject<?>>(reactive, wrapper.reactiveObject));

        return wrapper;
    }

    @ZenMethod
    public static ReactiveObjectWrapper getReactiveObject(String fieldName)
    {
        Map<String, Tuple<Reactive, ReactiveObject<?>>> def = reactiveObjectDefs.get(CrtMvvm.currentMvvm);
        if (def == null) return null;
        Tuple<Reactive, ReactiveObject<?>> tuple = def.get(fieldName);
        if (tuple == null) return null;
        return new ReactiveObjectWrapper(tuple.getSecond());
    }

    @Override
    public void start()
    {
        IViewModelStart action = startActions.get(runtimeMvvm);
        if (action != null) action.start();
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {

    }
}
