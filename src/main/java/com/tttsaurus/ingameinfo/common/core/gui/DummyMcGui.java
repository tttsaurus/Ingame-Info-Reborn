package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.gui.delegate.dummy.IDummyDrawScreen;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.dummy.IDummyKeyTyped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;

public final class DummyMcGui extends GuiScreen
{
    private IDummyDrawScreen drawAction = null;
    private IDummyKeyTyped typeAction = null;

    public void setDrawAction(IDummyDrawScreen drawAction) { this.drawAction = drawAction; }
    public void setTypeAction(IDummyKeyTyped typeAction) { this.typeAction = typeAction; }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (typeAction != null) typeAction.type(keyCode);
    }

    @Override
    public void initGui()
    {
        Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
    }

    @Override
    public void onGuiClosed()
    {
        Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
        Minecraft.getMinecraft().setIngameFocus();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (drawAction != null) drawAction.draw();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
