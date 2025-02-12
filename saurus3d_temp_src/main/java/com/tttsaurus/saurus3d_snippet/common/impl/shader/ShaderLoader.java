package com.tttsaurus.saurus3d_snippet.common.impl.shader;

import com.tttsaurus.saurus3d_snippet.common.api.shader.IShaderLoader;
import com.tttsaurus.saurus3d_snippet.common.api.shader.Shader;
import com.tttsaurus.saurus3d_snippet.common.api.reader.RlReaderUtils;

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
