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
import com.taco.suit_lady.util.tools.list_tools.ListsSL.SimpleOperationResponder;
import com.taco.suit_lady.util.tools.list_tools.Operation;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private final ListProperty<GameObject> selectedTileContents;
    
    protected GameTileEditorPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.selectedTileContents = new SimpleListProperty<>(FXCollections.observableArrayList());
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
        
        getUIData().readOnlySelectedTileProperty().addListener((observable, oldValue, newValue) -> {
            sync(() -> {
                selectedTileContents.unbind();
                selectedTileContents.clear();
                if (newValue != null)
                    selectedTileContents.bind(newValue.getOccupyingObjects());
            });
        });
        
        ListsSL.applyListener(selectedTileContents, (op1, op2, opType, triggerType) -> {
            if (triggerType == Operation.TriggerType.CHANGE) switch (opType) {
                case ADDITION -> onAdded(op1.contents());
                case REMOVAL -> onRemoved(op1.contents());
                case PERMUTATION -> System.out.println("Permutation");
            }
        });
        
        tileContentsListView.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX,
                        cellData -> ResourcesSL.get(
                                cellData,
                                () -> weaver().loadController(GameTileContentElementController.class),
                                listView.hashCode()))));
        
        return this;
    }
    
    private void onAdded(GameObject obj) {
        tileContentsListView.getItems().add(obj);
    }
    
    private void onRemoved(GameObject obj) {
        tileContentsListView.getItems().remove(obj);
    }
    
    private void refreshListView(@Nullable GameObject obj) {
        tileContentsListView.getItems().clear();
        if (obj != null) {
            System.out.println("Adding All: " + obj);
            tileContentsListView.getItems().addAll(obj);
        }
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
