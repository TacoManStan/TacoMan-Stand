package com.taco.tacository.ui.content;

import com.taco.tacository.ui.jfx.components.ImagePane;
import com.taco.tacository.util.UndefinedRuntimeException;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * A {@link ContentView} that displays a {@link ImagePane} as its content.
 */
public class PaintableContentView extends ContentView<ImagePane> {

	public PaintableContentView() {
		this(new ImagePane());
	}

	public PaintableContentView(WritableImage image) {
		this(new ImagePane(image));
	}

	public PaintableContentView(ImagePane content) {
		super(content);
		this.init();
	}

	@Override
	protected void onContentChange(ImagePane oldContent, ImagePane newContent) {
		throw new UndefinedRuntimeException("NYI");
	} // TODO

	private void init() {
		this.getContent().setRequireWritableContent(true);
	}

	//<editor-fold desc="Properties">

	/**
	 * Returns the {@link WritableImage} being displayed by this {@code PaintableContentView}.
	 * <p>
	 * Calling this method is identical to calling {@link ImagePane#getWritableImage() getContent().getWritableImage()}.
	 *
	 * @return The {@code WritableImage} being displayed by this {@code PaintableContentView}.
	 */
	public final WritableImage getImage() {
		return getContent().getWritableImage();
	}

	/**
	 * Sets the {@link WritableImage} to be displayed by this {@code PaintableContentView} to the specified value.
	 * <p>
	 * Calling this method is identical to calling {@link ImagePane#setImage(Image) getContent().getWritableImage()}.
	 *
	 * @param image The {@code WritableImage} to be displayed by this {@code PaintableContentView}.
	 */
	public final void setImage(WritableImage image) {
		getContent().setImage(image);
	}

	//

	/**
	 * Returns the {@link PixelWriter} for this {@code PaintableContentView}.
	 * <p>
	 * Calling this method is identical to calling {@link WritableImage#getPixelWriter() getImage().getPixelWriter()}.
	 *
	 * @return The {@link PixelWriter} for this {@code PaintableContentView}.
	 */
	public final PixelWriter getPixelWriter() {
		WritableImage image = getImage();
		if (image != null)
			return image.getPixelWriter();
		throw new NullPointerException("Image cannot be null when retrieving PixelWriter.");
	}

	//</editor-fold>

	//

	public interface Painter {

	}
}
