package com.taco.suit_lady.data.dummy_data.calendar;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.ListChangeListener;

public class Calendar
{
    private final ReadOnlyListWrapper<CalendarObject> calendarObjectsWrapper;
    private final CalendarObjectGroup[][] calendarObjectMatrix;
    
    public Calendar()
    {
        this.calendarObjectsWrapper = new ReadOnlyListWrapper<>();
        this.calendarObjectMatrix = new CalendarObjectGroup[7][6];
        
        this.fillCalendarObjectMatrix();
        
        this.calendarObjectsWrapper.addListener((ListChangeListener<CalendarObject>) change -> resync());
    }
    
    public ReadOnlyListProperty<CalendarObject> calendarObjectsProperty()
    {
        return calendarObjectsWrapper.getReadOnlyProperty();
    }
    
    // Must be either synchronized or exclusively accessed sequentially — i.e., not thread safe unless synchronized.
    private void resync()
    {
        clearCalendarObjectMatrix();
        
        for (CalendarObject ce: calendarObjectsWrapper)
        {
            final int gridX = CalendarTools.getGridX(ce);
            final int gridY = CalendarTools.getGridY(ce);
            
            calendarObjectMatrix[gridX][gridY].addCalendarEvent(ce);
        }
    }
    
    /**
     * <b>--- To Format ---</b>
     * <br><br>
     * Clears all CalendarEvent objects from each CalendarEventGroup in the calendar event matrix.
     */
    private void clearCalendarObjectMatrix()
    {
        for (int x = 0; x < 7; x++)
            for (int y = 0; y < 6; y++)
                calendarObjectMatrix[x][y].clear();
    }
    
    /**
     * <b>--- To Format ---</b>
     * <br><br>
     * Should only be executed once during the Calendar's initialization process (either in constructor or if initialize method is added in the future).
     */
    private void fillCalendarObjectMatrix()
    {
        for (int x = 0; x < 7; x++)
            for (int y = 0; y < 6; y++)
                calendarObjectMatrix[x][y] = new CalendarObjectGroup();
    }
}
