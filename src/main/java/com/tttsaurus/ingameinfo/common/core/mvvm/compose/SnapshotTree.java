package com.tttsaurus.ingameinfo.common.core.mvvm.compose;

import java.util.*;

public class SnapshotTree
{
    private final ComposeNode rootNode;
    private ComposeNode currRootNode;
    private ComposeNode lastVisitedNode;

    public SnapshotTree()
    {
        rootNode = new ComposeNode("ROOT", null, -1);
        currRootNode = rootNode;
        lastVisitedNode = rootNode;
    }

    public ComposeNode add(String name, int sysKey)
    {
        ComposeNode newNode = new ComposeNode(name, currRootNode, sysKey);
        currRootNode.children.add(newNode);
        lastVisitedNode = newNode;
        return newNode;
    }
    public void gotoNextLayer()
    {
        currRootNode = lastVisitedNode;
    }
    public void gotoPrevLayer()
    {
        if (currRootNode.parent != null)
            currRootNode = currRootNode.parent;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append("\n");
        recursiveToString(rootNode, builder, 0);
        return builder.toString();
    }

    private static void recursiveToString(ComposeNode root, StringBuilder builder, int depth)
    {
        for (int i = 0; i < depth; i++)
            builder.append("  ");

        builder.append(root.uiElementName).append("\n");

        for (ComposeNode node: root.children)
            recursiveToString(node, builder, depth + 1);
    }

    //<editor-fold desc="diffing algorithm">

    private static void swaps(List<UpdatePlan> updatePlans, List<ComposeNode> nodes, Map<Integer, Integer> perm)
    {
        Set<Integer> visited = new HashSet<>();

        for (Integer start: perm.keySet())
        {
            if (visited.contains(start)) continue;

            List<Integer> cycle = new ArrayList<>();
            Integer current = start;
            while (!visited.contains(current))
            {
                cycle.add(current);
                visited.add(current);

                Integer next = perm.get(current);
                if (next == null) break;
                current = next;
            }

            for (int i = 1; i < cycle.size(); i++)
            {
                UpdatePlan plan = new UpdatePlan(cycle.get(0), UpdatePlan.ActionType.SWAP);
                plan.swapIndex = cycle.get(i);
                Collections.swap(nodes, plan.index, plan.swapIndex);
                updatePlans.add(plan);
            }
        }
    }

