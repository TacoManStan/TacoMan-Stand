package com.taco.suit_lady.view.ui.jfx.overlay;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    
    private final ReadOnlyListWrapper<Overlay> overlays;
    
    public OverlayHandler(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable Overlay... initialOverlays)
    {
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input");
        this.lock = lock;
        
        this.overlays = new ReadOnlyListWrapper<>(initInitialOverlayList(initialOverlays));
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private ObservableList<Overlay> initInitialOverlayList(@Nullable Overlay[] initialOverlays)
    {
        return initialOverlays != null ? FXCollections.observableArrayList(initialOverlays) : FXCollections.observableArrayList();
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
    
    //<editor-fold desc="--- UNUSED/OLD ---">
    
    /**
     * <p>Currently Unused</p>
     */
    private ObservableList<Overlay> initInitialOverlayListOld(@Nullable Object wildcard)
    {
        return wildcard instanceof ObservableList<?> && ArrayTools.containsTypeAll((ObservableList<?>) wildcard, Overlay.class)
                ? ((ObservableList<Overlay>) wildcard) : FXCollections.observableArrayList();
    }
    
    //</editor-fold>
}
