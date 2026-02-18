package com.tttsaurus.ingameinfo.common.core.mvvm.binding;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.event.UIEventListener;
import com.tttsaurus.ingameinfo.common.core.gui.event.UIEvent;
import com.tttsaurus.ingameinfo.common.core.mvvm.view.View;
import java.util.List;

public class EventListenerBinder
{
    public <T extends UIEvent> void bind(View view, String uid, Class<T> type, UIEventListener<T> listener, int ordinal)
    {
        List<Element> list = view.getElements(uid);

        int index = 0;
        for (Element item: list)
        {
            if (ordinal != -1 && ordinal != index++) continue;

            item.addEventListener(type, listener);
        }
    }
}
