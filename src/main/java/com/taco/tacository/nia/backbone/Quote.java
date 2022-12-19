package com.taco.tacository.nia.backbone;


import com.taco.tacository.util.tools.Obj;
import com.taco.tacository.util.tools.Props;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Quote {
    
    private final ObjectProperty<Interview> ownerProperty;
    private final ObjectProperty<Speaker> speakerProperty;
    
    public Quote(@Nullable Interview owner, @Nullable Speaker speaker) {
        this.ownerProperty = new SimpleObjectProperty<>(owner); this.speakerProperty = new SimpleObjectProperty<>(speaker);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ObjectProperty<Interview> ownerProperty() { return ownerProperty; }
    public final @Nullable Interview getOwner() { return ownerProperty.get(); }
    public final @Nullable Interview setOwner(@NotNull Interview owner) { return Props.setProperty(ownerProperty, owner); }
    
    public final @NotNull ObjectProperty<Speaker> speakerProperty() { return speakerProperty; }
    public final @Nullable Speaker getSpeaker() { return speakerProperty.get(); }
    public final @Nullable Speaker setSpeaker(@NotNull Speaker speaker) { return Props.setProperty(speakerProperty, speaker); }
    
    //</editor-fold>
}
