package com.tttsaurus.ingameinfo.common.core.render.texture;

public class NinePatchBorder
{
    public static class Patch
    {
        public Texture2DImpl tex;
        public float width;
        public float height;
        public boolean tiling;
        public boolean sizeDeductionByPixels;

        public Patch(Texture2DImpl tex, float width, float height, boolean tiling, boolean sizeDeductionByPixels)
        {
            this.tex = tex;
            this.width = width;
            this.height = height;
            this.tiling = tiling;
            this.sizeDeductionByPixels = sizeDeductionByPixels;
        }
        public Patch(Texture2DImpl tex)
        {
            this.tex = tex;
            width = 0f;
            height = 0f;
            tiling = false;
            sizeDeductionByPixels = true;
        }
    }

    public Patch topLeft;
    public Patch topCenter;
    public Patch topRight;

    public Patch centerLeft;
    public Patch center;
    public Patch centerRight;

    public Patch bottomLeft;
    public Patch bottomCenter;
    public Patch bottomRight;

    public NinePatchBorder(Texture2DImpl topLeft,
                           Texture2DImpl topCenter,
                           Texture2DImpl topRight,
                           Texture2DImpl centerLeft,
                           Texture2DImpl center,
                           Texture2DImpl centerRight,
                           Texture2DImpl bottomLeft,
                           Texture2DImpl bottomCenter,
                           Texture2DImpl bottomRight)
    {
        this.topLeft = new Patch(topLeft);
        this.topCenter = new Patch(topCenter);
        this.topRight = new Patch(topRight);
        this.centerLeft = new Patch(centerLeft);
        this.center = new Patch(center);
        this.centerRight = new Patch(centerRight);
        this.bottomLeft = new Patch(bottomLeft);
        this.bottomCenter = new Patch(bottomCenter);
        this.bottomRight = new Patch(bottomRight);
    }

    public NinePatchBorder(Patch topLeft,
                           Patch topCenter,
                           Patch topRight,
                           Patch centerLeft,
                           Patch center,
                           Patch centerRight,
                           Patch bottomLeft,
                           Patch bottomCenter,
                           Patch bottomRight)
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
