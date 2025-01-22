package com.tttsaurus.ingameinfo.common.api.internal;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.api.function.IAction_2Param;
import com.tttsaurus.ingameinfo.common.api.function.IFunc;
import com.tttsaurus.ingameinfo.common.api.function.IFunc_1Param;
import com.tttsaurus.ingameinfo.common.api.function.IFunc_2Param;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySyncTo;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@SuppressWarnings("all")
public class InternalMethods
{
    public static InternalMethods instance;

    public IFunc<GuiLayout> GuiLayout$constructor;
    public IFunc_1Param<MainGroup, GuiLayout> GuiLayout$mainGroup$getter;
    public IFunc_1Param<IgiGuiContainer, GuiLayout> GuiLayout$igiGuiContainer$getter;
    public IAction_2Param<IgiGuiContainer, ViewModel> IgiGuiContainer$viewModel$setter;
    public IAction_2Param<View, MainGroup> View$mainGroup$setter;
    public IFunc_2Param<GuiLayout, ViewModel, String> ViewModel$init;
    public IFunc_1Param<GuiLayout, View> View$init;
    public IFunc_1Param<Map<String, IStylePropertySyncTo>, Element> Element$syncToMap;

    public InternalMethods()
    {
        try
        {
            Constructor<GuiLayout> constructor = GuiLayout.class.getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            GuiLayout$constructor = () ->
            {
                try
                {
                    return constructor.newInstance();
                }
                catch (Exception ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            GuiLayout$constructor = null;
            InGameInfoReborn.logger.info("Reflection setup failed for GuiLayout$constructor: " + exception.getMessage());
        }

        try
        {
            Field field = GuiLayout.class.getDeclaredField("mainGroup");
            field.setAccessible(true);
            GuiLayout$mainGroup$getter = (arg0) ->
            {
                try
                {
                    return (MainGroup)field.get(arg0);
                }
                catch (Exception ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            GuiLayout$mainGroup$getter = null;
            InGameInfoReborn.logger.info("Reflection setup failed for GuiLayout$mainGroup$getter: " + exception.getMessage());
        }

        try
        {
            Field field = GuiLayout.class.getDeclaredField("igiGuiContainer");
            field.setAccessible(true);
            GuiLayout$igiGuiContainer$getter = (arg0) ->
            {
                try
                {
                    return (IgiGuiContainer)field.get(arg0);
                }
                catch (Exception ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            GuiLayout$igiGuiContainer$getter = null;
            InGameInfoReborn.logger.info("Reflection setup failed for GuiLayout$igiGuiContainer$getter: " + exception.getMessage());
        }

        try
        {
            Field field = IgiGuiContainer.class.getDeclaredField("viewModel");
            field.setAccessible(true);
            IgiGuiContainer$viewModel$setter = (arg0, arg1) ->
            {
                try
                {
                    field.set(arg0, arg1);
                }
                catch (Exception ignored) { }
            };
        }
        catch (Exception exception)
        {
            IgiGuiContainer$viewModel$setter = null;
            InGameInfoReborn.logger.info("Reflection setup failed for IgiGuiContainer$viewModel$setter: " + exception.getMessage());
        }

        try
        {
            Field field = View.class.getDeclaredField("mainGroup");
            field.setAccessible(true);
            View$mainGroup$setter = (arg0, arg1) ->
            {
                try
                {
                    field.set(arg0, arg1);
                }
                catch (Exception ignored) { }
            };
        }
        catch (Exception exception)
        {
            View$mainGroup$setter = null;
            InGameInfoReborn.logger.info("Reflection setup failed for View$mainGroup$setter: " + exception.getMessage());
        }

        try
        {
            Method method = ViewModel.class.getDeclaredMethod("init", String.class);
            method.setAccessible(true);
            ViewModel$init = (arg0, arg1) ->
            {
                try
                {
                    return (GuiLayout)method.invoke(arg0, arg1);
                }
                catch (Exception ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            ViewModel$init = null;
            InGameInfoReborn.logger.info("Reflection setup failed for ViewModel$init: " + exception.getMessage());
        }

        try
        {
            Method method = View.class.getDeclaredMethod("init", new Class[0]);
            method.setAccessible(true);
            View$init = (arg0) ->
            {
                try
                {
                    return (GuiLayout)method.invoke(arg0, new Object[0]);
                }
                catch (Exception ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            View$init = null;
            InGameInfoReborn.logger.info("Reflection setup failed for View$init: " + exception.getMessage());
        }

        try
        {
            Field field = Element.class.getDeclaredField("syncToMap");
            field.setAccessible(true);
            Element$syncToMap = (arg0) ->
            {
                try
                {
                    return (Map<String, IStylePropertySyncTo>)field.get(arg0);
                }
                catch (Exception ignored) { return null; }
            };
        }
        catch (Exception exception)
        {
            Element$syncToMap = null;
            InGameInfoReborn.logger.info("Reflection setup failed for Element$syncToMap: " + exception.getMessage());
        }
    }
}
