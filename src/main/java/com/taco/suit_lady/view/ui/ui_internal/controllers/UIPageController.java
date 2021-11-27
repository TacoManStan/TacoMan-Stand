package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.pages.ExamplePage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class UIPageController<T extends UIPage<?>> extends Controller
{
    private T page; // Functionally Final
    
    protected UIPageController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
    
    /**
     * <p>Returns the {@link UIPage} that is controller by this {@link UIPageController}.</p>
     *
     * @return The {@link UIPage} that is controller by this {@link UIPageController}.
     */
    public @NotNull T getPage()
    {
        return page;
    }
    
    /**
     * <p>Sets the {@link UIPage} whose UI is controlled by this {@link UIPageController} to the specified value.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link #setPage(UIPage) This method} should be called as <code><i>{@link UIPage#getController() getController()}<b>.</b>{@link #setPage(UIPage) setPage}<b>(</b>this<b>)</b></i></code> at the end of every {@link UIPage} implementations constructor.</li>
     *     <li>This method should <i>never</i> be used except for as described above.</li>
     *     <li>Refer to <code><i>{@link ExamplePage#ExamplePage(UINode, String) ExamplePage(UINode, String)}</i></code> for example usage.</li>
     * </ol>
     *
     * @param page The {@link UIPage} this {@link UIPageController} is to control.
     */
    // TO-EXPAND
    public void setPage(@NotNull UIPage<?> page)
    {
        ExceptionTools.nullCheck(page, "UIPage");
        
        try {
            this.page = (T) page;
        } catch (Exception e) {
            throw ExceptionTools.ex(e, "UIPage must be of type T [" + page.getClass() + "]");
        }
        
        // TODO - This is smelly af, move it somewhere else and properly abstract it.
        onPageBindingComplete();
    }
    
    /**
     * <b>--- To Format ---</b>
     * <br><br>
     * Runs UIPageController have been {@link #initialize() auto-initialized} by the JFX internals and immediately after both the UIPage and UIPageController have non-null references to each other.
     */
    protected void onPageBindingComplete() { }
}