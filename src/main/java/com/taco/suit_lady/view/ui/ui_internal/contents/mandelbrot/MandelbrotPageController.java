package com.taco.suit_lady.view.ui.ui_internal.contents.mandelbrot;

import com.taco.suit_lady.view.ui.UIPageController;
import com.taco.suit_lady.view.ui.jfx.components.DecimalFormatter;
import com.taco.suit_lady.view.ui.jfx.components.DoubleField2;
import com.taco.suit_lady.view.ui.jfx.components.IntField;
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
    
    @FXML private DoubleField2 xMinTextField;
    @FXML private DoubleField2 yMinTextField;
    @FXML private DoubleField2 xMaxTextField;
    @FXML private DoubleField2 yMaxTextField;
    
    @FXML private ChoiceBox<MandelbrotColorScheme> colorSchemeChoiceBox;
    
    
    @FXML private Label widthLabel;
    @FXML private Label heightLabel;
    @FXML private Label canvasWidthLabel;
    @FXML private Label canvasHeightLabel;
    
    //
    
    @FXML private IntField intFieldTest;
    @FXML private Label xMinLabel;
    @FXML private Label intFieldTestLabel;
    
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
    
    protected Label getCanvasWidthLabel() {
        return canvasWidthLabel;
    }
    
    protected Label getCanvasHeightLabel() {
        return canvasHeightLabel;
    }
    
    protected IntField getIntFieldTest() {
        return intFieldTest;
    }
    
    protected Label getXMinLabel() {
        return xMinLabel;
    }
    
    protected Label getIntFieldTestLabel() {
        return intFieldTestLabel;
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
