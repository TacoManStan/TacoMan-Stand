package com.taco.suit_lady.ui.ui_internal.console;

import com.taco.suit_lady.ui.console.ConsoleMessageable;
import com.taco.suit_lady.ui.jfx.lists.treehandler.WrappingTreeCellData;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.Validatable;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.TreeView;

import java.util.Objects;

public class ConsoleUIDataContainer
{
    private final TreeView<WrappingTreeCellData<ConsoleMessageable<?>>> treeView;
    private final Validatable<ConsoleMessageable<?>> validator;
    
    private final ObservableBooleanValue showTRiBotProperty;
    private final ObservableBooleanValue showClientProperty;
    private final ObservableBooleanValue showScriptProperty;
    private final ObservableBooleanValue showSelectedInstanceOnlyProperty;
    
    public ConsoleUIDataContainer(TreeView<WrappingTreeCellData<ConsoleMessageable<?>>> treeView)
    {
        this(treeView, null, null, null, null);
    }
    
    public ConsoleUIDataContainer(
            TreeView<WrappingTreeCellData<ConsoleMessageable<?>>> treeView,
            ObservableBooleanValue showTRiBotProperty,
            ObservableBooleanValue showClientProperty,
            ObservableBooleanValue showScriptProperty,
            ObservableBooleanValue showSelectedInstanceOnlyProperty)
    {
        this.treeView = treeView;
        
        this.showTRiBotProperty = hlpr_getObservable(showTRiBotProperty);
        this.showClientProperty = hlpr_getObservable(showClientProperty);
        this.showScriptProperty = hlpr_getObservable(showScriptProperty);
        this.showSelectedInstanceOnlyProperty = hlpr_getObservable(showSelectedInstanceOnlyProperty);
        
        //
        this.validator = obj -> Objects.nonNull(obj); // CHANGE-HERE
    }
    
    //<editor-fold desc="Properties">
    
    public final TreeView<WrappingTreeCellData<ConsoleMessageable<?>>> getTreeView()
    {
        return treeView;
    }
    
    public final Validatable<ConsoleMessageable<?>> getValidator()
    {
        return validator;
    }
    
    //
    
    public final ObservableBooleanValue showTRiBotProperty()
    {
        return showTRiBotProperty;
    }
    
    public final boolean isShowingTRiBot()
    {
        return showTRiBotProperty.get();
    }
    
    //
    
    public final ObservableBooleanValue showClientProperty()
    {
        return showClientProperty;
    }
    
    public final boolean isShowingClient()
    {
        return showClientProperty.get();
    }
    
    //
    
    public final ObservableBooleanValue showScriptProperty()
    {
        return showScriptProperty;
    }
    
    public final boolean isShowingScript()
    {
        return showScriptProperty.get();
    }
    
    //
    
    public final ObservableBooleanValue showSelectedInstanceOnlyProperty()
    {
        return showSelectedInstanceOnlyProperty;
    }
    
    public final boolean isShowingSelectedInstanceOnly()
    {
        return showSelectedInstanceOnlyProperty.get();
    }
    
    //</editor-fold>
    
    private ObservableBooleanValue hlpr_getObservable(ObservableBooleanValue observable)
    {
        return observable != null ? observable : BindingsSL.boolBinding(true);
    }
}

/*
 * TODO LIST:
 * [S] Create a more modular version of this system that is easily expandable.
 * [S] Make it so the functionality of ClientConsoleMessage is internally contained, and so an instanceof check isn't necessary.
 */