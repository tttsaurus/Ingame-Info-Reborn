package com.tttsaurus.ingameinfo.common.api.internal;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("all")
public class InternalMethods
{
    public static InternalMethods instance;

    public IFunc<GuiLayout> GuiLayout$constructor;
    public IFunc_1Param<MainGroup, GuiLayout> GuiLayout$mainGroup$getter;
    public IFunc_1Param<IgiGuiContainer, GuiLayout> GuiLayout$igiGuiContainer$getter;
    public IAction_2Param<IgiGuiContainer, ViewModel> IgiGuiContainer$viewModel$setter;
    public IAction_2Param<View, MainGroup> View$mainGroup$setter;
    public IFunc_1Param<GuiLayout, ViewModel> ViewModel$init;
    public IFunc_1Param<GuiLayout, View> View$init;

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
            InGameInfoReborn.LOGGER.info("Reflection setup failed for GuiLayout$constructor: " + exception.getMessage());
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
            InGameInfoReborn.LOGGER.info("Reflection setup failed for GuiLayout$mainGroup$getter: " + exception.getMessage());
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
            InGameInfoReborn.LOGGER.info("Reflection setup failed for GuiLayout$igiGuiContainer$getter: " + exception.getMessage());
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
            InGameInfoReborn.LOGGER.info("Reflection setup failed for IgiGuiContainer$viewModel$setter: " + exception.getMessage());
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
            InGameInfoReborn.LOGGER.info("Reflection setup failed for View$mainGroup$setter: " + exception.getMessage());
        }

        try
        {
            Method method = ViewModel.class.getDeclaredMethod("init", new Class[0]);
            method.setAccessible(true);
            ViewModel$init = (arg0) ->
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
            ViewModel$init = null;
            InGameInfoReborn.LOGGER.info("Reflection setup failed for ViewModel$init: " + exception.getMessage());
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
            InGameInfoReborn.LOGGER.info("Reflection setup failed for View$init: " + exception.getMessage());
        }

        InGameInfoReborn.LOGGER.info("Reflection setup finished.");
    }
}
