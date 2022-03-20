package com.taco.suit_lady.util.tools.fx_tools;

import com.sun.javafx.application.PlatformImpl;
import com.taco.suit_lady.ui.jfx.Colorable;
import com.taco.suit_lady.ui.jfx.hyperlink.HyperlinkNodeFX;
import com.taco.suit_lady.ui.jfx.lists.Listable;
import com.taco.suit_lady.util.SimplePredicate;
import com.taco.suit_lady.util.UndefinedRuntimeException;
import com.taco.suit_lady.util.tools.*;
import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.values.numbers.Bounds;
import com.taco.suit_lady.util.values.enums.Axis;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.enums.OpResultType;
import com.taco.suit_lady.util.values.enums.OpType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import com.taco.suit_lady.ui.console.ConsoleBB;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Contains a variety of classes that provide JavaFX utility features.
 */
public class FX {
    private FX() { } //No Instance
    
    public static @NotNull Num2D dimensionsOf(@NotNull Region region) { return new Num2D(region.getWidth(), region.getHeight()); }
    
    //<editor-fold desc="EDT/FX Thread">
    
    /**
     * Checks if the current {@link Thread} is the the JavaFX Application thread.
     *
     * @return True if the current {@link Thread} is the the JavaFX Application thread.
     *
     * @see Platform#isFxApplicationThread()
     */
    public static boolean isFXThread() {
        return Platform.isFxApplicationThread();
    }
    
    /**
     * Checks if the current {@link Thread} is the AWT event dispatch thread (EDT)
     *
     * @return True if the current {@link Thread} is the EDT, false otherwise.
     *
     * @see EventQueue#isDispatchThread()
     */
    public static boolean isEDT() {
        return EventQueue.isDispatchThread();
    }
    
    //
    
    /**
     * Runs the specified Runnable on the JavaFX Thread.
     *
     * @param runnable The Runnable to be executed.
     * @param wait     True if this method should block until the Runnable is finished execution, false if this method should return as soon as the Runnable has been published.
     *                 //
     */
    public static void runFX(Runnable runnable, boolean wait) {
        Exc.nullCheck(runnable, "Runnable cannot be null");
        try {
            if (isFXThread())
                runnable.run();
            else if (wait)
                PlatformImpl.runAndWait(runnable);
            else
                Platform.runLater(runnable);
        } catch (Exception e) {
            if (!e.getMessage().equalsIgnoreCase("toolkit has exited"))
                throw Exc.ex(e);
        }
    }
    public static void runFX(Runnable runnable) { runFX(runnable, true); }
    
    /**
     * Runs the specified Callable on the JavaFX Thread, and then returns its result when it is done.
     *
     * @param callable The Callable to be executed.
     */
    public static <V> V callFX(Callable<V> callable) {
        Exc.nullCheck(callable, "Callable cannot be null");
        try {
            if (isFXThread())
                return callable.call();
            else {
                ObjectProperty<V> _objProperty = new SimpleObjectProperty<>(null);
                PlatformImpl.runAndWait(() -> {
                    try {
                        _objProperty.set(callable.call());
                    } catch (Exception e) {
                        if (!e.getMessage().equalsIgnoreCase("toolkit has exited"))
                            throw Exc.ex(e);
                    }
                });
                return _objProperty.get();
            }
        } catch (Exception e) {
            throw Exc.ex(e);
        }
    }
    
    //
    
    /**
     * Runs the specified FXRunnable on the EDT. If the current thread is already the EDT, this method simply calls the run() method of the specified Runnable.
     * <p>
     * This method is exceptionally useful for starting background threads that aren't terminated by TRiBot on the end of the script.
     *
     * @param runnable The FXRunnable being executed.
     * @param wait     True if this method should block until the Runnable is finished execution, false if this method should return as soon as the Runnable has been published.
     */
    public static void runEDT(Runnable runnable, boolean wait) {
        Exc.nullCheck(runnable, "Runnable cannot be null");
        try {
            if (EventQueue.isDispatchThread())
                runnable.run();
            else if (wait)
                EventQueue.invokeAndWait(runnable);
            else
                EventQueue.invokeLater(runnable);
        } catch (Exception e) {
            throw Exc.ex(e);
        }
    }
    
    /**
     * Runs the specified Callable on the JavaFX Thread, and then returns its result when it is done.
     *
     * @param callable The Callable to be executed.
     */
    public static <V> V runEDT(Callable<V> callable) {
        Exc.nullCheck(callable, "Callable cannot be null");
        try {
            if (isFXThread())
                return callable.call();
            else {
                ObjectProperty<V> _objProperty = new SimpleObjectProperty<>(null);
                EventQueue.invokeAndWait(() -> {
                    try {
                        _objProperty.set(callable.call());
                    } catch (Exception e) {
                        throw Exc.ex(e);
                    }
                });
                return _objProperty.get();
            }
        } catch (Exception e) {
            throw Exc.ex(e);
        }
    }
    
    //
    
    //<editor-fold desc="--- CHECK FX ---">
    
    //<editor-fold desc="> Require FX">
    
    public static void requireFX() { requireFX((Lock) null); }
    public static void requireFX(@Nullable Lock lock) { checkFX(true, lock); }
    
    public static void requireFX(@Nullable Runnable action) { requireFX(null, action); }
    public static void requireFX(@Nullable Lock lock, @Nullable Runnable action) { checkFX(true, lock, action); }
    
