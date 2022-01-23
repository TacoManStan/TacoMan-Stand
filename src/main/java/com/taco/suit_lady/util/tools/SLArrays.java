package com.taco.suit_lady.util.tools;

import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.List;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

// TODO - Convert to non-static
public class SLArrays {
    /**
     * <p>Sorts the specified {@link List} using the {@link Comparator#naturalOrder() Natural Order} as the {@link Comparator}.</p>
     * <hr>
     * <h4>Example Implementation</h4>
     * <pre>{@code ...
     * list.sort(Comparator.naturalOrder());
     * return list;}</pre>
     *
     * @param list The {@link List} to be sorted.
     * @param <E>  The type of {@code objects} contained within the specified {@link List}.
     *
     * @return The sorted {@link List}.
     *
     * @see List
     * @see Comparable
     * @see Comparator
     * @see List#sort(Comparator)
     * @see Comparator#naturalOrder()
     */
    @Contract("_ -> param1")
    public static <E extends Comparable<E>> @NotNull List<E> sort(@NotNull List<E> list) {
        list.sort(Comparator.naturalOrder());
        return list;
    }
    
    /**
     * <p>Returns the {@link T element} at the specified {@code movedToIndex} using <i>{@code () -> null}</i> as the {@link Supplier Fallback Supplier}.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link SLArrays}<b>.</b>{@link SLArrays#getAt(int, List, Supplier, Supplier) getAt}<b>(</b>{@code movedToIndex}<b>,</b> {@code list}<b>,</b> <u>{@code () -> null)}</u><b>,</b> <u>{@code () -> null)}</u><b>)</b>
     * </code></i></blockquote>
     *
     * @param index The {@code movedToIndex} to {@link List#get(int) retrieve} the desired {@link T element} from.
     * @param list  The {@link List} from which the returned {@link T element} is {@link List#get(int) retrieved}.
     * @param <T>   The type of element contained in the specified {@link List}.
     *              <p>Also the type of {@link Object} returned by {@link #getAt(int, List, Supplier, Supplier) this method}.
     *
     * @return The {@link T element} at the specified {@code movedToIndex} in the specified {@link List}.
     */
    public static <T> T getAt(@NonNegative int index, @NotNull List<T> list) {
        return getAt(index, list, () -> null);
    }
    
    /**
     * <p>Returns the {@link T element} at the specified {@code movedToIndex} using optional {@link Supplier Fallback Supplier} to handle all types of invalid queries.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link SLArrays}<b>.</b>{@link SLArrays#getAt(int, List, Supplier, Supplier) getAt}<b>(</b>{@code movedToIndex}<b>,</b> {@code list}<b>,</b> {@code fallbackSupplier}<b>,</b> {@code fallbackSupplier}<b>)</b>
     * </code></i></blockquote>
     *
     * @param index            The {@code movedToIndex} to {@link List#get(int) retrieve} the desired {@link T element} from.
     * @param list             The {@link List} from which the returned {@link T element} is {@link List#get(int) retrieved}.
     * @param fallbackSupplier An optional {@link Supplier} that is called if the specified {@code movedToIndex} is invalid for any reason or if the {@link T element} at the specified {@code movedToIndex} is {@code null}.
     * @param <T>              The type of element contained in the specified {@link List}.
     *                         <p>Also the type of {@link Object} returned by {@link #getAt(int, List, Supplier, Supplier) this method}.
     *
     * @return The {@link T element} at the specified {@code movedToIndex} in the specified {@link List}.
     *
     * @see #getAt(int, List, Supplier, Supplier)
     */
    public static <T> T getAt(@NonNegative int index, @NotNull List<T> list, @Nullable Supplier<T> fallbackSupplier) {
        return getAt(index, list, fallbackSupplier, fallbackSupplier);
    }
    
    /**
     * <p>Returns the {@link T element} at the specified {@code movedToIndex} using optional {@link Supplier Fallback Suppliers} to handle various types of invalid queries.</p>
     * <p><b>LOGIC OVERVIEW</b></p>
     * <ol>
     *     <li>
     *         <b>List is <u>Null</u></b>
     *         <ul>
     *             <li>Throw a {@link NullPointerException}.</li>
     *         </ul>
     *     </li>
     *     <li>
     *         <b>Index is <u>Valid</u></b>
     *         <ul>
     *             <li>
     *                 Retrieve the {@link T element} at the specified {@code movedToIndex}.
     *                 <ol>
     *                     <li>
     *                         <b>Element is <u>Not Null</u></b>
     *                         <ul>
     *                             <li>Return the {@link T element}.</li>
     *                         </ul>
     *                     </li>
     *                     <li>
     *                         <b>Element is <u>Null</u></b>
     *                         <ul>
     *                             <li>If the {@link Supplier ifNullFallbackSupplier} is {@code non-null}, return its {@link Supplier#get() value}.</li>
     *                             <li>If the {@link Supplier ifNullFallbackSupplier} is {@code null}, throw a new {@link NullPointerException}.</li>
     *                         </ul>
     *                     </li>
     *                 </ol>
     *             </li>
     *         </ul>
     *     </li>
     *     <li>
     *         <b>Index is <u>Invalid</u></b>
     *         <ul>
     *             <li>If the {@link Supplier ifInvalidIndexFallbackSupplier} is {@code non-null}, return its {@link Supplier#get() value}.</li>
     *             <li>If the {@link Supplier ifInvalidIndexFallbackSupplier} is {@code null}, throw a new {@link IndexOutOfBoundsException}.</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param index                          The {@code movedToIndex} to {@link List#get(int) retrieve} the desired {@link T element} from.
     * @param list                           The {@link List} from which the returned {@link T element} is {@link List#get(int) retrieved}.
     * @param ifInvalidIndexFallbackSupplier An optional {@link Supplier} that is called if the specified {@code movedToIndex} is invalid for any reason.
     * @param ifNullFallbackSupplier         An optional {@link Supplier} that is called if the {@link T element} at the specified {@code movedToIndex} is {@code null}.
     * @param <T>                            The type of element contained in the specified {@link List}.
     *                                       <p>Also the type of {@link Object} returned by {@link #getAt(int, List, Supplier, Supplier) this method}.
     *
     * @return The {@link T element} at the specified {@code movedToIndex} in the specified {@link List}.
     * <p>See documentation body for details.
     *
     * @throws IndexOutOfBoundsException If the specified {@code movedToIndex} is a negative value.
     * @throws NullPointerException      If the specified {@link List} is {@code null}.
     * @throws IndexOutOfBoundsException If the specified {@code movedToIndex} is not within the {@link List#size() bounds} of the specified {@link List} and the {@link Supplier ifInvalidIndexFallbackSupplier} was not specified.
     * @throws NullPointerException      If the {@link T element} at the specified {@code movedToIndex} is {@code null} and the {@link Supplier ifNullFallbackSupplier} was not specified.
     */
    public static <T> T getAt(@NonNegative int index, @NotNull List<T> list, @Nullable Supplier<T> ifInvalidIndexFallbackSupplier, @Nullable Supplier<T> ifNullFallbackSupplier) {
        SLExceptions.nullCheck(list, "List cannot be null.");
        
        T value;
        
        if (index >= 0 && index < list.size()) {
            value = list.get(index);
            if (value == null)
                if (ifNullFallbackSupplier != null)
                    value = ifNullFallbackSupplier.get();
                else
                    throw SLExceptions.ex(new NullPointerException(), "Value at movedToIndex [" + index + "] is null, but ifNull fallback Supplier was not provided.");
        } else if (ifInvalidIndexFallbackSupplier != null)
            value = ifInvalidIndexFallbackSupplier.get();
        else
            throw SLExceptions.ex(new IndexOutOfBoundsException(), "Index value [" + index + "] is not valid for List:  " + list);
        
        return value;
    }
    
