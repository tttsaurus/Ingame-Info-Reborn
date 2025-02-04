package com.tttsaurus.saurus3d_temp.common.impl.shader;

import com.tttsaurus.saurus3d_temp.common.api.shader.IShaderLoader;
import com.tttsaurus.saurus3d_temp.common.api.shader.Shader;
import com.tttsaurus.saurus3d_temp.common.api.reader.RlReaderUtils;

public class ShaderLoader implements IShaderLoader
{
    @Override
    public Shader load(String rl, Shader.ShaderType shaderType)
    {
        String raw = RlReaderUtils.read(rl, true);
        if (raw.isEmpty()) return null;

        return new Shader(raw, shaderType);
    }
}
