package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.api.gui.style.*;
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
import java.util.Map;

@SuppressWarnings("all")
public class VvmBinding<TView extends View>
{
    public TView view = null;

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

    public void bindReactiveCollection(Reactive reactive, ReactiveCollection reactiveCollection)
    {
        int index = 0;
        List<Element> elements = view.getElements(reactive.targetUid());
        for (Element element: elements)
        {
            if (reactive.ordinal() != -1 && reactive.ordinal() != index++) continue;

            if (ElementGroup.class.isAssignableFrom(element.getClass()))
            {
                reactiveCollection.group = (ElementGroup)element;
                return;
            }
        }
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

        int index = 0;
        List<Element> elements = view.getElements(reactive.targetUid());
        for (Element element: elements)
        {
            if (reactive.ordinal() != -1 && reactive.ordinal() != index++) continue;

            IStylePropertySetter stylePropertySetter = ElementRegistry.getStylePropertySetter(element.getClass(), reactive.property());
            Class<?> stylePropertyClass = ElementRegistry.getStylePropertyClass(stylePropertySetter);

            // whether style property exists
            if (stylePropertySetter == null || stylePropertyClass == null) continue;
            // whether type matches
            if (!TypeUtils.looseTypeCheck(reactiveObjectParameter, stylePropertyClass)) continue;

            // viewmodel to view sync
            if (reactive.initiativeSync())
            {
                IAction_1Param<Object> action = ElementRegistry.getStylePropertySetterFullCallback(element, reactive.property());
                reactiveObject.initiativeCallbacks.add((value) ->
                {
                    action.invoke(value);
                });
            }
            // view to viewmodel sync
            if (reactive.passiveSync())
            {
                Map<String, IStylePropertySyncTo> syncToMap = InternalMethods.instance.Element$syncToMap$getter.invoke(element);
                IStylePropertyGetter getter = ElementRegistry.getStylePropertyGetter(stylePropertySetter);
                if (getter != null)
                {
                    syncToMap.put(reactive.property(), () ->
                    {
                        reactiveObject.setInternal(getter.get(element));
                    });
                    reactiveObject.setInternal(getter.get(element));
                }
            }
        }
    }
}
