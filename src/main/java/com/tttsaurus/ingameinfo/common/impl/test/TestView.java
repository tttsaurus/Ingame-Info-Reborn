package com.tttsaurus.ingameinfo.common.impl.test;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.mvvm.view.View;
import com.tttsaurus.ingameinfo.common.impl.gui.control.SimpleButton;
import com.tttsaurus.ingameinfo.common.impl.gui.control.Text;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.HorizontalGroup;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.VerticalGroup;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import java.awt.*;

public class TestView extends View
{
    @Override
    public void init(GuiLayout guiLayout)
    {
        guiLayout
                //.setDebug(true)
                .setHeldItemWhitelist(true)
                .addHeldItemWhitelist(new ItemStack(Items.APPLE))
                .startGroup(new HorizontalGroup(), "\"padding\" : {\"top\" : 10, \"left\" : 10}")
                .addElement(new Text(), "\"uid\" : \"AAA\", \"text\" : \"Test1\", \"scale\" : 2.0f, \"color\" : " + Color.GREEN.getRGB() + ", \"alignment\" : BOTTOM_LEFT, \"pivot\" : BOTTOM_LEFT, \"backgroundStyle\" : \"roundedBoxWithOutline\"")
                .startGroup(new VerticalGroup())
                .addElement(new Text(), "\"text\" : \"Test2\", \"scale\" : 2.0f")
                .addElement(new Text(), "\"text\" : \"Test3\"")
                .addElement(new SimpleButton(), "\"text\" : \"Test4\", \"width\" : 80, \"holdColor\" : " + Color.GREEN.getRGB())
                .endGroup()
                .endGroup();
    }
}
