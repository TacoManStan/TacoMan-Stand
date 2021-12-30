package com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_list_page;

import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.suit_lady.ui.pages.impl.content_selector.mandelbrot_test.MandelbrotContentHandler;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TaskTools;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.ReentrantLock;

public class MandelbrotContentHandlerOLD
        implements Springable {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    private final MandelbrotContentHandler contentHandler;
    
    public MandelbrotContentHandlerOLD(@NotNull Springable springable) {
        this.springable = springable.asStrict();
        this.lock = new ReentrantLock();
        
        this.contentHandler = new MandelbrotContentHandler(this);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReentrantLock getLock() {
        return lock;
    }
    
    public final MandelbrotContentHandler getContentHandler() {
        return contentHandler;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    //</editor-fold>
}
