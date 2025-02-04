#version 330 core

uniform sampler2D screenTexture;

in vec2 TexCoords;
out vec4 FragColor;

void main()
{
    vec4 finalFragColor;

    if (texture(screenTexture, TexCoords).a == 0.0)
        finalFragColor = vec4(texture(screenTexture, TexCoords).rgb, 0.0);
    else
        finalFragColor = vec4(texture(screenTexture, TexCoords).rgb, 0.5);

    FragColor = finalFragColor;
}
