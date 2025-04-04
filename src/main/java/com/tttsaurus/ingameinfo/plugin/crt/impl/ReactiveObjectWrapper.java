package com.tttsaurus.ingameinfo.plugin.crt.impl;

import com.tttsaurus.ingameinfo.common.api.animation.text.ITextAnimDef;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Skewness;
import com.tttsaurus.ingameinfo.common.api.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.plugin.crt.api.Types;
import com.tttsaurus.ingameinfo.plugin.crt.impl.types.*;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@SuppressWarnings("all")
@ZenRegister
@ZenClass("mods.ingameinfo.mvvm.ReactiveObject")
public final class ReactiveObjectWrapper
{
    protected ReactiveObject<?> reactiveObject;

    public ReactiveObjectWrapper(ReactiveObject<?> reactiveObject)
    {
        this.reactiveObject = reactiveObject;
    }

    private ReactiveObjectWrapper() { }

    public static ReactiveObjectWrapper instantiate(Types type)
    {
        ReactiveObjectWrapper wrapper = new ReactiveObjectWrapper();
        switch (type)
        {
            case Int -> wrapper.reactiveObject = new ReactiveObject<Integer>(){};
            case Long -> wrapper.reactiveObject = new ReactiveObject<Long>(){};
            case Short -> wrapper.reactiveObject = new ReactiveObject<Short>(){};
            case Byte -> wrapper.reactiveObject = new ReactiveObject<Byte>(){};
            case Double -> wrapper.reactiveObject = new ReactiveObject<Double>(){};
            case Float -> wrapper.reactiveObject = new ReactiveObject<Float>(){};
            case Char -> wrapper.reactiveObject = new ReactiveObject<Character>(){};
            case Boolean -> wrapper.reactiveObject = new ReactiveObject<Boolean>(){};
            case String -> wrapper.reactiveObject = new ReactiveObject<String>(){};
            case Alignment -> wrapper.reactiveObject = new ReactiveObject<Alignment>(){};
            case Padding -> wrapper.reactiveObject = new ReactiveObject<Padding>(){};
            case Pivot -> wrapper.reactiveObject = new ReactiveObject<Pivot>(){};
            case Skewness -> wrapper.reactiveObject = new ReactiveObject<Skewness>(){};
            case GhostableItem -> wrapper.reactiveObject = new ReactiveObject<GhostableItem>(){};
            case TextAnimDef -> wrapper.reactiveObject = new ReactiveObject<ITextAnimDef>(){};
        }
        return wrapper;
    }

    @ZenMethod
    public Object get()
    {
        Object obj = reactiveObject.get();

        if (obj instanceof Alignment alignment)
            return new AlignmentWrapper(alignment);
        if (obj instanceof Padding padding)
            return new PaddingWrapper(padding);
        if (obj instanceof Pivot pivot)
            return new PivotWrapper(pivot);
        if (obj instanceof Skewness skewness)
            return new SkewnessWrapper(skewness);
        if (obj instanceof GhostableItem ghostableItem)
            return new GhostableItemWrapper(ghostableItem);
        if (obj instanceof ITextAnimDef iTextAnimDef)
            return new TextAnimDefWrapper(iTextAnimDef);

        return obj;
    }
    @ZenMethod
    public void set(Object value)
    {
        Object input = value;

        if (value instanceof AlignmentWrapper alignmentWrapper)
            input = alignmentWrapper.alignment;
        if (value instanceof PaddingWrapper paddingWrapper)
            input = paddingWrapper.padding;
        if (value instanceof PivotWrapper pivotWrapper)
            input = pivotWrapper.pivot;
        if (value instanceof SkewnessWrapper skewnessWrapper)
            input = skewnessWrapper.skewness;
        if (value instanceof GhostableItemWrapper ghostableItemWrapper)
            input = ghostableItemWrapper.ghostableItem;
        if (value instanceof TextAnimDefWrapper textAnimDefWrapper)
            input = textAnimDefWrapper.iTextAnimDef;

        reactiveObject.set(input);
    }
}
