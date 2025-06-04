package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.ElementProperty;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import net.minecraft.util.Tuple;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ElementPropertiesDeserializer implements IDeserializer<List<ElementProperty>>
{
    private final Class<? extends Element> clazz;
    public ElementPropertiesDeserializer(Class<? extends Element> clazz) { this.clazz = clazz; }

    @Override
    public List<ElementProperty> deserialize(String raw)
    {
        List<ElementProperty> properties = new ArrayList<>();

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
                    if (obj != null) properties.add(new ElementProperty(pair.getFirst(), obj));
                }
            }
        }

        return properties;
    }
}
