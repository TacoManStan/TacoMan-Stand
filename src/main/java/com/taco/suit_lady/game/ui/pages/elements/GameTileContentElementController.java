package com.taco.suit_lady.game.ui.pages.elements;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/game/pages/game_tile_content_element.fxml")
@Scope("prototype")
public class GameTileContentElementController extends CellController<GameObject> {
    
    @FXML private AnchorPane root;
    
    @FXML private Label objectNameLabel;
    @FXML private ImagePane objectImagePane;
    
    public GameTileContentElementController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public Pane root() { return root; }
    
    @Override public void initialize() { super.initialize(); }
    
    @Override protected void onContentChange(GameObject oldCellContents, GameObject newCellContents) {
        objectNameLabel.textProperty().unbind();
        objectImagePane.imageProperty().unbind();
        if (newCellContents != null) {
            objectNameLabel.textProperty().bind(newCellContents.getModel().getDefinition().readOnlyImageIdProperty());
            objectImagePane.imageProperty().bind(newCellContents.getModel().imageBinding());
        }
    }
    
    //</editor-fold>
}
