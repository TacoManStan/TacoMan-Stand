package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.view.ui.console.Console;
import com.taco.suit_lady.view.ui.console.ConsoleMessageable;
import com.taco.suit_lady.view.ui.jfx.lists.treehandler.WrappingTreeCellData;
import com.taco.suit_lady.view.ui.ui_internal.console.ConsolePage;
import com.taco.suit_lady.view.ui.ui_internal.console.ConsoleUIDataContainer;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/sidebar/console/console.fxml")
@Scope("prototype")
public class ConsoleController extends SidebarNodeGroupController<ConsolePage>
{
    @FXML private BorderPane root;
    
    @FXML private TreeView<WrappingTreeCellData<ConsoleMessageable<?>>> consoleTree;
    
    protected ConsoleController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    //
    
    public final TreeView<WrappingTreeCellData<ConsoleMessageable<?>>> getConsoleTree()
    {
        return consoleTree;
    }
    
    //
    
    @Override
    @FXML public void initialize()
    {
        Console.consolify(weaver(), ctx(), new ConsoleUIDataContainer(consoleTree));
    }
}