package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainable;
import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.ui.ContentData;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import javafx.beans.Observable;
import org.jetbrains.annotations.NotNull;

public class GameViewContentData extends ContentData
        implements ObservablePropertyContainable, SpringableWrapper, UIDProcessable, GameComponent {
    
    private final GameViewContent content;
    
    public GameViewContentData(@NotNull GameViewContent content) {
        this.content = content;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    @Override public @NotNull Springable springable() { return content; }
    @Override public @NotNull Observable[] properties() { return new Observable[]{}; }
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("game_view_content");
        return uidProcessor;
    }
    
    //</editor-fold>
}
