package com.tttsaurus.ingameinfo.plugin.crt;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.api.reflection.AnnotationUtils;
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
    public static final Map<String, Tuple<Reactive, ReactiveObject<?>>> reactiveObjectDefs = new HashMap<>();

    @ZenMethod
    public static ReactiveObjectWrapper<?> addReactiveObject(String fieldName, TypesWrapper typesWrapper, String targetUid, String property, @Optional boolean initiativeSync, @Optional boolean passiveSync)
    {
        if (reactiveObjectDefs.containsKey(fieldName)) return null;

        Map<String, Object> values = new HashMap<>();
        values.put("targetUid", targetUid);
        values.put("property", property);
        values.put("initiativeSync", initiativeSync);
        values.put("passiveSync", passiveSync);
        Reactive reactive = AnnotationUtils.createAnnotation(Reactive.class, values);

        ReactiveObjectWrapper<?> wrapper = ReactiveObjectWrapper.instantiate(typesWrapper.types, typesWrapper.types.clazz);

        reactiveObjectDefs.put(fieldName, new Tuple<Reactive, ReactiveObject<?>>(reactive, wrapper.reactiveObject));

        return wrapper;
    }

    @Override
    public void start()
    {

    }
}
