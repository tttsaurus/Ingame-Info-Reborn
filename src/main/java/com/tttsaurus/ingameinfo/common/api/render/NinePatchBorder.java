package com.tttsaurus.ingameinfo.common.api.render;

public class NinePatchBorder
{
    public Texture2D topLeft;
    public Texture2D topCenter;
    public Texture2D topRight;

    public Texture2D centerLeft;
    public Texture2D center;
    public Texture2D centerRight;

    public Texture2D bottomLeft;
    public Texture2D bottomCenter;
    public Texture2D bottomRight;

    public NinePatchBorder(Texture2D topLeft,
                           Texture2D topCenter,
                           Texture2D topRight,
                           Texture2D centerLeft,
                           Texture2D center,
                           Texture2D centerRight,
                           Texture2D bottomLeft,
                           Texture2D bottomCenter,
                           Texture2D bottomRight)
    {
        this.topLeft = topLeft;
        this.topCenter = topCenter;
        this.topRight = topRight;
        this.centerLeft = centerLeft;
        this.center = center;
        this.centerRight = centerRight;
        this.bottomLeft = bottomLeft;
        this.bottomCenter = bottomCenter;
        this.bottomRight = bottomRight;
    }
}
