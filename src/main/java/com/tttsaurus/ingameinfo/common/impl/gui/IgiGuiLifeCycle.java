package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.event.IgiGuiInitEvent;
import com.tttsaurus.ingameinfo.common.core.event.RegainScreenFocusEvent;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.placeholder.IPlaceholderDrawScreen;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.placeholder.IPlaceholderKeyTyped;
import com.tttsaurus.ingameinfo.common.core.function.IFunc;
import com.tttsaurus.ingameinfo.common.core.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.core.render.IGlDisposable;
import com.tttsaurus.ingameinfo.common.core.render.RenderHints;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import com.tttsaurus.ingameinfo.config.IgiConfig;
import com.tttsaurus.ingameinfo.common.core.reader.RlReaderUtils;
import com.tttsaurus.ingameinfo.common.core.render.shader.Shader;
import com.tttsaurus.ingameinfo.common.core.render.shader.ShaderProgram;
import com.tttsaurus.ingameinfo.common.core.render.shader.ShaderLoadingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.apache.commons.lang3.time.StopWatch;
import org.lwjgl.opengl.*;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import static com.tttsaurus.ingameinfo.common.core.render.CommonBuffers.INT_BUFFER_16;
import static com.tttsaurus.ingameinfo.common.core.render.CommonBuffers.FLOAT_BUFFER_16;

@SuppressWarnings("all")
public final class IgiGuiLifeCycle
{
    private static Minecraft minecraft = Minecraft.getMinecraft();
    private static ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

    //<editor-fold desc="fixed update timing variables">
    // all in second
    private static int maxFps_FixedUpdate = 125;
    private static double timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    public static void setMaxFps_FixedUpdate(int fps)
    {
        maxFps_FixedUpdate = fps;
        timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    }
    private static int estimatedFps_FixedUpdate = 0;
    private static double deltaTime_FixedUpdate = 0d;
    private static double excessTime_FixedUpdate = 0d;
    private static final StopWatch stopwatch_FixedUpdate = new StopWatch();
    //</editor-fold>

    //<editor-fold desc="refresh fbo timing variables">
    // all in second
    private static int maxFps_RefreshFbo = 240;
    private static double timePerFrame_RefreshFbo = 1d / maxFps_RefreshFbo;
    public static void setMaxFps_RefreshFbo(int fps)
    {
        maxFps_RefreshFbo = fps;
        timePerFrame_RefreshFbo = 1d / maxFps_RefreshFbo;
    }
    private static int estimatedFps_RefreshFbo = 0;
    private static double excessTime_RefreshFbo = 0d;
    private static final StopWatch stopwatch_RefreshFbo = new StopWatch();
    private static int estimatedUnlimitedFps = 1;
    private static float estimatedFboRefreshRate = 0f;
    //</editor-fold>

    //<editor-fold desc="fbo variables">
    private static boolean enableFbo = true;
    public static void setEnableFbo(boolean flag) { enableFbo = flag; }
    public static boolean getEnableFbo() { return enableFbo; }
    private static boolean refreshFbo = true;
    private static Framebuffer fbo = null;
    private static Framebuffer shaderFbo = null;
    private static boolean enableMultisampleOnFbo = true;
    public static void setEnableMultisampleOnFbo(boolean flag) { enableMultisampleOnFbo = flag; }
    public static boolean getEnableMultisampleOnFbo() { return enableMultisampleOnFbo; }
    private static Framebuffer resolvedFbo = null;
    //</editor-fold>

    //<editor-fold desc="shader variables">
    private static boolean enableShader = true;
    public static void setEnableShader(boolean flag) { enableShader = flag; }
    private static ShaderProgram shaderProgram = null;
    private static int texUnit1TextureID;
    private static boolean uniformsPassed = false;
    //</editor-fold>

    //<editor-fold desc="render time debug variables">
    // all in nanosecond
    private static boolean renderTimeDebug = false;
    public static void setRenderTimeDebug(boolean flag) { renderTimeDebug = flag; }
    private static final StopWatch cpuTimeStopwatch = new StopWatch();
    private static final long[] cpuTimeNanoFor50Frames = new long[50];
    private static final long[] gpuTimeNanoFor50Frames = new long[50];
    private static int timeNanoArrayIndex = 0;
    private static RandomAccessFile renderTimeDebugFile = null;
    //</editor-fold>

