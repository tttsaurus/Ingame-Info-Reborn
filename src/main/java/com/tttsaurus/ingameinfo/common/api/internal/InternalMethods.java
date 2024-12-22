package com.tttsaurus.ingameinfo.common.api.internal;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
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
    public IAction_2Param<View, MainGroup> View$mainGroup$setter;
    public IFunc_1Param<GuiLayout, ViewModel> ViewModel$init;

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
            InGameInfoReborn.LOGGER.info("Reflection setup failed for GuiLayout$mainGroup$getter" + exception.getMessage());
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
            InGameInfoReborn.LOGGER.info("Reflection setup failed for View$mainGroup$setter" + exception.getMessage());
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
            InGameInfoReborn.LOGGER.info("Reflection setup failed for ViewModel$init" + exception.getMessage());
        }

        InGameInfoReborn.LOGGER.info("Reflection setup succeeded. Internal methods are ready");
    }
}
