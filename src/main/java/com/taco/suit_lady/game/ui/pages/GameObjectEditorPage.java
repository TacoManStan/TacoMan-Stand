package com.taco.suit_lady.game.ui.pages;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.attributes.Attribute;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import com.taco.suit_lady.util.synchronization.Lockable;
import org.checkerframework.checker.guieffect.qual.UI;
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
