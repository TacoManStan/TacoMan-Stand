package com.taco.suit_lady.view.ui.content;

import com.taco.suit_lady.uncategorized.UndefinedRuntimeException;
import com.taco.suit_lady.view.ui.jfx.image.WritableImagePane;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * A {@link ContentView} that displays a {@link WritableImagePane} as its content.
 */
public class PaintableContentView extends ContentView<WritableImagePane> {

	public PaintableContentView() {
		this(new WritableImagePane());
	}

	public PaintableContentView(WritableImage image) {
		this(new WritableImagePane(image));
	}

	public PaintableContentView(WritableImagePane content) {
		super(content);
		init();
	}

	@Override
	protected void onContentChange(WritableImagePane oldContent, WritableImagePane newContent) {
		throw new UndefinedRuntimeException("NYI");
	} // TODO

	private void init() {

	}

	//<editor-fold desc="Properties">

	/**
	 * Returns the {@link WritableImage} being displayed by this {@code PaintableContentView}.
	 * <p>
	 * Calling this method is identical to calling {@link WritableImagePane#getWritableImage() getContent().getWritableImage()}.
	 *
	 * @return The {@code WritableImage} being displayed by this {@code PaintableContentView}.
	 */
	public final WritableImage getImage() {
		return getContent().getWritableImage();
	}

	/**
	 * Sets the {@link WritableImage} to be displayed by this {@code PaintableContentView} to the specified value.
	 * <p>
	 * Calling this method is identical to calling {@link WritableImagePane#setImage(Image) getContent().getWritableImage()}.
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
