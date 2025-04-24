package com.tttsaurus.ingameinfo.common.api.mvvm.binding;

import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;

public interface IReactiveCollectionGetter
{
    ReactiveCollection get(ViewModel<?> target);
}
