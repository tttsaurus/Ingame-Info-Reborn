#version 330 core

layout (location = 0) in vec3 ndcPos;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

// ------ In World Rendering Parameters ------

uniform bool inWorld;

uniform mat4 modelView;
uniform mat4 projection;

// in world local transformation
uniform mat4 transformation;
uniform vec3 camPos;
uniform vec3 targetWorldPos;

uniform bool unprojectToWorld;
// required when not unprojecting to the world
uniform float screenWidthHeightRatio;

// ------------

out vec2 TexCoord;
out vec3 FragNormal;

void main()
{
    if (inWorld)
    {
        if (unprojectToWorld)
        {
            vec4 anchorPos = inverse(modelView) * inverse(projection) * vec4(0, 0, -1, 1);
            anchorPos /= anchorPos.w;
            vec4 worldPos = inverse(modelView) * inverse(projection) * vec4(ndcPos.x, ndcPos.y, -1, 1);
            worldPos /= worldPos.w;
            vec4 transformed = transformation * vec4(worldPos.xyz - anchorPos.xyz, 1);
            transformed /= transformed.w;

            gl_Position = projection * modelView * vec4(targetWorldPos - camPos + transformed.xyz, 1);
        }
        else
        {
            vec3 adjustedNdc = vec3(ndcPos.x, ndcPos.y / screenWidthHeightRatio, 0);
            vec4 transformed = transformation * vec4(adjustedNdc, 1);
            transformed /= transformed.w;

            gl_Position = projection * modelView * vec4(targetWorldPos - camPos + transformed.xyz, 1);
        }
    }
    else
    {
        gl_Position = vec4(ndcPos.x, ndcPos.y, 0, 1);
    }

    TexCoord = texCoord;
    FragNormal = normal;
}
