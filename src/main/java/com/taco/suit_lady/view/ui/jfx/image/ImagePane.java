package com.taco.suit_lady.view.ui.jfx.image;

import com.taco.suit_lady.util.ExceptionTools;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * <p>An extension of {@link AnchorPane} that displays an {@link Image} via an internally-wrapped {@link WrappedImageView ImageView}.</p>
 */
public class ImagePane extends AnchorPane
{
    /**
     * <p>The {@link ImageView} that is {@link WrappedImageView wrapped} by this {@link ImagePane}.</p>
     */
    private ImageView imageView;
    
    /**
     * <p>Refer to {@link #init(Object) Univeral Initialization Method} for details.</p>
     * <p><b>Identical to...</b></p>
     * <blockquote>
     * <ol>
     *     <li><code>new Sidebar(<u>(Image) null</u>)</code></li>
     *     <li><code>new Sidebar(<u>(String) null</u>)</code></li>
     * </ol>
     * </blockquote>
     */
    public ImagePane()
    {
        init(null);
    }
    
    /**
     * <p><b>Fully-Parameterized Constructor [1/2]</b></p>
     * <p>Constructs a new {@link ImagePane} that internally {@link WrappedImageView wraps} an {@link ImageView} containing the specified {@link Image}.</p>
     * <blockquote>Refer to <code>{@link #init(Object) Univeral Initialization Method}</code> for additional information.</blockquote>
     *
     * @param image The {@link Image} object displayed by the {@link ImageView} that is {@link WrappedImageView wrapped} by this {@link ImagePane}.
     */
    public ImagePane(@Nullable Image image)
    {
        init(image);
    }
    
    /**
     * <p><b>Fully-Parameterized Constructor [2/2]</b></p>
     * <p>Constructs a new {@link ImagePane} that internally {@link WrappedImageView wraps} an {@link ImageView} containing the {@link Image} that is {@link Image#Image(String) constructed} using the specified {@link Image#getUrl() URL}.</p>
     * <blockquote>Refer to <code>{@link #init(Object) Univeral Initialization Method}</code> for additional information.</blockquote>
     *
     * @param url The {@link String} representing the {@link Image#getUrl() URL} used to {@link Image#Image(String) construct} the {@link Image} displayed by the {@link ImageView} that is {@link WrappedImageView wrapped} by this {@link ImagePane}.
     */
    public ImagePane(@Nullable String url)
    {
        init(url);
    }
    
    /**
     * <p><b>Universal Initialization Method</b></p>
     * <p>Initializes this {@link ImagePane} based on the specified {@code input}.</p>
     * <p><hr>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link ImageView} that is wrapped by this {@link ImagePane} is not {@link ImageView#focusTraversableProperty() Focus Traversable}.</li>
     *     <li>
     *         The {@link ImageView} that is wrapped by this {@link ImagePane} is set to the same dimensions as this {@link ImagePane} object.
     *         <ol>
     *             <li>
     *                 Specifically, the {@code anchors}
     *                 — {@link AnchorPane#getTopAnchor(Node) Top}, {@link AnchorPane#getTopAnchor(Node) Bottom}, {@link AnchorPane#getTopAnchor(Node) Left}, {@link AnchorPane#getTopAnchor(Node) Right} —
     *                 are set to {@code 0}.
     *             </li>
     *         </ol>
     *     </li>
     *     <li>Finally, the {@link ImageView} that is wrapped by this {@link ImagePane} is {@link List#add(Object) added} as a {@link #getChildren() child} of this {@link ImagePane}.</li>
     * </ol>
     * <p><b>Parameter Details: Input</b></p>
     * <ol>
     *     <li>
     *         <b>Input Type</b>
     *         <ol>
     *             <li><b>Image: </b>Used as the {@link Image} to {@link ImageView#ImageView(Image) construct} the {@link ImageView} that is wrapped by this {@link ImagePane}.</li>
     *             <li><b>String: </b>Used as the {@link Image#getUrl() URL} to {@link ImageView#ImageView(String) construct} the {@link Image} that is subsequently used to {@link ImageView#ImageView(Image) construct} the {@link ImageView} that is wrapped by this {@link ImagePane}.</li>
     *             <li><b>null: </b>The fallback option that results in the {@link ImageView} that is wrapped by this {@link ImagePane} to be {@link ImageView#ImageView() empty}.</li>
     *         </ol>
     *     </li>
     *     <li>If the {@code input} passed to {@link #init(Object) this method} is anything not listed above, an {@link RuntimeException exception} is thrown.</li>
     * </ol>
     *
     * @param
     *
     * @throws RuntimeException If the specified value is of an invalid type.
     */
    private void init(Object input)
    {
        if (input == null)
            imageView = new ImageView();
        else if (input instanceof String)
            imageView = new ImageView((String) input);
        else if (input instanceof Image)
            imageView = new ImageView((Image) input);
        else
            throw ExceptionTools.ex("Input Must be an Image, a String, or null [" + input.getClass() + "]");
        
        imageView.setFocusTraversable(false);
        
        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setBottomAnchor(imageView, 0.0);
        AnchorPane.setLeftAnchor(imageView, 0.0);
        AnchorPane.setRightAnchor(imageView, 0.0);
        
        getChildren().add(imageView);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ObjectProperty property} containing the {@link Image} that is wrapped by this {@link ImagePane} instance.</p>
     *
     * @return The {@link ObjectProperty property} containing the {@link Image} that is wrapped by this {@link ImagePane} instance.
     */
    public ObjectProperty<Image> imageProperty()
    {
        return imageView.imageProperty();
    }
    
    /**
     * <p>Returns the {@link Image} that is wrapped by this {@link ImagePane} instance.</p>
     *
     * @return The {@link Image} that is wrapped by this {@link ImagePane} instance.
     *
     * @see #imageProperty()
     */
    public Image getImage()
    {
        return imageView.imageProperty().get();
    }
    
    /**
     * <p>Sets the {@link Image} that is wrapped by this {@link ImagePane} to the specified value.</p>
     *
     * @param image The value to be set as the new {@link Image} wrapped by this {@link ImagePane}.
     *
     * @see #imageProperty()
     */
    public void setImage(Image image)
    {
        imageView.imageProperty().set(image);
    }
    
    //</editor-fold>
    
    private static class WrappedImageView extends ImageView
    {
        private WrappedImageView()
        {
            super();
            init();
        }
        
        private WrappedImageView(String url)
        {
            super(url);
            init();
        }
        
        private WrappedImageView(Image image)
        {
            super(image);
            init();
        }
        
        private void init()
        {
            setPreserveRatio(false);
        }
        
        //<editor-fold desc="Implementation">
        
        @Override
        public double prefWidth(double height)
        {
            Image image = getImage();
            if (image == null)
                return minWidth(height);
            return image.getWidth();
        }
        
        @Override
        public double prefHeight(double width)
        {
            Image image = getImage();
            if (image == null)
                return minHeight(width);
            return image.getHeight();
        }
        
        @Override
        public double minWidth(double height)
        {
            return 0;
        }
        
        @Override
        public double minHeight(double width)
        {
            return 0;
        }
        
        @Override
        public double maxWidth(double height)
        {
            return 16384;
        }
        
        @Override
        public double maxHeight(double height)
        {
            return 16384;
        }
        
        //
        
        @Override
        public boolean isResizable()
        {
            return true;
        }
        
        @Override
        public void resize(double width, double height)
        {
            setFitWidth(width);
            setFitHeight(height);
        }
        
        //</editor-fold>
    }
}
