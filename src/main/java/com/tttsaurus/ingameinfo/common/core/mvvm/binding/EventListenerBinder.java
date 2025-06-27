package com.tttsaurus.ingameinfo.common.core.mvvm.binding;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.event.IUIEventListener;
import com.tttsaurus.ingameinfo.common.core.gui.event.UIEvent;
import java.util.List;

public class EventListenerBinder
{
    public <T extends UIEvent> void bind(VvmBinding<?> vvmBinding, String uid, Class<T> type, IUIEventListener<T> listener, int ordinal)
    {
        List<Element> list = vvmBinding.view.getElements(uid);

        int index = 0;
        for (Element item: list)
        {
            if (ordinal != -1 && ordinal != index++) continue;

            item.addEventListener(type, listener);
        }
    }
}
