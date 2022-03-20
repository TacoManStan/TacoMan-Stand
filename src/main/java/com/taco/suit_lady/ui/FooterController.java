package com.taco.suit_lady.ui;

import com.taco.suit_lady.ui.ui_internal.controllers.Controller;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class FooterController<F extends Footer<F, FC, T, TD, TC>, FC extends FooterController<F, FC, T, TD, TC>,
        T extends Content<T, TD, TC, F, FC>, TD extends ContentData<T, TD, TC, F, FC>, TC extends ContentController<T, TD, TC, F, FC>>
        extends Controller {
    
    private F footer; // Functionally Final
    
    protected FooterController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public @NotNull F getFooter() { return footer; }
    
    //</editor-fold>
    
    @Override public void initialize() {
        FX.setAnchors(root());
    }
    
    //<editor-fold desc="--- INTERNAL ---">
    
    protected void setFooter(@NotNull Footer<F, FC, T, TD, TC> footer) {
        try {
            this.footer = (F) Exc.nullCheck(footer, "UIPage");
        } catch (Exception e) {
            throw Exc.ex(e, "UIPage must be of type T [" + footer.getClass() + "]");
        }
        onPageBindingComplete();
    }
    
    protected void onPageBindingComplete() { }
    
    //</editor-fold>
}