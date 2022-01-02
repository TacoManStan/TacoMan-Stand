package com.taco.suit_lady.ui.contents.mandelbrot;

import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.ui.jfx.button.ImageButton;
import com.taco.suit_lady.ui.jfx.components.DoubleField2;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.components.IntField2;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@FxmlView("/fxml/sidebar/pages/mandelbrot/mandelbrot_page.fxml")
@Scope("prototype")
public class MandelbrotPageController extends UIPageController<MandelbrotPage> {
    
    @FXML private AnchorPane root;
    
    //
    
    @FXML private ProgressBar progressBar;
    
    @FXML private Button regenerateButton;
    @FXML private ImagePane autoRegenerateImagePane;
    
    @FXML private IntField2 precisionTextField;
    
    @FXML private ChoiceBox<MandelbrotColorScheme> colorSchemeChoiceBox;
    @FXML private ImagePane invertColorSchemeImagePane;
    private ImageButton invertColorSchemeImageButton;
    
    //
    
    @FXML private DoubleField2 xMinTextField;
    @FXML private DoubleField2 yMinTextField;
    @FXML private DoubleField2 xMaxTextField;
    @FXML private DoubleField2 yMaxTextField;
    
    
    @FXML private Label widthLabel;
    @FXML private Label heightLabel;
    @FXML private Label widthScaledLabel;
    @FXML private Label heightScaledLabel;
    
    @FXML private Label xMinScaledLabel;
    @FXML private Label yMinScaledLabel;
    @FXML private Label xMaxScaledLabel;
    @FXML private Label yMaxScaledLabel;
    
    @FXML private Label canvasWidthLabel;
    @FXML private Label canvasHeightLabel;
    
    @FXML private Button saveConfigButton;
    @FXML private Button loadConfigButton;
    
    protected MandelbrotPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected ProgressBar getProgressBar() {
        return progressBar;
    }
    
    
    protected Button getRegenerateButton() {
        return regenerateButton;
    }
    
    protected ImagePane getAutoRegenerateImagePane() {
        return autoRegenerateImagePane;
    }
    
    
    protected IntField2 getPrecisionTextField() {
        return precisionTextField;
    }
    
    
    protected ChoiceBox<MandelbrotColorScheme> getColorSchemeChoiceBox() {
        return colorSchemeChoiceBox;
    }
    
    protected ImagePane getInvertColorSchemeImagePane() {
        return invertColorSchemeImagePane;
    }
    
    protected ImageButton getInvertColorSchemeImageButton() {
        return invertColorSchemeImageButton;
    }
    
    // MIN/MAX Input Fields
    
    protected DoubleField2 getXMinTextField() {
        return xMinTextField;
    }
    
    protected DoubleField2 getYMinTextField() {
        return yMinTextField;
    }
    
    protected DoubleField2 getXMaxTextField() {
        return xMaxTextField;
    }
    
    protected DoubleField2 getYMaxTextField() {
        return yMaxTextField;
    }
    
    
    protected Button getSaveConfigButton() {
        return saveConfigButton;
    }
    
    protected Button getLoadConfigButton() {
        return loadConfigButton;
    }
    
    //<editor-fold desc="--- INFORMATION LABELS ---">
    
    protected Label getWidthLabel() {
        return widthLabel;
    }
    
    protected Label getHeightLabel() {
        return heightLabel;
    }
    
    protected Label getWidthScaledLabel() {
        return widthScaledLabel;
    }
    
    protected Label getHeightScaledLabel() {
        return heightScaledLabel;
    }
    
    
    protected Label getXMinScaledLabel() {
        return xMinScaledLabel;
    }
    
    protected Label getYMinScaledLabel() {
        return yMinScaledLabel;
    }
    
    protected Label getXMaxScaledLabel() {
        return xMaxScaledLabel;
    }
    
    protected Label getYMaxScaledLabel() {
        return yMaxScaledLabel;
    }
    
    
    protected Label getCanvasWidthLabel() {
        return canvasWidthLabel;
    }
    
    protected Label getCanvasHeightLabel() {
        return canvasHeightLabel;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMAGE BUTTONS ---">
    
    //    private ImageButton invertColorSchemeImageButton;
    private ImageButton autoRegenerateImageButton;
    
    //    protected final ImageButton getInvertColorSchemeImageButton() {
    //        return invertColorSchemeImageButton;
    //    }
    
    protected final ImageButton getPauseAutoRegenerationImageButton() {
        return autoRegenerateImageButton;
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
        progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) {
                if (newValue)
                    regenerateButton.setId("progress-button");
                else
                    regenerateButton.setId(null);
                regenerateButton.applyCss();
            }
        });
        
        Arrays.stream(MandelbrotColorScheme.values()).forEach(colorScheme -> colorSchemeChoiceBox.getItems().add(colorScheme));
        autoRegenerateImageButton = new ImageButton(
                this,
                "Toggle Auto-Regeneration",
                "pause",
                autoRegenerateImagePane,
                null,
                null,
                true,
                ImageButton.SMALL
        ).init();
        
        invertColorSchemeImageButton = new ImageButton(
                this,
                "Invert Color Scheme",
                "rerun",
                invertColorSchemeImagePane,
                null,
                null,
                true,
                ImageButton.SMALL
        ).init();
    }
    
    //</editor-fold>
}
