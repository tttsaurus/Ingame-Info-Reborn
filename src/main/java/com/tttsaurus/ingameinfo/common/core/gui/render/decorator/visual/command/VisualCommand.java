package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.reflection.TypeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class VisualCommand
{
    private final List<List<Class<?>>> paramLayouts = new ArrayList<>();

    protected int matchParam(Object... args)
    {
        Class<?>[] params = new Class<?>[args.length];
        for (int i = 0; i < params.length; i++)
        {
            Class<?> clazz = args[i].getClass();
            if (TypeUtils.isWrappedPrimitive(clazz)) clazz = TypeUtils.toPrimitive(clazz);
            params[i] = clazz;
        }

        for (int i = 0; i < paramLayouts.size(); i++)
        {
            List<Class<?>> _params = paramLayouts.get(i);
            if (_params.size() != params.length) continue;

            boolean eq = true;
            for (int j = 0; j < _params.size(); j++)
            {
                if (!Objects.equals(_params.get(j), params[j]))
                {
                    eq = false;
                    break;
                }
            }
            if (eq) return i;
        }

        return -1;
    }

    protected static List<Class<?>> params(Class<?>... classes)
    {
        return Arrays.asList(classes);
    }

    @SafeVarargs
    protected VisualCommand(List<Class<?>>... layouts)
    {
        paramLayouts.addAll(Arrays.asList(layouts));
    }

    public abstract void execute(Object... args);
}
