package com.taco.suit_lady.logic.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.GameMapModel;
import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.ArraysSL;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.tacository.json.JElement;
import com.taco.tacository.json.JLoadable;
import com.taco.tacository.json.JObject;
import com.taco.tacository.json.JUtil;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class GameTileModel
        implements SpringableWrapper, Lockable, GameComponent, JObject, JLoadable {
    
    private final GameTile owner;
    
    
    private final ReadOnlyStringWrapper imageIdProperty;
    private final ListProperty<String> terrainTileObjects;
    
    private final ObjectBinding<Image> imageBinding;
    
    public GameTileModel(@NotNull GameTile owner) {
        this.owner = owner;
        
        
        this.imageIdProperty = new ReadOnlyStringWrapper("grass");
        this.terrainTileObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.imageBinding = BindingsSL.objBinding(() -> {
            final int tileSize = getGameMap().getTileSize();
            final Image baseImage = ResourcesSL.getGameImage("tiles/", getImageId());
            final WritableImage aggregateImage = new WritableImage(tileSize, tileSize);
            
            aggregateImage.getPixelWriter().setPixels(0, 0, tileSize, tileSize, baseImage.getPixelReader(), 0, 0);
            for (String s: terrainTileObjects)
                aggregateImage.getPixelWriter().setPixels(0, 0, tileSize, tileSize, ResourcesSL.getGameImage("tiles/", s).getPixelReader(), 0, 0);
            
            return aggregateImage;
        }, imageIdProperty, terrainTileObjects);
        
        this.imageBinding.addListener((observable, oldValue, newValue) -> {
            final GameMap gameMap = getGameMap();
            if (gameMap != null) {
                final GameMapModel gameMapModel = gameMap.getModel();
                if (gameMapModel != null)
                    gameMapModel.refreshMapImage();
            }
        });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameTile getOwner() { return owner; }
    
    
    public final ReadOnlyStringWrapper imageIdProperty() { return imageIdProperty; }
    public final ReadOnlyStringProperty readOnlyImageIdProperty() { return imageIdProperty.getReadOnlyProperty(); }
    public final String getImageId() { return imageIdProperty.get(); }
    public final String setImageId(@NotNull String newValue) { return PropertiesSL.setProperty(imageIdProperty, newValue); }
    
    public final ListProperty<String> terrainTileObjects() { return terrainTileObjects; }
    
    public final ObjectBinding<Image> imageBinding() { return imageBinding; }
    public final Image getImage() { return imageBinding.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @NotNull Lock getLock() { return getOwner().getLock(); }
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    //
    
    @Override public String getJID() { return "game-tile-model"; }
    
    @Override public void load(JsonObject parent) {
        setImageId(JUtil.loadString(parent, "image-id"));
        final ArrayList<String> tempTerrainObjs = new ArrayList<>(JUtil.loadArray(parent, "tile-objs", o -> {
            if (o instanceof String os)
                return (String) o;
            return null;
        }));
        terrainTileObjects.addAll(ArraysSL.removeNull(tempTerrainObjs));
    }
    
    @Override public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("image-id", getImageId()),
                JUtil.createArray("tile-objs", terrainTileObjects)
        };
    }
    
    //</editor-fold>
}
