package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.layout.ElementGroup;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.api.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.api.serialization.ixml.RawIxmlUtils;
import com.tttsaurus.ingameinfo.common.api.serialization.json.RawJsonUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import net.minecraft.util.Tuple;
import java.util.List;

public class GuiLayoutDeserializer implements IDeserializer<GuiLayout>
{
    @Override
    public GuiLayout deserialize(String raw)
    {
        GuiLayout guiLayout = InternalMethods.instance.GuiLayout$constructor.invoke();

        if (raw.isEmpty()) return guiLayout;

        List<Tuple<String, String>> nodes = RawIxmlUtils.extractNodes(raw);

        for (Tuple<String, String> pair: nodes)
        {
            if (pair.getFirst().equals("Def"))
            {
                List<Tuple<String, String>> defs = RawIxmlUtils.splitParams(pair.getSecond());
                for (Tuple<String, String> def: defs)
                {
                    String field = def.getFirst();
                    String value = def.getSecond();
                    if (field.equals("debug"))
                        guiLayout.setDebug(Boolean.parseBoolean(value));
                    else if (field.equals("exitKey"))
                        try { guiLayout.setExitKeyForFocusedGui(Integer.parseInt(value)); }
                        catch (Exception ignored) { }
                    else if (field.equals("focused"))
                        guiLayout.setFocused(Boolean.parseBoolean(value));
                    else if (field.equals("hasFocusBg"))
                        guiLayout.setHasFocusBackground(Boolean.parseBoolean(value));
                    else if (field.equals("bgColor"))
                            try { guiLayout.setBackgroundColor(Integer.parseInt(value)); }
                            catch (Exception ignored) { }
                    else if (field.equals("useHeldItemWhitelist"))
                        guiLayout.setHeldItemWhitelist(Boolean.parseBoolean(value));
                    else if (field.equals("useHeldItemBlacklist"))
                        guiLayout.setHeldItemBlacklist(Boolean.parseBoolean(value));
                    else if (field.equals("heldItemWhitelist"))
                    {
                        ItemDeserializer itemDeserializer = new ItemDeserializer();
                        for (String rawItem: RawJsonUtils.splitArray(value))
                        {
                            GhostableItem item = itemDeserializer.deserialize(rawItem);
                            if (item != null) guiLayout.addHeldItemWhitelist(item);
                        }
                    }
                    else if (field.equals("heldItemBlacklist"))
                    {
                        ItemDeserializer itemDeserializer = new ItemDeserializer();
                        for (String rawItem: RawJsonUtils.splitArray(value))
                        {
                            GhostableItem item = itemDeserializer.deserialize(rawItem);
                            if (item != null) guiLayout.addHeldItemBlacklist(item);
                        }
                    }
                }
            }
            else if (pair.getFirst().equals("/Group"))
            {
                guiLayout.endGroup();
                continue;
            }

            Element element = ElementRegistry.newElement(pair.getFirst());
            if (element != null)
            {
                if (ElementGroup.class.isAssignableFrom(element.getClass()))
                    guiLayout.startGroup((ElementGroup)element, (new ElementStylesDeserializer(element.getClass())).deserialize(pair.getSecond()));
                else
                    guiLayout.addElement(element, (new ElementStylesDeserializer(element.getClass())).deserialize(pair.getSecond()));
            }
        }

        return guiLayout;
    }
}
