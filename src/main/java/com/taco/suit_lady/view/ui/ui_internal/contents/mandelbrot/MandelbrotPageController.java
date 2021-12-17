package com.taco.suit_lady.view.ui.ui_internal.contents.mandelbrot;

import com.taco.suit_lady.view.ui.UIPageController;
import com.taco.suit_lady.view.ui.jfx.components.DoubleField2;
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
    @FXML private CheckBox autoRegenerateCheckBox;
    
    @FXML private DoubleField2 xMinTextField;
    @FXML private DoubleField2 yMinTextField;
    @FXML private DoubleField2 xMaxTextField;
    @FXML private DoubleField2 yMaxTextField;
    
    @FXML private ChoiceBox<MandelbrotColorScheme> colorSchemeChoiceBox;
    
    
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
    
    protected CheckBox getAutoRegenerateCheckBox() {
        return autoRegenerateCheckBox;
    }
    
    protected ChoiceBox<MandelbrotColorScheme> getColorSchemeChoiceBox() {
        return colorSchemeChoiceBox;
    }
    
    
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
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
        Arrays.stream(MandelbrotColorScheme.values()).forEach(
                colorScheme -> colorSchemeChoiceBox.getItems().add(colorScheme));
        
        progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) {
                if (newValue)
                    regenerateButton.setId("progress-button");
                else
                    regenerateButton.setId(null);
                regenerateButton.applyCss();
            }
        });
        
        //        xMinTextField.setTextFormatter(new DecimalFormatter());
        //        yMinTextField.setTextFormatter(new DecimalFormatter());
        //        xMaxTextField.setTextFormatter(new DecimalFormatter());
        //        yMaxTextField.setTextFormatter(new DecimalFormatter());
        
        //        intFieldTest.valueProperty().addListener((observable, oldValue, newValue) -> {
        //            intFieldTestLabel.setText("" + intFieldTest.getValue());
        //        });
    }
    
    //</editor-fold>
}
