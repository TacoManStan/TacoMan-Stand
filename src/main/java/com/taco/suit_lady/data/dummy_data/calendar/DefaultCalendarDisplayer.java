package com.taco.suit_lady.data.dummy_data.calendar;

import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
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
public class DefaultCalendarDisplayer
    implements CalendarDisplayable
{
    private final ObservableList<Calendar> calendars;
    private final ReadOnlyObjectWrapper<Predicate<CalendarObject>> filterProperty;
    
    private final int GRID_X = 7;
    private final int GRID_Y = 6;
    private final ObservableList<CalendarObject>[][] calendarObjectMatrix;
    
    public DefaultCalendarDisplayer()
    {
        this.calendars = FXCollections.emptyObservableList();
        this.filterProperty = new ReadOnlyObjectWrapper<>(CalendarTools.getDefaultFilter());
        
        this.calendarObjectMatrix = new ObservableList[GRID_X][GRID_Y];
    }
    
    //
    
    public void addCalendar(Calendar calendar)
    {
        if (calendar == null)
            throw new NullPointerException("Calendar cannot be null.");
        calendars.add(calendar);
    }
    
    public boolean removeCalendar(Calendar calendar)
    {
        return calendars.remove(calendar);
    }
    
    public Calendar removeCalendar(int index)
    {
        return calendars.remove(index);
    }
    
    //
    
    public ReadOnlyObjectProperty<Predicate<CalendarObject>> filterProperty()
    {
        return filterProperty.getReadOnlyProperty();
    }
    
    public Predicate<CalendarObject> getFilter()
    {
        return filterProperty.get();
    }
    
    public void setFilter(Predicate<CalendarObject> filter)
    {
        filterProperty.set(filter);
    }
    
    //
    
    @Override
    public ReadOnlyListProperty<CalendarObject> activeObjectsProperty()
    {
        throw ExceptionTools.nyi();
    }
}