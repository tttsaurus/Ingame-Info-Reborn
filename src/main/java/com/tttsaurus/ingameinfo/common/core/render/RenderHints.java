package com.tttsaurus.ingameinfo.common.core.render;

import com.tttsaurus.ingameinfo.common.core.function.IFunc;
import com.tttsaurus.ingameinfo.common.core.render.texture.param.FilterMode;
import com.tttsaurus.ingameinfo.common.core.render.texture.param.WrapMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;

@SuppressWarnings("all")
public final class RenderHints
{
    public static class GlStateManager
    {
        public enum BindTextureHint
        {
            TEXTURE_2D,
            TEXTURE_2D_MULTISAMPLE
        }
    }
    public static class Framebuffer
    {
        public enum CreateFramebufferHint
        {
            TEXTURE_2D,
            TEXTURE_2D_MULTISAMPLE
        }
        public enum FramebufferClearHint
        {
            UNBIND_FBO,
            DONT_UNBIND_FBO
        }
    }

    //<editor-fold desc="gl version">
    private static boolean glVersionParsed = false;
    private static int majorGlVersion = -1;
    private static int minorGlVersion = -1;
    private static String rawGlVersion = "";

    public static int getMajorGlVersion()
    {
        if (!glVersionParsed) parseGlVersion();
        return majorGlVersion;
    }
    public static int getMinorGlVersion()
    {
        if (!glVersionParsed) parseGlVersion();
        return minorGlVersion;
    }
    public static String getRawGlVersion()
    {
        if (!glVersionParsed) parseGlVersion();
        return rawGlVersion;
    }
    private static void parseGlVersion()
    {
        glVersionParsed = true;
        rawGlVersion = GL11.glGetString(GL11.GL_VERSION);

        if (rawGlVersion != null)
        {
            String[] parts = rawGlVersion.split("\\s+")[0].split("\\.");
            if (parts.length >= 2)
            {
                try
                {
                    majorGlVersion = Integer.parseInt(parts[0]);
                    minorGlVersion = Integer.parseInt(parts[1]);
                }
                catch (NumberFormatException ignored) { }
            }
        }
        else
            rawGlVersion = "";

        if (rawGlVersion.isEmpty() || majorGlVersion == -1 || minorGlVersion == -1)
            throw new RuntimeException("RenderHints.parseGlVersion() failed to parse GL version.");
    }
    //</editor-fold>

    //<editor-fold desc="render hints">
    private static GlStateManager.BindTextureHint hint_GlStateManager$BindTextureHint = GlStateManager.BindTextureHint.TEXTURE_2D;
    private static Framebuffer.CreateFramebufferHint hint_Framebuffer$CreateFramebufferHint = Framebuffer.CreateFramebufferHint.TEXTURE_2D;
    private static Framebuffer.FramebufferClearHint hint_Framebuffer$FramebufferClearHint = Framebuffer.FramebufferClearHint.UNBIND_FBO;
    private static int hint_Framebuffer$FramebufferSampleNum = 2;
    private static FilterMode hint_Texture2D$FilterModeMin = FilterMode.LINEAR;
    private static FilterMode hint_Texture2D$FilterModeMag = FilterMode.LINEAR;
    private static WrapMode hint_Texture2D$WrapModeS = WrapMode.CLAMP;
    private static WrapMode hint_Texture2D$WrapModeT = WrapMode.CLAMP;
    // per unit scaled resolution
    private static float hint_pixelPerUnit = 1f;
    //</editor-fold>

    //<editor-fold desc="simplified hint setters">
    public static void multisampleTexBind()
    {
        hint_GlStateManager$BindTextureHint = GlStateManager.BindTextureHint.TEXTURE_2D_MULTISAMPLE;
    }
    public static void defaultTexBind()
    {
        hint_GlStateManager$BindTextureHint = GlStateManager.BindTextureHint.TEXTURE_2D;
    }
    public static void multisampleFbo()
    {
        hint_Framebuffer$CreateFramebufferHint = Framebuffer.CreateFramebufferHint.TEXTURE_2D_MULTISAMPLE;
    }
    public static void defaultFbo()
    {
        hint_Framebuffer$CreateFramebufferHint = Framebuffer.CreateFramebufferHint.TEXTURE_2D;
    }
    public static void clearFboWithoutUnbind()
    {
        hint_Framebuffer$FramebufferClearHint = Framebuffer.FramebufferClearHint.DONT_UNBIND_FBO;
    }
    public static void clearFboWithUnbind()
    {
        hint_Framebuffer$FramebufferClearHint = Framebuffer.FramebufferClearHint.UNBIND_FBO;
    }
    public static void fboSampleNum(int num)
    {
        if (num < 1) num = 1;
        if (num > 4) num = 4;
        hint_Framebuffer$FramebufferSampleNum = num;
    }

    public static void texture2dLinearFilterMin()
    {
        hint_Texture2D$FilterModeMin = FilterMode.LINEAR;
    }
    public static void texture2dNearestFilterMin()
    {
        hint_Texture2D$FilterModeMin = FilterMode.NEAREST;
    }

