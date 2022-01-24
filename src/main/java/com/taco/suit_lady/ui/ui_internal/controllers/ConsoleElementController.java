package com.taco.suit_lady.ui.ui_internal.controllers;

import com.taco.suit_lady.util.timing.Timing;
import com.taco.suit_lady.ui.console.ConsoleMessageable;
import com.taco.suit_lady.ui.jfx.components.button.ImageButton;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.lists.treehandler.WrappingTreeCellData;
import com.taco.tacository.quick.ConsoleBB;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/console/console_element.fxml")
@Scope("prototype")
public final class ConsoleElementController extends CellController<WrappingTreeCellData<ConsoleMessageable<?>>>
{
    //<editor-fold desc="Static">
    
    private static final Color TEXT_COLOR = new Color(153D / 255D, 153D / 255D, 153D / 255D, 1D);
    
    //</editor-fold>
    
    //<editor-fold desc="FXML">
    
    @FXML private Pane root;
    
    @FXML private ImagePane settingsImagePane;
    //	@FXML @DoNotRename private TextFlow messageTextFlow;
    @FXML private HBox contentHBox;
    
    //</editor-fold>
    
    
    private ImageButton settingsButton;
    
    private Label timestampLabel;
    private Label messageLabel;
    
    public ConsoleElementController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    //<editor-fold desc="Initialize">
    
    @Override
    public void initialize()
    {
        this.settingsButton = new ImageButton(
                this,
                "Details",
                "details",
                settingsImagePane,
                this::onDetails,
                null,
                false,
                null
        );
        this.settingsButton.init();
        
        this.settingsImagePane.setMinSize(settingsImagePane.getImage().getWidth(), settingsImagePane.getImage().getHeight());
        
        this.addLabel(this.timestampLabel = new Label());
        this.addLabel(this.messageLabel = new Label());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Implementation">
    
    @Override
    protected void onContentChange(WrappingTreeCellData<ConsoleMessageable<?>> oldCellData, WrappingTreeCellData<ConsoleMessageable<?>> newCellData)
    {
        //        ConsoleBB.CONSOLE.print(
        //                "Content Changed: "
        //                + "[" + oldCellData + "]"
        //                + " --> "
        //                + "[" + newCellData + "]"
        //        );
        
        if (newCellData != null) {
            final ConsoleMessageable<?> message = newCellData.getWrappedObject();
            final boolean validMessage = message != null;
            
            String timestampString = validMessage ? Timing.timestamp(message.getTimestamp()) + " " : newCellData.getName();
            String messageString = validMessage ? (String) message.getText() : null;
            
            timestampLabel.setText(timestampString);
            messageLabel.setText(messageString);
            
            timestampLabel.setMinWidth(timestampLabel.getPrefWidth());
        }
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Helpers">
    
    private void onDetails()
    {
        ConsoleBB.CONSOLE.dev("Settings ImageButton Pressed");
        ConsoleBB.CONSOLE.dev("Let's type out a really long message as a test to see what the console will do when the message is too long to fit inside of the allocated console area.");
    }
    
    private void addLabel(Label label)
    {
        ToolsFX.runFX(() -> contentHBox.getChildren().add(label), true);
    }
    
    //</editor-fold>
}

/*
 * TODO LIST:
 * [S] Add support for ConsoleMessageable types other than SimpleConsoleMessage.
 * [S] Make console scrollable rather than just cutting the message off.
 * [S] Print the timestamp properly.
 * [S] Figure out way to use Label instead of Text for messages?
 * [S] Move message output generation to ConsoleMessageable instance instead of here.
 */