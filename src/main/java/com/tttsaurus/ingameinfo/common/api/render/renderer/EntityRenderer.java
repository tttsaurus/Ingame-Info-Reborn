package com.tttsaurus.ingameinfo.common.api.render.renderer;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityEntry;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class EntityRenderer implements IRenderer
{
    private final Entity entity;
    private final Render<Entity> renderer;

    protected float x = 0;
    protected float y = 0;
    protected float rotationX = 0;
    protected float rotationY = 0;
    protected float rotationZ = 0;
    protected float scaleX = 1;
    protected float scaleY = 1;
    protected float scaleZ = 1;

    //<editor-fold desc="getters & setters">
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getRotationX() { return rotationX; }
    public void setRotationX(float rotationX) { this.rotationX = rotationX; }

    public float getRotationY() { return rotationY; }
    public void setRotationY(float rotationY) { this.rotationY = rotationY; }

    public float getRotationZ() { return rotationZ; }
    public void setRotationZ(float rotationZ) { this.rotationZ = rotationZ; }

    public float getScaleX() { return scaleX; }
    public void setScaleX(float scaleX) { this.scaleX = scaleX; }

    public float getScaleY() { return scaleY; }
    public void setScaleY(float scaleY) { this.scaleY = scaleY; }

    public float getScaleZ() { return scaleZ; }
    public void setScaleZ(float scaleZ) { this.scaleZ = scaleZ; }
    //</editor-fold>

    public EntityRenderer(EntityEntry entityEntry)
    {
        Minecraft mc = Minecraft.getMinecraft();
        this.entity = entityEntry.newInstance(mc.player.world);
        this.renderer = mc.getRenderManager().getEntityRenderObject(entity);
    }

    private void setFloatBuffer4(FloatBuffer floatBuffer, float... floats)
    {
        floatBuffer.clear();
        floatBuffer.put(floats);
        floatBuffer.flip();
    }

    private final IntBuffer intBuffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
    private final FloatBuffer floatBuffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
    public void render()
    {
        intBuffer.clear();
        floatBuffer.clear();

        // color
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, floatBuffer);
        float colorR = floatBuffer.get(0);
        float colorG = floatBuffer.get(1);
        float colorB = floatBuffer.get(2);
        float colorA = floatBuffer.get(3);

        // blendState
        boolean blendState = GL11.glIsEnabled(GL11.GL_BLEND);

        // depthTest
        boolean depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);

        // cullFace
        boolean cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);

        // light0
        boolean light0 = GL11.glIsEnabled(GL11.GL_LIGHT0);

        // light1
        boolean light1 = GL11.glIsEnabled(GL11.GL_LIGHT1);

        // colorMat
        boolean colorMat = GL11.glIsEnabled(GL11.GL_COLOR_MATERIAL);

        // alphaTest
        boolean alphaTest = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);

        // rescaleNormal
        boolean rescaleNormal = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);

        // lighting
        boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);

        // colorMatFace & colorMatMode
        GL11.glGetInteger(GL11.GL_COLOR_MATERIAL_FACE, intBuffer);
        int colorMatFace = intBuffer.get(0);
        GL11.glGetInteger(GL11.GL_COLOR_MATERIAL_PARAMETER, intBuffer);
        int colorMatMode = intBuffer.get(0);

        // depthWriteMask
        GL11.glGetInteger(GL11.GL_DEPTH_WRITEMASK, intBuffer);
        int depthWriteMask = intBuffer.get(0);

        // shadeModel
        GL11.glGetInteger(GL11.GL_SHADE_MODEL, intBuffer);
        int shadeModel = intBuffer.get(0);

        // lightModelAmbient
        GL11.glGetFloat(GL11.GL_LIGHT_MODEL_AMBIENT, floatBuffer);
        float lightModelAmbientR = floatBuffer.get(0);
        float lightModelAmbientG = floatBuffer.get(1);
        float lightModelAmbientB = floatBuffer.get(2);
        float lightModelAmbientA = floatBuffer.get(3);

        // s-l
        float[] light00InfoRs = new float[4];
        float[] light00InfoGs = new float[4];
        float[] light00InfoBs = new float[4];
        float[] light00InfoAs = new float[4];
        GL11.glGetLight(GL11.GL_LIGHT0, GL11.GL_SPOT_DIRECTION, floatBuffer);
        light00InfoRs[0] = floatBuffer.get(0);
        light00InfoGs[0] = floatBuffer.get(1);
        light00InfoBs[0] = floatBuffer.get(2);
        light00InfoAs[0] = floatBuffer.get(3);
        GL11.glGetLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, floatBuffer);
        light00InfoRs[1] = floatBuffer.get(0);
        light00InfoGs[1] = floatBuffer.get(1);
        light00InfoBs[1] = floatBuffer.get(2);
        light00InfoAs[1] = floatBuffer.get(3);
        GL11.glGetLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, floatBuffer);
        light00InfoRs[2] = floatBuffer.get(0);
        light00InfoGs[2] = floatBuffer.get(1);
        light00InfoBs[2] = floatBuffer.get(2);
        light00InfoAs[2] = floatBuffer.get(3);
        GL11.glGetLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, floatBuffer);
        light00InfoRs[3] = floatBuffer.get(0);
        light00InfoGs[3] = floatBuffer.get(1);
        light00InfoBs[3] = floatBuffer.get(2);
        light00InfoAs[3] = floatBuffer.get(3);

        float[] light01InfoRs = new float[4];
        float[] light01InfoGs = new float[4];
        float[] light01InfoBs = new float[4];
        float[] light01InfoAs = new float[4];
        GL11.glGetLight(GL11.GL_LIGHT1, GL11.GL_SPOT_DIRECTION, floatBuffer);
        light01InfoRs[0] = floatBuffer.get(0);
        light01InfoGs[0] = floatBuffer.get(1);
        light01InfoBs[0] = floatBuffer.get(2);
        light01InfoAs[0] = floatBuffer.get(3);
        GL11.glGetLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, floatBuffer);
        light01InfoRs[1] = floatBuffer.get(0);
        light01InfoGs[1] = floatBuffer.get(1);
        light01InfoBs[1] = floatBuffer.get(2);
        light01InfoAs[1] = floatBuffer.get(3);
        GL11.glGetLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, floatBuffer);
        light01InfoRs[2] = floatBuffer.get(0);
        light01InfoGs[2] = floatBuffer.get(1);
        light01InfoBs[2] = floatBuffer.get(2);
        light01InfoAs[2] = floatBuffer.get(3);
        GL11.glGetLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, floatBuffer);
        light01InfoRs[3] = floatBuffer.get(0);
        light01InfoGs[3] = floatBuffer.get(1);
        light01InfoBs[3] = floatBuffer.get(2);
        light01InfoAs[3] = floatBuffer.get(3);

        // textureBinding2D & activeTexture
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, intBuffer);
        int textureBinding2D = intBuffer.get(0);
        GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE, intBuffer);
        int activeTexture = intBuffer.get(0);

        // e-l
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, intBuffer);
        int textureBinding2D0 = intBuffer.get(0);
        int textureWrapS0 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S);
        int textureWrapT0 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T);
        int textureMinFilter0 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER);
        int textureMagFilter0 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER);

        // s-w0
        boolean texture0 = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        int textureEnvMode0 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE);
        int combineRGB0 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB);
        int combineAlpha0 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA);
        int source0RGB0 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB);
        int source0Alpha0 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA);
        int operand0RGB0 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB);
        int operand0Alpha0 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA);

        // e-w0
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE1);
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, intBuffer);
        int textureBinding2D1 = intBuffer.get(0);
        int textureWrapS1 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S);
        int textureWrapT1 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T);
        int textureMinFilter1 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER);
        int textureMagFilter1 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER);

        // s-w1
        boolean texture1 = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        int textureEnvMode1 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE);
        int combineRGB1 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB);
        int combineAlpha1 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA);
        int source0RGB1 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB);
        int source0Alpha1 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA);
        int operand0RGB1 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB);
        int operand0Alpha1 = GL11.glGetTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA);

        //<editor-fold desc="rendering">
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, RenderUtils.zLevel);
        GlStateManager.scale(-10f, 10f, 10f);
        GlStateManager.scale(scaleX, scaleY, scaleZ);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        if (rotationX != 0) GlStateManager.rotate(rotationX, 1.0f, 0.0f, 0.0f);
        if (rotationY != 0) GlStateManager.rotate(rotationY, 0.0f, 1.0f, 0.0f);
        if (rotationZ != 0) GlStateManager.rotate(rotationZ, 0.0f, 0.0f, 1.0f);

        GlStateManager.setActiveTexture(activeTexture);
        renderer.doRender(entity, 0, 0, 0, 0, 0);

        GlStateManager.popMatrix();
        //</editor-fold>

        // free alphaTest
        if (alphaTest)
            GlStateManager.enableAlpha();
        else
            GlStateManager.disableAlpha();

        // free rescaleNormal
        if (rescaleNormal)
            GlStateManager.enableRescaleNormal();
        else
            GlStateManager.disableRescaleNormal();

        // free lighting
        if (lighting)
            GlStateManager.enableLighting();
        else
            GlStateManager.disableLighting();

        // free s-l
        setFloatBuffer4(floatBuffer, light01InfoRs[0], light01InfoGs[0], light01InfoBs[0], light01InfoAs[0]);
        GlStateManager.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, floatBuffer);
        setFloatBuffer4(floatBuffer, light01InfoRs[1], light01InfoGs[1], light01InfoBs[1], light01InfoAs[1]);
        GlStateManager.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, floatBuffer);
        setFloatBuffer4(floatBuffer, light01InfoRs[2], light01InfoGs[2], light01InfoBs[2], light01InfoAs[2]);
        GlStateManager.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, floatBuffer);
        setFloatBuffer4(floatBuffer, light01InfoRs[3], light01InfoGs[3], light01InfoBs[3], light01InfoAs[3]);
        GlStateManager.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, floatBuffer);

        setFloatBuffer4(floatBuffer, light00InfoRs[0], light00InfoGs[0], light00InfoBs[0], light00InfoAs[0]);
        GlStateManager.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, floatBuffer);
        setFloatBuffer4(floatBuffer, light00InfoRs[1], light00InfoGs[1], light00InfoBs[1], light00InfoAs[1]);
        GlStateManager.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, floatBuffer);
        setFloatBuffer4(floatBuffer, light00InfoRs[2], light00InfoGs[2], light00InfoBs[2], light00InfoAs[2]);
        GlStateManager.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, floatBuffer);
        setFloatBuffer4(floatBuffer, light00InfoRs[3], light00InfoGs[3], light00InfoBs[3], light00InfoAs[3]);
        GlStateManager.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, floatBuffer);

        // free e-l
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        GlStateManager.bindTexture(textureBinding2D0);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, textureWrapS0);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, textureWrapT0);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, textureMinFilter0);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, textureMagFilter0);

        // free s-w0
        if (texture0)
            GlStateManager.enableTexture2D();
        else
            GlStateManager.disableTexture2D();
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, textureEnvMode0);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, combineRGB0);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, combineAlpha0);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, source0RGB0);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, source0Alpha0);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, operand0RGB0);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, operand0Alpha0);

        // free e-w0
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE1);
        GlStateManager.bindTexture(textureBinding2D1);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, textureWrapS1);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, textureWrapT1);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, textureMinFilter1);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, textureMagFilter1);

        // free s-w1
        if (texture1)
            GlStateManager.enableTexture2D();
        else
            GlStateManager.disableTexture2D();
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, textureEnvMode1);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, combineRGB1);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, combineAlpha1);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, source0RGB1);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, source0Alpha1);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, operand0RGB1);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, operand0Alpha1);

        // free textureBinding2D & activeTexture
        GlStateManager.setActiveTexture(activeTexture);
        GlStateManager.bindTexture(textureBinding2D);

        // free lightModelAmbient
        setFloatBuffer4(floatBuffer, lightModelAmbientR, lightModelAmbientG, lightModelAmbientB, lightModelAmbientA);
        GlStateManager.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, floatBuffer);

        // free shadeModel
        GlStateManager.shadeModel(shadeModel);

        // free depthWriteMask
        GlStateManager.depthMask(depthWriteMask == 1);

        // free colorMatFace & colorMatMode
        GlStateManager.colorMaterial(colorMatFace, colorMatMode);

        // free colorMat
        if (colorMat)
            GlStateManager.enableColorMaterial();
        else
            GlStateManager.disableColorMaterial();

        // free light1
        if (light1)
            GlStateManager.enableLight(1);
        else
            GlStateManager.disableLight(1);

        // free light0
        if (light0)
            GlStateManager.enableLight(0);
        else
            GlStateManager.disableLight(0);

        // free cullFace
        if (cullFace)
            GlStateManager.enableCull();
        else
            GlStateManager.disableCull();

        // free depthTest
        if (depthTest)
            GlStateManager.enableDepth();
        else
            GlStateManager.disableDepth();

        // free blendState
        if (blendState)
            GlStateManager.enableBlend();
        else
            GlStateManager.disableBlend();

        // free color
        GlStateManager.color(colorR, colorG, colorB, colorA);
    }
}