    public static boolean isEmpty(boolean @NotNull [] arr) { return arr.length == 0; }
    public static boolean isEmpty(int @NotNull [] arr) { return arr.length == 0; }
    public static boolean isEmpty(double @NotNull [] arr) { return arr.length == 0; }
    public static boolean isEmpty(float @NotNull [] arr) { return arr.length == 0; }
    @Contract(pure = true) public static boolean isEmpty(@NotNull Object @NotNull [] arr) { return arr.length == 0; }
    public static boolean isNullOrEmpty(@Nullable Object @Nullable [] arr) { return arr == null || arr.length == 0; }
    
    /**
     * Filters any duplicates in the specified array and returns a new array
     * with no duplicates.
     * <p>
     * Note that this method <i>does not</i> alter the original array in any
     * way.
     *
     * @param <T> The object type of the array.
     * @param arr The array to filter.
     *
     * @return An array with the duplicates filtered out.
     */
    public static <T> T[] filterDuplicatesArray(T[] arr) {
        if (arr.length < 1)
            return arr;
        HashSet<T> set = new LinkedHashSet<>(java.util.Arrays.asList(arr));
        return set.toArray((T[]) Array.newInstance(arr.getClass(), set.size()));
    }
    
    /**
     * Filters any duplicates in the specified ArrayList and returns a new
     * ArrayList with no duplicates.
     * <p>
     * Note that this method <i>does not</i> alter the original ArrayList in any
     * way.
     *
     * @param <T> The object type of the ArrayList.
     * @param arr The ArrayList to filter.
     *
     * @return An ArrayList with the duplicates filtered out.
     */
    public static <T> ArrayList<T> filterDuplicatesList(T[] arr) {
        return new ArrayList<>(java.util.Arrays.asList(filterDuplicatesArray(arr)));
    }
    
    /**
     * Checks whether or not the specified values can be retrieved from the
     * specified array.
     *
     * @param array The array to check.
     * @param i     The x value to be retrieved from the 2D array.
     * @param j     The y value to be retrieved from the 2D array.
     *
     * @return True if the specified values can be retrieved from the specified
     * array, false otherwise.
     */
    public static boolean isInMatrixBounds(Object[][] array, int i, int j) {
        return i >= 0 && j >= 0 && i < array.length && j < array[i].length;
    }
    
    /**
     * Returns the element at the specified movedToIndex of the specified {@link List}.
     *
     * @param index The movedToIndex.
     * @param list  The {@link List}.
     * @param <T>   The type of elements in the specified {@link List}.
     *
     * @return The element at the specified movedToIndex of the specified {@link List}.
     */
    public static <T> T getElementAt(int index, List<T> list) {
        if (list.size() > index && index >= 0) {
            final T element = list.get(index);
            if (element != null)
                return element;
        }
        return null;
    }
    
    /**
     * Returns the element at the specified movedToIndex of the specified array.
     *
     * @param index The movedToIndex.
     * @param arr   The array.
     * @param <T>   The type of elements in the specified array.
     *
     * @return The element at the specified movedToIndex of the specified array.
     */
    public static <T> T getElementAt(int index, T[] arr) {
        return getElementAt(index, java.util.Arrays.asList(arr));
    }
    
    /**
     * Returns the element that is contained within the specified ArrayList that
     * is equal to the specified element, or null if an Object that is equal to
     * the specified element is not contained within the specified ArrayList.
     *
     * @param <T>     The type of element.
     * @param element The element.
     * @param arr     The ArrayList.
     *
     * @return The element that is contained within the specified ArrayList that
     * is equal to the specified element, or null if an Object that is
     * equal to the specified element is not contained within the
     * specified ArrayList.
     */
    public static <T> T getElement(T element, ArrayList<T> arr) {
        final int index = arr.indexOf(element);
        if (index == -1)
            return null;
        
        return arr.get(index);
    }
    
    /**
     * Returns the element that is contained within the specified array that is
     * equal to the specified element, or null if an Object that is equal to the
     * specified element is not contained within the specified array.
     *
     * @param <T>     The type of element.
     * @param element The element.
     * @param arr     The ArrayList.
     *
     * @return The element that is contained within the specified array that is
     * equal to the specified element, or null if an Object that is
     * equal to the specified element is not contained within the
     * specified array.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> T getElement(T element, T... arr) {
        return getElement(element, new ArrayList<>(java.util.Arrays.asList(arr)));
    }
    
    /**
     * Returns the first non-null element in the specified List. If none exists,
     * returns null.
     *
     * @param <T>  The type of elements in the specified List.
     * @param list The ArrayList.
     *
     * @return The first non-null element in the specified List. If none exists,
     * returns null.
     */
    public static <T> T getFirstNonNullElement(List<T> list) {
        if (list != null)
            for (T o: list)
                if (o != null)
                    return o;
        return null;
    }
    
    /**
     * Returns the first non-null element in the specified array. If none
     * exists, returns null.
     *
     * @param <T> The type of elements in the specified array.
     * @param arr The array.
     *
     * @return The first non-null element in the specified array. If none
     * exists, returns null.
     */
    @SafeVarargs
    public static <T> T getFirstNonNullElement(T... arr) {
        if (arr != null)
            for (T o: arr)
                if (o != null)
                    return o;
        return null;
    }
    
    /**
     * Searches the specified ArrayList for the largest portion of the specified
     * sub ArrayList. The portion of the sub ArrayList that is found always
     * starts at element 0.
     *
     * @param <T>    The type of the ArrayLists
     * @param arr    The base ArrayList.
     * @param subArr The ArrayList that is being searched for.
     *
     * @return A point representing the location of the sub array. The x
     * coordinate of the point is the movedToIndex of the first matching
     * element in the base array that was found, the y coordinate is the
     * last matching element in the base array that was found.
     */
    public static <T> Point getLongestIndex(ArrayList<T> arr, ArrayList<T> subArr) {
        ArrayList<Integer> possibleIndexes = new ArrayList<>();
        T sub1 = subArr.get(0);
        for (int i = 0; i < arr.size(); i++)
            if (arr.get(i).equals(sub1))
                possibleIndexes.add(i);
        int longest = 0;
        int tempIndex = -1;
        for (int index: possibleIndexes)
            for (int i = 0; i < subArr.size(); i++)
                if (i + index >= arr.size() || !subArr.get(i).equals(arr.get(i + index))) {
                    if (tempIndex == -1 || i >= longest) {
                        longest = i;
                        tempIndex = index;
                    }
                    break;
                }
        int min = tempIndex;
        int max = tempIndex + longest;
        for (int i = min; i < max; i++)
            System.out.println(i + ": " + arr.get(i));
        return new Point(min, max);
    }
    
    /**
     * Concatenates the specified arrays into one array.
     *
     * @param <T>  The generic type of the array.
     * @param arrs The arrays.
     *
     * @return The result of the second array being concatenated onto the first
     * array.
     */
    @SafeVarargs
    public static <T> T[] concat(T[]... arrs) {
        if (arrs != null)
            if (arrs.length > 1) {
                int totalLength = 0;
                for (T[] arr: arrs)
                    if (arr != null)
                        totalLength += arr.length;
                T[] arr = (T[]) Array.newInstance(arrs[0].getClass().getComponentType(), totalLength);
                int currentIndex = 0;
                for (T[] arr2: arrs) {
                    System.arraycopy(arr2, 0, arr, currentIndex, arr2.length);
                    currentIndex += arr2.length;
                }
                return arr;
            } else
                return arrs[0];
        return null;
    }
    
    @SafeVarargs
    public static <T> T[] concat(boolean append, T[] arr, T... toConcat) {
        return append ? concat(arr, toConcat) : concat(toConcat, arr);
    }
    
    /**
     * Concatenates the second array onto the first array and returns the
     * result.
     *
     * @param arr1 The first array.
     * @param arr2 The second array.
     *
     * @return The result of the second array being concatenated onto the first
     * array.
     */
    public static int[] concat(int[] arr1, int[] arr2) {
        int[] copy = (int[]) Array.newInstance(arr1.getClass().getComponentType(), arr1.length + arr2.length);
        System.arraycopy(arr1, 0, copy, 0, arr1.length);
        System.arraycopy(arr2, 0, copy, arr1.length, arr2.length);
        return copy;
    }
    
