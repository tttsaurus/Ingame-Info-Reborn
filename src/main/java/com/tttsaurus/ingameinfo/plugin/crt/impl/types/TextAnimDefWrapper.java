package com.tttsaurus.ingameinfo.plugin.crt.impl.types;

import com.tttsaurus.ingameinfo.common.core.animation.text.CharInfo;
import com.tttsaurus.ingameinfo.common.core.animation.text.ITextAnimDef;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.wrapped.DoubleProperty;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.animation.text.TextAnimDef")
public final class TextAnimDefWrapper
{
    @ZenRegister
    @ZenClass("mods.ingameinfo.animation.text.ITextAnimDef")
    public interface ITextAnimDefWrapper
    {
        void calcAnim(CharInfosWrapper charInfos, TimerWrapper timer, double deltaTime);
    }
    @ZenRegister
    @ZenClass("mods.ingameinfo.animation.text.CharInfos")
    public static class CharInfosWrapper
    {
        @ZenRegister
        @ZenClass("mods.ingameinfo.animation.text.CharInfo")
        public static class CharInfoWrapper
        {
            public final CharInfo charInfo;
            public CharInfoWrapper(CharInfo charInfo)
            {
                this.charInfo = charInfo;
            }

            @ZenMethod
            public float getX() { return charInfo.x; }
//            @ZenMethod
//            public void setX(float x) { charInfo.x = x; }
            @ZenMethod
            public float getY() { return charInfo.y; }
            @ZenMethod
            public void setY(float y) { charInfo.y = y; }
            @ZenMethod
            public float getScale() { return charInfo.scale; }
//            @ZenMethod
//            public void setScale(float scale) { charInfo.scale = scale; }
            @ZenMethod
            public int getColor() { return charInfo.color; }
            @ZenMethod
            public void setColor(int color) { charInfo.color = color; }
            @ZenMethod
            public boolean getShadow() { return charInfo.shadow; }
            @ZenMethod
            public void setShadow(boolean shadow) { charInfo.shadow = shadow; }
        }

        public final CharInfo[] charInfos;
        public CharInfosWrapper(CharInfo[] charInfos)
        {
            this.charInfos = charInfos;
        }

        @ZenMethod
        public CharInfoWrapper getCharInfo(int index)
        {
            if (index < 0 || index >= charInfos.length)
                throw new IndexOutOfBoundsException("Index " + index + " is invalid for an array of length " + charInfos.length);
            return new CharInfoWrapper(charInfos[index]);
        }
        @ZenMethod
        public int getCharInfosLength()
        {
            return charInfos.length;
        }
    }
    @ZenRegister
    @ZenClass("mods.ingameinfo.animation.text.Timer")
    public static class TimerWrapper
    {
        public final DoubleProperty doubleProperty;
        public TimerWrapper(DoubleProperty doubleProperty)
        {
            this.doubleProperty = doubleProperty;
        }

        @ZenMethod
        public double getTime()
        {
            return doubleProperty.get();
        }
        @ZenMethod
        public void setTime(double time)
        {
            doubleProperty.set(time);
        }
    }

    private ITextAnimDefWrapper iTextAnimDefWrapper;

    public final ITextAnimDef iTextAnimDef;
    public TextAnimDefWrapper(ITextAnimDef iTextAnimDef)
    {
        this.iTextAnimDef = iTextAnimDef;
    }
    public TextAnimDefWrapper(ITextAnimDefWrapper iTextAnimDefWrapper)
    {
        this.iTextAnimDefWrapper = iTextAnimDefWrapper;
        this.iTextAnimDef = (charInfos, timer, deltaTime) ->
        {
            this.iTextAnimDefWrapper.calcAnim(new CharInfosWrapper(charInfos), new TimerWrapper(timer), deltaTime);
        };
    }

    @ZenMethod("new")
    public static TextAnimDefWrapper newTextAnimDef(ITextAnimDefWrapper iTextAnimDefWrapper)
    {
        return new TextAnimDefWrapper(iTextAnimDefWrapper);
    }
}
