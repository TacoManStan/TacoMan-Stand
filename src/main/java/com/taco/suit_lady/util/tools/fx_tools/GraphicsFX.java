package com.taco.suit_lady.util.tools.fx_tools;

import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class GraphicsFX {
	private GraphicsFX() {} //No instance

	/**
	 * Returns the {@link Point} relative to the {@link MouseEvent#getSource() source} in which the mouse is at for the specified {@link MouseEvent}.
	 *
	 * @param event The {@link MouseEvent}.
	 * @return The {@link Point} relative to the {@link MouseEvent#getSource() source} in which the mouse is at for the specified {@link MouseEvent}.
	 */
	public static Point getPoint(MouseEvent event) {
		return new Point((int) event.getX(), (int) event.getY());
	}

	/**
	 * Returns the {@link Point} relative to the {@link DragEvent#getSource() source} in which the mouse is at for the specified {@link DragEvent}.
	 *
	 * @param event The {@link MouseEvent}.
	 * @return The {@link Point} relative to the {@link DragEvent#getSource() source} in which the mouse is at for the specified {@link DragEvent}.
	 */
	public static Point getPoint(DragEvent event) {
		return new Point((int) event.getX(), (int) event.getY());
	}

	//

	/**
	 * Converts the specified {@link Color Color} into an {@link Color AWT Color}.
	 *
	 * @param color The {@link Color Color}.
	 * @return The converted {@link Color AWT Color}.
	 */
	public static java.awt.Color toAWTColor(Color color) {
		if (color != null) {
			final int red = (int) (color.getRed() * 255);
			final int green = (int) (color.getGreen() * 255);
			final int blue = (int) (color.getBlue() * 255);
			final int alpha = (int) (color.getOpacity() * 255);
			return new java.awt.Color(red, green, blue, alpha);
		}
		return java.awt.Color.BLACK;
	}

	/**
	 * Converts the specified {@link Color Color} into an {@link Color AWT Color}.
	 *
	 * @param color The {@link Color Color}.
	 * @return The converted {@link Color AWT Color}.
	 */
	public static Color toFXColor(java.awt.Color color) {
		if (color != null) {
			final double red = color.getRed() / 255.0;
			final double green = color.getGreen() / 255.0;
			final double blue = color.getBlue() / 255.0;
			final double opacity = color.getAlpha() / 255.0;
			return new Color(red, green, blue, opacity);
		}
		return Color.BLACK;
	}

	//

	/**
	 * Creates and then returns a deep copy of the specified {@link BufferedImage}.
	 * <p>
	 * A deep copy is defined as an object that has no references to the original object it has been copied from.
	 * In other words, a deep copy can be modified without the risk of accidentally modifying the original.
	 * <p>
	 * Keep in mind that the cost of creating a deep copy is much higher than that of a shallow copy.
	 *
	 * @param image The {@code BufferedImage} being copied.
	 * @return A deep copy of the specified {@code BufferedImage}.
	 */
	public static @NotNull BufferedImage deepCopy(@NotNull BufferedImage image) {
		ColorModel color_model = image.getColorModel();
		return new BufferedImage(color_model, image.copyData(null), color_model.isAlphaPremultiplied(), null);
	}
	
	public static WritableImage asImage(@NotNull Parent node) {
		return node.snapshot(new SnapshotParameters(), null);
	}
}