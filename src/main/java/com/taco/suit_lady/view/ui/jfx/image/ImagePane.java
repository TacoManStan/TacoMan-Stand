package com.taco.suit_lady.view.ui.jfx.image;

import com.taco.suit_lady.util.ExceptionTools;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.WritableObjectValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;
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
    
    private ObjectBinding<WritableImage> writableImageBinding;
    private BooleanBinding isWritableBinding;
    private BooleanProperty requiresWritableContentProperty;
    
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
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
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
     *     <li>Use as Writable ImagePane: Refer to <code><i>{@link #initWritableProperties()}</i></code>.</li>
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
     *     <li>Throws an {@link RuntimeException exception} if the type of {@code input} passed to {@link #init(Object) this method} is not listed above.</li>
     * </ol>
     *
     * @param
     *
     * @throws RuntimeException If the specified value is of an invalid type.
     */
    void init(@Nullable Object input)
    {
        if (input == null)
            this.imageView = new ImageView();
        else if (input instanceof String)
            this.imageView = new ImageView((String) input);
        else if (input instanceof Image)
            this.imageView = new ImageView((Image) input);
        else
            throw ExceptionTools.ex("Input Must be an Image, a String, or null [" + input.getClass() + "]");
        
        this.imageView.setFocusTraversable(false);
        
        AnchorPane.setTopAnchor(this.imageView, 0.0);
        AnchorPane.setBottomAnchor(this.imageView, 0.0);
        AnchorPane.setLeftAnchor(this.imageView, 0.0);
        AnchorPane.setRightAnchor(this.imageView, 0.0);
        
        this.getChildren().add(this.imageView);
        
        this.initWritableProperties();
    }
    
    /**
     * <p>Initializes properties that allow this {@link ImagePane} to be {@link WritableImage writable}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>To construct an {@link ImagePane} that must be {@link WritableImage writable}, refer to <code><i>{@link #requiresWritableContentProperty()}</i></code>.</li>
     *     <li>If the {@link #getImage() Image} displayed by this {@link ImagePane} is a {@link WritableImage}, the {@link #writableImageBinding() Writable Image Binding} can be used to easily access it.</li>
     *     <li>If the {@link #getImage() Image} displayed by this {@link ImagePane} is <i>not</i> a {@link WritableImage}, the {@link #writableImageBinding() WritableImage Binding} will contain {@code null}.</li>
     *     <li>The {@link #isWritableBinding() Is Writable Binding} is a convenience {@link BooleanBinding binding} that reflects if the {@link #getImage() contents} of this {@link ImagePane} are currently {@link WritableImage writable}.</li>
     * </ol>
     */
    private void initWritableProperties()
    {
        // Throw exception if this ImagePane is set to Require Writable but contains an Image that is not a WritableImage.
        this.imageProperty().addListener((observable, oldImage, newImage) -> {
            if (requiresWritableContent() && newImage != null && !(newImage instanceof WritableImage))
                throw new UnsupportedOperationException("Attempting to set writable image to a non-writable image type.");
        });
        
        this.requiresWritableContentProperty = new SimpleBooleanProperty(false);
        this.writableImageBinding = Bindings.createObjectBinding(
                () -> {
                    final Image image = getImage();
                    if (isWritable())
                        return (WritableImage) image;
                    return null;
                }, imageProperty(), requiresWritableContentProperty());
        this.isWritableBinding = Bindings.createBooleanBinding(
                () -> getImage() instanceof WritableImage,
                imageProperty(), requiresWritableContentProperty()
        );
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ObjectProperty property} containing the {@link Image} that is displayed by the {@link ImageView} that is {@link WrappedImageView wrapped} by this {@link ImagePane}.</p>
     *
     * @return The {@link ObjectProperty property} containing the {@link Image} that is displayed by the {@link ImageView} that is {@link WrappedImageView wrapped} by this {@link ImagePane}.
     */
    public @NotNull ObjectProperty<Image> imageProperty()
    {
        return imageView.imageProperty();
    }
    
    /**
     * <p>Returns the {@link Image} that is displayed by the {@link ImageView} that is {@link WrappedImageView wrapped} by this {@link ImagePane}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>this<b>.</b>{@link #imageView}<b>.</b>{@link ImageView#imageProperty() imageProperty()}<b>.</b>{@link ObservableObjectValue#get() get()}</code></i></blockquote>
     *
     * @return The {@link Image} that is displayed by the {@link ImageView} that is {@link WrappedImageView wrapped} by this {@link ImagePane}.
     *
     * @see #imageProperty()
     */
    public @Nullable Image getImage()
    {
        return imageView.imageProperty().get();
    }
    
    /**
     * <p>Sets the {@link Image} that is displayed by the {@link ImageView} that is {@link WrappedImageView wrapped} by this {@link ImagePane} to the specified value.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>this<b>.</b>{@link #imageView}<b>.</b>{@link ImageView#imageProperty() imageProperty()}<b>.</b>{@link WritableObjectValue#set(Object) set}<b>(</b>image<b>)</b></code></i></blockquote>
     *
     * @param image The {@link Image} to be displayed by the {@link ImageView} that is {@link WrappedImageView wrapped} by this {@link ImagePane}.
     *
     * @see #imageProperty()
     */
    public void setImage(@Nullable Image image)
    {
        imageView.imageProperty().set(image);
    }
    
    /**
     * <p>Returns an {@link ObjectBinding} bound to the {@link #getImage() Image} displayed by this {@link ImagePane}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the {@link #getImage() Image} displayed by this {@link ImagePane} is not {@link WritableImage writable}, the {@link ObjectBinding binmding} returned by {@link #writableImageBinding() this method} will {@link ObjectBinding#get() contain} {@code null}.</li>
     * </ol>
     *
     * @return An {@link ObjectBinding} bound to the {@link #getImage() Image} displayed by this {@link ImagePane}.
     */
    public ObjectBinding<WritableImage> writableImageBinding()
    {
        return writableImageBinding;
    }
    
    /**
     * <p>Returns the {@link #getImage() Image} displayed by this {@link ImagePane}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Returns {@code null} if the {@link #getImage() Image} displayed by this {@link ImagePane} is not {@link WritableImage writable}.</li>
     * </ol>
     * <p>Refer to <code><i>{@link #writableImageBinding()}</i></code> for additional information.</p>
     *
     * @return The {@link #getImage() Image} displayed by this {@link ImagePane}. Returns {@code null} if the {@link #getImage() Image} displayed by this {@link ImagePane} is not {@link WritableImage writable}.
     *
     * @see #writableImageBinding()
     * @see #isWritableBinding()
     */
    public WritableImage getWritableImage()
    {
        return writableImageBinding.get();
    }
    
    /**
     * <p>Returns a {@link BooleanBinding} that reflects if this {@link ImagePane} is currently {@link WritableImage writable} or not.</p>
     *
     * @return A {@link BooleanBinding} that reflects if this {@link ImagePane} is currently {@link WritableImage writable} or not.
     */
    public BooleanBinding isWritableBinding()
    {
        return isWritableBinding;
    }
    
    /**
     * <p>Checks if this {@link ImagePane} is currently {@link WritableImage writable} or not.</p>
     *
     * @return True if this {@link ImagePane} is currently {@link WritableImage writable}, false if it is not.
     *
     * @see #isWritableBinding()
     */
    public boolean isWritable()
    {
        return isWritableBinding.get();
    }
    
    /**
     * <p>A {@link BooleanProperty} defining whether this {@link ImagePane} is required to be {@link WritableImage writable} or not.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>By default, {@link ImagePane ImagePanes} are <i>not</i> required to be {@link WritableImage writable}.</li>
     *     <li>Setting this {@link ImagePane} to {@link #setRequireWritableContent(boolean) Require Writable} will cause an {@link RuntimeException exception} to be thrown if its {@link #getImage() content} is ever changed to a non-writable {@link Image} type.</li>
     *     <li>Setting this {@link ImagePane} to {@link #setRequireWritableContent(boolean) Require Writable} still allows its {@link #getImage() content} to be {@code null}.</li>
     * </ol>
     * <p>Refer to the {@link #init(Object) Universal Initialization Method} for additional information.</p>
     *
     * @return A {@link BooleanProperty} defining whether this {@link ImagePane} is required to be {@link WritableImage writable} or not.
     */
    public BooleanProperty requiresWritableContentProperty()
    {
        return requiresWritableContentProperty;
    }
    
    /**
     * <p>Checks if this {@link ImagePane} has been set to require its {@link #getImage() content} to be {@link WritableImage writable} or not.</p>
     * <blockquote>Refer to <code><i>{@link #requiresWritableContentProperty()}</i></code> for additional information.</blockquote>
     *
     * @return True if this {@link ImagePane} has been set to require its {@link #getImage() content} to be {@link WritableImage writable}, false if it has not.
     */
    public boolean requiresWritableContent()
    {
        return requiresWritableContentProperty.get();
    }
    
    /**
     * <p>Sets the {@link #requiresWritableContent() Requires Writable} flag for this {@link ImagePane} to the specified value.</p>
     * <blockquote>Refer to <code><i>{@link #requiresWritableContentProperty()}</i></code> for additional information.</blockquote>
     *
     * @param requireWritableContent True if this {@link ImagePane} should require its {@link #getImage() content} to be {@link WritableImage writable}, false if it should not.
     */
    public void setRequireWritableContent(boolean requireWritableContent)
    {
        requiresWritableContentProperty.set(requireWritableContent);
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
