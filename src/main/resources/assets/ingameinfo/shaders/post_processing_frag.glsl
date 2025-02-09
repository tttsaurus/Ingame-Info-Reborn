#version 330 core

uniform sampler2D screenTexture;
uniform bool enableAlpha;
uniform float targetAlpha;

in vec2 TexCoords;
out vec4 FragColor;

void main()
{
    float texAlpha = texture(screenTexture, TexCoords).a;
    vec4 finalFragColor = vec4(texture(screenTexture, TexCoords).rgb, texAlpha);

    if (enableAlpha)
    {
        if (texAlpha == 0.0)
            finalFragColor = vec4(texture(screenTexture, TexCoords).rgb, 0.0);
        else
            finalFragColor = vec4(texture(screenTexture, TexCoords).rgb, targetAlpha * texAlpha);
    }

    FragColor = finalFragColor;
}
