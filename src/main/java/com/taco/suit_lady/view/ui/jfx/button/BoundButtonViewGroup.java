package com.taco.suit_lady.view.ui.jfx.button;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class BoundButtonViewGroup<T extends ButtonViewable> extends ButtonViewGroup
{
    private final ObservableList<T> buttonViewables;
    
    public BoundButtonViewGroup(ObservableList<T> buttonViewables)
    {
        this(buttonViewables, null);
    }
    
    public BoundButtonViewGroup(ObservableList<T> buttonViewables, ReentrantLock lock)
    {
        super(lock);
        this.buttonViewables = buttonViewables;
        
        //
        
        this.buttonViewables.addListener((ListChangeListener<T>) change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(buttonViewable -> {
                    if (buttonViewable != null) {
                        ImageButton imageButton = buttonViewable.getButtonView();
                        if (imageButton != null)
                            this.buttons.add(imageButton);
                    }
                });
                
                change.getRemoved().forEach(buttonViewable -> {
                    if (buttonViewable != null) {
                        ImageButton imageButton = buttonViewable.getButtonView();
                        if (imageButton != null)
                            this.buttons.remove(imageButton);
                    }
                });
            }
        });
    }
    
    //<editor-fold desc="Properties">
    
    public ObservableList<T> buttonViewables()
    {
        return buttonViewables;
    }
    
    //</editor-fold>
    
    //
    
    public T getViewableByButton(ImageButton button)
    {
        if (button != null) {
            lock.lock();
            try {
                List<T> buttonViewables = buttonViewables();
                for (T buttonViewable: buttonViewables)
                    if (Objects.equals(button, buttonViewable.getButtonView()))
                        return buttonViewable;
            } finally {
                lock.unlock();
            }
        }
        return null;
    }
}
