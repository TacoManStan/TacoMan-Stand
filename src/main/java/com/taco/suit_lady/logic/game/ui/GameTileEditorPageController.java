package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.objects.GameTile;
import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.BindingsSL;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

@Component
@FxmlView("/fxml/game/pages/game_tile_editor_page.fxml")
@Scope("prototype")
public class GameTileEditorPageController extends UIPageController<GameTileEditorPage>
        implements Lockable, GameComponent {
    
    @FXML private AnchorPane root;
    @FXML private Label titleLabel;
    
    protected GameTileEditorPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Lock getLock() { return getPage().getLock(); }
    
    //
    
    @Override public Pane root() { return root; }
    
    @Override public void initialize() { }
    
    public GameTileEditorPageController init() {
        titleLabel.textProperty().bind(BindingsSL.stringBinding(() -> {
            final GameTile tile = getUIData().getSelectedTile();
            if (tile != null)
                return "Tile [" + tile.getXLoc() + ", " + tile.getYLoc() + "]";
            else
                return "No Tile Selected";
        }, getUIData().readOnlySelectedTileProperty()));
        
        return this;
    }
    
    
    @Override public @NotNull GameViewContent getGame() { return getPage().getGame(); }
    
    //</editor-fold>
}
