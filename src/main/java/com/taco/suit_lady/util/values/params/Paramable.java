package com.taco.suit_lady.util.values.params;

import com.taco.suit_lady.util.values.Value2D;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Paramable<K> {
    
    @NotNull List<Value2D<K, Class<?>>> requiredParams();
}
