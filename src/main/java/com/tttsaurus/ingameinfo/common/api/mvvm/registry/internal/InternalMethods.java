package com.tttsaurus.ingameinfo.common.api.mvvm.registry.internal;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class InternalMethods
{
    public IFunc<GuiLayout> GuiLayout$constructor;
    public IFunc_1Param<MainGroup, GuiLayout> GuiLayout$mainGroup$getter;
    public IAction_2Param<View, MainGroup> View$mainGroup$setter;

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
        catch (Exception ignored) { GuiLayout$constructor = null; }
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
        catch (Exception ignored) { GuiLayout$mainGroup$getter = null; }
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
        catch (Exception ignored) { View$mainGroup$setter = null; }
    }
}
