package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.ElementStyle;
import com.tttsaurus.ingameinfo.common.core.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import net.minecraft.util.Tuple;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ElementStylesDeserializer implements IDeserializer<List<ElementStyle>>
{
    private final Class<? extends Element> clazz;
    public ElementStylesDeserializer(Class<? extends Element> clazz) { this.clazz = clazz; }

    @Override
    public List<ElementStyle> deserialize(String raw)
    {
        List<ElementStyle> elementStyles = new ArrayList<>();

        RawElementStylesDeserializer deserializer = new RawElementStylesDeserializer();
        List<Tuple<String, String>> list = deserializer.deserialize(raw);
        for (Tuple<String, String> pair: list)
        {
            IStylePropertySetter setter = ElementRegistry.getStylePropertySetter(clazz, pair.getFirst());
            if (setter != null)
            {
                IDeserializer<?> stylePropertyDeserializer = ElementRegistry.getStylePropertyDeserializer(setter);
                if (stylePropertyDeserializer != null)
                {
                    Object obj = stylePropertyDeserializer.deserialize(pair.getSecond());
                    if (obj != null) elementStyles.add(new ElementStyle(pair.getFirst(), obj));
                }
            }
        }

        return elementStyles;
    }
}
