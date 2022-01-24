package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.tools.fx_tools.FX;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * Utility methods related to {@link Property Properties} and {@link Bindings}.
 */
public class SLBindings {
    
    //<editor-fold desc="--- BASIC BINDINGS ---">
    
    /**
     * <p>Constructs and then returns a {@link BooleanBinding} that always returns the value of {@code value}.</p>
     *
     * @return A newly constructed {@link BooleanBinding} that always returns the value of {@code value}.
     */
    public static @NotNull BooleanBinding bindBoolean(boolean value) {
        return Bindings.createBooleanBinding(() -> value);
    }
    
    /**
     * <p>Constructs a new {@link BooleanBinding} that always reflects the {@link ObservableBooleanValue#get() value} of the specified {@link ObservableBooleanValue}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@code value} is {@code null}, the {@link BooleanBinding returned binding} always {@link BooleanBinding#get() returns} {@code false}.</li>
     * </ol>
     *
     * @param observable The {@link ObservableBooleanValue} to be reflected by the returned {@link BooleanBinding}.
     *
     * @return A new {@link BooleanBinding} that always reflects the {@link ObservableBooleanValue#get() value} of the specified {@code value}.
     */
    public static @NotNull BooleanBinding bindBoolean(ObservableBooleanValue observable) {
        if (observable == null)
            return bindBoolean(false);
        return Bindings.createBooleanBinding(() -> observable.get(), observable);
    }
    
    /**
     * <p>Constructs and then returns an {@link IntegerBinding} that always returns the value of {@code value}.</p>
     *
     * @return A newly constructed {@link IntegerBinding} that always returns the value of {@code value}.
     */
    public static @NotNull IntegerBinding bindInteger(int value) {
        return Bindings.createIntegerBinding(() -> value);
    }
    
    /**
     * <p>Constructs a new {@link IntegerBinding} that always reflects the {@link ObservableIntegerValue#get() value} of the specified {@link ObservableIntegerValue}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@code value} is {@code null}, the {@link IntegerBinding returned binding} always {@link IntegerBinding#get() returns} {@code 0}.</li>
     * </ol>
     *
     * @param observable The {@link ObservableIntegerValue} to be reflected by the returned {@link IntegerBinding}.
     *
     * @return A new {@link IntegerBinding} that always reflects the {@link ObservableIntegerValue#get() value} of the specified {@code value}.
     */
    public static @NotNull IntegerBinding bindInteger(ObservableIntegerValue observable) {
        if (observable == null)
            return bindInteger(0);
        return Bindings.createIntegerBinding(() -> observable.get(), observable);
    }
    
    /**
     * <p>Constructs and then returns a {@link LongBinding} that always returns the value of {@code value}.</p>
     *
     * @return A newly constructed {@link LongBinding} that always returns the value of {@code value}.
     */
    public static @NotNull LongBinding bindLong(long value) {
        return Bindings.createLongBinding(() -> value);
    }
    
    /**
     * <p>Constructs a new {@link LongBinding} that always reflects the {@link ObservableLongValue#get() value} of the specified {@link ObservableLongValue}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@code value} is {@code null}, the {@link LongBinding returned binding} always {@link LongBinding#get() returns} {@code 0}.</li>
     * </ol>
     *
     * @param observable The {@link ObservableLongValue} to be reflected by the returned {@link LongBinding}.
     *
     * @return A new {@link LongBinding} that always reflects the {@link ObservableLongValue#get() value} of the specified {@code value}.
     */
    public static @NotNull LongBinding bindLong(ObservableLongValue observable) {
        if (observable == null)
            return bindLong(0);
        return Bindings.createLongBinding(() -> observable.get(), observable);
    }
    
    /**
     * <p>Constructs and then returns a {@link FloatBinding} that always returns the value of {@code value}.</p>
     *
     * @return A newly constructed {@link FloatBinding} that always returns the value of {@code value}.
     */
    public static @NotNull FloatBinding bindFloat(float value) {
        return Bindings.createFloatBinding(() -> value);
    }
    
