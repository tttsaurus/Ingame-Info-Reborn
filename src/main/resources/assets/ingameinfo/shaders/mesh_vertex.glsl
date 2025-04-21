#version 330 core

layout (location = 0) in vec3 ndcPos;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

uniform mat4 modelView;
uniform mat4 projection;
uniform vec3 camPos;
uniform vec3 targetWorldPos;
uniform float screenWidthHeightRatio;
uniform bool unprojectToWorld;

out vec2 TexCoord;
out vec3 FragNormal;

void main()
{
    if (unprojectToWorld)
    {
        vec4 anchorPos = inverse(modelView) * inverse(projection) * vec4(0, 0, -1, 1);
        anchorPos /= anchorPos.w;

        vec4 worldPos = inverse(modelView) * inverse(projection) * vec4(ndcPos.x, ndcPos.y, -1, 1);
        worldPos /= worldPos.w;

        gl_Position = projection * modelView * vec4(targetWorldPos - camPos + worldPos.xyz - anchorPos.xyz, 1);
    }
    else
    {
        vec3 adjustedNdc = vec3(ndcPos.x, ndcPos.y / screenWidthHeightRatio, 0);
        gl_Position = projection * modelView * vec4(targetWorldPos - camPos + adjustedNdc, 1);
    }

    TexCoord = texCoord;
    FragNormal = normal;
}
