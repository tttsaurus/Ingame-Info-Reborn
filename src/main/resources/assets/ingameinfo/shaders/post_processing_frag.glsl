#if USE_MULTISAMPLE
    uniform sampler2DMS screenTexture;
    uniform int sampleNum;
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

    float alphas = 0f;
    vec3 colors = vec3(0f, 0f, 0f);

    for (int i = 0; i < sampleNum; i++)
    {
        alphas += texelFetch(screenTexture, texelPos, i).a;
        colors += texelFetch(screenTexture, texelPos, i).rgb;
    }

    alpha = alphas / float(sampleNum);
    color = colors / float(sampleNum);
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
