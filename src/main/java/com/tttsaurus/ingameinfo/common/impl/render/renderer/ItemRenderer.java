package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import com.tttsaurus.ingameinfo.common.core.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.renderer.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;

public class ItemRenderer implements IRenderer
{
    protected GhostableItem item = null;
    protected final RenderItem renderer;

    protected float x = 0;
    protected float y = 0;
    protected float scaleX = 1;
    protected float scaleY = 1;

    //<editor-fold desc="getters & setters">
    public void setItem(GhostableItem item) { this.item = item; }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getScaleX() { return scaleX; }
    public void setScaleX(float scaleX) { this.scaleX = scaleX; }

    public float getScaleY() { return scaleY; }
    public void setScaleY(float scaleY) { this.scaleY = scaleY; }
    //</editor-fold>

    public ItemRenderer()
    {
        this.renderer = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void render()
    {
        if (item == null) return;
        if (item.getItemStack() == null) return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, RenderUtils.zLevel);
        GlStateManager.scale(scaleX, scaleY, 1f);
        RenderHelper.enableGUIStandardItemLighting();
        renderer.renderItemAndEffectIntoGUI(item.getItemStack(), 0, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
