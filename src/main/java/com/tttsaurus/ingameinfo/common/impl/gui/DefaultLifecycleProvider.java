package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLifecycleProvider;
import com.tttsaurus.ingameinfo.common.core.commonutils.RlReaderUtils;
import com.tttsaurus.ingameinfo.common.core.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.core.render.IGlDisposable;
import com.tttsaurus.ingameinfo.common.core.render.RenderHints;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.shader.Shader;
import com.tttsaurus.ingameinfo.common.core.render.shader.ShaderLoadingUtils;
import com.tttsaurus.ingameinfo.common.core.render.shader.ShaderProgram;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import com.tttsaurus.ingameinfo.config.IgiDefaultLifecycleProviderConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.time.StopWatch;
import org.lwjgl.opengl.*;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.tttsaurus.ingameinfo.common.core.render.CommonBuffers.FLOAT_BUFFER_16;
import static com.tttsaurus.ingameinfo.common.core.render.CommonBuffers.INT_BUFFER_16;

public final class DefaultLifecycleProvider extends GuiLifecycleProvider
{
    //<editor-fold desc="fixed update timing variables">
    // units are all in second
    private final StopWatch stopwatch_FixedUpdate = new StopWatch();
    private int estimatedFps_FixedUpdate = 0;
    private double deltaTime_FixedUpdate = 0d;
    private double excessTime_FixedUpdate = 0d;
    //</editor-fold>

    //<editor-fold desc="render update timing variables">
    // units are all in second
    private final StopWatch stopwatch_RenderUpdate = new StopWatch();
    private int estimatedFps_RenderUpdate = 0;
    private double excessTime_RenderUpdate = 0d;
    private int estimatedUnlimitedFps = 1;
    private float estimatedFboRefreshRate = 0f;
    private float renderUpdateAlpha = 0f;
    //</editor-fold>

    //<editor-fold desc="fbo variables">
    private boolean enableFbo = true;
    public void setEnableFbo(boolean flag) { enableFbo = flag; }
    private boolean refreshFbo = true;
    private Framebuffer fbo = null;
    private Framebuffer shaderFbo = null;
    private boolean enableMultisampleOnFbo = true;
    public void setEnableMultisampleOnFbo(boolean flag) { enableMultisampleOnFbo = flag; }
    private Framebuffer resolvedFbo = null;
    //</editor-fold>

    //<editor-fold desc="shader variables">
    private boolean enableShader = true;
    public void setEnableShader(boolean flag) { enableShader = flag; }
    private ShaderProgram shaderProgram = null;
    private int texUnit1TextureID;
    private boolean uniformsPassed = false;
    //</editor-fold>

    //<editor-fold desc="render time debug variables">
    // units are all in nanosecond
    private boolean renderTimeDebug = false;
    public void setRenderTimeDebug(boolean flag) { renderTimeDebug = flag; }
    private final StopWatch cpuTimeStopwatch = new StopWatch();
    private final long[] cpuTimeNanoFor50Frames = new long[50];
    private final long[] gpuTimeNanoFor50Frames = new long[50];
    private int timeNanoArrayIndex = 0;
    private RandomAccessFile renderTimeDebugFile = null;
    //</editor-fold>

