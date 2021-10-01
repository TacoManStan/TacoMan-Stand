package com.taco.suit_lady.view.ui.jfx.dialog;

import com.taco.suit_lady.util.TB;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Callback;

public class DialogCallback<T> {

	private T defaultValue;
	private Callbackable<T> callbackable;

	private Dialog<T> dialog;
	private Callback<ButtonType, T> callback;
	private ButtonBar.ButtonData[] acceptableData;

	/**
	 * Constructs a new {@link DialogCallback} with the specified {@link Callbackable}.
	 */
	public DialogCallback(T defaultValue, Callbackable<T> callbackable, ButtonBar.ButtonData... acceptableData) {
		assert defaultValue != null;
		this.defaultValue = defaultValue;
		this.callbackable = callbackable;
		this.acceptableData = acceptableData;
	}

	/**
	 * Initializes this {@link DialogCallback}.
	 */
	public void init() {
		this.dialog = new Dialog<>();
		this.callback = this::callFX;
	}

	/**
	 * Returns the {@link Dialog} for this {@link DialogCallback}.
	 * @return The {@link Dialog} for this {@link DialogCallback}.
	 */
	public Dialog<T> getDialog() { return dialog; }

	/**
	 * Returns the {@link Callback} for this {@link DialogCallback}.
	 * @return The {@link Callback} for this {@link DialogCallback}.
	 */
	public Callback<ButtonType, T> getCallback() { return callback; }

	/**
	 * Returns the value for this {@link DialogCallback}.
	 * @param param The {@link ButtonType} to be used.
	 * @return The value for this {@link DialogCallback}.
	 */
	public T callFX(ButtonType param) {
		if (param != null) {
			ButtonBar.ButtonData[] datas = acceptableData != null && acceptableData.length > 0 ? acceptableData : new ButtonBar.ButtonData[]{ButtonBar.ButtonData.OK_DONE, ButtonBar.ButtonData.YES};
			if (param.getButtonData() != null)
				if (TB.comparing().equalsAny(param.getButtonData(), (Object[]) datas))
					return callbackable.callFX(param);
				else if (param.getButtonData() != ButtonBar.ButtonData.CANCEL_CLOSE)
					return defaultValue;
		}
		return null;
	}

	/**
	 * Designed so lambdas can be used with {@link DialogCallback}.
	 * @param <T> The type of element returned by the {@link DialogCallback}.
	 */
	public interface Callbackable<T> {
		T callFX(ButtonType param);
	}
}