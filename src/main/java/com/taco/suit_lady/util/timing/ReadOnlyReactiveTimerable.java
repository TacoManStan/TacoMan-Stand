package com.taco.suit_lady.util.timing;

import javafx.beans.property.ReadOnlyObjectProperty;

public interface ReadOnlyReactiveTimerable
{
    ReadOnlyObjectProperty<Runnable> onTimeoutProperty();
    Runnable getOnTimeout();
}
