package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import com.tttsaurus.ingameinfo.common.core.render.Mesh;
import com.tttsaurus.ingameinfo.common.core.render.shader.Shader;
import com.tttsaurus.ingameinfo.common.core.render.shader.ShaderLoadingUtils;
import com.tttsaurus.ingameinfo.common.core.render.shader.ShaderProgram;
import org.lwjgl.util.vector.Vector3f;
import java.nio.FloatBuffer;

public class Mesh2DRenderer extends MeshRenderer
{
    public Mesh2DRenderer(Mesh mesh)
    {
        super(mesh, null);
        Shader frag = ShaderLoadingUtils.load("ingameinfo:shaders/mesh2d_frag.glsl", Shader.ShaderType.FRAGMENT);
        Shader vertex = ShaderLoadingUtils.load("ingameinfo:shaders/mesh2d_vertex.glsl", Shader.ShaderType.VERTEX);
        ShaderProgram program = new ShaderProgram(frag, vertex);
        program.setup();
        shaderProgram = program;
    }

    public Mesh2DRenderer setUniform_InWorld(boolean inWorld)
    {
        shaderProgram.setUniform("inWorld", inWorld);
        return this;
    }
    public Mesh2DRenderer setUniform_ModelView(FloatBuffer modelView)
    {
        shaderProgram.setUniform("modelView", modelView);
        return this;
    }
    public Mesh2DRenderer setUniform_Projection(FloatBuffer projection)
    {
        shaderProgram.setUniform("projection", projection);
        return this;
    }
    public Mesh2DRenderer setUniform_Transformation(FloatBuffer transformation)
    {
        shaderProgram.setUniform("transformation", transformation);
        return this;
    }
    public Mesh2DRenderer setUniform_CamPos(Vector3f camPos)
    {
        shaderProgram.setUniform("camPos", camPos.x, camPos.y, camPos.z);
        return this;
    }
    public Mesh2DRenderer setUniform_TargetWorldPos(Vector3f targetWorldPos)
    {
        shaderProgram.setUniform("targetWorldPos", targetWorldPos.x, targetWorldPos.y, targetWorldPos.z);
        return this;
    }
    public Mesh2DRenderer setUniform_UnprojectToWorld(boolean unprojectToWorld)
    {
        shaderProgram.setUniform("unprojectToWorld", unprojectToWorld);
        return this;
    }
    public Mesh2DRenderer setUniform_ScreenWidthHeightRatio(float screenWidthHeightRatio)
    {
        shaderProgram.setUniform("screenWidthHeightRatio", screenWidthHeightRatio);
        return this;
    }
}
