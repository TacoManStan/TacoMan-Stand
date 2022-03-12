package com.taco.suit_lady.logic.triggers.implementations;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.triggers.TriggerEvent;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

public class UnitArrivedEvent extends TriggerEvent<UnitArrivedEvent> {
    
    private final GameObject source;
    
    private final Num2D movedFrom;
    private final Num2D movedTo;
    
    private final String type;
    
    public UnitArrivedEvent(@NotNull GameObject source, @NotNull NumExpr2D<?> movedFrom, @NotNull NumExpr2D<?> movedTo, @NotNull String type) {
        super(source);
        this.source = source;
        
        this.movedFrom = movedFrom.asNum2D();
        this.movedTo = movedTo.asNum2D();
        
        this.type = type;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Num2D getMovedFrom() { return movedFrom; }
    public final Num2D getMovedTo() { return movedTo; }
    
    public final String getType() { return type; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public GameObject getSource() { return (GameObject) super.getSource(); }
    
    //</editor-fold>
}
