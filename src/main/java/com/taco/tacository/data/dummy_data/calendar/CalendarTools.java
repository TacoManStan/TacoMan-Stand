package com.taco.tacository.data.dummy_data.calendar;

import java.util.function.Predicate;

public class CalendarTools
{
    private CalendarTools() { } // No Instance
    
    //
    
    public static int getGridX(CalendarObject event)
    {
        return 0; //nyi
    }
    
    public static int getGridY(CalendarObject event)
    {
        return 0; //nyi
    }
    
    //
    
    /**
     * <b>--- To Format ---</b>
     * <br><br>
     * Checks if the provided {@link CalendarObject} is valid.
     * <br>
     */
    public static boolean validate(CalendarObject calendarObject)
    {
        return calendarObject != null;
    }
    
    /**
     * <b>--- To Format ---</b>
     * <br><br>
     * Returns a {@link Predicate} that always returns true if the {@link CalendarObject} is {@link #validate(CalendarObject) valid}.
     */
    public static Predicate<CalendarObject> getDefaultFilter()
    {
        return calObj -> true;
    }
}
