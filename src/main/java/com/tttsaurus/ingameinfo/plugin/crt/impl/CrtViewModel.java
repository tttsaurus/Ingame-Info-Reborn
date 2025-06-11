package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.core.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveCollection;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.core.reflection.AnnotationUtils;
import com.tttsaurus.ingameinfo.plugin.crt.api.TypesWrapper;
import com.tttsaurus.ingameinfo.plugin.crt.api.viewmodel.IGuiExit;
import com.tttsaurus.ingameinfo.plugin.crt.api.viewmodel.IViewModelFixedUpdate;
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

    //<editor-fold desc="static methods">

    // key: mvvm registry name
    private static final Map<String, IViewModelFixedUpdate> fixedUpdates = new HashMap<>();

    @ZenMethod
    public static void setFixedUpdate(IViewModelFixedUpdate update) { fixedUpdates.put(CrtMvvm.currentMvvm, update); }

    // key: mvvm registry name
    private static final Map<String, IViewModelStart> startActions = new HashMap<>();

    @ZenMethod
    public static void setStartAction(IViewModelStart action) { startActions.put(CrtMvvm.currentMvvm, action); }

    // key: mvvm registry name
    public static final Map<String, Map<String, Tuple<Reactive, ReactiveObject<?>>>> reactiveObjectDefs = new HashMap<>();
    public static final Map<String, Map<String, Tuple<Reactive, ReactiveCollection>>> reactiveCollectionDefs = new HashMap<>();

    @ZenMethod
    public static ReactiveObjectWrapper registerReactiveObject(String fieldName, TypesWrapper typesWrapper, String targetUid, String property, @Optional boolean initiativeSync, @Optional boolean passiveSync, @Optional(valueLong = -1) int ordinal)
    {
        Map<String, Tuple<Reactive, ReactiveObject<?>>> def = reactiveObjectDefs.computeIfAbsent(CrtMvvm.currentMvvm, k -> new HashMap<>());

        if (def.containsKey(fieldName)) return null;

        Map<String, Object> values = new HashMap<>();
        values.put("targetUid", targetUid);
        values.put("property", property);
        values.put("initiativeSync", initiativeSync);
        values.put("passiveSync", passiveSync);
        values.put("ordinal", ordinal);
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

    @ZenMethod
    public static ReactiveCollectionWrapper registerReactiveCollection(String fieldName, String targetUid, @Optional(valueLong = -1) int ordinal)
    {
        Map<String, Tuple<Reactive, ReactiveCollection>> def = reactiveCollectionDefs.computeIfAbsent(CrtMvvm.currentMvvm, k -> new HashMap<>());

        if (def.containsKey(fieldName)) return null;

        Map<String, Object> values = new HashMap<>();
        values.put("targetUid", targetUid);
        values.put("property", "");
        values.put("initiativeSync", false);
        values.put("passiveSync", false);
        values.put("ordinal", ordinal);
        Reactive reactive = AnnotationUtils.createAnnotation(Reactive.class, values);

        ReactiveCollectionWrapper wrapper = new ReactiveCollectionWrapper(new ReactiveCollection());

        def.put(fieldName, new Tuple<Reactive, ReactiveCollection>(reactive, wrapper.reactiveCollection));

        return wrapper;
    }

    @ZenMethod
    public static ReactiveCollectionWrapper getReactiveCollection(String fieldName)
    {
        Map<String, Tuple<Reactive, ReactiveCollection>> def = reactiveCollectionDefs.get(CrtMvvm.currentMvvm);
        if (def == null) return null;
        Tuple<Reactive, ReactiveCollection> tuple = def.get(fieldName);
        if (tuple == null) return null;
        return new ReactiveCollectionWrapper(tuple.getSecond());
    }
    //</editor-fold>

    @ZenMethod
    public void setActive(boolean flag) { super.setActive(flag); }
    @ZenMethod
    public boolean getActive() { return super.getActive(); }
    @ZenMethod
    public void setExitCallback(IGuiExit callback) { super.setExitCallback(callback::invoke); }
    @ZenMethod
    public void setFocused(boolean flag) { super.setFocused(flag);}
    @ZenMethod
    public boolean getFocused() { return super.getFocused(); }

    @Override
    public void onStart()
    {
        IViewModelStart action = startActions.get(runtimeMvvm);
        if (action != null) action.start(this);
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {
        IViewModelFixedUpdate fixedUpdate = fixedUpdates.get(runtimeMvvm);
        if (fixedUpdate != null) fixedUpdate.update(this, deltaTime);
    }
}
