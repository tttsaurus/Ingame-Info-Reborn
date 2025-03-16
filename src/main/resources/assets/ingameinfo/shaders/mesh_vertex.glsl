#version 330 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

out vec2 TexCoord;
out vec3 FragNormal;

void main()
{
    gl_Position = vec4(pos, 1.0);

    TexCoord = texCoord;
    FragNormal = normal;
}
