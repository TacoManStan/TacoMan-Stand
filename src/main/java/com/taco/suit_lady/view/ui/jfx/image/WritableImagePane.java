package com.taco.suit_lady.view.ui.jfx.image;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.image.WritableImage;

public class WritableImagePane extends ImagePane {

	private final ObjectBinding<WritableImage> writableImageBinding;

	public WritableImagePane() {
		super();
		this.writableImageBinding = Bindings.createObjectBinding(() -> (WritableImage) getImage(), imageProperty());
		init();
	}

	public WritableImagePane(WritableImage image) {
		super(image);
		this.writableImageBinding = Bindings.createObjectBinding(() -> (WritableImage) getImage(), imageProperty());
		init();
	}

	private void init() {
		imageProperty().addListener((observable, oldImage, newImage) -> {
			if (newImage != null && !(newImage instanceof WritableImage))
				throw new UnsupportedOperationException("Attempting to set writable image to a non-writable image type.");
		});
	}

	//<editor-fold desc="Properties">

	public ObjectBinding<WritableImage> writableImageBinding() {
		return writableImageBinding;
	}

	public WritableImage getWritableImage() {
		return writableImageBinding.get();
	}

	//</editor-fold>
}
