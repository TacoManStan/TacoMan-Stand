package com.taco.suit_lady.view.ui.jfx.image;

import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ImagePane extends AnchorPane {

	private final ImageView wrappedImageView;

	public ImagePane() {
		this.wrappedImageView = new WrappedImageView();
		init();
	}

	public ImagePane(Image image) {
		this.wrappedImageView = new WrappedImageView(image);
		init();
	}

	public ImagePane(String url) {
		this.wrappedImageView = new WrappedImageView(url);
		init();
	}

	private void init() {
		wrappedImageView.setFocusTraversable(false);

		AnchorPane.setTopAnchor(wrappedImageView, 0.0);
		AnchorPane.setBottomAnchor(wrappedImageView, 0.0);
		AnchorPane.setLeftAnchor(wrappedImageView, 0.0);
		AnchorPane.setRightAnchor(wrappedImageView, 0.0);
		
		super.getChildren().add(wrappedImageView);
	}

	//<editor-fold desc="Properties">

	public ObjectProperty<Image> imageProperty() {
		return wrappedImageView.imageProperty();
	}

	public Image getImage() {
		return wrappedImageView.imageProperty().get();
	}

	public void setImage(Image image) {
		wrappedImageView.imageProperty().set(image);
	}

	//</editor-fold>

	//

	private static class WrappedImageView extends ImageView {

		private WrappedImageView() {
			init();
		}

		private WrappedImageView(String url) {
			super(url);
			init();
		}

		private WrappedImageView(Image image) {
			super(image);
			init();
		}

		private void init() {
			setPreserveRatio(false);
		}

		//<editor-fold desc="Implementation">

		@Override
		public double prefWidth(double height) {
			Image image = getImage();
			if (image == null)
				return minWidth(height);
			return image.getWidth();
		}

		@Override
		public double prefHeight(double height) {
			Image image = getImage();
			if (image == null)
				return minHeight(height);
			return image.getHeight();
		}

		@Override
		public double minWidth(double height) {
			return 0;
		}

		@Override
		public double minHeight(double height) {
			return 0;
		}

		@Override
		public double maxWidth(double height) {
			return 16384;
		}

		@Override
		public double maxHeight(double height) {
			return 16384;
		}

		//

		@Override
		public boolean isResizable() {
			return true;
		}

		@Override
		public void resize(double width, double height) {
			setFitWidth(width);
			setFitHeight(height);
		}

		//</editor-fold>
	}
}
