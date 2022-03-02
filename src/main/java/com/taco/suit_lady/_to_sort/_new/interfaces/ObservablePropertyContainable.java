package com.taco.suit_lady._to_sort._new.interfaces;

import com.taco.suit_lady.util.tools.Exceptions;
import javafx.beans.Observable;
import javafx.beans.binding.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface ObservablePropertyContainable
{
    @NotNull Observable[] properties();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default @NotNull BooleanBinding createBooleanBinding(@NotNull Callable<Boolean> func)
    {
        return Bindings.createBooleanBinding(Exceptions.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull DoubleBinding createDoubleBinding(@NotNull Callable<Double> func)
    {
        return Bindings.createDoubleBinding(Exceptions.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull FloatBinding createFloatBinding(@NotNull Callable<Float> func)
    {
        return Bindings.createFloatBinding(Exceptions.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull IntegerBinding createIntegerBinding(@NotNull Callable<Integer> func)
    {
        return Bindings.createIntegerBinding(Exceptions.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull LongBinding createLongBinding(@NotNull Callable<Long> func)
    {
        return Bindings.createLongBinding(Exceptions.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull <T> ObjectBinding<T> createObjectBinding(@NotNull Callable<T> func)
    {
        return Bindings.createObjectBinding(Exceptions.nullCheck(func, "Callable Function"), properties());
    }
    
    //</editor-fold>
}
