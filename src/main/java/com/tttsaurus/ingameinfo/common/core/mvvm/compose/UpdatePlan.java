package com.tttsaurus.ingameinfo.common.core.mvvm.compose;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdatePlan
{
    public enum ActionType
    {
        ADD,
        SWAP,
        REMOVE,
        UPDATE_PROP,
        GOTO_NEXT_LAYER,
        GOTO_PREV_LAYER
    }

    public final int index;
    public final ActionType actionType;

    public String newElementName;
    public Map<String, Object> newStyleProperties;

    public int swapIndex;

    public String stylePropertyName;
    public Object stylePropertyOverride;

    public UpdatePlan(int index, ActionType actionType)
    {
        this.index = index;
        this.actionType = actionType;
    }

    @Override
    public String toString()
    {
        return switch (actionType)
        {
            case ADD ->
            {
                StringBuilder builder = new StringBuilder();
                builder.append("ADD ").append(newElementName).append(" at ").append(index);

                List<String> props = new ArrayList<>();
                for (Map.Entry<String, Object> prop: newStyleProperties.entrySet())
                    props.add(prop.getKey() + "=" + prop.getValue().toString());

                if (!props.isEmpty())
                    builder.append(" (").append(String.join(", ", props)).append(")");

                yield builder.toString();
            }
            case SWAP -> "SWAP " + index + " <-> " + swapIndex;
            case REMOVE -> "REMOVE at " + index;
            case UPDATE_PROP -> "UPDATE_PROP at " + index + " (" + stylePropertyName + "=" + stylePropertyOverride + ")";
            case GOTO_NEXT_LAYER -> "GOTO_NEXT_LAYER at " + index;
            case GOTO_PREV_LAYER -> "GOTO_PREV_LAYER";
        };
    }
}
