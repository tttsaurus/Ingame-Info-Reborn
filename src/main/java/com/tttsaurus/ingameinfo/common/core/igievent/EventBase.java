package com.tttsaurus.ingameinfo.common.core.igievent;

import com.tttsaurus.ingameinfo.common.core.function.IAction;
import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.core.function.IAction_2Param;
import java.util.ArrayList;
import java.util.List;

// supports 0~2 args as input
@SuppressWarnings("all")
public abstract class EventBase<T> implements IEvent<T>
{
    private final List<Object> listeners = new ArrayList<>();

    protected void addListenerInternal(IAction action)
    {
        listeners.add(action);
    }
    protected void addListenerInternal(IAction_1Param action)
    {
        listeners.add(action);
    }
    protected void addListenerInternal(IAction_2Param action)
    {
        listeners.add(action);
    }

    public void trigger(Object... args)
    {
        for (Object listener: listeners)
        {
            Class<?> clazz = listener.getClass();
            if (IAction.class.isAssignableFrom(clazz) && args.length == 0)
            {
                IAction action = (IAction)listener;
                action.invoke();
            }
            else if (IAction_1Param.class.isAssignableFrom(clazz) && args.length == 1)
            {
                IAction_1Param action = (IAction_1Param)listener;
                action.invoke(args[0]);
            }
            else if (IAction_2Param.class.isAssignableFrom(clazz) && args.length == 2)
            {
                IAction_2Param action = (IAction_2Param)listener;
                action.invoke(args[0], args[1]);
            }
        }
    }
}
