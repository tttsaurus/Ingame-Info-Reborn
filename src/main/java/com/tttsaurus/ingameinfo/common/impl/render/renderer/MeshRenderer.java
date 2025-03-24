package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import com.tttsaurus.ingameinfo.common.api.render.renderer.IRenderer;
import com.tttsaurus.ingameinfo.common.impl.render.mesh.Mesh;
import com.tttsaurus.saurus3d_snippet.common.api.shader.Shader;
import com.tttsaurus.saurus3d_snippet.common.api.shader.ShaderProgram;
import com.tttsaurus.saurus3d_snippet.common.impl.shader.ShaderLoader;

public class MeshRenderer implements IRenderer
{
    public static ShaderProgram SHARED_MESH_SHADER_PROGRAM;
    static
    {
        ShaderLoader loader = new ShaderLoader();
        Shader frag = loader.load("ingameinfo:shaders/mesh_frag.glsl", Shader.ShaderType.FRAGMENT);
        Shader vertex = loader.load("ingameinfo:shaders/mesh_vertex.glsl", Shader.ShaderType.VERTEX);
        SHARED_MESH_SHADER_PROGRAM = new ShaderProgram(frag, vertex);
        SHARED_MESH_SHADER_PROGRAM.setup();
    }

    public Mesh mesh;
    public ShaderProgram shaderProgram;

    public MeshRenderer(Mesh mesh, ShaderProgram shaderProgram)
    {
        this.mesh = mesh;
        this.shaderProgram = shaderProgram;
    }

    //<editor-fold desc="getters & setters">
    public void setMesh(Mesh mesh) { this.mesh = mesh; }
    public void setShaderProgram(ShaderProgram shaderProgram) { this.shaderProgram = shaderProgram; }
    //</editor-fold>

    @Override
    public void render()
    {
        if (mesh == null || shaderProgram == null) return;
        if (!mesh.getSetup()) return;
        if (!shaderProgram.getSetup()) return;

        shaderProgram.use();
        mesh.render();
        shaderProgram.unuse();
    }
}
