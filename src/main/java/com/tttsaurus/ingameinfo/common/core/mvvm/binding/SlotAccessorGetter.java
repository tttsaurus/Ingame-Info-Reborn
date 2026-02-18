package com.tttsaurus.ingameinfo.common.core.mvvm.binding;

import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;

public interface SlotAccessorGetter
{
    SlotAccessor get(ViewModel<?> target);
}
