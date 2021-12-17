package com.taco.suit_lady.view.ui.ui_internal.contents.mandelbrot;

import com.taco.suit_lady.view.ui.UIPageController;
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
    
    @FXML private ProgressBar progressBar;
    @FXML private Button regenerateButton;
    
    @FXML private TextField xLocTextField;
    @FXML private TextField yLocTextField;
    @FXML private TextField widthTextField;
    @FXML private TextField heightTextField;
    @FXML private ChoiceBox<MandelbrotColorScheme> colorSchemeChoiceBox;
    
    @FXML private Label widthLabel;
    @FXML private Label heightLabel;
    @FXML private Label canvasWidthLabel;
    @FXML private Label canvasHeightLabel;
    
    protected MandelbrotPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- FXML FIELD ACCESSORS ---">
    
    protected ProgressBar getProgressBar() {
        return progressBar;
    }
    
    protected Button getRegenerateButton() {
        return regenerateButton;
    }
    
    protected TextField getXLocTextField() {
        return xLocTextField;
    }
    
    protected TextField getYLocTextField() {
        return yLocTextField;
    }
    
    protected TextField getWidthTextField() {
        return widthTextField;
    }
    
    protected TextField getHeightTextField() {
        return heightTextField;
    }
    
    protected Label getWidthLabel() {
        return widthLabel;
    }
    
    protected Label getHeightLabel() {
        return heightLabel;
    }
    
    protected Label getCanvasWidthLabel() {
        return canvasWidthLabel;
    }
    
    protected Label getCanvasHeightLabel() {
        return canvasHeightLabel;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
        Arrays.stream(MandelbrotColorScheme.values()).forEach(
                colorScheme -> colorSchemeChoiceBox.getItems().add(colorScheme));
    }
    
    //</editor-fold>
}
