package com.taco.suit_lady.ui;

import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.ui.ui_internal.controllers.Controller;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class UIPageController<T extends UIPage<?>> extends Controller {
    
    private T page; // Functionally Final
    
    protected UIPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    /**
     * <p>Returns the {@link UIPage} that is controller by this {@link UIPageController}.</p>
     *
     * @return The {@link UIPage} that is controller by this {@link UIPageController}.
     */
    public @NotNull T getPage() { return page; }
    
    /**
     * <p>Sets the {@link UIPage} whose UI is controlled by this {@link UIPageController} to the specified value.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link #setPage(UIPage) This method} is called internally by the {@link UIPage} {@link UIPage#UIPage(UIBook, Object...) constructor}.</li>
     *     <li>{@link #setPage(UIPage) This method} should <i>never</i> be called directly, or anywhere outside of the {@link UIPage} {@link UIPage#UIPage(UIBook, Object...) constructor}.</li>
     *     <li>Once the {@link UIPage} has been {@link #setPage(UIPage) set}, <code><i>{@link #onPageBindingComplete()}</i></code> is called to wrap up the operation.</li>
     * </ol>
     *
     * @param page The {@link UIPage} this {@link UIPageController} is to control.
     */
    protected void setPage(@NotNull UIPage<?> page) {
        try {
            this.page = (T) Exc.nullCheck(page, "UIPage");
        } catch (Exception e) {
            throw Exc.ex(e, "UIPage must be of type T [" + page.getClass() + "]");
        }
        onPageBindingComplete();
    }
    
    /**
     * <p>Executed at the very end of <code><i>{@link #setPage(UIPage)}</i></code>.</p>
     */
    protected void onPageBindingComplete() { }
}