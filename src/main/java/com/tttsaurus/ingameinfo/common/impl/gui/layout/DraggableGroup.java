package com.tttsaurus.ingameinfo.common.impl.gui.layout;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.input.MouseUtils;

@RegisterElement
public class DraggableGroup extends DraggableContainerGroup
{
    @StyleProperty
    public float overrideX = 0f;
    @StyleProperty
    public float overrideY = 0f;

    private float startDragPosX = 0f;
    private float startDragPosY = 0f;
    private boolean unlockDragging = false;
    // stores the actual pos (top-left) and size of draggable area
    private Rect dragArea = new Rect(0, 0, 0, 0);

    @StyleProperty
    public boolean restrictiveDragging = true;

    @StyleProperty(setterCallbackPre = "nonNegativeFloatValidation")
    public float dragAreaWidth = 0f;
    @StyleProperty(setterCallbackPre = "nonNegativeFloatValidation")
    public float dragAreaHeight = 0f;
    @StyleProperty(setterCallbackPre = "alignmentValidation")
    public Alignment dragAreaAlignment = Alignment.NULL;
    @StyleProperty(setterCallbackPre = "pivotValidation")
    public Pivot dragAreaPivot = Pivot.TOP_LEFT;
    @StyleProperty(setterCallbackPre = "paddingValidation")
    public Padding dragAreaPadding = new Padding(0f, 0f, 0f, 0f);

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);

        if (dragAreaWidth == 0f)
            dragArea.width = rect.width;
        else
            dragArea.width = dragAreaWidth;
        if (dragAreaHeight == 0f)
            dragArea.height = rect.height;
        else
            dragArea.height = dragAreaHeight;

        dragArea.x = rect.x + rect.width * dragAreaAlignment.vertical;
        dragArea.y = rect.y + rect.height * dragAreaAlignment.horizontal;
        if (dragAreaPivot.vertical == 0 || dragAreaPivot.vertical == 0.5f) dragArea.x += dragAreaPadding.left;
        if (dragAreaPivot.vertical == 1 || dragAreaPivot.vertical == 0.5f) dragArea.x -= dragAreaPadding.right;
        if (dragAreaPivot.horizontal == 0 || dragAreaPivot.horizontal == 0.5f) dragArea.y += dragAreaPadding.top;
        if (dragAreaPivot.horizontal == 1 || dragAreaPivot.horizontal == 0.5f) dragArea.y -= dragAreaPadding.bottom;

        dragArea.x -= dragArea.width * dragAreaPivot.vertical;
        dragArea.y -= dragArea.height * dragAreaPivot.horizontal;
    }

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        super.onRenderUpdate(queue, focused);

        if (!enabled) return;
        if (focused)
        {
            if (unlockDragging)
            {
                overrideX = rect.x + MouseUtils.getMouseX() - startDragPosX;
                overrideY = rect.y + MouseUtils.getMouseY() - startDragPosY;
                requestReCalc();
            }
            float x = MouseUtils.getMouseX();
            float y = MouseUtils.getMouseY();
            if (MouseUtils.isMouseDownLeft())
            {
                if (dragArea.contains(x, y))
                    unlockDragging = true;
                startDragPosX = x;
                startDragPosY = y;
            }
            else
                unlockDragging = false;
        }
        else
            unlockDragging = false;
        //super.onRenderUpdate(queue, focused);
    }
}
