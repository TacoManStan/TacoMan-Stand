package com.taco.suit_lady.view.ui.jfx.fxtools;

import com.sun.javafx.application.PlatformImpl;
import com.taco.suit_lady.util.UndefinedRuntimeException;
import com.taco.suit_lady.util.*;
import com.taco.suit_lady.view.ui.jfx.Colorable;
import com.taco.suit_lady.view.ui.jfx.hyperlink.HyperlinkNodeFX;
import com.taco.suit_lady.view.ui.jfx.lists.Listable;
import com.taco.util.quick.ConsoleBB;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Contains a variety of classes that provide JavaFX utility features.
 */
public class FXTools
{
    public static FXTools get()
    {
        return TB.fx();
    }
    
    public FXTools() { }
    
    //<editor-fold desc="EDT/FX Thread">
    
    /**
     * Checks if the current {@link Thread} is the the JavaFX Application thread.
     *
     * @return True if the current {@link Thread} is the the JavaFX Application thread.
     * @see Platform#isFxApplicationThread()
     */
    public boolean isFXThread()
    {
        return Platform.isFxApplicationThread();
    }
    
    /**
     * Checks if the current {@link Thread} is the AWT event dispatch thread (EDT)
     *
     * @return True if the current {@link Thread} is the EDT, false otherwise.
     * @see EventQueue#isDispatchThread()
     */
    public boolean isEDT()
    {
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
    public void runFX(Runnable runnable, boolean wait)
    {
        ExceptionTools.nullCheck(runnable, "Runnable cannot be null");
        try
        {
            if (isFXThread())
                runnable.run();
            else if (wait)
                PlatformImpl.runAndWait(runnable);
            else
                Platform.runLater(runnable);
        }
        catch (Exception e)
        {
            if (!e.getMessage().equalsIgnoreCase("toolkit has exited"))
                throw ExceptionTools.ex(e);
        }
    }
    
    /**
     * Runs the specified Callable on the JavaFX Thread, and then returns its result when it is done.
     *
     * @param callable The Callable to be executed.
     */
    public <V> V runFX(Callable<V> callable)
    {
        ExceptionTools.nullCheck(callable, "Callable cannot be null");
        try
        {
            if (isFXThread())
                return callable.call();
            else
            {
                ObjectProperty<V> _objProperty = new SimpleObjectProperty<>(null);
                PlatformImpl.runAndWait(() -> {
                    try
                    {
                        _objProperty.set(callable.call());
                    }
                    catch (Exception e)
                    {
                        if (!e.getMessage().equalsIgnoreCase("toolkit has exited"))
                            throw ExceptionTools.ex(e);
                    }
                });
                return _objProperty.get();
            }
        }
        catch (Exception e)
        {
            throw ExceptionTools.ex(e);
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
    public void runEDT(Runnable runnable, boolean wait)
    {
        ExceptionTools.nullCheck(runnable, "Runnable cannot be null");
        try
        {
            if (EventQueue.isDispatchThread())
                runnable.run();
            else if (wait)
                EventQueue.invokeAndWait(runnable);
            else
                EventQueue.invokeLater(runnable);
        }
        catch (Exception e)
        {
            throw ExceptionTools.ex(e);
        }
    }
    
    /**
     * Runs the specified Callable on the JavaFX Thread, and then returns its result when it is done.
     *
     * @param callable The Callable to be executed.
     */
    public <V> V runEDT(Callable<V> callable)
    {
        ExceptionTools.nullCheck(callable, "Callable cannot be null");
        try
        {
            if (isFXThread())
                return callable.call();
            else
            {
                ObjectProperty<V> _objProperty = new SimpleObjectProperty<>(null);
                EventQueue.invokeAndWait(() -> {
                    try
                    {
                        _objProperty.set(callable.call());
                    }
                    catch (Exception e)
                    {
                        throw ExceptionTools.ex(e);
                    }
                });
                return _objProperty.get();
            }
        }
        catch (Exception e)
        {
            throw ExceptionTools.ex(e);
        }
    }
    
    //
    
    /**
     * Throws a {@link RuntimeException} if the current {@link Thread} is not the FX Thread.
     */
    public void requireFX()
    {
        if (!isFXThread())
            throw ExceptionTools.ex(new IllegalStateException("Operation must be executed on the FX Thread."));
    }
    
    /**
     * Throws a {@link RuntimeException} if the current {@link Thread} is not the EDT.
     */
    public void requireEDT()
    {
        if (!isEDT())
            throw ExceptionTools.ex(new IllegalStateException("Operation must be executed on the EDT."));
    }
    
    //</editor-fold>
    
    /**
     * Returns the {@code key code} for the specified {@link KeyCode}.
     *
     * @param keyCode The {@link KeyCode}.
     * @return The {@code key code} for the specified {@link KeyCode}.
     */
    public int getKeyCode(KeyCode keyCode)
    {
        return ExceptionTools.nullCheck(keyCode, "JFX KeyCode").getCode();
    }
    
    /**
     * Returns the {@code key char} for the specified {@link KeyCode}.
     *
     * @param keyCode The {@link KeyCode}.
     * @return The {@code key char} for the specified {@link KeyCode}.
     */
    public String getKeyChar(KeyCode keyCode)
    {
        return ExceptionTools.nullCheck(keyCode, "JFX KeyCode").getChar();
    }
    
    //
    
    /**
     * Returns a custom {@link DataFormat} with the specified id name.
     * <p>
     * If a custom {@link DataFormat} with the specified id name already exists, the existing {@link DataFormat} is returned.
     * <p>
     * Returns null if the specified id name is null or empty.
     *
     * @param id The id name of the custom {@link DataFormat}.
     * @return A custom {@link DataFormat} with the specified id name.
     */
    public DataFormat getDataFormat(String id)
    {
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
     * @return True if the removal was successful, false otherwise.
     */
    public boolean removeFromParent(Node node)
    {
        if (node != null)
        {
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
    public void setVisible(Node node, boolean visible)
    {
        if (node != null)
        {
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
     * @return The children {@link Node Nodes} of the specified {@link TextFlow TextFlows} combined into a single {@link TextFlow}.
     */
    public TextFlow combineToFlow(TextFlow textFlow, Object... objs)
    {
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
     * @return The children {@link Node Nodes} of the specified {@link TextFlow TextFlows} combined into a single {@link TextFlow}.
     */
    public TextFlow combineToFlow(Object... objs)
    {
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
     * @see #combineToFlow(Object...)
     */
    private void addFlowObj(Object obj, ArrayList<Node> children)
    {
        if (obj != null)
            if (obj instanceof String)
                addFlowObj(new Text((String) obj), children);
            else if (obj instanceof Text)
            {
                Text text = (Text) obj;
                text.getStyleClass().add("text");
                children.add(text);
            }
            else if (obj instanceof TextFlow)
            {
                TextFlow flow = (TextFlow) obj;
                for (Node child: flow.getChildren())
                    addFlowObj(child, children);
            }
            else if (obj instanceof Node)
            {
                Node node = (Node) obj;
                children.add(node);
            }
            else if (obj instanceof HyperlinkNodeFX)
            {
                ArrayList<Node> nodes = ((HyperlinkNodeFX) obj).getNodes();
                for (Object obj2: nodes)
                    addFlowObj(obj2, children);
            }
            else if (obj instanceof ArrayList)
            {
                ArrayList parents = (ArrayList) obj;
                for (Object obj2: parents)
                    addFlowObj(obj2, children);
            }
            else if (obj instanceof Object[])
            {
                Object[] parents = (Object[]) obj;
                for (Object obj2: parents)
                    addFlowObj(obj2, children);
            }
            else
                ConsoleBB.CONSOLE.print("ERROR: " + TB.general().getSimpleName(obj.getClass()) + " is an invalid type.");
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
     * @return The converted {@code TextField} (the same {@code TextField} that was passed as a parameter).
     */
    public TextField numberTextField(TextField textField, boolean allowDecimals)
    {
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
     * @return The converted {@code TextField} (the same {@code TextField} that was passed as a parameter).
     */
    public TextField numberTextField(TextField textField, boolean allowDecimals, double initialValue)
    {
        ExceptionTools.nullCheck(textField, "TextField cannot be null.");
        
        textField.addEventFilter(KeyEvent.KEY_TYPED, _keyEvent -> {
            String _str = _keyEvent.getCharacter().toLowerCase();
            String _nStr = textField.getText() + _str;
            if (!isValidSuffix(_nStr, "k", "m", "b") ||
                !isValidPrefix(_nStr, "-") ||
                ((_str.endsWith("k") || _str.endsWith("m") || _str.endsWith("b")) && textField.getText().endsWith(".")))
            {
                _keyEvent.consume();
                return;
            }
            
            Character[] _numbers = TB.arrays().concat(
                    new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', 'k', 'm', 'b'},
                    (allowDecimals ? new Character[]{'.'} : new Character[]{})
            );
            int _periodCount = 0;
            
            for (char _c: _nStr.toCharArray())
                if (_c == '.')
                    _periodCount++;
            if (_periodCount > 1)
            {
                _keyEvent.consume();
                return;
            }
            
            char[] _strChars = _str.toCharArray();
            STR_LOOP:
            for (char _strChar: _strChars)
            {
                for (char _strChar2: _numbers)
                    if (_strChar == _strChar2)
                        continue STR_LOOP;
                _keyEvent.consume();
                return;
            }
        });
        
        textField.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER && event.getEventType().equals(KeyEvent.KEY_RELEASED))
            {
                textField.setText("" + getIntValue(textField));
                textField.positionCaret(textField.getText().length());
            }
        });
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean focused) -> {
            if (!focused)
            {
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
    public void constructButtonMenu(Menu menu, Button button, EventHandler<ActionEvent> handler)
    {
        if (menu != null && button != null)
        {
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
    public void constructUpdatableWebView(WebView webView, ObservableStringValue observable, ObservableValue... updaters)
    {
        constructUpdatableWebView(webView, new SimpleObjectProperty<>(observable), updaters);
    }
    
    /**
     * Sets the specified {@link WebView} to be updatable.
     *
     * @param webView    The {@link WebView}.
     * @param observable The {@link ObservableValue} that will retrieve the information for the {@link WebView}.
     * @param updaters   Any {@link ObservableValue observable values} that will trigger an update when changed. Leave empty to only observe the specified {@link ObservableValue}.
     */
    public void constructUpdatableWebView(WebView webView, ObservableValue<? extends ObservableStringValue> observable, ObservableValue... updaters)
    {
        webView.setFocusTraversable(false);
        webView.setOnMouseClicked(Event::consume);
        webView.setOnMousePressed(Event::consume);
        webView.setOnMouseReleased(Event::consume);
        webView.getEngine().loadContent(observable.getValue().get());
        
        ChangeListener changeListener = (observable1, oldValue, newValue) -> {
            ObservableStringValue observableString = observable.getValue();
            if (observableString != null)
            {
                String string = observableString.get();
                if (string != null)
                {
                    webView.getEngine().loadContent(StringTools.get().html(string, true));
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
     * @return The integer value from the specified {@link TextField}.
     */
    public int getIntValue(TextField textField)
    {
        return (int) CalculationTools.getLongkmb(textField.getText(), false);
    }
    
    /**
     * Returns the integer value from the specified {@link TextField}.
     * <p>
     * If the specified {@link TextField} does not contain a valid number (included numbers defined by "k" and "m"), this method throws an exception.
     *
     * @param textField The {@link TextField}.
     * @return The integer value from the specified {@link TextField}.
     */
    public long getLongValue(TextField textField)
    {
        return CalculationTools.getLongkmb(textField.getText(), false);
    }
    
    /**
     * Returns the double value from the specified {@link TextField}.
     * <p>
     * If the specified {@link TextField} does not contain a valid number (included numbers defined by "k" and "m"), this method throws an exception.
     *
     * @param textField The {@link TextField}.
     * @return The double value from the specified {@link TextField}.
     */
    public double getValue(TextField textField)
    {
        return CalculationTools.getkmb(textField.getText(), false);
    }
    
    /**
     * Resets the specified {@link ComboBox} to either a blank value, or its default (first) value.
     *
     * @param comboBox The {@link ComboBox} being reset.
     * @param clear    True if the specified {@link ComboBox} should be cleared (set to a blank val     false if the specified {@link ComboBox} should be set to its default (first)
     *                 value.
     */
    public <T> void reset(ComboBox<T> comboBox, boolean clear)
    {
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
    public void editChildren(Parent parent, ChildEditable editable, boolean includeParents)
    {
        getChildren(parent, includeParents).forEach(editable::edit);
    }
    
    /**
     * Returns all of the children {@link Node Nodes} of the specified {@link Parent}.
     * <p>
     * This method uses a deep search, so all children, grandchildren, etc will be included in the search.
     *
     * @param parent         The {@link Parent}.
     * @param includeParents True if the parents should also be included, false otherwise.
     * @return All of the children {@link Node Nodes} of the specified {@link Parent}.
     */
    public ArrayList<Node> getChildren(Parent parent, boolean includeParents)
    {
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
    public void setAllEnabled(Parent parent, boolean enabled, Node... excludes)
    {
        ArrayList<Node> excludes_list = new ArrayList<>(excludes.length);
        excludes_list.addAll(java.util.Arrays.asList(excludes));
        for (Node c: excludes)
            if (c instanceof Parent)
                excludes_list.addAll(getChildren((Parent) c, true));
        getChildren(parent, true).stream().filter(c -> !ArrayTools.contains(c, excludes_list.toArray())).forEach(c -> c.setDisable(!enabled));
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
    public void showTooltip(Stage owner, Control control, String tooltipText, boolean consumeAutoHidingEvents)
    {
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
     * @return A new {@link Tooltip} with the specified text.
     */
    public Tooltip createTooltip(String text)
    {
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
     * @return The fully implemented {@link ContextMenu popup menu} created for and attached to the specified {@link Node}.
     */
    public ContextMenu onClick(
            Node node, boolean handlePrevious,
            EventHandler<MouseEvent> onClick,
            EventHandler<MouseEvent> onDoubleClick,
            EventHandler<MouseEvent> onMiddleClick,
            SimplePredicate popupCondition, MenuItem... items)
    {
        ContextMenu menu = new ContextMenu(items);
        EventHandler<? super MouseEvent> oldOnClick = node.getOnMouseClicked();
        node.setOnMouseClicked(event -> {
            if (oldOnClick != null && handlePrevious)
                oldOnClick.handle(event); //Handle the old onMouseClick handler
            if (event.isPopupTrigger() && (popupCondition == null || popupCondition.test()))
            {
                if (menu.isShowing())
                    menu.hide();
                menu.show(node, event.getScreenX(), event.getScreenY());
            }
            else if (menu.isShowing())
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
     * @return A new {@link CustomMenuItem} with the specified text, {@link Tooltip}, and {@link EventHandler}.
     */
    public CustomMenuItem createMenuItem(String text, Tooltip tooltip, EventHandler<ActionEvent> eventHandler)
    {
        return createMenuItem(new Label(text), tooltip, eventHandler);
    }
    
    /**
     * Creates a new {@link CustomMenuItem} with the specified {@link Node}, {@link Tooltip}, and {@link EventHandler}.
     *
     * @param node         The {@link Node} that is to be displayed as the content of the {@link CustomMenuItem}.
     * @param tooltip      The {@link Tooltip} of the {@link CustomMenuItem}.
     * @param eventHandler The {@link EventHandler} that is executed when the {@link CustomMenuItem} is clicked.
     * @return A new {@link CustomMenuItem} with the specified {@link Node}, {@link Tooltip}, and {@link EventHandler}.
     */
    public CustomMenuItem createMenuItem(Node node, Tooltip tooltip, EventHandler<ActionEvent> eventHandler)
    {
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
     * @return True if the style class addition/removal was successful, false otherwise.
     */
    public boolean applyCSS(Node node, boolean active, String... styleClasses)
    {
        if (node != null && styleClasses != null)
        {
            for (String styleClass: styleClasses)
                if (styleClass != null)
                    if (active)
                    {
                        if (!node.getStyleClass().contains(styleClass))
                            node.getStyleClass().add(styleClass);
                    }
                    else
                        node.getStyleClass().remove(styleClass);
            if (active)
                ArrayTools.containsAll(node.getStyleClass(), styleClasses);
            return !ArrayTools.containsAny(node.getStyleClass(), styleClasses);
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
     * @throws NullPointerException If the specified {@link Node} or styles array is null.
     */
    public void applyCSSInLine(Node node, boolean overwrite, String... styles)
    {
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
     * @return True if the mouse is on the specified {@link Node} based on the specified {@link MouseEvent}, false otherwise.
     */
    public boolean isMouseOnNode(Node node)
    {
        Objects.requireNonNull(node, "Node cannot be null");
        return node.isHover();
    }
    
    public boolean isMouseOnEventSource(MouseEvent event)
    {
        Objects.requireNonNull(event, "Event cannot be null");
        if (event.getSource() instanceof Node)
            return isMouseOnNode((Node) event.getSource());
        return false;
    }
    
    public BorderPane progressOverlay(Node parent)
    {
        return progressOverlay(parent, null, -1, null);
    }
    
    public BorderPane progressOverlay(Node parent, ObservableValue<? extends Number> progressProperty)
    {
        return progressOverlay(parent, null, -1, progressProperty);
    }
    
    public BorderPane progressOverlay(Node parent, Node backgroundContent, int maxSize, ObservableValue<? extends Number> progressProperty)
    {
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
        {
            stackPane.getChildren().add(backgroundContent);
        }
        stackPane.getChildren().add(indicator);
        return constructOverlay(parent, stackPane, -1.0,
                                OverlayBackgroundOpacityType.OPAQUE,
                                OverlayResizeType.FILL
        );
    }
    
    public ProgressIndicator getOverlayIndicator(BorderPane overlayPane)
    {
        Objects.requireNonNull(overlayPane, "Overlay Pane cannot be null");
        Node center = overlayPane.getCenter();
        if (center != null && center instanceof StackPane)
        {
            java.util.List<Node> stackOverlayChildren = overlayPane.getChildren();
            for (Node child: stackOverlayChildren)
                if (child != null && child instanceof ProgressIndicator)
                    return (ProgressIndicator) child;
        }
        return null;
    }
    
    /**
     * Creates and then returns an constructOverlay {@link Pane} with the specified parent {@link Node} and {@link Node content}.
     *
     * @param parent  The parent {@link Node}.
     * @param content The {@link Node content}.
     * @return an constructOverlay {@link Pane} with the specified parent {@link Node} and {@link Node content}.
     */
    public BorderPane constructOverlay(Node parent, Node content)
    {
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
     * @return an constructOverlay {@link Pane} with the specified parent {@link Node} and {@link Node content}.
     */
    public BorderPane constructOverlay(Node parent, Node content, double opacity, OverlayBackgroundOpacityType backgroundOpacityType, OverlayResizeType resizeType)
    {
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
        else if (parent instanceof BorderPane)
        {
            Node center = ((BorderPane) parent).getCenter();
            if (center != null && center instanceof StackPane)
                rootStackPane = (StackPane) center;
            else
                throw new RuntimeException("If parent is a BorderPane, then parent center must be a Stack Pane (center=" + center + ")");
        }
        else
        {
            Scene scene = parent.getScene();
            if (scene != null)
            {
                Parent root = scene.getRoot();
                if (root != null)
                {
                    if (root instanceof StackPane)
                    {
                        rootStackPane = (StackPane) root;
                    }
                    else if (root instanceof BorderPane)
                    {
                        Node center = ((BorderPane) root).getCenter();
                        if (center != null && center instanceof StackPane)
                            rootStackPane = (StackPane) center;
                        else
                            throw new RuntimeException("If root is a BorderPane, then root center must be a Stack Pane (center=" + center + ")");
                    }
                    else
                        throw new ClassCastException("Root must be instance of Stack Pane or BorderPane (root=" + GeneralTools.get().getSimpleName(root) + ")");
                }
                else
                    throw new NullPointerException("Root cannot be null");
            }
            else
                throw new NullPointerException("Scene cannot be null");
        }
        
        //
        
        BorderPane overlay = new BorderPane();
        
        overlay.prefWidthProperty().bind(rootStackPane.widthProperty());
        overlay.prefHeightProperty().bind(rootStackPane.heightProperty());
        
        if (content != null)
        {
            overlay.setCenter(content);
            
            //Content properties
            if (opacity != -1.0)
                applyCSSInLine(content, false, "-fx-opacity: " + opacity);
            if (resizeType == OverlayResizeType.FILL)
            {
                if (content instanceof Region)
                {
                    Region contentRegion = (Region) content;
                    contentRegion.prefWidthProperty().bind(rootStackPane.widthProperty());
                    contentRegion.prefHeightProperty().bind(rootStackPane.heightProperty());
                }
                else if (content instanceof ImageView)
                {
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
    
    public void constructDraggableNode(Node... nodes)
    {
        Objects.requireNonNull(nodes, "Nodes cannot be null");
        ObjectProperty<Stage> stageProperty = new SimpleObjectProperty<>();
        
        for (Node node: nodes)
        {
            Objects.requireNonNull(node, "Node cannot be null");
            if (stageProperty.get() == null)
                stageProperty.set(getSceneStage(node.getScene()));
            Objects.requireNonNull(stageProperty.get());
            
            Point p = new Point();
            node.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY)
                {
                    Scene scene = stageProperty.get().getScene();
                    double px = (event.getSceneX() / scene.getWidth()) * 100.0;
                    double py = (event.getSceneY() / scene.getHeight()) * 100.0;
                    p.setLocation(px, py);
                }
            });
            node.setOnMouseDragged(event -> {
                if (event.getButton() == MouseButton.PRIMARY)
                {
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
    
    public NodeMover constructResizableNode(Stage stage, Region corner, Region top, Region bottom, Region left, Region right, Node... excludeNodes)
    {
        NodeMover mover = new NodeMover(stage, corner, top, bottom, left, right, excludeNodes);
        mover.begin();
        return mover;
    }
    
    public boolean isResizing(InputEvent event)
    {
        Scene scene = getEventScene(event);
        if (scene != null)
        {
            Cursor cursor = scene.getCursor();
            if (cursor != null)
                return cursor == Cursor.H_RESIZE || cursor == Cursor.V_RESIZE || cursor == Cursor.SE_RESIZE;
        }
        return false;
    }
    
    public Scene getEventScene(InputEvent event)
    {
        Objects.requireNonNull(event, "Event cannot be null");
        Object source = event.getSource();
        if (source != null)
        {
            if (source instanceof Scene)
                return (Scene) source;
            else if (!(source instanceof Node))
                throw new ClassCastException("Event source must be a Node");
            return ((Node) source).getScene();
        }
        return null;
    }
    
    public Stage getSceneStage(Scene scene)
    {
        Objects.requireNonNull(scene, "Scene cannot be null");
        Window window = scene.getWindow();
        if (window != null)
        {
            if (!(window instanceof Stage))
                throw new ClassCastException("Scene window must be a Stage");
            return (Stage) window;
        }
        return null;
    }
    
    //
    
    public void passdownOrder(Node root, Consumer<Node> action)
    {
        ExceptionTools.nullCheck(root, "Root node cannot be null");
        ExceptionTools.nullCheck(action, "Task cannot be null");
        if (root instanceof Parent)
            ((Parent) root).getChildrenUnmodifiable().forEach(child -> passdownOrder(child, action));
    }
    
    //
    
    //<editor-fold desc="Node Sizing">
    
    public void bindToParent(Region region, Region parent, BindOrientation bindOrientation, BindType bindType)
    {
        bindToParent(region, parent, true, false, bindOrientation, bindType);
    }
    
    public void bindToParent(Region region, Region parent, ObservableDoubleValue observableOffset, BindOrientation bindOrientation, BindType bindType)
    {
        bindToParent(region, parent, true, false, observableOffset, bindOrientation, bindType);
    }
    
    public void bindToParent(Region region, Region parent, boolean includePadding, boolean includeInsets, BindOrientation bindOrientation, BindType bindType)
    {
        bindToParent(region, parent, includePadding, includeInsets, null, bindOrientation, bindType);
    }
    
    public void bindToParent(
            Region region,
            Region parent,
            boolean includePadding,
            boolean includeInsets,
            ObservableDoubleValue observableOffset,
            BindOrientation bindOrientation,
            BindType bindType)
    {
        ExceptionTools.nullCheck(region, "Region");
        ExceptionTools.nullCheck(parent, "Parent Region");
        ExceptionTools.nullCheck(bindOrientation, "Bind Orientation");
        ExceptionTools.nullCheck(bindType, "Bind Type");
        
        ObservableDoubleValue _observableOffset = observableOffset == null ? new SimpleDoubleProperty(0.0) : observableOffset;
        
        DoubleProperty _widthProperty;
        DoubleProperty _heightProperty;
        if (bindType == BindType.PREF || bindType == BindType.BOTH)
        {
            _widthProperty = region.prefWidthProperty();
            _heightProperty = region.prefHeightProperty();
        }
        else if (bindType == BindType.MAX)
        {
            _widthProperty = region.maxWidthProperty();
            _heightProperty = region.maxHeightProperty();
        }
        else
            throw ExceptionTools.unsupported("Unknown BindType: " + bindType);
        
        if (bindOrientation == BindOrientation.WIDTH || bindOrientation == BindOrientation.BOTH)
        {
            _widthProperty.bind(Bindings.createDoubleBinding(() -> {
                return getNodeSize(parent, includePadding, includeInsets, _observableOffset.get(), BindOrientation.WIDTH);
            }, parent.widthProperty(), parent.heightProperty(), parent.paddingProperty(), parent.insetsProperty(), _observableOffset));
            if (bindType == BindType.BOTH)
                region.maxWidthProperty().bind(region.prefWidthProperty());
        }
        
        if (bindOrientation == BindOrientation.HEIGHT || bindOrientation == BindOrientation.BOTH)
        {
            _heightProperty.bind(Bindings.createDoubleBinding(() -> {
                return getNodeSize(parent, includePadding, includeInsets, _observableOffset.get(), BindOrientation.HEIGHT);
            }, parent.widthProperty(), parent.heightProperty(), parent.paddingProperty(), parent.insetsProperty(), _observableOffset));
            if (bindType == BindType.BOTH)
                region.maxHeightProperty().bind(region.prefHeightProperty());
        }
    }
    
    public double getNodeSize(Region region, boolean includePadding, boolean includeInsets, BindOrientation bindOrientation)
    {
        return getNodeSize(region, includePadding, includeInsets, 0.0, bindOrientation);
    }
    
    public double getNodeSize(Region region, boolean includePadding, boolean includeInsets, double offset, BindOrientation bindOrientation)
    {
        ExceptionTools.nullCheck(region, "Region");
        ExceptionTools.nullCheck(bindOrientation, "BindOrientation");
        
        Insets _insets = region.getInsets();
        Insets _padding = region.getPadding();
        if (bindOrientation == BindOrientation.WIDTH)
            return region.getWidth() + offset - (includePadding ? _padding.getLeft() + _padding.getRight() : 0) + (includeInsets ? _insets.getLeft() + _insets.getRight() : 0);
        else if (bindOrientation == BindOrientation.HEIGHT)
            return region.getHeight() + offset - (includePadding ? _padding.getTop() + _padding.getBottom() : 0) + (includeInsets ? _insets.getTop() + _insets.getBottom() : 0);
        else
            throw ExceptionTools.unsupported("BindOrientation \"" + bindOrientation + "\" is not supported.");
    }
    
    public enum BindOrientation
    {
        WIDTH,
        HEIGHT,
        BOTH
    }
    
    public enum BindType
    {
        PREF,
        MAX,
        BOTH
    }
    
    //</editor-fold>
    
    //
    
    //<editor-fold desc="Combo Box/List">
    
    /**
     * Turns the specified {@link ComboBox} into an auto-complete box.
     *
     * @param <T>        The type of elements in the specified {@link ComboBox}.
     * @param comboBox   The {@link ComboBox}.
     * @param mode       The {@link AutoCompleteMode} for the auto-complete box.
     * @param promptText The text that is displayed when no items are selected.
     * @see #applyCellFactory(ComboBox)
     * @see AutoCompleteMode
     */
    public <T> void autoCompleteComboBox(ComboBox<T> comboBox, AutoCompleteMode mode, String promptText)
    {
        applyCellFactory(comboBox);
        if (mode != null)
        {
            ObservableList<T> data = comboBox.getItems();
            comboBox.setEditable(true);
            if (promptText != null)
                comboBox.setPromptText(promptText);
            comboBox.getEditor().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (comboBox.getSelectionModel().getSelectedIndex() < 0)
                    comboBox.getEditor().setText(null);
            });
            comboBox.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> comboBox.hide());
            comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                
                private boolean moveCaretToPos = false;
                private int caretPos;
                
                @Override public void handle(KeyEvent event)
                {
                    if (event.getCode() == KeyCode.UP)
                    {
                        caretPos = -1;
                        moveCaret(comboBox.getEditor().getText().length());
                        return;
                    }
                    else if (event.getCode() == KeyCode.DOWN)
                    {
                        if (!comboBox.isShowing())
                            comboBox.show();
                        caretPos = -1;
                        moveCaret(comboBox.getEditor().getText().length());
                        return;
                    }
                    else if (event.getCode() == KeyCode.BACK_SPACE)
                    {
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                    }
                    else if (event.getCode() == KeyCode.DELETE)
                    {
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
                            if (aData instanceof Listable)
                            {
                                String[] searchKeys = ((Listable) aData).getSearchKeys();
                                if (searchKeys != null)
                                    for (String searchKey: searchKeys)
                                        if (isSearchKeyValid(searchKey, mode))
                                        {
                                            list.add(aData);
                                            continue DATA;
                                        }
                            }
                            else if (isSearchKeyValid(aData.toString(), mode))
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
                
                private boolean isSearchKeyValid(String searchKey, AutoCompleteMode mode)
                {
                    if (searchKey != null)
                        if (mode == AutoCompleteMode.STARTS_WITH && searchKey.toLowerCase().startsWith(comboBox.getEditor().getText().toLowerCase()))
                            return true;
                        else if (mode == AutoCompleteMode.CONTAINING && searchKey.toLowerCase().contains(comboBox.getEditor().getText().toLowerCase()))
                            return true;
                    return false;
                }
                
                private void moveCaret(int textLength)
                {
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
     * @see #getShortText(Object)
     * @see #autoCompleteComboBox(ComboBox, AutoCompleteMode, String)
     */
    public <T> void applyCellFactory(ComboBox<T> comboBox)
    {
        comboBox.setConverter(new StringConverter<T>()
        {
            
            private final Map<String, T> map = new HashMap<>();
            
            @Override public String toString(T obj)
            {
                String str = getShortText(obj);
                if (str != null)
                {
                    map.put(str, obj);
                    return str;
                }
                return "";
            }
            
            @Override public T fromString(String str)
            {
                if (!map.containsKey(str))
                {
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
     * @see #getShortText(Object)
     */
    public <T> void applyCellFactory(ListView<T> listView)
    {
        listView.setCellFactory((ListView<T> param) -> getCell());
    }
    
    /**
     * Refreshes the specified {@link ListView}.
     *
     * @param <T>      The type of elements.
     * @param listView The {@link ListView} being refreshed.
     */
    public <T> void refresh(ListView<T> listView)
    {
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
     * @return A new {@link ListCell} that can be used to format {@link ListView ListViews} and {@link ComboBox ComboBoxs}.
     */
    private <T> ListCell<T> getCell()
    {
        return new ListCell<T>()
        {
            @Override public void updateItem(T item, boolean empty)
            {
                super.updateItem(item, empty);
                if (item != null)
                {
                    setText(getShortText(item));
                    if (item instanceof Colorable)
                    {
                        java.awt.Color awtColor = ((Colorable) item).getColorAWT();
                        if (awtColor != null)
                            setTextFill(Color.color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), awtColor.getAlpha()));
                    }
                }
                else
                    setText(null);
            }
        };
    }
    
    /**
     * Returns the currently selected value of the specified {@link ComboBox}.
     *
     * @param <T>      The type of elements in the specified {@link ComboBox}.
     * @param comboBox The {@link ComboBox}.
     * @return The currently selected value of the specified {@link ComboBox}, or null if no element is selected.
     */
    public <T> T getSelectedValue(ComboBox<T> comboBox)
    {
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
     * @return The currently selected value of the specified {@link ListView}, or null if no element is selected.
     */
    public <T> T getSelectedValue(ListView<T> listView)
    {
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
    public synchronized <T> void addElement(T item, ListView<T> listView, boolean focus)
    {
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
     * @see #addElement(Object, ListView, boolean)
     */
    public synchronized <T> void addElement(T item, ListView<T> listView)
    {
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
    public synchronized <T> void addElement(T item, ComboBox<T> comboBox, boolean focus)
    {
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
     * @see #addElement(Object, ComboBox, boolean)
     */
    public synchronized <T> void addElement(T item, ComboBox<T> comboBox)
    {
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
    public synchronized <T> void removeSelected(ListView<T> listView, boolean focus)
    {
        runFX(() -> {
            final T selectedItem = getSelectedValue(listView);
            if (selectedItem != null)
            {
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
    public synchronized <T> void removeSelected(ComboBox<T> comboBox, boolean focus)
    {
        runFX(() -> {
            final T selectedItem = getSelectedValue(comboBox);
            if (selectedItem != null)
            {
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
     * @deprecated Because this method is inefficient - O(n^n)
     */
    public <T extends Enum> void addEnumsToComboBox(ComboBox<T> comboBox, T t, boolean selectFirst, T... excludeValues)
    {
        if (comboBox != null)
        {
            applyCellFactory(comboBox);
            if (t != null)
            {
                final Enum[] e_list = EnumTools.get().list(t);
                for (Enum e: e_list)
                    if (!ArrayTools.contains(e, excludeValues))
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
     * <li>Enums (uses the {@link StringTools#enumToString(Enum)} method)</li>
     * <li>Non-null Objects (uses the {@link Object#toString()} method)</li>
     * </ol>
     *
     * @param obj The {@link Object}.
     * @return A getText representation of the specified {@link Object}, or null if the specified {@link Object} is null.
     */
    public String getShortText(Object obj)
    {
        if (obj != null)
        {
            String str;
            if (obj instanceof Listable)
                str = ((Listable) obj).getShortText();
            else if (obj instanceof Enum)
                str = StringTools.get().enumToString((Enum) obj);
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
    public final int NO_MIN_SIZE_LOCK = 0;
    /**
     * An unbounded minimum size.
     */
    public final int UNBOUNDED_SIZE_LOCK = -1;
    /**
     * The current size.
     */
    public final int CURRENT_SIZE_LOCK = -2;
    /**
     * The current minimum size.
     */
    public final int NO_CHANGE_SIZE_LOCK = Integer.MIN_VALUE;
    
    /**
     * Locks the size of the specified {@link Stage} based on the specified {@link FXDialogTools.SizeType}.
     *
     * @param stage    The {@link Stage} being locked.
     * @param sizeType The {@link FXDialogTools.SizeType} that controls how the {@link Stage} can be resized.
     * @return True if the {@link Stage Stage's} size was locked successfully, false otherwise.
     */
    public boolean lockSize(Stage stage, FXDialogTools.SizeType sizeType)
    {
        if (stage != null && sizeType != null)
        {
            if (sizeType == FXDialogTools.SizeType.MINIMUM_SIZE)
            {
                stage.setMinWidth(stage.getWidth());
                stage.setMinHeight(stage.getHeight());
            }
            else if (sizeType == FXDialogTools.SizeType.NON_RESIZEABLE)
                stage.setResizable(false);
            else if (sizeType == FXDialogTools.SizeType.RESIZEABLE)
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
     * @return True if the {@link Stage Stage's} size was locked successfully, false otherwise.
     * @see #NO_MIN_SIZE_LOCK
     * @see #UNBOUNDED_SIZE_LOCK
     * @see #CURRENT_SIZE_LOCK
     * @see #NO_CHANGE_SIZE_LOCK
     */
    public boolean lockSize(Stage stage, double minWidth, double minHeight)
    {
        if (stage != null)
        {
            if (minWidth == -1)
                minWidth = Integer.MAX_VALUE;
            else if (minWidth == -2)
                minWidth = stage.getWidth();
            
            if (minHeight == -1)
                minHeight = Integer.MAX_VALUE;
            else if (minHeight == -2)
                minHeight = stage.getHeight();
            
            //
            
            if (minWidth >= 0)
            {
                if (stage.getWidth() < minWidth)
                    stage.setWidth(minWidth);
                stage.setMinWidth(minWidth);
            }
            
            if (minHeight >= 0)
            {
                if (stage.getHeight() < minHeight)
                    stage.setHeight(minHeight);
                stage.setMinHeight(minHeight);
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * Locks the size of the specified {@link Region} based on the specified {@link FXDialogTools.SizeType}.
     * <p>
     * Note that {@link Region regions} do not have a {@code setResizable()} method. This methods:
     * <ul>
     * <li>{@link FXDialogTools.SizeType#NON_RESIZEABLE} sets the {@link Region#setMinSize(double, double) minimum size} and {@link Region#setMaxSize(double, double) maximum size} to the current size of the {@link
     * Region}.</li>
     * <li>{@link FXDialogTools.SizeType#RESIZEABLE} sets the {@link Region#setMaxSize(double, double) maximum size} to {@link Integer#MAX_VALUE}.</li>
     * </ul>
     *
     * @param region   The {@link Region} being locked.
     * @param sizeType The {@link FXDialogTools.SizeType} that controls how the {@link Region} can be resized.
     * @return True if the {@link Region Region's} size was locked successfully, false otherwise.
     */
    public boolean lockSize(Region region, FXDialogTools.SizeType sizeType)
    {
        if (region != null && sizeType != null)
        {
            double width = region.getWidth();
            double height = region.getHeight();
            if (sizeType == FXDialogTools.SizeType.MINIMUM_SIZE)
                region.setMinSize(width, height);
            else if (sizeType == FXDialogTools.SizeType.NON_RESIZEABLE)
            {
                region.setMaxSize(width, height);
                region.setMinSize(width, height);
            }
            else if (sizeType == FXDialogTools.SizeType.RESIZEABLE)
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
     * @return The recently loaded {@link FXMLLoader} for the specified resource path.
     */
    public <T> FXMLLoader loadFXML(String resource, FXMLLoader loader, T controller)
    {
        loader = loader == null ? new FXMLLoader() : loader;
        try
        {
            //				Printing.dev("Initializing FXML Loader for resource: " + resource);
            //				System.err.println("Initializing FXML Loader for resource: " + resource);
            if (controller != null)
                loader.setController(controller);
            loader.load(TB.resources().getResourceStream(resource));
            return loader;
        }
        catch (Exception e)
        {
            throw new UndefinedRuntimeException("Error loading resource: " + resource);
        }
    }
    
    //</editor-fold>
    
    //
    
    //<editor-fold desc="Helper Methods">
    
    private boolean isValidSuffix(String str, String... suffixes)
    {
        for (final String suffix: suffixes)
            if (str.contains(suffix) && (!str.endsWith(suffix) || StringTools.get().getCount(str, suffix) != 1 || str.length() == suffix.length()))
                return false;
        return true;
    }
    
    private boolean isValidPrefix(String str, String... prefixes)
    {
        for (String prefix: prefixes)
            if (str.contains(prefix) && (!str.startsWith(prefix) || StringTools.get().getCount(str, prefix) != 1))
                return false;
        return true;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Pseudo Classes">
    
    public final PseudoClass INVALID = PseudoClass.getPseudoClass("invalid");
    public final PseudoClass DISABLED = PseudoClass.getPseudoClass("custom_disabled");
    
    //</editor-fold>
    
    //<editor-fold desc="Classes">
    
    //<editor-fold desc="Overlay Enums">
    
    public enum OverlayBackgroundOpacityType
    {
        TRANSPARENT,
        OPAQUE
    }
    
    public enum OverlayResizeType
    {
        FILL,
        INHERIT
    }
    
    //</editor-fold>
    
    /**
     * Defines the auto-complete mode for an {@link #autoCompleteComboBox(ComboBox, AutoCompleteMode, String) ComboBox}.
     */
    public enum AutoCompleteMode
    {
        STARTS_WITH, CONTAINING,
        ;
    }
    
    public interface ChildEditable
    {
        ChildEditable DISABLE = (final Node node) -> node.setDisable(true);
        ChildEditable ENABLE = (final Node node) -> node.setDisable(false);
        ChildEditable HIDE = (final Node node) -> node.setVisible(false);
        ChildEditable SHOW = (final Node node) -> node.setVisible(true);
        
        void edit(Node component);
    }
    
    public class NodeMover
    {
        
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
        
        public NodeMover(Stage stage, Region corner, Region top, Region bottom, Region left, Region right, Node... excludeNodes)
        {
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
            
            this.excludeNodes = excludeNodes != null ? excludeNodes : null;
            
            this.anchorProperty = new SimpleObjectProperty<>();
            this.stageWidthProperty = new SimpleDoubleProperty();
            this.stageHeightProperty = new SimpleDoubleProperty();
        }
        
        //
        
        public Scene scene()
        {
            return stage.getScene();
        }
        
        //
        
        private void begin()
        {
            addEventFilters(corner, resizingCorner, Cursor.SE_RESIZE, true);
            addEventFilters(top, resizingTop, Cursor.V_RESIZE, false);
            addEventFilters(bottom, resizingBottom, Cursor.V_RESIZE, true);
            addEventFilters(left, resizingLeft, Cursor.H_RESIZE, false);
            addEventFilters(right, resizingRight, Cursor.H_RESIZE, true);
        }
        
        private void addEventFilters(Region region, ReadOnlyBooleanWrapper wrapper, Cursor cursor, boolean active)
        {
            region.setMouseTransparent(!active);
            if (active)
            {
                region.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
                    if (!stage.isMaximized())
                    {
                        scene().setCursor(cursor);
                        event.consume();
                    }
                });
                region.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
                    if (anchorProperty.get() == null)
                    {
                        scene().setCursor(Cursor.DEFAULT);
                        event.consume();
                    }
                });
                region.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (!stage.isMaximized())
                    {
                        wrapper.set(true);
                        anchorProperty.set(new Point2D(event.getScreenX() - stage.getX(), event.getScreenY() - stage.getY()));
                        stageWidthProperty.set(stage.getWidth());
                        stageHeightProperty.set(stage.getHeight());
                        event.consume();
                    }
                });
                region.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                    if (wrapper.get())
                    {
                        scene().setCursor(Cursor.DEFAULT);
                        anchorProperty.set(null);
                        wrapper.set(false);
                    }
                });
                region.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
                    if (wrapper.get() && !stage.isMaximized())
                    {
                        drag(event);
                        event.consume();
                    }
                });
            }
        }
        
        private void drag(MouseEvent event)
        {
            Cursor cursor = scene().getCursor();
            if (cursor != null)
            {
                if (cursor == Cursor.H_RESIZE || cursor == Cursor.SE_RESIZE)
                    setWidth(event);
                if (cursor == Cursor.V_RESIZE || cursor == Cursor.SE_RESIZE)
                    setHeight(event);
            }
        }
        
        private void setWidth(MouseEvent event)
        {
            stage.setWidth(Math.max(stage.getMinWidth(), stageWidthProperty.get() + (event.getScreenX() - (anchorProperty.get().getX() + stage.getX()))));
            event.consume();
        }
        
        private void setHeight(MouseEvent event)
        {
            stage.setHeight(Math.max(stage.getMinHeight(), stageHeightProperty.get() + (event.getScreenY() - (anchorProperty.get().getY() + stage.getY()))));
            event.consume();
        }
        
        private boolean isOnExcludeNode()
        {
            if (excludeNodes != null)
                for (Node node: excludeNodes)
                    if (node != null && isMouseOnNode(node))
                        return true;
            return false;
        }
    }
    
    //</editor-fold>
}

/*
 * TODO:
 * 1.
 */