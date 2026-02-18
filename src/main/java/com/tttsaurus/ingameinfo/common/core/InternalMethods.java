package com.tttsaurus.ingameinfo.common.core;

import com.cleanroommc.kirino.utils.ReflectionUtils;
import com.google.common.base.Preconditions;
import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertySyncTo;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveCallback;
import com.tttsaurus.ingameinfo.common.core.render.text.FormattedText;
import com.tttsaurus.ingameinfo.common.core.function.*;
import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLifecycleProvider;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.RenderDecorator;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.VisualBuilderAccessor;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveCollection;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.SlotAccessor;
import com.tttsaurus.ingameinfo.common.core.mvvm.context.SharedContext;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;
import com.tttsaurus.ingameinfo.common.core.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.core.gui.layout.MainGroup;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.Map;

public final class InternalMethods
{
    private final static ConstructorDelegate CONSTRUCTOR_DELEGATE;
    private final static GetterDelegate GETTER_DELEGATE;
    private final static SetterDelegate SETTER_DELEGATE;
    private final static MethodDelegate METHOD_DELEGATE;

    static
    {
        CONSTRUCTOR_DELEGATE = new ConstructorDelegate(
                ReflectionUtils.getConstructor(GuiLayout.class),
                ReflectionUtils.getConstructor(VisualBuilderAccessor.class),
                ReflectionUtils.getConstructor(FormattedText.class, String.class));

        Preconditions.checkNotNull(CONSTRUCTOR_DELEGATE.GuiLayout$constructor);
        Preconditions.checkNotNull(CONSTRUCTOR_DELEGATE.VisualBuilderAccessor$constructor);
        Preconditions.checkNotNull(CONSTRUCTOR_DELEGATE.FormattedText$constructor);

        GETTER_DELEGATE = new GetterDelegate(
                ReflectionUtils.getFieldGetter(GuiLayout.class, "mainGroup", MainGroup.class),
                ReflectionUtils.getFieldGetter(GuiLayout.class, "igiGuiContainer", IgiGuiContainer.class),
                ReflectionUtils.getFieldGetter(Element.class, "syncToMap", Map.class), // Map<String, IStylePropertySyncTo>
                ReflectionUtils.getFieldGetter(ReactiveObject.class, "initiativeCallbacks", List.class), // List<IReactiveCallback>
                ReflectionUtils.getFieldGetter(ReactiveObject.class, "passiveCallbacks", List.class), // List<IReactiveCallback>
                ReflectionUtils.getFieldGetter(GuiLifecycleProvider.class, "openedGuiMap", Map.class), // Map<String, IgiGuiContainer>
                ReflectionUtils.getFieldGetter(ViewModel.class, "slotAccessors", List.class), // List<SlotAccessor>
                ReflectionUtils.getFieldGetter(ViewModel.class, "sharedContext", SharedContext.class),
                ReflectionUtils.getFieldGetter(IgiRuntime.class, "instance", IgiRuntime.class));

        Preconditions.checkNotNull(GETTER_DELEGATE.GuiLayout$mainGroup$getter);
        Preconditions.checkNotNull(GETTER_DELEGATE.GuiLayout$igiGuiContainer$getter);
        Preconditions.checkNotNull(GETTER_DELEGATE.Element$syncToMap$getter);
        Preconditions.checkNotNull(GETTER_DELEGATE.ReactiveObject$initiativeCallbacks$getter);
        Preconditions.checkNotNull(GETTER_DELEGATE.ReactiveObject$passiveCallbacks$getter);
        Preconditions.checkNotNull(GETTER_DELEGATE.GuiLifecycleProvider$openedGuiMap$getter);
        Preconditions.checkNotNull(GETTER_DELEGATE.ViewModel$slotAccessors$getter);
        Preconditions.checkNotNull(GETTER_DELEGATE.ViewModel$sharedContext$getter);
        Preconditions.checkNotNull(GETTER_DELEGATE.IgiRuntime$instance$getter);

        SETTER_DELEGATE = new SetterDelegate(
                ReflectionUtils.getFieldSetter(IgiGuiContainer.class, "viewModel", ViewModel.class),
                ReflectionUtils.getFieldSetter(View.class, "mainGroup", MainGroup.class),
                ReflectionUtils.getFieldSetter(ViewModel.class, "isActiveSetter", Action1Param.class),
                ReflectionUtils.getFieldSetter(ViewModel.class, "isActiveGetter", Func.class),
                ReflectionUtils.getFieldSetter(ViewModel.class, "exitCallbackSetter", Action1Param.class),
                ReflectionUtils.getFieldSetter(ViewModel.class, "isFocusedSetter", Action1Param.class),
                ReflectionUtils.getFieldSetter(ViewModel.class, "isFocusedGetter", Func.class),
                ReflectionUtils.getFieldSetter(ReactiveCollection.class, "group", ElementGroup.class),
                ReflectionUtils.getFieldSetter(SlotAccessor.class, "group", ElementGroup.class),
                ReflectionUtils.getFieldSetter(GuiLayout.class, "igiGuiContainer", IgiGuiContainer.class),
                ReflectionUtils.getFieldSetter(Element.class, "parent", ElementGroup.class),
                ReflectionUtils.getFieldSetter(View.class, "renderDecorator", RenderDecorator.class),
                ReflectionUtils.getFieldSetter(GuiLifecycleProvider.class, "lifecycleHolderName", String.class));

        Preconditions.checkNotNull(SETTER_DELEGATE.IgiGuiContainer$viewModel$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.View$mainGroup$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.ViewModel$isActiveSetter$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.ViewModel$isActiveGetter$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.ViewModel$exitCallbackSetter$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.ViewModel$isFocusedSetter$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.ViewModel$isFocusedGetter$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.ReactiveCollection$group$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.SlotAccessor$group$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.GuiLayout$igiGuiContainer$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.Element$parent$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.View$renderDecorator$setter);
        Preconditions.checkNotNull(SETTER_DELEGATE.GuiLifecycleProvider$lifecycleHolderName$setter);

        METHOD_DELEGATE = new MethodDelegate(
                ReflectionUtils.getMethod(ViewModel.class, "init", GuiLayout.class, String.class, MvvmRegistry.class),
                ReflectionUtils.getMethod(View.class, "init", GuiLayout.class, IgiGuiContainer.class),
                ReflectionUtils.getMethod(ViewModel.class, "getRenderDecorator", RenderDecorator.class),
                ReflectionUtils.getMethod(IgiRuntime.class, "init", void.class));

        Preconditions.checkNotNull(METHOD_DELEGATE.ViewModel$init);
        Preconditions.checkNotNull(METHOD_DELEGATE.View$init);
        Preconditions.checkNotNull(METHOD_DELEGATE.ViewModel$getRenderDecorator);
        Preconditions.checkNotNull(METHOD_DELEGATE.IgiRuntime$init);

        InGameInfoReborn.LOGGER.info("IGI InternalMethods' reflection setup initialized.");
    }

