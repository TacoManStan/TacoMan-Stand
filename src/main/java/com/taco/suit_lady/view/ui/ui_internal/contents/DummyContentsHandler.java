package com.taco.suit_lady.view.ui.ui_internal.contents;

import com.taco.suit_lady.util.ExceptionTools;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;

import java.util.Objects;

public class DummyContentsHandler
{
    
    private final ReadOnlyListWrapper<DummyInstance> instances;
    private final ReadOnlySelectedDummyInstanceWrapper selectedInstanceProperty;
    
    public DummyContentsHandler()
    {
        this.instances = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        this.selectedInstanceProperty = new ReadOnlySelectedDummyInstanceWrapper();
    }
    
    public final void initialize() { } // TODO (if needed)
    
    //
    
    // <editor-fold desc="Properties">
    
    public final ReadOnlyListProperty<DummyInstance> instances()
    {
        return instances.getReadOnlyProperty();
    }
    
    //
    
    // <editor-fold desc="Selected Client">
    
    public final ReadOnlySelectedDummyInstanceWrapper selectedInstanceProperty()
    {
        return selectedInstanceProperty;
    }
    
    public final ReadOnlySelectedDummyInstanceProperty readOnlySelectedClientProperty()
    {
        return selectedInstanceProperty.getReadOnlyProperty();
    } // Handled by security manager
    
    public final DummyInstance getSelected()
    {
        return selectedInstanceProperty.get();
    }
    
    //
    
    public void select(DummyInstance instance)
    {
        selectedInstanceProperty.set(instance);
    }
    
    public void clearSelection()
    {
        select(null);
    }
    
    public boolean isSelected(DummyInstance instance)
    {
        return Objects.equals(instance, getSelected());
    }
    
    // </editor-fold>
    
    // </editor-fold>
    
    // <editor-fold desc="Implementation">
    
    // </editor-fold>
    
    //    // CHANGE-HERE (BELOW)
    //
    //    public DummyInstance getByThread()
    //    {
    //        return getByThread(Thread.currentThread().getThreadGroup());
    //    }
    //
    //    public DummyInstance getByThread(ThreadGroup threadGroup)
    //    {
    //        if (threadGroup != null)
    //        {
    //            if (threadGroup instanceof ScriptThreadable)
    //            {
    //                ExecutorThreadFactory<ScriptContext, ScriptThreadGroup> _threadFactory = ((ScriptThreadGroup) threadGroup).getThreadFactory();
    //                if (_threadFactory != null)
    //                {
    //                    ScriptContext _ctx = _threadFactory.getBean();
    //                    if (_ctx != null)
    //                        return _ctx.client();
    //                }
    //            }
    //        }
    //        return null;
    //    }
    
    public DummyInstance newInstance()
    {
        DummyInstance instance = new DummyInstance();
        instances.add(instance);
        return instance;
    }
    
    //
    
    // TODO: Stop exceptions from preventing a full shut-down
    public void shutdown()
    {
        // CHANGE-HERE (BELOW)
        instances.forEach(this::shutdown);
        if (!instances.isEmpty())
            throw ExceptionTools.ex("Client List should be empty! (" + instances + ")");
    }
    
    public boolean shutdown(DummyInstance instance)
    {
        instances.remove(instance);
        instance.shutdownInstanceEngine();
        return true; // TODO: This should actually return the proper value.
    }
}