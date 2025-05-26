package com.tttsaurus.ingameinfo.common.core.mvvm.compose;

import com.google.common.collect.ImmutableList;
import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Slot;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import java.util.ArrayList;
import java.util.List;

public final class ComposeValidator
{
    private static ComposeValidator instance;
    private static boolean init = false;

    public static ComposeValidator getInstance() { return instance; }

    public final ImmutableList<String> validElementNames;
    public final ImmutableList<String> validElementGroupNames;

    public ComposeValidator(ImmutableList<String> validElementNames, ImmutableList<String> validElementGroupNames)
    {
        this.validElementNames = validElementNames;
        this.validElementGroupNames = validElementGroupNames;
    }

    public static void init()
    {
        if (init) return;

        List<String> validElementNames = new ArrayList<>();
        List<String> validElementGroupNames = new ArrayList<>();
        for (Class<? extends Element> clazz: ElementRegistry.getConstructableElements())
        {
            String name = clazz.getSimpleName();
            validElementNames.add(name);
            if (ElementGroup.class.isAssignableFrom(clazz) && !clazz.equals(Slot.class))
                validElementGroupNames.add(name);
        }

        instance = new ComposeValidator(ImmutableList.copyOf(validElementNames), ImmutableList.copyOf(validElementGroupNames));

        init = true;
    }

    public boolean element(String name)
    {
        return validElementNames.contains(name);
    }
    public boolean group(String name)
    {
        return validElementGroupNames.contains(name);
    }

    public void error(String reason)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Compose Error: ").append(reason).append("\n");

        for (StackTraceElement stackTraceElement : new Exception().getStackTrace())
            builder
                    .append('\t').append("at")
                    .append(' ')
                    .append(stackTraceElement.getClassName())
                    .append('.')
                    .append(stackTraceElement.getMethodName())
                    .append('(')
                    .append(stackTraceElement.getFileName())
                    .append(':')
                    .append(stackTraceElement.getLineNumber())
                    .append(')')
                    .append('\n');

        InGameInfoReborn.logger.error(builder.toString());
    }
}
