package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotColorScheme;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.components.button.ImageButton;
import com.taco.suit_lady.ui.jfx.components.formatted_controls.DoubleField;
import com.taco.suit_lady.ui.jfx.components.formatted_controls.IntField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@FxmlView("/fxml/game_view/pages/game_view_page.fxml")
@Scope("prototype")
public class GameViewPageController extends UIPageController<GameViewPage> {
    
    @FXML private AnchorPane root;
    
    //
    
    @FXML private ProgressBar progressBar;
    
    @FXML private Button regenerateButton;
    @FXML private ImagePane autoRegenerateImagePane;
    
    @FXML private IntField precisionTextField;
    
    @FXML private ChoiceBox<MandelbrotColorScheme> colorSchemeChoiceBox;
    @FXML private ImagePane invertColorSchemeImagePane;
    private ImageButton invertColorSchemeImageButton;
    
    //
    
    @FXML private DoubleField xMinTextField;
    @FXML private DoubleField yMinTextField;
    @FXML private DoubleField xMaxTextField;
    @FXML private DoubleField yMaxTextField;
    
    
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
    
    protected GameViewPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
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
    
    
    protected IntField getPrecisionTextField() {
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
    
    protected DoubleField getXMinTextField() {
        return xMinTextField;
    }
    
    protected DoubleField getYMinTextField() {
        return yMinTextField;
    }
    
    protected DoubleField getXMaxTextField() {
        return xMaxTextField;
    }
    
    protected DoubleField getYMaxTextField() {
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