    public static void texture2dLinearFilterMag()
    {
        hint_Texture2D$FilterModeMag = FilterMode.LINEAR;
    }
    public static void texture2dNearestFilterMag()
    {
        hint_Texture2D$FilterModeMag = FilterMode.NEAREST;
    }

    public static void texture2dRepeatWrapS()
    {
        hint_Texture2D$WrapModeS = WrapMode.REPEAT;
    }
    public static void texture2dClampWrapS()
    {
        hint_Texture2D$WrapModeS = WrapMode.CLAMP;
    }
    public static void texture2dClampToEdgeWrapS()
    {
        hint_Texture2D$WrapModeS = WrapMode.CLAMP_TO_EDGE;
    }
    public static void texture2dClampToBorderWrapS()
    {
        hint_Texture2D$WrapModeS = WrapMode.CLAMP_TO_BORDER;
    }
    public static void texture2dMirroredRepeatWrapS()
    {
        hint_Texture2D$WrapModeS = WrapMode.MIRRORED_REPEAT;
    }

    public static void texture2dRepeatWrapT()
    {
        hint_Texture2D$WrapModeT = WrapMode.REPEAT;
    }
    public static void texture2dClampWrapT()
    {
        hint_Texture2D$WrapModeT = WrapMode.CLAMP;
    }
    public static void texture2dClampToEdgeWrapT()
    {
        hint_Texture2D$WrapModeT = WrapMode.CLAMP_TO_EDGE;
    }
    public static void texture2dClampToBorderWrapT()
    {
        hint_Texture2D$WrapModeT = WrapMode.CLAMP_TO_BORDER;
    }
    public static void texture2dMirroredRepeatWrapT()
    {
        hint_Texture2D$WrapModeT = WrapMode.MIRRORED_REPEAT;
    }

    public static void pixelPerUnit(float pixel)
    {
        hint_pixelPerUnit = pixel;
    }
    //</editor-fold>

    //<editor-fold desc="hint setters">
    public static void setHint_GlStateManager$BindTextureHint(GlStateManager.BindTextureHint hint)
    {
        hint_GlStateManager$BindTextureHint = hint;
    }
    public static void setHint_Framebuffer$CreateFramebufferHint(Framebuffer.CreateFramebufferHint hint)
    {
        hint_Framebuffer$CreateFramebufferHint = hint;
    }
    public static void setHint_Framebuffer$FramebufferClearHint(Framebuffer.FramebufferClearHint hint)
    {
        hint_Framebuffer$FramebufferClearHint = hint;
    }
    public static void setHint_Framebuffer$FramebufferSampleNum(int hint)
    {
        hint_Framebuffer$FramebufferSampleNum = hint;
    }
    public static void setHint_Texture2D$FilterModeMin(FilterMode hint)
    {
        hint_Texture2D$FilterModeMin = hint;
    }
    public static void setHint_Texture2D$FilterModeMag(FilterMode hint)
    {
        hint_Texture2D$FilterModeMag = hint;
    }
    public static void setHint_Texture2D$WrapModeS(WrapMode hint)
    {
        hint_Texture2D$WrapModeS = hint;
    }
    public static void setHint_Texture2D$WrapModeT(WrapMode hint)
    {
        hint_Texture2D$WrapModeT = hint;
    }
    public static void setHint_pixelPerUnit(float pixel)
    {
        hint_pixelPerUnit = pixel;
    }
    //</editor-fold>

    //<editor-fold desc="hint getters">
    public static GlStateManager.BindTextureHint getHint_GlStateManager$BindTextureHint() { return hint_GlStateManager$BindTextureHint; }
    public static Framebuffer.CreateFramebufferHint getHint_Framebuffer$CreateFramebufferHint() { return hint_Framebuffer$CreateFramebufferHint; }
    public static Framebuffer.FramebufferClearHint getHint_Framebuffer$FramebufferClearHint() { return hint_Framebuffer$FramebufferClearHint; }
    public static int getHint_Framebuffer$FramebufferSampleNum() { return hint_Framebuffer$FramebufferSampleNum; }
    public static FilterMode getHint_Texture2D$FilterModeMin() { return hint_Texture2D$FilterModeMin; }
    public static FilterMode getHint_Texture2D$FilterModeMag() { return hint_Texture2D$FilterModeMag; }
    public static WrapMode getHint_Texture2D$WrapModeS() { return hint_Texture2D$WrapModeS; }
    public static WrapMode getHint_Texture2D$WrapModeT() { return hint_Texture2D$WrapModeT; }
    public static float getHint_pixelPerUnit() { return hint_pixelPerUnit; }
    //</editor-fold>

    //<editor-fold desc="active render info">
    // inspired by <https://github.com/Laike-Endaril/Fantastic-Lib/blob/669c3306bbebca9de1c3959e6dd4203b5b7215d4/src/main/java/com/fantasticsource/mctools/Render.java>
    private static boolean isActiveRenderInfoGettersInit = false;
    private static IFunc<FloatBuffer> modelViewMatrixGetter;
    private static IFunc<FloatBuffer> projectionMatrixGetter;

