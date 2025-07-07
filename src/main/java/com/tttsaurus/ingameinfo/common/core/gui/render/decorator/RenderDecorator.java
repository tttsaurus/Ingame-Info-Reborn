package com.tttsaurus.ingameinfo.common.core.gui.render.decorator;

import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.IVisualModifier;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;
import java.util.*;

public class RenderDecorator
{
    private final Map<Class<? extends IRenderOp>, Map<RenderOpPhase, List<IVisualModifier>>> visualModifiers = new HashMap<>();

    private Class<? extends IRenderOp> currKey = null;
    private Map<RenderOpPhase, List<IVisualModifier>> currValue = null;

    public boolean isModifying(Class<? extends IRenderOp> targetOpType)
    {
        currKey = targetOpType;
        currValue = visualModifiers.get(targetOpType);
        return currValue != null;
    }

    public List<IVisualModifier> getModifiers(Class<? extends IRenderOp> targetOpType, RenderOpPhase phase)
    {
        List<IVisualModifier> list;

        if (Objects.equals(currKey, targetOpType))
        {
            if (currValue == null)
                return new ArrayList<>();

            List<IVisualModifier> value = currValue.get(phase);
            if (value == null)
                return new ArrayList<>();

            list = value;
        }
        else
        {
            Map<RenderOpPhase, List<IVisualModifier>> map = visualModifiers.get(targetOpType);

            if (map == null)
                return new ArrayList<>();

            List<IVisualModifier> value = map.get(phase);
            if (value == null)
                return new ArrayList<>();

            list = value;
        }

        return list;
    }

    public void register(Class<? extends IRenderOp> targetOpType, RenderOpPhase phase, IVisualModifier visualModifier)
    {
        Map<RenderOpPhase, List<IVisualModifier>> map = visualModifiers.computeIfAbsent(targetOpType, k -> new HashMap<>());
        List<IVisualModifier> list = map.computeIfAbsent(phase, k -> new ArrayList<>());

        list.add(visualModifier);
    }
}