    /**
     * <p>Constructs a new {@link FloatBinding} that always reflects the {@link ObservableFloatValue#get() value} of the specified {@link ObservableFloatValue}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@code value} is {@code null}, the {@link FloatBinding returned binding} always {@link FloatBinding#get() returns} {@code 0}.</li>
     * </ol>
     *
     * @param observable The {@link ObservableFloatValue} to be reflected by the returned {@link FloatBinding}.
     *
     * @return A new {@link FloatBinding} that always reflects the {@link ObservableFloatValue#get() value} of the specified {@code value}.
     */
    public static @NotNull FloatBinding bindFloat(ObservableFloatValue observable) {
        if (observable == null)
            return bindFloat(0);
        return Bindings.createFloatBinding(() -> observable.get(), observable);
    }
    
    /**
     * <p>Constructs and then returns a {@link DoubleBinding} that always returns the value of {@code value}.</p>
     *
     * @return A newly constructed {@link DoubleBinding} that always returns the value of {@code value}.
     */
    public static @NotNull DoubleBinding bindDouble(double value) {
        return Bindings.createDoubleBinding(() -> value);
    }
    
    /**
     * <p>Constructs a new {@link DoubleBinding} that always reflects the {@link ObservableDoubleValue#get() value} of the specified {@link ObservableDoubleValue}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@code value} is {@code null}, the {@link DoubleBinding returned binding} always {@link DoubleBinding#get() returns} {@code 0}.</li>
     * </ol>
     *
     * @param observable The {@link ObservableDoubleValue} to be reflected by the returned {@link DoubleBinding}.
     *
     * @return A new {@link DoubleBinding} that always reflects the {@link ObservableDoubleValue#get() value} of the specified {@code value}.
     */
    public static @NotNull DoubleBinding bindDouble(ObservableDoubleValue observable) {
        if (observable == null)
            return bindDouble(0);
        return Bindings.createDoubleBinding(() -> observable.get(), observable);
    }
    
    /**
     * <p>Constructs and then returns a {@link StringBinding} that always returns the value of {@code value}.</p>
     *
     * @return A newly constructed {@link StringBinding} that always returns the value of {@code value}.
     */
    public static @NotNull StringBinding bindString(String value) {
        return Bindings.createStringBinding(() -> value);
    }
    
    /**
     * <p>Constructs a new {@link StringBinding} that always reflects the {@link ObservableStringValue#get() value} of the specified {@link ObservableStringValue}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@code value} is {@code null}, the {@link StringBinding returned binding} always {@link StringBinding#get() returns} an empty {@link String}.</li>
     * </ol>
     *
     * @param observable The {@link ObservableStringValue} to be reflected by the returned {@link StringBinding}.
     *
     * @return A new {@link StringBinding} that always reflects the {@link ObservableStringValue#get() value} of the specified {@code value}.
     */
    public static @NotNull StringBinding bindString(ObservableStringValue observable) {
        if (observable == null)
            return bindString("");
        return Bindings.createStringBinding(() -> observable.get(), observable);
    }
    
    /**
     * <p>Constructs and then returns a {@link ObjectBinding} that always returns the value of {@code value}.</p>
     *
     * @return A newly constructed {@link ObjectBinding} that always returns the value of {@code value}.
     */
    public static <T> @NotNull ObjectBinding<T> bindObject(T value) {
        return Bindings.createObjectBinding(() -> value);
    }
    
