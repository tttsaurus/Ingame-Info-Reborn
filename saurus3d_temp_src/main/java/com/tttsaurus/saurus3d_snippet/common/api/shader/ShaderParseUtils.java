package com.tttsaurus.saurus3d_snippet.common.api.shader;

import com.tttsaurus.saurus3d_snippet.common.api.shader.uniform.UniformField;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ShaderParseUtils
{
    public static List<UniformField> getUniformFields(String shaderSource)
    {
        List<UniformField> fields = new ArrayList<>();

        /*

        layout(std140) uniform MyUniformBlock {
            mat4 projectionMatrix;
            vec3 lightPosition;
            float time;
        };

        doesn't support uniform block for now
        */

        Pattern pattern = Pattern.compile("\\buniform\\s+(\\w+)\\s+(\\w+)(\\[\\d+\\])*\\s*;");
        Matcher matcher = pattern.matcher(shaderSource);

        while (matcher.find())
        {
            String type = matcher.group(1);
            String name = matcher.group(2);
            String arr = matcher.group(3);
            if (arr != null) name += arr;
            fields.add(new UniformField(type, name));
        }

        return fields;
    }
}
