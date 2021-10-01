package com.taco.suit_lady.util;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * Utility methods related to {@link Property Properties} and {@link Bindings}.
 */
public class BindingTools
{
    public static BindingTools get()
    {
        return TB.bindings();
    }
    
    BindingTools() { }
    
    /**
     * Constructs and then returns a {@link BooleanBinding} that always returns the value of {@code value}.
     *
     * @return A newly constructed {@link BooleanBinding} that always returns the value of {@code value}.
     */
    public BooleanBinding booleanBinding(final boolean value)
    {
        return Bindings.createBooleanBinding(() -> value);
    }
    
    /**
     * Constructs and then returns an {@link IntegerBinding} that always returns the value of {@code value}.
     *
     * @return A newly constructed {@link IntegerBinding} that always returns the value of {@code value}.
     */
    public IntegerBinding integerBinding(final int value)
    {
        return Bindings.createIntegerBinding(() -> value);
    }
    
    /**
     * Constructs and then returns a {@link LongBinding} that always returns the value of {@code value}.
     *
     * @return A newly constructed {@link LongBinding} that always returns the value of {@code value}.
     */
    public LongBinding longBinding(final long value)
    {
        return Bindings.createLongBinding(() -> value);
    }
    
    /**
     * Constructs and then returns a {@link FloatBinding} that always returns the value of {@code value}.
     *
     * @return A newly constructed {@link FloatBinding} that always returns the value of {@code value}.
     */
    public FloatBinding floatBinding(final float value)
    {
        return Bindings.createFloatBinding(() -> value);
    }
    
    /**
     * Constructs and then returns a {@link DoubleBinding} that always returns the value of {@code value}.
     *
     * @return A newly constructed {@link DoubleBinding} that always returns the value of {@code value}.
     */
    public DoubleBinding doubleBinding(final double value)
    {
        return Bindings.createDoubleBinding(() -> value);
    }
    
    /**
     * Constructs and then returns a {@link StringBinding} that always returns the value of {@code value}.
     *
     * @return A newly constructed {@link StringBinding} that always returns the value of {@code value}.
     */
    public StringBinding stringBinding(final String value)
    {
        return Bindings.createStringBinding(() -> value);
    }
    
