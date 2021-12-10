package com.taco.suit_lady.util.timing;

public interface ReactiveTimerable
    extends ReadOnlyReactiveTimerable
{
    void setOnTimeout(Runnable onTimeout);
}