    private static void recursiveDiff(List<UpdatePlan> updatePlans, ComposeNode oldRoot, ComposeNode newRoot)
    {
        Map<ComposeNode, ComposeNode> correspondingNodes = new HashMap<>();

        // match old and new nodes regardless of order
        int i = 0;
        int shift = 0;
        for (ComposeNode node1: oldRoot.children)
        {
            // leaf node
            if (node1.children.isEmpty())
            {
                boolean hasCorresponding = false;

                // try to find a corresponding node
                for (int j = 0; j < newRoot.children.size(); j++)
                {
                    ComposeNode node2 = newRoot.children.get(j);
                    if (node2.children.isEmpty() &&
                            node1.uiElementName.equals(node2.uiElementName) &&
                            node1.getFinalKey().equals(node2.getFinalKey()) &&
                            node2.styleProperties.keySet().containsAll(node1.styleProperties.keySet()))
                    {
                        correspondingNodes.put(node1, node2);
                        hasCorresponding = true;
                        break;
                    }
                }

                if (hasCorresponding)
                {
                    for (Map.Entry<String, Object> entry: correspondingNodes.get(node1).styleProperties.entrySet())
                    {
                        Object oldValue = node1.styleProperties.get(entry.getKey());
                        if (oldValue == null || !oldValue.equals(entry.getValue()))
                        {
                            UpdatePlan plan = new UpdatePlan(i - shift, UpdatePlan.ActionType.UPDATE_PROP);
                            plan.stylePropertyName = entry.getKey();
                            plan.stylePropertyOverride = entry.getValue();
                            updatePlans.add(plan);
                        }
                    }
                }
                else
                {
                    UpdatePlan plan = new UpdatePlan(i - shift, UpdatePlan.ActionType.REMOVE);
                    oldRoot.children.remove(i);
                    updatePlans.add(plan);
                    shift++;
                }
            }
            // root node
            else
            {
                boolean hasCorresponding = false;

                // try to find a corresponding node
                for (int j = 0; j < newRoot.children.size(); j++)
                {
                    ComposeNode node2 = newRoot.children.get(j);
                    if (!node2.children.isEmpty() &&
                            node1.uiElementName.equals(node2.uiElementName) &&
                            node1.getFinalKey().equals(node2.getFinalKey()) &&
                            node2.styleProperties.keySet().containsAll(node1.styleProperties.keySet()))
                    {
                        correspondingNodes.put(node1, node2);
                        hasCorresponding = true;
                        break;
                    }
                }

                if (hasCorresponding)
                {
                    updatePlans.add(new UpdatePlan(i - shift, UpdatePlan.ActionType.GOTO_NEXT_LAYER));
                    recursiveDiff(updatePlans, node1, correspondingNodes.get(node1));
                    updatePlans.add(new UpdatePlan(-1, UpdatePlan.ActionType.GOTO_PREV_LAYER));

                    for (Map.Entry<String, Object> entry: correspondingNodes.get(node1).styleProperties.entrySet())
                    {
                        Object oldValue = node1.styleProperties.get(entry.getKey());
                        if (oldValue == null || !oldValue.equals(entry.getValue()))
                        {
                            UpdatePlan plan = new UpdatePlan(i - shift, UpdatePlan.ActionType.UPDATE_PROP);
                            plan.stylePropertyName = entry.getKey();
                            plan.stylePropertyOverride = entry.getValue();
                            updatePlans.add(plan);
                        }
                    }
                }
                else
                {
                    UpdatePlan plan = new UpdatePlan(i - shift, UpdatePlan.ActionType.REMOVE);
                    oldRoot.children.remove(i);
                    updatePlans.add(plan);
                    shift++;
                }
            }
            i++;
        }

        // add missing nodes
        for (i = 0; i < newRoot.children.size(); i++)
        {
            ComposeNode node = newRoot.children.get(i);
            // no corresponding node -> it's the missing one
            if (!correspondingNodes.containsValue(node))
            {
                UpdatePlan plan = new UpdatePlan(i, UpdatePlan.ActionType.ADD);
                plan.newElementName = node.uiElementName;
                plan.newStyleProperties = node.styleProperties;
                oldRoot.children.add(i, new ComposeNode("PLACEHOLDER", oldRoot, -1));
                updatePlans.add(plan);

                // root node
                if (!node.children.isEmpty())
                {
                    updatePlans.add(new UpdatePlan(i, UpdatePlan.ActionType.GOTO_NEXT_LAYER));
                    // no need to sync oldRoot here
                    recursiveAddAll(updatePlans, node);
                    updatePlans.add(new UpdatePlan(-1, UpdatePlan.ActionType.GOTO_PREV_LAYER));
                }
            }
        }

        // now old len == new len
        // swap nodes to get the correct order
        Map<Integer, Integer> perm = new HashMap<>();
        for (Map.Entry<ComposeNode, ComposeNode> entry: correspondingNodes.entrySet())
        {
            int from = oldRoot.children.indexOf(entry.getKey());
            int to = newRoot.children.indexOf(entry.getValue());
            if (from != to) perm.put(from, to);
        }
        if (!perm.isEmpty())
            swaps(updatePlans, oldRoot.children, perm);
    }

    private static void recursiveAddAll(List<UpdatePlan> updatePlans, ComposeNode root)
    {
        for (int i = 0; i < root.children.size(); i++)
        {
            ComposeNode node = root.children.get(i);

            UpdatePlan plan = new UpdatePlan(i, UpdatePlan.ActionType.ADD);
            plan.newElementName = node.uiElementName;
            plan.newStyleProperties = node.styleProperties;
            updatePlans.add(plan);

            // root node
            if (!node.children.isEmpty())
            {
                updatePlans.add(new UpdatePlan(i, UpdatePlan.ActionType.GOTO_NEXT_LAYER));
                recursiveAddAll(updatePlans, node);
                updatePlans.add(new UpdatePlan(-1, UpdatePlan.ActionType.GOTO_PREV_LAYER));
            }
        }
    }

    // mutates itself
    // not mutating newSnapshot
    public List<UpdatePlan> diff(SnapshotTree newSnapshot)
    {
        List<UpdatePlan> updatePlans = new ArrayList<>();
        recursiveDiff(updatePlans, rootNode, newSnapshot.rootNode);
        return updatePlans;
    }

    public List<UpdatePlan> addAll()
    {
        List<UpdatePlan> updatePlans = new ArrayList<>();
        recursiveAddAll(updatePlans, rootNode);
        return updatePlans;
    }

    //</editor-fold>
}
