package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.impl.mvvm.registry.MvvmRegistry;
import java.lang.reflect.ParameterizedType;

public class VvmBinding<T extends View>
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

        GuiLayout guiLayout = MvvmRegistry.internalMethods.GuiLayout$constructor.invoke();
        view.init(guiLayout);
        MainGroup mainGroup = MvvmRegistry.internalMethods.GuiLayout$mainGroup$getter.invoke(guiLayout);
        MvvmRegistry.internalMethods.View$mainGroup$setter.invoke(view, mainGroup);

        return guiLayout;
    }

    public void bindReactiveObject(Reactive reactive, ReactiveObject<?> reactiveObject)
    {
        //InGameInfoReborn.LOGGER.info("bind: " + reactive.targetUid());
        //view.getElements(reactive.targetUid());
    }
}
