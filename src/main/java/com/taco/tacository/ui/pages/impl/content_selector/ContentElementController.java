package com.taco.tacository.ui.pages.impl.content_selector;

import com.taco.tacository._to_sort._new.debugger.Debugger;
import com.taco.tacository.ui.ContentData;
import com.taco.tacository.ui.jfx.components.button.ImageButton;
import com.taco.tacository.ui.jfx.components.ImagePane;
import com.taco.tacository.ui.ui_internal.controllers.CellController;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class ContentElementController<
        D extends ContentData<T, D, ?, ?, ?>,
        P extends ContentSelectorPage<D, P, SC, EC, H, T>,
        SC extends ContentSelectorPageController<D, P, SC, EC, H, T>,
        EC extends ContentElementController<D, P, SC, EC, H, T>,
        H extends ContentHandler<D, P, SC, EC, H, T>,
        T extends ListableContent<D, ?, P, SC, EC, H, T, ?, ?>>
        extends CellController<T> {
    
    @FXML private AnchorPane root;
    
    @FXML private ImagePane closeImagePane;
    @FXML private ImagePane iconImagePane;
    
    private ImageButton closeButton;
    
    public ContentElementController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected void onContentChange(T oldCellContents, T newCellContents) {
        // TODO: Ensure empty content element controllers don't prevent old cell contents from being garbage collected
        if (newCellContents != null) {
            iconImagePane.imageProperty().bind(newCellContents.iconImageProperty());
            newCellContents.setElementController((EC) this);
        }
        if (oldCellContents != null) {
            iconImagePane.imageProperty().unbind();
            oldCellContents.setElementController(null);
        }
//        if (newCellContents != null) {
//            playButton.disabledProperty().bind(newCellContents.runningProperty());
//            stopButton.disabledProperty().bind(Bindings.not(newCellContents.runningProperty()));
//        } else {
//            playButton.disabledProperty().unbind();
//            playButton.setDisabled(true);
//            stopButton.disabledProperty().unbind();
//            stopButton.setDisabled(false);
//        }
    }
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
//        this.iconButton = new ImageButton(this, iconImagePane, "logo", this::icon, null, false, new Point2D(50, 50)).initialize();
        this.closeButton = new ImageButton(
                this,
                "Shutdown Instance",
                "close",
                closeImagePane,
                this::close,
                null,
                false,
                new Point2D(15, 15)).init();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- HELPER METHODS ---">
    
    private void icon() {
        debugger().print(Debugger.DEBUG, "Logo Pressed");
    }
    
    private void close() {
        debugger().print(Debugger.DEBUG, "Close Pressed");
        getContents().shutdown();
    }
    
    //</editor-fold>
}
