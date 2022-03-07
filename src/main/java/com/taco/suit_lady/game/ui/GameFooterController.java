package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.ui.FooterController;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.components.button.ImageButton;
import com.taco.suit_lady.util.values.bounds.Dimensions;
import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.tools.printing.Printer;
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
    
    private ImageButton selectionPreviewImageButton;
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
        selectionPreviewImageButton = new ImageButton(
                this,
                "Selection Portrait Dummy Image",
                "account_manager",
                selectionPreviewImagePane,
                () -> Printer.print("Selection Portrait Dummy Preview Pressed"),
                null,
                false,
                new Point2D(100, 200)
        ).init();
        
        this.commandCardButtonMatrix = new ImageButton[ccWidth()][ccHeight()];
        A.fillMatrix(dimensions -> {
            final ImagePane imagePane = new ImagePane();
            commandCardGridPane.add(imagePane, dimensions.width().intValue(), dimensions.height().intValue());
            return new ImageButton(
                    this,
                    "Command Card Dummy Button [" + dimensions.width() + ", " + dimensions.height() + "]",
                    "home",
                    imagePane,
                    () -> Printer.print("CC Button Pressed: " + dimensions),
                    null,
                    false,
                    null).init();
        }, commandCardButtonMatrix);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="> Command Card">
    
    public final int ccWidth() { return commandCardGridPane.getColumnCount(); }
    public final int ccHeight() { return commandCardGridPane.getRowCount(); }
    
    public final @NotNull ImageButton[][] getCommandCardButtonMatrix() { return commandCardButtonMatrix; }
    public final @Nullable ImageButton getCommandCardButtonAt(int x, int y) { return (x < ccWidth() && y < ccHeight() && x >= 0 && y >= 0) ? commandCardButtonMatrix[x][y] : null; }
    public final @Nullable Dimensions getCommandCardButtonLocation(@Nullable ImageButton button) { return button != null ? A.iterateMatrix((d, ip) -> ip.equals(button) ? d : null, commandCardButtonMatrix) : null; }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public Pane root() { return root; }
    
    //</editor-fold>
}