    //<editor-fold desc="igi event calls">
    private static String lastBiomeRegistryName = "";
    private static void triggerIgiEvents()
    {
        // works on sp client
        EventCenter.igiGuiFpsEvent.trigger(estimatedFps_FixedUpdate, estimatedFps_RefreshFbo);
        EventCenter.igiGuiFboRefreshRateEvent.trigger(estimatedFboRefreshRate);
        EventCenter.gameFpsEvent.trigger(Minecraft.getDebugFPS());
        Runtime runtime = Runtime.getRuntime();
        EventCenter.gameMemoryEvent.trigger(runtime.totalMemory() - runtime.freeMemory(), runtime.totalMemory());
        EntityPlayerSP player = minecraft.player;
        if (player != null)
        {
            Biome biome = player.world.getBiome(player.getPosition());
            ResourceLocation rl = biome.getRegistryName();
            if (rl != null)
            {
                String biomeRegistryName = rl.toString();
                if (!biomeRegistryName.equals(lastBiomeRegistryName))
                {
                    lastBiomeRegistryName = biomeRegistryName;
                    EventCenter.enterBiomeEvent.trigger(biome.getBiomeName(), biomeRegistryName);
                }
            }
        }

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null)
        {
            // works on mp client
            IgiNetwork.requestTpsMspt(EventCenter.gameTpsMsptEvent::trigger);
        }
        else
        {
            // works on sp client
            long[] tickTimes = server.tickTimeArray;
            double averageTickTime = 0d;

            for (long tickTime : tickTimes)
                averageTickTime += tickTime / 1.0E6d;
            averageTickTime /= tickTimes.length;

            int tps = (int)(Math.min(1000d / averageTickTime, 20d));
            EventCenter.gameTpsMsptEvent.trigger(tps, averageTickTime);
        }

