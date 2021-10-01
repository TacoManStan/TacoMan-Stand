package com.taco.suit_lady.util.settings;

import com.taco.suit_lady.util.ExceptionTools;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

public abstract class SavableSetting<T, Z extends Property<T>>
		implements ObservableValue<T>, Property<T>, Storable
{

	private Z observable;

	private final String settingName;
	private final StringProperty descriptionProperty;
	private final ReadOnlyBooleanWrapper savingEnabledProperty;

	protected final Z property;

	public SavableSetting(Z observable, T defaultValue) {
		this(null, "", defaultValue);
		if (observable != null)
			propertyBinding(observable);
	}

	public SavableSetting(String settingName, String description, T defaultValue) {
		this.observable = null;

		this.settingName = settingName;
		this.descriptionProperty = new SimpleStringProperty(description);
		this.savingEnabledProperty = new ReadOnlyBooleanWrapper();

		this.property = createProperty();

		this.setValue(defaultValue);
		this.init();
	}

	private void init() {
		descriptionProperty.addListener((observable, oldValue, newValue) -> {
			if (!descriptionProperty.isBound())
				if (newValue == null)
					setDescription("");
				else if (newValue.contains("{v}"))
					setDescription(newValue.replace("{v}", "" + getValue()));
		});
		setDescription(getDescription());
	}

	//<editor-fold desc="Properties">

	/**
	 * Returns the name of this {@code SavableSetting} object.
	 *
	 * @return The name of this {@code SavableSetting} object.
	 */
	public final String getSettingName() {
		return settingName;
	}

	//

	/**
	 * Returns the description property for this {@link SavableSetting}.
	 * <p>
	 * The description property is an optional value that allows you to show a description to users when the setting is displayed in a GUI.
	 *
	 * @return The description property for this {@link SavableSetting}.
	 */
	public final StringProperty descriptionProperty() {
		return descriptionProperty;
	}

	/**
	 * Returns the description of this {@link SavableSetting}.
	 *
	 * @return The description of this {@link SavableSetting}.
	 */
	public final String getDescription() {
		return descriptionProperty.get();
	}

	/**
	 * Sets the description of this {@link SavableSetting} to the specified value.
	 *
	 * @param description The description.
	 */
	public final void setDescription(String description) {
		descriptionProperty.set(description);
	}

	//

	public final ReadOnlyBooleanProperty savingEnabledProperty() {
		return savingEnabledProperty.getReadOnlyProperty();
	}

	/**
	 * Checks if saving/loading is enabled for this {@link SavableSetting}.
	 *
	 * @return True if saving/loading is enabled for this {@link SavableSetting}, false otherwise.
	 */
	public final boolean isSavingEnabled() {
		return savingEnabledProperty.get();
	}

	/**
	 * Turns saving/loading on or off for this {@link SavableSetting}.
	 *
	 * @param savingEnabled True if saving should be enabled, false otherwise.
	 */
	protected final void setSavingEnabled(boolean savingEnabled) {
		savingEnabledProperty.set(savingEnabled);
	}

	//</editor-fold>

	//

	/**
	 * This method should be used when a game setting is being displayed as an option.
	 *
	 * @param observable The {@link Z observable} that is being observed.
	 * @throws RuntimeException If {@link Z observable} has already been set.
	 */
	public void propertyBinding(Z observable) {
		if (this.observable != null)
			throw ExceptionTools.ex("The observing property has already been set.");
		this.observable = observable;
		bindBidirectional(this.observable);
	}

	/**
	 * Unbinds (closes) this {@link SavableSetting} from its observing property.
	 * If the {@link #propertyBinding(Property) observing property} is null, this method does nothing.
	 * <p>
	 * This method should always be called if the {@link #propertyBinding(Property) observing property} has been set (non-null).
	 */
	public void dispose() {
		if (observable != null)
			unbindBidirectional(observable);
		observable = null;
	}

	//

	protected abstract Z createProperty();

	public abstract boolean createBinding(Node node);

	//

	//<editor-fold desc="Interface Implementation">

	@Override public void addListener(InvalidationListener listener) {
		property.addListener(listener);
	}

	@Override public void removeListener(InvalidationListener listener) {
		property.addListener(listener);
	}

	@Override public void addListener(ChangeListener<? super T> listener) {
		property.addListener(listener);
	}

	@Override public void removeListener(ChangeListener<? super T> listener) {
		property.addListener(listener);
	}

	//

	@Override public T getValue() {
		return property.getValue();
	}

	public Class<T> getValueClass() {
		return (Class<T>) getValue().getClass();
	}

	@Override public void setValue(T value) {
		property.setValue(value);
	}

	//

	@Override public void bind(ObservableValue<? extends T> observable) {
		property.bind(observable);
	}

	@Override public void unbind() {
		property.unbind();
	}

	@Override public boolean isBound() {
		return property.isBound();
	}

	@Override public void bindBidirectional(Property<T> other) {
		property.bindBidirectional(other);
	}

	@Override public void unbindBidirectional(Property<T> other) {
		property.unbindBidirectional(other);
	}

	@Override public Object getBean() {
		return property.getBean();
	}

	@Override public String getName() {
		return property.getName();
	}

	//</editor-fold>

	//<editor-fold desc="Equals/Hash Code">

	/**
	 * Two {@link SavableSetting settings} are equal if and only if their names are the same (case sensitive).
	 *
	 * @param o The {@link Object} being compared.
	 * @return True if the specified {@link Object} is a {@link SavableSetting setting} and if its name is equal to this {@link SavableSetting setting's} name.
	 */
	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		else if (o == null || getClass() != o.getClass())
			return false;
		SavableSetting<?, ?> that = (SavableSetting<?, ?>) o;
		if (settingName != null && that.settingName != null)
			return settingName.equals(that.settingName);
		return super.equals(o);
	}

	@Override public int hashCode() {
		if (settingName != null)
			return settingName.hashCode();
		return super.hashCode();
	}

	//</editor-fold>
}
