package com.tttsaurus.saurus3d.common.api.shader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ShaderParseUtils
{
    public static List<UniformField> getUniformFields(String shaderSource)
    {
        List<UniformField> fields = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\buniform\\s+(\\w+)\\s+(\\w+)\\s*;");
        Matcher matcher = pattern.matcher(shaderSource);

        while (matcher.find())
        {
            String type = matcher.group(1);
            String name = matcher.group(2);
            fields.add(new UniformField(type, name));
        }

        return fields;
    }
}
