package com.taco.suit_lady.logic.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.GameMapModel;
import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.AggregateImagePaintCommand;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.ArraysSL;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import com.taco.suit_lady.util.tools.list_tools.ListsSL;
import com.taco.suit_lady.util.tools.list_tools.Operation;
import com.taco.tacository.json.JElement;
import com.taco.tacository.json.JLoadable;
import com.taco.tacository.json.JObject;
import com.taco.tacository.json.JUtil;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameTileModel
        implements SpringableWrapper, Lockable, GameComponent, JObject, JLoadable {
    
    private final GameTile owner;
    
    
    private final ReadOnlyStringWrapper imageIdProperty;
    private final ListProperty<String> terrainTileObjects;
    
    private final BooleanProperty showBorderProperty;
    private final ObjectBinding<Image> imageBinding;
    private final ObjectBinding<Image> borderlessImageBinding;
    private final ObjectBinding<Image> textureOnlyImageBinding;
    
    public GameTileModel(@NotNull GameTile owner) {
        this.owner = owner;
        
        
        this.imageIdProperty = new ReadOnlyStringWrapper("grass");
        this.terrainTileObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.showBorderProperty = new SimpleBooleanProperty(false);
        
        this.imageBinding = BindingsSL.objBinding(
                () -> ToolsFX.generateCompositeImage(getGameMap().getTileSize(), getGameMap().getTileSize(), getImageList(ImageListType.ALL).toArray(new Image[0])),
                imageIdProperty, terrainTileObjects, showBorderProperty);
        this.borderlessImageBinding = BindingsSL.objBinding(
                () -> ToolsFX.generateCompositeImage(getGameMap().getTileSize(), getGameMap().getTileSize(), getImageList(ImageListType.BORDERLESS).toArray(new Image[0])),
                imageIdProperty, terrainTileObjects, showBorderProperty);
        this.textureOnlyImageBinding = BindingsSL.objBinding(
                () -> ToolsFX.generateCompositeImage(getGameMap().getTileSize(), getGameMap().getTileSize(), getImageList(ImageListType.TEXTURE_ONLY).toArray(new Image[0])),
                imageIdProperty, terrainTileObjects, showBorderProperty);
        
        this.imageBinding.addListener((observable, oldValue, newValue) -> {
            final GameMap gameMap = getGameMap();
            if (gameMap != null) {
                final GameMapModel gameMapModel = gameMap.getModel();
                if (gameMapModel != null)
                    gameMapModel.refreshMapImage();
            }
        });
    }
    
    private List<Image> getImageList(@NotNull ImageListType listType) {
        ArrayList<Image> resultList = new ArrayList<>();
        resultList.add(ResourcesSL.getGameImage("tiles/", getImageId()));
        if (listType.equals(ImageListType.BORDERLESS) || listType.equals(ImageListType.ALL))
            for (String imgId: terrainTileObjects)
                resultList.add(ResourcesSL.getGameImage("tiles/", imgId));
        if (listType.equals(ImageListType.ALL) && isBorderShowing())
            resultList.add(ToolsFX.generateSelectionBorder(getGameMap().getTileSize(), getGameMap().getTileSize(), 2, null));
        return resultList;
    }
    
    private enum ImageListType {
        ALL, BORDERLESS, TEXTURE_ONLY
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameTile getOwner() { return owner; }
    
    
    public final ReadOnlyStringWrapper imageIdProperty() { return imageIdProperty; }
    public final ReadOnlyStringProperty readOnlyImageIdProperty() { return imageIdProperty.getReadOnlyProperty(); }
    public final String getImageId() { return imageIdProperty.get(); }
    public final String setImageId(@NotNull String newValue) { return PropertiesSL.setProperty(imageIdProperty, newValue); }
    
    public final ListProperty<String> terrainTileObjects() { return terrainTileObjects; }
    
    
    public final BooleanProperty showBorderProperty() { return showBorderProperty; }
    public final boolean isBorderShowing() { return showBorderProperty.get(); }
    public final boolean setBorderShowing(boolean newValue) { return PropertiesSL.setProperty(showBorderProperty, newValue); }
    
    
    public final ObjectBinding<Image> imageBinding() { return imageBinding; }
    public final Image getImage() { return imageBinding.get(); }
    
    public final ObjectBinding<Image> borderlessImageBinding() { return borderlessImageBinding; }
    public final Image getBorderlessImage() { return borderlessImageBinding.get(); }
    
    public final ObjectBinding<Image> textureOnlyImageBinding() { return textureOnlyImageBinding; }
    public final Image getTextureOnlyImage() { return textureOnlyImageBinding.get(); }
    
    //    public final AggregateImagePaintCommand getTerrainObjPaintCommand() { return terrainObjPaintCommand; }
    
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
                return os;
            return null;
        }));
        terrainTileObjects.addAll(ArraysSL.removeNull(tempTerrainObjs));
    }
    
    @Override public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("image-id", getImageId()),
                JUtil.createArray("tile-objs", terrainTileObjects.toArray(new String[0]))
        };
    }
    
    //</editor-fold>
}