    /**
     * Calculates the median of the specified array of ints.
     *
     * @param arr    The array of ints.
     * @param offset The offset of the median. -1 is default (.50). The higher the
     *               offset, the higher the "median" value will be.
     *
     * @return The median of the specified array of ints. Returns -1 if the
     * offset is invalid.
     */
    public static int getMedian(int[] arr, double offset) {
        if (offset == -1)
            offset = .50;
        else if (offset <= 0 || offset > 1)
            return -1;
        final int[] sortedArr = arr.clone();
        java.util.Arrays.sort(sortedArr);
        if (sortedArr.length == 0)
            return -1;
        else if ((int) (sortedArr.length * offset) <= 0)
            return sortedArr[0];
        else if ((int) (sortedArr.length * offset) >= sortedArr.length)
            return sortedArr[sortedArr.length - 1];
        if (sortedArr.length % 2d == 0) {
            int length = (int) (sortedArr.length * offset);
            if (length > 0)
                return (sortedArr[(int) (sortedArr.length * offset)]
                        + sortedArr[((int) (sortedArr.length * offset)) - 1]) / 2;
        } else
            return sortedArr[(int) (sortedArr.length * offset)];
        return -1;
    }
    
    /**
     * Converts the List of Integers to an array of ints.
     *
     * @param arr The List of Integers.
     *
     * @return An array of ints the same as the specified List of Integers.
     */
    public static int[] convertIntegers(List<Integer> arr) {
        int[] ret = new int[arr.size()];
        Iterator<Integer> iterator = arr.iterator();
        for (int i = 0; i < ret.length; i++)
            ret[i] = iterator.next();
        return ret;
    }
    
    /**
     * Returns the movedToIndex of the specified value in the specified array.
     *
     * @param <T>   The type of elements inside the array.
     * @param value The value being searched for.
     * @param arr   The array being searched.
     *
     * @return The movedToIndex of the specified value in the specified array.
     */
    @SafeVarargs
    public static <T> int indexOf(T value, T... arr) {
        if (value != null)
            for (int i = 0; i < arr.length; i++)
                if (arr[i] != null)
                    if (value.equals(arr[i]))
                        return i;
        return -1;
    }
    
    public static <T> T[] getReverse(T[] arr) {
        final T[] clone = arr.clone();
        reverse(clone);
        return clone;
    }
    
    public static <T> T[] reverse(T[] arr) {
        if (arr != null) {
            final T[] clone = arr.clone();
            for (int i = 0; i < arr.length; i++)
                arr[i] = clone[arr.length - (i + 1)];
        }
        return arr;
    }
    
    public static <T> void edit(List<T> list, ElementCopier<T, T> editer) {
        if (list != null && editer != null)
            for (int i = 0; i < list.size(); i++)
                try {
                    list.set(i, editer.copy(i, list.get(i)));
                } catch (Exception ignore) {
                }
    }
    
    /**
     * Returns the furthest left point between the specified min and max indexes
     * of the specified array.
     *
     * @param min      The minimum (inclusive) movedToIndex.
     * @param max      The maximum (non-inclusive) movedToIndex.
     * @param lookNull True if this method should look for a null element, false
     *                 otherwise.
     * @param arr      The array.
     * @param <T>      The type of elements inside the array.
     *
     * @return The furthest left point between the specified min and max indexes
     * of the specified array, or -1 if no elements were found.
     */
    private static <T> int getFurthestLeft(int min, int max, boolean lookNull, T[] arr) {
        boolean reached = false;
        for (int i = min; i < max; i++)
            if ((arr[i] == null) == lookNull) {
                if (reached || lookNull)
                    return i;
            } else
                reached = true;
        return -1;
    }
    
    public static <T> T[] createAndFillArray(List<T> list, Class<T> clazz) {
        T[] arr = createArray((Class<T[]>) SLTools.getArrayClass(clazz), list.size());
        if (arr != null)
            for (int i = 0; i < list.size(); i++) {
                T element = list.get(i);
                if (element != null)
                    arr[i] = element;
            }
        return arr;
    }
    
    public static <T> T[] createArray(Class<T[]> type, int size) {
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }
    
    //
    
    /**
     * Returns the {@link Map#values() values} of the specified {@link Map} as a new {@code List}.
     *
     * @param map The {@code Map}.
     * @param <V> The type of values stored within the specified {@code Map}.
     *
     * @return The values of the specified {@code Map} as a new {@code List}.
     *
     * @throws NullPointerException if the specified {@code Map} is null.
     * @see #getMapValues(Map, List)
     */
    public static <V> ArrayList<V> getMapValues(Map<?, V> map) {
        return (ArrayList<V>) getMapValues(SLExceptions.nullCheck(map, "Map"), null);
    }
    
    /**
     * Appends the {@link Map#values() values} of the specified {@link Map} onto the specified {@code List}, and then returns the {@code List}.
     * <p>
     * If the specified {@code List} is null, a new {@code ArrayList} is created.
     *
     * @param map        The {@code Map}.
     * @param targetList The {@code List} the values are being appended onto.
     *                   <br>Specify null to create a new {@code ArrayList}.
     * @param <V>        The type of values stored within the specified {@code Map}.
     *
     * @return The specified {@code List} with all values of the specified {@code Map} appended onto it.
     *
     * @throws NullPointerException if the specified {@code Map} is null.
     * @see #getMapValues(Map)
     */
    public static <V> List<V> getMapValues(Map<?, V> map, List<V> targetList) {
        Collection<V> _mapContents = SLExceptions.nullCheck(map, "Map").values();
        
        if (targetList == null)
            return new ArrayList<>(_mapContents);
        
        if (!targetList.addAll(_mapContents))
            throw SLExceptions.ex("Failed to add contents of map (" + map + ")" + " to target list (" + targetList + ")");
        
        return targetList;
    }
    
    
    // <editor-fold desc="Add/Remove">
    
    // <editor-fold desc="Add">
    
    /**
     * Adds the specified element to the specified {@code List}. The element is
     * added if and only if the element is {@code non-null}, and the specified
     * {@code List} does not already contain the element.
     *
     * @param list The {@code List} being added to.
     * @param obj  The element being added to the specified {@code List}.
     * @param <T>  The type of elements contained within the specified
     *             {@code List}.
     *
     * @throws NullPointerException if the specified {@code List} is null.
     */
    public static <T> void add(List<T> list, T obj) {
        if (!list.contains(obj))
            list.add(obj);
    }
    
    /**
     * Adds the specified element to the specified {@code List} if the specified
     * {@link Predicate} is met.
     *
     * @param list      The {@code List} being added to.
     * @param condition The {@code Predicate} that must be met in order for the
     *                  specified element to be added to the specified {@code List}.
     * @param obj       The element being added to the specified {@code List}.
     * @param <T>       The type of elements contained within the specified
     *                  {@code List}.
     *
     * @throws NullPointerException If the {@code List} or {@code Predicate} is {@code null}.
     */
    public static <T> void addIf(List<T> list, Predicate<T> condition, T obj) {
        if (condition.test(obj))
            list.add(obj);
    }
    