    //<editor-fold desc="igi event calls">
    private String lastBiomeRegistryName = "";
    private void triggerIgiEvents()
    {
        // works on sp client
        EventCenter.igiGuiFpsEvent.trigger(estimatedFps_FixedUpdate, estimatedFps_RenderUpdate);
        EventCenter.igiGuiFboRefreshRateEvent.trigger(estimatedFboRefreshRate);
        EventCenter.gameFpsEvent.trigger(Minecraft.getDebugFPS());
        Runtime runtime = Runtime.getRuntime();
        EventCenter.gameMemoryEvent.trigger(runtime.totalMemory() - runtime.freeMemory(), runtime.totalMemory());
        EntityPlayerSP player = MC.player;
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
    private double timer = 0.5f;

    @Override
    protected void fixedUpdate()
    {
        definedFixedUpdate(deltaTime_FixedUpdate);

        timer += deltaTime_FixedUpdate;
        if (timer >= 0.5d)
        {
            timer -= 0.5d;
            triggerIgiEvents();
        }
        if (timer >= 0.5f)
            timer %= 0.5f;
    }

    @Override
    protected void renderUpdate()
    {
        storeCommonGlStates();
        if (renderTimeDebug)
            renderUpdateInternalDebug();
        else
            renderUpdateInternal();
        restoreCommonGlStates();
    }

    private void renderUpdateInternal()
    {
        if (enableFbo)
        {
            if (enableShader) compileShaders();
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

        definedRenderUpdate();

        if (enableFbo)
        {
            if (enableShader)
            {
                bindShaderFbo();
                activateShaders();
                RenderUtils.renderFbo(resolution, fbo, false);
                deactivateShaders();
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

    private void renderUpdateInternalDebug()
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

        renderUpdateInternal();

        cpuTimeStopwatch.stop();
        GL15.glEndQuery(GL33.GL_TIME_ELAPSED);
        GL15.glGetQueryObject(gpuTimeQueryID, GL15.GL_QUERY_RESULT, INT_BUFFER_16);
        long gpuTimeNano = INT_BUFFER_16.get(0);
        long cpuTimeNano = cpuTimeStopwatch.getNanoTime();
        if (timeNanoArrayIndex == 50)
        {
            long avgCpuTimeNano = Arrays.stream(cpuTimeNanoFor50Frames).sum() / 50L;
            long avgGpuTimeNano = Arrays.stream(gpuTimeNanoFor50Frames).sum() / 50L;

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
    //</editor-fold>

    //<editor-fold desc="fbo methods">
    private void resolveMultisampledFbo()
    {
        // init resolvedFbo
        if (resolvedFbo == null)
        {
            resolvedFbo = new Framebuffer(MC.displayWidth, MC.displayHeight, true);
            resolvedFbo.framebufferColor[0] = 0f;
            resolvedFbo.framebufferColor[1] = 0f;
            resolvedFbo.framebufferColor[2] = 0f;
            resolvedFbo.framebufferColor[3] = 0f;
            resolvedFbo.enableStencil();
            resolvedFbo.unbindFramebuffer();
            GlResourceManager.addDisposable((IGlDisposable)resolvedFbo);
        }

        if (resolvedFbo.framebufferWidth != MC.displayWidth || resolvedFbo.framebufferHeight != MC.displayHeight)
        {
            resolvedFbo.createBindFramebuffer(MC.displayWidth, MC.displayHeight);
            resolvedFbo.unbindFramebuffer();
        }

        OpenGlHelper.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo.framebufferObject);
        OpenGlHelper.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, resolvedFbo.framebufferObject);

        GL30.glBlitFramebuffer(0, 0, resolvedFbo.framebufferWidth, resolvedFbo.framebufferHeight, 0, 0, resolvedFbo.framebufferWidth, resolvedFbo.framebufferHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
    }
    private void bindShaderFbo()
    {
        // init shaderFbo
        if (shaderFbo == null)
        {
            shaderFbo = new Framebuffer(MC.displayWidth, MC.displayHeight, true);
            shaderFbo.framebufferColor[0] = 0f;
            shaderFbo.framebufferColor[1] = 0f;
            shaderFbo.framebufferColor[2] = 0f;
            shaderFbo.framebufferColor[3] = 0f;
            shaderFbo.enableStencil();
            shaderFbo.bindFramebuffer(true);
            GlResourceManager.addDisposable((IGlDisposable)shaderFbo);
        }

        if (shaderFbo.framebufferWidth != MC.displayWidth || shaderFbo.framebufferHeight != MC.displayHeight)
        {
            shaderFbo.createBindFramebuffer(MC.displayWidth, MC.displayHeight);
            shaderFbo.bindFramebuffer(true);
        }
        else
        {
            RenderHints.clearFboWithoutUnbind();
            shaderFbo.framebufferClear();
            RenderHints.clearFboWithUnbind();
        }
    }
    private void bindFbo()
    {
        // init fbo
        if (fbo == null)
        {
            if (enableMultisampleOnFbo)
            {
                RenderHints.multisampleTexBind();
                RenderHints.multisampleFbo();
            }
            fbo = new Framebuffer(MC.displayWidth, MC.displayHeight, true);
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

        if (fbo.framebufferWidth != MC.displayWidth || fbo.framebufferHeight != MC.displayHeight)
        {
            if (enableMultisampleOnFbo)
            {
                RenderHints.multisampleTexBind();
                RenderHints.multisampleFbo();
            }
            fbo.createBindFramebuffer(MC.displayWidth, MC.displayHeight);
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
    private void bindMcFbo()
    {
        Framebuffer mcFbo = MC.getFramebuffer();

        if (mcFbo.framebufferWidth != MC.displayWidth || mcFbo.framebufferHeight != MC.displayHeight)
            mcFbo.createBindFramebuffer(MC.displayWidth, MC.displayHeight);

        mcFbo.bindFramebuffer(true);
    }
    //</editor-fold>

    //<editor-fold desc="shader methods">
    private void compileShaders()
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

            InGameInfoReborn.LOGGER.info(shaderProgram.getSetupDebugReport());
        }
    }
    private void activateShaders()
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
            shaderProgram.setUniform("enableAlpha", IgiDefaultLifecycleProviderConfig.ENABLE_PP_ALPHA);
            shaderProgram.setUniform("targetAlpha", IgiDefaultLifecycleProviderConfig.PP_ALPHA);
        }
    }
    private void deactivateShaders()
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
    private int textureID = 0;
    private float r = 0, g = 0, b = 0, a = 0;
    private boolean blend = false;
    private boolean lighting = false;
    private boolean texture2D = false;
    private boolean alphaTest = false;
    private int shadeModel = 0;
    private boolean depthTest = false;
    private boolean cullFace = false;
    private int blendSrcRgb;
    private int blendDstRgb;
    private int blendSrcAlpha;
    private int blendDstAlpha;
    private int alphaFunc;
    private float alphaRef;
    //</editor-fold>

    //<editor-fold desc="gl state management">
    private void storeCommonGlStates()
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
    private void restoreCommonGlStates()
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

    @Override
    protected boolean isUsingFramebuffer()
    {
        return enableFbo;
    }

    @Override
    protected boolean isUsingMultisampleFramebuffer()
    {
        return enableMultisampleOnFbo;
    }

    @Override
    public float getRenderLerpAlpha()
    {
        return renderUpdateAlpha;
    }

    @Override
    protected void timing()
    {
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

            reserveFixedUpdate();

            excessTime_FixedUpdate = currentTime + excessTime_FixedUpdate - timePerFrame_FixedUpdate;
        }
        if (excessTime_FixedUpdate >= timePerFrame_FixedUpdate)
            excessTime_FixedUpdate %= timePerFrame_FixedUpdate;
        //</editor-fold>

        //<editor-fold desc="render update timing">
        if (enableFbo)
        {
            if (!stopwatch_RenderUpdate.isStarted())
            {
                stopwatch_RenderUpdate.start();
                stopwatch_RenderUpdate.split();
            }

            // unit: second
            currentTime = stopwatch_RenderUpdate.getNanoTime() / 1.0E9d;
            estimatedFboRefreshRate = (Math.min(((float)estimatedFps_RenderUpdate) / ((float)estimatedUnlimitedFps), 1f) + estimatedFboRefreshRate) / 2f;
            double lastSplitTime = stopwatch_RenderUpdate.getSplitNanoTime() / 1.0E9d;
            stopwatch_RenderUpdate.split();
            if (currentTime - lastSplitTime > 0d)
                estimatedUnlimitedFps = ((int)(1d / (currentTime - lastSplitTime)) + estimatedUnlimitedFps) / 2;
            if (currentTime + excessTime_RenderUpdate >= timePerFrame_RenderUpdate)
            {
                stopwatch_RenderUpdate.stop();
                stopwatch_RenderUpdate.reset();
                stopwatch_RenderUpdate.start();
                stopwatch_RenderUpdate.split();

                estimatedFps_RenderUpdate = ((int)(1d / (currentTime + excessTime_RenderUpdate)) + estimatedFps_RenderUpdate) / 2;

                refreshFbo = true;

                double fixedUpdateTime = stopwatch_FixedUpdate.getNanoTime() / 1.0E9d;
                if (fixedUpdateTime + excessTime_FixedUpdate >= timePerFrame_FixedUpdate)
                    renderUpdateAlpha = 1f;
                else
                    renderUpdateAlpha = (float)((fixedUpdateTime + excessTime_FixedUpdate) / timePerFrame_FixedUpdate);

                excessTime_RenderUpdate = currentTime + excessTime_RenderUpdate - timePerFrame_RenderUpdate;
            }
            if (excessTime_RenderUpdate >= timePerFrame_RenderUpdate)
                excessTime_RenderUpdate %= timePerFrame_RenderUpdate;
        }
        else
        {
            double fixedUpdateTime = stopwatch_FixedUpdate.getNanoTime() / 1.0E9d;
            if (fixedUpdateTime + excessTime_FixedUpdate >= timePerFrame_FixedUpdate)
                renderUpdateAlpha = 1f;
            else
                renderUpdateAlpha = (float)((fixedUpdateTime + excessTime_FixedUpdate) / timePerFrame_FixedUpdate);
        }
        //</editor-fold>
    }
}
