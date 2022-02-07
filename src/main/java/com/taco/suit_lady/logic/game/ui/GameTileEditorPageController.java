package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.objects.GameObject;
import com.taco.suit_lady.logic.game.objects.GameTile;
import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.ui.jfx.lists.ListCellFX;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.suit_lady.util.tools.list_tools.ListsSL;
import com.taco.suit_lady.util.tools.list_tools.Operation;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    @FXML private ListView<String> terrainObjListView;
    @FXML private ChoiceBox<String> tileImageIdChoiceBox;
    @FXML private Button addTerrainObjButton;
    @FXML private Button removeTerrainObjButton;
    @FXML private ChoiceBox<String> terrainObjSelectorChoiceBox;
    @FXML private ImagePane tileImagePane;
    
    private final ReadOnlyStringWrapper selectedTileImageIdProperty;
    private final ReadOnlyListWrapper<String> selectedTileTerrainObjsProperty;
    
    private final ListProperty<GameObject> selectedTileContents;
    
    protected GameTileEditorPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.selectedTileContents = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.selectedTileImageIdProperty = new ReadOnlyStringWrapper();
        this.selectedTileTerrainObjsProperty = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Lock getLock() { return getPage().getLock(); }
    
    //
    
    @Override public Pane root() { return root; }
    
    @Override public void initialize() {
        tileImageIdChoiceBox.getItems().addAll("grass", "dirt", "sand", "rock");
        tileImageIdChoiceBox.valueProperty().bindBidirectional(selectedTileImageIdProperty);
        
        
        terrainObjSelectorChoiceBox.getItems().addAll("rock_e", "rock_s", "rock_w", "rock_ne", "rock_nw", "rock_se", "rock_sw");
        terrainObjSelectorChoiceBox.getSelectionModel().selectFirst();
        
        addTerrainObjButton.setOnAction(event -> onAddTerrainObj(event));
        removeTerrainObjButton.setOnAction(event -> onRemoveTerrainObj(event));
    }
    
    public GameTileEditorPageController init() {
        titleLabel.textProperty().bind(BindingsSL.stringBinding(() -> {
            final GameTile tile = getUIData().getSelectedTile();
            if (tile != null)
                return "Tile [" + tile.getLocationX() + ", " + tile.getLocationY() + "]";
            else
                return "No Tile Selected";
        }, getUIData().readOnlySelectedTileProperty()));
        
        
        ListsSL.applyListener(selectedTileContents, (op1, op2, opType, triggerType) -> {
            if (triggerType == Operation.TriggerType.CHANGE) switch (opType) {
                case ADDITION -> onObjAdded(op1.contents());
                case REMOVAL -> onObjRemoved(op1.contents());
                case PERMUTATION -> System.out.println("Occupying Game Obj Permutation");
            }
        });
        
        ListsSL.applyListener(selectedTileTerrainObjsProperty, (op1, op2, opType, triggerType) -> {
            if (triggerType == Operation.TriggerType.CHANGE) switch (opType) {
                case ADDITION -> onTerrainObjAdded(op1.contents());
                case REMOVAL -> onTerrainObjRemoved(op1.contents());
                case PERMUTATION -> System.out.println("Tile Terrain Obj Permutation");
            }
        });
        
        tileContentsListView.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX,
                        cellData -> ResourcesSL.get(
                                cellData,
                                () -> weaver().loadController(GameTileContentElementController.class),
                                listView.hashCode()))));
        
        getUIData().readOnlySelectedTileProperty().addListener((observable, oldValue, newValue) -> {
            sync(() -> {
                if (oldValue != null) {
                    selectedTileContents.unbind();
                    selectedTileTerrainObjsProperty.unbind();
                    selectedTileImageIdProperty.unbindBidirectional(oldValue.getModel().imageIdProperty());
                    tileImagePane.imageProperty().unbind();
                }
                if (newValue != null) {
                    selectedTileContents.bind(newValue.getOccupyingObjects());
                    selectedTileTerrainObjsProperty.bind(newValue.getModel().terrainTileObjects());
                    selectedTileImageIdProperty.bindBidirectional(newValue.getModel().imageIdProperty());
                    tileImagePane.imageProperty().bind(newValue.getModel().imageBinding());
//                    System.out.println("Terrain Objs for Tile [" + newValue.getXLoc() + ", " + newValue.getYLoc() + "]: " + newValue.getModel().terrainTileObjects());
                }
            });
        });
        
        return this;
    }
    
    private void onObjAdded(GameObject obj) { tileContentsListView.getItems().add(obj); }
    private void onObjRemoved(GameObject obj) { tileContentsListView.getItems().remove(obj); }
    
    private void onTerrainObjAdded(String obj) { terrainObjListView.getItems().add(obj); }
    private void onTerrainObjRemoved(String obj) { terrainObjListView.getItems().remove(obj); }
    
    private void onAddTerrainObj(ActionEvent event) {
        syncFX(() -> {
            selectedTileTerrainObjsProperty.add(terrainObjSelectorChoiceBox.getValue());
        });
    }
    private void onRemoveTerrainObj(ActionEvent event) {
        syncFX(() -> {
            final String selectedTerrainObj = terrainObjListView.getSelectionModel().getSelectedItem();
            if (selectedTerrainObj != null)
                selectedTileTerrainObjsProperty.remove(selectedTerrainObj);
            else
                System.err.println("WARNING: Attempting to remove null terrain obj.");
        });
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
