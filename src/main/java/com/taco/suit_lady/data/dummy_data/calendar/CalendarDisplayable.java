package com.taco.suit_lady.data.dummy_data.calendar;

import javafx.beans.property.ReadOnlyListProperty;

public interface CalendarDisplayable
{
    ReadOnlyListProperty<CalendarObject> activeObjectsProperty();
}
