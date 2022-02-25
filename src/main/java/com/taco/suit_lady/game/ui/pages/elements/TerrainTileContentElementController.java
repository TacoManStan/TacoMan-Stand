package com.taco.suit_lady.game.ui.pages.elements;

import com.taco.suit_lady.game.objects.tiles.TileTerrainObject;
import com.taco.suit_lady.game.objects.tiles.TileTerrainObjectID;
import com.taco.suit_lady.game.objects.tiles.TileTerrainObjectOrientationID;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.components.button.ImageButton;
import com.taco.suit_lady.ui.jfx.components.button.ImageButtonGroup;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/game/pages/terrain_tile_content_element.fxml")
@Scope("prototype")
public class TerrainTileContentElementController extends CellController<TileTerrainObject> {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    
    @FXML private ImagePane previewImagePane;
    @FXML private Label titleLabel;
    @FXML private ChoiceBox<TileTerrainObjectID> textureChoiceBox;
    
    
    @FXML private ImagePane borderlessImagePane;
    
    @FXML private ImagePane nImagePane;
    @FXML private ImagePane sImagePane;
    @FXML private ImagePane eImagePane;
    @FXML private ImagePane wImagePane;
    
    @FXML private ImagePane nwImagePane;
    @FXML private ImagePane neImagePane;
    @FXML private ImagePane swImagePane;
    @FXML private ImagePane seImagePane;
    
    //
    
    private ImageButton borderlessImageButton;
    
    private ImageButton nImageButton;
    private ImageButton sImageButton;
    private ImageButton eImageButton;
    private ImageButton wImageButton;
    
    private ImageButton neImageButton;
    private ImageButton nwImageButton;
    private ImageButton seImageButton;
    private ImageButton swImageButton;
    
    //</editor-fold>
    
    private final ImageButtonGroup buttonGroup;
    