    /**
     * Constructs and then returns a {@link ObjectBinding} that always returns the value of {@code value}.
     *
     * @return A newly constructed {@link ObjectBinding} that always returns the value of {@code value}.
     */
    public <T> ObjectBinding<T> objBinding(final T value)
    {
        return Bindings.createObjectBinding(() -> value);
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Recursive Bindings                                                          *
     *                                                                             *
     * *************************************************************************** */
    
    /**
     * Constructs a new {@link RecursiveBinding} with a new {@link ReentrantLock}.
     *
     * @param function         The {@code Function}. //TODO - Expand
     * @param updateObservable The {@code ObservableValue}. //TODO - Expand
     * @param updateBindings   The array of {@code Observables}. //TODO - Expand
     * @return A new {@link RecursiveBinding}.
     * @see RecursiveBinding
     */
    public <U, V> RecursiveBinding<U, V> recursiveBinding(Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable... updateBindings)
    {
        return recursiveBinding(new ReentrantLock(), function, updateObservable, updateBindings);
    }
    
    /**
     * Constructs a new {@link RecursiveBinding} with the specified {@link ReentrantLock}.
     *
     * @param lock             The {@code ReentrantLock} to be used to synchronize calls to the returned {@code RecursiveBinding}.
     * @param function         The {@code Function}. //TODO - Expand
     * @param updateObservable The {@code ObservableValue}. //TODO - Expand
     * @param updateBindings   The array of {@code Observables}. //TODO - Expand
     * @return A new {@code RecursiveBinding}.
     * @see RecursiveBinding
     */
    public <U, V> RecursiveBinding<U, V> recursiveBinding(Lock lock, Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable... updateBindings)
    {
        return new RecursiveBinding<>(lock, function, updateObservable, updateBindings);
    }
    
    //<editor-fold desc="Recursive Binding Class">
    
    /**
     * Allows you to bind an {@link ObservableValue} to a dynamic {@link Binding}.
     * <p>
     * Example Class 1:
     * <blockquote><pre>
     * <code>public static class ExampleClass1 {
     *
     *      private final {@link ObjectProperty}{@code <ExampleClass2>} selectedExampleProperty;
     *
     *      public ExampleClass1(ExampleClass2 selectedExampleClass2) {
     *          this.selectedExampleProperty = new {@link SimpleObjectProperty}{@code <>}(selectedExampleClass2);}
     *      }
     *
     *      ... Assumed to have standard Property methods ...
     * }</code></pre></blockquote>
     * Example Class 2:
     * <blockquote><pre>
     * <code>public static class ExampleClass2 {
     *
     *      private final {@link StringProperty} nameProperty;
     *
     *      public ExampleClass2(String name) {
     *          this.nameProperty = new {@link SimpleStringProperty} (name);
     *      }
     *
     *      ... Assumed to have standard Property methods ...
     * }</code></pre></blockquote>
     * <p>
     * Example Usage:
     * <blockquote><pre>
     * <code>ExampleClass2 example1 = new ExampleClass2("Example 1");
     * ExampleClass2 example2 = new ExampleClass2("Example 2");
     * ExampleClass1 exampleClass1 = new ExampleClass1(example1);
     * {@link Binding}{@code <String>} recursiveNameBinding = Properties.recursiveBinding(ExampleClass2::nameProperty, exampleClass1.selectedExampleProperty());
     * recursiveNameBinding.addListener((observable, oldValue, newValue){@code ->} System.out.println("Changing Value - [" + oldValue + "{@code ->} " + newValue + "]"));
     * example1.setName("New Name (Example 1)");
     * exampleClass1.setSelectedExample(example2);
     * example1.setName("Another New Name (Example 1)");
     * example2.setName("New Name (Example 2)");
     * exampleClass1.setSelectedExample(example1);
     * </code></pre></blockquote>
     */
    public static class RecursiveBinding<U, V>
            implements Binding<V>
    {
        
        private final Lock lock;
        
        private ObjectProperty<Function<U, ObservableValue<V>>> functionProperty;
        
        private final ObjectProperty<V> backingBindingProperty;
        private final ObservableValue<U> updateObservable;
        private final Observable[] updateBindings;
        
        private Binding<V> binding;
        
        /**
         * Constructs a new {@code RecursiveBinding} object.
         *
         * @param lock             The {@code ReentrantLock} used for this {@code RecursiveBinding}.
         *                         Specify {@code null} to not use a {@code ReentrantLock}.
         * @param function         The {@code Function} used to retrieve a new {@code ObservableValue} when the {@code updateObservable} changes.
         * @param updateObservable The {@code ObservableValue} that calls the {@code Function} upon changing.
         * @param updateBindings   Any additional {@code Observables} that should trigger an update (call to the {@code Function}) upon changing.
         *                         <i>Optional</i>.
         * @see BindingTools#recursiveBinding(Function, ObservableValue, Observable...)}
         * @see BindingTools#recursiveBinding(Lock, Function, ObservableValue, Observable...)}
         */
        protected RecursiveBinding(Lock lock, Function<U, ObservableValue<V>> function, ObservableValue<U> updateObservable, Observable[] updateBindings)
        {
            this.lock = lock;
            
            this.functionProperty = new SimpleObjectProperty<>(function);
            
            this.backingBindingProperty = new SimpleObjectProperty<>();
            this.updateObservable = updateObservable;
            this.updateBindings = updateBindings;
            
            this.initialize();
        }
        
        //<editor-fold desc="Properties">
        
        //
        
        /**
         * Returns the {@link ObjectProperty} for this {@code RecursiveBinding}.
         *
         * @return The {@code ObjectProperty} for this {@code RecursiveBinding}.
         * @see #getFunction()
         * @see #setFunction(Function)
         */
        public ObjectProperty<Function<U, ObservableValue<V>>> functionProperty()
        {
            return functionProperty;
        }
        
        /**
         * Returns the {@link Function} for this {@code RecursiveBinding}.
         *
         * @return The {@code Function} for this {@code RecursiveBinding}.
         * @see #functionProperty()
         */
        public Function<U, ObservableValue<V>> getFunction()
        {
            return functionProperty.get();
        }
        
        /**
         * Sets the {@link Function} for this {@code RecursiveBinding}.
         * <p>
         * Calling this method causes this {@code RecursiveBinding} to update.
         *
         * @param function The new {@code Function}.
         * @see #functionProperty()
         */
        public void setFunction(Function<U, ObservableValue<V>> function)
        {
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
        private Binding<V> getBackingBinding()
        {
            return binding;
        }
        
        //</editor-fold>
        
        /**
         * Initializes this {@code RecursiveBinding}.
         * <p>
         * Calling this method more than once will result in an {@code RuntimeException} being thrown.
         */
        private void initialize()
        {
            updateObservable.addListener((observable, oldValue, newValue) -> update(oldValue, newValue));
            binding = Bindings.createObjectBinding(
                    backingBindingProperty::get,
                    TB.arrays().concat(new Observable[]{backingBindingProperty}, updateBindings)
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
        private void update(U oldValue, U newValue)
        {
            Function<U, ObservableValue<V>> function = getFunction();
            ExceptionTools.nullCheck(function, "Function");
            ObservableValue<V> observableValue = function.apply(newValue);
            if (observableValue != null)
                backingBindingProperty.bind(observableValue);
            else
                backingBindingProperty.unbind();
            invalidate();
        }
        
        //<editor-fold desc="Binding Methods">
        
        /**
         * Calls the {@link Binding#isValid() isValid()} method of the {@code backing Binding}.
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override public boolean isValid()
        {
            return getBackingBinding().isValid();
        }
        
        /**
         * Calls the {@link Binding#invalidate() invalidate()} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void invalidate()
        {
            getBackingBinding().invalidate();
        }
        
        /**
         * Calls the {@link Binding#getDependencies() getDependencies()} method of the {@code backing Binding}.
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override public ObservableList<?> getDependencies()
        {
            return getBackingBinding().getDependencies();
        }
        
        /**
         * Calls the {@link Binding#dispose() dispose()} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void dispose()
        {
            getBackingBinding().dispose();
        } //TODO - Might need to actually dispose of this instance rather than just the Binding instance.
        
        /**
         * Calls the {@link Binding#addListener(ChangeListener) addListener(ChangeListener)} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void addListener(ChangeListener<? super V> listener)
        {
            getBackingBinding().addListener(listener);
        }
        
        /**
         * Calls the {@link Binding#removeListener(ChangeListener) removeListener(ChangeListener)} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void removeListener(ChangeListener<? super V> listener)
        {
            getBackingBinding().removeListener(listener);
        }
        
        /**
         * Calls the {@link Binding#getValue() getValue()} method of the {@code backing Binding}.
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override public V getValue()
        {
            return getBackingBinding().getValue();
        }
        
        /**
         * Calls the {@link Binding#addListener(InvalidationListener) addListener(InvalidationListener)} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void addListener(InvalidationListener listener)
        {
            getBackingBinding().addListener(listener);
        }
        
        /**
         * Calls the {@link Binding#removeListener(InvalidationListener) removeListener(InvalidationListener)} method of the {@code backing Binding}.
         * {@inheritDoc}
         */
        @Override public void removeListener(InvalidationListener listener)
        {
            getBackingBinding().removeListener(listener);
        }
        
        //</editor-fold>
    }
    
    //</editor-fold>
    
    /* *************************************************************************** *
     *                                                                             *
     * General                                                                     *
     *                                                                             *
     * *************************************************************************** */
    
    /**
     * Creates and then returns an {@link IntegerBinding} that increments by 1 every time any of the specified {@link Observable Observables} is updated.
     *
     * @param obs The {@code Observables}.
     * @return An {@link IntegerBinding} that increments by 1 every time any of the specified {@link Observable Observables} is updated.
     */
    public IntegerBinding incrementingBinding(Observable... obs)
    {
        AtomicInteger _int = new AtomicInteger(0);
        return Bindings.createIntegerBinding(_int::incrementAndGet, obs);
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * EDT/FX Bindings                                                             *
     *                                                                             *
     * *************************************************************************** */
    
    /**
     * Binds the specified {@link Property} to the specified {@link ObservableValue}, ensuring that the update process is done on the FX thread.
     *
     * @param property        The {@code Property} being bound.
     * @param observableValue The {@code ObservableValue} the specified {@code Property} is being bound to.
     * @param <T>             The type of {@code Object} wrapped by the specified values.
     */
    public <T> void bindFX(Property<T> property, ObservableValue<T> observableValue, Lock lock)
    {
        observableValue.addListener(
                (observable, oldValue, newValue) ->
                {
                    if (property.isBound())
                        throw ExceptionTools.ex("Property is already bound (" + property + ")");
                    TB.fx().runFX(() -> property.setValue(newValue), true);
                });
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Checking Bindings                                                           *
     *                                                                             *
     * *************************************************************************** */
    
    /**
     * Creates a {@link BooleanBinding} that is bound to the null status of the specified {@code Binding}.
     *
     * @param binding The {@code Binding} who's value is to be null-checked.
     * @return The newly created {@code BooleanBinding} bound to the null status of the specified {@code Binding}.
     */
    public BooleanBinding nullCheckBinding(Binding binding)
    {
        ExceptionTools.nullCheck(binding, "Binding");
        return Bindings.createBooleanBinding(() -> binding.getValue() != null, binding);
    }
}