    public static <T> T requireFX(@Nullable Supplier<T> action) { return requireFX(null, action); }
    public static <T> T requireFX(@Nullable Lock lock, @Nullable Supplier<T> action) { return checkFX(true, lock, action); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Forbid FX">
    
    public static void forbidFX() { forbidFX((Lock) null); }
    public static void forbidFX(@Nullable Lock lock) { checkFX(false, lock); }
    
    public static void forbidFX(@Nullable Runnable action) { forbidFX(null, action); }
    public static void forbidFX(@Nullable Lock lock, @Nullable Runnable action) { checkFX(false, lock, action); }
    
    public static <T> T forbidFX(@Nullable Supplier<T> action) { return forbidFX(null, action); }
    public static <T> T forbidFX(@Nullable Lock lock, @Nullable Supplier<T> action) { return checkFX(false, lock, action); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Check FX">
    
    public static void checkFX(boolean require) { checkFX(require, null); }
    public static void checkFX(boolean require, @Nullable Lock lock) {
        Exe.sync(lock, () -> {
            if (require && !isFXThread())
                throw Exc.ex(new IllegalStateException("Operation must be executed on the FX Thread."));
            if (!require && isFXThread())
                throw Exc.ex(new IllegalStateException("Operation must NOT be executed on the FX Thread."));
        }, true);
    }
    
    public static void checkFX(boolean require, @Nullable Lock lock, @Nullable Runnable action) {
        checkFX(require, lock, () -> Obj.getIfNonNull(() -> action, v -> {
            v.run();
            return null;
        }));
    }
    
    public static <T> T checkFX(boolean require, @Nullable Lock lock, @Nullable Supplier<T> action) {
        return Exe.sync(lock, () -> {
            checkFX(require);
            return Obj.getIfNonNull(() -> action, v -> action.get());
        }, true);
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //
    
    /**
     * Throws a {@link RuntimeException} if the current {@link Thread} is not the EDT.
     */
    public static void requireEDT() {
        if (!isEDT())
            throw Exc.ex(new IllegalStateException("Operation must be executed on the EDT."));
    }
    
    //</editor-fold>
    
    /**
     * Returns the {@code key code} for the specified {@link KeyCode}.
     *
     * @param keyCode The {@link KeyCode}.
     *
     * @return The {@code key code} for the specified {@link KeyCode}.
     */
    public static int getKeyCode(KeyCode keyCode) {
        return Exc.nullCheck(keyCode, "JFX KeyCode").getCode();
    }
    
    /**
     * Returns the {@code key char} for the specified {@link KeyCode}.
     *
     * @param keyCode The {@link KeyCode}.
     *
     * @return The {@code key char} for the specified {@link KeyCode}.
     */
    public static String getKeyChar(KeyCode keyCode) {
        return Exc.nullCheck(keyCode, "JFX KeyCode").getChar();
    }
    
    
    /**
     * Returns a custom {@link DataFormat} with the specified id name.
     * <p>
     * If a custom {@link DataFormat} with the specified id name already exists, the existing {@link DataFormat} is returned.
     * <p>
     * Returns null if the specified id name is null or empty.
     *
     * @param id The id name of the custom {@link DataFormat}.
     *
     * @return A custom {@link DataFormat} with the specified id name.
     */
    public static @NotNull DataFormat getDataFormat(String id) {
        DataFormat format = DataFormat.lookupMimeType(id);
        if (format != null)
            return format;
        return new DataFormat(id);
    }
    
    /**
     * Removes the specified child {@link Node} from its {@link Parent parent}.
     * <p>
     * The {@link Parent parent} must be of type {@link Pane}.
     *
     * @param node The {@link Node}.
     *
     * @return True if the removal was successful, false otherwise.
     */
    public static boolean removeFromParent(Node node) {
        if (node != null) {
            Parent parent = node.getParent();
            if (parent != null)
                if (parent instanceof Pane)
                    return ((Pane) parent).getChildren().remove(node);
        }
        return false;
    }
    
    /**
     * Sets the specified {@link Node} as hidden or shown depending on the specified boolean value.
     * <p>
     * This method binds the {@link Node node's} {@link Node#managedProperty() Managed Property} to its {@link Node#visibleProperty()} Visible Property}.
     *
     * @param node    The {@link Node} being shown/hidden.
     * @param visible True if the {@link Node} should be shown, false if it should be hidden.
     */
    public static void setVisible(Node node, boolean visible) {
        if (node != null) {
            node.managedProperty().bind(node.visibleProperty());
            node.setVisible(visible);
        }
    }
    
    /**
     * Combines the children {@link Node Nodes} of the specified {@link TextFlow TextFlows} into a single {@link TextFlow} and then returns the result.
     *
     * @param objs The {@link Node Nodes} being combined. This array can contain any of the following:
     *             <ol>
     *             <li>An {@link ArrayList} of {@link Node Nodes}.</li>
     *             <li>An array of {@link Node Nodes}.</li>
     *             <li>A {@link Node}.</li>
     *             <li>A {@link HyperlinkNodeFX}.</li>
     *             </ol>
     *
     * @return The children {@link Node Nodes} of the specified {@link TextFlow TextFlows} combined into a single {@link TextFlow}.
     */
    public static @Nullable TextFlow combineToFlow(TextFlow textFlow, Object @NotNull ... objs) {
        ArrayList<Node> textChildren = new ArrayList<>();
        for (Object obj: objs)
            if (obj == null)
                return null;
            else
                addFlowObj(obj, textChildren);
        textFlow.getChildren().addAll(textChildren.toArray(new Node[textChildren.size()]));
        return textFlow;
    }
    
    /**
     * Combines the children {@link Node Nodes} of the specified {@link TextFlow TextFlows} into a single {@link TextFlow} and then returns the result.
     *
     * @param objs The {@link Node Nodes} being combined. This array can contain any of the following:
     *             <ol>
     *             <li>An {@link ArrayList} of {@link Node Nodes}.</li>
     *             <li>An array of {@link Node Nodes}.</li>
     *             <li>A {@link Node}.</li>
     *             <li>A {@link HyperlinkNodeFX}.</li>
     *             </ol>
     *
     * @return The children {@link Node Nodes} of the specified {@link TextFlow TextFlows} combined into a single {@link TextFlow}.
     */
    public static @Nullable TextFlow combineToFlow(Object @NotNull ... objs) {
        ArrayList<Node> textChildren = new ArrayList<>();
        for (Object obj: objs)
            if (obj == null)
                return null;
            else
                addFlowObj(obj, textChildren);
        return new TextFlow(textChildren.toArray(new Node[textChildren.size()]));
    }
    
    /**
     * Adds the specified {@link Object} to the specified {@link ArrayList} of {@link Node Nodes}.
     * <p>
     * This method should only be called by {@link #combineToFlow(Object...)}
     *
     * @param obj      The {@link Object} being added.
     * @param children The {@link ArrayList} of {@link Node Nodes} that is being added to.
     *
     * @see #combineToFlow(Object...)
     */
    private static void addFlowObj(Object obj, ArrayList<Node> children) {
        if (obj != null)
            if (obj instanceof String)
                addFlowObj(new Text((String) obj), children);
            else if (obj instanceof Text) {
                Text text = (Text) obj;
                text.getStyleClass().add("text");
                children.add(text);
            } else if (obj instanceof TextFlow) {
                TextFlow flow = (TextFlow) obj;
                for (Node child: flow.getChildren())
                    addFlowObj(child, children);
            } else if (obj instanceof Node) {
                Node node = (Node) obj;
                children.add(node);
            } else if (obj instanceof HyperlinkNodeFX) {
                ArrayList<Node> nodes = ((HyperlinkNodeFX) obj).getNodes();
                for (Object obj2: nodes)
                    addFlowObj(obj2, children);
            } else if (obj instanceof ArrayList) {
                ArrayList parents = (ArrayList) obj;
                for (Object obj2: parents)
                    addFlowObj(obj2, children);
            } else if (obj instanceof Object[]) {
                Object[] parents = (Object[]) obj;
                for (Object obj2: parents)
                    addFlowObj(obj2, children);
            } else
                ConsoleBB.CONSOLE.print("ERROR: " + TB.getSimpleName(obj.getClass()) + " is an invalid type.");
    }
    
    /**
     * Converts the specified {@link TextField} into a {@code Number Text Field}.
     * <p>
     * A {@code Number Text Field} will only accept numbers as input, as well as "k", "m", and "b" to denote thousands, millions, and billions, respectively.
     * <br>
     * The text field will automatically update when the {@code TextField} loses focus or when the user presses enter.
     * <p>
     * The default value of the {@code TextField} is {@code 0}.
     *
     * @param textField     The {@code TextField}.
     * @param allowDecimals True if the {@code TextField} should allow doubles, false if the {@code TextField} should be limited to integers only.
     *
     * @return The converted {@code TextField} (the same {@code TextField} that was passed as a parameter).
     */
    @Contract("_, _ -> param1")
    public static @NotNull TextField numberTextField(TextField textField, boolean allowDecimals) {
        return numberTextField(textField, allowDecimals, 0);
    }
    
    /**
     * Converts the specified {@link TextField} into a {@code Number Text Field}.
     * <p>
     * A {@code Number Text Field} will only accept numbers as input, as well as "k", "m", and "b" to denote thousands, millions, and billions, respectively.
     * <br>
     * The text field will automatically update when the {@code TextField} loses focus or when the user presses enter.
     *
     * @param textField     The {@code TextField}.
     * @param allowDecimals True if the {@code TextField} should allow doubles, false if the {@code TextField} should be limited to integers only.
     * @param initialValue  The initial value of the {@code TextField}.
     *
     * @return The converted {@code TextField} (the same {@code TextField} that was passed as a parameter).
     */
    @Contract("_, _, _ -> param1")
    public static @NotNull TextField numberTextField(TextField textField, boolean allowDecimals, double initialValue) {
        Exc.nullCheck(textField, "TextField cannot be null.");
        
        textField.addEventFilter(KeyEvent.KEY_TYPED, _keyEvent -> {
            String _str = _keyEvent.getCharacter().toLowerCase();
            String _nStr = textField.getText() + _str;
            if (!isValidSuffix(_nStr, "k", "m", "b") ||
                !isValidPrefix(_nStr, "-") ||
                ((_str.endsWith("k") || _str.endsWith("m") || _str.endsWith("b")) && textField.getText().endsWith("."))) {
                _keyEvent.consume();
                return;
            }
            
            Character[] _numbers = A.concatMulti(
                    new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', 'k', 'm', 'b'},
                    (allowDecimals ? new Character[]{'.'} : new Character[]{})
                                                );
            int _periodCount = 0;
            
            for (char _c: _nStr.toCharArray())
                if (_c == '.')
                    _periodCount++;
            if (_periodCount > 1) {
                _keyEvent.consume();
                return;
            }
            
            char[] _strChars = _str.toCharArray();
            STR_LOOP:
            for (char _strChar: _strChars) {
                for (char _strChar2: _numbers)
                    if (_strChar == _strChar2)
                        continue STR_LOOP;
                _keyEvent.consume();
                return;
            }
        });
        
        textField.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER && event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                textField.setText("" + getIntValue(textField));
                textField.positionCaret(textField.getText().length());
            }
        });
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean focused) -> {
            if (!focused) {
                textField.setText("" + getIntValue(textField));
                textField.positionCaret(textField.getText().length());
            }
        });
        
        if (allowDecimals)
            textField.setText("" + initialValue);
        else
            textField.setText("" + ((int) initialValue));
        
        return textField;
    }
    
    /**
     * Turns the specified {@link Menu} into a Menu Button using the specified {@link Button}.}
     *
     * @param menu    The {@link Menu} being turned into a Menu Button.
     * @param button  The {@link Button}. Null to create a new {@link Button}.
     *                Set the name of the {@link Button} to empty if you want to use the existing name of the {@link Menu}.
     * @param handler The {@link EventHandler} for when the {@link Button} is pressed. Null to use the default handler of the specified {@link Button}.
     */
    public static void constructButtonMenu(Menu menu, Button button, EventHandler<ActionEvent> handler) {
        if (menu != null && button != null) {
            if (handler != null)
                button.setOnAction(handler);
            if (button.getText().isEmpty())
                button.setText(menu.getText());
            button.setMinSize(0, 0);
            button.setPadding(new Insets(0, 10, 1, 10));
            menu.setText("");
            menu.setOnMenuValidation(Event::consume);
            menu.setOnShowing(Event::consume);
            menu.setOnShown(Event::consume);
            menu.getItems().clear();
            menu.getStyleClass().add("button-menu");
            menu.setGraphic(button);
        }
    }
    
    /**
     * Sets the specified {@link WebView} to be updatable.
     * <p>
     * This is the same as calling {@code updateWebView(webView, new SimpleObjectProperty<>(observable), updaters))}.
     *
     * @param webView    The {@link WebView}.
     * @param observable The {@link ObservableStringValue} that will retrieve the information for the {@link WebView}.
     * @param updaters   Any {@link ObservableValue observable values} that will trigger an update when changed. Leave empty to only observe the specified {@link ObservableStringValue}.
     */
    public static void constructUpdatableWebView(WebView webView, ObservableStringValue observable, ObservableValue... updaters) {
        constructUpdatableWebView(webView, new SimpleObjectProperty<>(observable), updaters);
    }
    
    /**
     * Sets the specified {@link WebView} to be updatable.
     *
     * @param webView    The {@link WebView}.
     * @param observable The {@link ObservableValue} that will retrieve the information for the {@link WebView}.
     * @param updaters   Any {@link ObservableValue observable values} that will trigger an update when changed. Leave empty to only observe the specified {@link ObservableValue}.
     */
    public static void constructUpdatableWebView(@NotNull WebView webView, @NotNull ObservableValue<? extends ObservableStringValue> observable, ObservableValue @NotNull ... updaters) {
        webView.setFocusTraversable(false);
        webView.setOnMouseClicked(Event::consume);
        webView.setOnMousePressed(Event::consume);
        webView.setOnMouseReleased(Event::consume);
        webView.getEngine().loadContent(observable.getValue().get());
        
        ChangeListener changeListener = (observable1, oldValue, newValue) -> {
            ObservableStringValue observableString = observable.getValue();
            if (observableString != null) {
                String string = observableString.get();
                if (string != null) {
                    webView.getEngine().loadContent(Str.html(string, true));
                    return;
                }
            }
            webView.getEngine().loadContent("");
        };
        ChangeListener<ObservableValue> observableChangeListener = (observable1, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.removeListener(changeListener);
            if (newValue != null)
                newValue.addListener(changeListener);
            changeListener.changed(observable1, oldValue, newValue);
        };
        
        for (ObservableValue updater: updaters)
            updater.addListener(changeListener);
        observable.getValue().addListener(changeListener);
        observable.addListener(observableChangeListener);
    }
    
    /**
     * Returns the integer value from the specified {@link TextField}.
     * <p>
     * If the specified {@link TextField} does not contain a valid number (included numbers defined by "k" and "m"), this method throws an exception.
     *
     * @param textField The {@link TextField}.
     *
     * @return The integer value from the specified {@link TextField}.
     */
    public static int getIntValue(@NotNull TextField textField) {
        return (int) Calc.getLongkmb(textField.getText(), false);
    }
    
    /**
     * Returns the integer value from the specified {@link TextField}.
     * <p>
     * If the specified {@link TextField} does not contain a valid number (included numbers defined by "k" and "m"), this method throws an exception.
     *
     * @param textField The {@link TextField}.
     *
     * @return The integer value from the specified {@link TextField}.
     */
    public static long getLongValue(@NotNull TextField textField) {
        return Calc.getLongkmb(textField.getText(), false);
    }
    
    /**
     * Returns the double value from the specified {@link TextField}.
     * <p>
     * If the specified {@link TextField} does not contain a valid number (included numbers defined by "k" and "m"), this method throws an exception.
     *
     * @param textField The {@link TextField}.
     *
     * @return The double value from the specified {@link TextField}.
     */
    public static double getValue(@NotNull TextField textField) {
        return Calc.getkmb(textField.getText(), false);
    }
    
    /**
     * Resets the specified {@link ComboBox} to either a blank value, or its default (first) value.
     *
     * @param comboBox The {@link ComboBox} being reset.
     * @param clear    True if the specified {@link ComboBox} should be cleared (set to a blank val     false if the specified {@link ComboBox} should be set to its default (first)
     *                 value.
     */
    public static <T> void reset(ComboBox<T> comboBox, boolean clear) {
        if (clear || !comboBox.getItems().isEmpty())
            comboBox.setValue(null);
        else
            comboBox.setValue(comboBox.getItems().get(0));
    }
    
    /**
     * Edits the children of the specified {@link Parent}.
     *
     * @param parent         The {@link Parent}.
     * @param editable       The {@link ChildEditable} that is to be used to edit the children.
     * @param includeParents True if the parents should also be included, false otherwise.
     */
    public static void editChildren(Parent parent, @NotNull ChildEditable editable, boolean includeParents) {
        getChildren(parent, includeParents).forEach(editable::edit);
    }
    
    /**
     * Returns all of the children {@link Node Nodes} of the specified {@link Parent}.
     * <p>
     * This method uses a deep search, so all children, grandchildren, etc will be included in the search.
     *
     * @param parent         The {@link Parent}.
     * @param includeParents True if the parents should also be included, false otherwise.
     *
     * @return All of the children {@link Node Nodes} of the specified {@link Parent}.
     */
    public static @NotNull ArrayList<Node> getChildren(@NotNull Parent parent, boolean includeParents) {
        ArrayList<Node> children = new ArrayList<>();
        parent.getChildrenUnmodifiable().stream().filter(child -> child instanceof Parent).forEach(child -> {
            Parent childParent = (Parent) child;
            ArrayList<Node> children2 = getChildren(childParent, includeParents);
            children.addAll(children2);
        });
        if (includeParents || parent.getChildrenUnmodifiable().isEmpty())
            children.add(parent);
        return children;
    }
    
    /**
     * Sets all of the editable child {@link Node Nodes} of the specified {@link Parent} to be enabled or disabled.
     *
     * @param parent   The {@link Parent} that is being enabled or disabled.
     * @param enabled  True if all of the editable {@link Node Nodes} should be enabled, false if they should be disabled.
     * @param excludes An array of {@link Node Nodes} that should not be affected by this method.
     */
    public static void setAllEnabled(Parent parent, boolean enabled, Node @NotNull ... excludes) {
        ArrayList<Node> excludes_list = new ArrayList<>(excludes.length);
        excludes_list.addAll(java.util.Arrays.asList(excludes));
        for (Node c: excludes)
            if (c instanceof Parent)
                excludes_list.addAll(getChildren((Parent) c, true));
        getChildren(parent, true).stream().filter(c -> !A.contains(c, excludes_list.toArray())).forEach(c -> c.setDisable(!enabled));
    }
    
    /**
     * Shows a temporary {@link Tooltip} for the specified {@link Control}.
     * <p>
     * While the temporary {@link Tooltip} is being shown, if the {@link Control} already had a {@link Tooltip}, that {@link Tooltip} will not be shown.
     * <p>
     * When the temporary {@link Tooltip} is hidden for any reason, the {@link Tooltip} of the {@link Control} is set back to its original {@link Tooltip}.
     *
     * @param owner                   The {@link Stage} owner of the specified {@link Control}.
     * @param control                 The {@link Control} in which the temporary {@link Tooltip} is being shown for.
     * @param tooltipText             The getText the temporary {@link Tooltip} is to display.
     * @param consumeAutoHidingEvents True if events that would normally hide the {@link Tooltip} should be consumed, false otherwise (a normal {@link Tooltip}).
     *                                <p>
     *                                See {@link Tooltip#setConsumeAutoHidingEvents(boolean)}
     */
    public static void showTooltip(Stage owner, @NotNull Control control, String tooltipText, boolean consumeAutoHidingEvents) {
        Point2D p = control.localToScene(0.0, 0.0);
        
        Tooltip oldTooltip = control.getTooltip();
        Tooltip newTooltip = createTooltip(tooltipText);
        
        control.setTooltip(newTooltip);
        newTooltip.setAutoHide(true);
        newTooltip.setConsumeAutoHidingEvents(consumeAutoHidingEvents);
        
        newTooltip.show(
                owner,
                p.getX() + control.getScene().getX() + control.getScene().getWindow().getX() + (control.getWidth() - (control.getWidth() / 3.0)),
                p.getY() + control.getScene().getY() + control.getScene().getWindow().getY() + (control.getHeight() - (control.getHeight() / 3.0))
                       );
        newTooltip.setOnHidden((final WindowEvent event) -> control.setTooltip(oldTooltip));
    }
    
    /**
     * Creates a new {@link Tooltip} with the specified text.
     *
     * @param text The text.
     *
     * @return A new {@link Tooltip} with the specified text.
     */
    @Contract("_ -> new")
    public static @NotNull Tooltip createTooltip(String text) {
        return new Tooltip(text);
    }
    
    /**
     * Applies the specified onClick properties to the specified {@link Node}.
     * <p>
     * This method does nothing and returns null if the specified {@link Node}
     *
     * @param node           The {@link Node}.
     * @param handlePrevious True if the previous properties of the {@link Node} should continue to be fired, false if they should be overwritten.
     * @param onClick        {@link EventHandler} executed when the {@link Node} is clicked. Null to not add an {@link EventHandler}.
     * @param onDoubleClick  {@link EventHandler} executed when the {@link Node} is double-clicked. Null to not add an {@link EventHandler}.
     * @param onMiddleClick  {@link EventHandler} executed when the {@link Node} is middle-clicked. Null to not add an {@link EventHandler}.
     * @param popupCondition The {@link SimplePredicate condition} that must be true in order for the {@link ContextMenu popup menu} to appear. Null to always should the {@link ContextMenu popup menu}.
     * @param items          The {@link MenuItem menu items} to be added to the {@link ContextMenu popup menu}.
     *
     * @return The fully implemented {@link ContextMenu popup menu} created for and attached to the specified {@link Node}.
     */
    public static @NotNull ContextMenu onClick(
            @NotNull Node node, boolean handlePrevious,
            EventHandler<MouseEvent> onClick,
            EventHandler<MouseEvent> onDoubleClick,
            EventHandler<MouseEvent> onMiddleClick,
            SimplePredicate popupCondition, MenuItem... items) {
        ContextMenu menu = new ContextMenu(items);
        EventHandler<? super MouseEvent> oldOnClick = node.getOnMouseClicked();
        node.setOnMouseClicked(event -> {
            if (oldOnClick != null && handlePrevious)
                oldOnClick.handle(event); //Handle the old onMouseClick handler
            if (event.isPopupTrigger() && (popupCondition == null || popupCondition.test())) {
                if (menu.isShowing())
                    menu.hide();
                menu.show(node, event.getScreenX(), event.getScreenY());
            } else if (menu.isShowing())
                menu.hide();
            if (event.getButton() == MouseButton.PRIMARY)
                if (event.getClickCount() == 1 && onClick != null)
                    onClick.handle(event);
                else if (event.getClickCount() == 2 && onDoubleClick != null)
                    onDoubleClick.handle(event);
            if (event.getButton() == MouseButton.MIDDLE && onMiddleClick != null)
                onMiddleClick.handle(event);
        });
        return menu;
    }
    
    /**
     * Creates a new {@link CustomMenuItem} with the specified text, {@link Tooltip}, and {@link EventHandler}.
     *
     * @param text         The text of the {@link CustomMenuItem}.
     * @param tooltip      The {@link Tooltip} of the {@link CustomMenuItem}.
     * @param eventHandler The {@link EventHandler} that is executed when the {@link CustomMenuItem} is clicked.
     *
     * @return A new {@link CustomMenuItem} with the specified text, {@link Tooltip}, and {@link EventHandler}.
     */
    public static @NotNull CustomMenuItem createMenuItem(String text, Tooltip tooltip, EventHandler<ActionEvent> eventHandler) {
        return createMenuItem(new Label(text), tooltip, eventHandler);
    }
    
    /**
     * Creates a new {@link CustomMenuItem} with the specified {@link Node}, {@link Tooltip}, and {@link EventHandler}.
     *
     * @param node         The {@link Node} that is to be displayed as the content of the {@link CustomMenuItem}.
     * @param tooltip      The {@link Tooltip} of the {@link CustomMenuItem}.
     * @param eventHandler The {@link EventHandler} that is executed when the {@link CustomMenuItem} is clicked.
     *
     * @return A new {@link CustomMenuItem} with the specified {@link Node}, {@link Tooltip}, and {@link EventHandler}.
     */
    public static @NotNull CustomMenuItem createMenuItem(Node node, Tooltip tooltip, EventHandler<ActionEvent> eventHandler) {
        CustomMenuItem item = new CustomMenuItem(node);
        if (tooltip != null)
            Tooltip.install(node, tooltip);
        if (eventHandler != null)
            item.setOnAction(eventHandler);
        return item;
    }
    
    /**
     * Adds or removes the specified style classes to the specified {@link Node} based on the specified {@code active} boolean parameter.
     *
     * @param node         The {@link Node} the style classes are being added/removed to/from.
     * @param active       True if the style classes should be added, false if they should be removed.
     * @param styleClasses The style classes being added/removed.
     *
     * @return True if the style class addition/removal was successful, false otherwise.
     */
    public static boolean applyCSS(Node node, boolean active, String... styleClasses) {
        if (node != null && styleClasses != null) {
            for (String styleClass: styleClasses)
                if (styleClass != null)
                    if (active) {
                        if (!node.getStyleClass().contains(styleClass))
                            node.getStyleClass().add(styleClass);
                    } else
                        node.getStyleClass().remove(styleClass);
            if (active)
                A.containsAll(node.getStyleClass(), styleClasses);
            return !A.containsAny(node.getStyleClass(), styleClasses);
        }
        return false;
    }
    
    /**
     * Applies the specified CSS properties to the specified {@link Node}.
     * <p>
     * Throws a {@link NullPointerException} if the specified {@link Node} or styles array is null.
     *
     * @param node      The {@link Node} in which the CSS properties are being added to.
     * @param overwrite True if the existing CSS properties should be overwritten, false if they should not.
     * @param styles    The CSS properties being added to the specified {@link Node}.
     *
     * @throws NullPointerException If the specified {@link Node} or styles array is null.
     */
    public static void applyCSSInLine(Node node, boolean overwrite, String... styles) {
        Objects.requireNonNull(node, "Node cannot be null");
        Objects.requireNonNull(styles, "Styles cannot be null");
        String css = node.getStyle();
        for (String style: styles)
            if (style != null)
                css += " " + style + ";";
        node.setStyle(css);
    }
    
    /**
     * Checks if the mouse is on the specified {@link Node} based on the specified {@link MouseEvent}.
     *
     * @param node The {@link Region}.
     *
     * @return True if the mouse is on the specified {@link Node} based on the specified {@link MouseEvent}, false otherwise.
     */
    public static boolean isMouseOnNode(Node node, MouseEvent event) {
        Objects.requireNonNull(node, "Node cannot be null");
        
        Point2D local = node.screenToLocal(event.getScreenX(), event.getScreenY());
        if (local.getX() < 0 || local.getY() < 0)
            return false;
        if (node instanceof Canvas canvas)
            return local.getX() < canvas.getWidth() && local.getY() < canvas.getHeight();
        else if (node instanceof Region region)
            return local.getX() < region.getWidth() && local.getY() < region.getHeight();
        
        return node.isHover();
    }
    
    public static boolean isMouseOnEventSource(MouseEvent event) {
        Objects.requireNonNull(event, "Event cannot be null");
        if (event.getSource() instanceof Node)
            return isMouseOnNode((Node) event.getSource(), event);
        return false;
    }
    
    
    public static <T extends Node> T setAnchors(T node, double left, double right, double top, double bottom) {
        Exc.nullCheck(node, "Input Node");
        
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setBottomAnchor(node, bottom);
        
        return node;
    }
    
    public static <T extends Node> T setAnchors(T node) {
        return setAnchors(node, 0, 0, 0, 0);
    }
    
    public static <T extends Node> T setAnchors(T node, double value) {
        return setAnchors(node, value, value, value, value);
    }
    
    public static <T extends Node> T setAnchors(T node, double lr, double tb) {
        return setAnchors(node, lr, lr, tb, tb);
    }
    
    
    public static void drawRectangle(Canvas canvas, Bounds bounds, boolean wipeCanvas, boolean fill) {
        FX.runFX(() -> {
            if (wipeCanvas)
                clearCanvasUnsafe(canvas);
            
            if (fill)
                canvas.getGraphicsContext2D().fillRect(
                        bounds.xD(), bounds.yD(),
                        bounds.wD(), bounds.hD());
            else
                canvas.getGraphicsContext2D().strokeRect(
                        bounds.xD(), bounds.yD(),
                        bounds.wD(), bounds.hD());
        }, true);
    }
    
    public static void drawOval(Canvas canvas, Bounds bounds, boolean wipeCanvas, boolean fill) {
        FX.runFX(() -> {
            if (wipeCanvas)
                clearCanvasUnsafe(canvas);
            
            if (fill)
                canvas.getGraphicsContext2D().fillOval(
                        bounds.xD(), bounds.yD(),
                        bounds.wD(), bounds.hD());
            else
                canvas.getGraphicsContext2D().strokeOval(
                        bounds.xD(), bounds.yD(),
                        bounds.wD(), bounds.hD());
        }, true);
    }
    
    public static void drawArc(Canvas canvas, Bounds bounds, double startAngle, double arcExtent, ArcType closure, boolean wipeCanvas, boolean fill) {
        FX.runFX(() -> {
            if (wipeCanvas)
                clearCanvasUnsafe(canvas);
            
            if (fill)
                canvas.getGraphicsContext2D().fillArc(
                        bounds.xD(), bounds.yD(), bounds.wD(), bounds.hD(),
                        startAngle, arcExtent, closure);
            else
                canvas.getGraphicsContext2D().strokeArc(
                        bounds.xD(), bounds.yD(), bounds.wD(), bounds.hD(),
                        startAngle, arcExtent, closure);
        }, true);
    }
    
    public static void drawImage(@NotNull Canvas canvas, int x, int y, @NotNull Image image, boolean safe, boolean wipeCanvas) {
        FX.runFX(() -> {
            if (wipeCanvas)
                clearCanvasUnsafe(canvas);
            canvas.getGraphicsContext2D().drawImage(image, x, y);
        }, true);
    }
    
    public static void drawImage(@NotNull Canvas canvas, @NotNull Bounds bounds, @NotNull Image image, boolean safe, boolean wipeCanvas) {
        FX.runFX(() -> {
            if (wipeCanvas)
                clearCanvasUnsafe(canvas);
            final Bounds b = safe ? bounds.boundsFloorDim() : bounds;
            canvas.getGraphicsContext2D().drawImage(image, b.xD(), b.yD(), b.wD(), b.hD());
        }, true);
    }
    
    public static void drawImageScaled(@NotNull Canvas canvas, @NotNull Image image, @NotNull Bounds source, boolean wipeCanvas) {
        drawImageScaled(canvas, image, source, 1d, 1d, wipeCanvas);
    }
    
    public static void drawImageScaled(@NotNull Canvas canvas, @NotNull Image image, @NotNull Bounds source, double xScale, double yScale, boolean wipeCanvas) {
        FX.runFX(() -> {
            if (wipeCanvas)
                clearCanvasUnsafe(canvas);
            
            final int imageWidth = (int) image.getWidth();
            final int imageHeight = (int) image.getHeight();
            final int canvasWidth = (int) canvas.getWidth();
            final int canvasHeight = (int) canvas.getHeight();
            
            final int xMinO = (int) (source.getLocation(Axis.X_AXIS, LocType.MIN).doubleValue() * xScale);
            final int yMinO = (int) (source.getLocation(Axis.Y_AXIS, LocType.MIN).doubleValue() * yScale);
            final int xMaxO = xMinO + (int) (source.wD() * xScale);
            final int yMaxO = yMinO + (int) (source.hD() * yScale);
            
            final int xMinF = Math.max(xMinO, 0);
            final int yMinF = Math.max(yMinO, 0);
            final int xMaxF = Math.min(xMaxO, imageWidth);
            final int yMaxF = Math.min(yMaxO, imageHeight);
            
            final int widthF = Math.min(imageWidth, xMaxF - xMinF);
            final int heightF = Math.min(imageHeight, yMaxF - yMinF);
            
            final int xDestF = Math.max(-xMinO, 0);
            final int yDestF = Math.max(-yMinO, 0);
            
            canvas.getGraphicsContext2D().drawImage(
                    image,
                    xMinF, yMinF, widthF, heightF,
                    xDestF, yDestF, widthF, heightF);
        }, true);
    }
    
    //<editor-fold desc="--- IMAGE OPERATIONS ---">
    
    //<editor-fold desc="> Empty Image Construction">
    
    public static @NotNull WritableImage emptyImage(@NotNull NumExpr2D<?> dimensions) { return emptyImage(dimensions.a(), dimensions.b()); }
    public static @NotNull WritableImage emptyImage(@NotNull Point2D dimensions) { return emptyImage(dimensions.getX(), dimensions.getY()); }
    
    public static @NotNull WritableImage emptyImage(@NotNull NumExpr<?> dimension) { return emptyImage(dimension.a()); }
    public static @NotNull WritableImage emptyImage(@NotNull Number dimension) { return emptyImage(dimension, dimension); }
    
    public static @NotNull WritableImage emptyImage(@NotNull Number width, @NotNull Number height) { return new WritableImage(width.intValue(), height.intValue()); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Image Generation">
    
    //<editor-fold desc=">> Standard Image Generation">
    
    public static @NotNull Image generateImage(@Nullable Lock lock,
                                               @NotNull Number locationX, @NotNull Number locationY,
                                               @NotNull Number width, @NotNull Number height,
                                               @NotNull BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        return Exe.sync(
                lock, () -> generateImage(
                        null, A.fillMatrix(
                                t -> pixelGenerator.apply(t, t.applyEach(
                                        locationX, locationY, OpType.ADD, OpResultType.EXACT)),
                                new Color[width.intValue()][height.intValue()])), true);
    }
    public static @NotNull Image generateImage(@Nullable Lock lock,
                                               @NotNull NumExpr2D<?> location,
                                               @NotNull NumExpr2D<?> dimensions,
                                               @NotNull BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        return generateImage(lock, location.a(), location.b(), dimensions.a(), dimensions.b(), pixelGenerator);
    }
    public static @NotNull Image generateImage(@Nullable Lock lock,
                                               @NotNull Number width, @NotNull Number height,
                                               @NotNull Function<NumExpr2D<?>, Color> pixelGenerator) {
        return generateImage(lock, 0, 0, width, height, (loc, dim) -> pixelGenerator.apply(dim));
    }
    public static @NotNull Image generateImage(@Nullable Lock lock,
                                               @NotNull NumExpr2D<?> dimensions,
                                               @NotNull Function<NumExpr2D<?>, Color> pixelGenerator) {
        return generateImage(lock, dimensions.a(), dimensions.b(), pixelGenerator);
    }
    
    
    public static @NotNull Image generateImage(@Nullable Lock lock, @NotNull Color[][] pixelDefinitionMatrix) {
        return Exe.sync(lock, () -> {
            final int width = pixelDefinitionMatrix.length;
            final int height = pixelDefinitionMatrix[0].length;
            final WritableImage image = new WritableImage(width, height);
            
            A.iterateMatrix((matrixCoordinates, color) -> {
                image.getPixelWriter().setColor(matrixCoordinates.aI(), matrixCoordinates.bI(), color);
                return null;
            }, pixelDefinitionMatrix);
            
            return image;
        }, true);
    }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Speciality Image Generation">
    
    public static @NotNull WritableImage writable(@NotNull Image input, boolean forceCopy) {
        return (input instanceof WritableImage writableInput) && !forceCopy
               ? writableInput
               : new WritableImage(input.getPixelReader(), (int) input.getWidth(), (int) input.getHeight());
    }
    public static @NotNull WritableImage writable(@NotNull Image input) { return writable(input, false); }
    
    public static <T> @NotNull WritableImage generateTiledImage(@NotNull Number tileSize, @NotNull T @NotNull [] @NotNull [] sourceMatrix, @NotNull Function<T, Image> factory, @NotNull Number borderThickness) {
        if (sourceMatrix.length == 0)
            throw Exc.ex("Source matrix width must be greater than 0.");
        else if (sourceMatrix[0].length == 0)
            throw Exc.ex("Source matrix height must be greater than 0.");
        
        final WritableImage aggregateImage = new WritableImage(tileSize.intValue() * sourceMatrix.length, tileSize.intValue() * sourceMatrix[0].length);
        
        A.iterateMatrix((dimensions, tile) -> {
            final T t = sourceMatrix[dimensions.aI()][dimensions.bI()];
            if (t != null) {
                final WritableImage image = writable(generateBorderOn(factory.apply(t), borderThickness));
                if (image != null) {
                    aggregateImage.getPixelWriter().setPixels(
                            tileSize.intValue() * dimensions.aI(), tileSize.intValue() * dimensions.bI(),
                            tileSize.intValue(), tileSize.intValue(),
                            image.getPixelReader(),
                            0, 0);
                }
            }
            return null;
        }, sourceMatrix);
        
        return aggregateImage;
    }
    
    public static <T> @NotNull Image generateTiledImage(@NotNull Number tileSize, @NotNull T[] @NotNull [] sourceMatrix, @NotNull Function<T, Image> factory) {
        return generateTiledImage(tileSize, sourceMatrix, factory, 0);
    }
    
    public static @NotNull WritableImage generateFilledImage(@NotNull Number width, @NotNull Number height, @NotNull Color color) {
        final WritableImage image = new WritableImage(width.intValue(), height.intValue());
        for (int i = 0; i < width.intValue(); i++)
            for (int j = 0; j < height.intValue(); j++)
                image.getPixelWriter().setColor(i, j, color);
        return image;
    }
    public static @NotNull WritableImage generateFilledImage(@NotNull Number tileDim, @NotNull Color color) { return generateFilledImage(tileDim, tileDim, color); }
    
    public static @NotNull WritableImage generateCompositeImage(@NotNull Number width, @NotNull Number height, @NotNull Image... images) {
        final WritableImage compositeImage = new WritableImage(width.intValue(), height.intValue());
        for (int i = 0; i < width.intValue(); i++) {
            for (int j = 0; j < height.intValue(); j++) {
                Color color = null;
                for (int q = 0; q < images.length; q++) {
                    color = blendPixels(color, images[q].getPixelReader().getColor(i, j));
                }
                compositeImage.getPixelWriter().setColor(i, j, color);
            }
        }
        return compositeImage;
    }
    
    //<editor-fold desc=">>> Image Generation: Borders">
    
    private static final int BORDER_THICKNESS_FALLBACK = 1;
    private static final Color BORDER_COLOR_FALLBACK = Color.DIMGRAY;
    
    public static @NotNull WritableImage generateBorderImage(@NotNull Number width, @NotNull Number height, @NotNull Number thickness, @Nullable BiFunction<Integer, Integer, Color> pixelColorFactory) {
        return generateBorderOn(new WritableImage(width.intValue(), height.intValue()), thickness, pixelColorFactory);
    }
    
    public static @NotNull WritableImage generateBorderImage(@NotNull Number width, @NotNull Number height, @NotNull Number thickness, @Nullable Color borderColor) {
        return generateBorderImage(width, height, thickness, (x, y) -> borderColor);
    }
    public static @NotNull WritableImage generateBorderImage(@NotNull Number width, @NotNull Number height, @NotNull Number thickness) { return generateBorderImage(width, height, thickness, borderColor()); }
    public static @NotNull WritableImage generateBorderImage(@NotNull Number width, @NotNull Number height, @Nullable Color borderColor) { return generateBorderImage(width, height, BORDER_THICKNESS_FALLBACK, borderColor); }
    public static @NotNull WritableImage generateBorderImage(@NotNull Number width, @NotNull Number height) { return generateBorderImage(width, height, BORDER_THICKNESS_FALLBACK, borderColor()); }
    
    
    public static @NotNull WritableImage generateBorderImage(@NotNull NumExpr2D<?> dimensions, @NotNull Number thickness, @Nullable BiFunction<Integer, Integer, Color> pixelColorFactory) {
        return generateBorderOn(new WritableImage(dimensions.aI(), dimensions.bI()), thickness, pixelColorFactory);
    }
    
    public static @NotNull WritableImage generateBorderImage(@NotNull NumExpr2D<?> dimensions, @NotNull Number thickness, @Nullable Color borderColor) {
        return generateBorderImage(dimensions, thickness, (x, y) -> borderColor);
    }
    public static @NotNull WritableImage generateBorderImage(@NotNull NumExpr2D<?> dimensions, @NotNull Number thickness) { return generateBorderImage(dimensions, thickness, borderColor()); }
    public static @NotNull WritableImage generateBorderImage(@NotNull NumExpr2D<?> dimensions, @Nullable Color borderColor) { return generateBorderImage(dimensions, BORDER_THICKNESS_FALLBACK, borderColor); }
    public static @NotNull WritableImage generateBorderImage(@NotNull NumExpr2D<?> dimensions) { return generateBorderImage(dimensions, BORDER_THICKNESS_FALLBACK, borderColor()); }
    
    //
    
    public static @NotNull WritableImage generateBorderOn(@NotNull Image image, @NotNull Number thickness, @Nullable BiFunction<Integer, Integer, Color> pixelColorFactory) {
        if (thickness.intValue() == 0)
            return writable(image);
        
        pixelColorFactory = pixelColorFactory != null ? pixelColorFactory : (integer, integer2) -> borderColor();
        final WritableImage writeableImage = writable(image);
        final int width = (int) writeableImage.getWidth();
        final int height = (int) writeableImage.getHeight();
//        System.out.println("Generating Borders w/ Dimensions " + new Num2D(width, height) + " and a thickness of " + thickness);
        for (int x = 0; x < width; x++) {
            for (int t = 0; t < thickness.intValue(); t++) {
//                System.out.println("Setting Pixel Color " + new Num2D(x, t) + "  w/ Color: " + pixelColorFactory.apply(x, 0));
                writeableImage.getPixelWriter().setColor(x, t, pixelColorFactory.apply(x, 0));
                writeableImage.getPixelWriter().setColor(x, height - (t + 1), pixelColorFactory.apply(x, height - 1));
            }
        }
        for (int y = 0; y < height; y++) {
            for (int t = 0; t < thickness.intValue(); t++) {
                writeableImage.getPixelWriter().setColor(t, y, pixelColorFactory.apply(0, y));
                writeableImage.getPixelWriter().setColor(width - (t + 1), y, pixelColorFactory.apply(width - 1, y));
            }
        }
        return writeableImage;
    }
    
    public static @NotNull WritableImage generateBorderOn(@NotNull Image image, @NotNull Number thickness, @Nullable Color borderColor) { return generateBorderOn(image, thickness, (x, y) -> borderColor(borderColor)); }
    public static @NotNull WritableImage generateBorderOn(@NotNull Image image, @NotNull Number thickness) { return generateBorderOn(image, thickness, borderColor()); }
    public static @NotNull WritableImage generateBorderOn(@NotNull Image image, @Nullable Color borderColor) { return generateBorderOn(image, BORDER_THICKNESS_FALLBACK, borderColor); }
    public static @NotNull WritableImage generateBorderOn(@NotNull Image image) { return generateBorderOn(image, BORDER_THICKNESS_FALLBACK, borderColor()); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc=">> Internal: Image Generation">
    
    private static Color blendPixels(Color pixelB, Color pixelA) {
        if (pixelA == null && pixelB == null)
            throw Exc.ex("PixelA & PixelB cannot both be null.");
        else if (pixelA == null && pixelB != null)
            return pixelB;
        else if (pixelB == null && pixelA != null)
            return pixelA;
        else {
            int aA = (int) (pixelA.getOpacity() * 255);
            int rA = (int) (pixelA.getRed() * 255);
            int gA = (int) (pixelA.getGreen() * 255);
            int bA = (int) (pixelA.getBlue() * 255);
            
            int aB = (int) (pixelB.getOpacity() * 255);
            int rB = (int) (pixelB.getRed() * 255);
            int gB = (int) (pixelB.getGreen() * 255);
            int bB = (int) (pixelB.getBlue() * 255);
            
            int aOut = aA + (aB * ((255 - aA) / 255));
            int rOut = ((rA * aA) + (rB * aB) * ((255 - aA) / 255)) / aOut;
            int gOut = ((gA * aA) + (gB * aB) * ((255 - aA) / 255)) / aOut;
            int bOut = ((bA * aA) + (bB * aB) * ((255 - aA) / 255)) / aOut;
            
            return new Color(rOut / 255D, gOut / 255D, bOut / 255D, aOut / 255D);
        }
    }
    
    //
    
    private static @NotNull Color borderColor(@Nullable Color color) { return color != null ? color : BORDER_COLOR_FALLBACK; }
    private static @NotNull Color borderColor() { return borderColor(null); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    
    public static Canvas clearCanvasUnsafe(Canvas canvas) {
        return clearCanvas(canvas, null);
    }
    
    public static Canvas clearCanvas(Canvas canvas) {
        return clearCanvas(canvas, new ReentrantLock());
    }
    
    public static Canvas clearCanvas(Canvas canvas, ReentrantLock lock) {
        Exc.nullCheck(canvas, "Canvas Input");
        
        if (lock != null)
            try {
                clearCanvasImpl(canvas);
            } finally {
                lock.unlock();
            }
        else
            clearCanvasImpl(canvas);
        
        return canvas;
    }
    
    private static void clearCanvasImpl(@NotNull Canvas canvas) {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    
    public static <T extends Node> T togglePickOnBounds(T node, boolean pickOnBounds) {
        Exc.nullCheck(node, "Input Node").setPickOnBounds(pickOnBounds);
        if (node instanceof Canvas)
            node.setMouseTransparent(!pickOnBounds);
        if (node instanceof Region)
            ((Region) node).getChildrenUnmodifiable().forEach(child -> togglePickOnBounds(child, pickOnBounds));
        return node;
    }
    
    
    public static @NotNull BorderPane progressOverlay(Node parent) {
        return progressOverlay(parent, null, -1, null);
    }
    
    public static @NotNull BorderPane progressOverlay(Node parent, ObservableValue<? extends Number> progressProperty) {
        return progressOverlay(parent, null, -1, progressProperty);
    }
    
    public static @NotNull BorderPane progressOverlay(Node parent, Node backgroundContent, int maxSize, ObservableValue<? extends Number> progressProperty) {
        if (maxSize == -1)
            maxSize = 60;
        ProgressIndicator indicator = new ProgressIndicator();
        if (progressProperty != null)
            indicator.progressProperty().bind(progressProperty);
        else
            indicator.setProgress(-1.0);
        indicator.setMaxSize(maxSize, maxSize);
        StackPane stackPane = new StackPane();
        if (backgroundContent != null)
            stackPane.getChildren().add(backgroundContent);
        stackPane.getChildren().add(indicator);
        return constructOverlay(parent, stackPane, -1.0,
                                OverlayBackgroundOpacityType.OPAQUE,
                                OverlayResizeType.FILL);
    }
    
    public static @Nullable ProgressIndicator getOverlayIndicator(BorderPane overlayPane) {
        Objects.requireNonNull(overlayPane, "Overlay Pane cannot be null");
        Node center = overlayPane.getCenter();
        if (center instanceof StackPane) {
            List<Node> stackOverlayChildren = overlayPane.getChildren();
            for (Node child: stackOverlayChildren)
                if (child instanceof ProgressIndicator)
                    return (ProgressIndicator) child;
        }
        return null;
    }
    
    /**
     * Creates and then returns an constructOverlay {@link Pane} with the specified parent {@link Node} and {@link Node content}.
     *
     * @param parent  The parent {@link Node}.
     * @param content The {@link Node content}.
     *
     * @return an constructOverlay {@link Pane} with the specified parent {@link Node} and {@link Node content}.
     */
    public static @NotNull BorderPane constructOverlay(Node parent, Node content) {
        return constructOverlay(parent, content, -1.0, OverlayBackgroundOpacityType.TRANSPARENT, OverlayResizeType.FILL);
    }
    
    /**
     * Creates and then returns an constructOverlay {@link Pane} with the specified parent {@link Node} and {@link Node content}.
     *
     * @param parent                The parent {@link Node}.
     * @param content               The {@link Node content}.
     * @param opacity               The opacity of the {@link Node content}. -1.0 for default opacity.
     * @param backgroundOpacityType The {@link OverlayBackgroundOpacityType}.
     * @param resizeType            The {@link OverlayResizeType}.
     *
     * @return an constructOverlay {@link Pane} with the specified parent {@link Node} and {@link Node content}.
     */
    public static @NotNull BorderPane constructOverlay(Node parent, Node content, double opacity, OverlayBackgroundOpacityType backgroundOpacityType, OverlayResizeType resizeType) {
        Objects.requireNonNull(parent, "Parent cannot be null");
        Objects.requireNonNull(backgroundOpacityType, "Background Opacity Type property cannot be null");
        Objects.requireNonNull(resizeType, "Resize Type property cannot be null");
        if (opacity > 1.0)
            throw new IndexOutOfBoundsException("Opacity must be less than or equal to 1.0 (" + opacity + ")");
        else if (opacity != -1.0 && opacity < 0.0)
            throw new IndexOutOfBoundsException("Opacity must be either greater than or equal to 0.0, or -1.0 (" + opacity + ")");
        
        Parent theParent;
        StackPane rootStackPane;
        if (parent instanceof StackPane)
            rootStackPane = (StackPane) parent;
        else if (parent instanceof BorderPane) {
            Node center = ((BorderPane) parent).getCenter();
            if (center != null && center instanceof StackPane)
                rootStackPane = (StackPane) center;
            else
                throw new RuntimeException("If parent is a BorderPane, then parent center must be a Stack Pane (center=" + center + ")");
        } else {
            Scene scene = parent.getScene();
            if (scene != null) {
                Parent root = scene.getRoot();
                if (root != null) {
                    if (root instanceof StackPane) {
                        rootStackPane = (StackPane) root;
                    } else if (root instanceof BorderPane) {
                        Node center = ((BorderPane) root).getCenter();
                        if (center != null && center instanceof StackPane)
                            rootStackPane = (StackPane) center;
                        else
                            throw new RuntimeException("If root is a BorderPane, then root center must be a Stack Pane (center=" + center + ")");
                    } else
                        throw new ClassCastException("Root must be instance of Stack Pane or BorderPane (root=" + TB.getSimpleName(root) + ")");
                } else
                    throw new NullPointerException("Root cannot be null");
            } else
                throw new NullPointerException("Scene cannot be null");
        }
        
        //
        
        BorderPane overlay = new BorderPane();
        
        overlay.prefWidthProperty().bind(rootStackPane.widthProperty());
        overlay.prefHeightProperty().bind(rootStackPane.heightProperty());
        
        if (content != null) {
            overlay.setCenter(content);
            
            //Content properties
            if (opacity != -1.0)
                applyCSSInLine(content, false, "-fx-opacity: " + opacity);
            if (resizeType == OverlayResizeType.FILL) {
                if (content instanceof Region) {
                    Region contentRegion = (Region) content;
                    contentRegion.prefWidthProperty().bind(rootStackPane.widthProperty());
                    contentRegion.prefHeightProperty().bind(rootStackPane.heightProperty());
                } else if (content instanceof ImageView) {
                    ImageView contentImageView = (ImageView) content;
                    contentImageView.fitWidthProperty().bind(rootStackPane.widthProperty());
                    contentImageView.fitHeightProperty().bind(rootStackPane.heightProperty());
                }
            }
        }
        
        //Background properties
        if (backgroundOpacityType == OverlayBackgroundOpacityType.TRANSPARENT)
            applyCSS(overlay, true, "empty");
        
        rootStackPane.getChildren().add(overlay);
        
        return overlay;
    }
    
    
    public static void constructDraggableNode(Node... nodes) {
        Objects.requireNonNull(nodes, "Nodes cannot be null");
        ObjectProperty<Stage> stageProperty = new SimpleObjectProperty<>();
        
        for (Node node: nodes) {
            Objects.requireNonNull(node, "Node cannot be null");
            if (stageProperty.get() == null)
                stageProperty.set(getSceneStage(node.getScene()));
            Objects.requireNonNull(stageProperty.get());
            
            Point p = new Point();
            node.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Scene scene = stageProperty.get().getScene();
                    double px = (event.getSceneX() / scene.getWidth()) * 100.0;
                    double py = (event.getSceneY() / scene.getHeight()) * 100.0;
                    p.setLocation(px, py);
                }
            });
            node.setOnMouseDragged(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Scene scene = stageProperty.get().getScene();
                    stageProperty.get().setMaximized(false);
                    stageProperty.get().setX(event.getScreenX() - (scene.getWidth() * (p.x / 100.0)));
                    stageProperty.get().setY(event.getScreenY() - (scene.getHeight() * (p.y / 100.0)));
                }
            });
            
            node.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)
                    stageProperty.get().setMaximized(!stageProperty.get().isMaximized());
            });
        }
    }
    
    public static @NotNull NodeMover constructResizableNode(Stage stage, Region corner, Region top, Region bottom, Region left, Region right, Node... excludeNodes) {
        NodeMover mover = new NodeMover(stage, corner, top, bottom, left, right, excludeNodes);
        mover.begin();
        return mover;
    }
    
    public static boolean isResizing(InputEvent event) {
        Scene scene = getEventScene(event);
        if (scene != null) {
            Cursor cursor = scene.getCursor();
            if (cursor != null)
                return cursor == Cursor.H_RESIZE || cursor == Cursor.V_RESIZE || cursor == Cursor.SE_RESIZE;
        }
        return false;
    }
    
    
    public static @Nullable Scene getEventScene(InputEvent event) {
        Objects.requireNonNull(event, "Event cannot be null");
        Object source = event.getSource();
        if (source != null) {
            if (source instanceof Scene)
                return (Scene) source;
            else if (!(source instanceof Node))
                throw new ClassCastException("Event source must be a Node");
            return ((Node) source).getScene();
        }
        return null;
    }
    
    public static @Nullable Stage getSceneStage(Scene scene) {
        Objects.requireNonNull(scene, "Scene cannot be null");
        Window window = scene.getWindow();
        if (window != null) {
            if (!(window instanceof Stage))
                throw new ClassCastException("Scene window must be a Stage");
            return (Stage) window;
        }
        return null;
    }
    
    
    public static void passdownOrder(Node root, Consumer<Node> action) {
        Exc.nullCheck(root, "Root node cannot be null");
        Exc.nullCheck(action, "Task cannot be null");
        if (root instanceof Parent)
            ((Parent) root).getChildrenUnmodifiable().forEach(child -> passdownOrder(child, action));
    }
    
    //
    
    //<editor-fold desc="Node Sizing">
    
    @Contract("_, _, _ -> param1")
    public static <T extends Region> @NotNull T bindToParent(@NotNull T child, @NotNull Region parent, boolean addTo) {
        return bindToParent(child, parent, BindOrientation.BOTH, BindType.BOTH, addTo);
    }
    
    @Contract("_, _, _, _, _ -> param1")
    public static <T extends Region> @NotNull T bindToParent(@NotNull T child, @NotNull Region parent, @NotNull BindOrientation bindOrientation, @NotNull BindType bindType, boolean addTo) {
        return bindToParent(child, parent, true, false, bindOrientation, bindType, addTo);
    }
    
    @Contract("_, _, _, _, _, _ -> param1")
    public static <T extends Region> @NotNull T bindToParent(
            @NotNull T child,
            @NotNull Region parent,
            @Nullable ObservableDoubleValue observableOffset,
            @NotNull BindOrientation bindOrientation,
            @NotNull BindType bindType,
            boolean addTo) {
        return bindToParent(child, parent, true, false, observableOffset, bindOrientation, bindType, addTo);
    }
    
    @Contract("_, _, _, _, _, _, _ -> param1")
    public static <T extends Region> @NotNull T bindToParent(
            @NotNull T child,
            @NotNull Region parent,
            boolean includePadding,
            boolean includeInsets,
            @NotNull BindOrientation bindOrientation,
            @NotNull BindType bindType,
            boolean addTo) {
        return bindToParent(child, parent, includePadding, includeInsets, null, bindOrientation, bindType, addTo);
    }
    
    /**
     * <p>Binds the specified {@link Region child} to the specified {@link Region parent} using the specified {@link BindOrientation} and {@link BindType} rules.</p>
     *
     * <p><b>Details</b></p>
     * <ol>
     *     <li>
     *         If {@code addTo} is true, the specified {@link Region child} will be added to the specified {@link Region parent} as a {@link Pane#getChildren() child element}.
     *         <ul>
     *             <li>If {@code addTo} is true, then the specified {@link Region} must be an instance of {@link Pane}.</li>
     *             <li>If {@code addTo} is true and the specified {@link Region} is <i>not</i> an instance of {@link Pane}, an {@link RuntimeException exception} is thrown.</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param child            The {@link Region child} element.
     * @param parent           The {@link Region parent} element that the {@link Region child} is being bound to in size.
     * @param includePadding   True if {@link Region#paddingProperty() padding} should be counted in size calculations, false if it should not.
     * @param includeInsets    True if {@link Region#insetsProperty() insets} should be counted in size calculations, false if they should not.
     * @param observableOffset An {@link ObservableValue} that defines any additional size modifications that should be added to the {@link Region child's} calculated dimensions.
     *                         If {@code null}, then an offset of {@code 0} is used.
     * @param bindOrientation  The {@link BindOrientation} defining which {@link Region parent} dimensions the {@link Region child} should be bound to.
     * @param bindType         The {@link BindType} defining which {@link Region child} dimension properties should be bound to the parent.
     *                         Options are {@link Region#prefWidthProperty() Pref. Width} and {@link Region#prefHeightProperty() Pref. Height}, {@link Region#maxWidthProperty() Max Width} and {@link Region#maxHeightProperty() Max Height}, or all of the above.
     * @param addTo            True if the {@link Region child} element should be automatically added to the specified {@link Region parent}, false if it should not (useful when the child has already been added to the parent upon method call).
     * @param <T>              The {@link Region Region Implementation} of the {@code child} element.
     *
     * @return The specified {@link Region child} element. Useful in chained method calls, though this method can be thought of as {@code functionally void}.
     */
    // TO-EXPAND
    @Contract("_, _, _, _, _, _, _, _ -> param1")
    public static <T extends Region> @NotNull T bindToParent(
            @NotNull T child,
            @NotNull Region parent,
            boolean includePadding,
            boolean includeInsets,
            @Nullable ObservableDoubleValue observableOffset,
            @NotNull BindOrientation bindOrientation,
            @NotNull BindType bindType,
            boolean addTo) {
        Exc.nullCheck(child, "Region");
        Exc.nullCheck(parent, "Parent Region");
        Exc.nullCheck(bindOrientation, "Bind Orientation");
        Exc.nullCheck(bindType, "Bind Type");
        
        final ObservableDoubleValue observableOffsetImpl = observableOffset == null ? new SimpleDoubleProperty(0.0) : observableOffset;
        final DoubleProperty widthProperty;
        final DoubleProperty heightProperty;
        
        if (addTo)
            if (parent instanceof Pane) ((Pane) parent).getChildren().add(child);
            else throw Exc.ex("Parent must be an implementation of Pane!  (" + parent.getClass() + ")");
        
        if (bindType == BindType.PREF || bindType == BindType.BOTH) {
            widthProperty = child.prefWidthProperty();
            heightProperty = child.prefHeightProperty();
        } else if (bindType == BindType.MAX) {
            widthProperty = child.maxWidthProperty();
            heightProperty = child.maxHeightProperty();
        } else
            throw Exc.unsupported("Unknown BindType: " + bindType);
        
        if (bindOrientation == BindOrientation.WIDTH || bindOrientation == BindOrientation.BOTH) {
            widthProperty.bind(Bindings.createDoubleBinding(
                    () -> getNodeSize(parent, includePadding, includeInsets, observableOffsetImpl.get(), BindOrientation.WIDTH),
                    parent.widthProperty(),
                    parent.heightProperty(),
                    parent.paddingProperty(),
                    parent.insetsProperty(),
                    observableOffsetImpl));
            if (bindType == BindType.BOTH)
                child.maxWidthProperty().bind(child.prefWidthProperty());
        }
        
        if (bindOrientation == BindOrientation.HEIGHT || bindOrientation == BindOrientation.BOTH) {
            heightProperty.bind(Bindings.createDoubleBinding(
                    () -> getNodeSize(parent, includePadding, includeInsets, observableOffsetImpl.get(), BindOrientation.HEIGHT),
                    parent.widthProperty(),
                    parent.heightProperty(),
                    parent.paddingProperty(),
                    parent.insetsProperty(),
                    observableOffsetImpl));
            if (bindType == BindType.BOTH)
                child.maxHeightProperty().bind(child.prefHeightProperty());
        }
        
        return child;
    }
    
    public static double getNodeSize(Region region, boolean includePadding, boolean includeInsets, BindOrientation bindOrientation) {
        return getNodeSize(region, includePadding, includeInsets, 0.0, bindOrientation);
    }
    
    public static double getNodeSize(Region region, boolean includePadding, boolean includeInsets, double offset, BindOrientation bindOrientation) {
        Exc.nullCheck(region, "Region");
        Exc.nullCheck(bindOrientation, "BindOrientation");
        
        Insets _insets = region.getInsets();
        Insets _padding = region.getPadding();
        if (bindOrientation == BindOrientation.WIDTH)
            return region.getWidth() + offset - (includePadding ? _padding.getLeft() + _padding.getRight() : 0) + (includeInsets ? _insets.getLeft() + _insets.getRight() : 0);
        else if (bindOrientation == BindOrientation.HEIGHT)
            return region.getHeight() + offset - (includePadding ? _padding.getTop() + _padding.getBottom() : 0) + (includeInsets ? _insets.getTop() + _insets.getBottom() : 0);
        else
            throw Exc.unsupported("BindOrientation \"" + bindOrientation + "\" is not supported.");
    }
    
    public enum BindOrientation {
        WIDTH,
        HEIGHT,
        BOTH
    }
    
    public enum BindType {
        PREF,
        MAX,
        BOTH
    }
    
    //</editor-fold>
    
    
    //<editor-fold desc="Combo Box/List">
    
    /**
     * Turns the specified {@link ComboBox} into an auto-complete box.
     *
     * @param <T>        The type of elements in the specified {@link ComboBox}.
     * @param comboBox   The {@link ComboBox}.
     * @param mode       The {@link AutoCompleteMode} for the auto-complete box.
     * @param promptText The text that is displayed when no items are selected.
     *
     * @see #applyCellFactory(ComboBox)
     * @see AutoCompleteMode
     */
    public static <T> void autoCompleteComboBox(ComboBox<T> comboBox, AutoCompleteMode mode, String promptText) {
        applyCellFactory(comboBox);
        if (mode != null) {
            ObservableList<T> data = comboBox.getItems();
            comboBox.setEditable(true);
            if (promptText != null)
                comboBox.setPromptText(promptText);
            comboBox.getEditor().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (comboBox.getSelectionModel().getSelectedIndex() < 0)
                    comboBox.getEditor().setText(null);
            });
            comboBox.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> comboBox.hide());
            comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                
                private boolean moveCaretToPos = false;
                private int caretPos;
                
                @Override public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.UP) {
                        caretPos = -1;
                        moveCaret(comboBox.getEditor().getText().length());
                        return;
                    } else if (event.getCode() == KeyCode.DOWN) {
                        if (!comboBox.isShowing())
                            comboBox.show();
                        caretPos = -1;
                        moveCaret(comboBox.getEditor().getText().length());
                        return;
                    } else if (event.getCode() == KeyCode.BACK_SPACE) {
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                    } else if (event.getCode() == KeyCode.DELETE) {
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                    }
                    
                    if (event.getCode() == KeyCode.RIGHT ||
                        event.getCode() == KeyCode.LEFT ||
                        event.isControlDown() ||
                        event.getCode() == KeyCode.HOME ||
                        event.getCode() == KeyCode.END ||
                        event.getCode() == KeyCode.TAB)
                        return;
                    
                    ObservableList<T> list = FXCollections.observableArrayList();
                    DATA:
                    for (T aData: data)
                        if (aData != null)
                            if (aData instanceof Listable) {
                                String[] searchKeys = ((Listable) aData).getSearchKeys();
                                if (searchKeys != null)
                                    for (String searchKey: searchKeys)
                                        if (isSearchKeyValid(searchKey, mode)) {
                                            list.add(aData);
                                            continue DATA;
                                        }
                            } else if (isSearchKeyValid(aData.toString(), mode))
                                list.add(aData);
                    String t = comboBox.getEditor().getText();
                    comboBox.setItems(list);
                    comboBox.getEditor().setText(t);
                    if (!moveCaretToPos)
                        caretPos = -1;
                    moveCaret(t.length());
                    if (!list.isEmpty())
                        comboBox.show();
                }
                
                private boolean isSearchKeyValid(String searchKey, AutoCompleteMode mode) {
                    if (searchKey != null)
                        if (mode == AutoCompleteMode.STARTS_WITH && searchKey.toLowerCase().startsWith(comboBox.getEditor().getText().toLowerCase()))
                            return true;
                        else if (mode == AutoCompleteMode.CONTAINING && searchKey.toLowerCase().contains(comboBox.getEditor().getText().toLowerCase()))
                            return true;
                    return false;
                }
                
                private void moveCaret(int textLength) {
                    if (caretPos == -1)
                        comboBox.getEditor().positionCaret(textLength);
                    else
                        comboBox.getEditor().positionCaret(caretPos);
                    moveCaretToPos = false;
                }
            });
        }
    }
    
    /**
     * Applies the cell factory data to the specified {@link ComboBox} so that the elements contained within are displayed properly.
     * <p>
     * This method is automatically called by {@link #autoCompleteComboBox(ComboBox, AutoCompleteMode, String)}.
     * <p>
     * For a list of supported types, see {@link #getShortText(Object)}.
     *
     * @param <T>      The type of elements in the specified {@link ComboBox}.
     * @param comboBox The {@link ComboBox}.
     *
     * @see #getShortText(Object)
     * @see #autoCompleteComboBox(ComboBox, AutoCompleteMode, String)
     */
    public static <T> void applyCellFactory(@NotNull ComboBox<T> comboBox) {
        comboBox.setConverter(new StringConverter<T>() {
            
            private final Map<String, T> map = new HashMap<>();
            
            @Override public String toString(T obj) {
                String str = getShortText(obj);
                if (str != null) {
                    map.put(str, obj);
                    return str;
                }
                return "";
            }
            
            @Override public T fromString(String str) {
                if (!map.containsKey(str)) {
                    comboBox.setValue(null);
                    comboBox.getEditor().clear();
                    return null;
                }
                return map.get(str);
            }
        });
        comboBox.setButtonCell(getCell());
        comboBox.setCellFactory(param -> getCell());
    }
    
    /**
     * Applies the cell factory data to the specified {@link ListView} so that the elements contained within are displayed properly.
     * <p>
     * For a list of supported types, see {@link #getShortText(Object)}.
     *
     * @param <T>      The type of elements in the specified {@link ListView}.
     * @param listView The {@link ListView}.
     *
     * @see #getShortText(Object)
     */
    public static <T> void applyCellFactory(@NotNull ListView<T> listView) {
        listView.setCellFactory((ListView<T> param) -> getCell());
    }
    
    /**
     * Refreshes the specified {@link ListView}.
     *
     * @param <T>      The type of elements.
     * @param listView The {@link ListView} being refreshed.
     */
    public static <T> void refresh(ListView<T> listView) {
        final T selectedItem = getSelectedValue(listView);
        final ObservableList<T> items = FXCollections.observableArrayList();
        items.addAll(listView.getItems());
        listView.<T>setItems(null);
        listView.<T>setItems(items);
        listView.getSelectionModel().select(selectedItem);
    }
    
    /**
     * Returns a new {@link ListCell} that can be used to format {@link ListView ListViews} and {@link ComboBox ComboBoxs}.
     *
     * @param <T> The type of elements.
     *
     * @return A new {@link ListCell} that can be used to format {@link ListView ListViews} and {@link ComboBox ComboBoxs}.
     */
    @Contract(" -> new")
    private static <T> @NotNull ListCell<T> getCell() {
        return new ListCell<T>() {
            @Override public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(getShortText(item));
                    if (item instanceof Colorable) {
                        java.awt.Color awtColor = ((Colorable) item).getColorAWT();
                        if (awtColor != null)
                            setTextFill(Color.color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), awtColor.getAlpha()));
                    }
                } else
                    setText(null);
            }
        };
    }
    
    /**
     * Returns the currently selected value of the specified {@link ComboBox}.
     *
     * @param <T>      The type of elements in the specified {@link ComboBox}.
     * @param comboBox The {@link ComboBox}.
     *
     * @return The currently selected value of the specified {@link ComboBox}, or null if no element is selected.
     */
    public static <T> @Nullable T getSelectedValue(@NotNull ComboBox<T> comboBox) {
        if (comboBox.getSelectionModel().getSelectedIndex() < 0)
            return null;
        else
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
    }
    
    /**
     * Returns the currently selected value of the specified {@link ListView}.
     *
     * @param <T>      The type of elements in the specified {@link ListView}.
     * @param listView The {@link ListView}.
     *
     * @return The currently selected value of the specified {@link ListView}, or null if no element is selected.
     */
    public static <T> @Nullable T getSelectedValue(@NotNull ListView<T> listView) {
        if (listView.getSelectionModel().getSelectedIndex() < 0)
            return null;
        else
            return listView.getItems().get(listView.getSelectionModel().getSelectedIndex());
    }
    
    /**
     * Adds the specified item to the specified {@link ListView}, and then selects the newly added item.
     *
     * @param <T>      The type of elements in the specified {@code ListView}.
     * @param item     The item being added.
     * @param listView The {@code ListView} being added to.
     * @param focus    True if the {@code ListView} should be focused after the value has been added, false if it should not.
     */
    public static synchronized <T> void addElement(T item, @NotNull ListView<T> listView, boolean focus) {
        listView.getItems().add(item);
        listView.getSelectionModel().select(item);
        if (focus)
            listView.requestFocus();
    }
    
    /**
     * Adds the specified item to the specified {@link ListView}, and then selects the newly added item.
     * <p>
     * Does not focus the {@code ComboBox} after adding the specified element.
     *
     * @param <T>      The type of elements in the specified {@code ListView}.
     * @param item     The item being added.
     * @param listView The {@code ListView} being added to.
     *
     * @see #addElement(Object, ListView, boolean)
     */
    public static synchronized <T> void addElement(T item, ListView<T> listView) {
        addElement(item, listView, false);
    }
    
    /**
     * Adds the specified item to the specified {@link ComboBox}, and then selects the newly added item.
     *
     * @param <T>      The type of elements in the specified {@code ComboBox}.
     * @param item     The item being added.
     * @param comboBox The {@code ComboBox} being added to.
     * @param focus    True if the {@code ComboBox} should be focused after the value has been added, false if it should not.
     */
    public static synchronized <T> void addElement(T item, @NotNull ComboBox<T> comboBox, boolean focus) {
        comboBox.getItems().add(item);
        comboBox.getSelectionModel().select(item);
        if (focus)
            comboBox.requestFocus();
    }
    
    /**
     * Adds the specified item to the specified {@link ComboBox}, and then selects the newly added item.
     * <p>
     * Does not focus the {@code ComboBox} after adding the specified element.
     *
     * @param <T>      The type of elements in the specified {@code ComboBox}.
     * @param item     The item being added.
     * @param comboBox The {@code ComboBox} being added to.
     *
     * @see #addElement(Object, ComboBox, boolean)
     */
    public static synchronized <T> void addElement(T item, ComboBox<T> comboBox) {
        addElement(item, comboBox, false);
    }
    
    /**
     * Deletes the selected item from the specified {@link ListView}, and then selects the next logical element.
     *
     * @param <T>      The type of elements in the specified {@link ListView}.
     * @param listView The {@link ListView}.
     * @param focus    True if the {@link ListView} should be focused after the value has been deleted, false otherwise.
     */
    @SuppressWarnings("Duplicates")
    public static synchronized <T> void removeSelected(ListView<T> listView, boolean focus) {
        runFX(() -> {
            final T selectedItem = getSelectedValue(listView);
            if (selectedItem != null) {
                int index = listView.getItems().indexOf(selectedItem);
                listView.getItems().remove(selectedItem);
                T nextItem;
                if (index < listView.getItems().size())
                    nextItem = listView.getItems().get(index);
                else if (listView.getItems().size() >= 1)
                    nextItem = listView.getItems().get(index - 1);
                else
                    nextItem = null;
                listView.getSelectionModel().select(nextItem);
                if (focus)
                    listView.requestFocus();
            }
        }, true);
    }
    
    /**
     * Deletes the selected item from the specified {@link ListView}, and then selects the next logical element.
     *
     * @param <T>      The type of elements in the specified {@link ListView}.
     * @param comboBox The {@link ListView}.
     * @param focus    True if the {@link ListView} should be focused after the value has been deleted, false otherwise.
     */
    @SuppressWarnings("Duplicates")
    public static synchronized <T> void removeSelected(ComboBox<T> comboBox, boolean focus) {
        runFX(() -> {
            final T selectedItem = getSelectedValue(comboBox);
            if (selectedItem != null) {
                int index = comboBox.getItems().indexOf(selectedItem);
                comboBox.getItems().remove(selectedItem);
                T nextItem;
                if (index < comboBox.getItems().size())
                    nextItem = comboBox.getItems().get(index);
                else if (comboBox.getItems().size() >= 1)
                    nextItem = comboBox.getItems().get(index - 1);
                else
                    nextItem = null;
                comboBox.getSelectionModel().select(nextItem);
                if (focus)
                    comboBox.requestFocus();
            }
        }, true);
    }
    
    /**
     * Adds all of the enums of the specified enum type to the specified {@link ComboBox}, and then sets the selected value to the enum specified.
     * <p>
     * If the specified {@link ComboBox} or the specified enum is null, this method returns silently and does nothing.
     *
     * @param <T>           The type of elements in the specified {@link ComboBox}.
     * @param comboBox      The {@link ComboBox}.
     * @param t             The type of enum.
     * @param selectFirst   True if the first value should be selected, false if the specified value should be selected.
     * @param excludeValues The values that should be excluded from the {@link ComboBox}.
     *
     * @deprecated Because this method is inefficient - O(n^n)
     */
    public static <T extends Enum> void addEnumsToComboBox(ComboBox<T> comboBox, T t, boolean selectFirst, T... excludeValues) {
        if (comboBox != null) {
            applyCellFactory(comboBox);
            if (t != null) {
                final Enum[] e_list = Enu.list(t);
                for (Enum e: e_list)
                    if (!A.contains(e, excludeValues))
                        comboBox.getItems().add((T) e);
                
                comboBox.setValue(selectFirst ? (T) e_list[0] : t);
            }
        }
    }
    
    /**
     * Returns a getText representation of the specified {@link Object}, or null if the specified {@link Object} is null.
     * <p>
     * This method supports the following:
     * <ol>
     * <li>The {@link Listable} interface (uses the {@link Listable#getShortText()} method</li>
     * <li>Enums (uses the {@link Str#enumToString(Enum)} method)</li>
     * <li>Non-null Objects (uses the {@link Object#toString()} method)</li>
     * </ol>
     *
     * @param obj The {@link Object}.
     *
     * @return A getText representation of the specified {@link Object}, or null if the specified {@link Object} is null.
     */
    public static String getShortText(Object obj) {
        if (obj != null) {
            String str;
            if (obj instanceof Listable)
                str = ((Listable) obj).getShortText();
            else if (obj instanceof Enum)
                str = Str.enumToString((Enum) obj);
            else
                str = obj.toString();
            return str;
        }
        return null;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Size Locks">
    
    /**
     * No minimum size.
     */
    public static final int NO_MIN_SIZE_LOCK = 0;
    /**
     * An unbounded minimum size.
     */
    public static final int UNBOUNDED_SIZE_LOCK = -1;
    /**
     * The current size.
     */
    public static final int CURRENT_SIZE_LOCK = -2;
    /**
     * The current minimum size.
     */
    public static final int NO_CHANGE_SIZE_LOCK = Integer.MIN_VALUE;
    
    /**
     * Locks the size of the specified {@link Stage} based on the specified {@link DialogsFX.SizeType}.
     *
     * @param stage    The {@link Stage} being locked.
     * @param sizeType The {@link DialogsFX.SizeType} that controls how the {@link Stage} can be resized.
     *
     * @return True if the {@link Stage Stage's} size was locked successfully, false otherwise.
     */
    public static boolean lockSize(Stage stage, DialogsFX.SizeType sizeType) {
        if (stage != null && sizeType != null) {
            if (sizeType == DialogsFX.SizeType.MINIMUM_SIZE) {
                stage.setMinWidth(stage.getWidth());
                stage.setMinHeight(stage.getHeight());
            } else if (sizeType == DialogsFX.SizeType.NON_RESIZEABLE)
                stage.setResizable(false);
            else if (sizeType == DialogsFX.SizeType.RESIZEABLE)
                stage.setResizable(true);
            return true;
        }
        return false;
    }
    
    /**
     * Locks the size of the specified {@link Stage} to the specified {@code minWidth} and {@code minHeight}.
     *
     * @param stage     The {@link Stage} being locked.
     * @param minWidth  The minimum width.
     * @param minHeight The minimum height.
     *
     * @return True if the {@link Stage Stage's} size was locked successfully, false otherwise.
     *
     * @see #NO_MIN_SIZE_LOCK
     * @see #UNBOUNDED_SIZE_LOCK
     * @see #CURRENT_SIZE_LOCK
     * @see #NO_CHANGE_SIZE_LOCK
     */
    public static boolean lockSize(Stage stage, double minWidth, double minHeight) {
        if (stage != null) {
            if (minWidth == -1)
                minWidth = Integer.MAX_VALUE;
            else if (minWidth == -2)
                minWidth = stage.getWidth();
            
            if (minHeight == -1)
                minHeight = Integer.MAX_VALUE;
            else if (minHeight == -2)
                minHeight = stage.getHeight();
            
            //
            
            if (minWidth >= 0) {
                if (stage.getWidth() < minWidth)
                    stage.setWidth(minWidth);
                stage.setMinWidth(minWidth);
            }
            
            if (minHeight >= 0) {
                if (stage.getHeight() < minHeight)
                    stage.setHeight(minHeight);
                stage.setMinHeight(minHeight);
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * Locks the size of the specified {@link Region} based on the specified {@link DialogsFX.SizeType}.
     * <p>
     * Note that {@link Region regions} do not have a {@code setResizable()} method. This methods:
     * <ul>
     * <li>{@link DialogsFX.SizeType#NON_RESIZEABLE} sets the {@link Region#setMinSize(double, double) minimum size} and {@link Region#setMaxSize(double, double) maximum size} to the current size of the {@link
     * Region}.</li>
     * <li>{@link DialogsFX.SizeType#RESIZEABLE} sets the {@link Region#setMaxSize(double, double) maximum size} to {@link Integer#MAX_VALUE}.</li>
     * </ul>
     *
     * @param region   The {@link Region} being locked.
     * @param sizeType The {@link DialogsFX.SizeType} that controls how the {@link Region} can be resized.
     *
     * @return True if the {@link Region Region's} size was locked successfully, false otherwise.
     */
    public static boolean lockSize(Region region, DialogsFX.SizeType sizeType) {
        if (region != null && sizeType != null) {
            double width = region.getWidth();
            double height = region.getHeight();
            if (sizeType == DialogsFX.SizeType.MINIMUM_SIZE)
                region.setMinSize(width, height);
            else if (sizeType == DialogsFX.SizeType.NON_RESIZEABLE) {
                region.setMaxSize(width, height);
                region.setMinSize(width, height);
            } else if (sizeType == DialogsFX.SizeType.RESIZEABLE)
                region.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
            return true;
        }
        return false;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="FXML">
    
    /**
     * Loads and then returns a new {@link FXMLLoader} for the specified resource path.
     *
     * @param resource   The resource path.
     * @param loader     The {@link FXMLLoader} being loaded.
     *                   Null to create a new {@link FXMLLoader}.
     * @param controller The controller to be assigned to the returned {@link FXMLLoader}.
     *                   Specify null if the controller is to be set later, or is set via FXML.
     *
     * @return The recently loaded {@link FXMLLoader} for the specified resource path.
     */
    public static <T> @NotNull FXMLLoader loadFXML(String resource, FXMLLoader loader, T controller) {
        loader = loader == null ? new FXMLLoader() : loader;
        try {
            //				Printing.dev("Initializing FXML Loader for resource: " + resource);
            //				System.err.println("Initializing FXML Loader for resource: " + resource);
            if (controller != null)
                loader.setController(controller);
            loader.load(Stuff.getResourceStream(resource));
            return loader;
        } catch (Exception e) {
            throw new UndefinedRuntimeException("Error loading resource: " + resource);
        }
    }
    
    //</editor-fold>
    
    
    //<editor-fold desc="Helper Methods">
    
    private static boolean isValidSuffix(String str, String @NotNull ... suffixes) {
        for (final String suffix: suffixes)
            if (str.contains(suffix) && (!str.endsWith(suffix) || Str.getCount(str, suffix) != 1 || str.length() == suffix.length()))
                return false;
        return true;
    }
    
    private static boolean isValidPrefix(String str, String @NotNull ... prefixes) {
        for (String prefix: prefixes)
            if (str.contains(prefix) && (!str.startsWith(prefix) || Str.getCount(str, prefix) != 1))
                return false;
        return true;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Pseudo Classes">
    
    public static final PseudoClass INVALID = PseudoClass.getPseudoClass("invalid");
    public static final PseudoClass DISABLED = PseudoClass.getPseudoClass("custom_disabled");
    
    //</editor-fold>
    
    //<editor-fold desc="Classes">
    
    //<editor-fold desc="Overlay Enums">
    
    public enum OverlayBackgroundOpacityType {
        TRANSPARENT,
        OPAQUE
    }
    
    public enum OverlayResizeType {
        FILL,
        INHERIT
    }
    
    //</editor-fold>
    
    /**
     * Defines the auto-complete mode for an {@link #autoCompleteComboBox(ComboBox, AutoCompleteMode, String) ComboBox}.
     */
    public enum AutoCompleteMode {
        STARTS_WITH, CONTAINING
    }
    
    public interface ChildEditable {
        ChildEditable DISABLE = (final Node node) -> node.setDisable(true);
        ChildEditable ENABLE = (final Node node) -> node.setDisable(false);
        ChildEditable HIDE = (final Node node) -> node.setVisible(false);
        ChildEditable SHOW = (final Node node) -> node.setVisible(true);
        
        void edit(Node component);
    }
    
    public static class NodeMover {
        
        private static final int RESIZE_AREA = 7;
        private static final int RESIZE_AREA_DIAGONAL = 10;
        
        //
        
        private final Stage stage;
        
        private Region corner;
        private Region top;
        private Region bottom;
        private Region left;
        private Region right;
        
        private final ReadOnlyBooleanWrapper resizingCorner;
        private final ReadOnlyBooleanWrapper resizingTop;
        private final ReadOnlyBooleanWrapper resizingBottom;
        private final ReadOnlyBooleanWrapper resizingLeft;
        private final ReadOnlyBooleanWrapper resizingRight;
        
        private final Node[] excludeNodes;
        
        private final ObjectProperty<Point2D> anchorProperty;
        private final DoubleProperty stageWidthProperty;
        private final DoubleProperty stageHeightProperty;
        
        public NodeMover(Stage stage, Region corner, Region top, Region bottom, Region left, Region right, Node... excludeNodes) {
            Objects.requireNonNull(this.stage = stage, "Stage cannot be null");
            
            this.corner = corner;
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
            
            this.resizingCorner = new ReadOnlyBooleanWrapper();
            this.resizingTop = new ReadOnlyBooleanWrapper();
            this.resizingBottom = new ReadOnlyBooleanWrapper();
            this.resizingLeft = new ReadOnlyBooleanWrapper();
            this.resizingRight = new ReadOnlyBooleanWrapper();
            
            this.excludeNodes = excludeNodes;
            
            this.anchorProperty = new SimpleObjectProperty<>();
            this.stageWidthProperty = new SimpleDoubleProperty();
            this.stageHeightProperty = new SimpleDoubleProperty();
        }
        
        //
        
        public Scene scene() {
            return stage.getScene();
        }
        
        //
        
        private void begin() {
            addEventFilters(corner, resizingCorner, Cursor.SE_RESIZE, true);
            addEventFilters(top, resizingTop, Cursor.V_RESIZE, false);
            addEventFilters(bottom, resizingBottom, Cursor.V_RESIZE, true);
            addEventFilters(left, resizingLeft, Cursor.H_RESIZE, false);
            addEventFilters(right, resizingRight, Cursor.H_RESIZE, true);
        }
        
        private void addEventFilters(@NotNull Region region, ReadOnlyBooleanWrapper wrapper, Cursor cursor, boolean active) {
            region.setMouseTransparent(!active);
            if (active) {
                region.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
                    if (!stage.isMaximized()) {
                        scene().setCursor(cursor);
                        event.consume();
                    }
                });
                region.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
                    if (anchorProperty.get() == null) {
                        scene().setCursor(Cursor.DEFAULT);
                        event.consume();
                    }
                });
                region.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (!stage.isMaximized()) {
                        wrapper.set(true);
                        anchorProperty.set(new Point2D(event.getScreenX() - stage.getX(), event.getScreenY() - stage.getY()));
                        stageWidthProperty.set(stage.getWidth());
                        stageHeightProperty.set(stage.getHeight());
                        event.consume();
                    }
                });
                region.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                    if (wrapper.get()) {
                        scene().setCursor(Cursor.DEFAULT);
                        anchorProperty.set(null);
                        wrapper.set(false);
                    }
                });
                region.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
                    if (wrapper.get() && !stage.isMaximized()) {
                        drag(event);
                        event.consume();
                    }
                });
            }
        }
        
        private void drag(MouseEvent event) {
            Cursor cursor = scene().getCursor();
            if (cursor != null) {
                if (cursor == Cursor.H_RESIZE || cursor == Cursor.SE_RESIZE)
                    setWidth(event);
                if (cursor == Cursor.V_RESIZE || cursor == Cursor.SE_RESIZE)
                    setHeight(event);
            }
        }
        
        private void setWidth(@NotNull MouseEvent event) {
            stage.setWidth(Math.max(stage.getMinWidth(), stageWidthProperty.get() + (event.getScreenX() - (anchorProperty.get().getX() + stage.getX()))));
            event.consume();
        }
        
        private void setHeight(@NotNull MouseEvent event) {
            stage.setHeight(Math.max(stage.getMinHeight(), stageHeightProperty.get() + (event.getScreenY() - (anchorProperty.get().getY() + stage.getY()))));
            event.consume();
        }
    }
    
    public static class Colors {
        private Colors() { } // No Instance
        
        @Contract(value = "_, _, _ -> new", pure = true)
        public static @NotNull Color from255(int r, int g, int b) {
            return from255(r, g, b, 255);
        }
        
        @Contract(value = "_, _, _, _ -> new", pure = true)
        public static @NotNull Color from255(int r, int g, int b, int a) {
            return Color.color(r / 255.0, g / 255.0, b / 255.0, a / 255.0);
        }
    }
    
    //</editor-fold>
}