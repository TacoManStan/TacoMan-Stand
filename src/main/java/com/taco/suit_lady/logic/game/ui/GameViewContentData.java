package com.taco.suit_lady.logic.game.ui;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainable;
import com.taco.suit_lady.ui.ContentData;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotColorScheme;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.tacository.json.JElement;
import com.taco.tacository.json.JLoadable;
import com.taco.tacository.json.JObject;
import com.taco.tacository.json.JUtil;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public class GameViewContentData extends ContentData
        implements ObservablePropertyContainable, SpringableWrapper, UIDProcessable {
    
    private final StrictSpringable springable;
    
    public GameViewContentData(@NotNull Springable springable) {
        this.springable = springable.asStrict();
    }
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return springable; }
    
    @Override public @NotNull Observable[] properties() { return new Observable[]{}; }
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("game_view_content");
        return uidProcessor;
    }
    
    //</editor-fold>
}
