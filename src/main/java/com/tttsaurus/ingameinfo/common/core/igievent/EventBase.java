package com.tttsaurus.ingameinfo.common.core.igievent;

import com.tttsaurus.ingameinfo.common.core.function.Action;
import com.tttsaurus.ingameinfo.common.core.function.Action1Param;
import com.tttsaurus.ingameinfo.common.core.function.Action2Param;
import java.util.ArrayList;
import java.util.List;

// supports 0~2 args as input
@SuppressWarnings("all")
public abstract class EventBase<T> implements Event<T>
{
    private final List<Object> listeners = new ArrayList<>();

    protected void addListenerInternal(Action action)
    {
        listeners.add(action);
    }
    protected void addListenerInternal(Action1Param action)
    {
        listeners.add(action);
    }
    protected void addListenerInternal(Action2Param action)
    {
        listeners.add(action);
    }

    public void trigger(Object... args)
    {
        for (Object listener: listeners)
        {
            Class<?> clazz = listener.getClass();
            if (Action.class.isAssignableFrom(clazz) && args.length == 0)
            {
                Action action = (Action)listener;
                action.invoke();
            }
            else if (Action1Param.class.isAssignableFrom(clazz) && args.length == 1)
            {
                Action1Param action = (Action1Param)listener;
                action.invoke(args[0]);
            }
            else if (Action2Param.class.isAssignableFrom(clazz) && args.length == 2)
            {
                Action2Param action = (Action2Param)listener;
                action.invoke(args[0], args[1]);
            }
        }
    }
}
