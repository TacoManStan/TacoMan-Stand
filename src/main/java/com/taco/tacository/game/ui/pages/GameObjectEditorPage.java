package com.taco.tacository.game.ui.pages;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.attributes.Attribute;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.ui.UIBook;
import com.taco.tacository.ui.UIPage;
import com.taco.tacository.util.synchronization.Lockable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

/**
 * <p>Defines a {@link UIPage} displaying the {@link Attribute Attributes} and other properties of the selected {@link GameObject} instance.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@link GameObjectEditorPage} is {@link #turnTo(UIPage) turned to} from the {@link GameTileEditorPage}.</li>
 * </ol>
 *
 * @see GameObjectEditorPageController
 */
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
