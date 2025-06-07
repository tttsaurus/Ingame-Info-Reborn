package com.tttsaurus.ingameinfo.common.core.internal;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.function.*;
import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLifecycleProvider;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.IStylePropertySyncTo;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.IReactiveCallback;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveCollection;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.SlotAccessor;
import com.tttsaurus.ingameinfo.common.core.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.core.gui.layout.MainGroup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class InternalMethods
{
    public static InternalMethods instance;

    public IFunc<GuiLayout> GuiLayout$constructor;

    public IFunc_1Param<MainGroup, GuiLayout> GuiLayout$mainGroup$getter;
    public IFunc_1Param<IgiGuiContainer, GuiLayout> GuiLayout$igiGuiContainer$getter;
    public IFunc_1Param<Map<String, IStylePropertySyncTo>, Element> Element$syncToMap$getter;
    public IFunc_1Param<List<IReactiveCallback>, ReactiveObject> ReactiveObject$initiativeCallbacks$getter;
    public IFunc_1Param<List<IReactiveCallback>, ReactiveObject> ReactiveObject$passiveCallbacks$getter;
    public IFunc_1Param<Map<String, IgiGuiContainer>, GuiLifecycleProvider> GuiLifecycleProvider$openedGuiMap$getter;
    public IFunc_1Param<List<SlotAccessor>, ViewModel> ViewModel$slotAccessors$getter;

    public IAction_2Param<IgiGuiContainer, ViewModel> IgiGuiContainer$viewModel$setter;
    public IAction_2Param<View, MainGroup> View$mainGroup$setter;
    public IAction_2Param<ViewModel, IAction_1Param<Boolean>> ViewModel$isActiveSetter$setter;
    public IAction_2Param<ViewModel, IFunc<Boolean>> ViewModel$isActiveGetter$setter;
    public IAction_2Param<ViewModel, IAction_1Param<IFunc<Boolean>>> ViewModel$exitCallbackSetter$setter;
    public IAction_2Param<ViewModel, IAction_1Param<Boolean>> ViewModel$isFocusedSetter$setter;
    public IAction_2Param<ViewModel, IFunc<Boolean>> ViewModel$isFocusedGetter$setter;
    public IAction_2Param<ReactiveCollection, ElementGroup> ReactiveCollection$group$setter;
    public IAction_2Param<SlotAccessor, ElementGroup> SlotAccessor$group$setter;
    public IAction_2Param<GuiLayout, IgiGuiContainer> GuiLayout$igiGuiContainer$setter;

    public IFunc_2Param<GuiLayout, ViewModel, String> ViewModel$init;
    public IFunc_2Param<GuiLayout, View, IgiGuiContainer> View$init;

    public InternalMethods()
    {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        //<editor-fold desc="getters">
        try
        {
            Constructor<GuiLayout> constructor = GuiLayout.class.getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            MethodHandle handle = lookup.unreflectConstructor(constructor);
            GuiLayout$constructor = () ->
            {
                try
                {
                    return (GuiLayout)handle.invoke();
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            GuiLayout$constructor = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for GuiLayout$constructor: " + exception.getMessage());
        }

        try
        {
            Field field = GuiLayout.class.getDeclaredField("mainGroup");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(field);
            GuiLayout$mainGroup$getter = (arg0) ->
            {
                try
                {
                    return (MainGroup)handle.invoke(arg0);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            GuiLayout$mainGroup$getter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for GuiLayout$mainGroup$getter: " + exception.getMessage());
        }

        try
        {
            Field field = GuiLayout.class.getDeclaredField("igiGuiContainer");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(field);
            GuiLayout$igiGuiContainer$getter = (arg0) ->
            {
                try
                {
                    return (IgiGuiContainer)handle.invoke(arg0);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            GuiLayout$igiGuiContainer$getter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for GuiLayout$igiGuiContainer$getter: " + exception.getMessage());
        }

        try
        {
            Field field = Element.class.getDeclaredField("syncToMap");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(field);
            Element$syncToMap$getter = (arg0) ->
            {
                try
                {
                    return (Map<String, IStylePropertySyncTo>)handle.invoke(arg0);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            Element$syncToMap$getter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for Element$syncToMap$getter: " + exception.getMessage());
        }

        try
        {
            Field field = ReactiveObject.class.getDeclaredField("initiativeCallbacks");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(field);
            ReactiveObject$initiativeCallbacks$getter = (arg0) ->
            {
                try
                {
                    return (List<IReactiveCallback>)handle.invoke(arg0);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            ReactiveObject$initiativeCallbacks$getter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ReactiveObject$initiativeCallbacks$getter: " + exception.getMessage());
        }

        try
        {
            Field field = ReactiveObject.class.getDeclaredField("passiveCallbacks");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(field);
            ReactiveObject$passiveCallbacks$getter = (arg0) ->
            {
                try
                {
                    return (List<IReactiveCallback>)handle.invoke(arg0);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            ReactiveObject$passiveCallbacks$getter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ReactiveObject$passiveCallbacks$getter: " + exception.getMessage());
        }

        try
        {
            Field field = GuiLifecycleProvider.class.getDeclaredField("openedGuiMap");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(field);
            GuiLifecycleProvider$openedGuiMap$getter = (arg0) ->
            {
                try
                {
                    return (Map<String, IgiGuiContainer>)handle.invoke(arg0);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            GuiLifecycleProvider$openedGuiMap$getter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for GuiLifecycleProvider$openedGuiMap$getter: " + exception.getMessage());
        }

        try
        {
            Field field = ViewModel.class.getDeclaredField("slotAccessors");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(field);
            ViewModel$slotAccessors$getter = (arg0) ->
            {
                try
                {
                    return (List<SlotAccessor>)handle.invoke(arg0);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            ViewModel$slotAccessors$getter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ViewModel$slotAccessors$getter: " + exception.getMessage());
        }
        //</editor-fold>

        //<editor-fold desc="setters">
        try
        {
            Field field = IgiGuiContainer.class.getDeclaredField("viewModel");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            IgiGuiContainer$viewModel$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            IgiGuiContainer$viewModel$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for IgiGuiContainer$viewModel$setter: " + exception.getMessage());
        }

        try
        {
            Field field = View.class.getDeclaredField("mainGroup");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            View$mainGroup$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            View$mainGroup$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for View$mainGroup$setter: " + exception.getMessage());
        }

        try
        {
            Field field = ViewModel.class.getDeclaredField("isActiveSetter");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            ViewModel$isActiveSetter$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            ViewModel$isActiveSetter$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ViewModel$isActiveSetter$setter: " + exception.getMessage());
        }

        try
        {
            Field field = ViewModel.class.getDeclaredField("isActiveGetter");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            ViewModel$isActiveGetter$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            ViewModel$isActiveGetter$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ViewModel$isActiveGetter$setter: " + exception.getMessage());
        }

        try
        {
            Field field = ViewModel.class.getDeclaredField("exitCallbackSetter");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            ViewModel$exitCallbackSetter$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            ViewModel$exitCallbackSetter$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ViewModel$exitCallbackSetter$setter: " + exception.getMessage());
        }

        try
        {
            Field field = ViewModel.class.getDeclaredField("isFocusedSetter");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            ViewModel$isFocusedSetter$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            ViewModel$isFocusedSetter$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ViewModel$isFocusedSetter$setter: " + exception.getMessage());
        }

        try
        {
            Field field = ViewModel.class.getDeclaredField("isFocusedGetter");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            ViewModel$isFocusedGetter$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            ViewModel$isFocusedGetter$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ViewModel$isFocusedGetter$setter: " + exception.getMessage());
        }

        try
        {
            Field field = ReactiveCollection.class.getDeclaredField("group");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            ReactiveCollection$group$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            ReactiveCollection$group$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ReactiveCollection$group$setter: " + exception.getMessage());
        }

        try
        {
            Field field = SlotAccessor.class.getDeclaredField("group");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            SlotAccessor$group$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            SlotAccessor$group$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for SlotAccessor$group$setter: " + exception.getMessage());
        }

        try
        {
            Field field = GuiLayout.class.getDeclaredField("igiGuiContainer");
            field.setAccessible(true);
            MethodHandle handle = lookup.unreflectSetter(field);
            GuiLayout$igiGuiContainer$setter = (arg0, arg1) ->
            {
                try
                {
                    handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { }
            };
        }
        catch (Exception exception)
        {
            GuiLayout$igiGuiContainer$setter = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for GuiLayout$igiGuiContainer$setter: " + exception.getMessage());
        }
        //</editor-fold>

        //<editor-fold desc="methods">
        try
        {
            Method method = ViewModel.class.getDeclaredMethod("init", String.class);
            method.setAccessible(true);
            MethodHandle handle = lookup.unreflect(method);
            ViewModel$init = (arg0, arg1) ->
            {
                try
                {
                    return (GuiLayout)handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            ViewModel$init = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for ViewModel$init: " + exception.getMessage());
        }

        try
        {
            Method method = View.class.getDeclaredMethod("init", IgiGuiContainer.class);
            method.setAccessible(true);
            MethodHandle handle = lookup.unreflect(method);
            View$init = (arg0, arg1) ->
            {
                try
                {
                    return (GuiLayout)handle.invoke(arg0, arg1);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            View$init = null;
            InGameInfoReborn.LOGGER.error("Reflection setup failed for View$init: " + exception.getMessage());
        }
        //</editor-fold>
    }
}
