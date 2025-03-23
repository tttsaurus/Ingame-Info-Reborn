package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import com.tttsaurus.ingameinfo.common.api.render.renderer.IRenderer;
import com.tttsaurus.ingameinfo.common.impl.render.mesh.Mesh;
import com.tttsaurus.saurus3d_snippet.common.api.shader.ShaderProgram;

public class MeshRenderer implements IRenderer
{
    private Mesh mesh = null;
    private ShaderProgram shaderProgram = null;

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
