package com.taco.suit_lady._to_sort._new.interfaces;

import com.taco.suit_lady.util.tools.Exc;
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
        return Bindings.createBooleanBinding(Exc.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull DoubleBinding createDoubleBinding(@NotNull Callable<Double> func)
    {
        return Bindings.createDoubleBinding(Exc.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull FloatBinding createFloatBinding(@NotNull Callable<Float> func)
    {
        return Bindings.createFloatBinding(Exc.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull IntegerBinding createIntegerBinding(@NotNull Callable<Integer> func)
    {
        return Bindings.createIntegerBinding(Exc.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull LongBinding createLongBinding(@NotNull Callable<Long> func)
    {
        return Bindings.createLongBinding(Exc.nullCheck(func, "Callable Function"), properties());
    }
    
    default @NotNull <T> ObjectBinding<T> createObjectBinding(@NotNull Callable<T> func)
    {
        return Bindings.createObjectBinding(Exc.nullCheck(func, "Callable Function"), properties());
    }
    
    //</editor-fold>
}
