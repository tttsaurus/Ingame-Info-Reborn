package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.ingameinfo.common.api.gui.delegate.placeholder.IPlaceholderDrawScreen;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.placeholder.IPlaceholderKeyTyped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;

public class PlaceholderMcGui extends GuiScreen
{
    private IPlaceholderDrawScreen drawAction = null;
    private IPlaceholderKeyTyped typeAction = null;

    public void setDrawAction(IPlaceholderDrawScreen drawAction) { this.drawAction = drawAction; }
    public void setTypeAction(IPlaceholderKeyTyped typeAction) { this.typeAction = typeAction; }

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
