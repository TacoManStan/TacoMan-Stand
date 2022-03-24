package com.taco.tacository.util.settings;

import com.taco.tacository.util.tools.fx_tools.GraphicsFX;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ColorWrapper
		implements Storable, Externalizable {

	/**
	 * Black (rgb 0, 0, 0)
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#000000;float:right;margin: 0 10px 0 0"></div><br/><br/>
	 */
	public static final Color DEFAULT = Color.color(0, 0, 0);

	//

	private final ObjectProperty<Color> colorProperty;
	private final ReadOnlyObjectWrapper<java.awt.Color> awtColorProperty;

	/**
	 * Constructs a new {@link ColorWrapper} with its {@link Color} set to the {@link #DEFAULT Default Value}.
	 */
	public ColorWrapper() {
		this(null);
	}

	/**
	 * Constructs a new {@link ColorWrapper} with its {@link Color} set to the specified value.
	 * <p>
	 * If the specified {@link Color} is null, the {@link Color} is set to the {@link #DEFAULT Default Color}.
	 * @param color The {@link Color}.
	 */
	public ColorWrapper(Color color) {
		this.colorProperty = new SimpleObjectProperty<>();
		this.awtColorProperty = new ReadOnlyObjectWrapper<>();

		this.colorProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue == null)
				setColor(DEFAULT);
		});
		this.awtColorProperty.bind(Bindings.createObjectBinding(() -> GraphicsFX.toAWTColor(getColor()), colorProperty));
		this.setColor(color);
	}

	/**
	 * Returns the color property for this {@link ColorWrapper}.
	 * @return The color property for this {@link ColorWrapper}.
	 */
	public ObjectProperty<Color> colorProperty() {
		return colorProperty;
	}

	/**
	 * Returns the {@link Color} of this {@link ColorWrapper}. Cannot be null.
	 * @return The {@link Color} of this {@link ColorWrapper}. Cannot be null.
	 */
	public Color getColor() {
		return colorProperty.get();
	}

	/**
	 * Sets the {@link Color} of this {@link ColorWrapper} to the specified value.
	 * <p>
	 * If the specified {@link Color} is null, the {@link Color} is set to the {@link #DEFAULT Default Color}.
	 * @param color The {@link Color}.
	 */
	public void setColor(Color color) {
		colorProperty().set(color);
	}

	/**
	 * Returns a read-only copy of the AWT color property for this {@link ColorWrapper}.
	 * @return A read-only copy of the AWT color property for this {@link ColorWrapper}.
	 */
	public ReadOnlyObjectProperty<java.awt.Color> awtColorProperty() {
		return awtColorProperty.getReadOnlyProperty();
	}

	/**
	 * Returns the {@link Color} of this {@link ColorWrapper} converted into a {@link java.awt.Color AWT Color}.
	 * @return The {@link Color} of this {@link ColorWrapper} converted into a {@link java.awt.Color AWT Color}.
	 */
	public java.awt.Color getAwtColor() {
		return awtColorProperty.get();
	}

	//<editor-fold desc="Interface Methods">
	
	// CHANGE-HERE

//	@Override public void xmlSave(XMLTools.Saver saver, Element parent) {
//		saver.write(parent, "red", getColor().getRed());
//		saver.write(parent, "green", getColor().getGreen());
//		saver.write(parent, "blue", getColor().getBlue());
//		saver.write(parent, "opacity", getColor().getOpacity());
//	}
//
//	@Override public void xmlLoad(XMLTools.Loader loader, String path) {
//		double red = loader.readDouble("red", path, -1);
//		double green = loader.readDouble("green", path, -1);
//		double blue = loader.readDouble("blue", path, -1);
//		double opacity = loader.readDouble("opacity", path, -1);
//		if (red >= 0 && green >= 0 && blue >= 0 && opacity >= 0)
//			setColor(Color.color(red, green, blue, opacity));
//	}
//
//	@Override public String xmlID() {
//		return "color";
//	}

	// CHANGE-HERE

//	@Override public boolean copyOf(ColorWrapper toCopy, Object... objs) {
//		Color wrapperColor = toCopy.getColor();
//		setColor(wrapperColor != null ? new Color(wrapperColor.getRed(), wrapperColor.getGreen(), wrapperColor.getBlue(), wrapperColor.getOpacity()) : null);
//		return true;
//	}

	//

	@Override public void writeExternal(ObjectOutput out) throws IOException {
		Color color = getColor();
		out.writeDouble(color.getRed());
		out.writeDouble(color.getGreen());
		out.writeDouble(color.getBlue());
		out.writeDouble(color.getOpacity());
	}

	@Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setColor(new Color(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble()));
	}
	
	//</editor-fold>
}