package com.taco.suit_lady.logic.triggers.implementations;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.triggers.TriggerEvent;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.NumExpr2D;
import org.jetbrains.annotations.NotNull;

public class UnitMovedEvent extends TriggerEvent<UnitMovedEvent> {
    
    private final GameObject source;
    
    private final Num2D movedFrom;
    private final Num2D movedTo;
    
    public UnitMovedEvent(@NotNull GameObject source, @NotNull NumExpr2D<?> movedFrom, @NotNull NumExpr2D<?> movedTo) {
        super(source);
        this.source = source;
        
        this.movedFrom = movedFrom.asNum2D();
        this.movedTo = movedTo.asNum2D();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Num2D getMovedFrom() { return movedFrom; }
    public final Num2D getMovedTo() { return movedTo; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public GameObject getSource() { return (GameObject) super.getSource(); }
    
    //</editor-fold>
}
