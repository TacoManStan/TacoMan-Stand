package com.taco.tacository.data.dummy_data.calendar;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;

public class Calendar
{
    private final ReadOnlyListWrapper<CalendarObject> calendarObjectsWrapper;
    
    public Calendar()
    {
        this.calendarObjectsWrapper = new ReadOnlyListWrapper<>();
    }
    
    public ReadOnlyListProperty<CalendarObject> calendarObjectsProperty()
    {
        return calendarObjectsWrapper.getReadOnlyProperty();
    }
}
