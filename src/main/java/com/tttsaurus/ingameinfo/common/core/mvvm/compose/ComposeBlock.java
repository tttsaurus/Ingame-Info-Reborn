package com.tttsaurus.ingameinfo.common.core.mvvm.compose;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ComposeBlock
{
    private final ElementGroup root;
    private SnapshotTree prevSnapshot = null;
    private SnapshotTree currSnapshot = new SnapshotTree();
    private final AtomicInteger sysKeyCounter = new AtomicInteger(0);
    private final ComposeValidator validator;
    private final AtomicBoolean abortThisFrame = new AtomicBoolean(false);

    private ThemeConfig themeConfig;

    public ComposeBlock(ElementGroup root)
    {
        this.root = root;
        ComposeValidator.init();
        validator = ComposeValidator.getInstance();
    }

    public final void update(ThemeConfig themeConfig)
    {
        this.themeConfig = themeConfig;

        compose();

        boolean abort = abortThisFrame.get();
        sysKeyCounter.set(0);
        abortThisFrame.set(false);

        if (abort)
        {
            currSnapshot = new SnapshotTree();
        }
        else
        {
            List<UpdatePlan> updatePlans;
            if (prevSnapshot == null)
                updatePlans = currSnapshot.addAll();
            else
                updatePlans = prevSnapshot.diff(currSnapshot);

            prevSnapshot = currSnapshot;
            currSnapshot = new SnapshotTree();

            if (reConstruct(updatePlans)) root.requestReCalc();
        }
    }

    private void recursiveReConstruct(ElementGroup root, List<UpdatePlan> updatePlans, AtomicInteger indexCounter)
    {
        if (indexCounter.get() >= updatePlans.size()) return;

        UpdatePlan plan = updatePlans.get(indexCounter.get());
        switch (plan.actionType)
        {
            case ADD ->
            {
                Element element = ElementRegistry.newElement(plan.newElementName);
                if (element != null)
                {
                    element.loadTheme(themeConfig);
                    root.elements.add(element);
                    for (Map.Entry<String, Object> entry: plan.newStyleProperties.entrySet())
                        element.setStyleProperty(entry.getKey(), entry.getValue());
                }
                indexCounter.incrementAndGet();
            }
            case SWAP ->
            {
                Collections.swap(root.elements, plan.index, plan.swapIndex);
                indexCounter.incrementAndGet();
            }
            case REMOVE ->
            {
                root.elements.remove(plan.index);
                indexCounter.incrementAndGet();
            }
            case UPDATE_PROP ->
            {
                Element element = root.elements.get(plan.index);
                element.setStyleProperty(plan.stylePropertyName, plan.stylePropertyOverride);
                indexCounter.incrementAndGet();
            }
            case GOTO_NEXT_LAYER ->
            {
                indexCounter.incrementAndGet();
                recursiveReConstruct((ElementGroup)root.elements.get(plan.index), updatePlans, indexCounter);
            }
            case GOTO_PREV_LAYER ->
            {
                indexCounter.incrementAndGet();
                return;
            }
        }

        recursiveReConstruct(root, updatePlans, indexCounter);
    }
    private boolean reConstruct(List<UpdatePlan> updatePlans)
    {
        boolean needReCalc = false;

        for (UpdatePlan plan: updatePlans)
        {
            if (plan.actionType != UpdatePlan.ActionType.UPDATE_PROP &&
                    plan.actionType != UpdatePlan.ActionType.GOTO_NEXT_LAYER &&
                    plan.actionType != UpdatePlan.ActionType.GOTO_PREV_LAYER)
            {
                needReCalc = true;
                break;
            }
        }

        recursiveReConstruct(root, updatePlans, new AtomicInteger(0));

        return needReCalc;
    }

    public abstract void compose();

    protected final ComposeNodeWorkspace ui(String name)
    {
        if (!validator.element(name))
        {
            validator.error(name + " is not a valid Element. This frame will be aborted.");
            abortThisFrame.set(true);
        }

        return new ComposeNodeWorkspace(
                currSnapshot.add(name, sysKeyCounter.getAndIncrement()),
                currSnapshot,
                sysKeyCounter,
                validator,
                abortThisFrame);
    }
}
