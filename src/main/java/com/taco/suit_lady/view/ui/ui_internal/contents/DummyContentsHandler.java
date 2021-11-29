package com.taco.suit_lady.view.ui.ui_internal.contents;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>Class used to manage and display {@link DummyInstance DummyInsstances}.</p>
 */
@Component
public class DummyContentsHandler
        implements Springable
{
    private final ConfigurableApplicationContext ctx;
    private final FxWeaver weaver;
    
    private final ReadOnlyListWrapper<DummyInstance> instances;
    private final ReadOnlySelectedDummyInstanceWrapper selectedInstanceProperty;
    
    public DummyContentsHandler(ConfigurableApplicationContext ctx, FxWeaver weaver)
    {
        this.ctx = ctx;
        this.weaver = weaver;
        
        this.instances = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        this.selectedInstanceProperty = new ReadOnlySelectedDummyInstanceWrapper();
    }
    
    public final void initialize() { }
    
    //
    
    // <editor-fold desc="Properties">
    
    public final ReadOnlyListProperty<DummyInstance> instances()
    {
        return instances.getReadOnlyProperty();
    }
    
    // <editor-fold desc="Selected Client">
    
    /**
     * <p>The {@link ReadOnlySelectedDummyInstanceWrapper} containing the currently selected {@link DummyInstance}.</p>
     *
     * @return The {@link ReadOnlySelectedDummyInstanceWrapper} containing the currently selected {@link DummyInstance}.
     */
    public final ReadOnlySelectedDummyInstanceWrapper selectedInstanceProperty()
    {
        return selectedInstanceProperty;
    }
    
    public final ReadOnlySelectedDummyInstanceProperty readOnlySelectedClientProperty()
    {
        return selectedInstanceProperty.getReadOnlyProperty();
    } // Handled by security manager
    
    public final DummyInstance getSelectedInstance()
    {
        return selectedInstanceProperty.get();
    }
    
    //
    
    /**
     * <p>{@link #selectedInstanceProperty() Selects} the specified {@link DummyInstance}.</p>
     *
     * @param instance The {@link DummyInstance} being {@link #selectedInstanceProperty() selected}.
     */
    public void select(DummyInstance instance)
    {
        selectedInstanceProperty.set(instance);
    }
    
    /**
     * <p>Clears the currently {@link #selectedInstanceProperty() selected} {@link DummyInstance}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #select(DummyInstance) select}<b>(</b>null<b>)</b></code></i></blockquote>
     */
    public void clearSelection()
    {
        select(null);
    }
    
    /**
     * <p>Checks if the specified {@link DummyInstance} object is currently {@link #selectedInstanceProperty() selected}.</p>
     *
     * @param instance The {@link DummyInstance} being checked.
     *
     * @return True if the specified {@link DummyInstance} is currently {@link #selectedInstanceProperty() selected}, false if it is not.
     */
    public boolean isSelected(DummyInstance instance)
    {
        return Objects.equals(instance, getSelectedInstance());
    }
    
    // </editor-fold>
    
    // </editor-fold>
    
    // <editor-fold desc="Implementation">
    
    // </editor-fold>
    
    /**
     * <p>Constructs a new {@link DummyInstance}, adds it to the {@link #instances() Instance List}, then returns the newly-constructed {@link DummyInstance object}.</p>
     *
     * @return The newly-constructed {@link DummyInstance}.
     */
    public DummyInstance newInstance()
    {
        final DummyInstance instance = new DummyInstance(this);
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
    
    @Override
    public FxWeaver weaver()
    {
        return weaver;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return ctx;
    }
}