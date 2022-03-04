package com.taco.suit_lady.ui.jfx.hyperlink;

import com.taco.suit_lady.util.tools.fx_tools.FX;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseButton;

import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class HyperlinkFX
{
    
    private final ReentrantLock lock;
    
    private boolean clicking = false;
    private boolean doubleClick = false;
    
    //
    
    private final String key;
    private final String text;
    
    private final EventHandler<ActionEvent> leftClickHandler;
    private final EventHandler<ActionEvent> rightClickHandler;
    private final EventHandler<ActionEvent> centerClickHandler;
    private boolean doubleClickMode;
    
    private Consumer<Hyperlink> onCreate;
    
    private final ObjectProperty<Hyperlink> hyperlinkProperty;
    private final BooleanProperty invalidProperty;
    private final BooleanProperty disabledProperty;
    
    /**
     * Constructs a new {@link HyperlinkFX} object given the specified key, getText String, and event handler.
     *
     * @param key              The key for this {@link HyperlinkFX}. To use the key, enter {@code {key_name}} into the getText passed into {@link HyperlinkNodeFX}.
     * @param text             The getText that is displayed as a {@link Hyperlink} wherever {@code {key_name}} is typed.
     * @param leftClickHandler The {@link ActionEvent} handler. This is run whenever the {@link Hyperlink} is clicked.
     */
    public HyperlinkFX(
            String key, String text,
            EventHandler<ActionEvent> leftClickHandler
    )
    {
        this(key, text, leftClickHandler, event ->
        {
        });
    }
    
    /**
     * Constructs a new {@link HyperlinkFX} object given the specified key, getText String, and event handler.
     *
     * @param key               The key for this {@link HyperlinkFX}. To use the key, enter {@code {key_name}} into the getText passed into {@link HyperlinkNodeFX}.
     * @param text              The getText that is displayed as a {@link Hyperlink} wherever {@code {key_name}} is typed.
     * @param leftClickHandler  The {@link ActionEvent} handler. This is run whenever the {@link Hyperlink} is clicked.
     * @param rightClickHandler The {@link ActionEvent} handler. This is run whenever the {@link Hyperlink} is <i>right</i> clicked.
     */
    public HyperlinkFX(
            String key, String text,
            EventHandler<ActionEvent> leftClickHandler,
            EventHandler<ActionEvent> rightClickHandler
    )
    {
        this(key, text, leftClickHandler, rightClickHandler, event ->
        {
        }, null);
    }
    
    /**
     * Constructs a new {@link HyperlinkFX} object given the specified key, getText String, and event handler.
     *
     * @param key               The key for this {@link HyperlinkFX}. To use the key, enter {@code {key_name}} into the getText passed into {@link HyperlinkNodeFX}.
     * @param text              The getText that is displayed as a {@link Hyperlink} wherever {@code {key_name}} is typed.
     * @param leftClickHandler  Run whenever the {@link Hyperlink} is clicked.
     * @param rightClickHandler Run whenever the {@link Hyperlink} is <i>right</i> clicked.
     */
    public HyperlinkFX(
            String key, String text,
            EventHandler<ActionEvent> leftClickHandler,
            EventHandler<ActionEvent> rightClickHandler,
            EventHandler<ActionEvent> centerClickHandler
    )
    {
        this(key, text, leftClickHandler, rightClickHandler, centerClickHandler, null);
    }
    
    /**
     * Constructs a new {@link HyperlinkFX} object given the specified key, getText String, and event handler.
     *
     * @param key               The key for this {@link HyperlinkFX}. To use the key, enter {@code {key_name}} into the getText passed into {@link HyperlinkNodeFX}.
     * @param text              The getText that is displayed as a {@link Hyperlink} wherever {@code {key_name}} is typed.
     * @param leftClickHandler  Run whenever the {@link Hyperlink} is clicked.
     * @param rightClickHandler Run whenever the {@link Hyperlink} is <i>right</i> clicked.
     * @param onCreate          The
     */
    public HyperlinkFX(
            String key, String text,
            EventHandler<ActionEvent> leftClickHandler,
            EventHandler<ActionEvent> rightClickHandler,
            EventHandler<ActionEvent> centerClickHandler,
            Consumer<Hyperlink> onCreate)
    {
        this.lock = new ReentrantLock();
        
        this.key = key;
        this.text = text;
        
        this.leftClickHandler = createEventHandler(leftClickHandler);
        this.rightClickHandler = createEventHandler(rightClickHandler);
        this.centerClickHandler = createEventHandler(centerClickHandler);
        this.doubleClickMode = false;
        
        this.onCreate = onCreate;
        
        this.hyperlinkProperty = new SimpleObjectProperty<>();
        this.invalidProperty = new SimpleBooleanProperty();
        this.disabledProperty = new SimpleBooleanProperty();
        
        this.init();
    }
    
    private void init()
    {
        InvalidationListener listener = param ->
        {
            final Hyperlink hyperlinkCopy = hyperlinkProperty.get();
            if (hyperlinkCopy != null)
            {
                hyperlinkCopy.pseudoClassStateChanged(FX.INVALID, invalidProperty().get());
                hyperlinkCopy.pseudoClassStateChanged(FX.DISABLED, disabledProperty().get());
            }
        };
        hyperlinkProperty.addListener(listener);
        invalidProperty().addListener(listener);
        disabledProperty().addListener(listener);
    }
    
    /**
     * Sets the on-create {@link Consumer} to the specified value.
     * <p>
     * The specified {@link Consumer} is called right after this {@link HyperlinkFX} creates a new {@link Hyperlink}.
     *
     * @param onCreate The on-create {@link Consumer}.
     */
    public void setOnCreate(Consumer<Hyperlink> onCreate)
    {
        this.onCreate = onCreate;
    }
    
    /**
     * Returns the invalid property for this {@link HyperlinkFX}.
     * <p>
     * When a hyperlink is invalid, it is shown as red. No mechanical changes are made to the hyperlink.
     *
     * @return The invalid property for this {@link HyperlinkFX}.
     */
    public final BooleanProperty invalidProperty()
    {
        return invalidProperty;
    }
    
    public final BooleanProperty disabledProperty()
    {
        return disabledProperty;
    }
    
    /**
     * Returns true if this {@link HyperlinkFX} is in double-click mode, false if it is in center-click mode.
     *
     * @return True if this {@link HyperlinkFX} is in double-click mode, false if it is in center-click mode.
     */
    public boolean isDoubleClickMode()
    {
        return doubleClickMode;
    }
    
    /**
     * Sets the double-click mode of this {@link HyperlinkFX} to the specified value.
     *
     * @param doubleClickMode True if this {@link HyperlinkFX} should be put into double-click mode, false if it should be put into center-click mode.
     */
    public void setDoubleClickMode(boolean doubleClickMode)
    {
        this.doubleClickMode = doubleClickMode;
    }
    
    /**
     * Returns the getText to display for this {@link HyperlinkFX}.
     * <br>
     * If the getText displayed by this {@link HyperlinkFX} needs to be dynamic, override this method.
     *
     * @return The getText to display for this {@link HyperlinkFX}.
     */
    public String getText()
    {
        return text;
    }
    
    /**
     * Returns the key of this {@link HyperlinkFX}.
     *
     * @return The key of this {@link HyperlinkFX}.
     */
    public final String getKey()
    {
        return key;
    }
    
    /**
     * Returns the {@link EventHandler} that handles when the {@link Hyperlink} generated by this {@link HyperlinkFX} is clicked.
     *
     * @return The {@link EventHandler} that handles when the {@link Hyperlink} generated by this {@link HyperlinkFX} is clicked.
     */
    protected final EventHandler<ActionEvent> getHandler()
    {
        return leftClickHandler;
    }
    
    /**
     * Returns the {@link EventHandler} that handles when the {@link Hyperlink} generated by this {@link HyperlinkFX} is <i>right</i> clicked.
     *
     * @return The {@link EventHandler} that handles when the {@link Hyperlink} generated by this {@link HyperlinkFX} is <i>right</i> clicked.
     */
    protected final EventHandler<ActionEvent> getRightClickHandler()
    {
        return rightClickHandler;
    }
    
    /**
     * Returns the {@link EventHandler} that handles when the {@link Hyperlink} generated by this {@link HyperlinkFX} is <i>double</i> clicked.
     *
     * @return The {@link EventHandler} that handles when the {@link Hyperlink} generated by this {@link HyperlinkFX} is <i>double</i> clicked.
     */
    protected final EventHandler<ActionEvent> getCenterClickHandler()
    {
        return centerClickHandler;
    }
    
    /**
     * Creates a new {@link Hyperlink} object from this {@link HyperlinkFX} and then returns the created {@link Hyperlink}.
     *
     * @return A new {@link Hyperlink} object from this {@link HyperlinkFX} and then returns the created {@link Hyperlink}.
     */
    protected final Hyperlink createHyperlink()
    {
        final Hyperlink hyperlink = new Hyperlink(getText());
        hyperlink.setOnMouseClicked(event ->
                                    {
                                        if (isDoubleClickMode())
                                        {
                                            if (event.getButton() == MouseButton.SECONDARY)
                                            {
                                                if (event.getClickCount() == 1)
                                                    rightClickHandler.handle(new ActionEvent());
                                            }
                                            else if (event.getButton() == MouseButton.PRIMARY)
                                            {
                                                if (event.getClickCount() == 1)
                                                {
                                                    doubleClick = false;
                                                    new java.util.Timer().schedule(new TimerTask()
                                                    {
                                                        @Override public void run()
                                                        {
                                                            if (!doubleClick)
                                                                FX.runFX(() -> leftClickHandler.handle(new ActionEvent()), false);
                                                        }
                                                    }, 250);
                                                }
                                                else
                                                {
                                                    doubleClick = true;
                                                    centerClickHandler.handle(new ActionEvent());
                                                }
                                            }
                                        }
                                        else if (event.getClickCount() == 1)
                                            if (event.getButton() == MouseButton.PRIMARY)
                                                if (event.isShiftDown())
                                                    rightClickHandler.handle(new ActionEvent());
                                                else if (event.isControlDown())
                                                    centerClickHandler.handle(new ActionEvent());
                                                else
                                                    leftClickHandler.handle(new ActionEvent());
                                            else if (event.getButton() == MouseButton.SECONDARY)
                                                rightClickHandler.handle(new ActionEvent());
                                            else if (event.getButton() == MouseButton.MIDDLE)
                                                centerClickHandler.handle(new ActionEvent());
                                    });
        if (onCreate != null)
            onCreate.accept(hyperlink);
        hyperlinkProperty.set(hyperlink);
        return hyperlink;
    }
    
    /**
     * Checks to see if the specified {@link HyperlinkFX} is valid or not.
     * <p>
     * A {@link HyperlinkFX} is valid if it is not null, its key is not null, and its getText is not null.
     *
     * @param hyperlink The {@link HyperlinkFX} being validated.
     * @return True if the specified {@link HyperlinkFX} is valid, false otherwise.
     */
    protected static boolean isValid(HyperlinkFX hyperlink)
    {
        return hyperlink != null && hyperlink.getKey() != null && hyperlink.getText() != null;
    }
    
    /**
     * Creates a new {@link HyperlinkFX} with the exact same properties as this one, but with different text.
     *
     * @param text The tex.
     * @param key  The key.
     * @return A new {@link HyperlinkFX} with the exact same properties as this one, but with different text.
     */
    public HyperlinkFX copy(String key, Object text)
    {
        return new HyperlinkFX(key, text.toString(), leftClickHandler, rightClickHandler, centerClickHandler);
    }
    
    @Override public String toString()
    {
        return "HyperlinkFX{" + "key=" + key + ", text=" + getText() + ", eventHandler=" + leftClickHandler + '}';
    }
    
    private EventHandler<ActionEvent> createEventHandler(EventHandler source)
    {
        if (source != null)
            return event ->
            {
                lock.lock();
                try
                {
                    if (!clicking)
                    {
                        clicking = true;
                        source.handle(event);
                        clicking = false;
                    }
                }
                finally
                {
                    lock.unlock();
                }
            };
        return event ->
        {
        };
    }
}
