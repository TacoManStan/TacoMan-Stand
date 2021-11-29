package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.tools.ResourceTools;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.jfx.button.ButtonViewable;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import com.taco.suit_lady.view.ui.ui_internal.pages.FallbackPage;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ApplicationContext;
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
    
    private final ReadOnlyStringWrapper buttonIDProperty;
    private final ReadOnlyStringWrapper nameProperty;
    
    private final Displayer<UIPage<?>> displayer;
    private final UIPageHandler pageHandler;
    
    private final ReadOnlyObjectWrapper<ImageButton> buttonViewProperty;
    
    public UINode(FxWeaver weaver, ConfigurableApplicationContext ctx, String name, String buttonID, Function<UINode, UIPage<?>> coverPageFunction, Runnable onAction)
    {
        this(weaver, ctx, name, buttonID, coverPageFunction, onAction, null);
    }
    
    /**
     * <p><b>Fully parameterized constructor for {@link UINode} objects.</b></p>
     * <br>
     *
     * @param weaver            The {@link FxWeaver} instance to be used for linking {@link UINode} {@code JavaFX} attributes to {@code Spring}.
     *                          <ul>
     *                              <li>See <i>{@link Springable#weaver()}</i></li>
     *                          </ul>
     * @param ctx               The {@link ApplicationContext} that allows easy access to {@code Spring-related} functionality for this {@link UINode}.
     *                          <ul>
     *                              <li>See <i>{@link Springable#ctx()}</i></li>
     *                          </ul>
     * @param name              The {@link #getName() name} to be assigned to this {@link UINode}.
     *                          <ul>
     *                              <li>See <i>{@link #nameProperty()}</i></li>
     *                          </ul>
     * @param buttonID          The {@link #getButtonID() ID} mapped to the {@link ResourceTools#getImage(String, String, String) cached} {@link Image image} to be used as the {@link #buttonViewProperty() button} for this {@link UINode}.
     *                          <p>
     * @param coverPageFunction The {@link Function} that will retrieve the {@link UIPage} to be used as the {@link UIPageHandler#coverPageProperty() cover page} of this {@link UINode}.
     *                          <ul>
     *                              <li>If the specified {@link Function} is {@code null}, a {@link FallbackPage} will be used as the {@link UIPageHandler#coverPageProperty() cover page} instead.</li>
     *                          </ul>
     * @param onAction          A {@link Runnable} that will be {@link Runnable#run() executed} when the {@link #buttonViewProperty() button} for this {@link UINode} is pressed.
     *                          <ul>
     *                              <li>See <i>{@link ImageButton#initialize()}</i> for exact implementation.</li>
     *                          </ul>
     * @param contentPane       The {@link StackPane} in which the contents ({@link UIPage pages}) of this {@link UINode} are displayed.
     *                          <ul>
     *                              <li>Accessed via <i>{@link #getContent() getContent()}<b>.</b>{@link Displayer#getDisplayContainer() getDisplayContainer()}</i></li>
     *                          </ul>
     */
    public UINode(FxWeaver weaver, ConfigurableApplicationContext ctx, String name, String buttonID, Function<UINode, UIPage<?>> coverPageFunction, Runnable onAction, StackPane contentPane)
    {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.lock = new ReentrantLock();
        this.buttonIDProperty = new ReadOnlyStringWrapper(buttonID != null ? buttonID : "missingno");
        this.nameProperty = new ReadOnlyStringWrapper(name);
        
        this.displayer = new Displayer<>(this.lock, contentPane);
        
        this.buttonViewProperty = new ReadOnlyObjectWrapper<>();
        
        this.pageHandler = new UIPageHandler(this.lock, this, coverPageFunction != null ? coverPageFunction.apply(this) : new FallbackPage(this));
        
        //
        
        displayer.bind(pageHandler.visiblePageBinding());
        
        buttonViewProperty.addListener((observable, oldValue, newValue) -> newValue.initialize());
        buttonViewProperty.set(new ImageButton(
                null,
                buttonIDProperty,
                () -> onAction(onAction),
                null,
                true,
                ImageButton.SMALL
        ));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ReadOnlyStringProperty property} defining the {@link ImageButton#nameBinding() file name} of the {@link ImageButton button} for this {@link UINode}.</p>
     *
     * @return The {@link ReadOnlyStringProperty property} defining the {@link ImageButton#nameBinding() file name} of the {@link ImageButton button} for this {@link UINode}.
     *
     * @see #getButtonID()
     */
    public ReadOnlyStringProperty buttonIDProperty()
    {
        return buttonIDProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link ImageButton#nameBinding() file name} of the {@link ImageButton button} for this {@link UINode}.</p>
     * <p>See <i>{@link #buttonIDProperty()}</i> for details.</p>
     *
     * @return The {@link ImageButton#nameBinding() file name} of the {@link ImageButton button} for this {@link UINode}.
     *
     * @see #buttonIDProperty()
     */
    public String getButtonID()
    {
        return buttonIDProperty.get();
    }
    
    //
    
    /**
     * <p>Returns the {@link ReadOnlyStringProperty property} containing the {@link #getName() name} of this {@link UINode}.</p>
     * <ol>
     *     <li>The {@link #getName() name} is only used for UI purposes, such as being displayed in a tooltip upon a user hovering over the {@link #buttonViewProperty() button} for this {@link UINode}.</li>
     *     <li>To change the actual {@link ImageButton button} for this {@link UINode}, see <i>{@link #buttonIDProperty()}</i>.</li>
     * </ol>
     *
     * @return The {@link ReadOnlyStringProperty property} containing the {@link #getName() name} of this {@link UINode}.
     *
     * @see #getName()
     */
    public ReadOnlyStringProperty nameProperty()
    {
        return nameProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link #nameProperty() name} of this {@link UINode}.</p>
     * <p>See <i>{@link #nameProperty()}</i> for details.</p>
     *
     * @return The {@link #nameProperty() name} of this {@link UINode}.
     *
     * @see #nameProperty()
     */
    public String getName()
    {
        return nameProperty.get();
    }
    
    //
    
    /**
     * <p>Returns the {@link Displayer} responsible for {@link Displayable displaying} currently-visible {@link UIPage pages} attached to this {@link UINode}.</p>
     *
     * @return The {@link Displayer} responsible for {@link Displayable displaying} currently-visible {@link UIPage pages} attached to this {@link UINode}.
     */
    public Displayer<UIPage<?>> getDisplayer()
    {
        return displayer;
    }
    
    //
    
    /**
     * <p>Returns the {@link UIPageHandler page handler} wrapping {@link UIPage page} functionality for this {@link UINode}.</p>
     *
     * @return The {@link UIPageHandler page handler} wrapping {@link UIPage page} functionality for this {@link UINode}.
     */
    public UIPageHandler getPageHandler()
    {
        return pageHandler;
    }
    
    //
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the {@link ImageButton} for selecting this {@link UINode}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the {@link ImageButton} for selecting this {@link UINode}.
     */
    public ReadOnlyObjectProperty<ImageButton> buttonViewProperty()
    {
        return buttonViewProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link ImageButton} for selecting this {@link UINode}.</p>
     *
     * @return The {@link ImageButton} for selecting this {@link UINode}.
     */
    @Override
    public ImageButton getButtonView()
    {
        return buttonViewProperty().get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    /**
     * <p>Returns the {@link Pane content} of this {@link UINode}.</p>
     * <blockquote>{@code Passthrough Definition:} <i><code>{@link #getDisplayer()}<b>.</b>{@link Displayer#getDisplayContainer() getDisplayContainer()}</code></i></blockquote>
     *
     * @return The {@link Pane content} of this {@link UINode}.
     */
    @Override
    public Pane getContent()
    {
        return getDisplayer().getDisplayContainer();
    }
    
    @Override
    public FxWeaver weaver()
    {
        return weaver;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return this.ctx;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- HELPER METHODS ---">
    
    private void onAction(Runnable onAction)
    {
        if (onAction != null)
            onAction.run();
    }
    
    //</editor-fold>
}
