package com.taco.tacository.data.dummy_data.calendar;

import javafx.beans.property.ReadOnlyListProperty;

public interface CalendarDisplayable
{
    ReadOnlyListProperty<CalendarObject> activeObjectsProperty();
}
