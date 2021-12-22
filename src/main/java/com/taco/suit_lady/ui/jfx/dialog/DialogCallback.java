package com.taco.suit_lady.ui.jfx.dialog;

import com.taco.suit_lady.util.tools.ObjectTools;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Callback;

import java.util.function.Supplier;

public class DialogCallback<R>
{
    
    private final Supplier<R> defaultSupplier;
    
    private Dialog<R> dialog;
    private Callback<ButtonType, R> callback;
    private final ButtonBar.ButtonData[] acceptableData;
    
    /**
     * <b>--- To Format ---</b>
     * <br><br>
     * Constructs a new {@link DialogCallback} object given the specified {@link Callback} function and optional data restrictions.
     * <br>
     * The default value is null.
     * @param callback The {@link Callback}.
     * @param acceptableData An optional array of {@link ButtonBar.ButtonData ButtonData} values restricting the functionality of this {@link DialogCallback}.
     */
    public DialogCallback(Callback<ButtonType, R> callback, ButtonBar.ButtonData... acceptableData)
    {
        this((R) null, callback, acceptableData);
    }
    
    public DialogCallback(R defaultValue, Callback<ButtonType, R> callback, ButtonBar.ButtonData... acceptableData)
    {
        this(() -> defaultValue, callback, acceptableData);
    }
    
    public DialogCallback(Supplier<R> defaultSupplier, Callback<ButtonType, R> callback, ButtonBar.ButtonData... acceptableData)
    {
        assert defaultSupplier != null;
        
        this.defaultSupplier = defaultSupplier;
        this.callback = callback;
        this.acceptableData = acceptableData;
    }
    
    /**
     * Initializes this {@link DialogCallback}.
     */
    public void init()
    {
        this.dialog = new Dialog<>();
        this.callback = this::callFX;
    }
    
    /**
     * Returns the {@link Dialog} for this {@link DialogCallback}.
     *
     * @return The {@link Dialog} for this {@link DialogCallback}.
     */
    public Dialog<R> getDialog()
    {
        return dialog;
    }
    
    /**
     * Returns the {@link Callback} for this {@link DialogCallback}.
     *
     * @return The {@link Callback} for this {@link DialogCallback}.
     */
    public Callback<ButtonType, R> getCallback()
    {
        return callback;
    }
    
    /**
     * Returns the value for this {@link DialogCallback}.
     *
     * @param param The {@link ButtonType} to be used.
     * @return The value for this {@link DialogCallback}.
     */
    public R callFX(ButtonType param)
    {
        if (param != null)
        {
            ButtonBar.ButtonData[] data = acceptableData != null && acceptableData.length > 0 ? acceptableData : new ButtonBar.ButtonData[]{ButtonBar.ButtonData.OK_DONE, ButtonBar.ButtonData.YES};
            if (param.getButtonData() != null)
                if (ObjectTools.equalsAny(param.getButtonData(), (Object[]) data))
                    return callback.call(param);
                else if (param.getButtonData() != ButtonBar.ButtonData.CANCEL_CLOSE)
                    return defaultSupplier.get();
        }
        return null;
    }
}