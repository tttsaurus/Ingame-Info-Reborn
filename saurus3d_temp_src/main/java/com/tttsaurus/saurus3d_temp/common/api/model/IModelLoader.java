package com.tttsaurus.saurus3d_temp.common.api.model;

import javax.annotation.Nullable;

public interface IModelLoader
{
    @Nullable
    Mesh load(String rl);
}
