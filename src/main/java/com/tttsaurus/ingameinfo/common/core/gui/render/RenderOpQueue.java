package com.tttsaurus.ingameinfo.common.core.gui.render;

import com.tttsaurus.ingameinfo.common.core.gui.render.op.RenderOp;
import java.util.ArrayDeque;
import java.util.Deque;

public final class RenderOpQueue
{
    private final Deque<RenderOp> deque = new ArrayDeque<>();

    public void enqueue(RenderOp op)
    {
        deque.offerLast(op);
    }
    public RenderOp dequeue()
    {
        return deque.pollFirst();
    }
}
