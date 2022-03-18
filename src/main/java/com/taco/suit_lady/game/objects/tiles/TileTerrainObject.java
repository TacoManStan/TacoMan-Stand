package com.taco.suit_lady.game.objects.tiles;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.Stuff;
import com.taco.tacository.json.*;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class TileTerrainObject
        implements SpringableWrapper, Serializable, GameComponent, UIDProcessable, JObject, JLoadable {
    
    private final GameTileModel owner;
    
    
    private final ObjectProperty<TileTerrainObjectID> idProperty;
    private final ObjectProperty<TileTerrainObjectOrientationID> orientationIdProperty;
    
    private final StringBinding aggregateTextureIdBinding;
    private final ObjectBinding<Image> imageBinding;
    
    public TileTerrainObject(@NotNull GameTileModel owner) {
        this.owner = owner;
        
        this.idProperty = new SimpleObjectProperty<>(TileTerrainObjectID.defaultInstance());
        this.orientationIdProperty = new SimpleObjectProperty<>(TileTerrainObjectOrientationID.defaultInstance());
        
        this.aggregateTextureIdBinding = Bind.stringBinding(() -> getId().value() + getOrientationId().value(), idProperty, orientationIdProperty);
        this.imageBinding = Bind.objBinding(() -> Stuff.getGameImage("tiles/", getAggregateTextureId()), aggregateTextureIdBinding);
        
        //
        
        aggregateTextureIdBinding.addListener((observable, oldValue, newValue) -> getOwner().refresh());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull GameTileModel getOwner() { return owner; }
    
    
    public final @NotNull ObjectProperty<TileTerrainObjectID> idProperty() { return idProperty; }
    public final @NotNull TileTerrainObjectID getId() { return idProperty.get(); }
    public final @NotNull TileTerrainObjectID setId(@NotNull TileTerrainObjectID newValue) { return Props.setProperty(idProperty, newValue); }
    
    public final @NotNull ObjectProperty<TileTerrainObjectOrientationID> orientationIdProperty() { return orientationIdProperty; }
    public final @NotNull TileTerrainObjectOrientationID getOrientationId() { return orientationIdProperty.get(); }
    public final @NotNull TileTerrainObjectOrientationID setOrientationId(@NotNull TileTerrainObjectOrientationID newValue) { return Props.setProperty(orientationIdProperty, newValue); }
    
    
    public final @NotNull StringBinding aggregateTextureIdBinding() { return aggregateTextureIdBinding; }
    public final @NotNull String getAggregateTextureId() { return aggregateTextureIdBinding.get(); }
    
    public final @NotNull ObjectBinding<Image> imageBinding() { return imageBinding; }
    public final @NotNull Image getImage() { return imageBinding.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    //
    
    @Override public @NotNull Springable springable() { return owner; }
    
    
    @Override public String getJID() { return "tile-terrain-obj"; }
    
    @Override public void load(JsonObject parent) {
        setId(TileTerrainObjectID.valueOf(JUtil.loadString(parent, TileTerrainObjectID.jsonId())));
        setOrientationId(TileTerrainObjectOrientationID.valueOf(JUtil.loadString(parent, TileTerrainObjectOrientationID.jsonId())));
    }
    
    @Override public JElement[] jFields() {
        return new JElement[]{
                JUtil.create(TileTerrainObjectID.jsonId(), getId().name()),
                JUtil.create(TileTerrainObjectOrientationID.jsonId(), getOrientationId().name())
        };
    }
    
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("tile-terrain-obj");
        return uidProcessor;
    }
    
    //</editor-fold>
}
