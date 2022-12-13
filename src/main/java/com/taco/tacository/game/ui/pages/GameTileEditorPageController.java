package com.taco.tacository.game.ui.pages;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.objects.tiles.GameTile;
import com.taco.tacository.game.objects.tiles.GameTileModel;
import com.taco.tacository.game.objects.tiles.TileTerrainObject;
import com.taco.tacository.game.ui.pages.elements.GameTileContentElementController;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.game.ui.pages.elements.TerrainTileContentElementController;
import com.taco.tacository.ui.UIPageController;
import com.taco.tacository.ui.jfx.components.ImagePane;
import com.taco.tacository.ui.jfx.lists.CellControlManager;
import com.taco.tacository.ui.jfx.lists.ListCellFX;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.UIDProcessable;
import com.taco.tacository.util.UIDProcessor;
import com.taco.tacository.util.tools.Bind;
import com.taco.tacository.util.tools.Stuff;
import com.taco.tacository.util.tools.fx_tools.FX;
import com.taco.tacository.util.tools.list_tools.L;
import com.taco.tacository.util.tools.list_tools.Op;
import javafx.beans.binding.ObjectBinding;
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
import javafx.scene.image.Image;
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
    @FXML private ListView<TileTerrainObject> terrainObjListView;
    @FXML private ChoiceBox<String> tileImageIdChoiceBox;
    @FXML private Button addTerrainObjButton;
    @FXML private Button removeTerrainObjButton;
    @FXML private ImagePane tileImagePane;
    @FXML private ImagePane texturePreviewImagePane;
    @FXML private Button editGameObjectButton;
    
    private final ReadOnlyStringWrapper selectedTileImageIdProperty;
    private final ReadOnlyListWrapper<TileTerrainObject> selectedTileTerrainObjsProperty;
    
    private ObjectBinding<Image> texturePreviewImageBinding;
    
    private final ListProperty<GameObject> selectedTileContents;
    
    
    private ObjectBinding<GameObject> selectedGameObjectBinding;
    
    protected GameTileEditorPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.selectedTileContents = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.selectedTileImageIdProperty = new ReadOnlyStringWrapper();
        
        this.selectedTileTerrainObjsProperty = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public void initialize() {
        tileImageIdChoiceBox.getItems().addAll("grass", "dirt", "sand", "rock");
        tileImageIdChoiceBox.valueProperty().bindBidirectional(selectedTileImageIdProperty);
        
        addTerrainObjButton.setOnAction(this::onAddTerrainObj);
        removeTerrainObjButton.setOnAction(this::onRemoveTerrainObj);
        
        editGameObjectButton.setOnAction(this::onEditGameObject);
    }
    
    public GameTileEditorPageController init() {
        titleLabel.textProperty().bind(Bind.stringBinding(() -> {
            final GameTile tile = getUIData().getSelectedTile();
            if (tile != null)
                return "Tile [" + tile.getLocationX() + ", " + tile.getLocationY() + "]";
            else
                return "No Tile Selected";
        }, getUIData().readOnlySelectedTileProperty()));
        
        
        L.applyListener(selectedTileContents, (op1, op2, opType, triggerType) -> {
            if (triggerType == Op.TriggerType.CHANGE) switch (opType) {
                case ADDITION -> onObjAdded(op1.contents());
                case REMOVAL -> onObjRemoved(op1.contents());
                case PERMUTATION -> System.out.println("Occupying Game Obj Permutation");
            }
        });
        
        L.applyListener(selectedTileTerrainObjsProperty, (op1, op2, opType, triggerType) -> {
            if (triggerType == Op.TriggerType.CHANGE) switch (opType) {
                case ADDITION -> onTerrainObjAdded(op1.contents());
                case REMOVAL -> onTerrainObjRemoved(op1.contents());
                case PERMUTATION -> System.out.println("Tile Terrain Obj Permutation");
            }
        });
        
        
        tileContentsListView.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX,
                        cellData -> Stuff.get(
                                cellData,
                                () -> weaver().loadController(GameTileContentElementController.class),
                                listView.hashCode()))));
        
        terrainObjListView.setCellFactory(listView -> new ListCellFX<>(
                listCellFX -> new CellControlManager<>(
                        listCellFX,
                        cellData -> Stuff.get(
                                cellData,
                                () -> weaver().loadController(TerrainTileContentElementController.class),
                                listView.hashCode()))));
        
        
        getUIData().readOnlySelectedTileProperty().addListener((observable, oldValue, newValue) -> FX.runFX(() -> {
            if (oldValue != null) {
                selectedTileContents.unbind();
                selectedTileTerrainObjsProperty.unbindBidirectional(oldValue.getModel().terrainTileObjects());
                selectedTileImageIdProperty.unbindBidirectional(oldValue.getModel().imageIdProperty());
                tileImagePane.imageProperty().unbind();
            }
            if (newValue != null) {
                selectedTileContents.bind(newValue.getOccupyingObjects());
                selectedTileTerrainObjsProperty.bindBidirectional(newValue.getModel().terrainTileObjects());
                selectedTileImageIdProperty.bindBidirectional(newValue.getModel().imageIdProperty());
                tileImagePane.imageProperty().bind(newValue.getModel().borderlessImageBinding());
            }
        }, true));
        
        
        texturePreviewImageBinding = Bind.recursiveObjBinding(getLock(), tile -> {
            if (tile != null) {
                final GameTileModel model = tile.getModel();
                if (model != null)
                    return model.textureOnlyImageBinding();
            }
            return null;
        }, getUIData().readOnlySelectedTileProperty());
        texturePreviewImagePane.imageProperty().bind(texturePreviewImageBinding);
        
        
        selectedGameObjectBinding = Bind.objBinding(() -> tileContentsListView.getSelectionModel().getSelectedItem(), tileContentsListView.getSelectionModel().selectedItemProperty());
        editGameObjectButton.disableProperty().bind(Bind.boolBinding(() -> getSelectedGameObject() == null, selectedGameObjectBinding));
        
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ObjectBinding<GameObject> selectedGameObjectBinding() { return selectedGameObjectBinding; }
    public final GameObject getSelectedGameObject() { return selectedGameObjectBinding.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Lock getLock() { return getPage().getLock(); }
    
    //
    
    @Override public Pane root() { return root; }
    
    //
    
    private void onObjAdded(GameObject obj) {
        if (obj != null) FX.runFX(() -> {
            tileContentsListView.getItems().add(obj);
            tileContentsListView.getSelectionModel().select(obj);
        });
    }
    
    private void onObjRemoved(GameObject obj) {
        if (obj != null) FX.runFX(() -> {
            tileContentsListView.getItems().remove(obj);
            if (tileContentsListView.getSelectionModel().getSelectedItem() == null)
                tileContentsListView.getSelectionModel().selectFirst();
        });
    }
    
    private void onTerrainObjAdded(TileTerrainObject obj) {
        if (obj != null) FX.runFX(() -> {
            terrainObjListView.getItems().add(obj);
            terrainObjListView.getSelectionModel().select(obj);
        });
    }
    private void onTerrainObjRemoved(TileTerrainObject obj) {
        if (obj != null) FX.runFX(() -> {
            terrainObjListView.getItems().remove(obj);
            if (terrainObjListView.getSelectionModel().getSelectedItem() == null)
                terrainObjListView.getSelectionModel().selectFirst();
        });
    }
    
    private void onAddTerrainObj(ActionEvent event) {
        FX.callFX(() -> selectedTileTerrainObjsProperty.add(new TileTerrainObject(getUIData().getSelectedTile().getModel())));
    }
    private void onRemoveTerrainObj(ActionEvent event) {
        FX.runFX(() -> {
            final TileTerrainObject selectedTerrainObject = terrainObjListView.getSelectionModel().getSelectedItem();
            if (selectedTerrainObject != null)
                selectedTileTerrainObjsProperty.remove(selectedTerrainObject);
            else
                System.err.println("WARNING: Attempting to remove null terrain obj.");
        });
    }
    
    
    private void onEditGameObject(@NotNull ActionEvent event) {
        final GameObject selectedGameObject = getSelectedGameObject();
        if (selectedGameObject != null)
            FX.runFX(() -> getPage().turnToNew(new GameObjectEditorPage(getPage().getOwner(), getPage()).init()));
        else
            System.err.println("WARNING: Attempting to edit a null GameObject.");
    }
    
    
    @Override public @NotNull GameViewContent getContent() { return getPage().getGame(); }
    
    //
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("game-tile-editor-data");
        return uidProcessor;
    }
    
    //</editor-fold>
}
