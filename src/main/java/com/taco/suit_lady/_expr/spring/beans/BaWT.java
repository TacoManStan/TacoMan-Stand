package com.taco.suit_lady._expr.spring.beans;

import com.taco.suit_lady.logic.calendar.CalendarBase;
import com.taco.suit_lady.logic.calendar.CalendarGroups;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p><b>Executes BaWT functions periodically throughout the application runtime.</b></p>
 * <br>
 * <hr>
 * <p><i>Should probably be replaced with JUnit tests at some point, but for now BaWT functions are few enough to be managed here.</i></p>
 * <p><i>TODO - See Above</i></p>
 * <hr>
 */
@Component
public class BaWT
        implements ApplicationContextAware
{
    private ApplicationContext ctx;
    
    public BaWT() { }
    
    //
    
    public void runBaWT_AvatarClick()
    {
        final CalendarGroups cg = ctx.getBean("calendar_base", CalendarBase.class).groups();
        cg.forEach(cge -> System.out.println(cge.getName()));
    }
    
    public String runBaWT_DialogContents()
    {
        return "heheXD";
    }
    
    //
    
    //<editor-fold desc="-- Bean Initialization --">
    
    @Override public void setApplicationContext(ApplicationContext ctx)
    {
        this.ctx = ctx;
    }
    
    //</editor-fold>
}
