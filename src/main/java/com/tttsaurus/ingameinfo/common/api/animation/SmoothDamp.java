package com.tttsaurus.ingameinfo.common.api.animation;

import net.minecraft.util.math.MathHelper;

public class SmoothDamp
{
    private float dis;
    private float from;
    private float to;
    private float vel;
    private final float smoothTime;
    private final float maxSpeed;

    //<editor-fold desc="getters & setters">
    public float getFrom() { return from; }
    public void setFrom(float from)
    {
        this.from = from;
        dis = Math.abs(to - from);
    }

    public float getTo() { return to; }
    public void setTo(float to)
    {
        this.to = to;
        dis = Math.abs(to - from);
    }
    //</editor-fold>

    public SmoothDamp(float from, float to, float smoothTime)
    {
        dis = Math.abs(to - from);
        this.from = from;
        this.to = to;
        vel = 0f;
        this.smoothTime = Math.max(smoothTime, 0.001f);
        maxSpeed = Float.POSITIVE_INFINITY;
    }
    public SmoothDamp(float from, float to, float smoothTime, float maxSpeed)
    {
        dis = Math.abs(to - from);
        this.from = from;
        this.to = to;
        vel = 0f;
        this.smoothTime = Math.max(smoothTime, 0.001f);
        this.maxSpeed = maxSpeed;
    }

    public float evaluate(float deltaTime)
    {
        float omega = 2f / smoothTime;
        float x = omega * deltaTime;
        float exp = 1f / (1f + x + 0.48f * x * x + 0.235f * x * x * x);
        float change = to - from;
        float maxChange = maxSpeed * smoothTime;
        change = MathHelper.clamp(change, -maxChange, maxChange);
        float temp = (vel - omega * change) * deltaTime;
        vel = (vel - omega * temp) * exp;
        from = from + change + (temp - change) * exp;
        if (Math.abs(to - from) <= 0.005f * dis) from = to;
        return from;
    }
}
