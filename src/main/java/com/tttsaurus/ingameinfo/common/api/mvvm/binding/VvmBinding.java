package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallback;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class VvmBinding<TView extends View>
{
    private TView view;

    public GuiLayout init(ViewModel<TView> viewModel)
    {
        Class<?> viewClass = (Class<?>)((ParameterizedType)viewModel.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try
        {
            view = (TView)viewClass.newInstance();
        }
        catch (Exception ignored) { }

        GuiLayout guiLayout = InternalMethods.instance.GuiLayout$constructor.invoke();
        view.init(guiLayout);
        MainGroup mainGroup = InternalMethods.instance.GuiLayout$mainGroup$getter.invoke(guiLayout);
        InternalMethods.instance.View$mainGroup$setter.invoke(view, mainGroup);

        return guiLayout;
    }

    public <T> void bindReactiveObject(Reactive reactive, ReactiveObject<T> reactiveObject)
    {
        if (reactive.targetUid().isEmpty()) return;
        Class<T> reactiveObjectParameter;
        try
        {
            reactiveObjectParameter = (Class<T>)((ParameterizedType)reactiveObject.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        // only happens if didn't use the anonymous subclass when instantiating the ReactiveObject
        catch (Exception ignored) { return; }

        List<Element> elements = view.getElements(reactive.targetUid());
        for (Element element: elements)
        {
            IStylePropertySetter setter = ElementRegistry.getStylePropertySetter(element.getClass(), reactive.property());
            IStylePropertyCallback setterCallbackPre = ElementRegistry.getStylePropertySetterCallbackPost(setter);
            IStylePropertyCallback setterCallbackPost = ElementRegistry.getStylePropertySetterCallbackPost(setter);
            Class<?> stylePropertyClass = ElementRegistry.getStylePropertyClass(setter);

            if (setter != null && stylePropertyClass != null)
                if (reactive.initiativeSync() && reactiveObjectParameter.equals(stylePropertyClass))
                    reactiveObject.setterCallbacks.add((value) ->
                    {
                        if (setterCallbackPre != null) setterCallbackPre.invoke(element, value);
                        setter.set(element, value);
                        if (setterCallbackPost != null) setterCallbackPost.invoke(element, value);
                    });
        }
    }
}
