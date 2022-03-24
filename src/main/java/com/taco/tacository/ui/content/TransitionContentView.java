package com.taco.tacository.ui.content;

import com.taco.tacository.util.tools.fx_tools.FX;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * A {@link ContentView} that only displays a single content at any given time.
 * The only time in which more than one content can ever be displayed is during a transition.
 */
public class TransitionContentView<P extends Pane> extends ContentView<P> {
    
    //<editor-fold desc="Static">
    
    private static final long DEFAULT_TRANSITION_TIME = 250L;
    
    //</editor-fold>
    
    private final StackPane rootPane;
    
    private final LongProperty transitionTimeProperty;
    private final ReadOnlyObjectWrapper<EventHandler<ActionEvent>> onShownHandlerProperty;
    
    private final ReadOnlyBooleanWrapper animatedProperty;
    
    public TransitionContentView(StackPane rootPane) {
        this(rootPane, null, DEFAULT_TRANSITION_TIME);
    }
    
    public TransitionContentView(StackPane rootPane, Number transitionTime) {
        this(rootPane, null, transitionTime);
    }
    
    public TransitionContentView(StackPane rootPane, P contentPane) {
        this(rootPane, contentPane, DEFAULT_TRANSITION_TIME);
    }
    
    public TransitionContentView(StackPane rootPane, P contentPane, Number transitionTime) {
        super(contentPane);
        this.rootPane = rootPane;
        
        this.transitionTimeProperty = new SimpleLongProperty(transitionTime.longValue());
        this.onShownHandlerProperty = new ReadOnlyObjectWrapper<>();
        
        this.animatedProperty = new ReadOnlyBooleanWrapper(true);
    }
    
    //<editor-fold desc="Properties">
    
    public final StackPane getRootPane() {
        return rootPane;
    }
    
    //
    
    public final LongProperty transitionTimeProperty() {
        return transitionTimeProperty;
    }
    
    public final long getTransitionTime() {
        return transitionTimeProperty.get();
    }
    
    public final void setTransitionTime(Number newDuration) {
        long long_duration = newDuration.longValue();
        if (long_duration < 0)
            throw new IndexOutOfBoundsException("Transition time must be >= 0.");
        transitionTimeProperty.set(long_duration);
    }
    
    //
    
    public final ReadOnlyObjectProperty<EventHandler<ActionEvent>> onShownHandlerProperty() {
        return onShownHandlerProperty.getReadOnlyProperty();
    }
    
    public final EventHandler<ActionEvent> getOnShownHandler() {
        return onShownHandlerProperty.get();
    }
    
    public final void setOnShownHandler(EventHandler<ActionEvent> onShownHandler) {
        onShownHandlerProperty.set(onShownHandler);
    }
    
    //
    
    public final ReadOnlyBooleanProperty animatedProperty() {
        return animatedProperty.getReadOnlyProperty();
    }
    
    public final boolean isAnimated() {
        return animatedProperty.get();
    }
    
    public final void setAnimated(boolean animated) {
        animatedProperty.set(animated);
    }
    
    //</editor-fold>
    
    //
    
    @Override
    protected final void onContentChange(P oldContent, P newContent) {
        if (isAnimated()) {
            Duration _transitionDuration = Duration.millis(getTransitionTime());
            EventHandler<ActionEvent> onShownHandler = getOnShownHandler();
            
            final Timeline animation = new Timeline();
            if (newContent != null) {
                newContent.setOpacity(0.0);
                animation.getKeyFrames().add(new KeyFrame(_transitionDuration, new KeyValue(newContent.opacityProperty(), 1.0)));
            }
            
            if (oldContent != null)
                animation.getKeyFrames().add(new KeyFrame(_transitionDuration, new KeyValue(oldContent.opacityProperty(), 0.0)));
            
            animation.setOnFinished(event -> {
                if (oldContent != null)
                    rootPane.getChildren().remove(oldContent);
                if (onShownHandler != null)
                    onShownHandler.handle(event);
            });
            
            FX.runFX(() -> {
                if (newContent != null) {
                    newContent.maxWidthProperty().bind(rootPane.widthProperty());
                    newContent.maxHeightProperty().bind(rootPane.heightProperty());
                    // Ignore exception for now - Doesn't cause any functionality problems.
                    try { rootPane.getChildren().add(newContent); } catch (Exception ignored) { }
                }
                animation.play();
            }, false);
        } else {
            if (oldContent != null)
                rootPane.getChildren().remove(oldContent);
            if (newContent != null) {
                newContent.maxWidthProperty().bind(rootPane.widthProperty());
                newContent.maxHeightProperty().bind(rootPane.heightProperty());
                rootPane.getChildren().add(newContent);
            }
        }
    }
}