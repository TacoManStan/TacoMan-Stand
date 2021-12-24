package com.taco.suit_lady.ui.pages.client_instance_list_old_demo;

import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public class DummyClient
        implements Springable, UIDProcessable {
    
    private final StrictSpringable springable;
    
    private final ReadOnlyStringWrapper titleProperty;
    private final ReadOnlyStringWrapper descriptionProperty;
    
    private final BooleanProperty runningProperty;
    
    public DummyClient(Springable springable) {
        this.springable = springable.asStrict();
        
        this.titleProperty = new ReadOnlyStringWrapper("Test Title");
        this.descriptionProperty = new ReadOnlyStringWrapper("Test Description");
        
        this.runningProperty = new SimpleBooleanProperty();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final BooleanProperty runningProperty() {
        return runningProperty;
    }
    
    public final boolean isRunning() {
        return runningProperty.get();
    }
    
    public final boolean setIsRunning(boolean newValue) {
        boolean oldValue = isRunning();
        runningProperty.set(newValue);
        return oldValue;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    private UIDProcessor uidProcessor;
    
    @Override
    public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("dummy_clients");
        return uidProcessor;
    }
    
    //</editor-fold>
    
    public void start() {
        debugger().print("Dummy Client Started");
    }
    
    public void shutdown() {
        debugger().print("Dummy Client Shutdown");
        clientHandler().shutdown(this);
    }
}
