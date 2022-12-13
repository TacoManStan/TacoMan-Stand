package com.taco.tacository.logic;

import com.taco.tacository.ui.Content;
import org.jetbrains.annotations.NotNull;

public interface ContentComponent<T extends Content<?, ?, ?, ?, ?>> {
    T getContent();
}
