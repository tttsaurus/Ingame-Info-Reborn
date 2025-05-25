package com.tttsaurus.ingameinfo.common.core.mvvm.compose;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComposeNode
{
    public final String uiElementName;
    public final Map<String, Object> styleProperties = new HashMap<>();
    public final List<ComposeNode> children = new CopyOnWriteArrayList<>();
    public final ComposeNode parent;

    public final String sysKey;
    public String userKey;

    public String getFinalKey()
    {
        return userKey == null ? sysKey : userKey;
    }

    protected ComposeNode(String name, ComposeNode parent, int sysKey)
    {
        uiElementName = name;
        this.parent = parent;
        this.sysKey = String.valueOf(sysKey);
        userKey = null;
    }
}