    /**
     * Adds each element in the specified array to the specified {@code List}
     * that meets the specified {@link Predicate}.
     *
     * @param list      The {@code List} being added to.
     * @param condition The {@code Predicate} that must be met in order for each
     *                  element to be added to the specified {@code List}.
     * @param lock      The {@code Lock} to be used to synchronize the addition of
     *                  each element in the specified array. Specify {@code null} if
     *                  synchronization is not needed.
     * @param objs      The elements being added to the specified {@code List}.
     * @param <T>       The type of elements contained within the specified
     *                  {@code List}.
     *
     * @throws NullPointerException If the {@code List}, {@code Predicate}, or array of elements
     *                              is {@code null}.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> void addIf(List<T> list, Predicate<T> condition, Lock lock, T... objs) {
        if (lock != null)
            for (T o: objs)
                addIf(list, condition, o);
        else
            for (T o: objs)
                addIf(list, condition, o);
    }
    
    /**
     * <p>Adds the specified {@link T element} to the front of the specified {@link List}.</p>
     *
     * @param list The {@link List} the {@link T element} is being added to.
     * @param obj  The {@link T element} being added.
     * @param <T>  The type of {@link T element} being added.
     */
    public static <T> void addToFront(List<T> list, T obj) {
        SLExceptions.nullCheck(list, "List").add(0, SLExceptions.nullCheck(obj, "Object Param"));
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Insert">
    
    /**
     * Inserts the specified {@code object} into the specified {@code List}
     * directly after the first instance of the specified
     * {@code search element}.
     * <p>
     * If the specified {@code search element} is not contained within the
     * specified {@code List}, this method returns false.
     *
     * @param list          The {@code List} the specified {@code object} is being
     *                      inserted into.
     * @param searchElement The {@code element} the specified {@code object} is being
     *                      inserted after.
     * @param objToInsert   The {@code object} being inserted into the specified
     *                      {@code List}.
     * @param <T>           The type of {@code objects} contained within the specified
     *                      {@code List}.
     *
     * @return True if the specified {@code object} was inserted correctly,
     * false if it was not.
     *
     * @throws NullPointerException If the specified {@code List} is {@code null}.
     */
    public static <T> boolean insertAfter(List<T> list, T searchElement, T objToInsert) {
        throw SLExceptions.nyi();
    } // TODO
    
    /**
     * Inserts the specified {@code object} into the specified {@code List}
     * directly before the first instance of the specified
     * {@code search element}.
     * <p>
     * If the specified {@code search element} is not contained within the
     * specified {@code List}, this method returns false.
     *
     * @param list          The {@code List} the specified {@code object} is being
     *                      inserted into.
     * @param searchElement The {@code element} the specified {@code object} is being
     *                      inserted after.
     * @param objToInsert   The {@code object} being inserted into the specified
     *                      {@code List}.
     * @param <T>           The type of {@code objects} contained within the specified
     *                      {@code List}.
     *
     * @return True if the specified {@code object} was inserted correctly,
     * false if it was not.
     *
     * @throws NullPointerException If the specified {@code List} is {@code null}.
     */
    public static <T> boolean insertBefore(List<T> list, T searchElement, T objToInsert) {
        throw SLExceptions.nyi();
    } // TODO
    
    //
    
    /**
     * Specifies how an {@code element} is to be inserted into a {@code List}.
     *
     * @see #insertBefore(List, Object, Object)
     * @see #insertAfter(List, Object, Object)
     */
    public enum InsertType {
        BEFORE, AFTER
    } // TODO - Implement into applicable methods.
    
    // </editor-fold>
    
    // <editor-fold desc="Remove">
    
    /**
     * Removes all null elements from the specified {@link Collection}.
     *
     * @param list The {@link Collection}.
     * @param <T>  The type of elements in the host {@link Collection}.
     */
    public static <T> void removeNull(Collection<T> list) {
        list.removeIf(element -> element == null);
    }
    
    /**
     * Removes all null elements from the specified array, and returns the
     * result as an {@link ArrayList}.
     * <p>
     * The source array is <i>NOT</i> modified.
     *
     * @param sourceArr The array being shifted.
     * @param <T>       The type of elements inside the array.
     */
    public static <T> List<T> removeNull(T[] sourceArr) {
        if (sourceArr != null) {
            final ArrayList<T> list = new ArrayList<>(sourceArr.length);
            
            for (T t: sourceArr)
                if (t != null)
                    list.add(t);
            
            return list;
        }
        
        return null;
    }
    
    //
    
    public static <V> boolean remove(Map<?, V> map, V obj) {
        SLExceptions.nullCheck(map, "Map");
        SLExceptions.nullCheck(obj, "Object to Remove");
        //        ConsoleBB.CONSOLE.dev("Removing \"" + obj + "\" from \"" + map + "\"");
        
        // DO NOT SIMPLIFY: removeAll(obj) would only remove the first instance.
        return map.values().removeAll(Collections.singleton(obj));
    }
    
    public static <V> boolean removeFirst(Map<?, V> map, V obj) {
        SLExceptions.nullCheck(map, "Map");
        SLExceptions.nullCheck(obj, "Object to Remove");
        
        return map.values().remove(obj);
    }
    
    // </editor-fold>
    
    // </editor-fold>
    
    // <editor-fold desc="Contains">
    
    /**
     * Checks to see if the specified integer array contains the specified
     * value.
     *
     * @param value The value.
     * @param array The array.
     *
     * @return True if the array contains the value, false otherwise.
     */
    public static boolean contains(int value, int... array) {
        if (array != null)
            for (int i: array)
                if (i == value)
                    return true;
        return false;
    }
    
    /**
     * Checks to see if the specified short array contains the specified value.
     *
     * @param value The key.
     * @param array The array.
     *
     * @return True if the array contains the key, false otherwise.
     */
    public static boolean contains(short value, short... array) {
        if (array != null)
            for (short a: array)
                if (a == value)
                    return true;
        return false;
    }
    
    /**
     * Checks to see if the specified char array contains the specified value.
     *
     * @param value The key.
     * @param array The array.
     *
     * @return True if the array contains the key, false otherwise.
     */
    public static boolean contains(char value, char... array) {
        if (array != null)
            for (char a: array)
                if (a == value)
                    return true;
        return false;
    }
    
    /**
     * Checks to see if the specified object array contains the specified
     * object.
     *
     * @param <T>    The type of array.
     * @param object The object.
     * @param array  The array.
     *
     * @return True if the array contains the object, false otherwise.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> boolean contains(T object, T... array) {
        return array != null && Stream.of(array).anyMatch(e -> Objects.equals(e, object));
    }
    
    /**
     * Checks if the specified object array contains the an object that matches
     * the specified {@link Predicate filter}.
     *
     * @param filter The {@link Predicate filter}.
     * @param array  The array.
     * @param <T>    The type of elements in the specified array.
     *
     * @return True if the specified object array contains the specified object
     * based on the specified {@link Predicate}, false otherwise.
     */
    @SafeVarargs
    public static <T> boolean contains(Predicate<T> filter, T... array) {
        if (array != null)
            for (T obj: array)
                try {
                    if (obj != null && (filter == null || filter.test(obj)))
                        return true;
                } catch (Exception ignore) { }
        return false;
    }
    
    /**
     * Checks to see if the specified object array contains the specified
     * object.
     * <p>
     * Uses the specified ArrayComparable to check the equality of the specified
     * objects.
     *
     * @param <T>        The type of array.
     * @param object     The object.
     * @param comparator The {@link Comparator} being used to compare the elements of
     *                   the array..
     * @param array      The array.
     *
     * @return True if the array contains the object, false otherwise.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> boolean containsCompare(T object, Comparator<T> comparator, T... array) {
        if (array != null && object != null)
            for (T obj: array)
                if (obj != null && comparator.compare(object, obj) > 0)
                    return true;
        return false;
    }
    
    /**
     * Checks if the specified array contains any null elements.
     * <p>
     * This method will return {@code false} if the specified array itself is null.
     *
     * @param array The array.
     * @param <T>   The type of elements in the array.
     *
     * @return True if the specified array contains any null elements, false otherwise.
     */
    @SafeVarargs
    public static <T> boolean containsNull(T... array) {
        if (array != null)
            return Stream.of(array).anyMatch(e -> e == null);
        return false;
    }
    
    /**
     * Checks to see if the specified array contains <i>ANY</i> of the specified
     * elements. Returns null if either of the specified arrays is null.
     *
     * @param array    The array.
     * @param elements The elements.
     * @param <T>      The type of elements in both arrays.
     *
     * @return True if the specified array contains <i>ANY</i> of the specified
     * elements, false otherwise.
     */
    @SafeVarargs
    public static <T> boolean containsAny(T[] array, T... elements) {
        return array != null && containsAny(java.util.Arrays.asList(array), elements);
    }
    
    /**
     * Checks if the specified {@link Collection} contains <i>ANY</i> of the
     * specified elements. Returns false if either the {@link Collection} or
     * array is null.
     *
     * @param collection The {@link Collection}.
     * @param elements   The elements.
     * @param <T>        The type of elements in both the {@link Collection} and array.
     *
     * @return True if the specified {@link Collection} contains <i>ANY</i> of
     * the specified elements, false otherwise.
     */
    @SafeVarargs
    public static <T> boolean containsAny(Collection<T> collection, T... elements) {
        return collection != null && elements != null &&
               collection.stream().anyMatch(
                       e1 -> Stream.of(elements).anyMatch(
                               e2 -> Objects.equals(e1, e2)));
    }
    
    /**
     * Checks if any of the specified array's contents matches the specified
     * {@link Predicate}. Returns false if either the array or filter is null.
     *
     * @param array  The array.
     * @param filter The {@link Predicate} being used as the filter.
     * @param <T>    The type of elements in the specified {@link Collection}.
     *
     * @return True if any of the specified array's contents matches the
     * specified {@link Predicate}, false otherwise.
     */
    public static <T> boolean containsAnyMatching(T[] array, Predicate<T> filter) {
        return array != null && java.util.Arrays.stream(array).anyMatch(filter);
    }
    
    /**
     * Checks if any of the specified {@link Collection Collection's} contents
     * matches the specified {@link Predicate}. Returns false if either the
     * {@link Collection} or filter is null.
     *
     * @param collection The {@link Collection}.
     * @param filter     The {@link Predicate} being used as the filter.
     * @param <T>        The type of elements in the specified {@link Collection}.
     *
     * @return True if any of the specified {@link Collection Collection's}
     * contents matches the specified {@link Predicate}, false
     * otherwise.
     */
    public static <T> boolean containsAnyMatching(Collection<T> collection, Predicate<T> filter) {
        return collection != null && filter != null && collection.stream().anyMatch(filter);
    }
    
    /**
     * Checks to see if the specified array contains <i>ALL</i> of the specified
     * elements. Returns null if either of the specified arrays is null.
     *
     * @param array    The array.
     * @param elements The elements.
     * @param <T>      The type of elements in both arrays.
     *
     * @return True if the specified array contains <i>ALL</i> of the specified
     * elements, false otherwise.
     */
    @SafeVarargs
    public static <T> boolean containsAll(T[] array, T... elements) {
        return array != null && elements != null && java.util.Arrays.asList(array).containsAll(java.util.Arrays.asList(elements));
    }
    
    /**
     * Checks to see if the specified {@link Collection} contains <i>ALL</i> of
     * the specified elements. Returns false if either the {@link Collection} or
     * array is null.
     *
     * @param collection The {@link Collection}.
     * @param elements   The elements.
     * @param <T>        The type of elements in both the {@link Collection} and array.
     *
     * @return True if the specified {@link Collection} contains <i>ALL</i> of
     * the specified elements, false otherwise.
     *
     * @deprecated Because this method is inefficient - O(n^2)
     */
    @SafeVarargs
    public static <T> boolean containsAll(Collection<T> collection, T... elements) {
        return collection != null && elements != null && collection.containsAll(java.util.Arrays.asList(elements));
    }
    
    /**
     * Checks to see if the specified array of strings contains the specified
     * string, ignoring case.
     *
     * @param string The string.
     * @param array  The array of strings.
     *
     * @return True if the specified array of strings contains the specified
     * string (ignoring case), false otherwise.
     */
    public static boolean containsString(String string, String... array) {
        if (string != null && array != null)
            return Stream.of(array).anyMatch(p -> p != null && p.equalsIgnoreCase(string));
        return false;
    }
    
    /**
     * Returns whether the specified String is contained within any String that
     * is contained within the specified ArrayList of Strings.
     * <p>
     * This method is not case-sensitive.
     *
     * @param string The String that is being checked.
     * @param arr    The ArrayList that is being checked.
     *
     * @return Whether the specified String is contained within any String that
     * is contained within the specified ArrayList of Strings.
     */
    public static boolean containsStringPart(String string, String... arr) {
        if (string == null || arr == null)
            return false;
        string = string.toLowerCase();
        final String str = string;
        return Stream.of(arr).anyMatch(p -> p != null && p.toLowerCase().contains(str));
    }
    
    /**
     * Returns true if any of the String elements contained in the specified
     * array of Strings contains any of the Strings in the specified array of
     * Strings, false otherwise.
     * <p>
     * This method is not case-sensitive.
     *
     * @param strings The array being tested.
     * @param arr     The array of Strings to be searched for.
     *
     * @return True if any of the String elements contained in the specified
     * array of Strings contains any of the Strings in the specified
     * array of Strings, false otherwise.
     *
     * @deprecated Because this method is inefficient - O(n^n)
     */
    public static boolean containsStringPart(String[] strings, String... arr) {
        return strings != null && arr != null && java.util.stream.Stream.of(strings).anyMatch(p -> p != null
                                                                                                   && java.util.stream.Stream.of(arr).anyMatch(p2 -> p2 != null && p.toLowerCase().contains(p2)));
    }
    
    /**
     * Returns true if any of the String elements contained in the specified
     * ArrayList of Strings contains any of the Strings in the specified varargs
     * of Strings, false otherwise.
     * <p>
     * This method is not case-sensitive.
     *
     * @param strings The ArrayList being tested.
     * @param arr     The array of Strings to be searched for.
     *
     * @return True if any of the String elements contained in the specified
     * ArrayList of Strings contains any of the Strings in the specified
     * varargs of Strings, false otherwise.
     *
     * @deprecated Because this method is inefficient - O(n^n)
     */
    public static boolean containsStringPart(ArrayList<String> strings, String... arr) {
        return strings != null && arr != null && strings.stream().anyMatch(p -> p != null
                                                                                && java.util.stream.Stream.of(arr).anyMatch(p2 -> p2 != null && p.toLowerCase().contains(p2)));
    }
    
    /**
     * Returns true if the specified name is contained in the specified array of
     * Enums.
     *
     * @param value The value that is being tested.
     * @param arr   The array that is being searched through.
     *
     * @return true if the specified name is contained in the specified array of
     * Enums.
     */
    public static boolean containsEnum(String value, Enum[] arr) {
        return value != null && arr != null
               && java.util.stream.Stream.of(arr).anyMatch(p -> p != null && p.name().equals(value));
    }
    
    /**
     * <p>TODO: The purge method should remove all elements from the specified collection that are not of the specified class type.</p>
     * <p>Any removed elements should be added to a List, which is then returned upon method completion.</p>
     */
    // TO-DOC
    public static <T> List<Object> purge(@NotNull Collection<Object> collection, @NotNull Class<T> clazz) {
        throw SLExceptions.nyi();
    }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>{@link SLArrays}<b>.</b>{@link #containsType(Collection, Class, boolean, boolean, boolean) containsType}<b>(</b>collection<b>,</b> clazz<b>,</b> <u>false</u><b>,</b> <u>false</u><b>,</b> <u>true</u><b>)</b></code></i></blockquote>
     *
     * @param collection The {@link Collection} being iterated.
     * @param clazz      The {@link Class type} of {@link Object} to scan for when iterating the specified {@link Collection}.
     *
     * @return True if the specified {@link Collection} contains elements of the specified {@link Class type} matching conditions defined by the additional parameters, false if it does not.
     */
    // TO-EXPAND
    public static boolean containsTypeAll(@NotNull Collection<?> collection, @NotNull Class<?> clazz) {
        return containsType(collection, clazz, false, false, true);
    }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>{@link SLArrays}<b>.</b>{@link #containsType(Collection, Class, boolean, boolean, boolean) containsType}<b>(</b>collection<b>,</b> clazz<b>,</b> <u>false</u><b>,</b> <u>false</u><b>,</b> <u>false</u><b>)</b></code></i></blockquote>
     *
     * @param collection The {@link Collection} being iterated.
     * @param clazz      The {@link Class type} of {@link Object} to scan for when iterating the specified {@link Collection}.
     *
     * @return True if the specified {@link Collection} contains elements of the specified {@link Class type} matching conditions defined by the additional parameters, false if it does not.
     */
    // TO-EXPAND
    public static boolean containsTypeAny(@NotNull Collection<?> collection, @NotNull Class<?> clazz) {
        return containsType(collection, clazz, false, false, false);
    }
    
    /**
     * <p>Checks if the specified {@link Collection} instance contains elements of the specified {@link Class type}.</p>
     * <p>Additional parameters define how the array is iterated and which conditions must be met.</p>
     *
     * @param collection        The {@link Collection} being iterated.
     * @param clazz             The {@link Class type} of {@link Object} to scan for when iterating the specified {@link Collection}.
     * @param allowNullElements True if {@code null} elements are permitted in the specified {@link Collection}, false if they are not.
     * @param allowEmpty        True if the specified {@link Collection} is permitted to be {@link Collection#isEmpty() empty}, false if it is not.
     * @param requireAll        True if <i>all</i> elements contained within the specified {@link Collection} must be of the specified {@link Class type}, false if only <i>one</i> element is required to match the specified {@link Class type}.
     *
     * @return True if the specified {@link Collection} contains elements of the specified {@link Class type} matching conditions defined by the additional parameters, false if it does not.
     */
    public static boolean containsType(@NotNull Collection<?> collection, @NotNull Class<?> clazz, boolean allowNullElements, boolean allowEmpty, boolean requireAll) {
        // If the collection instance is null, throw a NPE.
        // If the collection is empty, return the value of the specified allowEmpty boolean.
        if (SLExceptions.nullCheck(collection, "Collection Parameter").isEmpty())
            return allowEmpty;
        
        // Keeps track of whether a valid element has been found or not.
        boolean found = false;
        
        // Iterate through the collection of objects.
        for (final Object o: collection) {
            // If the element is not null, continue to perform additional checks.
            if (o != null) {
                boolean cast = true;
                // Try to cast the element to the specified class and set 'cast' to equal true if the cast was successful or false if a CC Exception was thrown.
                try { clazz.cast(o); } catch (ClassCastException e) { cast = false; }
                // If the cast was NOT successful AND requireAll was set to true, return false.
                if (!cast && requireAll)
                    return false;
                // If the cast WAS successful, set the 'found' boolean to true.
                if (cast)
                    found = true;
            } else
                // If the element is null and null elements are not permitted, return false. Otherwise, continue iteration.
                if (!allowNullElements)
                    return false;
        }
        
        // If no return value was reached throughout iteration, then the specified collection has passed all checks, and the boolean tracking if a valid element was found is returned.
        return found;
    }
    
    // </editor-fold>
    
    //<editor-fold desc="Copy">
    
    /**
     * Creates and then returns a shallow copy of the specified {@link Collection}.
     * <p>
     * The value returned by this method is <i>always</i> an {@link ArrayList}.
     *
     * @param collection The {@code Collection} being copied.
     * @param <V>        The type of elements in the specified {@code Collection}.
     *
     * @return A shallow copy of the specified {@code Collection}.
     *
     * @throws NullPointerException If the specified {@code Collection} is null.
     */
    public static <V> ArrayList<V> copy(Collection<V> collection) {
        return new ArrayList<>(SLExceptions.nullCheck(collection, "Collection"));
    }
    
    /**
     * Creates and then returns a shallow copy of the specified {@link Map}.
     * <p>
     * The value returned by this method is <i>always</i> a {@link HashMap}.
     *
     * @param map The {@code Map} being copied.
     * @param <V> The type of elements in the specified {@code Collection}.
     *
     * @return A shallow copy of the specified {@code Map}.
     *
     * @throws NullPointerException If the specified {@code Map} is null.
     */
    public static <K, V> Map<K, V> copy(Map<K, V> map) {
        HashMap<K, V> _map = new HashMap<>(SLExceptions.nullCheck(map, "Map").size());
        _map.putAll(map);
        return _map;
    }
    
    //</editor-fold>
    
    // <editor-fold desc="To Array">
    
    /**
     * Converts the specified list of strings into an array.
     * <p>
     * If the list is null, this method returns an empty array of strings.
     *
     * @param list The list to convert.
     *
     * @return An array representing the list.
     */
    public static String[] toArrayString(List<String> list) {
        if (list == null)
            return new String[0];
        else
            return list.toArray(new String[list.size()]);
    }
    
    /**
     * Converts the specified list of ints into an array.
     * <p>
     * If the list is null, this method returns an empty array of ints.
     *
     * @param list The list to convert.
     *
     * @return An array representing the list.
     */
    public static int[] toArrayInt(List<Integer> list) {
        if (list == null)
            return new int[0];
        else {
            int[] arr = new int[list.size()];
            for (int i = 0; i < list.size(); i++)
                arr[i] = list.get(i);
            return arr;
        }
    }
    
    /**
     * Converts the specified list of doubles into an array.
     * <p>
     * If the list is null, this method returns an empty array of doubles.
     *
     * @param list The list to convert.
     *
     * @return An array representing the list.
     */
    public static double[] toArrayDouble(List<Double> list) {
        if (list == null)
            return new double[0];
        else {
            final double[] arr = new double[list.size()];
            for (int i = 0; i < list.size(); i++)
                arr[i] = list.get(i);
            return arr;
        }
    }
    
    /**
     * Converts the specified list of longs into an array.
     * <p>
     * If the list is null, this method returns an empty array of longs.
     *
     * @param list The list to convert.
     *
     * @return An array representing the list.
     */
    public static long[] toArrayLong(List<Long> list) {
        if (list == null)
            return new long[0];
        else {
            final long[] arr = new long[list.size()];
            for (int i = 0; i < list.size(); i++)
                arr[i] = list.get(i);
            return arr;
        }
    }
    
    /**
     * Converts the specified list of booleans into an array.
     * <p>
     * If the list is null, this method returns an empty array of booleans.
     *
     * @param list The list to convert.
     *
     * @return An array representing the list.
     */
    public static boolean[] toArrayBoolean(List<Boolean> list) {
        if (list == null)
            return new boolean[0];
        final boolean[] arr = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++)
            arr[i] = list.get(i);
        return arr;
    }
    
    /**
     * Converts the specified array of wrapper objects to an array of their
     * primitive form.
     *
     * @param arr The array of wrapper objects.
     *
     * @return The converted array.
     */
    public static int[] toArray(Integer[] arr) {
        if (arr == null)
            return null;
        final int[] primArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Integer value = arr[i];
            if (value != null)
                primArr[i] = value;
            else
                primArr[i] = -1;
        }
        return primArr;
    }
    
    /**
     * Converts the specified array of wrapper objects to an array of their
     * primitive form.
     *
     * @param arr The array of wrapper objects.
     *
     * @return The converted array.
     */
    public static long[] toArray(Long[] arr) {
        if (arr == null)
            return null;
        final long[] primArr = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Long value = arr[i];
            if (value != null)
                primArr[i] = value;
            else
                primArr[i] = -1;
        }
        return primArr;
    }
    
