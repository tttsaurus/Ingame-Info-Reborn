package com.tttsaurus.ingameinfo.common.impl.render;

import com.tttsaurus.ingameinfo.common.api.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.api.render.IGlDisposable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class Mesh implements IGlDisposable
{
    private final float[] vertices;
    private final int[] indices;

    private final int verticesLength;
    private final int indicesLength;
    private int vao;
    private int vbo;
    private int ebo;

    public int getVerticesLength() { return verticesLength; }
    public int getIndicesLength() { return indicesLength; }
    public int getVao() { return vao; }
    public int getVbo() { return vbo; }
    public int getEbo() { return ebo; }

    public Mesh(float[] vertices, int[] indices)
    {
        this.vertices = vertices;
        this.indices = indices;
        verticesLength = vertices.length;
        indicesLength = indices.length;
    }

    public void setup()
    {
        int prevVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int prevVbo = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        int prevEbo = GL11.glGetInteger(GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING);

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertices.length * Float.BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices).flip();

        IntBuffer indexBuffer = ByteBuffer.allocateDirect(indices.length * Integer.BYTES)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        indexBuffer.put(indices).flip();

        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        ebo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 0);  // First 3 floats for position
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);  // Next 2 floats for texCoord
        GL20.glEnableVertexAttribArray(1);

        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 5 * Float.BYTES);  // Last 3 floats for normal
        GL20.glEnableVertexAttribArray(2);

        GL30.glBindVertexArray(prevVao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, prevVbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, prevEbo);

        GlResourceManager.addDisposable(this);
    }

    public void render()
    {
        int prevVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);

        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesLength, GL11.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(prevVao);
    }

    public void dispose()
    {
        GL30.glDeleteVertexArrays(vao);
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(ebo);
    }
}
