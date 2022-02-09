package com.taco.suit_lady.game.objects.tiles;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.GameMap;
import com.taco.suit_lady.game.GameMapModel;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.ArraysSL;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import com.taco.tacository.json.JElement;
import com.taco.tacository.json.JLoadable;
import com.taco.tacository.json.JObject;
import com.taco.tacository.json.JUtil;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class TileModel
        implements SpringableWrapper, Lockable, GameComponent, JObject, JLoadable {
    
    private final GameTile owner;
    
    private final ReadOnlyIntegerWrapper manualRefreshProperty;
    
    
    private final ReadOnlyStringWrapper imageIdProperty;
    private final ListProperty<TileTerrainObject> terrainTileObjects;
    
    
    private final BooleanProperty showBorderProperty;
    
    private final ObjectBinding<Image> imageBinding;
    private final ObjectBinding<Image> borderlessImageBinding;
    private final ObjectBinding<Image> textureOnlyImageBinding;
    
    public TileModel(@NotNull GameTile owner) {
        this.owner = owner;
        
        this.manualRefreshProperty = new ReadOnlyIntegerWrapper(0);
        
        
        this.imageIdProperty = new ReadOnlyStringWrapper("grass");
        this.terrainTileObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.showBorderProperty = new SimpleBooleanProperty(false);
        
        this.imageBinding = BindingsSL.objBinding(
                () -> ToolsFX.generateCompositeImage(getGameMap().getTileSize(), getGameMap().getTileSize(), getImageList(ImageListType.ALL).toArray(new Image[0])),
                imageIdProperty, terrainTileObjects, showBorderProperty, manualRefreshProperty);
        this.borderlessImageBinding = BindingsSL.objBinding(
                () -> ToolsFX.generateCompositeImage(getGameMap().getTileSize(), getGameMap().getTileSize(), getImageList(ImageListType.BORDERLESS).toArray(new Image[0])),
                imageIdProperty, terrainTileObjects, showBorderProperty, manualRefreshProperty);
        this.textureOnlyImageBinding = BindingsSL.objBinding(
                () -> ToolsFX.generateCompositeImage(getGameMap().getTileSize(), getGameMap().getTileSize(), getImageList(ImageListType.TEXTURE_ONLY).toArray(new Image[0])),
                imageIdProperty, terrainTileObjects, showBorderProperty, manualRefreshProperty);
        
        this.imageBinding.addListener((observable, oldValue, newValue) -> {
            final GameMap gameMap = getGameMap();
            if (gameMap != null) {
                final GameMapModel gameMapModel = gameMap.getModel();
                if (gameMapModel != null)
                    gameMapModel.refreshMapImage();
            }
        });
    }
    
    private @NotNull List<Image> getImageList(@NotNull ImageListType listType) {
        ArrayList<Image> resultList = new ArrayList<>();
        resultList.add(ResourcesSL.getGameImage("tiles/", getImageId()));
        if (listType.equals(ImageListType.BORDERLESS) || listType.equals(ImageListType.ALL))
            for (TileTerrainObject terrainObj: terrainTileObjects)
                resultList.add(ResourcesSL.getGameImage("tiles/", terrainObj.getAggregateTextureId()));
        if (listType.equals(ImageListType.ALL) && isBorderShowing())
            resultList.add(ToolsFX.generateSelectionBorder(getGameMap().getTileSize(), getGameMap().getTileSize(), 2, null));
        return resultList;
    }
    
    private enum ImageListType {
        ALL, BORDERLESS, TEXTURE_ONLY
    }
    
    
    public final void refresh() { sync(() -> manualRefreshProperty.set(manualRefreshProperty.get() + 1)); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameTile getOwner() { return owner; }
    
    
    public final ReadOnlyStringWrapper imageIdProperty() { return imageIdProperty; }
    public final ReadOnlyStringProperty readOnlyImageIdProperty() { return imageIdProperty.getReadOnlyProperty(); }
    public final String getImageId() { return imageIdProperty.get(); }
    public final String setImageId(@NotNull String newValue) { return PropertiesSL.setProperty(imageIdProperty, newValue); }
    
    public final ListProperty<TileTerrainObject> terrainTileObjects() { return terrainTileObjects; }
    
    
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
        terrainTileObjects.addAll(ArraysSL.removeNull(new ArrayList<>(JUtil.loadArray(parent, "tile-objs", o -> {
            if (o instanceof JsonObject jo) {
                final TileTerrainObject terrainObj = new TileTerrainObject(this);
                terrainObj.load(jo);
                return terrainObj;
            } else if (o instanceof String so)
                System.out.println("Attempting to load legacy save file... skipping " + so + " load process");
            else
                System.err.println("Attempting to load unknown JsonArray element: " + o);
            return null;
        }))));
    }
    
    @Override public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("image-id", getImageId()),
                JUtil.createArray("tile-objs", terrainTileObjects.toArray(new TileTerrainObject[0]))
        };
    }
    
    //</editor-fold>
}
