package com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command;

import com.tttsaurus.ingameinfo.common.core.reflection.TypeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class VisualCommand
{
    private final List<List<Class<?>>> paramLayouts = new ArrayList<>();

    // type widening
    @SuppressWarnings("all")
    protected final Object[] castArgs(int layout, Object... args)
    {
        List<Class<?>> params = paramLayouts.get(layout);
        for (int i = 0; i < args.length; i++)
        {
            Class<?> expect = params.get(i);
            if (args[i] instanceof Byte arg)
            {
                if (expect == short.class) args[i] = arg.shortValue();
                if (expect == int.class) args[i] = arg.intValue();
                if (expect == long.class) args[i] = arg.longValue();
                if (expect == float.class) args[i] = arg.floatValue();
                if (expect == double.class) args[i] = arg.doubleValue();
            }
            else if (args[i] instanceof Short arg)
            {
                if (expect == int.class) args[i] = arg.intValue();
                if (expect == long.class) args[i] = arg.longValue();
                if (expect == float.class) args[i] = arg.floatValue();
                if (expect == double.class) args[i] = arg.doubleValue();
            }
            else if (args[i] instanceof Character arg)
            {
                if (expect == int.class) args[i] = (int)arg.charValue();
                if (expect == long.class) args[i] = (long)(int)arg.charValue();
                if (expect == float.class) args[i] = (float)(int)arg.charValue();
                if (expect == double.class) args[i] = (double)(int)arg.charValue();
            }
            else if (args[i] instanceof Integer arg)
            {
                if (expect == long.class) args[i] = arg.longValue();
                if (expect == float.class) args[i] = arg.floatValue();
                if (expect == double.class) args[i] = arg.doubleValue();
            }
            else if (args[i] instanceof Long arg)
            {
                if (expect == float.class) args[i] = arg.floatValue();
                if (expect == double.class) args[i] = arg.doubleValue();
            }
            else if (args[i] instanceof Float arg)
            {
                if (expect == double.class) args[i] = arg.doubleValue();
            }
        }
        return args;
    }

    @SuppressWarnings("all")
    private final boolean matchClass(Class<?> expect, Class<?> in)
    {
        if (Objects.equals(expect, in)) return true;

        if (in == byte.class)
            return expect == short.class ||
                    expect == int.class ||
                    expect == long.class ||
                    expect == float.class ||
                    expect == double.class;

        if (in == short.class)
            return expect == int.class ||
                    expect == long.class ||
                    expect == float.class ||
                    expect == double.class;

        if (in == char.class)
            return expect == int.class ||
                    expect == long.class ||
                    expect == float.class ||
                    expect == double.class;

        if (in == int.class)
            return expect == long.class ||
                    expect == float.class ||
                    expect == double.class;

        if (in == long.class)
            return expect == float.class ||
                    expect == double.class;

        if (in == float.class)
            return expect == double.class;

        return false;
    }

    protected int matchParams(Object... args)
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
                if (!matchClass(_params.get(j), params[j]))
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
        for (int i = 0; i < classes.length; i++)
            if (TypeUtils.isWrappedPrimitive(classes[i])) classes[i] = TypeUtils.toPrimitive(classes[i]);

        return Arrays.asList(classes);
    }

    @SafeVarargs
    protected VisualCommand(List<Class<?>>... layouts)
    {
        paramLayouts.addAll(Arrays.asList(layouts));
    }

    public abstract void execute(Object... args);
}
