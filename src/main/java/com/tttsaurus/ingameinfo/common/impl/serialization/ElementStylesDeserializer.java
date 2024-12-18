package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.ElementStyle;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import net.minecraft.util.Tuple;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ElementStylesDeserializer implements IDeserializer<List<ElementStyle>>
{
    private final Class<? extends Element> clazz;
    public ElementStylesDeserializer(Class<? extends Element> clazz) { this.clazz = clazz; }

    @Override
    public List<ElementStyle> deserialize(String raw, String protocol)
    {
        List<ElementStyle> elementStyles = new ArrayList<>();

        RawElementStylesDeserializer deserializer = new RawElementStylesDeserializer();
        List<Tuple<String, String>> list = deserializer.deserialize(raw, protocol);
        for (Tuple<String, String> pair: list)
        {
            IStylePropertySetter setter = ElementRegistry.getStylePropertySetter(clazz, pair.getFirst());
            if (setter != null)
            {
                IDeserializer<?> stylePropertyDeserializer = ElementRegistry.getStylePropertyDeserializer(setter);
                if (stylePropertyDeserializer != null)
                {
                    Object obj = stylePropertyDeserializer.deserialize(pair.getSecond(), protocol);
                    if (obj != null) elementStyles.add(new ElementStyle(pair.getFirst(), obj));
                }
            }
        }

        return elementStyles;
    }
}