    public static FloatBuffer getModelViewMatrix()
    {
        if (!isActiveRenderInfoGettersInit) initActiveRenderInfoGetters();
        return modelViewMatrixGetter.invoke();
    }
    public static FloatBuffer getProjectionMatrix()
    {
        if (!isActiveRenderInfoGettersInit) initActiveRenderInfoGetters();
        return projectionMatrixGetter.invoke();
    }

    private static void initActiveRenderInfoGetters()
    {
        isActiveRenderInfoGettersInit = true;

        Field modelViewMatrixField = null;
        try { modelViewMatrixField = ActiveRenderInfo.class.getDeclaredField("MODELVIEW"); }
        catch (Exception ignored)
        {
            try { modelViewMatrixField = ActiveRenderInfo.class.getDeclaredField("field_178812_b"); }
            catch (Exception ignored2) { }
        }
        if (modelViewMatrixField == null)
            throw new RuntimeException("RenderHints.initActiveRenderInfoGetters() failed to find the getter of MODELVIEW.");

        Field projectionMatrixField = null;
        try { projectionMatrixField = ActiveRenderInfo.class.getDeclaredField("PROJECTION"); }
        catch (Exception ignored)
        {
            try { projectionMatrixField = ActiveRenderInfo.class.getDeclaredField("field_178813_c"); }
            catch (Exception ignored2) { }
        }
        if (projectionMatrixField == null)
            throw new RuntimeException("RenderHints.initActiveRenderInfoGetters() failed to find the getter of PROJECTION.");

        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try
        {
            modelViewMatrixField.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(modelViewMatrixField);
            modelViewMatrixGetter = () ->
            {
                try
                {
                    return (FloatBuffer)handle.invoke();
                }
                catch (Throwable e) { return null; }
            };
        }
        catch (Exception exception) { throw new RuntimeException(exception); }

        try
        {
            projectionMatrixField.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(projectionMatrixField);
            projectionMatrixGetter = () ->
            {
                try
                {
                    return (FloatBuffer)handle.invoke();
                }
                catch (Throwable e) { return null; }
            };
        }
        catch (Exception exception) { throw new RuntimeException(exception); }
    }
    //</editor-fold>

    //<editor-fold desc="partial ticks">
    // inspired by <https://github.com/Laike-Endaril/Fantastic-Lib/blob/669c3306bbebca9de1c3959e6dd4203b5b7215d4/src/main/java/com/fantasticsource/mctools/Render.java>
    private static boolean isPartialTickGetterInit = false;
    private static IFunc<Double> partialTickGetter;

    public static double getPartialTick()
    {
        if (!isPartialTickGetterInit) initPartialTickGetter();
        Minecraft minecraft = Minecraft.getMinecraft();
        return minecraft.isGamePaused() ? partialTickGetter.invoke() : minecraft.getRenderPartialTicks();
    }

    private static void initPartialTickGetter()
    {
        isPartialTickGetterInit = true;

        Field partialTickField = null;
        try { partialTickField = Minecraft.class.getDeclaredField("renderPartialTicksPaused"); }
        catch (Exception ignored)
        {
            try { partialTickField = Minecraft.class.getDeclaredField("field_193996_ah"); }
            catch (Exception ignored2) { }
        }
        if (partialTickField == null)
            throw new RuntimeException("RenderHints.initPartialTickGetter() failed to find the getter of renderPartialTicksPaused.");

        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try
        {
            partialTickField.setAccessible(true);
            MethodHandle handle = lookup.unreflectGetter(partialTickField);
            partialTickGetter = () ->
            {
                try
                {
                    return (double)(float)handle.invoke(Minecraft.getMinecraft());
                }
                catch (Throwable e) { return null; }
            };
        }
        catch (Exception exception) { throw new RuntimeException(exception); }
    }
    //</editor-fold>

    //<editor-fold desc="camera">
    public static Vec3d getWorldOffset()
    {
        Entity camera = Minecraft.getMinecraft().getRenderViewEntity();
        if (camera == null) camera = Minecraft.getMinecraft().player;
        double partialTicks = getPartialTick();

        double camX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * partialTicks;
        double camY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * partialTicks;
        double camZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * partialTicks;

        return new Vec3d(camX, camY, camZ);
    }
    public static Vec3d getCameraPos()
    {
        Entity camera = Minecraft.getMinecraft().getRenderViewEntity();
        if (camera == null) camera = Minecraft.getMinecraft().player;
        double partialTicks = getPartialTick();

        double camX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * partialTicks;
        double camY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * partialTicks + camera.getEyeHeight();
        double camZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * partialTicks;

        return new Vec3d(camX, camY, camZ);
    }
    public static Vector2f getCameraRotationInDegree()
    {
        Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
        if (viewEntity == null) return new Vector2f(0, 0);
        return new Vector2f(viewEntity.rotationYaw, viewEntity.rotationPitch);
    }
    public static Vector2f getCameraRotationInRadian()
    {
        Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
        if (viewEntity == null) return new Vector2f(0, 0);
        return new Vector2f((float)Math.toRadians(viewEntity.rotationYaw), (float)Math.toRadians(viewEntity.rotationPitch));
    }
    //</editor-fold>
}
