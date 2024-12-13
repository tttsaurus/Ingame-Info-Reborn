package com.tttsaurus.ingameinfo.common.api.gui.style;

import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StylePropertyDeserializer
{
    Class<? extends IDeserializer<?>> value();
}
