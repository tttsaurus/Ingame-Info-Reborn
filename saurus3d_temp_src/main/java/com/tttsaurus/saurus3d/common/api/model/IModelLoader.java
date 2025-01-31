package com.tttsaurus.saurus3d.common.api.model;

import javax.annotation.Nullable;

public interface IModelLoader
{
    @Nullable
    Mesh load(String rl);
}
