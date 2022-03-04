package com.taco.suit_lady.ui.jfx.dialog;

import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.tools.fx_tools.DialogsFX;
import com.taco.suit_lady.ui.ui_internal.controllers.Controller;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class DialogController<R>
		extends Controller
{

	private R value;
	private final ReadOnlyObjectWrapper<DialogCallback<R>> callbackProperty;

	private final String title;
	private final String message;
	private final double spacing;
	private final ButtonType[] buttonTypes;

	/**
	 * Constructs a new {@link DialogController}.
	 */
	public DialogController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
		this(weaver, ctx, null, null, 0.0);
	}

	/**
	 * Constructs a new {@link DialogController} given the specified values.
	 *
	 * @param title       The title.
	 * @param buttonTypes The {@link ButtonType ButtonTypes}.
	 */
	public DialogController(FxWeaver weaver, ConfigurableApplicationContext ctx, String title, ButtonType... buttonTypes) {
		this(weaver, ctx, title, null, 0.0, buttonTypes);
	}

	/**
	 * Constructs a new {@link DialogController} given the specified values.
	 *
	 * @param title       The title.
	 * @param message     The message.
	 * @param spacing     The spacing.
	 * @param buttonTypes The {@link ButtonType ButtonTypes}.
	 */
	public DialogController(FxWeaver weaver, ConfigurableApplicationContext ctx, String title, String message, double spacing, ButtonType... buttonTypes) {
		super(weaver, ctx);
		
		this.value = null;
		this.callbackProperty = new ReadOnlyObjectWrapper<>(createCallback());

		this.title = title;
		this.message = message;
		this.spacing = spacing;
		this.buttonTypes = setButtonTypes(buttonTypes);
	}

	//<editor-fold desc="Properties">

	/**
	 * Returns the callback property for this {@link DialogController}.
	 *
	 * @return The callback property for this {@link DialogController}.
	 */
	public ReadOnlyObjectProperty<DialogCallback<R>> callbackProperty() {
		return callbackProperty.getReadOnlyProperty();
	}

	/**
	 * Returns the {@link DialogCallback} for this {@link DialogController}. See {@link #createCallback()} for more information.
	 *
	 * @return The {@link DialogCallback} for this {@link DialogController}.
	 *
	 * @see #createCallback()
	 */
	public DialogCallback<R> getCallback() {
		return callbackProperty.get();
	}

	//

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public double getSpacing() {
		return spacing;
	}

	//

	public ButtonType[] getButtonTypes() {
		return buttonTypes;
	}

	/**
	 * Sets the {@link ButtonType Button Types}.
	 * <p>
	 * Returns {@link DialogsFX#DONE} if the specified array is null or empty.
	 *
	 * @param buttonTypes The array of accepted {@link ButtonType ButtonTypes}.
	 * @return The {@link ButtonType ButtonTypes}.I
	 *
	 * @throws NullPointerException If any of the specified {@link ButtonType Button Types} are null.
	 */
	private ButtonType[] setButtonTypes(ButtonType... buttonTypes) {
		if (buttonTypes != null && buttonTypes.length > 0)
			if (!A.containsNull(buttonTypes))
				return buttonTypes;
			else
				throw new NullPointerException("No ButtonType can be null.");
		return DialogsFX.DONE_CANCEL;
	}

	//</editor-fold>

	//

	//<editor-fold desc="Showing">

	/**
	 * Shows a {@link Dialog} for this {@link DialogController} and then returns the value returned by the {@link #getCallback() callback result}.
	 *
	 * @return The value returned by the {@link #getCallback() callback result} created when the {@link Dialog} is closed.
	 */
	public final R show() {
		return show(true);
	}

	/**
	 * Re-shows a {@link Dialog} for this {@link DialogController} and then returns the value returned by the {@link #getCallback() callback result}.
	 *
	 * @return The value returned by the {@link #getCallback() callback result} created when the {@link Dialog} is closed.
	 */
	public final R reshow() {
		return show(false);
	}

	/**
	 * Shows a {@link Dialog} for this {@link DialogController} and then returns the value returned by the {@link #getCallback() callback result}.
	 *
	 * @param launch True if this {@link DialogController} should be launched before being displayed in the {@link Dialog}, false otherwise.
	 * @return The value returned by the {@link #getCallback() callback result} created when the {@link Dialog} is closed.
	 */
	protected R show(boolean launch) {
		value = DialogsFX.showControllableDialog(title, message, spacing, buttonTypes, launch, this);
		onClose();
		return value;
	}

	//</editor-fold>

	//<editor-fold desc="Value">

	/**
	 * Returns the value for this {@link DialogController}.
	 *
	 * @return The value for this {@link DialogController}.
	 */
	public R value() {
		return value;
	}

	/**
	 * Loads the value for this {@link DialogController} from the GUI components.
	 *
	 * @return The value that was loaded.
	 *
	 * @see #reloadValue()
	 */
	protected abstract R loadValue();

	/**
	 * Loads the value for this {@link DialogController} from the GUI components and then sets the value to the loaded value.
	 *
	 * @return The value that was loaded.
	 */
	public final R reloadValue() {
		return value = loadValue();
	}

	/**
	 * Returns the default value that is returned when the {@link Dialog} is closed or cancelled. Null by default.
	 *
	 * @return The default value that is returned when the {@link Dialog} is closed or cancelled. Null by default.
	 */
	protected R defaultValue() {
		return null;
	}

	//</editor-fold>

	/**
	 * Generates and then returns the {@link DialogCallback} for this {@link DialogController}.
	 * <p>
	 * The {@link DialogCallback} is used to display this {@link DialogController} in a
	 * {@link DialogsFX#showControllableDialog(String, String, double, ButtonType[], boolean, DialogController) dialog}.
	 *
	 * @return The created {@link DialogCallback}.
	 */
	protected DialogCallback<R> createCallback() {
		return new DialogCallback<>(defaultValue(), buttonType -> loadValue());
	}

	/**
	 * Executed directly after the {@link Dialog} is closed.
	 */
	public abstract void onClose();
}
