package com.tttsaurus.ingameinfo.plugin.crt.impl.animation;

import com.tttsaurus.ingameinfo.common.impl.animation.SmoothDamp;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.animation.SmoothDamp")
public class SmoothDampWrapper
{
    private final SmoothDamp smoothDamp;
    public SmoothDampWrapper(float from, float to, float smoothTime)
    {
        smoothDamp = new SmoothDamp(from, to, smoothTime);
    }
    public SmoothDampWrapper(float from, float to, float smoothTime, float maxSpeed)
    {
        smoothDamp = new SmoothDamp(from, to, smoothTime, maxSpeed);
    }

    @ZenMethod
    public static SmoothDampWrapper newSmoothDamp(float from, float to, float smoothTime)
    {
        return new SmoothDampWrapper(from, to, smoothTime);
    }
    @ZenMethod
    public static SmoothDampWrapper newSmoothDamp(float from, float to, float smoothTime, float maxSpeed)
    {
        return new SmoothDampWrapper(from, to, smoothTime, maxSpeed);
    }

    @ZenMethod
    public float getFrom() { return smoothDamp.getFrom(); }
    @ZenMethod
    public void setFrom(float value) { smoothDamp.setFrom(value); }
    @ZenMethod
    public float getTo() { return smoothDamp.getTo(); }
    @ZenMethod
    public void setTo(float value) { smoothDamp.setTo(value); }
    @ZenMethod
    public float evaluate(float deltaTime) { return smoothDamp.evaluate(deltaTime);}
}
