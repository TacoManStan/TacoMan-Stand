package com.taco.tacository.game.ui.pages;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.ui.UIBook;
import com.taco.tacository.ui.UIPage;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines the {@link GameViewContent#getCoverPage() Cover} {@link UIPage Page} for {@link GameViewContent}.</p>
 *
 * @see GameViewPageController
 */
public class GameViewPage extends UIPage<GameViewPageController>
        implements GameComponent {
    
    private final GameViewContent content;
    
    public GameViewPage(@NotNull UIBook owner, @NotNull GameViewContent content, Object... constructorParams) {
        super(owner, constructorParams);
        this.content = content;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    @Override protected void initializePage(@NotNull Object[] constructorParams) { }
    @Override protected @NotNull Class<GameViewPageController> controllerDefinition() { return GameViewPageController.class; }
    
    //</editor-fold>
}
