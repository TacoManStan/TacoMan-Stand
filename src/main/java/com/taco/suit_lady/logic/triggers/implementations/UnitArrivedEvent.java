package com.taco.suit_lady.logic.triggers.implementations;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.triggers.TriggerEvent;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

public class UnitArrivedEvent extends TriggerEvent<UnitArrivedEvent> {
    
    private final GameObject source;
    
    private final Point2D movedFrom;
    private final Point2D movedTo;
    
    private final String type;
    
    public UnitArrivedEvent(@NotNull GameObject source, @NotNull Point2D movedFrom, @NotNull Point2D movedTo, @NotNull String type) {
        super(source);
        this.source = source;
        
        this.movedFrom = movedFrom;
        this.movedTo = movedTo;
        
        this.type = type;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Point2D getMovedFrom() { return movedFrom; }
    public final Point2D getMovedTo() { return movedTo; }
    
    public final String getType() { return type; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public GameObject getSource() { return (GameObject) super.getSource(); }
    
    //</editor-fold>
}
