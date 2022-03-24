package com.taco.tacository.ui.jfx.hyperlink;

import com.taco.tacository.ui.console.ConsoleBB;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class HyperlinkNodeFX
{

	private final Object owner;

	private final HashMap<String, HyperlinkFX> hyperlinks;
	private String text;

	private final ArrayList<Node> nodes;

	/**
	 * Creates a new {@link HyperlinkNodeFX} with no {@link HyperlinkFX}, and an empty getText.
	 */
	public HyperlinkNodeFX(Object owner) {
		this(owner, "");
	}

	/**
	 * Creates a new {@code HyperlinkNodeFX} with the specified {@link HyperlinkFX} and getText.
	 * @param text       The getText that should be displayed by this {@link HyperlinkNodeFX}.
	 * @param hyperlinks The {@link HyperlinkFX}s that are to be displayed by this {@code HyperlinkNodeFX}.
	 */
	public HyperlinkNodeFX(Object owner, String text, HyperlinkFX... hyperlinks) {
		this.owner = owner;

		this.hyperlinks = new HashMap<>();
		this.text = text;
		this.nodes = new ArrayList<>();

		this.addHyperlinks(hyperlinks);
		this.reset();
	}

	/**
	 * Adds the specified {@link HyperlinkFX}s to be added to this {@link HyperlinkNodeFX}.
	 * @param hyperlinks The {@link HyperlinkFX}s to be added to this {@link HyperlinkNodeFX}.
	 */
	public final void addHyperlinks(HyperlinkFX... hyperlinks) {
		for (HyperlinkFX hyperlink : hyperlinks)
			if (HyperlinkFX.isValid(hyperlink))
				this.hyperlinks.put(hyperlink.getKey(), hyperlink);
	}

	/**
	 * Removes the specified {@link HyperlinkFX}s to be removed from this {@link HyperlinkNodeFX}.
	 * @param hyperlinks The {@link HyperlinkFX}s to be removed from this {@link HyperlinkNodeFX}.
	 */
	public final void removeHyperlinks(HyperlinkFX... hyperlinks) {
		for (HyperlinkFX hyperlink : hyperlinks)
			if (HyperlinkFX.isValid(hyperlink) && this.hyperlinks.keySet().contains(hyperlink.getKey()))
				this.hyperlinks.remove(hyperlink.getKey());
	}

	/**
	 * Removes the specified {@link HyperlinkFX} keys to be removed from this {@link HyperlinkNodeFX}.
	 * @param hyperlinkKeys The {@link HyperlinkFX} keys to be removed from this {@link HyperlinkNodeFX}.
	 */
	public final void removeHyperlinks(String... hyperlinkKeys) {
		for (String hyperlinkKey : hyperlinkKeys)
			if (hyperlinkKey != null && this.hyperlinks.keySet().contains(hyperlinkKey))
				this.hyperlinks.remove(hyperlinkKey);
	}

	/**
	 * Sets the raw getText of this {@link HyperlinkNodeFX} to the specified value.
	 * Use {@code {key_name}} to indicate where a {@link Hyperlink} should be placed.
	 * @param text The getText.
	 */
	public final void setText(String text) {
		this.text = formatText(text);
	}

	/**
	 * Appends the specified String to the end of the raw getText with no additional formatting.
	 * @param text The getText being added.
	 * @see #appendText(String, boolean)
	 */
	public final void appendText(String text) {
		this.text += text;
	}

	/**
	 * Appends the specified String to the end of the raw getText with the specified type of line break formatting.
	 * @param text      The getText being added.
	 * @param planeLine True if a new line should be added after the specified String is appended, false if an "option break" should be added.
	 * @see #appendText(String)
	 */
	public final void appendText(String text, boolean planeLine) {
		this.text += text + (planeLine ? "<br>" : "<hr>");
	}

	/**
	 * Resets this {@link HyperlinkNodeFX} to the current {@link HyperlinkFX}s that have been added to it.
	 */
	public final void reset() {
		nodes.clear();
		final String[] splits = formatText(text).split("\\{key=");
		for (int i = 0; i < splits.length; i++) {
			final String split = splits[i];
			if (i != 0) {
				final String[] textSplit = split.split("}");
				final HyperlinkFX hyperlink = hyperlinks.get(textSplit[0]);
				if (hyperlink == null)
					debug(textSplit[0]);
				else
					addNode(hyperlink.createHyperlink());
				if (textSplit.length == 2)
					addNode(new Text(textSplit[1]));
			} else
				addNode(new Text(split));
		}
	}

	private void debug(String text) {
		final Node node;
		synchronized (nodes) {
			if (!nodes.isEmpty()) {
				node = nodes.get(0);
				ConsoleBB.CONSOLE.print("Hyperlink invalid: " + text + " (" + node + ")");
			} else
				ConsoleBB.CONSOLE.print("Nodes are empty (" + owner);
		}
	}

	/**
	 * Adds the {@link Node} to this {@link HyperlinkNodeFX}.
	 * <p>
	 * Applies CSS and layout, as well as setting up the vertical translation binding of the {@link Node}.
	 * @param node The {@link Node} being added.
	 */
	private void addNode(Node node) {
		if (node != null) {
			if (node instanceof Text) {
				final Text tempText = (Text) node;
				if (tempText.getText().isEmpty())
					return;
			} else if (node instanceof Hyperlink) {
				final Hyperlink tempHyperlink = (Hyperlink) node;
				if (tempHyperlink.getText().isEmpty())
					return;
			}
			nodes.add(node);
		}
	}

	/**
	 * Returns the {@link Node Nodes} for this {@link HyperlinkNodeFX}.
	 * @return The {@link Node Nodes} for this {@link HyperlinkNodeFX}.
	 */
	public final ArrayList<Node> getNodes() {
		return nodes;
	}

	/**
	 * Formats the specified getText to be the correct value, and returns the result.
	 * @param text The getText being formatted.
	 * @return The formatted version of the specified getText, or null if the specified getText was null.
	 */
	private static String formatText(String text) {
		return text != null ? text.replace("~tab ", "&nbsp;&nbsp;&nbsp;&nbsp;") : null;
	}
}
