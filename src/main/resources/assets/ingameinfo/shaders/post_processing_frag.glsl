#version 400 core

uniform sampler2DMS screenTexture;
uniform bool enableAlpha;
uniform float targetAlpha;

in vec2 TexCoords;
out vec4 FragColor;

void main()
{
    ivec2 texelPos = ivec2(gl_FragCoord.xy);

    int sampleIndex = 0;

    float alpha = texelFetch(screenTexture, texelPos, sampleIndex).a;
    vec3 color = texelFetch(screenTexture, texelPos, sampleIndex).rgb;

    vec4 finalFragColor = vec4(color, alpha);

    if (enableAlpha)
    {
        if (alpha == 0.0)
            finalFragColor = vec4(color, 0.0);
        else
            finalFragColor = vec4(color, targetAlpha * alpha);
    }

    FragColor = finalFragColor;
}
