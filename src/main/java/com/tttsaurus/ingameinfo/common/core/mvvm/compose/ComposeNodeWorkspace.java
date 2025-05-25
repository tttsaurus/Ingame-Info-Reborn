package com.tttsaurus.ingameinfo.common.core.mvvm.compose;

import com.tttsaurus.ingameinfo.common.core.function.IAction;
import java.util.concurrent.atomic.AtomicInteger;

public class ComposeNodeWorkspace
{
    private final ComposeNode node;
    private final SnapshotTree tree;

    private boolean enableUserKey = false;
    private final AtomicInteger sysKeyCounter;

    protected ComposeNodeWorkspace(ComposeNode node, SnapshotTree tree, AtomicInteger sysKeyCounter)
    {
        this.node = node;
        this.tree = tree;
        this.sysKeyCounter = sysKeyCounter;
    }

    public ComposeNodeWorkspace prop(String stylePropertyName, Object value)
    {
        node.styleProperties.put(stylePropertyName, value);
        return this;
    }

    public ComposeNodeWorkspace key(String key)
    {
        if (!enableUserKey)
        {
            enableUserKey = true;
            sysKeyCounter.decrementAndGet();
        }
        node.userKey = key;
        return this;
    }

    public void wrap(IAction action)
    {
        tree.gotoNextLayer();
        action.invoke();
        tree.gotoPrevLayer();
    }
}
