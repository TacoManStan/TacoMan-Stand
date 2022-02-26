package com.taco.suit_lady.game.ui;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainable;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.ui.ContentData;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import javafx.beans.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class GameViewContentData extends ContentData<GameViewContent, GameViewContentData, GameViewContentController, GameFooter, GameFooterController>
        implements ObservablePropertyContainable, SpringableWrapper, Lockable, UIDProcessable, GameComponent {
    
    private final GameViewContent content;
    private final GameUIData uiData;
    
    public GameViewContentData(@NotNull GameViewContent content) {
        this.content = content;
        this.uiData = new GameUIData(this);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    @Override public @NotNull GameViewContentData getData() { return this; }
    @Override public @NotNull GameUIData getUIData() { return uiData; }
    
    //
    
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
