package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.ui.FooterController;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.components.button.ImageButton;
import com.taco.suit_lady.ui.jfx.util.Dimensions;
import com.taco.suit_lady.util.tools.ArraysSL;
import com.taco.suit_lady.util.tools.Print;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/game/content/game_view_footer.fxml")
@Scope("prototype")
public class GameFooterController
        extends FooterController<GameFooter, GameFooterController, GameViewContent, GameViewContentData, GameViewContentController> {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    
    @FXML private ImagePane selectionPreviewImagePane;
    
    //<editor-fold desc="> Command Card">
    
    //    @FXML private ImagePane ccImagePane00;
    //    @FXML private ImagePane ccImagePane10;
    //    @FXML private ImagePane ccImagePane20;
    //    @FXML private ImagePane ccImagePane30;
    //
    //    @FXML private ImagePane ccImagePane01;
    //    @FXML private ImagePane ccImagePane11;
    //    @FXML private ImagePane ccImagePane21;
    //    @FXML private ImagePane ccImagePane31;
    //
    //    @FXML private ImagePane ccImagePane02;
    //    @FXML private ImagePane ccImagePane12;
    //    @FXML private ImagePane ccImagePane22;
    //    @FXML private ImagePane ccImagePane32;
    //
    //    @FXML private ImagePane ccImagePane03;
    //    @FXML private ImagePane ccImagePane13;
    //    @FXML private ImagePane ccImagePane23;
    //    @FXML private ImagePane ccImagePane33;
    
    @FXML private GridPane commandCardGridPane;
    
    //</editor-fold>
    
    //</editor-fold>
    
    private ImageButton[][] commandCardButtonMatrix;
    
    protected GameFooterController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public void initialize() {
        super.initialize();
        
        initCommandCard();
    }
    
    private void initCommandCard() {
        //<editor-fold desc="> Fill Matrix">
        
        //        commandCardMatrix[0][0] = ccImagePane00;
        //        commandCardMatrix[1][0] = ccImagePane10;
        //        commandCardMatrix[2][0] = ccImagePane20;
        //        commandCardMatrix[3][0] = ccImagePane30;
        //
        //        commandCardMatrix[0][1] = ccImagePane01;
        //        commandCardMatrix[1][1] = ccImagePane11;
        //        commandCardMatrix[2][1] = ccImagePane21;
        //        commandCardMatrix[3][1] = ccImagePane31;
        //
        //        commandCardMatrix[0][2] = ccImagePane02;
        //        commandCardMatrix[1][2] = ccImagePane12;
        //        commandCardMatrix[2][2] = ccImagePane22;
        //        commandCardMatrix[3][2] = ccImagePane32;
        //
        //        commandCardMatrix[0][3] = ccImagePane03;
        //        commandCardMatrix[1][3] = ccImagePane13;
        //        commandCardMatrix[2][3] = ccImagePane23;
        //        commandCardMatrix[3][3] = ccImagePane33;
        
        //</editor-fold>
        
        this.commandCardButtonMatrix = new ImageButton[ccWidth()][ccHeight()];
        
        ArraysSL.fillMatrix(dimensions -> {
            final ImagePane imagePane = new ImagePane();
            commandCardGridPane.add(imagePane, dimensions.width(), dimensions.height());
            return new ImageButton(
                    this,
                    "Command Card Dummy Button [" + dimensions.width() + ", " + dimensions.height() + "]",
                    "home",
                    imagePane,
                    () -> Print.print("CC Button Pressed: " + dimensions),
                    null,
                    false,
                    null).init();
        }, commandCardButtonMatrix);
        
//        ArraysSL.iterateMatrix((d, ip) -> {
//            Print.print("Setting Image Button For...  " + d);
//            new ImageButton(
//                    this,
//                    "Command Card Dummy Button [" + d.width() + ", " + d.height() + "]",
//                    "home",
//                    ip,
//                    () -> Print.print("CC Button Pressed: " + getCommandCardButtonLocation(ip)),
//                    null,
//                    false,
//                    null).init();
//            return null;
//        }, commandCardMatrix);
        
        new ImageButton(
                this,
                "Selection Portrait Dummy Image",
                "account_manager",
                selectionPreviewImagePane,
                () -> Print.print("Selection Portrait Dummy Preview Pressed"),
                null,
                false,
                new Point2D(100, 200)
        ).init();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    private int ccWidth() { return commandCardGridPane.getColumnCount(); }
    private int ccHeight() { return commandCardGridPane.getRowCount(); }
    
    public final @NotNull ImageButton[][] getCommandCardButtonMatrix() { return commandCardButtonMatrix; }
    public final @Nullable ImageButton getCommandCardButtonAt(int x, int y) { return (x < ccWidth() && y < ccHeight() && x >= 0 && y >= 0) ? commandCardButtonMatrix[x][y] : null; }
    public final @Nullable Dimensions getCommandCardButtonLocation(@Nullable ImageButton button) { return button != null ? ArraysSL.iterateMatrix((d, ip) -> ip.equals(button) ? d : null, commandCardButtonMatrix) : null; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public Pane root() { return root; }
    
    //</editor-fold>
}
