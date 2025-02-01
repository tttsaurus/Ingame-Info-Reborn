#version 330 core

uniform sampler2D screenTexture;

in vec2 TexCoords;
out vec4 FragColor;

void main()
{
    // params
    float FXAA_SPAN_MAX = 5.0;
    float FXAA_REDUCE_MUL = 1.0 / 4.0;
    float FXAA_REDUCE_MIN = 1.0 / 64.0;

    bool EDGE_SMOOTHING = true;
    float EDGE_ALPHA_VALUE = 0.75;

    vec2 size = 1.0 / textureSize(screenTexture, 0);

    vec3 color = texture(screenTexture, TexCoords).rgb;
    vec3 colorUp = texture(screenTexture, TexCoords + vec2(1.0, 0.0) * size).rgb;
    vec3 colorDown = texture(screenTexture, TexCoords - vec2(1.0, 0.0) * size).rgb;
    vec3 colorRight = texture(screenTexture, TexCoords + vec2(0.0, 1.0) * size).rgb;
    vec3 colorLeft = texture(screenTexture, TexCoords - vec2(0.0, 1.0) * size).rgb;

    float alpha = texture(screenTexture, TexCoords).a;
    float alphaUp = texture(screenTexture, TexCoords + vec2(1.0, 0.0) * size).a;
    float alphaDown = texture(screenTexture, TexCoords - vec2(1.0, 0.0) * size).a;
    float alphaRight = texture(screenTexture, TexCoords + vec2(0.0, 1.0) * size).a;
    float alphaLeft = texture(screenTexture, TexCoords - vec2(0.0, 1.0) * size).a;

    if (alpha != 0.0 && (alphaUp == 0.0 || alphaDown == 0.0 || alphaRight == 0.0 || alphaLeft == 0.0))
    {
        FragColor = vec4(color, alpha);
        return;
    }

    if (alpha == 0.0 && EDGE_SMOOTHING)
    {
        vec3 mixColors[4];
        int index = 0;

        if (alphaUp != 0.0)
        {
            mixColors[index] = colorUp;
            index++;
        }
        if (alphaDown != 0.0)
        {
            mixColors[index] = colorDown;
            index++;
        }
        if (alphaRight != 0.0)
        {
            mixColors[index] = colorRight;
            index++;
        }
        if (alphaLeft != 0.0)
        {
            mixColors[index] = colorLeft;
            index++;
        }

        if (index != 0)
        {
            vec3 mixedColor = vec3(0.0, 0.0, 0.0);
            for (int i = 0; i < index; i++)
            {
                mixedColor += mixColors[i];
            }
            mixedColor /= float(index);

            float alphaDifference = max(max(abs(alpha - alphaLeft), abs(alpha - alphaRight)), max(abs(alpha - alphaUp), abs(alpha - alphaDown)));

            if (alphaDifference != 0)
            {
                alpha = alphaDifference * EDGE_ALPHA_VALUE;
                FragColor = vec4(mixedColor, alpha);
                return;
            }
        }
    }

    vec3 luma = vec3(0.299, 0.587, 0.114);
    float lumaColor = dot(color, luma);
    float lumaUp = dot(colorUp, luma);
    float lumaDown = dot(colorDown, luma);
    float lumaRight = dot(colorRight, luma);
    float lumaLeft = dot(colorLeft, luma);

    float edge = max(abs(lumaColor - lumaUp), abs(lumaColor - lumaDown));
    edge = max(edge, max(abs(lumaColor - lumaRight), abs(lumaColor - lumaLeft)));
    edge = max(edge, FXAA_REDUCE_MIN);

    vec3 result = color + (colorUp + colorDown + colorRight + colorLeft - 4.0 * color) * FXAA_REDUCE_MUL;
    result = mix(color, result, step(FXAA_REDUCE_MIN, edge));

    FragColor = vec4(result, alpha);
}

