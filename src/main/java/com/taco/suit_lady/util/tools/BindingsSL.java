package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.list_tools.ListsSL;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * Utility methods providing various {@link Binding} manipulations & operations, some of which are {@code passthrough methods} to functions located in the default JavaFX API {@link Bindings} class.
 * <p><b>Details</b></p>
 * <ol>
 *     <li>For functions pertaining to {@link Property Properties} and other {@link Observable Observables}, refer to {@link PropertiesSL}.</li>
 *     <li>For functions pertaining to {@link ObservableList Observable Lists} as well as {@link Collection Standard Collections}, refer to {@link ListsSL}.</li>
 * </ol>
 */
//TO-EXPAND
public class BindingsSL {
    
    //<editor-fold desc="--- CONSTANT BINDINGS ---">
    
    /**
     * <p>Constructs and then returns a {@link BooleanBinding} that is bound to the specified constant {@code value}.</p>
     *
     * @return A newly constructed {@link BooleanBinding} that is bound to the specified constant {@code value}.
     */
    public static @NotNull BooleanBinding boolBinding(boolean value) {
        return Bindings.createBooleanBinding(() -> value);
    }
    
    /**
     * <p>Constructs and then returns an {@link IntegerBinding} that is bound to the specified constant {@code value}.</p>
     *
     * @return A newly constructed {@link IntegerBinding} that is bound to the specified constant {@code value}.
     */
    public static @NotNull IntegerBinding intBinding(@NotNull Number value) {
        return Bindings.createIntegerBinding(() -> value.intValue());
    }
    
    /**
     * <p>Constructs and then returns a {@link LongBinding} that is bound to the specified constant {@code value}.</p>
     *
     * @return A newly constructed {@link LongBinding} that is bound to the specified constant {@code value}.
     */
    public static @NotNull LongBinding longBinding(@NotNull Number value) {
        return Bindings.createLongBinding(() -> value.longValue());
    }
    
    /**
     * <p>Constructs and then returns a {@link FloatBinding} that is bound to the specified constant {@code value}.</p>
     *
     * @return A newly constructed {@link FloatBinding} that is bound to the specified constant {@code value}.
     */
    public static @NotNull FloatBinding floatBinding(@NotNull Number value) {
        return Bindings.createFloatBinding(() -> value.floatValue());
    }
    
    /**
     * <p>Constructs and then returns a {@link DoubleBinding} that is bound to the specified constant {@code value}.</p>
     *
     * @return A newly constructed {@link DoubleBinding} that is bound to the specified constant {@code value}.
     */
    public static @NotNull DoubleBinding doubleBinding(@NotNull Number value) {
        return Bindings.createDoubleBinding(() -> value.doubleValue());
    }
    
    /**
     * <p>Constructs and then returns a {@link StringBinding} that is bound to the specified constant {@code value}.</p>
     *
     * @return A newly constructed {@link StringBinding} that is bound to the specified constant {@code value}.
     */
    public static @NotNull StringBinding stringBinding(String value) {
        return Bindings.createStringBinding(() -> value);
    }
    
