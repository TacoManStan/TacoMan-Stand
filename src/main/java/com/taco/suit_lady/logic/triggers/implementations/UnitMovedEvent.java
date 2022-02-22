package com.taco.suit_lady.logic.triggers.implementations;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.triggers.TriggerEvent;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

public class UnitMovedEvent extends TriggerEvent<UnitMovedEvent> {
    
    private final GameObject source;
    
    private final Point2D movedFrom;
    private final Point2D movedTo;
    
    public UnitMovedEvent(@NotNull GameObject source, @NotNull Point2D movedFrom, @NotNull Point2D movedTo) {
        super(source);
        this.source = source;
        
        this.movedFrom = movedFrom;
        this.movedTo = movedTo;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Point2D getMovedFrom() { return movedFrom; }
    public final Point2D getMovedTo() { return movedTo; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public GameObject getSource() { return (GameObject) super.getSource(); }
    
    //</editor-fold>
}
