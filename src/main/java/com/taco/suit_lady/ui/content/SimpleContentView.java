package com.taco.suit_lady.ui.content;

import com.taco.suit_lady.util.UndefinedRuntimeException;
import javafx.scene.layout.Pane;

/**
 * A {@link ContentView} that displays a single {@link Pane} as its content.
 */
public class SimpleContentView<P extends Pane> extends ContentView<P> {

	public SimpleContentView(P contentPane) {
		super(contentPane);
	}

	@Override protected void onContentChange(P oldContent, P newContent) {
		throw new UndefinedRuntimeException("NYI");
	} // TODO
}
