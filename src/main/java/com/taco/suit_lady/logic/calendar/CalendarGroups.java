package com.taco.suit_lady.logic.calendar;

import java.util.*;
import java.util.stream.IntStream;

public class CalendarGroups
        implements Collection<CalendarGroup>
{
    private final ArrayList<CalendarGroup> calendarGroups;
    private final Collection<CalendarGroup> readOnlyCalendarGroupsCopy;
    
    CalendarGroups()
    {
        this.calendarGroups = new ArrayList<>();
        this.readOnlyCalendarGroupsCopy = Collections.unmodifiableCollection(calendarGroups);
    }
    
    //
    
    public Collection<CalendarGroup> get()
    {
        // Possibly an unnecessary string of back and forth conversions performed internally - ensure this is optimized.
        // You might also be better off using streams...
        synchronized (readOnlyCalendarGroupsCopy)
        {
            return readOnlyCalendarGroupsCopy;
        }
    }
    
    public ArrayList<CalendarGroup> snapshot()
    {
        synchronized (readOnlyCalendarGroupsCopy)
        {
            final List<CalendarGroup> src = readOnlyCalendarGroupsCopy.stream().toList();
            final ArrayList<CalendarGroup> dest = new ArrayList<>(src.size());
            IntStream.range(0, src.size()).forEach(i -> dest.set(i, src.get(i)));
            return dest;
        }
    }
    
    //
    
    //<editor-fold desc="Collection Implementation">
    
    @Override
    public int size()
    {
        synchronized (readOnlyCalendarGroupsCopy)
        {
            return readOnlyCalendarGroupsCopy.size();
        }
    }
    
    @Override
    public boolean isEmpty()
    {
        synchronized (readOnlyCalendarGroupsCopy)
        {
            return readOnlyCalendarGroupsCopy.isEmpty();
        }
    }
    
    @Override
    public boolean contains(Object o)
    {
        synchronized (readOnlyCalendarGroupsCopy)
        {
            return readOnlyCalendarGroupsCopy.contains(o);
        }
    }
    
    @Override
    public Iterator<CalendarGroup> iterator()
    {
        synchronized (readOnlyCalendarGroupsCopy)
        {
            return readOnlyCalendarGroupsCopy.iterator();
        }
    }
    
    @Override
    public Object[] toArray()
    {
        synchronized (readOnlyCalendarGroupsCopy)
        {
            return readOnlyCalendarGroupsCopy.toArray();
        }
    }
    
    @SuppressWarnings("SuspiciousToArrayCall") @Override
    public <T> T[] toArray(T[] a)
    {
        synchronized (readOnlyCalendarGroupsCopy)
        {
            return readOnlyCalendarGroupsCopy.toArray(a);
        }
    }
    
    @Override
    public boolean containsAll(Collection<?> c)
    {
        synchronized (readOnlyCalendarGroupsCopy)
        {
            return calendarGroups.containsAll(c);
        }
    }
    
    // -- UNSUPPORTED OPERATIONS -- //
    
    @Override
    public boolean add(CalendarGroup calendarGroup)
    {
        throw new UnsupportedOperationException("CalendarGroup Collection implementations are Read-Only.");
    }
    
    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("CalendarGroup Collection implementations are Read-Only.");
    }
    
    
    @Override
    public boolean addAll(Collection<? extends CalendarGroup> c)
    {
        throw new UnsupportedOperationException("CalendarGroups is Read-Only.");
    }
    
    @Override
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("CalendarGroup Collection implementations are Read-Only.");
    }
    
    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("CalendarGroup Collection implementations are Read-Only.");
    }
    
    @Override
    public void clear()
    {
        throw new UnsupportedOperationException("CalendarGroup Collection implementations are Read-Only.");
    }
    
    //
    
    //<editor-fold desc="Protected Collection Modification Implementations">
    
    boolean add_impl(CalendarGroup calendarGroup)
    {
        return calendarGroups.add(calendarGroup);
    }
    
    @SuppressWarnings("SuspiciousMethodCalls")
    boolean remove_impl(Object o)
    {
        return calendarGroups.remove(o);
    }
    
    boolean addAll_impl(Collection<? extends CalendarGroup> c)
    {
        return calendarGroups.addAll(c);
    }
    
    @SuppressWarnings("SuspiciousMethodCalls")
    boolean removeAll_impl(Collection<?> c)
    {
        return calendarGroups.removeAll(c);
    }
    
    @SuppressWarnings("SuspiciousMethodCalls")
    boolean retainAll_impl(Collection<?> c)
    {
        return calendarGroups.retainAll(c);
    }
    
    void clear_impl()
    {
        calendarGroups.clear();
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
