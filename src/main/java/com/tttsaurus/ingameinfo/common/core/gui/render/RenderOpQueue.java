package com.tttsaurus.ingameinfo.common.core.gui.render;

import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;
import java.util.ArrayDeque;
import java.util.Deque;

public final class RenderOpQueue
{
    private final Deque<IRenderOp> deque = new ArrayDeque<>();

    public void enqueue(IRenderOp op)
    {
        deque.offerLast(op);
    }
    public IRenderOp dequeue()
    {
        return deque.pollFirst();
    }
}