    record ConstructorDelegate(
            MethodHandle GuiLayout$constructor,
            MethodHandle VisualBuilderAccessor$constructor,
            MethodHandle FormattedText$constructor) { }

    record GetterDelegate(
            MethodHandle GuiLayout$mainGroup$getter,
            MethodHandle GuiLayout$igiGuiContainer$getter,
            MethodHandle Element$syncToMap$getter,
            MethodHandle ReactiveObject$initiativeCallbacks$getter,
            MethodHandle ReactiveObject$passiveCallbacks$getter,
            MethodHandle GuiLifecycleProvider$openedGuiMap$getter,
            MethodHandle ViewModel$slotAccessors$getter,
            MethodHandle ViewModel$sharedContext$getter,
            MethodHandle IgiRuntime$instance$getter) { }

    record SetterDelegate(
            MethodHandle IgiGuiContainer$viewModel$setter,
            MethodHandle View$mainGroup$setter,
            MethodHandle ViewModel$isActiveSetter$setter,
            MethodHandle ViewModel$isActiveGetter$setter,
            MethodHandle ViewModel$exitCallbackSetter$setter,
            MethodHandle ViewModel$isFocusedSetter$setter,
            MethodHandle ViewModel$isFocusedGetter$setter,
            MethodHandle ReactiveCollection$group$setter,
            MethodHandle SlotAccessor$group$setter,
            MethodHandle GuiLayout$igiGuiContainer$setter,
            MethodHandle Element$parent$setter,
            MethodHandle View$renderDecorator$setter,
            MethodHandle GuiLifecycleProvider$lifecycleHolderName$setter) { }

    record MethodDelegate(
            MethodHandle ViewModel$init,
            MethodHandle View$init,
            MethodHandle ViewModel$getRenderDecorator,
            MethodHandle IgiRuntime$init) { }

