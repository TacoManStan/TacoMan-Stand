package com.taco.suit_lady.view.ui;

import com.taco.suit_lady._to_sort._new.ReadOnlyObservableList;
import com.taco.suit_lady._to_sort._new.ReadOnlyObservableListWrapper;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OverlayHandler
        implements Springable, Lockable
{
    private final Springable springable;
    private final ReentrantLock lock;

    private final StackPane rootPane;
    private final ReadOnlyObservableListWrapper<Overlay> overlays;
    
    public OverlayHandler(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable Overlay... initialOverlays)
    {
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input");
        this.lock = lock;
        
        this.overlays = new ReadOnlyObservableListWrapper<>(initInitialOverlayList(initialOverlays));
        this.rootPane = new StackPane();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private @NotNull ObservableList<Overlay> initInitialOverlayList(@Nullable Overlay[] initialOverlays)
    {
        return initialOverlays != null ? FXCollections.observableArrayList(initialOverlays) : FXCollections.observableArrayList();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObservableList<Overlay> overlays()
    {
        return overlays.getReadOnlyList();
    }
    
    protected final StackPane getRoot()
    {
        return rootPane;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver()
    {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx()
    {
        return springable.ctx();
    }
    
    @Override
    public @NotNull Lock getLock()
    {
        return lock != null ? lock : new ReentrantLock();
    }
    
    //</editor-fold>
}
