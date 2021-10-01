package com.taco.suit_lady.view.ui.jfx.button;

import com.taco.suit_lady.util.TB;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

// TODO - Fix synchronization issues with selecting an element as the elements list is being modified
public class ButtonViewGroup
{
    
    protected final ReentrantLock lock;
    
    protected final ListProperty<ImageButton> buttons;
    private final ReadOnlyObjectWrapper<ImageButton> selectedButtonProperty;
    
    public ButtonViewGroup()
    {
        this(null);
    }
    
    public ButtonViewGroup(ReentrantLock lock)
    {
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.buttons = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.selectedButtonProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.buttons.addListener((ListChangeListener<ImageButton>) change -> {
            this.lock.lock();
            try
            {
                while (change.next())
                {
                    change.getAddedSubList().forEach(button -> button.setButtonGroup(this));
                    change.getRemoved().forEach(button -> button.setButtonGroup(null));
                }
            }
            finally
            {
                this.lock.unlock();
            }
        });
        
        this.selectedButtonProperty().addListener((observable, oldButton, newButton) -> {
            if (oldButton != null)
                oldButton.setSelected(false);
            if (newButton != null)
                newButton.setSelected(true);
        });
    }
    
    //<editor-fold desc="Properties">
    
    protected ObservableList<ImageButton> buttonViews()
    {
        return buttons;
    }
    
    //
    
    public ReadOnlyObjectProperty<ImageButton> selectedButtonProperty()
    {
        return selectedButtonProperty.getReadOnlyProperty();
    }
    
    public ImageButton getSelectedButton()
    {
        return selectedButtonProperty.get();
    }
    
    public boolean isButtonSelected(ImageButton button)
    {
        return button != null && Objects.equals(button, getSelectedButton());
    }
    
    public ImageButton setSelectedButton(ImageButton button)
    {
        ImageButton oldSelection = getSelectedButton();
        selectedButtonProperty.set(button);
        return oldSelection;
    }
    
    public ImageButton clearSelection()
    {
        return setSelectedButton(null);
    }
    
    public boolean clearSelection(ImageButton... buttons)
    {
        if (TB.general().equalsAny(getSelectedButton(), (Object[]) buttons))
        {
            setSelectedButton(null);
            return true;
        }
        return false;
    }
    
    public ImageButton selectFirst()
    {
        try
        {
            return setSelectedButton(buttonViews().get(0));
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }
    
    public ImageButton selectLast()
    {
        try
        {
            return setSelectedButton(buttonViews().get(buttonViews().size() - 1));
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }
    
    //</editor-fold>
}
