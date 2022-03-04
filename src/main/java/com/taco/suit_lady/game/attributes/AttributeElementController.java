package com.taco.suit_lady.game.attributes;

import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/game/pages/attribute_element.fxml")
@Scope("prototype")
public class AttributeElementController extends CellController<Attribute<?>> {
    
    @FXML private AnchorPane root;
    
    @FXML private Label attributeNameLabel;
    @FXML private AnchorPane valueAnchorPane;
    
    private final ReadOnlyObjectWrapper<Region> valuePaneProperty;
    
    public AttributeElementController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.valuePaneProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public void initialize() {
        super.initialize();
        
        valuePaneProperty.addListener((observable, oldValue, newValue) -> FX.requireFX(() -> {
            if (oldValue != null)
                valueAnchorPane.getChildren().remove(oldValue);
            if (newValue != null) {
                FX.setAnchors(newValue);
                valueAnchorPane.getChildren().add(newValue);
            }
        }));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<Region> readOnlyValuePaneProperty() { return valuePaneProperty.getReadOnlyProperty(); }
    public final Region getValuePane() { return valuePaneProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public Pane root() { return root; }
    
    @Override protected void onContentChange(Attribute<?> oldCellContents, Attribute<?> newCellContents) {
        if (oldCellContents != null) {
            if (newCellContents == null) {
                valuePaneProperty.set(null);
                attributeNameLabel.textProperty().unbind();
            }
            oldCellContents.getModel().pauseBindings();
        }
        
        if (newCellContents != null) {
            attributeNameLabel.textProperty().bind(Bind.stringBinding(() -> newCellContents.getId() + ": ", newCellContents.readOnlyIdProperty()));
            valuePaneProperty.set(newCellContents.getModel().refreshBindings());
        }
    }
    
    //</editor-fold>
}
