#version 330 core

layout (location = 0) in vec3 ndcPos;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

uniform mat4 modelView;
uniform mat4 projection;
//uniform mat4 transformation;
uniform vec3 camPos;
uniform vec3 targetWorldPos;
uniform float screenWidthHeightRatio;

out vec2 TexCoord;
out vec3 FragNormal;

void main()
{
    vec2 fixedNdc = vec2(ndcPos.x, ndcPos.y / screenWidthHeightRatio);

//    vec3 right = vec3(modelViewInverse[0][0], modelViewInverse[1][0], modelViewInverse[2][0]);
//    vec3 up = vec3(modelViewInverse[0][1], modelViewInverse[1][1], modelViewInverse[2][1]);
//
//    float scaleX = 0.1;
//    float scaleY = 0.1;
//    vec3 offset = fixedNdc.x * right * scaleX + fixedNdc.y * up * scaleY;

    gl_Position = projection * modelView * vec4(targetWorldPos - camPos + vec3(fixedNdc, 0), 1);

    TexCoord = texCoord;
    FragNormal = normal;
}
