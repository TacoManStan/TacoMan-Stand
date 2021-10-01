package com.taco.suit_lady.view.ui.jfx.fxtools;

import com.taco.suit_lady.util.EnumTools;
import com.taco.suit_lady.util.StringTools;
import com.taco.suit_lady.util.TB;
import com.taco.suit_lady.view.ui.jfx.dialog.DialogCallback;
import com.taco.suit_lady.view.ui.jfx.dialog.DialogController;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;

import java.util.ArrayList;
import java.util.Optional;

/**
 * //TODO
 */
public class FXDialogTools
{
    private FXDialogTools() { } //No instance
    
    //<editor-fold desc="Button Types">
    
    /**
     * Allows the user to select {@code Ok} only.
     */
    public static final ButtonType[] OK = {new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE)};
    
    /**
     * Allows the user to select {@code Done} only.
     */
    public static final ButtonType[] DONE = {new ButtonType("Done", ButtonBar.ButtonData.OK_DONE)};
    
    /**
     * Allows the user to select either {@code Ok} or {@code Cancel}.
     */
    public static final ButtonType[] DONE_CANCEL = {new ButtonType("Done", ButtonBar.ButtonData.OK_DONE), new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)};
    
    /**
     * Allows the user to select either {@code Yes} or {@code No}.
     */
    public static final ButtonType[] YES_NO = {new ButtonType("Yes", ButtonBar.ButtonData.YES), new ButtonType("No", ButtonBar.ButtonData.NO)};
    
    /**
     * Allows the user to select either {@code Yes}, {@code No}, or {@code Cancel}
     */
    public static final ButtonType[] YES_NO_CANCEL = {
            new ButtonType("Yes", ButtonBar.ButtonData.YES),
            new ButtonType("No", ButtonBar.ButtonData.NO), new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
    };
    
    //</editor-fold>
    
    /**
     * The default number that is returned by Number dialogs that represent the equivalent to "null".
     */
    public static final int NULL_NUMBER = 64486486;
    
    /**
     * The default spacing value for all {@link Dialog Dialogs}. {@code (3.0)}
     */
    protected static final double DEFAULT_SPACING = 3.0;
    
    private static final int DEFAULT_WIDTH = 425;
    private static final int DEFAULT_HEIGHT = 200;
    
    private static boolean canAdd = true;
    private static final Object dialogs_lock = new Object();
    private static ArrayList<Dialog> dialogs = new ArrayList<>();
    
    /**
     * Shows a {@link Dialog} with the specified getText displayed.
     * <p>
     * A {@link WebView} is used to display the specified getText.
     * <p>
     * <b>Can be called off of the FX thread.</b>
     *
     * @param text    The text to display for the user. Null to use the title as the text.
     * @param message The text to be displayed.
     */
    public static void showInfoDialog(String message, String text)
    {
        showInfoDialog(null, message, text);
    }
    
    /**
     * Shows a {@link Dialog} with the specified text displayed.
     * <p>
     * A {@link WebView} is used to display the specified text.
     * <p>
     * <b>Can be called off of the FX thread.</b>
     *
     * @param title   The title of the dialog window. Null for no title.
     * @param message The message to be displayed on the dialog window.
     * @param text    The text to be displayed.
     */
    public static void showInfoDialog(String title, String message, String text)
    {
        FXTools.get().runFX(() -> {
            WebView webView = new WebView();
            webView.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            webView.setMinSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            webView.getEngine().loadContent(text);
            showDialog(title, message, webView, null, DEFAULT_SPACING, OK, SizeType.RESIZEABLE, null);
        }, true);
    }
    
    /**
     * Shows a new message dialog with the specified message getText.
     * <p>
     * The difference between this method and {@link #showInfoDialog(String, String)} is that this method uses a simple {@link Label} to display the message rather than a {@link WebView}.
     * <p>
     * <b>Can be called off of the FX thread.</b>
     *
     * @param title   The title of the {@link Dialog}.
     * @param message The message to display for the user. Null to use the title as the message.
     */
    public static void showMessageDialog(String title, String message)
    {
        FXTools.get().runFX(() -> showDialog(title, message, null, null, DEFAULT_SPACING, OK, SizeType.RESIZEABLE, null), true);
    }
    
    /**
     * Shows a confirm {@link Dialog}.
     *
     * @param message The message to display for the user. Null to use the title as the message.
     * @return True if the Yes option is chosen, false if the No option is chosen (or if the {@link Dialog} is closed).
     */
    public static boolean showConfirmDialog(String title, String message)
    {
        Boolean bool = showDialog(title, message, null, null, DEFAULT_SPACING, YES_NO, SizeType.RESIZEABLE, new DialogCallback<>(false, (param) -> true));
        return bool != null ? bool : false;
    }
    
    /**
     * Shows a Yes, No, or Cancel option {@link Dialog}.
     *
     * @param title   The title of the {@link Dialog}.
     * @param message The message to display for the user. Null to use the title as the message.
     * @return The {@link ButtonBar.ButtonData} that was selected by the user, or null if the {@link Dialog} was closed.
     */
    public static ButtonBar.ButtonData showYesNoCancelDialog(String title, String message)
    {
        return showDialog(title, message, null, null, DEFAULT_SPACING, YES_NO_CANCEL, SizeType.RESIZEABLE, new DialogCallback<>(null, (param -> param != null ? param.getButtonData() : null)));
    }
    
    /**
     * Shows a {@link Dialog} that allows the user to enter an integer value.
     *
     * @param title        The title of the {@link Dialog}.
     * @param message      The message to display for the user. Null to use the title as the message.
     * @param initialValue The initial value (always uses first element). Null or empty for default.
     * @return The integer that the user entered, or 0 if the {@link Dialog} was cancelled or closed.
     */
    public static int showIntDialog(String title, String message, int... initialValue)
    {
        int initialVal = initialValue != null && initialValue.length != 0 ? initialValue[0] : 0;
        TextField textField = new TextField();
        FXTools.get().numberTextField(textField, false);
        textField.setText("" + initialVal);
        Integer value = showDialog(title, message, textField, textField, DEFAULT_SPACING, DONE_CANCEL, SizeType.RESIZEABLE, new DialogCallback<>(NULL_NUMBER, param -> FXTools.get().getIntValue(textField)));
        return value != null ? value : NULL_NUMBER;
    }
    
    /**
     * Shows a {@link Dialog} that allows the user to enter a long value.
     *
     * @param title        The title of the {@link Dialog}.
     * @param message      The message to display for the user. Null to use the title as the message.\
     * @param initialValue The initial value (always uses first element). Null or empty for default.
     * @return The long that the user entered, or 0 if the {@link Dialog} was cancelled or closed.
     */
    public static long showLongDialog(String title, String message, long... initialValue)
    {
        long initialVal = initialValue != null && initialValue.length != 0 ? initialValue[0] : 0;
        TextField textField = new TextField();
        FXTools.get().numberTextField(textField, false);
        textField.setText("" + initialVal);
        Long value = showDialog(
                title,
                message,
                textField,
                textField,
                DEFAULT_SPACING,
                DONE_CANCEL,
                SizeType.RESIZEABLE,
                new DialogCallback<>((long) NULL_NUMBER, param -> FXTools.get().getLongValue(textField))
        );
        return value != null ? value : NULL_NUMBER;
    }
    
    /**
     * Shows a {@link Dialog} that allows the user to enter a number (double) value.
     *
     * @param title   The title of the {@link Dialog}.
     * @param message The message to display for the user. Null to use the title as the message.
     * @return The number(double) that the user entered, or 0.0 if the {@link Dialog} was cancelled or closed.
     */
    public static double showDoubleDialog(String title, String message, double... initialValue)
    {
        double initialVal = initialValue != null && initialValue.length != 0 ? initialValue[0] : 0.0;
        TextField textField = new TextField();
        FXTools.get().numberTextField(textField, true);
        textField.setText("" + initialVal);
        Double value = showDialog(
                title,
                message,
                textField,
                textField,
                DEFAULT_SPACING,
                DONE_CANCEL,
                SizeType.RESIZEABLE,
                new DialogCallback<>((double) NULL_NUMBER, param -> FXTools.get().getValue(textField))
        );
        return value != null ? value : NULL_NUMBER;
    }
    
    /**
     * Shows a {@link Dialog} that allows the user to enter a String value using a {@link TextField}.
     *
     * @param title      The title of the {@link Dialog}.
     * @param message    The message to display for the user. Null to use the title as the message.
     * @param allowEmpty True if the {@link Dialog} should accept empty Strings, false if the {@link Dialog} should force the user to enter in at least 1 character (or close the {@link Dialog}).
     * @return The String that the user entered, or null if the {@link Dialog} was cancelled or closed.
     */
    public static String showStringDialog(String title, String message, boolean allowEmpty, String... initialValue)
    {
        String value;
        do
        {
            String initialVal = initialValue != null && initialValue.length != 0 ? initialValue[0] : "";
            TextField textField = new TextField();
            textField.setText(initialVal);
            value = showDialog(title, message, textField, textField, DEFAULT_SPACING, DONE_CANCEL, SizeType.RESIZEABLE, new DialogCallback<>(null, param -> textField.getText()));
            if (value != null && value.isEmpty() && !allowEmpty)
                showMessageDialog("You must enter at least 1 character.", null);
        }
        while (value != null && value.isEmpty() && !allowEmpty);
        return value;
    }
    
    /**
     * Shows a {@link Dialog} that allows the user to enter a large String value using a {@link TextArea}.
     *
     * @param title   The title of the {@link Dialog}.
     * @param message The message to display for the user. Null to use the title as the message.
     * @return The String that the user entered, or null if the {@link Dialog} was cancelled or closed.
     */
    public static String showTextDialog(String title, String message, String... initialValue)
    {
        String initialVal = initialValue != null && initialValue.length != 0 ? initialValue[0] : "";
        TextArea area = new TextArea();
        area.setText(initialVal);
        return showDialog(title, message, area, area, DEFAULT_SPACING, DONE_CANCEL, SizeType.RESIZEABLE, new DialogCallback<>(null, param -> area.getText()));
    }
    
    /**
     * Shows a {@link Dialog} that allows the user to choose a value from the specified array of potential values.
     *
     * @param title            The title of the {@link Dialog}.
     * @param message          The message to display for the user. Null to use the title as the message.
     * @param autoCompleteMode The {@link FXTools.AutoCompleteMode}, or null if the {@link ComboBox} is not an auto-complete box.
     * @param defaultOption    The default option that the {@link ComboBox} returns if the user does not select a value.
     * @param options          The array of potential options that the user can choose.
     * @param <T>              The type of element in the {@link ComboBox}.
     * @return The value that the user chose from the {@link ComboBox}, or the default value if the {@link Dialog} was cancelled or closed.
     */
    @SafeVarargs public static <T> T showChooseOptionDialog(
            String title,
            String message,
            FXTools.AutoCompleteMode autoCompleteMode,
            T defaultOption,
            boolean selectFirst,
            String promptText,
            T... options)
    {
        ComboBox<T> comboBox = new ComboBox<>();
        FXTools.get().autoCompleteComboBox(comboBox, autoCompleteMode, promptText);
        comboBox.getItems().addAll(options);
        if (autoCompleteMode == null)
            if (selectFirst)
                comboBox.getSelectionModel().selectFirst();
            else
                comboBox.getSelectionModel().select(defaultOption);
        return showDialog(
                title,
                message,
                comboBox,
                autoCompleteMode != null ? comboBox : null,
                DEFAULT_SPACING,
                DONE_CANCEL,
                SizeType.RESIZEABLE,
                new DialogCallback<>(defaultOption, param -> FXTools.get().getSelectedValue(comboBox))
        );
    }
    
    /**
     * Shows a {@link Dialog} that allows the user to choose a value from all of the available enum values based on the specified default option enum.
     *
     * @param title            The title of the {@link Dialog}.
     * @param message          The message to display for the user. Null to use the title as the message.
     * @param autoCompleteMode The {@link FXTools.AutoCompleteMode}, or null if the {@link ComboBox} is not an auto-complete box.
     * @param defaultOption    The default option that the {@link ComboBox} returns if the user does not select a value.
     * @param <T>              The type of element in the {@link ComboBox}.
     * @return The value that the user chose from the {@link ComboBox}, or the default value if the {@link Dialog} was cancelled or closed.
     * @see #showChooseOptionDialog(String, String, FXTools.AutoCompleteMode, Object, boolean, String, Object[])
     */
    public static <T extends Enum> T showChooseEnumDialog(String title, String message, FXTools.AutoCompleteMode autoCompleteMode, T defaultOption, String promptText, boolean selectFirst)
    {
        if (promptText != null && promptText.isEmpty())
            promptText = "Start typing " + StringTools.get().classToString(defaultOption.getClass());
        return showChooseOptionDialog(title, message, autoCompleteMode, defaultOption, selectFirst, promptText, (T[]) EnumTools.get().list(defaultOption));
    }
    
    /**
     * Shows a {@link ListView} that allows the user to choose any number of values from the specified array of potential values.
     *
     * @param title   The title of the {@link Dialog}.
     * @param message The message to display for the user. Null to use the title as the message.
     * @param options The array of potential options that the user can choose.
     * @param <T>     The type of element in the {@link ListView}.
     * @param empty   The values to be returned when empty.
     * @return The values that the user has selected from the {@link ListView}, or the default value if the {@link Dialog} was cancelled or closed.
     */
    @SafeVarargs public static <T> T[] showChooseOptionsDialog(String title, String message, T[] empty, T... options)
    {
        ListView<T> listView = new ListView<>();
        FXTools.get().applyCellFactory(listView);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setMinSize(0, 0);
        listView.setPrefSize(200, 250);
        listView.getItems().addAll(options);
        return showDialog(
                title,
                message,
                listView,
                listView,
                DEFAULT_SPACING,
                DONE_CANCEL,
                SizeType.RESIZEABLE,
                new DialogCallback<>(empty, param -> listView.getSelectionModel().getSelectedItems().toArray(empty))
        );
    }
    
    /**
     * Shows a {@link Dialog} with the specified {@link DialogController controller} as the base.
     *
     * @param title       The title of the {@link Dialog}.
     * @param message     The message to display for the user. Null to use the title as the message.
     * @param spacing     The spacing around the {@link Node}. Use 0 for no spacing ({@link Node} will not be encased in a parent container).
     * @param buttonTypes The {@link ButtonType button types} defining the {@link Button buttons} to be displayed at the base of the {@link Dialog}.
     * @param launch      True if the {@link DialogController} should be launched before being displayed in the {@link Dialog}, false otherwise.
     * @param controller  The {@link DialogController} that is being displayed.
     * @return The value returned by the {@link DialogCallback callback} of the {@link DialogController}, or null if the {@link DialogController} has not been initialized.
     */
    public static <R> R showControllableDialog(String title, String message, double spacing, ButtonType[] buttonTypes, boolean launch, DialogController<R> controller)
    {
        // CHANGE-HERE
//        if (launch)
//            controller.launch();
        //        if (controller.getInitializer().isInitialized()) // CHANGE-HERE
        return showDialog(title, message, controller.root(), controller.root(), spacing, buttonTypes, SizeType.RESIZEABLE, controller.getCallback());
        //        return null; // CHANGE-HERE
    }
    
    /**
     * Shows a {@link Dialog} with the specified {@link Node content} and title. Does not use a {@link DialogCallback}.
     * <p>
     * This method is typically used for testing, or can also be used to display simple information to the user, such as an {@link Image}.
     *
     * @param title    The title of the {@link Dialog} window.
     * @param message  The message to display for the user. The message is displayed directly above the {@code content}.
     *                 <p>
     *                 Null to use the title as the message, empty to display no message.
     * @param content  The {@link Node contents} of the {@link Dialog} window.
     * @param sizeType The {@link SizeType} that controls how the {@link Dialog} can be resized.  Null to not use a {@link SizeType}.
     *                 <p>
     *                 Note that {@link SizeType#MINIMUM_SIZE} does <i>NOT</i> work for {@link Dialog dialogs}.
     */
    public static void showDialog(String title, String message, Node content, SizeType sizeType)
    {
        showDialog(title, message, content, null, 0.0, DONE, sizeType, null);
    }
    
    /**
     * Shows a {@link Dialog} with the specified {@link Node} content using the specified {@link DialogCallback}.
     *
     * @param <T>         The type of content returned by the {@link DialogCallback}.
     * @param title       The title of the dialog window. Null for no title.
     * @param content     The {@link Node} content of the {@link Dialog}.
     * @param buttonTypes The {@link ButtonType button types} defining the {@link Button buttons} to be displayed at the base of the {@link Dialog}.
     * @param sizeType    The {@link SizeType} that controls how the {@link Dialog} can be resized.  Null to not use a {@link SizeType}.
     *                    <p>
     *                    Note that {@link SizeType#MINIMUM_SIZE} does <i>NOT</i> work for {@link Dialog dialogs}.
     * @param callback    The {@link DialogCallback}. Null to not use a callback. Using a null callback will cause this method to always return null.
     * @return The value returned by the {@link DialogCallback}.
     */
    public static <T> T showDialog(String title, Node content, ButtonType[] buttonTypes, SizeType sizeType, DialogCallback<T> callback)
    {
        return showDialog(title, null, content, content, DEFAULT_SPACING, buttonTypes, sizeType, callback);
    }
    
    /**
     * Shows a {@link Dialog} with the specified {@link Node} content using the specified {@link DialogCallback}.
     *
     * @param <T>            The type of content returned by the {@link DialogCallback}.
     * @param title          The title of the dialog window. Null for no title.
     * @param message        The message to display for the user. The message is displayed directly above the {@code content}.
     *                       <p>
     *                       Empty to use the title as the message, null to display no message.
     * @param content        The {@link Node} content of the {@link Dialog}.
     * @param contentToFocus The {@link Node} content that is to be focused immediately after the {@link Dialog} is shown. Null to focus the {@code content}.
     * @param spacing        The spacing around the {@link Node}. Use -1 for default spacing, or 0 for no spacing.
     * @param buttonTypes    The {@link ButtonType button types} defining the {@link Button buttons} to be displayed at the base of the {@link Dialog}.
     * @param sizeType       The {@link SizeType} that controls how the {@link Dialog} can be resized. Null to not use a {@link SizeType}.
     *                       <p>
     *                       Note that {@link SizeType#MINIMUM_SIZE} does <i>NOT</i> work for {@link Dialog dialogs}.
     * @param callback       The {@link DialogCallback}. Null to not use a callback. Using a null callback will cause this method to always return null.
     * @return The value returned by the {@link DialogCallback}.
     */
    public static <T> T showDialog(String title, String message, Node content, Node contentToFocus, double spacing, ButtonType[] buttonTypes, SizeType sizeType, DialogCallback<T> callback)
    {
        return FXTools.get().runFX(() -> {
            final Dialog<T> dialog;
            final DialogPane dialog_pane;
            
            final BorderPane parent_container = new BorderPane();
            final Node container;
            final Node final_content_to_focus = contentToFocus != null ? contentToFocus : content;
            
            // Create the dialog object
            if (callback != null)
            {
                callback.init();
                dialog = callback.getDialog();
                dialog.setResultConverter(callback.getCallback());
            }
            else
                dialog = new Dialog<>();
            
            dialog_pane = dialog.getDialogPane();
            
            // Apply css to dialog
            dialog_pane.getStylesheets().add(TB.resources().getResourceURL("css/tribot.css").toExternalForm());
            dialog_pane.getStyleClass().add("dialogFX");
            
            // Set the spacing around the dialog's content
            final double final_spacing = spacing == -1 ? DEFAULT_SPACING : spacing;
            if (final_spacing > 0 && content != null)
            {
                container = new AnchorPane(content);
                AnchorPane.setLeftAnchor(content, final_spacing);
                AnchorPane.setRightAnchor(content, final_spacing);
                AnchorPane.setTopAnchor(content, final_spacing);
            }
            else
                container = content;
            
            // Set the dialog's title (if applicable)
            if (title != null)
                dialog.setTitle(title);
            
            // Set the dialog's message (if applicable)
            if (message != null && !message.isEmpty())
                parent_container.setTop(new Label(message));
            
            if (container != null)
                parent_container.setCenter(container);
            if (container instanceof Region && ((Region) container).getPrefWidth() < 300)
                parent_container.setPrefWidth(300);
            // Set the dialog's content
            dialog.getDialogPane().setContent(parent_container);
            
            // Add the option buttons to the dialog
            for (ButtonType buttonType: buttonTypes)
                dialog.getDialogPane().getButtonTypes().add(buttonType);
            dialog.setResizable(true);
            
            // Add dialog to the dialogs list
            synchronized (dialogs_lock)
            {
                if (canAdd)
                    dialogs.add(dialog);
            }
            // Set the dialog to be removed from the dialogs list when closed
            dialog.setOnCloseRequest((DialogEvent event) -> {
                synchronized (dialogs_lock)
                {
                    if (canAdd)
                        dialogs.remove(dialog);
                }
            });
            
            // Set the dialog's resize type
            if (SizeType.NON_RESIZEABLE == sizeType)
                dialog.setResizable(false);
            else if (SizeType.RESIZEABLE == sizeType)
                dialog.setResizable(true);
            
            // TODO [S]: Use Executor?
            new Thread(() -> {
                // TODO [S]: Make this more efficient
                // Wait for dialog to appear
                while (!dialog.isShowing())
                    TB.general().sleep(5);
                // Focus content if not a ComboBox, ChoiceBox, or ListView
                FXTools.get().runFX(() -> {
                    if (final_content_to_focus != null && !(TB.general().instanceOf(final_content_to_focus, ComboBox.class, ChoiceBox.class, ListView.class)))
                        final_content_to_focus.requestFocus();
                }, false);
            }).start();
            
            // Set dialog to return value of specified Callback (if applicable)
            if (callback != null)
            {
                Optional<T> oValue = dialog.showAndWait();
                if (oValue != null && oValue.isPresent())
                    return oValue.get();
            }
            else
                dialog.showAndWait();
            return null;
        });
    }
    
    /**
     * Returns an array of {@link ButtonBar.ButtonData} values based on the specified array of {@link ButtonType} values.
     *
     * @param buttonTypes The array of {@link ButtonType} values.
     * @return An array of {@link ButtonBar.ButtonData} values based on the specified array of {@link ButtonType} values.
     */
    public static ButtonBar.ButtonData[] buttonData(ButtonType[] buttonTypes)
    {
        ButtonBar.ButtonData[] buttonData = new ButtonBar.ButtonData[buttonTypes.length];
        for (int i = 0; i < buttonTypes.length; i++)
            buttonData[i] = buttonTypes[i].getButtonData();
        return buttonData;
    }
    
    /**
     * Closes all currently open dialogs.
     */
    public static void closeAll()
    {
        synchronized (dialogs_lock)
        {
            canAdd = false;
            if (dialogs != null)
            {
                dialogs.forEach(Dialog::close);
                dialogs.clear();
                dialogs = null;
            }
            canAdd = true;
        }
    }
    
    //
    
    public enum SizeType
    {
        RESIZEABLE, MINIMUM_SIZE, NON_RESIZEABLE;
    }
}