    public TerrainTileContentElementController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.buttonGroup = new ImageButtonGroup();
        this.terrainObjectOrientationIdListener = (observable, oldValue, newValue) -> buttonGroup.setSelectedButton(getButtonFromOrientationId(newValue));
    }
    
    public final ImageButtonGroup getButtonGroup() { return buttonGroup; }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public Pane root() { return root; }
    
    @Override public void initialize() {
        super.initialize();
        
        initImageButtons();
        buttonGroup.selectedButtonProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == null && newValue == null)
                throw ExceptionsSL.ex("Old and new button values cannot both be null.");
            
            final TileTerrainObject contents = getContents();
            if (contents != null) {
                final TileTerrainObjectOrientationID orientationID = getOrientationIdFromButton(newValue);
                if (orientationID != null)
                    contents.setOrientationId(orientationID);
                else if (oldValue != null)
                    buttonGroup.setSelectedButton(oldValue);
            }
        });
    }
    
    private void initImageButtons() {
        //<editor-fold desc="> ImageButton Initialization">
        
        borderlessImageButton = new ImageButton(this, "Borderless", "box_arrow_borderless", borderlessImagePane, null, null, true, ImageButton.SMALL).init();
        
        nImageButton = new ImageButton(this, "North", "box_arrow_n", nImagePane, null, null, true, ImageButton.SMALL).init();
        sImageButton = new ImageButton(this, "South", "box_arrow_s", sImagePane, null, null, true, ImageButton.SMALL).init();
        eImageButton = new ImageButton(this, "East", "box_arrow_e", eImagePane, null, null, true, ImageButton.SMALL).init();
        wImageButton = new ImageButton(this, "West", "box_arrow_w", wImagePane, null, null, true, ImageButton.SMALL).init();
        
        neImageButton = new ImageButton(this, "North-East", "box_arrow_ne", neImagePane, null, null, true, ImageButton.SMALL).init();
        nwImageButton = new ImageButton(this, "North-West", "box_arrow_nw", nwImagePane, null, null, true, ImageButton.SMALL).init();
        seImageButton = new ImageButton(this, "South-East", "box_arrow_se", seImagePane, null, null, true, ImageButton.SMALL).init();
        swImageButton = new ImageButton(this, "South-West", "box_arrow_sw", swImagePane, null, null, true, ImageButton.SMALL).init();
        
        //</editor-fold>
        
        borderlessImageButton.setButtonGroup(buttonGroup);
        
        nImageButton.setButtonGroup(buttonGroup);
        sImageButton.setButtonGroup(buttonGroup);
        eImageButton.setButtonGroup(buttonGroup);
        wImageButton.setButtonGroup(buttonGroup);
        
        neImageButton.setButtonGroup(buttonGroup);
        nwImageButton.setButtonGroup(buttonGroup);
        seImageButton.setButtonGroup(buttonGroup);
        swImageButton.setButtonGroup(buttonGroup);
        
        
        buttonGroup.setSelectedButton(borderlessImageButton);
    }
    
    private final ChangeListener<? super TileTerrainObjectOrientationID> terrainObjectOrientationIdListener;
    
    @Override protected void onContentChange(TileTerrainObject oldCellContents, TileTerrainObject newCellContents) {
        titleLabel.textProperty().unbind();
        previewImagePane.imageProperty().unbind();
        
        if (oldCellContents != null) {
            textureChoiceBox.valueProperty().unbindBidirectional(oldCellContents.idProperty());
            oldCellContents.orientationIdProperty().removeListener(terrainObjectOrientationIdListener);
        }
        
        if (newCellContents != null) {
            titleLabel.textProperty().bind(newCellContents.aggregateTextureIdBinding());
            previewImagePane.imageProperty().bind(newCellContents.imageBinding());
            textureChoiceBox.valueProperty().bindBidirectional(newCellContents.idProperty());
            newCellContents.orientationIdProperty().addListener(terrainObjectOrientationIdListener);
        }
    }
    
    //</editor-fold>
    
    private TileTerrainObjectOrientationID getOrientationIdFromSelectedButton() {
        return getOrientationIdFromButton(buttonGroup.getSelectedButton());
    }
    
    private TileTerrainObjectOrientationID getOrientationIdFromButton(@Nullable ImageButton button) {
        if (button != null)
            if (button.equals(borderlessImageButton))
                return TileTerrainObjectOrientationID.BORDERLESS;
            else if (button.equals(nImageButton))
                return TileTerrainObjectOrientationID.NORTH;
            else if (button.equals(sImageButton))
                return TileTerrainObjectOrientationID.SOUTH;
            else if (button.equals(eImageButton))
                return TileTerrainObjectOrientationID.EAST;
            else if (button.equals(wImageButton))
                return TileTerrainObjectOrientationID.WEST;
            else if (button.equals(neImageButton))
                return TileTerrainObjectOrientationID.NORTH_EAST;
            else if (button.equals(nwImageButton))
                return TileTerrainObjectOrientationID.NORTH_WEST;
            else if (button.equals(seImageButton))
                return TileTerrainObjectOrientationID.SOUTH_EAST;
            else if (button.equals(swImageButton))
                return TileTerrainObjectOrientationID.SOUTH_WEST;
            else
                throw ExceptionsSL.ex("Unmapped ImageButton: " + button);
        return null;
    }
    
    private ImageButton getButtonFromOrientationId(@Nullable TileTerrainObjectOrientationID orientationID) {
        if (orientationID != null)
            return switch (orientationID) {
                case BORDERLESS -> borderlessImageButton;
                
                case NORTH -> nImageButton;
                case SOUTH -> sImageButton;
                case EAST -> eImageButton;
                case WEST -> wImageButton;
                
                case NORTH_EAST -> neImageButton;
                case NORTH_WEST -> nwImageButton;
                case SOUTH_EAST -> seImageButton;
                case SOUTH_WEST -> swImageButton;
                
                default -> throw ExceptionsSL.ex("Unmapped Orientation ID: " + orientationID);
            };
        return null;
    }
}