    //<editor-fold desc="constructors">
    public static GuiLayout GuiLayout$constructor()
    {
        try
        {
            return (GuiLayout) CONSTRUCTOR_DELEGATE.GuiLayout$constructor.invokeExact();
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static VisualBuilderAccessor VisualBuilderAccessor$constructor()
    {
        try
        {
            return (VisualBuilderAccessor) CONSTRUCTOR_DELEGATE.VisualBuilderAccessor$constructor.invokeExact();
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static FormattedText FormattedText$constructor(String arg0)
    {
        try
        {
            return (FormattedText) CONSTRUCTOR_DELEGATE.FormattedText$constructor.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }
    //</editor-fold>

    //<editor-fold desc="getters">
    public static MainGroup GuiLayout$mainGroup$getter(GuiLayout arg0)
    {
        try
        {
            return (MainGroup) GETTER_DELEGATE.GuiLayout$mainGroup$getter.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static IgiGuiContainer GuiLayout$igiGuiContainer$getter(GuiLayout arg0)
    {
        try
        {
            return (IgiGuiContainer) GETTER_DELEGATE.GuiLayout$igiGuiContainer$getter.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, StylePropertySyncTo> Element$syncToMap$getter(Element arg0)
    {
        try
        {
            return (Map<String, StylePropertySyncTo>) GETTER_DELEGATE.Element$syncToMap$getter.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<ReactiveCallback> ReactiveObject$initiativeCallbacks$getter(ReactiveObject arg0)
    {
        try
        {
            return (List<ReactiveCallback>) GETTER_DELEGATE.ReactiveObject$initiativeCallbacks$getter.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<ReactiveCallback> ReactiveObject$passiveCallbacks$getter(ReactiveObject arg0)
    {
        try
        {
            return (List<ReactiveCallback>) GETTER_DELEGATE.ReactiveObject$passiveCallbacks$getter.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, IgiGuiContainer> GuiLifecycleProvider$openedGuiMap$getter(GuiLifecycleProvider arg0)
    {
        try
        {
            return (Map<String, IgiGuiContainer>) GETTER_DELEGATE.GuiLifecycleProvider$openedGuiMap$getter.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<SlotAccessor> ViewModel$slotAccessors$getter(ViewModel arg0)
    {
        try
        {
            return (List<SlotAccessor>) GETTER_DELEGATE.ViewModel$slotAccessors$getter.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static SharedContext ViewModel$sharedContext$getter(ViewModel arg0)
    {
        try
        {
            return (SharedContext) GETTER_DELEGATE.ViewModel$sharedContext$getter.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static IgiRuntime IgiRuntime$instance$getter()
    {
        try
        {
            return (IgiRuntime) GETTER_DELEGATE.IgiRuntime$instance$getter.invokeExact();
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }
    //</editor-fold>

    //<editor-fold desc="setters">
    public static void IgiGuiContainer$viewModel$setter(IgiGuiContainer arg0, ViewModel arg1)
    {
        try
        {
            SETTER_DELEGATE.IgiGuiContainer$viewModel$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void View$mainGroup$setter(View arg0, MainGroup arg1)
    {
        try
        {
            SETTER_DELEGATE.View$mainGroup$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void ViewModel$isActiveSetter$setter(ViewModel arg0, Action1Param<Boolean> arg1)
    {
        try
        {
            SETTER_DELEGATE.ViewModel$isActiveSetter$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void ViewModel$isActiveGetter$setter(ViewModel arg0, Func<Boolean> arg1)
    {
        try
        {
            SETTER_DELEGATE.ViewModel$isActiveGetter$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void ViewModel$exitCallbackSetter$setter(ViewModel arg0, Action1Param<Func<Boolean>> arg1)
    {
        try
        {
            SETTER_DELEGATE.ViewModel$exitCallbackSetter$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void ViewModel$isFocusedSetter$setter(ViewModel arg0, Action1Param<Boolean> arg1)
    {
        try
        {
            SETTER_DELEGATE.ViewModel$isFocusedSetter$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void ViewModel$isFocusedGetter$setter(ViewModel arg0, Func<Boolean> arg1)
    {
        try
        {
            SETTER_DELEGATE.ViewModel$isFocusedGetter$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void ReactiveCollection$group$setter(ReactiveCollection arg0, ElementGroup arg1)
    {
        try
        {
            SETTER_DELEGATE.ReactiveCollection$group$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void SlotAccessor$group$setter(SlotAccessor arg0, ElementGroup arg1)
    {
        try
        {
            SETTER_DELEGATE.SlotAccessor$group$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void GuiLayout$igiGuiContainer$setter(GuiLayout arg0, IgiGuiContainer arg1)
    {
        try
        {
            SETTER_DELEGATE.GuiLayout$igiGuiContainer$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void Element$parent$setter(Element arg0, ElementGroup arg1)
    {
        try
        {
            SETTER_DELEGATE.Element$parent$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void View$renderDecorator$setter(View arg0, RenderDecorator arg1)
    {
        try
        {
            SETTER_DELEGATE.View$renderDecorator$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void GuiLifecycleProvider$lifecycleHolderName$setter(GuiLifecycleProvider arg0, String arg1)
    {
        try
        {
            SETTER_DELEGATE.GuiLifecycleProvider$lifecycleHolderName$setter.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }
    //</editor-fold>

    //<editor-fold desc="methods">
    public static GuiLayout ViewModel$init(ViewModel arg0, String arg1, MvvmRegistry arg2)
    {
        try
        {
            return (GuiLayout) METHOD_DELEGATE.ViewModel$init.invokeExact(arg0, arg1, arg2);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static GuiLayout View$init(View arg0, IgiGuiContainer arg1)
    {
        try
        {
            return (GuiLayout) METHOD_DELEGATE.View$init.invokeExact(arg0, arg1);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static RenderDecorator ViewModel$getRenderDecorator(ViewModel arg0)
    {
        try
        {
            return (RenderDecorator) METHOD_DELEGATE.ViewModel$getRenderDecorator.invokeExact(arg0);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void IgiRuntime$init()
    {
        try
        {
            METHOD_DELEGATE.IgiRuntime$init.invokeExact();
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }
    //</editor-fold>
}
