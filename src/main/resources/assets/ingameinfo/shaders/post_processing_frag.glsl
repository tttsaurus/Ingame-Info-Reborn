#if USE_MULTISAMPLE
    uniform sampler2DMS screenTexture;
#else
    uniform sampler2D screenTexture;
#endif

uniform bool enableAlpha;
uniform float targetAlpha;

in vec2 TexCoords;
out vec4 FragColor;

void main()
{
    float alpha;
    vec3 color;
    vec4 finalFragColor;

#if USE_MULTISAMPLE
    ivec2 texelPos = ivec2(gl_FragCoord.xy);

    int sampleIndex = 0;

    alpha = texelFetch(screenTexture, texelPos, sampleIndex).a;
    color = texelFetch(screenTexture, texelPos, sampleIndex).rgb;
#else
    alpha = texture(screenTexture, TexCoords).a;
    color = texture(screenTexture, TexCoords).rgb;
#endif

    finalFragColor = vec4(color, alpha);

    if (enableAlpha)
    {
        if (alpha == 0.0)
            finalFragColor = vec4(color, 0.0);
        else
            finalFragColor = vec4(color, targetAlpha * alpha);
    }

    FragColor = finalFragColor;
}
