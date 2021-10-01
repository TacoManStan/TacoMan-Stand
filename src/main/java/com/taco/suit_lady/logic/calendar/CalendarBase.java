package com.taco.suit_lady.logic.calendar;

import org.springframework.stereotype.Component;

@Component("calendar_base")
public class CalendarBase
{
    private final CalendarGroups calendarGroups;
    
    CalendarBase()
    {
        this.calendarGroups = new CalendarGroups();
        this.addTestGroups();
    }
    
    private void addTestGroups()
    {
        this.calendarGroups.add_impl(new CalendarGroup("Calendar Group 1"));
        this.calendarGroups.add_impl(new CalendarGroup("Calendar Group 2"));
        this.calendarGroups.add_impl(new CalendarGroup("Calendar Group 3"));
    }
    
    //
    
    public final CalendarGroups groups()
    {
        return calendarGroups;
    }
}
