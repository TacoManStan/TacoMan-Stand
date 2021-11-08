package com.taco.suit_lady.data.dummy_data.calendar;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.function.Predicate;

/**
 * <b>--- To Format ---</b>
 * <br><br>
 * Class responsible for displaying the data enclosed in / managed by a Calendar object.
 * <br>
 * Note that eventually this should be abstracted out into different display methods. For testing, even might want to make a Swing version, idk YOLO.
 */
public class CalendarDisplayer
{
    private final ObservableList<Calendar> calendars;
    private final ReadOnlyObjectWrapper<Predicate<CalendarObject>> filter;
    
    public CalendarDisplayer()
    {
        this.calendars = FXCollections.emptyObservableList();
        this.filter = new ReadOnlyObjectWrapper<>(CalendarTools.getDefaultFilter());
    }
    
    //
    
    public void addCalendar(Calendar calendar)
    {
        if (calendar == null)
            throw new NullPointerException("Calendar cannot be null.");
        
        this.calendars.add(calendar);
    }
    
    public boolean removeCalendar(Calendar calendar)
    {
        return this.calendars.remove(calendar);
    }
    
    public Calendar removeCalendar(int index)
    {
        return this.calendars.remove(index);
    }
}