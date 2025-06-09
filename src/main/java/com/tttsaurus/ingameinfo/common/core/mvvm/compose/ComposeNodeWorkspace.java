package com.tttsaurus.ingameinfo.common.core.mvvm.compose;

import com.tttsaurus.ingameinfo.common.core.function.IAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ComposeNodeWorkspace
{
    private final ComposeNode node;
    private final SnapshotTree tree;

    private boolean enableUserKey = false;
    private final AtomicInteger sysKeyCounter;
    private final ComposeValidator validator;
    private final AtomicBoolean abortThisFrame;

    protected ComposeNodeWorkspace(ComposeNode node, SnapshotTree tree, AtomicInteger sysKeyCounter, ComposeValidator validator, AtomicBoolean abortThisFrame)
    {
        this.node = node;
        this.tree = tree;
        this.sysKeyCounter = sysKeyCounter;
        this.validator = validator;
        this.abortThisFrame = abortThisFrame;
    }

    public ComposeNodeWorkspace prop(String stylePropertyName, Object value)
    {
        node.styleProperties.put(stylePropertyName, value);
        return this;
    }

    public ComposeNodeWorkspace key(int key)
    {
        return key(String.valueOf(key));
    }
    public ComposeNodeWorkspace key(String key)
    {
        if (!enableUserKey)
        {
            enableUserKey = true;
            sysKeyCounter.decrementAndGet();
        }
        node.userKey = "!" + key;
        return this;
    }

    public void wrap(IAction action)
    {
        if (!validator.group(node.uiElementName))
        {
            validator.error("Failed to wrap because " + node.uiElementName + " is not a ElementGroup. This frame will be aborted.");
            abortThisFrame.set(true);
        }

        tree.gotoNextLayer();
        action.invoke();
        tree.gotoPrevLayer();
    }
}
