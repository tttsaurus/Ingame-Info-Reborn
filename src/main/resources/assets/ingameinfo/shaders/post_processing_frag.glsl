#version 330 core

uniform sampler2D screenTexture;

in vec2 TexCoords;
out vec4 FragColor;

void main()
{
    if (texture(screenTexture, TexCoords).a == 0.0)
        FragColor = vec4(texture(screenTexture, TexCoords).rgb, 0.0);
    else
        FragColor = vec4(texture(screenTexture, TexCoords).rgb, 0.5);
}
