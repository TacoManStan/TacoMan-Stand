package com.taco.suit_lady.view.ui.ui_internal.contents;

import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Objects;
import java.util.function.BiFunction;

public abstract class ContentPane<T> extends StackPane {

	private final ObjectProperty<T> contentProperty;
	private final BiFunction<T, T, Pane> onDisplaySwitchListener;

	private Pane currentContentPane;

	public ContentPane(BiFunction<T, T, Pane> onDisplaySwitchListener) {
		this.contentProperty = new SimpleObjectProperty<>();
		this.onDisplaySwitchListener = onDisplaySwitchListener;

		this.currentContentPane = null;

		//

		this.contentProperty.addListener((observable, oldContent, newContent) -> switchContent(oldContent, newContent));
	}

	//<editor-fold desc="Properties">

	public final ObjectProperty<T> contentProperty() {
		return contentProperty;
	}

	public final T getContent() {
		return contentProperty.get();
	}

	public final void setContent(T newContent) {
		contentProperty.set(newContent);
	}

	//

	public final BiFunction<T, T, Pane> getOnDisplaySwitchListener() {
		return onDisplaySwitchListener;
	}

	//</editor-fold>

	//<editor-fold desc="Helper Methods">

	private void switchContent(T oldContent, T newContent) {
		FXTools.get().runFX(() -> {
			if (!Objects.equals(oldContent, newContent)) {
				if (currentContentPane != null) {
					getChildren().remove(currentContentPane);
					currentContentPane.prefWidthProperty().unbind();
					currentContentPane.prefHeightProperty().unbind();
				}

				Pane newContentPane = onDisplaySwitchListener.apply(oldContent, newContent);
				currentContentPane = newContentPane;
				if (newContentPane != null) {
					getChildren().add(newContentPane);
					newContentPane.prefWidthProperty().bind(widthProperty());
					newContentPane.prefHeightProperty().bind(heightProperty());
				}
			}
		}, true);
	}

	//</editor-fold>
}

/*
 * TODO:
 * 1. Remove external access to StackPane children modification.
 * 2. Add transition animation support.
 */

/*
 * NOTES:
 * 1. Should we allow the content to be bound?
 */