package com.taco.suit_lady.view._nonimported._expr.tfx_port;

import com.taco.suit_lady.data._expr.mongodb.test_country.Country;
import com.taco.suit_lady.uncategorized.spring.util.ApplicationContextAwareSL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView("MainViewSL.fxml")
public class FXMainControllerSL
        implements ApplicationContextAwareSL
{
    //<editor-fold desc="-- FXML Fields --">
    
    @FXML private StackPane root;
    
    @FXML private HBox windowBar;
    @FXML private StackPane dragBar;
    
    //
    
    @FXML private GridPane gridPane;
    
    @FXML private StackPane contentStackPaneRoot;
    @FXML private BorderPane contentLayoutPane;
    @FXML private StackPane mainContentPane;
    
    @FXML private TreeView<Country> consoleTree;
    
    //
    
    @FXML private TextField globalSearchField;
    
    //
    
    @FXML private BorderPane sidebarPane;
    
    @FXML private AnchorPane sidebarPaneAnchor;
    
    @FXML private Button generalSidebarButton;
    @FXML private Button inDevelopmentSidebarButton;
    @FXML private Button nyiSidebarButton;
    
    @FXML private StackPane sidebarChildButtonsPane;
    @FXML private StackPane sidebarContentPane;
    
    //
    
    @FXML private CheckBox consoleTRiBotCheckBox;
    @FXML private CheckBox consoleClientCheckBox;
    @FXML private CheckBox consoleScriptCheckBox;
    @FXML private CheckBox consoleSelectedInstanceOnlyCheckBox;
    
    //</editor-fold>
    
    //<editor-fold desc="Abstract Overrides">
    
    @FXML public void initialize()
    {
    
    }
    
    //
    
    //<editor-fold desc="-- INTERNAL --">
    
    private ApplicationContext ctx;
    
    @Override public void setApplicationContext(ApplicationContext ctx)
    {
        this.ctx = ctx;
    }
    
    @Override public ApplicationContext ctx()
    {
        return ctx;
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
