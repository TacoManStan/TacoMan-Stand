package com.taco.suit_lady.game.ui.pages;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import com.taco.suit_lady.util.Lockable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class GameObjectEditorPage extends UIPage<GameObjectEditorPageController>
        implements Lockable, GameComponent {
    
    private final GameTileEditorPage parent;
    
    public GameObjectEditorPage(@NotNull UIBook owner, @NotNull GameTileEditorPage parent, Object... constructorParams) {
        super(owner, constructorParams);
        this.parent = parent;
    }
    
    public final GameObjectEditorPage init() {
        getController().init();
        return this;
    }
    
    public final GameTileEditorPage getParent() { return parent; }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull Lock getLock() { return getGame().getLock(); }
    
    //
    
    @Override protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override protected final @NotNull Class<GameObjectEditorPageController> controllerDefinition() {
        return GameObjectEditorPageController.class;
    }
    
    @Override public final @NotNull GameViewContent getGame() { return parent.getGame(); }
    
    //</editor-fold>
}
