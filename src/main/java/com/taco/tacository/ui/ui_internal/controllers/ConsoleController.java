package com.taco.tacository.ui.ui_internal.controllers;

import com.taco.tacository.ui.console.Console;
import com.taco.tacository.ui.console.ConsoleMessageable;
import com.taco.tacository.ui.jfx.lists.treehandler.WrappingTreeCellData;
import com.taco.tacository.ui.ui_internal.console.ConsolePage;
import com.taco.tacository.ui.ui_internal.console.ConsoleUIDataContainer;
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
@FxmlView("/fxml/console/console.fxml")
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
    public void initialize()
    {
        ctx().getBean(Console.class).consolify(new ConsoleUIDataContainer(consoleTree));
    }
    
}