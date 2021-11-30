package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.ui_internal.controllers.Controller;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class ContentNew<C extends Controller>
        implements Springable
{
    private final Springable strictSpringable;
    
    private final StackPane contentRoot;
    private final C controller;
    
    private final ReadOnlyListWrapper<UINode> books;
    
    public ContentNew(@NotNull Springable springable)
    {
        ExceptionTools.nullCheck(springable, "Springable Parent");
        this.strictSpringable = springable.asStrict();
        
        this.contentRoot = new StackPane();
        
        this.books = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        
        this.controller = ExceptionTools.nullCheckMessage(
                weaver().loadController(ExceptionTools.nullCheck(controllerDefinition(), "Controller Definition Class")),
                "Error Loading Controller of Type [" + controllerDefinition() + "] — Ensure corresponding .fxml file exists and that the controller is defined inside."
        );
    }
    
    public @NotNull StackPane getContentRoot()
    {
        return contentRoot;
    }
    
    public @NotNull ReadOnlyListProperty<UINode> books()
    {
        return books.getReadOnlyProperty();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public FxWeaver weaver()
    {
        return strictSpringable.weaver();
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return strictSpringable.ctx();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract Class<C> controllerDefinition();
    
    //</editor-fold>
}
