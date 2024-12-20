package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.IgiGui;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class VVMBinding<T extends View>
{
    private T view;
    private ViewModel<T> viewModel;

    public GuiLayout init(ViewModel<T> viewModel)
    {
        this.viewModel = viewModel;
        Class<?> viewClass = (Class<?>)((ParameterizedType)viewModel.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try
        {
            view = (T)viewClass.newInstance();
        }
        catch (Exception ignored) { }

        GuiLayout guiLayout = IgiGui.getBuilder();
        view.init(guiLayout);
        MainGroup mainGroup = null;
        try
        {
            Field field = guiLayout.getClass().getDeclaredField("mainGroup");
            field.setAccessible(true);
            mainGroup = (MainGroup)field.get(guiLayout);
        }
        catch (Exception ignored) { }
        try
        {
            Field field = view.getClass().getSuperclass().getDeclaredField("mainGroup");
            field.setAccessible(true);
            field.set(view, mainGroup);
        }
        catch (Exception ignored) { }

        return guiLayout;
    }
}
