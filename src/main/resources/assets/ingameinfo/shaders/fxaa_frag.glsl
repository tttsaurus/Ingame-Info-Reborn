#version 330 core

uniform sampler2D screenTexture;
in vec2 TexCoords;
out vec4 FragColor;

void main()
{
    FragColor = texture(screenTexture, TexCoords);
    //FragColor = vec4(1.0, 0.5, 0.2, 1.0);
}
