package com.taco.tacository.game.ui.nia;

import com.taco.tacository._to_sort._new.interfaces.ObservablePropertyContainable;
import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.ui.*;
import com.taco.tacository.ui.ContentData;
import com.taco.tacository.util.UIDProcessable;
import com.taco.tacository.util.UIDProcessor;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.synchronization.Lockable;
import javafx.beans.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class NiaContentData extends ContentData<NiaContent, NiaContentData, NiaContentController, NiaFooter, NiaFooterController>
        implements ObservablePropertyContainable, SpringableWrapper, Lockable, UIDProcessable {
    
    private final NiaContent content;
    private final NiaUIData uiData;
    
    public NiaContentData(@NotNull NiaContent content) {
        this.content = content;
        this.uiData = new NiaUIData(this);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return content; }
    @Override public @NotNull Lock getLock() { return content.getLock(); }
    
    @Override public @NotNull Observable[] properties() { return new Observable[]{}; }
    
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("game_view_content");
        return uidProcessor;
    }
    
    //</editor-fold>
}
