package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallbackPost;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallbackPre;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.api.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtView;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@SuppressWarnings("all")
public class VvmBinding<TView extends View>
{
    private TView view;

    public GuiLayout init(ViewModel<TView> viewModel, String mvvmRegistryName)
    {
        Class<?> viewClass = (Class<?>)((ParameterizedType)viewModel.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try
        {
            view = (TView)viewClass.newInstance();
        }
        catch (Exception ignored) { }

        // crt support
        if (CrtView.class.isAssignableFrom(viewClass))
            ((CrtView)view).runtimeMvvm = mvvmRegistryName;

        GuiLayout guiLayout = InternalMethods.instance.View$init.invoke(view);
        MainGroup mainGroup = InternalMethods.instance.GuiLayout$mainGroup$getter.invoke(guiLayout);
        InternalMethods.instance.View$mainGroup$setter.invoke(view, mainGroup);

        return guiLayout;
    }

    // todo: handle the binding of passiveSync
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
            IStylePropertyCallbackPre setterCallbackPre = ElementRegistry.getStylePropertySetterCallbackPre(setter);
            IStylePropertyCallbackPost setterCallbackPost = ElementRegistry.getStylePropertySetterCallbackPost(setter);
            Class<?> stylePropertyClass = ElementRegistry.getStylePropertyClass(setter);

            if (setter != null && stylePropertyClass != null)
                if (reactive.initiativeSync() && TypeUtils.looseTypeCheck(reactiveObjectParameter, stylePropertyClass))
                {
                    IAction_1Param<Object> action = ElementRegistry.getStylePropertySetterWithCallbacksHandled(
                            setter,
                            element,
                            setterCallbackPre,
                            setterCallbackPost);
                    reactiveObject.setterCallbacks.add((value) ->
                    {
                        action.invoke(value);
                    });
                }
        }
    }
}