    /**
     * <p>Constructs and then returns a {@link ObjectBinding} that is bound to the specified constant {@code value}.</p>
     *
     * @return A newly constructed {@link ObjectBinding} that is bound to the specified constant {@code value}.
     */
    public static <T> @NotNull ObjectBinding<T> objBinding(T value) {
        return Bindings.createObjectBinding(() -> value);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- DIRECT BINDINGS ---">
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull BooleanBinding boolBinding(@NotNull ObservableValue<Boolean> observableValue, Observable... dependencies) {
        return Bindings.createBooleanBinding(() -> observableValue.getValue(), ArraysSL.concat(new Observable[]{observableValue}, dependencies));
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull IntegerBinding intBinding(@NotNull ObservableValue<? extends Number> observableValue, Observable... dependencies) {
        return Bindings.createIntegerBinding(() -> observableValue.getValue().intValue(), ArraysSL.concat(new Observable[]{observableValue}, dependencies));
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull LongBinding longBinding(@NotNull ObservableValue<? extends Number> observableValue, Observable... dependencies) {
        return Bindings.createLongBinding(() -> observableValue.getValue().longValue(), ArraysSL.concat(new Observable[]{observableValue}, dependencies));
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull FloatBinding floatBinding(@NotNull ObservableValue<? extends Number> observableValue, Observable... dependencies) {
        return Bindings.createFloatBinding(() -> observableValue.getValue().floatValue(), ArraysSL.concat(new Observable[]{observableValue}, dependencies));
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull DoubleBinding doubleBinding(@NotNull ObservableValue<? extends Number> observableValue, Observable... dependencies) {
        return Bindings.createDoubleBinding(() -> observableValue.getValue().doubleValue(), ArraysSL.concat(new Observable[]{observableValue}, dependencies));
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull StringBinding stringBinding(@NotNull ObservableValue<String> observableValue, Observable... dependencies) {
        return Bindings.createStringBinding(() -> observableValue.getValue(), ArraysSL.concat(new Observable[]{observableValue}, dependencies));
    }
    
    /**
     * <p>Constructs a new {@link Binding} object bound to reflect the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} object.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The specified {@link ObservableValue} object is automatically added to the returned {@link Binding} as a dependency.</li>
     *     <li>The {@code dependencies} parameter can be used to provide additional {@link Observable} objects that will trigger a value recalculation, but this is not required (and almost always unnecessary).</li>
     * </ol>
     * <p><b>Binding Factory Method List</b></p>
     * <ol>
     *     <li>Boolean Binding: <i>{@link #boolBinding(Callable, Observable...)}</i></li>
     *     <li>Integer Binding: <i>{@link #intBinding(Callable, Observable...)}</i></li>
     *     <li>Long Binding: <i>{@link #longBinding(Callable, Observable...)}</i></li>
     *     <li>Float Binding: <i>{@link #floatBinding(Callable, Observable...)}</i></li>
     *     <li>Double Binding: <i>{@link #doubleBinding(Callable, Observable...)}</i></li>
     *     <li>String Binding: <i>{@link #stringBinding(Callable, Observable...)}</i></li>
     *     <li>Object Binding (this method): <i>{@link #objBinding(Callable, Observable...)}</i></li>
     * </ol>
     *
     * @param observableValue The {@link ObservableValue} object whose value is bound to the returned {@link Binding} instance.
     * @param dependencies    An optional varargs parameter that allows additional {@link Observable} objects that will trigger a value recalculation on the returned {@link Binding} to be added to the {@link Binding} as dependencies.
     *                        <br>This parameter is provided as its presence adds no additional parameter clutter, but it is rarely required in direct binding logic.
     *
     * @return A new {@link Binding} object bound to reflect the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} object.
     */
    public static <T> @NotNull ObjectBinding<T> objBinding(@NotNull ObservableValue<T> observableValue, Observable... dependencies) {
        return Bindings.createObjectBinding(() -> observableValue.getValue(), ArraysSL.concat(new Observable[]{observableValue}, dependencies));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- DEFAULT PASSTHROUGH BINDINGS">
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull BooleanBinding boolBinding(@NotNull Callable<Boolean> function, Observable... dependencies) {
        return Bindings.createBooleanBinding(function, dependencies);
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull IntegerBinding intBinding(@NotNull Callable<? extends Number> function, Observable... dependencies) {
        return Bindings.createIntegerBinding(() -> function.call().intValue(), dependencies);
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull LongBinding longBinding(@NotNull Callable<? extends Number> function, Observable... dependencies) {
        return Bindings.createLongBinding(() -> function.call().longValue(), dependencies);
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull FloatBinding floatBinding(@NotNull Callable<? extends Number> function, Observable... dependencies) {
        return Bindings.createFloatBinding(() -> function.call().floatValue(), dependencies);
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull DoubleBinding doubleBinding(@NotNull Callable<? extends Number> function, Observable... dependencies) {
        return Bindings.createDoubleBinding(() -> function.call().doubleValue(), dependencies);
    }
    
    /**
     * <p><i>See {@link #objBinding(Callable, Observable...)}</i></p>
     */
    public static @NotNull StringBinding stringBinding(@NotNull Callable<String> function, Observable... dependencies) {
        return Bindings.createStringBinding(function, dependencies);
    }
    
    /**
     * <p>Constructs a new {@link ObjectBinding} whose value is bound to reflect the result of the specified {@link Callable}.</p>
     * <p>The value of the returned {@link Binding} is updated when any of the specified {@link Observable} dependencies are {@link Observable#addListener(InvalidationListener) invalidated}.</p>
     *
     * @param function     The {@link Callable} used to calculate the new value of the returned {@link Binding} whenever its value is updated.
     * @param dependencies An {@code array} of {@link Observable} objects that will trigger a value recalculation on the returned {@link Binding} when any is {@link Observable#addListener(InvalidationListener) invalidated}.
     * @param <T>          The type of object the returned {@link Binding} contains.
     *
     * @return A newly-constructed {@link ObjectBinding} whose value is bound to reflect the result of the specified {@link Callable}.
     */
    public static <T> @NotNull ObjectBinding<T> objBinding(@NotNull Callable<T> function, Observable... dependencies) {
        return Bindings.createObjectBinding(function, dependencies);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- RECURSIVE BINDINGS ---">
    
    //<editor-fold desc="> Default Type Wrapper Factories">
    
    
    public static <U> @NotNull BooleanBinding boolBindingRecursive(@Nullable ReentrantLock lock, Function<U, ObservableValue<Boolean>> function, ObservableValue<U> updateObservable, Observable... dependencies) {
        return boolBinding(recursiveBinding(lock, function, updateObservable), dependencies);
    }
    
    public static <U> @NotNull IntegerBinding intBindingRecursive(@Nullable ReentrantLock lock, Function<U, ObservableValue<Number>> function, ObservableValue<U> updateObservable, Observable... dependencies) {
        return intBinding(recursiveBinding(lock, function, updateObservable), dependencies);
    }
    
    public static <U> @NotNull LongBinding longBindingRecursive(@Nullable ReentrantLock lock, Function<U, ObservableValue<Number>> function, ObservableValue<U> updateObservable, Observable... dependencies) {
        return longBinding(recursiveBinding(lock, function, updateObservable), dependencies);
    }
    
    public static <U> @NotNull FloatBinding floatBindingRecursive(@Nullable ReentrantLock lock, Function<U, ObservableValue<Number>> function, ObservableValue<U> updateObservable, Observable... dependencies) {
        return floatBinding(recursiveBinding(lock, function, updateObservable), dependencies);
    }
    
    public static <U> @NotNull DoubleBinding doubleBindingRecursive(@Nullable ReentrantLock lock, Function<U, ObservableValue<Number>> function, ObservableValue<U> updateObservable, Observable... dependencies) {
        return doubleBinding(recursiveBinding(lock, function, updateObservable), dependencies);
    }
    
    public static <U, V> @NotNull ObjectBinding<V> objBindingRecursive(@Nullable ReentrantLock lock, Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable... dependencies) {
        return objBinding(recursiveBinding(lock, function, updateObservable), dependencies);
    }
    
    //</editor-fold>
    
    /**
     * <p>Constructs a new {@link RecursiveBinding RecursiveBinding} object with a new {@link ReentrantLock}.</p>
     * <blockquote><i>See {@link RecursiveBinding} for details.</i></blockquote>
     *
     * @param function         The {@link Function} used to calculate the value of the returned {@link RecursiveBinding} object.
     * @param updateObservable The {@link ObservableValue} bound to the object containing the {@link ObservableValue} represented by the returned {@link RecursiveBinding}.
     * @param dependencies     An optional varargs of {@link Observable} objects that will cause a {@code recalculation} on the returned {@link RecursiveBinding} when {@link Observable#addListener(InvalidationListener) invalidated}.
     *
     * @return The new {@link RecursiveBinding}.
     *
     * @see RecursiveBinding
     */
    @Contract("_, _, _ -> new")
    public static <U, V> @NotNull RecursiveBinding<U, V> recursiveBinding(Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable... dependencies) {
        return recursiveBinding(new ReentrantLock(), function, updateObservable, dependencies);
    }
    
    /**
     * <p>Constructs a new {@link RecursiveBinding} with the specified {@link ReentrantLock}.</p>
     * <blockquote><i>See {@link RecursiveBinding} for details.</i></blockquote>
     *
     * @param lock             The {@code ReentrantLock} to be used to synchronize calls to the returned {@code RecursiveBinding}.
     * @param function         The {@link Function} used to calculate the value of the returned {@link RecursiveBinding} object.
     * @param updateObservable The {@link ObservableValue} bound to the object containing the {@link ObservableValue} represented by the returned {@link RecursiveBinding}.
     * @param dependencies     An optional varargs of {@link Observable} objects that will cause a {@code recalculation} on the returned {@link RecursiveBinding} when {@link Observable#addListener(InvalidationListener) invalidated}.
     *
     * @return A new {@code RecursiveBinding}.
     *
     * @see RecursiveBinding
     */
    @Contract("_, _, _, _ -> new")
    public static <U, V> @NotNull RecursiveBinding<U, V> recursiveBinding(ReentrantLock lock, Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable... dependencies) {
        return new RecursiveBinding<>(lock, function, updateObservable, dependencies);
    }
    
    /**
     * <p>Allows you to bind an {@link ObservableValue} to a dynamic {@link Binding}.</p>
     * <br><hr><br>
     * <p><b>Example Class 1:</b></p>
     * <pre><code>
     * public static class ExampleClass1 {
     *      private final {@link ObjectProperty}{@literal ExampleClass2} selectedExampleProperty;
     *
     *      public ExampleClass1(ExampleClass2 selectedExampleClass2) {
     *          this.selectedExampleProperty = new {@link SimpleObjectProperty}{@literal <>}selectedExampleClass2);
     *      }
     *
     *      ... Assumed to have standard Property methods ...
     * }</code></pre>
     * <br>
     * <p><b>Example Class 2:</b></p>
     * <pre><code>
     * public static class ExampleClass2 {
     *      private final {@link StringProperty} nameProperty;
     *
     *      public ExampleClass2(String name) {
     *          this.nameProperty = new {@link SimpleStringProperty}(name);
     *      }
     *
     *      ... Assumed to have standard Property methods ...
     * }</code></pre>
     * <br>
     * <p><b>Example Usage:</b></p>
     * <pre><code>
     * ExampleClass2 example1 = new ExampleClass2("Example 1");
     * ExampleClass2 example2 = new ExampleClass2("Example 2");
     * ExampleClass1 exampleClass1 = new ExampleClass1(example1);
     *
     * {@link Binding}{@literal <String>} recursiveNameBinding = Properties.recursiveBinding(ExampleClass2::nameProperty, exampleClass1.selectedExampleProperty());
     * recursiveNameBinding.addListener((observable, oldValue, newValue)-> System.out.println("Changing Value - [" + oldValue + "-> " + newValue + "]"));
     *
     * example1.setName("New Name (Example 1)");
     * exampleClass1.setSelectedExample(example2);
     * example1.setName("Another New Name (Example 1)");
     * example2.setName("New Name (Example 2)");
     * exampleClass1.setSelectedExample(example1);</code></pre>
     */
    public static class RecursiveBinding<U, V>
            implements Lockable, Binding<V> {
        
        private final ReentrantLock lock;
        
        private final ObjectProperty<Function<U, ObservableValue<V>>> functionProperty;
        
        private final ObjectProperty<V> backingBindingProperty;
        private final ObservableValue<U> updateObservable;
        private final Observable[] updateBindings;
        
        private Binding<V> binding;
        
        /**
         * <p>Constructs a new {@code RecursiveBinding} object.</p>
         *
         * @param lock             The {@code ReentrantLock} used for this {@code RecursiveBinding}.
         *                         Specify {@code null} to not use a {@code ReentrantLock}.
         * @param function         The {@code Function} used to retrieve a new {@code ObservableValue} when the {@code updateObservable} changes.
         * @param updateObservable The {@code ObservableValue} that calls the {@code Function} upon changing.
         * @param dependencies     Any additional {@code Observables} that should trigger an update (call to the {@code Function}) upon changing.
         *                         <i>Optional</i>.
         *
         * @see BindingsSL#recursiveBinding(Function, ObservableValue, Observable...)
         * @see BindingsSL#recursiveBinding(ReentrantLock, Function, ObservableValue, Observable...)
         */
        protected RecursiveBinding(ReentrantLock lock, Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable[] dependencies) {
            this.lock = lock;
            
            this.functionProperty = new SimpleObjectProperty<>(function);
            
            this.backingBindingProperty = new SimpleObjectProperty<>();
            this.updateObservable = updateObservable;
            this.updateBindings = dependencies;
            
            this.initialize();
        }
        
        //<editor-fold desc="--- PROPERTIES ---">
        
        /**
         * <p>Returns the {@link ObjectProperty} containing the {@link Function} used to calculate the {@link ObservableValue#getValue() value} of this {@link RecursiveBinding} instance upon its {@link #invalidate() invalidation}.</p>
         *
         * @return The {@link ObjectProperty} containing the {@link Function} used to calculate the {@link ObservableValue#getValue() value} of this {@link RecursiveBinding} instance upon its {@link #invalidate() invalidation}.
         */
        public ObjectProperty<Function<U, ObservableValue<V>>> functionProperty() { return functionProperty; }
        public Function<U, ObservableValue<V>> getFunction() { return functionProperty.get(); }
        public Function<U, ObservableValue<V>> setFunction(Function<U, ObservableValue<V>> newValue) { return PropertiesSL.setProperty(functionProperty, newValue); }
        
        /**
         * <p>Returns the backing {@link Binding} attached to this {@code RecursiveBinding}.</p>
         * <p>All implemented {@code Binding} methods are passed through to the backing {@code Binding}.</p>
         *
         * @return The backing {@code Binding} attached to this {@code RecursiveBinding}.
         */
        private Binding<V> getBackingBinding() { return binding; }
        
        //</editor-fold>
        
        //<editor-fold desc="--- IMPLEMENTATIONS ---">
        
        //<editor-fold desc="> Binding">
        
        @Override public ObservableList<?> getDependencies() { return getBackingBinding().getDependencies(); }
        @Override public V getValue() { return getBackingBinding().getValue(); }
        
        @Override public void dispose() { getBackingBinding().dispose(); }
        
        
        @Override public boolean isValid() { return getBackingBinding().isValid(); }
        @Override public void invalidate() { getBackingBinding().invalidate(); }
        
        
        @Override public void addListener(ChangeListener<? super V> listener) { getBackingBinding().addListener(listener); }
        @Override public void removeListener(ChangeListener<? super V> listener) { getBackingBinding().removeListener(listener); }
        
        @Override public void addListener(InvalidationListener listener) { getBackingBinding().addListener(listener); }
        @Override public void removeListener(InvalidationListener listener) { getBackingBinding().removeListener(listener); }
        
        //</editor-fold>
        
        @Override public @NotNull ReentrantLock getLock() { return lock; }
        
        //</editor-fold>
        
        //<editor-fold desc="--- INTERNAL ---">
        
        /**
         * Initializes this {@code RecursiveBinding}.
         * <p>
         * Calling this method more than once will result in an {@code RuntimeException} being thrown.
         */
        private void initialize() {
            updateObservable.addListener((observable, oldValue, newValue) -> update(oldValue, newValue));
            binding = Bindings.createObjectBinding(
                    backingBindingProperty::get,
                    ArraysSL.concat(new Observable[]{backingBindingProperty}, updateBindings)
                                                  );
            binding.invalidate();
            update(updateObservable.getValue(), updateObservable.getValue());
        }
        
        /**
         * Updates this {@code RecursiveBinding} based on the {@link #getFunction() Function}.
         *
         * @param oldValue The old value. <i>Currently unused.</i>
         * @param newValue The new value.
         */
        private void update(U oldValue, U newValue) {
            Function<U, ObservableValue<V>> function = getFunction();
            ExceptionsSL.nullCheck(function, "Function");
            ObservableValue<V> observableValue = function.apply(newValue);
            if (observableValue != null)
                backingBindingProperty.bind(observableValue);
            else
                backingBindingProperty.unbind();
            invalidate();
        }
        
        //</editor-fold>
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- MISC. BINDINGS ---">
    
    /**
     * Creates a {@link BooleanBinding} that is bound to the null status of the specified {@code Binding}.
     *
     * @param binding The {@code Binding} who's value is to be null-checked.
     *
     * @return The newly created {@code BooleanBinding} bound to the null status of the specified {@code Binding}.
     */
    public static @NotNull BooleanBinding createNullCheckBinding(Binding<?> binding) {
        ExceptionsSL.nullCheck(binding, "Binding");
        return Bindings.createBooleanBinding(() -> binding.getValue() != null, binding);
    }
    
    /**
     * Creates and then returns an {@link IntegerBinding} that increments by 1 every time any of the specified {@link Observable Observables} is updated.
     *
     * @param obs The {@code Observables}.
     *
     * @return An {@link IntegerBinding} that increments by 1 every time any of the specified {@link Observable Observables} is updated.
     */
    public static @NotNull IntegerBinding incrementingBinding(Observable... obs) {
        AtomicInteger _int = new AtomicInteger(0);
        return Bindings.createIntegerBinding(_int::incrementAndGet, obs);
    }
    
    //</editor-fold>
}