package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.Springable;
import com.taco.suit_lady.view.ui.jfx.button.ButtonViewable;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import com.taco.suit_lady.view.ui.ui_internal.pages.DummyPage;
import javafx.beans.property.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class UINode
        implements Displayable, ButtonViewable, Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    //
    
    private final ReentrantLock lock;
    
    private final ReadOnlyStringWrapper idProperty;
    private final ReadOnlyStringWrapper nameProperty;
    
    private final ReadOnlyObjectWrapper<UINodeGroup> group; //e.g., owner
    
    private final Displayer<UIPage<?>> displayer;
    private final UIPageHandler pageHandler;
    
    private final ReadOnlyObjectWrapper<ImageButton> imageButtonProperty;
    private final ReadOnlyBooleanWrapper expandableProperty;
    
    private final ObjectProperty<Runnable> onGroupChangeProperty;
    private final ObjectProperty<Runnable> onGroupChangedProperty;
    
    public UINode(FxWeaver weaver, ConfigurableApplicationContext ctx, String name, String id, Function<UINode, UIPage<?>> coverPageFunction, Runnable onAction)
    {
        this(weaver, ctx, name, id, coverPageFunction, onAction, null);
    }
    
    public UINode(FxWeaver weaver, ConfigurableApplicationContext ctx, String name, String id, Function<UINode, UIPage<?>> coverPageFunction, Runnable onAction, StackPane contentPane)
    {
        this.weaver = weaver;
        this.ctx = ctx;
        
        id = id == null ? "missingno" : id;
        
        this.lock = new ReentrantLock();
        this.idProperty = new ReadOnlyStringWrapper(id);
        this.nameProperty = new ReadOnlyStringWrapper(name);
        
        this.displayer = new Displayer<>(this.lock, contentPane);
        
        this.group = new ReadOnlyObjectWrapper<>();
        
        this.imageButtonProperty = new ReadOnlyObjectWrapper<>();
        this.expandableProperty = new ReadOnlyBooleanWrapper();
        
        this.onGroupChangeProperty = new SimpleObjectProperty<>();
        this.onGroupChangedProperty = new SimpleObjectProperty<>();
        
        this.pageHandler = new UIPageHandler(this.lock, this, coverPageFunction != null ? coverPageFunction.apply(this) : new DummyPage(this));
        
        //
        
        displayer.bindAndInvalidate(pageHandler.visiblePageBinding());
        
        group.addListener((observable, oldValue, newValue) ->
                          {
                              groupChangeResponse(true); // TODO - This doesn't work quite correctly.
                              if (getID() == null)
                                  idProperty.set("missingno");
                              groupChangeResponse(false);
                          });
        
        imageButtonProperty.addListener((observable, oldValue, newValue) -> newValue.initialize());
        imageButtonProperty.set(new ImageButton(null, idProperty, () -> onAction(onAction), true, true, ImageButton.SMALL));
    }
    
    @Override
    public FxWeaver weaver()
    {
        return this.weaver;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return this.ctx;
    }
    
    //<editor-fold desc="Properties">
    
    public ReadOnlyStringProperty idProperty()
    {
        return idProperty.getReadOnlyProperty();
    }
    
    public String getID()
    {
        return idProperty.get();
    }
    
    //
    
    public ReadOnlyStringProperty nameProperty()
    {
        return nameProperty.getReadOnlyProperty();
    }
    
    public String getName()
    {
        return nameProperty.get();
    }
    
    //
    
    public Displayer<UIPage<?>> getDisplayer()
    {
        return displayer;
    }
    
    //
    
    public ReadOnlyObjectProperty<UINodeGroup> groupProperty()
    {
        return group.getReadOnlyProperty();
    }
    
    public UINodeGroup getGroup()
    {
        return group.get();
    }
    
    public void setGroup(UINodeGroup nodeGroup)
    {
        this.group.set(nodeGroup);
    }
    
    //
    
    public UIPageHandler getPageHandler()
    {
        return pageHandler;
    }
    
    //
    
    public ReadOnlyObjectProperty<ImageButton> buttonViewProperty()
    {
        return imageButtonProperty.getReadOnlyProperty();
    }
    
    public ImageButton getButtonView()
    {
        return imageButtonProperty.get();
    }
    
    //
    
    public ReadOnlyBooleanProperty expandableProperty()
    {
        return expandableProperty.getReadOnlyProperty();
    }
    
    public boolean isExpandable()
    {
        return expandableProperty.get();
    }
    
    protected void setExpandable(boolean expandable)
    {
        expandableProperty.set(expandable);
    }
    
    //
    
    //<editor-fold desc="Event Responders">
    
    //
    
    public ObjectProperty<Runnable> onGroupChangeProperty()
    {
        return onGroupChangeProperty;
    }
    
    public Runnable getOnGroupChangeResponder()
    {
        return onGroupChangeProperty.get();
    }
    
    public void setOnGroupChangeResponder(Runnable responder)
    {
        onGroupChangeProperty.set(responder);
    }
    
    //
    
    public ObjectProperty<Runnable> onGroupChangedProperty()
    {
        return onGroupChangedProperty;
    }
    
    public Runnable getOnGroupChangedResponder()
    {
        return onGroupChangedProperty.get();
    }
    
    public void setOnGroupChangedResponder(Runnable responder)
    {
        onGroupChangedProperty.set(responder);
    }
    
    //
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="Implementation">
    
    @Override public Pane getContent()
    {
        return displayer.getDisplayContainer();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Helpers">
    
    private void onAction(Runnable onAction)
    {
        if (onAction != null)
            onAction.run();
    }
    
    private void groupChangeResponse(boolean pre)
    {
        Runnable responder = pre ? getOnGroupChangeResponder() : getOnGroupChangedResponder();
        if (responder != null)
            responder.run();
    }
    
    //</editor-fold>
}
