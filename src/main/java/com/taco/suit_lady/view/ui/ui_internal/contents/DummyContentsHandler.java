package com.taco.suit_lady.view.ui.ui_internal.contents;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Objects;

/**
 * <p>Class used to manage and display {@link DummyInstance DummyInsstances}.</p>
 * <p>{@link DummyContentsHandler} singleton instance is managed by the {@code Spring Framework}.</p>
 */
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
    
    // <editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyListProperty<DummyInstance> instances()
    {
        return instances.getReadOnlyProperty();
    }
    
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
    
    // <editor-fold desc="--- IMPLEMENTATIONS ---">
    
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
    
    // </editor-fold>
    
    /**
     * <p>Constructs a new {@link DummyInstance}, adds it to the {@link #instances() Instance List}, then returns the newly-constructed {@link DummyInstance object}.</p>
     *
     * @return The newly-constructed {@link DummyInstance}.
     *
     * @throws RuntimeException If call to <code><i>{@link ReadOnlyListWrapper#add(Object) instances.add(instance)}</i></code> fails (returns {@code false}).
     */
    public DummyInstance newInstance()
    {
        final DummyInstance instance = new DummyInstance(this);
        if (!instances.add(instance))
            throw ExceptionTools.ex("Could not add instance: " + instance);
        return instance;
    }
    
    /**
     * <p>Shuts down this {@link DummyContentsHandler} and all {@link DummyInstance DummyInstances} it {@link #instances() contains}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>More specifically, <code><i>{@link #shutdown(DummyInstance)}</i></code> is called on every {@link DummyInstance} in this {@link DummyContentsHandler ContentsHandler's} {@link #instances() Instance List}.</li>
     * </ol>
     *
     * @throws RuntimeException If the shutdown operation was unsuccessful.
     * @see #shutdown(DummyInstance)
     */
    // TODO: Stop exceptions from preventing a full shut-down
    public void shutdown()
    {
        final ArrayList<DummyInstance> shutdownFailures = new ArrayList<>();
        instances.forEach(instance -> {
            if (!shutdown(instance))
                shutdownFailures.add(instance);
        });
        
        if (!shutdownFailures.isEmpty())
            throw ExceptionTools.ex("Some shutdown operations have failed! " + shutdownFailures);
        if (!instances.isEmpty())
            throw ExceptionTools.ex("Instance List is not empty after shutdown operation! " + instances);
    }
    
    /**
     * <p>Shuts down the specified {@link DummyInstance}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>First, the specified {@link DummyInstance} is removed from the {@link #instances() Instance List}.</li>
     *     <li>Second, <code><i>{@link DummyInstance#shutdownInstanceEngine()}</i></code> is called on the specified {@link DummyInstance}.</li>
     *     <li>For {@link #shutdown(DummyInstance) this method} to return {@code true}, both of the aforementioned operations must have been successful.</li>
     *     <li>Note that both operations are always attempted, even if the first is unsuccessful.</li>
     * </ol>
     *
     * @param instance The {@link DummyInstance} being shutdown.
     *
     * @return True if the specified {@link DummyInstance} was successfully shutdown, false if it was not.
     */
    public boolean shutdown(DummyInstance instance)
    {
        final boolean removed = instances.remove(instance);
        return instance.shutdownInstanceEngine() && removed;
    }
}