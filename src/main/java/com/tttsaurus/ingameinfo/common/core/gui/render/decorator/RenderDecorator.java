package com.tttsaurus.ingameinfo.common.core.gui.render.decorator;

import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.VisualModifier;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.RenderOp;
import java.util.*;

public class RenderDecorator
{
    private boolean empty = true;
    public boolean isEmpty() { return empty; }

    private final Map<Class<? extends RenderOp>, Map<RenderOpPhase, List<VisualModifier>>> visualModifiers = new HashMap<>();

    private Class<? extends RenderOp> currKey = null;
    private Map<RenderOpPhase, List<VisualModifier>> currValue = null;

    public boolean isModifying(Class<? extends RenderOp> targetOpType)
    {
        currKey = targetOpType;
        currValue = visualModifiers.get(targetOpType);
        return currValue != null;
    }

    public List<VisualModifier> getModifiers(Class<? extends RenderOp> targetOpType, RenderOpPhase phase)
    {
        List<VisualModifier> list;

        if (Objects.equals(currKey, targetOpType))
        {
            if (currValue == null)
                return new ArrayList<>();

            List<VisualModifier> value = currValue.get(phase);
            if (value == null)
                return new ArrayList<>();

            list = value;
        }
        else
        {
            Map<RenderOpPhase, List<VisualModifier>> map = visualModifiers.get(targetOpType);

            if (map == null)
                return new ArrayList<>();

            List<VisualModifier> value = map.get(phase);
            if (value == null)
                return new ArrayList<>();

            list = value;
        }

        return list;
    }

    public void register(Class<? extends RenderOp> targetOpType, RenderOpPhase phase, VisualModifier visualModifier)
    {
        Map<RenderOpPhase, List<VisualModifier>> map = visualModifiers.computeIfAbsent(targetOpType, k -> new HashMap<>());
        List<VisualModifier> list = map.computeIfAbsent(phase, k -> new ArrayList<>());

        list.add(visualModifier);

        empty = false;
    }
}
