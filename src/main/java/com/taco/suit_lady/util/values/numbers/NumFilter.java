package com.taco.suit_lady.util.values.numbers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public enum NumFilter {
    
    THROW_NPE("Throw NullPointerException"),
    THROW_NFE("Throw NumberFormatException"),
    ALLOW_INFINITY("Allow Infinity"),
    ALLOW_NaN("Allow NaN"),
    INT_ONLY("Allow Decimal");
    
    //
    
    private final String key;
    
    NumFilter(String key) {
        this.key = key;
    }
    
    @Contract(pure = true)
    public final @NotNull String key() {
        return this.key.toLowerCase();
    }
    
    public boolean matches(String[] args) {
        final List<String> argsList = Arrays.asList(args);
        return matches(argsList);
    }
    
    public boolean matches(@NotNull List<String> argsList) {
        for (int i = 0; i < argsList.size(); i++)
            argsList.set(i, argsList.get(i).toLowerCase());
        return argsList.contains(key());
    }
}
