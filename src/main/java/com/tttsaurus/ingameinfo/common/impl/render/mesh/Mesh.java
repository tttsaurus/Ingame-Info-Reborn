package com.tttsaurus.ingameinfo.common.impl.render.mesh;

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

public class Mesh implements IGlDisposable
{
    private float[] vertices;
    private int[] indices;

    private ByteBuffer vertexBuffer;
    private ByteBuffer indexBuffer;

    private boolean setup;
    private int eboIndexOffset;

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
    public boolean getSetup() { return setup; }
    protected int getEboIndexOffset() { return eboIndexOffset; }
    protected void setEboIndexOffset(int offset) { eboIndexOffset = offset; }

    public Mesh(float[] vertices, int[] indices)
    {
        this.vertices = vertices;
        this.indices = indices;
        verticesLength = vertices.length;
        indicesLength = indices.length;
        setup = false;
        eboIndexOffset = 0;
    }

    public void setup()
    {
        if (setup) return;

        int prevVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int prevVbo = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        int prevEbo = GL11.glGetInteger(GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING);

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * Float.BYTES).order(ByteOrder.nativeOrder());
        vertexBuffer.asFloatBuffer().put(vertices).flip();

        indexBuffer = ByteBuffer.allocateDirect(indices.length * Integer.BYTES).order(ByteOrder.nativeOrder());
        indexBuffer.asIntBuffer().put(indices).flip();

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

        vertices = null;
        indices = null;

        setup = true;

        GlResourceManager.addDisposable(this);
    }

    public void updateVerticesByMappedBuffer(float[] newVertices)
    {
        if (!setup)
            throw new IllegalArgumentException("This mesh isn't set up so you can't update");
        if (newVertices.length != verticesLength)
            throw new IllegalArgumentException("New vertex array length must match existing length");

        int prevVbo = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        ByteBuffer mappedBuffer = GL30.glMapBufferRange(GL15.GL_ARRAY_BUFFER, 0, (long) verticesLength * Float.BYTES, GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_UNSYNCHRONIZED_BIT, vertexBuffer);

        if (mappedBuffer != null)
        {
            mappedBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer().put(newVertices);
            GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
        }
        else
            throw new RuntimeException("Failed to map buffer");

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, prevVbo);
    }
    public void updateVerticesByBufferSubData(float[] newVertices)
    {
        if (!setup)
            throw new IllegalArgumentException("This mesh isn't set up so you can't update");
        if (newVertices.length != verticesLength)
            throw new IllegalArgumentException("New vertex array length must match existing length");

        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(newVertices.length * Float.BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(newVertices).flip();

        int prevVbo = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertexBuffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, prevVbo);
    }
    public void updateIndicesByMappedBuffer(int[] newIndices)
    {
        if (!setup)
            throw new IllegalArgumentException("This mesh isn't set up so you can't update");
        if (newIndices.length != indicesLength)
            throw new IllegalArgumentException("New index array length must match existing length");

        int prevEbo = GL11.glGetInteger(GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);

        ByteBuffer mappedBuffer = GL30.glMapBufferRange(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, (long) indicesLength * Integer.BYTES, GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_UNSYNCHRONIZED_BIT, indexBuffer);

        if (mappedBuffer != null)
        {
            mappedBuffer.order(ByteOrder.nativeOrder()).asIntBuffer().put(newIndices);
            GL15.glUnmapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER);
        }
        else
            throw new RuntimeException("Failed to map buffer");

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, prevEbo);
    }
    public void updateIndicesByBufferSubData(int[] newIndices)
    {
        if (!setup)
            throw new IllegalArgumentException("This mesh isn't set up so you can't update");
        if (newIndices.length != indicesLength)
            throw new IllegalArgumentException("New index array length must match existing length");

        IntBuffer indexBuffer = ByteBuffer.allocateDirect(newIndices.length * Integer.BYTES)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        indexBuffer.put(newIndices).flip();

        int prevEbo = GL11.glGetInteger(GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, indexBuffer);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, prevEbo);
    }

    public void render()
    {
        if (!setup) return;

        int prevVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int prevEbo = GL11.glGetInteger(GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING);

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);

        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesLength, GL11.GL_UNSIGNED_INT, eboIndexOffset);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, prevEbo);
        GL30.glBindVertexArray(prevVao);
    }

    public void dispose()
    {
        GL30.glDeleteVertexArrays(vao);
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(ebo);
        vertexBuffer = null;
        indexBuffer = null;
        setup = false;
    }
}
