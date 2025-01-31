#version 330 core

out vec2 TexCoords;

void main()
{
    if (gl_VertexID == 0)
    {
        // top-left corner
        gl_Position = vec4(-1.0, 1.0, 0.0, 1.0);
        TexCoords = vec2(0.0, 1.0);
    }
    else if (gl_VertexID == 1)
    {
        // top-right corner
        gl_Position = vec4(1.0, 1.0, 0.0, 1.0);
        TexCoords = vec2(1.0, 1.0);
    }
    else if (gl_VertexID == 2)
    {
        // bottom-right corner
        gl_Position = vec4(1.0, -1.0, 0.0, 1.0);
        TexCoords = vec2(1.0, 0.0);
    }
    else if (gl_VertexID == 3)
    {
        // bottom-left corner
        gl_Position = vec4(-1.0, -1.0, 0.0, 1.0);
        TexCoords = vec2(0.0, 0.0);
    }
}
