package com.taco.tacository.game.ui.pages;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.objects.tiles.GameTile;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.ui.UIBook;
import com.taco.tacository.ui.UIPage;
import com.taco.tacository.util.synchronization.Lockable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

/**
 * <p>Defines the {@link UIPage} displaying the currently-selected {@link GameTile} for a {@link GameViewContent} instance.</p>
 *
 * @see GameTileEditorPageController
 */
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
    
    @Override public final @NotNull GameViewContent getContent() { return content; }
    
    //</editor-fold>
}
