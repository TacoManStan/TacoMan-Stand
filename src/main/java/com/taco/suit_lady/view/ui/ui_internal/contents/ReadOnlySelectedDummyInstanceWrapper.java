package com.taco.suit_lady.view.ui.ui_internal.contents;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

/**
 * A wrapper class containing properties that are kept updated to mimic the properties of the currently selected {@link DummyInstance}.
 */
public class ReadOnlySelectedDummyInstanceWrapper extends SimpleObjectProperty<DummyInstance>
        implements ReadOnlySelectedDummyInstanceProperty
{
    
    private final ReadOnlyStringWrapper titleBinding;
    private final ReadOnlyStringWrapper descriptionBinding;
    
    protected ReadOnlySelectedDummyInstanceWrapper()
    {
        this.titleBinding = new ReadOnlyStringWrapper();
        this.descriptionBinding = new ReadOnlyStringWrapper();
        
        // Initialization
        
        addListener((observable, oldClient, newClient) -> update(oldClient, newClient));
    }
    
    //<editor-fold desc="UI Bindings">
    
    @Override
    public ReadOnlyStringProperty titleBinding()
    {
        return titleBinding.getReadOnlyProperty();
    }
    
    @Override
    public String getTitle()
    {
        return titleBinding.get();
    }
    
    @Override
    public ReadOnlyStringProperty descriptionBinding()
    {
        return descriptionBinding.getReadOnlyProperty();
    }
    
    @Override
    public String getDescription()
    {
        return descriptionBinding.get();
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //
    
    private void update(DummyInstance oldContents, DummyInstance newContents)
    {
        // TODO [S]: Potentially re-enable synchronization once nested task support has been implemented.
        //		sync(() -> {
        if (newContents != null)
        {
            titleBinding.bind(newContents.ui().titleProperty());
            descriptionBinding.bind(newContents.ui().descriptionProperty());
        }
        else
        {
            titleBinding.unbind();
            descriptionBinding.unbind();
        }
        //		});
    }
    
    //
    
    //<editor-fold desc="Implementation">
    
    //<editor-fold desc="Property">
    
    @Override
    protected void fireValueChangedEvent()
    {
        super.fireValueChangedEvent();
        if (readOnlyProperty != null)
        {
            readOnlyProperty.fireValueChangedEvent();
        }
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="Read Only Impl Class">
    
    private ReadOnlySelectedDummyInstancePropertyImpl readOnlyProperty;
    
    public ReadOnlySelectedDummyInstanceProperty getReadOnlyProperty()
    {
        if (readOnlyProperty == null)
            readOnlyProperty = new ReadOnlySelectedDummyInstancePropertyImpl();
        return readOnlyProperty;
    }
    
    //
    
    private final class ReadOnlySelectedDummyInstancePropertyImpl extends ReadOnlyObjectPropertyBase<DummyInstance>
            implements ReadOnlySelectedDummyInstanceProperty
    {
        
        //<editor-fold desc="Selected Client Property">
        
        //<editor-fold desc="UI Bindings">
        
        @Override
        public ReadOnlyStringProperty titleBinding()
        {
            return ReadOnlySelectedDummyInstanceWrapper.this.titleBinding();
        }
        
        @Override
        public String getTitle()
        {
            return ReadOnlySelectedDummyInstanceWrapper.this.getTitle();
        }
        
        @Override
        public ReadOnlyStringProperty descriptionBinding()
        {
            return ReadOnlySelectedDummyInstanceWrapper.this.descriptionBinding();
        }
        
        @Override
        public String getDescription()
        {
            return ReadOnlySelectedDummyInstanceWrapper.this.getDescription();
        }
        
        //</editor-fold>
        
        //</editor-fold>
        
        //
        
        @Override
        public Object getBean()
        {
            return ReadOnlySelectedDummyInstanceWrapper.this.getBean();
        }
        
        @Override
        public String getName()
        {
            return ReadOnlySelectedDummyInstanceWrapper.this.getName();
        }
        
        //
        
        @Override
        public DummyInstance getValue()
        {
            return ReadOnlySelectedDummyInstanceWrapper.this.getValue();
        }
        
        @Override
        public DummyInstance get()
        {
            return ReadOnlySelectedDummyInstanceWrapper.this.get();
        }
        
        //
        
        @Override
        public void addListener(ChangeListener<? super DummyInstance> listener)
        {
            ReadOnlySelectedDummyInstanceWrapper.this.addListener(listener);
        }
        
        @Override
        public void removeListener(ChangeListener<? super DummyInstance> listener)
        {
            ReadOnlySelectedDummyInstanceWrapper.this.removeListener(listener);
        }
        
        @Override
        public void addListener(InvalidationListener listener)
        {
            ReadOnlySelectedDummyInstanceWrapper.this.addListener(listener);
        }
        
        @Override
        public void removeListener(InvalidationListener listener)
        {
            ReadOnlySelectedDummyInstanceWrapper.this.removeListener(listener);
        }
        
        //
        
        // Does not pass call to wrapper because the value changed event needs to be fired for the impl as well as the wrapper.
        @Override
        protected void fireValueChangedEvent()
        {
            super.fireValueChangedEvent();
        }
    }
    
    //</editor-fold>
}