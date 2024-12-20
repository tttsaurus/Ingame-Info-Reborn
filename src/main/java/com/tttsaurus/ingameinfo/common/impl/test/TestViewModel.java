package com.tttsaurus.ingameinfo.common.impl.test;

import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;

public class TestViewModel extends ViewModel<TestView>
{
    @Reactive(targetUid = "AAA", property = "text")
    public ReactiveObject<String> testString;
}
