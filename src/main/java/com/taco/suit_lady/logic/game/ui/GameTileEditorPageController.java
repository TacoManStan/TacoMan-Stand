package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.objects.GameObject;
import com.taco.suit_lady.logic.game.objects.GameTile;
import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.ui.jfx.lists.ListCellFX;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.suit_lady.util.tools.list_tools.ListsSL;
import com.taco.suit_lady.util.tools.list_tools.Operation;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
        implements Lockable, GameComponent, UIDProcessable {
    
    @FXML private AnchorPane root;
    
    @FXML private Label titleLabel;
    @FXML private ListView<GameObject> tileContentsListView;
    
    private final ReadOnlyObjectWrapper<ObservableList<GameObject>> selectedTileContentsProperty;
    
    protected GameTileEditorPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        selectedTileContentsProperty = new ReadOnlyObjectWrapper<>();
    }
    
    public final ReadOnlyObjectProperty<ObservableList<GameObject>> readOnlySelectedTileContentsProperty() { return selectedTileContentsProperty.getReadOnlyProperty(); }
    public final ObservableList<GameObject> getSelectedTileContents() { return selectedTileContentsProperty.get(); }
    
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
        
        
        selectedTileContentsProperty.bind(BindingsSL.objBinding(() -> {
            final GameTile selectedTile = getUIData().getSelectedTile();
            if (selectedTile != null)
                return selectedTile.getOccupyingObjects();
            else
                return FXCollections.observableArrayList();
        }, getUIData().readOnlySelectedTileProperty()));
        
        tileContentsListView.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX,
                        cellData -> ResourcesSL.get(
                                cellData,
                                () -> weaver().loadController(GameTileContentElementController.class),
                                listView.hashCode()))));
        
        selectedTileContentsProperty.addListener((observable, oldValue, newValue) -> {
            sync(() -> {
                tileContentsListView.getItems().clear();
                if (newValue != null) {
                    System.out.println("Adding All: " + newValue);
                    tileContentsListView.getItems().addAll(newValue);
                }
            });
        });
        
        //        ListsSL.applyListener(, (op, opType, triggerType) -> {
        //            if (triggerType == Operation.TriggerType.CHANGE)
        //                switch (opType) {
        //                    case ADDITION -> onAdded(op.contents());
        //                    case REMOVAL -> onRemoved(op.contents());
        //                }
        //        });
        
        return this;
    }
    
    
    @Override public @NotNull GameViewContent getGame() { return getPage().getGame(); }
    
    //
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("game-tile-editor-data");
        return uidProcessor;
    }
    
    //</editor-fold>
}