    /**
     * Converts the specified array of wrapper objects to an array of their
     * primitive form.
     *
     * @param arr The array of wrapper objects.
     *
     * @return The converted array.
     */
    public static double[] toArray(Double[] arr) {
        if (arr == null)
            return null;
        final double[] primArr = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Double value = arr[i];
            if (value != null)
                primArr[i] = value;
            else
                primArr[i] = -1;
        }
        return primArr;
    }
    
    /**
     * Converts the specified array of wrapper objects to an array of their
     * primitive form.
     *
     * @param arr The array of wrapper objects.
     *
     * @return The converted array.
     */
    public static boolean[] toArray(Boolean[] arr) {
        if (arr == null)
            return null;
        final boolean[] primArr = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Boolean value = arr[i];
            if (value != null)
                primArr[i] = value;
            else
                primArr[i] = false;
        }
        return primArr;
    }
    
    /**
     * Converts the specified array of wrapper objects to an array of their
     * primitive form.
     *
     * @param arr The array of wrapper objects.
     *
     * @return The converted array.
     */
    public static char[] toArray(Character[] arr) {
        if (arr == null)
            return null;
        final char[] primArr = new char[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Character value = arr[i];
            if (value != null)
                primArr[i] = value;
            // else
            // primArr[i] = '';
        }
        return primArr;
    }
    
    /**
     * Converts the specified array of primitive objects to an array of their
     * wrapper form.
     *
     * @param primArr The array of primitive objects.
     *
     * @return The converted array.
     */
    public static Integer[] toArray(int[] primArr) {
        if (primArr == null)
            return null;
        final Integer[] arr = new Integer[primArr.length];
        for (int i = 0; i < primArr.length; i++)
            arr[i] = primArr[i];
        return arr;
    }
    
    /**
     * Converts the specified array of primitive objects to an array of their
     * wrapper form.
     *
     * @param primArr The array of primitive objects.
     *
     * @return The converted array.
     */
    public static Long[] toArray(long[] primArr) {
        if (primArr == null)
            return null;
        final Long[] arr = new Long[primArr.length];
        for (int i = 0; i < primArr.length; i++)
            arr[i] = primArr[i];
        return arr;
    }
    
    /**
     * Converts the specified array of primitive objects to an array of their
     * wrapper form.
     *
     * @param primArr The array of primitive objects.
     *
     * @return The converted array.
     */
    public static Double[] toArray(double[] primArr) {
        if (primArr == null)
            return null;
        final Double[] arr = new Double[primArr.length];
        for (int i = 0; i < primArr.length; i++)
            arr[i] = primArr[i];
        return arr;
    }
    
    /**
     * Converts the specified array of primitive objects to an array of their
     * wrapper form.
     *
     * @param primArr The array of primitive objects.
     *
     * @return The converted array.
     */
    public static Character[] toArray(char[] primArr) {
        if (primArr == null)
            return null;
        final Character[] arr = new Character[primArr.length];
        for (int i = 0; i < primArr.length; i++)
            arr[i] = primArr[i];
        return arr;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="To String">
    
    /**
     * Returns a String representation of the {@link Class#getSimpleName()
     * simple class name} of each element of the array, using the default
     * toString method of {@link List}.
     *
     * @param arr The {@link List}.
     *
     * @return A String representation of the {@link Class#getSimpleName()
     * simple class name} of each element of the array, using the
     * default toString method of {@link List}.
     */
    public static String toString(Object... arr) {
        return toString(java.util.Arrays.asList(arr));
    }
    
    /**
     * Returns a String representation of the {@link Class#getSimpleName()
     * simple class name} of each element of the list, using the default
     * toString method of {@link List}.
     *
     * @param list The {@link List}.
     *
     * @return A String representation of the {@link Class#getSimpleName()
     * simple class name} of each element of the list, using the default
     * toString method of {@link List}.
     */
    public static <T> String toString(List<T> list) {
        Iterator<T> it = list.iterator();
        if (!it.hasNext())
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (; ; ) {
            T e = it.next();
            if (e == list)
                stringBuilder.append("this Collection");
            else
                stringBuilder.append(e);
            if (!it.hasNext())
                return stringBuilder.toString();
            stringBuilder.append(',').append(' ');
        }
    }
    
    /**
     * Returns a String representation of the {@link Class#getSimpleName()
     * simple class name} of each element of the array, using the default
     * toString method of {@link List}.
     *
     * @param arr The {@link List}.
     *
     * @return A String representation of the {@link Class#getSimpleName()
     * simple class name} of each element of the array, using the
     * default toString method of {@link List}.
     */
    public static String toClassString(Object... arr) {
        return toClassString(java.util.Arrays.asList(arr));
    }
    
    /**
     * Returns a String representation of the {@link Class#getSimpleName()
     * simple class name} of each element of the list, using the default
     * toString method of {@link List}.
     *
     * @param list The {@link List}.
     *
     * @return A String representation of the {@link Class#getSimpleName()
     * simple class name} of each element of the list, using the default
     * toString method of {@link List}.
     */
    public static String toClassString(List list) {
        if (list != null) {
            ArrayList<String> stringArr = new ArrayList<>(list.size());
            for (Object obj: list)
                stringArr.add(obj == null ? "null" : SLTools.getSimpleName(obj.getClass()));
            return stringArr.toString();
        }
        return null;
    }
    
    // </editor-fold>
    
    //
    
    //<editor-fold desc="Classes">
    
    public static class PlaceElement<T> {
        
        private final int index;
        private final int maxIndex;
        private final T element;
        
        public PlaceElement(int index, T element) {
            this(index, -1, element);
        }
        
        public PlaceElement(int index, int maxIndex, T element) {
            this.index = index;
            this.maxIndex = maxIndex;
            this.element = element;
        }
    }
    
    public interface ElementFilter<T> {
        boolean accept(T element);
    }
    
    public interface ElementCopier<P, R> {
        R copy(int index, P p);
    }
    
    //
    
    public static class Advanced {
        private Advanced() {
        } // No instance
        
        /**
         * Moves the elements in the specified array over so that null elements
         * moved to the back of the array, and non-null elements are moved to
         * the front.
         * <p>
         * The stop points "split" the array into sub-arrays, and each null
         * element in each sub-array will be moved to the back of their
         * sub-array.
         * <p>
         * So for example, if you specify {@code 3} as a stop point, no elements
         * from 0-3 will be moved greater than 3, and no elements from
         * 4-infinity will be moved less than 4.
         * <p>
         * Leave the stop points array blank to shift all elements in the array.
         *
         * @param arr        The array being shifted.
         * @param stopPoints The array of stop points.
         * @param <T>        The type of elements inside the array.
         */
        public static <T> void shift(T[] arr, int... stopPoints) {
            if (arr != null && stopPoints != null && stopPoints.length > 0) {
                java.util.Arrays.sort(stopPoints);
                stopPoints = concat(concat(new int[]{0}, stopPoints), new int[]{arr.length});
                for (int i = stopPoints.length - 1; i > 0; i--) {
                    int currentStopPoint = stopPoints[i];
                    int nextStopPoint = stopPoints[i - 1];
                    for (int j = currentStopPoint; j >= (i > 0 ? nextStopPoint : 0); j--) {
                        int furthestLeftNull;
                        int furthestLeftNonNull;
                        while ((furthestLeftNull = getFurthestLeft(nextStopPoint, j, true,
                                                                   arr
                                                                  )) < (furthestLeftNonNull = getFurthestLeft(nextStopPoint, j, false, arr))) {
                            arr[furthestLeftNull] = arr[furthestLeftNonNull];
                            arr[furthestLeftNonNull] = null;
                        }
                    }
                }
            }
        }
        
        /**
         * Shifts the elements defined by the specified min and max indexes in
         * the specified array to the left the specified number of spaces.
         *
         * @param arr      The array.
         * @param minIndex The minimum (inclusive) movedToIndex.
         * @param maxIndex The maximum (exclusive) movedToIndex. Specify -1 for no max movedToIndex
         *                 (the end of the array).
         * @param spaces   The number of spaces the elements are being shifted.
         * @param safe     True if the operation should be done only if the operation
         *                 can be safely completed without overwriting any non-null
         *                 elements, false otherwise.
         * @param <T>      The type of elements inside the array.
         *
         * @return True if the shift was successful, false otherwise.
         */
        public static <T> boolean shiftExact(T[] arr, int minIndex, int maxIndex, int spaces, boolean safe) {
            maxIndex = maxIndex == -1 ? arr.length : maxIndex;
            if (safe)
                for (int i = minIndex - spaces; i < minIndex; i++) {
                    if (i < 0 || arr[i] != null)
                        return false;
                }
            for (int i = minIndex; i < maxIndex; i++) {
                arr[i - spaces] = arr[i];
                arr[i] = null;
            }
            return true;
        }
        
        /**
         * Makes space in the specified array at the specified movedToIndex of the
         * specified size. The space is made by moving existing elements to the
         * right.
         *
         * @param arr    The array.
         * @param index  The movedToIndex the space is being added at.
         * @param spaces The number of indexes in which the space should take up.
         * @param safe   True if the operation should be done only if the operation
         *               can be safely completed without overwriting any non-null
         *               elements, false otherwise.
         * @param <T>    The type of elements inside the array.
         *
         * @return True if the space was made, false otherwise.
         */
        public static <T> boolean spread(T[] arr, int index, int spaces, boolean safe) {
            if (safe)
                for (int i = arr.length - 1; i >= arr.length - spaces; i--)
                    if (arr[i] != null)
                        return false;
            for (int i = arr.length - 1; i >= index; i--)
                if (i - spaces >= 0)
                    arr[i] = arr[i - spaces];
            for (int i = 0; i < spaces; i++) {
                if (i + index < arr.length)
                    arr[i + index] = null;
            }
            return true;
        }
        
        /**
         * Inserts the specified {@link PlaceElement PlaceElements} inside the
         * specified array.
         * <p>
         * Each PlaceElement is placed into the specified array under the
         * following conditions:
         * <ol>
         * <li>The movedToIndex is between the calculated minimum and maximum indexes
         * (see below)</li>
         * <li>The movedToIndex is non-null.</li>
         * </ol>
         * The minimum movedToIndex is always set to the movedToIndex specified by the
         * PlaceElement.
         * <p>
         * If the PlaceElement is the last element in the PlaceElement array,
         * then the maximum movedToIndex is set to {@code arr.length - 1}. In all other
         * scenarios, the maximum movedToIndex is set to the movedToIndex specified by the
         * next PlaceElement in PlaceElement array.
         *
         * @param arr      The array.
         * @param elements The {@link PlaceElement PlaceElements}.
         * @param <T>      The type of elements inside the array.
         */
        @SafeVarargs
        public static <T> void place(T[] arr, PlaceElement<T>... elements) {
            for (int i = 0; i < elements.length; i++) {
                PlaceElement<T> currentElement = elements[i];
                int currentIndex = currentElement.index;
                int nextIndex;
                if (currentElement.maxIndex != -1)
                    nextIndex = currentElement.maxIndex;
                else if (i != elements.length - 1)
                    nextIndex = elements[i + 1].index;
                else
                    nextIndex = arr.length - 1;
                int index = getFurthestLeft(currentIndex, nextIndex, true, arr);
                if (index != -1)
                    arr[index] = currentElement.element;
            }
        }
        
        /**
         * Inserts the specified element into the specified array at the
         * specified movedToIndex.
         *
         * @param arr     The array.
         * @param element The element that is being inserted into the array.
         * @param index   The movedToIndex in which the element is being inserted into.
         * @param safe    True if the operation should be done only if the operation
         *                can be safely completed without overwriting any non-null
         *                elements, false otherwise.
         * @param <T>     The type of elements inside the array.
         *
         * @return True if the element was inserted successfully, false
         * otherwise.
         */
        public static <T> boolean insert(T[] arr, T element, int index, boolean safe) {
            if (spread(arr, index, 1, safe)) {
                arr[index] = element;
                return true;
            }
            return false;
        }
        
        /**
         * Moves an element from the first specified array to the second
         * specified array.
         *
         * @param arr1        The first array (the array being moved <i>from</i>).
         * @param arr2        The second array (the array being moved <i>to</i>).
         * @param index1      The movedToIndex of the element being moved from the first array.
         * @param index2      The movedToIndex in which the element from the first array is
         *                    being moved to in the second array.
         * @param stopShiftAt The movedToIndex (max) in which elements should stop being
         *                    shifted in the first array after the element has been
         *                    removed. Specify -1 to remove until the end of the array,
         *                    or 0 to not move at all.
         * @param safe        True if the operation should be done only if the operation
         *                    can be safely completed without overwriting any non-null
         *                    elements, false otherwise.
         * @param <T>         The type of elements inside the array.
         *
         * @return True if the element was successfully moved, false otherwise.
         */
        public static <T> boolean move(T[] arr1, T[] arr2, int index1, int index2, int stopShiftAt, boolean safe) {
            T obj = arr1[index1];
            if (insert(arr2, obj, index2, safe)) {
                arr1[index1] = null;
                if (stopShiftAt == 0 || shiftExact(arr1, index1 + 1, stopShiftAt, 1, safe))
                    return true;
                else
                    arr1[index1] = obj;
            }
            return false;
        }
        
        /**
         * Moves an element from the first specified array to the second
         * specified array.
         *
         * @param arr1        The first array (the array being moved <i>from</i>).
         * @param arr2        The second array (the array being moved <i>to</i>).
         * @param obj         The element being moved from the first array.
         * @param index       The movedToIndex in which the element from the first array is
         *                    being moved to in the second array.
         * @param stopShiftAt The movedToIndex (max) in which elements should stop being
         *                    shifted in the first array after the element has been
         *                    removed. Specify -1 to remove until the end of the array,
         *                    or 0 to not move at all.
         * @param safe        True if the operation should be done only if the operation
         *                    can be safely completed without overwriting any non-null
         *                    elements, false otherwise.
         * @param <T>         The type of elements inside the array.
         *
         * @return True if the element was successfully moved, false otherwise.
         */
        public static <T> boolean move(T[] arr1, T[] arr2, T obj, int index, int stopShiftAt, boolean safe) {
            return move(arr1, arr2, indexOf(obj, arr1), index, stopShiftAt, safe);
        }
        
        /**
         * Copies the specified target array into the specified source array
         * using the specififed {@link ElementCopier}.
         *
         * @param sourceArr The source array.
         * @param targetArr The target array.
         * @param copier    The {@link ElementCopier}.
         * @param <T>       The type of elements inside of the source array.
         * @param <Z>       The type of elements inside of the target array.
         *
         * @return The target array after the elements from the source array
         * have been copied into it. Note that this method does
         * <i>not</i> create a new array.
         */
        public static <T, Z> Z[] copyInto(T[] sourceArr, Z[] targetArr, ElementCopier<T, Z> copier) {
            return copyInto(sourceArr, targetArr, copier, null);
        }
        
        /**
         * Copies the specified target array into the specified source array
         * using the specififed {@link ElementCopier}.
         *
         * @param sourceArr The source array.
         * @param targetArr The target array.
         * @param copier    The {@link ElementCopier}.
         * @param onNull    The {@link Supplier} that is run for null elements. Null
         *                  to keep all null elements as null.
         * @param <T>       The type of elements inside of the source array.
         * @param <Z>       The type of elements inside of the target array.
         *
         * @return The target array after the elements from the source array
         * have been copied into it. Note that this method does
         * <i>not</i> create a new array.
         */
        public static <T, Z> Z[] copyInto(
                T[] sourceArr, Z[] targetArr, ElementCopier<T, Z> copier,
                Supplier<Z> onNull) {
            if (sourceArr == null)
                throw SLExceptions.ex(new NullPointerException("Source array cannot be null"));
            else if (targetArr == null)
                throw SLExceptions.ex(new NullPointerException("Target array cannot be null"));
            else if (copier == null)
                throw SLExceptions.ex(new NullPointerException("ElementCopier cannot be null"));
            else if (sourceArr.length != targetArr.length)
                throw SLExceptions
                        .ex(new IndexOutOfBoundsException("Source array length must equal target array length"));
            for (int i = 0; i < sourceArr.length; i++) {
                if (sourceArr[i] != null)
                    try {
                        targetArr[i] = copier.copy(i, sourceArr[i]);
                    } catch (Exception ignore) {
                    }
                else
                    targetArr[i] = (onNull != null ? onNull.get() : null);
            }
            return targetArr;
        }
    }
    
    //</editor-fold>
}

/*
 * TODO LIST:
 * [S] This class is getting too large. Split it up into separate, logical classes (such as ArrayTools, MapTools, CollectionTools, etc).
 * [S} Create RemovalType enum (FIRST, LAST, ALL, etc).
 */