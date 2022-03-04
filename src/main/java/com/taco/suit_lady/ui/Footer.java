package com.taco.suit_lady.ui;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.printer.Print;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

// TO-DOC
public abstract class Footer<F extends Footer<F, FC, T, TD, TC>, FC extends FooterController<F, FC, T, TD, TC>,
        T extends Content<T, TD, TC, F, FC>, TD extends ContentData<T, TD, TC, F, FC>, TC extends ContentController<T, TD, TC, F, FC>>
        implements Displayable, Springable {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final T content;
    private final FC controller;
    
    public Footer(@NotNull T content, Object... constructorParams) {
        this.weaver = Exc.nullCheck(content.weaver(), "FxWeaver");
        this.ctx = Exc.nullCheck(content.ctx(), "ApplicationContext");
        
        this.content = content;
        
        // Compound expression containing null checks for both the controller definition and the resulting constructor instance itself
        this.controller = Exc.nullCheckMessage(
                weaver().loadController(Exc.nullCheck(controllerDefinition(), "Controller Definition Class")),
                "Error Loading Controller of Type [" + controllerDefinition() + "] — Ensure controller class is defined in FXML file.");
        
        this.controller.setFooter(this);
        this.initializeFooter(constructorParams);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final T getContent() { return content; }
    public @NotNull FC getController() { return controller; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull FxWeaver weaver() { return weaver; }
    @Override public @NotNull ConfigurableApplicationContext ctx() { return ctx; }
    
    @Override public @Nullable Pane getContentPane() { return getController().root(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void initializeFooter(@NotNull Object[] constructorParams);
    protected abstract @NotNull Class<FC> controllerDefinition();
    
    protected void onContentChange(boolean shown) {
        Print.print(shown ? "Content Shown (" + getContent() + ")" : "Content Hidden (" + getContent() + ")");
    }
    
    //</editor-fold>
}