        EventCenter.triggerModCompatEvents();
    }
    //</editor-fold>

    //<editor-fold desc="fixed & render updates">
    private static double timer = 0.5f;
    private static void onFixedUpdate()
    {
        //<editor-fold desc="gui container fixed update">
        for (IgiGuiContainer container: openedGuiMap.values())
            container.onFixedUpdate(deltaTime_FixedUpdate);
        //</editor-fold>

        timer += deltaTime_FixedUpdate;
        if (timer >= 0.5d)
        {
            timer -= 0.5d;
            triggerIgiEvents();
        }
    }
    private static void onRenderUpdate()
    {
        if (enableFbo)
        {
            compileShader();
            if (!refreshFbo)
            {
                if (enableShader)
                {
                    RenderUtils.renderFbo(resolution, shaderFbo, true);
                }
                else
                {
                    if (enableMultisampleOnFbo)
                        RenderUtils.renderFbo(resolution, resolvedFbo, true);
                    else
                        RenderUtils.renderFbo(resolution, fbo, true);
                }
                return;
            }
            refreshFbo = false;
            bindFbo();
        }

        //<editor-fold desc="gui container render update">
        ItemStack heldItemMainhand = null;
        EntityPlayerSP player = minecraft.player;
        if (player != null)
            heldItemMainhand = player.getHeldItemMainhand();

        List<Map.Entry<String, IgiGuiContainer>> entryList = new ArrayList<>(openedGuiMap.entrySet());
        String firstFocused = "";
        for (int i = entryList.size() - 1; i >= 0; i--)
        {
            Map.Entry<String, IgiGuiContainer> entry = entryList.get(i);
            if (entry.getValue().getFocused())
            {
                firstFocused = entry.getKey();
                break;
            }
        }
        for (Map.Entry<String, IgiGuiContainer> entry: openedGuiMap.entrySet())
        {
            IgiGuiContainer container = entry.getValue();
            boolean display = true;

            if (heldItemMainhand != null)
            {
                if (container.getUseHeldItemWhitelist())
                {
                    display = false;
                    for (GhostableItem item: container.getHeldItemWhitelist())
                        if (item.getItemStack() != null)
                            if (item.getItemStack().isItemEqual(heldItemMainhand))
                                display = true;
                }
                if (container.getUseHeldItemBlacklist())
                {
                    for (GhostableItem item: container.getHeldItemBlacklist())
                        if (item.getItemStack() != null)
                            if (item.getItemStack().isItemEqual(heldItemMainhand))
                                display = false;
                }
            }

            if (display)
                container.onRenderUpdate(entry.getKey().equals(firstFocused));
        }
        //</editor-fold>

        if (enableFbo)
        {
            if (enableShader)
            {
                bindShaderFbo();
                activateShader();
                RenderUtils.renderFbo(resolution, fbo, false);
                deactivateShader();
                bindMcFbo();

                RenderUtils.renderFbo(resolution, shaderFbo, true);
            }
            else
            {
                if (enableMultisampleOnFbo) resolveMultisampledFbo();
                bindMcFbo();

                if (enableMultisampleOnFbo)
                    RenderUtils.renderFbo(resolution, resolvedFbo, true);
                else
                    RenderUtils.renderFbo(resolution, fbo, true);
            }
        }
    }
    private static void onRenderUpdateDebug()
    {
        if (renderTimeDebugFile == null)
        {
            try
            {
                renderTimeDebugFile = new RandomAccessFile("ingameinfo_render_time.csv", "rw");
                renderTimeDebugFile.setLength(0);
                renderTimeDebugFile.seek(0);
                renderTimeDebugFile.write("Cpu Time Nano,Gpu Time Nano,Avg Cpu Time Nano,Avg Gpu Time Nano\n".getBytes(StandardCharsets.UTF_8));
            }
            catch (Exception ignored) { }
        }

        int gpuTimeQueryID = GL15.glGenQueries();
        GL15.glBeginQuery(GL33.GL_TIME_ELAPSED, gpuTimeQueryID);
        cpuTimeStopwatch.reset();
        cpuTimeStopwatch.start();

        onRenderUpdate();

        cpuTimeStopwatch.stop();
        GL15.glEndQuery(GL33.GL_TIME_ELAPSED);
        GL15.glGetQueryObject(gpuTimeQueryID, GL15.GL_QUERY_RESULT, INT_BUFFER_16);
        long gpuTimeNano = INT_BUFFER_16.get(0);
        long cpuTimeNano = cpuTimeStopwatch.getNanoTime();
        if (timeNanoArrayIndex == 50)
        {
            long avgCpuTimeNano = Arrays.stream(cpuTimeNanoFor50Frames).sum() / 50l;
            long avgGpuTimeNano = Arrays.stream(gpuTimeNanoFor50Frames).sum() / 50l;

            try
            {
                renderTimeDebugFile.write((
                        cpuTimeNanoFor50Frames[timeNanoArrayIndex - 1] + "," +
                        gpuTimeNanoFor50Frames[timeNanoArrayIndex - 1] + "," +
                        avgCpuTimeNano + "," +
                        avgGpuTimeNano + "\n").getBytes(StandardCharsets.UTF_8));
            }
            catch (Exception ignored) { }

            for (int i = 1; i < 50; i++)
                cpuTimeNanoFor50Frames[i - 1] = cpuTimeNanoFor50Frames[i];
            cpuTimeNanoFor50Frames[timeNanoArrayIndex - 1] = cpuTimeNano;

            for (int i = 1; i < 50; i++)
                gpuTimeNanoFor50Frames[i - 1] = gpuTimeNanoFor50Frames[i];
            gpuTimeNanoFor50Frames[timeNanoArrayIndex - 1] = gpuTimeNano;
        }
        else
        {
            try
            {
                renderTimeDebugFile.write((
                        cpuTimeNano + "," +
                        gpuTimeNano + ",0,0\n").getBytes(StandardCharsets.UTF_8));
            }
            catch (Exception ignored) { }
            cpuTimeNanoFor50Frames[timeNanoArrayIndex] = cpuTimeNano;
            gpuTimeNanoFor50Frames[timeNanoArrayIndex] = gpuTimeNano;
            timeNanoArrayIndex++;
        }
    }
    private static void onRenderUpdateWrapped()
    {
        storeCommonGlStates();
        if (renderTimeDebug)
            onRenderUpdateDebug();
        else
            onRenderUpdate();
        restoreCommonGlStates();
    }
    //</editor-fold>

    //<editor-fold desc="fbo methods">
    private static void resolveMultisampledFbo()
    {
        // init resolvedFbo
        if (resolvedFbo == null)
        {
            resolvedFbo = new Framebuffer(minecraft.displayWidth, minecraft.displayHeight, true);
            resolvedFbo.framebufferColor[0] = 0f;
            resolvedFbo.framebufferColor[1] = 0f;
            resolvedFbo.framebufferColor[2] = 0f;
            resolvedFbo.framebufferColor[3] = 0f;
            resolvedFbo.enableStencil();
            resolvedFbo.unbindFramebuffer();
            GlResourceManager.addDisposable((IGlDisposable)resolvedFbo);
        }

        if (resolvedFbo.framebufferWidth != minecraft.displayWidth || resolvedFbo.framebufferHeight != minecraft.displayHeight)
        {
            resolvedFbo.createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
            resolvedFbo.unbindFramebuffer();
        }

        OpenGlHelper.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo.framebufferObject);
        OpenGlHelper.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, resolvedFbo.framebufferObject);

        GL30.glBlitFramebuffer(0, 0, resolvedFbo.framebufferWidth, resolvedFbo.framebufferHeight, 0, 0, resolvedFbo.framebufferWidth, resolvedFbo.framebufferHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
    }
    private static void bindShaderFbo()
    {
        // init shaderFbo
        if (shaderFbo == null)
        {
            shaderFbo = new Framebuffer(minecraft.displayWidth, minecraft.displayHeight, true);
            shaderFbo.framebufferColor[0] = 0f;
            shaderFbo.framebufferColor[1] = 0f;
            shaderFbo.framebufferColor[2] = 0f;
            shaderFbo.framebufferColor[3] = 0f;
            shaderFbo.enableStencil();
            shaderFbo.bindFramebuffer(true);
            GlResourceManager.addDisposable((IGlDisposable)shaderFbo);
        }

        if (shaderFbo.framebufferWidth != minecraft.displayWidth || shaderFbo.framebufferHeight != minecraft.displayHeight)
        {
            shaderFbo.createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
            shaderFbo.bindFramebuffer(true);
        }
        else
        {
            RenderHints.clearFboWithoutUnbind();
            shaderFbo.framebufferClear();
            RenderHints.clearFboWithUnbind();
        }
    }
    private static void bindFbo()
    {
        // init fbo
        if (fbo == null)
        {
            if (enableMultisampleOnFbo)
            {
                RenderHints.multisampleTexBind();
                RenderHints.multisampleFbo();
            }
            fbo = new Framebuffer(minecraft.displayWidth, minecraft.displayHeight, true);
            fbo.framebufferColor[0] = 0f;
            fbo.framebufferColor[1] = 0f;
            fbo.framebufferColor[2] = 0f;
            fbo.framebufferColor[3] = 0f;
            fbo.enableStencil();
            if (enableMultisampleOnFbo)
            {
                RenderHints.defaultTexBind();
                RenderHints.defaultFbo();
            }
            fbo.bindFramebuffer(true);
            GlResourceManager.addDisposable((IGlDisposable)fbo);
        }

        if (fbo.framebufferWidth != minecraft.displayWidth || fbo.framebufferHeight != minecraft.displayHeight)
        {
            if (enableMultisampleOnFbo)
            {
                RenderHints.multisampleTexBind();
                RenderHints.multisampleFbo();
            }
            fbo.createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
            if (enableMultisampleOnFbo)
            {
                RenderHints.defaultTexBind();
                RenderHints.defaultFbo();
            }
            fbo.bindFramebuffer(true);
        }
        else
        {
            RenderHints.clearFboWithoutUnbind();
            fbo.framebufferClear();
            RenderHints.clearFboWithUnbind();
        }
    }
    private static void bindMcFbo()
    {
        Framebuffer mcFbo = minecraft.getFramebuffer();

        if (mcFbo.framebufferWidth != minecraft.displayWidth || mcFbo.framebufferHeight != minecraft.displayHeight)
            mcFbo.createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);

        mcFbo.bindFramebuffer(true);
    }
    //</editor-fold>

    //<editor-fold desc="shader methods">
    private static void compileShader()
    {
        // init shader program
        if (shaderProgram == null)
        {
            String rawFrag = RlReaderUtils.read("ingameinfo:shaders/post_processing_frag.glsl", true);
            StringBuilder rawFragBuilder = new StringBuilder();

            if (enableMultisampleOnFbo)
                rawFragBuilder
                        .append("#version 400 core\n")
                        .append("#define USE_MULTISAMPLE 1\n");
            else
                rawFragBuilder
                        .append("#version 330 core\n")
                        .append("#define USE_MULTISAMPLE 0\n");
            rawFragBuilder.append(rawFrag);

            Shader frag = new Shader("ingameinfo:shaders/post_processing_frag.glsl", rawFragBuilder.toString(), Shader.ShaderType.FRAGMENT);
            Shader vertex = ShaderLoadingUtils.load("ingameinfo:shaders/post_processing_vertex.glsl", Shader.ShaderType.VERTEX);

            shaderProgram = new ShaderProgram(frag, vertex);
            shaderProgram.setup();

            InGameInfoReborn.logger.info(shaderProgram.getSetupDebugReport());
        }
    }
    private static void activateShader()
    {
        shaderProgram.use();

        GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE, INT_BUFFER_16);
        int texUnit = INT_BUFFER_16.get(0);

        GlStateManager.setActiveTexture(GL13.GL_TEXTURE1);
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, INT_BUFFER_16);
        texUnit1TextureID = INT_BUFFER_16.get(0);

        if (enableMultisampleOnFbo) RenderHints.multisampleTexBind();
        fbo.bindFramebufferTexture();
        if (enableMultisampleOnFbo) RenderHints.defaultTexBind();

        GlStateManager.setActiveTexture(texUnit);

        if (!uniformsPassed)
        {
            uniformsPassed = true;
            shaderProgram.setUniform("screenTexture", 1);
            if (enableMultisampleOnFbo)
                shaderProgram.setUniform("sampleNum", RenderHints.getHint_Framebuffer$FramebufferSampleNum());
            shaderProgram.setUniform("enableAlpha", IgiConfig.ENABLE_PP_ALPHA);
            shaderProgram.setUniform("targetAlpha", IgiConfig.PP_ALPHA);
        }
    }
    private static void deactivateShader()
    {
        GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE, INT_BUFFER_16);
        int texUnit = INT_BUFFER_16.get(0);

        GlStateManager.setActiveTexture(GL13.GL_TEXTURE1);
        GlStateManager.bindTexture(texUnit1TextureID);

        GlStateManager.setActiveTexture(texUnit);

        shaderProgram.unuse();
    }
    //</editor-fold>

    //<editor-fold desc="gl states">
    private static int textureID = 0;
    private static float r = 0, g = 0, b = 0, a = 0;
    private static boolean blend = false;
    private static boolean lighting = false;
    private static boolean texture2D = false;
    private static boolean alphaTest = false;
    private static int shadeModel = 0;
    private static boolean depthTest = false;
    private static boolean cullFace = false;
    private static int blendSrcRgb;
    private static int blendDstRgb;
    private static int blendSrcAlpha;
    private static int blendDstAlpha;
    private static int alphaFunc;
    private static float alphaRef;
    //</editor-fold>

    //<editor-fold desc="gl state management">
    private static void storeCommonGlStates()
    {
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, INT_BUFFER_16);
        textureID = INT_BUFFER_16.get(0);
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, FLOAT_BUFFER_16);
        r = FLOAT_BUFFER_16.get(0);
        g = FLOAT_BUFFER_16.get(1);
        b = FLOAT_BUFFER_16.get(2);
        a = FLOAT_BUFFER_16.get(3);
        blend = GL11.glIsEnabled(GL11.GL_BLEND);
        lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        texture2D = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        alphaTest = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        GL11.glGetInteger(GL11.GL_SHADE_MODEL, INT_BUFFER_16);
        shadeModel = INT_BUFFER_16.get(0);
        depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB, INT_BUFFER_16);
        blendSrcRgb = INT_BUFFER_16.get(0);
        GL11.glGetInteger(GL14.GL_BLEND_DST_RGB, INT_BUFFER_16);
        blendDstRgb = INT_BUFFER_16.get(0);
        GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA, INT_BUFFER_16);
        blendSrcAlpha = INT_BUFFER_16.get(0);
        GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA, INT_BUFFER_16);
        blendDstAlpha = INT_BUFFER_16.get(0);
        GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC, INT_BUFFER_16);
        alphaFunc = INT_BUFFER_16.get(0);
        GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF, FLOAT_BUFFER_16);
        alphaRef = FLOAT_BUFFER_16.get(0);
    }
    private static void restoreCommonGlStates()
    {
        GlStateManager.alphaFunc(alphaFunc, alphaRef);
        GlStateManager.tryBlendFuncSeparate(blendSrcRgb, blendDstRgb, blendSrcAlpha, blendDstAlpha);
        if (cullFace)
            GlStateManager.enableCull();
        else
            GlStateManager.disableCull();
        if (depthTest)
            GlStateManager.enableDepth();
        else
            GlStateManager.disableDepth();
        GlStateManager.shadeModel(shadeModel);
        if (alphaTest)
            GlStateManager.enableAlpha();
        else
            GlStateManager.disableAlpha();
        if (texture2D)
            GlStateManager.enableTexture2D();
        else
            GlStateManager.disableTexture2D();
        if (lighting)
            GlStateManager.enableLighting();
        else
            GlStateManager.disableLighting();
        if (blend)
            GlStateManager.enableBlend();
        else
            GlStateManager.disableBlend();
        GlStateManager.color(r, g, b, a);
        GlStateManager.bindTexture(textureID);
    }
    //</editor-fold>

    //<editor-fold desc="screen focus">
    private static boolean listenRegainScreenFocus = false;
    //</editor-fold>

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        //<editor-fold desc="gui container init">
        for (IgiGuiContainer container : openedGuiMap.values())
            if (!container.getInitFlag())
                container.onInit();
        //</editor-fold>

        //<editor-fold desc="gui container resize">
        if (resolution.getScaleFactor() != event.getResolution().getScaleFactor() ||
            resolution.getScaledWidth() != event.getResolution().getScaledWidth() ||
            resolution.getScaledHeight() != event.getResolution().getScaledHeight())
        {
            resolution = event.getResolution();
            for (IgiGuiContainer container: openedGuiMap.values())
                container.onScaledResolutionResize();
        }
        //</editor-fold>

        //<editor-fold desc="fixed update timing">
        if (!stopwatch_FixedUpdate.isStarted())
            stopwatch_FixedUpdate.start();

        // unit: second
        double currentTime = stopwatch_FixedUpdate.getNanoTime() / 1.0E9d;
        if (currentTime + excessTime_FixedUpdate >= timePerFrame_FixedUpdate)
        {
            stopwatch_FixedUpdate.stop();
            stopwatch_FixedUpdate.reset();
            stopwatch_FixedUpdate.start();

            deltaTime_FixedUpdate = currentTime;
            estimatedFps_FixedUpdate = ((int)(1d / (currentTime + excessTime_FixedUpdate)) + estimatedFps_FixedUpdate) / 2;

            onFixedUpdate();

            excessTime_FixedUpdate = currentTime + excessTime_FixedUpdate - timePerFrame_FixedUpdate;
        }
        if (excessTime_FixedUpdate >= timePerFrame_FixedUpdate)
            excessTime_FixedUpdate %= timePerFrame_FixedUpdate;
        //</editor-fold>

        //<editor-fold desc="refresh fbo timing">
        if (enableFbo)
        {
            if (!stopwatch_RefreshFbo.isStarted())
            {
                stopwatch_RefreshFbo.start();
                stopwatch_RefreshFbo.split();
            }

            // unit: second
            currentTime = stopwatch_RefreshFbo.getNanoTime() / 1.0E9d;
            estimatedFboRefreshRate = (Math.min(((float)estimatedFps_RefreshFbo) / ((float)estimatedUnlimitedFps), 1f) + estimatedFboRefreshRate) / 2f;
            double lastSplitTime = stopwatch_RefreshFbo.getSplitNanoTime() / 1.0E9d;
            stopwatch_RefreshFbo.split();
            if (currentTime - lastSplitTime > 0d)
                estimatedUnlimitedFps = ((int)(1d / (currentTime - lastSplitTime)) + estimatedUnlimitedFps) / 2;
            if (currentTime + excessTime_RefreshFbo >= timePerFrame_RefreshFbo)
            {
                stopwatch_RefreshFbo.stop();
                stopwatch_RefreshFbo.reset();
                stopwatch_RefreshFbo.start();
                stopwatch_RefreshFbo.split();

                estimatedFps_RefreshFbo = ((int)(1d / (currentTime + excessTime_RefreshFbo)) + estimatedFps_RefreshFbo) / 2;

                refreshFbo = true;

                excessTime_RefreshFbo = currentTime + excessTime_RefreshFbo - timePerFrame_RefreshFbo;
            }
            if (excessTime_RefreshFbo >= timePerFrame_RefreshFbo)
                excessTime_RefreshFbo %= timePerFrame_RefreshFbo;
        }
        //</editor-fold>

        //<editor-fold desc="placeholder gui">
        if (!isPlaceholderGuiOn)
            onRenderUpdateWrapped();
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            // close placeholder
            if (isPlaceholderGuiOn)
            {
                if (openedGuiMap.isEmpty())
                {
                    isPlaceholderGuiOn = false;
                    placeholderGui = null;
                    minecraft.displayGuiScreen(null);
                }
                else
                {
                    AtomicBoolean focus = new AtomicBoolean(false);
                    openedGuiMap.forEach((uuid, guiContainer) -> focus.set(focus.get() || (guiContainer.getFocused() && guiContainer.getActive())));

                    if (!focus.get())
                    {
                        isPlaceholderGuiOn = false;
                        placeholderGui = null;
                        minecraft.displayGuiScreen(null);
                    }
                }
            }
            // open placeholder
            else if (!openedGuiMap.isEmpty() && minecraft.currentScreen == null)
            {
                AtomicBoolean focus = new AtomicBoolean(false);
                openedGuiMap.forEach((uuid, guiContainer) -> focus.set(focus.get() || (guiContainer.getFocused() && guiContainer.getActive())));

                if (focus.get())
                {
                    placeholderGui = new PlaceholderMcGui();
                    placeholderGui.setDrawAction(new IPlaceholderDrawScreen()
                    {
                        @Override
                        public void draw()
                        {
                            onRenderUpdateWrapped();
                        }
                    });
                    placeholderGui.setTypeAction(new IPlaceholderKeyTyped()
                    {
                        @Override
                        public void type(int keycode)
                        {
                            List<Map.Entry<String, IgiGuiContainer>> entryList = new ArrayList<>(openedGuiMap.entrySet());
                            String key = "";
                            IFunc<Boolean> exitCallback = null;
                            for (int i = entryList.size() - 1; i >= 0; i--)
                            {
                                Map.Entry<String, IgiGuiContainer> entry = entryList.get(i);
                                IgiGuiContainer container = entry.getValue();
                                if (container.getFocused())
                                    if (keycode == container.getExitKeyForFocusedGui())
                                    {
                                        key = entry.getKey();
                                        exitCallback = container.getExitCallback();
                                        break;
                                    }
                            }
                            if (!key.isEmpty())
                            {
                                if (exitCallback.invoke())
                                    openedGuiMap.remove(key);
                            }
                        }
                    });
                    minecraft.displayGuiScreen(placeholderGui);
                    isPlaceholderGuiOn = true;
                }
            }
        }
        //</editor-fold>

        //<editor-fold desc="regain screen focus event">
        if (!Display.isActive() && !listenRegainScreenFocus)
            listenRegainScreenFocus = true;
        if (Display.isActive() && listenRegainScreenFocus)
        {
            listenRegainScreenFocus = false;
            MinecraftForge.EVENT_BUS.post(new RegainScreenFocusEvent());
        }
        //</editor-fold>

        if (initFlag)
        {
            initFlag = false;
            MinecraftForge.EVENT_BUS.post(new IgiGuiInitEvent());
        }
    }

    private static boolean initFlag = true;

    // placeholder related
    private static boolean isPlaceholderGuiOn = false;
    private static PlaceholderMcGui placeholderGui;

    // key: mvvm registry name
    private static final Map<String, IgiGuiContainer> openedGuiMap = new LinkedHashMap<>();
    private static Map<String, IgiGuiContainer> getOpenedGuiMap() { return openedGuiMap; }

    public static void openIgiGui(String mvvmRegistryName, IgiGuiContainer guiContainer)
    {
        if (openedGuiMap.containsKey(mvvmRegistryName)) return;
        openedGuiMap.put(mvvmRegistryName, guiContainer);
    }
    public static void closeIgiGui(String mvvmRegistryName)
    {
        openedGuiMap.remove(mvvmRegistryName);
    }
}
