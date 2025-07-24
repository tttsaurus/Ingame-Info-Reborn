package com.tttsaurus.ingameinfo.common.core.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;

public final class IgiDummyScreen extends GuiScreen
{
    private IGuiScreenDrawScreen drawAction = null;
    private IGuiScreenKeyTyped typeAction = null;

    public void setDrawAction(IGuiScreenDrawScreen drawAction) { this.drawAction = drawAction; }
    public void setTypeAction(IGuiScreenKeyTyped typeAction) { this.typeAction = typeAction; }

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
