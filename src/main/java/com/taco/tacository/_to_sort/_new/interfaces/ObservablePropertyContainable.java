package com.taco.tacository._to_sort._new.interfaces;

import com.taco.tacository._to_sort._new.ObservablePropertyContainer;
import com.taco.tacository.util.tools.Exc;
import javafx.beans.Observable;
import javafx.beans.binding.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

/**
 * <p>Defines an {@link #properties() array} of {@link Observable} objects for this {@link ObservablePropertyContainable} instance.</p>
 * <p><b>Binding Factories</b></p>
 * <ol>
 *     <li>Use any of the provided {@code default} methods to construct a new {@link Binding}.</li>
 *     <li>Each {@link Binding} constructed by any of the provided {@code default} methods are automatically updated when any of the {@link Observable} values contained within the {@link #properties() Property Array} are changed.</li>
 *     <li>
 *         <b>Binding Factory Methods</b>
 *         <ul>
 *             <li><i>{@link #createBooleanBinding(Callable)}</i></li>
 *             <li><i>{@link #createDoubleBinding(Callable)}</i></li>
 *             <li><i>{@link #createFloatBinding(Callable)}</i></li>
 *             <li><i>{@link #createIntegerBinding(Callable)}</i></li>
 *             <li><i>{@link #createLongBinding(Callable)}</i></li>
 *             <li><i>{@link #createStringBinding(Callable)}</i></li>
 *             <li><i>{@link #createObjectBinding(Callable)}</i></li>
 *         </ul>
 *     </li>
 * </ol>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>By contract, the <i>{@link #properties()}</i> method should return an {@code array} containing all {@link Observable} members of this {@link ObservablePropertyContainable} implementation.</li>
 *     <li>In niche instances in which one or more {@link Observable} members of this {@link ObservablePropertyContainable} implementation are not present in the returned {@code array}, this information must be specified in the {@code Javadocs} of that particular {@link ObservablePropertyContainable} implementation.</li>
 *     <li>An {@link ObservablePropertyContainer} can be used to provide additional functionality to an {@link ObservablePropertyContainable} implementation.</li>
 * </ol>
 *
 * @see ObservablePropertyContainer
 */
@FunctionalInterface
public interface ObservablePropertyContainable {
    
    @NotNull Observable[] properties();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default @NotNull BooleanBinding createBooleanBinding(@NotNull Callable<Boolean> func) { return Bindings.createBooleanBinding(Exc.nullCheck(func, "Callable Function"), properties()); }
    
    default @NotNull IntegerBinding createIntegerBinding(@NotNull Callable<Integer> func) { return Bindings.createIntegerBinding(Exc.nullCheck(func, "Callable Function"), properties()); }
    default @NotNull LongBinding createLongBinding(@NotNull Callable<Long> func) { return Bindings.createLongBinding(Exc.nullCheck(func, "Callable Function"), properties()); }
    default @NotNull FloatBinding createFloatBinding(@NotNull Callable<Float> func) { return Bindings.createFloatBinding(Exc.nullCheck(func, "Callable Function"), properties()); }
    default @NotNull DoubleBinding createDoubleBinding(@NotNull Callable<Double> func) { return Bindings.createDoubleBinding(Exc.nullCheck(func, "Callable Function"), properties()); }
    
    default @NotNull StringBinding createStringBinding(@NotNull Callable<String> func) { return Bindings.createStringBinding(Exc.nullCheck(func, "Callable Function"), properties()); }
    default @NotNull <T> ObjectBinding<T> createObjectBinding(@NotNull Callable<T> func) { return Bindings.createObjectBinding(Exc.nullCheck(func, "Callable Function"), properties()); }
    
    //</editor-fold>
}
