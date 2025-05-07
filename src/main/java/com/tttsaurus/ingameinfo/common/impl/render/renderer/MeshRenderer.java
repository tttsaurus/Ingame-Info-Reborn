package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import com.tttsaurus.ingameinfo.common.core.render.renderer.IRenderer;
import com.tttsaurus.ingameinfo.common.core.render.Mesh;
import com.tttsaurus.ingameinfo.common.core.render.shader.ShaderProgram;

public class MeshRenderer implements IRenderer
{
    public Mesh getMesh() { return mesh; }
    public ShaderProgram getShaderProgram() { return shaderProgram; }

    protected Mesh mesh;
    protected ShaderProgram shaderProgram;

    public MeshRenderer(Mesh mesh, ShaderProgram shaderProgram)
    {
        this.mesh = mesh;
        this.shaderProgram = shaderProgram;
    }

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
