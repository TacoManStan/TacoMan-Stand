package com.taco.suit_lady.ui.pages.client_instance_list_old_demo;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TaskTools;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DummyClientHandler
        implements Springable {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReentrantLock lock;
    
    private final ReadOnlyListWrapper<DummyClient> clientList;
    private final ObjectProperty<DummyClient> selectedClientProperty;
    
    public DummyClientHandler(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.lock = new ReentrantLock();
        
        this.clientList = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        this.selectedClientProperty = new SimpleObjectProperty<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReentrantLock getLock() {
        return lock;
    }
    
    
    public final ReadOnlyListProperty<DummyClient> clientList() {
        return clientList.getReadOnlyProperty();
    }
    
     public final ObjectProperty<DummyClient> selectedClientProperty() {
        return selectedClientProperty;
     }
     
     public final DummyClient getSelectedClient() {
        return selectedClientProperty.get();
     }
     
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return ctx;
    }
    
    //</editor-fold>
    
    public final @NotNull DummyClient newClient() {
        DummyClient client = new DummyClient(this);
        TaskTools.sync(lock, () -> clientList.add(client));
        client.start();
        return client;
    }
    
    
    public void shutdown() {
        // TODO: Move to separate, synchronized task threads for each DummyClient instance.
        logiCore().execute(() -> {
            TaskTools.sync(lock, () -> clientList.forEach(client -> shutdown(client)));
            if (!clientList.isEmpty())
                throw ExceptionTools.ex("Client List should be empty! (" + clientList + ")");
        });
    }
    
    public void shutdown(DummyClient client) {
        clientList.remove(client);
        client.shutdown();
    }
}
