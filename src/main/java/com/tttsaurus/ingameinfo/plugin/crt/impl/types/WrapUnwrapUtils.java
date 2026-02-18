package com.tttsaurus.ingameinfo.plugin.crt.impl.types;

import com.tttsaurus.ingameinfo.common.core.animation.text.TextAnimDef;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Skewness;
import com.tttsaurus.ingameinfo.common.core.commonutils.GhostableItem;
import javax.annotation.Nullable;

public final class WrapUnwrapUtils
{
    public static Object safeWrap(Object value)
    {
        Object wrap = wrap(value);
        return wrap == null ? value : wrap;
    }

    @Nullable
    public static Object wrap(Object value)
    {
        if (value instanceof Alignment alignment)
            return new AlignmentWrapper(alignment);
        if (value instanceof Padding padding)
            return new PaddingWrapper(padding);
        if (value instanceof Pivot pivot)
            return new PivotWrapper(pivot);
        if (value instanceof Skewness skewness)
            return new SkewnessWrapper(skewness);
        if (value instanceof GhostableItem ghostableItem)
            return new GhostableItemWrapper(ghostableItem);
        if (value instanceof TextAnimDef textAnimDef)
            return new TextAnimDefWrapper(textAnimDef);

        return null;
    }

    public static Object safeUnwrap(Object value)
    {
        Object unwrap = unwrap(value);
        return unwrap == null ? value : unwrap;
    }

    @Nullable
    public static Object unwrap(Object value)
    {
        if (value instanceof AlignmentWrapper alignmentWrapper)
            return alignmentWrapper.alignment;
        if (value instanceof PaddingWrapper paddingWrapper)
            return paddingWrapper.padding;
        if (value instanceof PivotWrapper pivotWrapper)
            return pivotWrapper.pivot;
        if (value instanceof SkewnessWrapper skewnessWrapper)
            return skewnessWrapper.skewness;
        if (value instanceof GhostableItemWrapper ghostableItemWrapper)
            return ghostableItemWrapper.ghostableItem;
        if (value instanceof TextAnimDefWrapper textAnimDefWrapper)
            return textAnimDefWrapper.textAnimDef;

        return null;
    }
}
