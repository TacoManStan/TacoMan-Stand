package com.taco.suit_lady.game.ui.pages;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import com.taco.suit_lady.util.Lockable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class GameTileEditorPage extends UIPage<GameTileEditorPageController>
        implements Lockable, GameComponent {
    
    private final GameViewContent content;
    
    public GameTileEditorPage(@NotNull UIBook owner, @NotNull GameViewContent content, Object... constructorParams) {
        super(owner, constructorParams);
        this.content = content;
    }
    
    public final GameTileEditorPage init() {
        getController().init();
        return this;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull Lock getLock() { return getGame().getLock(); }
    
    //
    
    @Override protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override protected final @NotNull Class<GameTileEditorPageController> controllerDefinition() {
        return GameTileEditorPageController.class;
    }
    
    @Override public final @NotNull GameViewContent getGame() { return content; }
    
    //</editor-fold>
}
