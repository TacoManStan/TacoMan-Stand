package com.taco.suit_lady.util.timing;

import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * <p>Defines an {@link Observable} {@link Property} implementation of {@link Duration}.</p>
 *
 * @see Duration
 */
public class DurationProperty extends Duration
        implements Property<Number> {
    
    public DurationProperty() {
        super();
    }
    
    public DurationProperty(Lock lock) {
        super(lock);
    }
    
    public DurationProperty(long duration) {
        super(duration);
    }
    
    public DurationProperty(Lock lock, long duration) {
        super(lock, duration);
    }
    
    public DurationProperty(long duration, TimeUnit inputTimeUnit) {
        super(duration, inputTimeUnit);
    }
    
    public DurationProperty(Lock lock, long duration, TimeUnit inputTimeUnit) {
        super(lock, duration, inputTimeUnit);
    }
    
    //
    
    //<editor-fold desc="Implementation">
    
    @Override public void bind(ObservableValue<? extends Number> observable) {
        impl_durationProperty().bind(observable);
    }
    
    @Override public void unbind() {
        impl_durationProperty().unbind();
    }
    
    @Override public boolean isBound() {
        return impl_durationProperty().isBound();
    }
    
    @Override public void bindBidirectional(Property<Number> other) {
        impl_durationProperty().bindBidirectional(other);
    }
    
    @Override public void unbindBidirectional(Property<Number> other) {
        impl_durationProperty().unbindBidirectional(other);
    }
    
    //</editor-fold>
}
