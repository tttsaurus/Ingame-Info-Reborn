#version 330 core

layout (location = 0) in vec3 ndcPos;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

uniform mat4 modelView;
uniform mat4 projection;
uniform mat4 transformation;
uniform vec3 camPos;
uniform vec3 targetWorldPos;

out vec2 TexCoord;
out vec3 FragNormal;

void main()
{
    vec4 worldPos = inverse(modelView) * inverse(projection) * vec4(ndcPos.x, ndcPos.y, 0, 1);
    worldPos /= worldPos.w;
    gl_Position = projection * modelView * transformation * vec4(worldPos.xyz - camPos + targetWorldPos, 1);

    TexCoord = texCoord;
    FragNormal = normal;
}