    /**
     * <p>Constructs a new {@link ObjectBinding} that always reflects the {@link ObservableObjectValue#get() value} of the specified {@link ObservableObjectValue}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@code value} is {@code null}, the {@link ObjectBinding returned binding} always {@link ObjectBinding#get() returns} {@code null}.</li>
     * </ol>
     *
     * @param observable The {@link ObservableObjectValue} to be reflected by the returned {@link ObjectBinding}.
     *
     * @return A new {@link ObjectBinding} that always reflects the {@link ObservableObjectValue#get() value} of the specified {@code value}.
     */
    public static <T> @NotNull ObjectBinding<T> bindObject(ObservableObjectValue<T> observable) {
        if (observable == null)
            return bindObject((T) null);
        return Bindings.createObjectBinding(() -> observable.get(), observable);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- DIRECT BINDINGS ---">
    
    public static @NotNull BooleanBinding bindBooleanDirect(@NotNull ObservableValue<Boolean> observableValue, Observable... updateObservables) {
        return Bindings.createBooleanBinding(() -> observableValue.getValue(), SLArrays.concat(new Observable[]{observableValue}, updateObservables));
    }
    
    public static @NotNull IntegerBinding bindIntegerDirect(@NotNull ObservableValue<? extends Number> observableValue, Observable... updateObservables) {
        return Bindings.createIntegerBinding(() -> observableValue.getValue().intValue(), SLArrays.concat(new Observable[]{observableValue}, updateObservables));
    }
    
    public static @NotNull LongBinding bindLongDirect(@NotNull ObservableValue<? extends Number> observableValue, Observable... updateObservables) {
        return Bindings.createLongBinding(() -> observableValue.getValue().longValue(), SLArrays.concat(new Observable[]{observableValue}, updateObservables));
    }
    
    public static @NotNull FloatBinding bindFloatDirect(@NotNull ObservableValue<? extends Number> observableValue, Observable... updateObservables) {
        return Bindings.createFloatBinding(() -> observableValue.getValue().floatValue(), SLArrays.concat(new Observable[]{observableValue}, updateObservables));
    }
    
    public static @NotNull DoubleBinding bindDoubleDirect(@NotNull ObservableValue<? extends Number> observableValue, Observable... updateObservables) {
        return Bindings.createDoubleBinding(() -> observableValue.getValue().doubleValue(), SLArrays.concat(new Observable[]{observableValue}, updateObservables));
    }
    
    public static @NotNull StringBinding bindStringDirect(@NotNull ObservableValue<String> observableValue, Observable... updateObservables) {
        return Bindings.createStringBinding(() -> observableValue.getValue(), SLArrays.concat(new Observable[]{observableValue}, updateObservables));
    }
    
    public static <T> @NotNull ObjectBinding<T> bindObjectDirect(@NotNull ObservableValue<T> observableValue, Observable... updateObservables) {
        return Bindings.createObjectBinding(() -> observableValue.getValue(), SLArrays.concat(new Observable[]{observableValue}, updateObservables));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CONVENIENCE BINDINGS ---">
    
    /**
     * Creates a {@link BooleanBinding} that is bound to the null status of the specified {@code Binding}.
     *
     * @param binding The {@code Binding} who's value is to be null-checked.
     *
     * @return The newly created {@code BooleanBinding} bound to the null status of the specified {@code Binding}.
     */
    public static BooleanBinding createNullCheckBinding(Binding<?> binding) {
        SLExceptions.nullCheck(binding, "Binding");
        return Bindings.createBooleanBinding(() -> binding.getValue() != null, binding);
    }
    
    /**
     * Creates and then returns an {@link IntegerBinding} that increments by 1 every time any of the specified {@link Observable Observables} is updated.
     *
     * @param obs The {@code Observables}.
     *
     * @return An {@link IntegerBinding} that increments by 1 every time any of the specified {@link Observable Observables} is updated.
     */
    public static IntegerBinding incrementingBinding(Observable... obs) {
        AtomicInteger _int = new AtomicInteger(0);
        return Bindings.createIntegerBinding(_int::incrementAndGet, obs);
    }
    
    /**
     * Binds the specified {@link Property} to the specified {@link ObservableValue}, ensuring that the update process is done on the FX thread.
     *
     * @param property        The {@code Property} being bound.
     * @param observableValue The {@code ObservableValue} the specified {@code Property} is being bound to.
     * @param <T>             The type of {@code Object} wrapped by the specified values.
     */
    public static <T> void bindFX(Property<T> property, ObservableValue<T> observableValue, Lock lock) {
        observableValue.addListener(
                (observable, oldValue, newValue) ->
                {
                    if (property.isBound())
                        throw SLExceptions.ex("Property is already bound (" + property + ")");
                    FX.runFX(() -> property.setValue(newValue), true);
                });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- RECURSIVE BINDINGS ---">
    
    //<editor-fold desc="> Recursive Primitive Wrappers">
    
    public static <U> @NotNull BooleanBinding bindBooleanRecursive(Function<U, ObservableValue<Boolean>> function, ObservableValue<U> updateObservable, Observable... updateObservables) {
        return bindBooleanDirect(bindRecursive(function, updateObservable, updateObservables));
    }
    
    public static <U> @NotNull IntegerBinding bindIntegerRecursive(Function<U, ObservableValue<Number>> function, ObservableValue<U> updateObservable, Observable... updateObservables) {
        return bindIntegerDirect(bindRecursive(function, updateObservable, updateObservables));
    }
    
    public static <U> @NotNull LongBinding bindLongDirect(Function<U, ObservableValue<Number>> function, ObservableValue<U> updateObservable, Observable... updateObservables) {
        return bindLongDirect(bindRecursive(function, updateObservable, updateObservables));
    }
    
    public static <U> @NotNull FloatBinding bindFloatDirect(Function<U, ObservableValue<Number>> function, ObservableValue<U> updateObservable, Observable... updateObservables) {
        return bindFloatDirect(bindRecursive(function, updateObservable, updateObservables));
    }
    
    public static <U> @NotNull DoubleBinding bindDoubleDirect(Function<U, ObservableValue<Number>> function, ObservableValue<U> updateObservable, Observable... updateObservables) {
        return bindDoubleDirect(bindRecursive(function, updateObservable, updateObservables));
    }
    
    //</editor-fold>
    
    /**
     * Constructs a new {@link RecursiveBinding} with a new {@link ReentrantLock}.
     *
     * @param function          The {@code Function}. //TODO - Expand
     * @param updateObservable  The {@code ObservableValue}. //TODO - Expand
     * @param updateObservables The array of {@code Observables}. //TODO - Expand
     *
     * @return A new {@link RecursiveBinding}.
     *
     * @see RecursiveBinding
     */
    @Contract("_, _, _ -> new")
    public static <U, V> @NotNull RecursiveBinding<U, V> bindRecursive(Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable... updateObservables) {
        return bindRecursive(new ReentrantLock(), function, updateObservable, updateObservables);
    }
    
    /**
     * Constructs a new {@link RecursiveBinding} with the specified {@link ReentrantLock}.
     *
     * @param lock              The {@code ReentrantLock} to be used to synchronize calls to the returned {@code RecursiveBinding}.
     * @param function          The {@code Function}. //TODO - Expand
     * @param updateObservable  The {@code ObservableValue}. //TODO - Expand
     * @param updateObservables The array of {@code Observables}. //TODO - Expand
     *
     * @return A new {@code RecursiveBinding}.
     *
     * @see RecursiveBinding
     */
    @Contract("_, _, _, _ -> new")
    public static <U, V> @NotNull RecursiveBinding<U, V> bindRecursive(Lock lock, Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable... updateObservables) {
        return new RecursiveBinding<>(lock, function, updateObservable, updateObservables);
    }
    
    /**
     * Allows you to bind an {@link ObservableValue} to a dynamic {@link Binding}.
     * <p>
     * <b>Example Class 1:</b>
     * <pre><code>
     * public static class ExampleClass1
     * {
     *      private final {@link ObjectProperty}{@literal ExampleClass2} selectedExampleProperty;
     *
     *      public ExampleClass1(ExampleClass2 selectedExampleClass2) {
     *          this.selectedExampleProperty = new {@link SimpleObjectProperty}{@literal <>}selectedExampleClass2);}
     *      }
     *
     *      ... Assumed to have standard Property methods ...
     * }</code></pre>
     * <b>Example Class 2:</b>
     * <pre><code>
     * public static class ExampleClass2
     * {
     *      private final {@link StringProperty} nameProperty;
     *
     *      public ExampleClass2(String name) {
     *          this.nameProperty = new {@link SimpleStringProperty} (name);
     *      }
     *
     *      ... Assumed to have standard Property methods ...
     * }</code></pre>
     * <p>
     * <b>Example Usage:</b>
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
            implements Binding<V> {
        
        private final Lock lock;
        
        private final ObjectProperty<Function<U, ObservableValue<V>>> functionProperty;
        
        private final ObjectProperty<V> backingBindingProperty;
        private final ObservableValue<U> updateObservable;
        private final Observable[] updateBindings;
        
        private Binding<V> binding;
        
        /**
         * Constructs a new {@code RecursiveBinding} object.
         *
         * @param lock              The {@code ReentrantLock} used for this {@code RecursiveBinding}.
         *                          Specify {@code null} to not use a {@code ReentrantLock}.
         * @param function          The {@code Function} used to retrieve a new {@code ObservableValue} when the {@code updateObservable} changes.
         * @param updateObservable  The {@code ObservableValue} that calls the {@code Function} upon changing.
         * @param updateObservables Any additional {@code Observables} that should trigger an update (call to the {@code Function}) upon changing.
         *                          <i>Optional</i>.
         *
         * @see SLBindings#bindRecursive(Function, ObservableValue, Observable...)}
         * @see SLBindings#bindRecursive(Lock, Function, ObservableValue, Observable...)}
         */
        protected RecursiveBinding(Lock lock, Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable[] updateObservables) {
            this.lock = lock;
            
            this.functionProperty = new SimpleObjectProperty<>(function);
            
            this.backingBindingProperty = new SimpleObjectProperty<>();
            this.updateObservable = updateObservable;
            this.updateBindings = updateObservables;
            
            this.initialize();
        }
        
        //<editor-fold desc="--- PROPERTIES ---">
        
        //
        
        /**
         * Returns the {@link ObjectProperty} for this {@code RecursiveBinding}.
         *
         * @return The {@code ObjectProperty} for this {@code RecursiveBinding}.
         *
         * @see #getFunction()
         * @see #setFunction(Function)
         */
        public ObjectProperty<Function<U, ObservableValue<V>>> functionProperty() {
            return functionProperty;
        }
        
        /**
         * Returns the {@link Function} for this {@code RecursiveBinding}.
         *
         * @return The {@code Function} for this {@code RecursiveBinding}.
         *
         * @see #functionProperty()
         */
        public Function<U, ObservableValue<V>> getFunction() {
            return functionProperty.get();
        }
        
        /**
         * Sets the {@link Function} for this {@code RecursiveBinding}.
         * <p>
         * Calling this method causes this {@code RecursiveBinding} to update.
         *
         * @param function The new {@code Function}.
         *
         * @see #functionProperty()
         */
        public void setFunction(Function<U, ObservableValue<V>> function) {
            functionProperty.set(function);
        }
        
        //
        
        /**
         * Returns the backing {@link Binding} attached to this {@code RecursiveBinding}.
         * <p>
         * All implemented {@code Binding} methods are passed through to the backing {@code Binding}.
         *
         * @return The backing {@code Binding} attached to this {@code RecursiveBinding}.
         */
        private Binding<V> getBackingBinding() {
            return binding;
        }
        
        //</editor-fold>
        
        //<editor-fold desc="--- BINDINGS ---">
        
        /**
         * Calls the {@link Binding#isValid() isValid()} method of the {@code backing Binding}.
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override public boolean isValid() {
            return getBackingBinding().isValid();
        }
        
        /**
         * Calls the {@link Binding#invalidate() invalidate()} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void invalidate() {
            getBackingBinding().invalidate();
        }
        
        /**
         * Calls the {@link Binding#getDependencies() getDependencies()} method of the {@code backing Binding}.
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override public ObservableList<?> getDependencies() {
            return getBackingBinding().getDependencies();
        }
        
        /**
         * Calls the {@link Binding#dispose() dispose()} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void dispose() {
            getBackingBinding().dispose();
        } //TODO - Might need to actually dispose of this instance rather than just the Binding instance.
        
        /**
         * Calls the {@link Binding#addListener(ChangeListener) addListener(ChangeListener)} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void addListener(ChangeListener<? super V> listener) {
            getBackingBinding().addListener(listener);
        }
        
        /**
         * Calls the {@link Binding#removeListener(ChangeListener) removeListener(ChangeListener)} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void removeListener(ChangeListener<? super V> listener) {
            getBackingBinding().removeListener(listener);
        }
        
        /**
         * Calls the {@link Binding#getValue() getValue()} method of the {@code backing Binding}.
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override public V getValue() {
            return getBackingBinding().getValue();
        }
        
        /**
         * Calls the {@link Binding#addListener(InvalidationListener) addListener(InvalidationListener)} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void addListener(InvalidationListener listener) {
            getBackingBinding().addListener(listener);
        }
        
        /**
         * Calls the {@link Binding#removeListener(InvalidationListener) removeListener(InvalidationListener)} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void removeListener(InvalidationListener listener) {
            getBackingBinding().removeListener(listener);
        }
        
        //</editor-fold>
        
        /**
         * Initializes this {@code RecursiveBinding}.
         * <p>
         * Calling this method more than once will result in an {@code RuntimeException} being thrown.
         */
        private void initialize() {
            updateObservable.addListener((observable, oldValue, newValue) -> update(oldValue, newValue));
            binding = Bindings.createObjectBinding(
                    backingBindingProperty::get,
                    SLArrays.concat(new Observable[]{backingBindingProperty}, updateBindings)
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
            SLExceptions.nullCheck(function, "Function");
            ObservableValue<V> observableValue = function.apply(newValue);
            if (observableValue != null)
                backingBindingProperty.bind(observableValue);
            else
                backingBindingProperty.unbind();
            invalidate();
        }
    }
    
    //</editor-fold>
}