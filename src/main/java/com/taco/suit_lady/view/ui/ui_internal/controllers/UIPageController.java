package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.util.ExceptionTools;
import com.taco.suit_lady.view.ui.UIPage;
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
     *     <li>This method is automatically internally called upon the construction of any {@link UIPage} instance.</li>
     *     <li>
     *         TODO - The page setting operation should be done in the {@link UIPage} constructor.
     *         <p>
     *         The only reason it currently is not is because you need additional experience with Spring.
     *     </li>
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
        this.onPageBindingComplete();
    }
    
    /**
     * <b>--- To Format ---</b>
     * <br><br>
     * Runs UIPageController have been {@link #initialize() auto-initialized} by the JFX internals and immediately after both the UIPage and UIPageController have non-null references to each other.
     */
    protected void onPageBindingComplete() { }
}