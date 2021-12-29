package com.taco.suit_lady.ui.contents.mandelbrot.mandelbrot_list_page;

import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContent;
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

public class MandelbrotContentHandler
        implements Springable {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    private final ReadOnlyListWrapper<MandelbrotContent> contentList;
    private final ObjectProperty<MandelbrotContent> selectedContentProperty;
    
    public MandelbrotContentHandler(Springable springable) {
        this.springable = springable.asStrict();
        this.lock = new ReentrantLock();
        
        this.contentList = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        this.selectedContentProperty = new SimpleObjectProperty<>();
        
        
        this.selectedContentProperty.addListener((observable, oldValue, newValue) -> ui().getContentManager().setContent(newValue));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReentrantLock getLock() {
        return lock;
    }
    
    
    public final ReadOnlyListProperty<MandelbrotContent> contentList() {
        return contentList.getReadOnlyProperty();
    }
    
     public final ObjectProperty<MandelbrotContent> selectedContentProperty() {
        return selectedContentProperty;
     }
     
     public final MandelbrotContent getSelectedContent() {
        return selectedContentProperty.get();
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
    
    public final @NotNull MandelbrotContent newInstance() {
        MandelbrotContent content = new MandelbrotContent(this);
        TaskTools.sync(lock, () -> contentList.add(content));
//        client.start();
        return content;
    }
    
    
    public void shutdown() {
        // TODO: Move to separate, synchronized task threads for each DummyClient instance.
        logiCore().execute(() -> {
            TaskTools.sync(lock, () -> contentList.forEach(client -> shutdown(client)));
            if (!contentList.isEmpty())
                throw ExceptionTools.ex("Mandelbrot Content List should be empty! (" + contentList + ")");
        });
    }
    
    public void shutdown(MandelbrotContent content) {
        contentList.remove(content);
//        content.shutdown();
    }
}
