package com.taco.suit_lady.util;

import com.taco.util.obj_traits.common.Nameable;

import java.awt.*;
import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

/**
 * Contains methods related to manipulating strings.
 */
public class StringTools
{
    public static StringTools get()
    {
        return TB.strings();
    }
    
    StringTools() { }
    
    /**
     * A constant representing one thousand (1,000).
     */
    private final int K = 1000;
    /**
     * A constant representing one million (1,000,000).
     */
    private final int M = 1000000;
    /**
     * A constant representing one billion (1,000,000,000).
     */
    private final int B = 1000000000;
    
    //
    
    /**
     * Checks if the any of the strings in the specified array of strings are contained within the specified string.
     *
     * @param string              The string.
     * @param strings             The array of second strings.
     * @param stringContainsArray True if the specified string must contain an element in the specified array, false if an element in the specified array must contain the specified string.
     * @return True if the any of the strings in the specified array of strings are contained within the specified string, false otherwise.
     */
    public boolean containsIgnoreCase(String string, boolean stringContainsArray, String... strings)
    {
        if (string != null && strings != null)
        {
            for (String s: strings)
                if (s != null && !s.isEmpty())
                    if (stringContainsArray ? string.toLowerCase().contains(s.toLowerCase()) : s.toLowerCase().contains(string.toLowerCase()))
                        return true;
        }
        return false;
    }
    
    /**
     * Checks to see if the first specified string is equal to the second specified string. This method is not case-sensitive.
     *
     * @param string1 The first string.
     * @param string2 The second string.
     * @return True if the first specified string is equal to the second specified string. This method is not case-sensitive, false otherwise.
     */
    public boolean equalsIgnoreCase(String string1, String string2)
    {
        return string1 != null && string2 != null && string1.equalsIgnoreCase(string2);
    }
    
    /**
     * Capitalizes the first letter of the specified string.
     *
     * @param string The string.
     * @return The string with the capital first letter. Returns the original string if it has a length of 0.
     */
    public String capitalizeFirst(String string)
    {
        if (string.length() < 1)
            return string;
        return string.replaceFirst(string.substring(0, 1), string.substring(0, 1).toUpperCase());
    }
    
    /**
     * Capitalizes the first letter of each word in the specified string.
     *
     * @param string The string.
     * @return The string with the first letter of each word being capitalized. Returns the original string if it has a length of 0.
     */
    public String capitalizeAllFirst(String string)
    {
        if (string.length() < 1)
            return string;
        String[] words = string.split(" ");
        String concatWord = "";
        for (int i = 0; i < words.length; i++)
            concatWord += capitalizeFirst(words[i]) + (i != words.length - 1 ? " " : "");
        return concatWord;
    }
    
    /**
     * Capitalizes the first letter of the first word of each sentence in the specified string.
     *
     * @param string The string (containing sentences).
     * @return The modified string with the first letter of the first word of each sentence in the specified string being capitalized.
     */
    public String capitalizeSentence(String string)
    {
        if (string != null)
        {
            final char[] chars = string.toCharArray();
            boolean found = false;
            for (int i = 0; i < chars.length; i++)
            {
                if (!found && Character.isLetter(chars[i]))
                {
                    chars[i] = Character.toUpperCase(chars[i]);
                    found = true;
                }
                else if (chars[i] == '.' || chars[i] == '?' || chars[i] == '!' || matches(i, chars, "</li>".toCharArray()))
                    found = false;
            }
            return String.valueOf(chars);
        }
        return null;
    }
    
    private boolean matches(int index, char[] chars1, char... chars2)
    {
        final Character[] reverse = ArrayTools.getReverse(ArrayTools.toArray(chars2));
        for (int i = 0; i < reverse.length; i++)
            if (chars1[index - i] != reverse[i])
                return false;
        return true;
    }
    
    /**
     * Replaces any underscores in the specified string with spaces.
     *
     * @param string The string.
     * @return A string that has its underscores replaced with spaces.
     */
    public String replaceUnderscores(String string)
    {
        return string.replaceAll("_", " ");
    }
    
    /**
     * Replaces any spaces in the specified string with underscores.
     *
     * @param string The string.
     * @return A string that has its spaces replaced with underscores.
     */
    public String makeUnderscores(String string)
    {
        return string.replaceAll(" ", "_");
    }
    
    /**
     * Formats the specified enum into a readable form.
     *
     * @param e The enum.
     * @return The formatted string.
     */
    public String enumToString(Enum e)
    {
        return capitalizeAllFirst(replaceUnderscores(e.name().toLowerCase()));
    }
    
    /**
     * Returns the name of the specified {@link Class} with spaces in between the camel-case words that the class name forms.
     *
     * @param aClass The {@link Class}.
     * @return The name of the specified {@link Class} with spaces in between the camel-case words that the class name forms.
     */
    public String classToString(Class aClass)
    {
        if (aClass != null)
        {
            String simpleName = GeneralTools.get().getSimpleName(aClass);
            ArrayList<Integer> indexes = new ArrayList<>();
            ArrayList<Character> characters = new ArrayList<>(Arrays.asList(ArrayTools.toArray(simpleName.toCharArray())));
            if (characters.size() > 1)
            {
                boolean succeeded = false;
                while (!succeeded)
                {
                    succeeded = true;
                    for (int i = 1; i < characters.size() && succeeded; i++)
                    {
                        if (Character.isUpperCase(characters.get(i)) && characters.get(i - 1) != ' ')
                        {
                            characters.add(i, ' ');
                            succeeded = false;
                        }
                    }
                }
                return String.valueOf(ArrayTools.toArray(characters.toArray(new Character[characters.size()])));
            }
        }
        return null;
    }
    
    /**
     * Gets the length of the specified string in pixels.
     *
     * @param g The graphics used to draw the string.
     * @param s The string.
     * @return The length.
     */
    public int getStringPixelLength(Graphics g, String s)
    {
        return g.getFontMetrics().stringWidth(s);
    }
    
    /**
     * Inserts commas (US LOCALE) into the specified integer.
     *
     * @param number The integer to insert commas into.
     * @return A string representing the specified integer with commas inserted.
     */
    public String commas(int number) { return commas((long) number); }
    
    /**
     * Inserts commas (US LOCALE) into the specified double.
     *
     * @param number The double to insert commas into.
     * @return A string representing the specified double with commas inserted.
     */
    public String commas(double number) { return commas((long) number); }
    
    /**
     * Inserts commas (US LOCALE) into the specified long.
     *
     * @param number The long to insert commas into.
     * @return A string representing the specified long with commas inserted.
     */
    public String commas(long number)
    {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }
    
    /**
     * Formats a number to be in KMB format.
     *
     * @param number    The number.
     * @param upperCase True to display the string in upper case, false for lower case.
     * @return A KMB formatted string representing the number.
     * @see #kmb(long, boolean)
     */
    public String kmb(int number, boolean upperCase) { return kmb((long) number, upperCase); }
    
    /**
     * Formats a number to be in KMB format.
     *
     * @param number    The number.
     * @param upperCase True to display the string in upper case, false for lower case.
     * @return A KMB formatted string representing the number.
     * @see #kmb(long, boolean)
     */
    public String kmb(double number, boolean upperCase) { return kmb((long) number, upperCase); }
    
    /**
     * Formats a number to be in KMB format.
     * <p>
     * KMB format is as follows: <ul> <li>Any number between 0 and 999 is represented as such.</li> <li>Any number between 1000 and 999999 is represented with a 'k' replacing the last trailing digit of the
     * number. e.g.
     * 52250 would be represented as 52.25k.</li> <li>Any number between 1000000 and 999999999 is represented with a 'm' replacing the last four trailing digits of the number. e.g. 1220000 would be
     * represented
     * as
     * 1.22m.</li> <li>Any number above 1000000000 is represented with a 'b' replacing the last seven trailing digits of the number. e.e. 1220000000 would be represented as 1.22b</li> </ul>
     * <p>
     * All negative numbers are treated the same as positive numbers.
     *
     * @param number    The number.
     * @param upperCase True to display the string in upper case, false for lower case.
     * @return A KMB formatted string representing the number.
     */
    public String kmb(long number, boolean upperCase)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        df.setMinimumFractionDigits(2);
        if (Math.abs(number) < K)
            return "" + number;
        else if (Math.abs(number) < M)
        {
            double num = (double) number / (double) K;
            return "" + df.format(num) + (upperCase ? "K" : "k");
        }
        else if (Math.abs(number) < B)
        {
            double num = (double) number / (double) M;
            return "" + df.format(num) + (upperCase ? "M" : "m");
        }
        double num = (double) number / (double) B;
        return "" + df.format(num) + (upperCase ? "B" : "b");
    }
    
    /**
     * Formats a number to be in a readable time format. The number inputted should be in milliseconds.
     * <p>
     * The format returned is DD:HH:MM:SS. Milliseconds are truncated and not included in the formatted display.
     * <p>
     * All negative numbers are treated the same as positive numbers.
     *
     * @param milliseconds The time being formatted, in milliseconds.
     * @return A KMB formatted string representing the number.
     */
    public String timeFormat(long milliseconds)
    {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        long days = hours / 24;
        String formatted = "";
        String dayString = "" + days;
        String hourString = "" + hours;
        if (hourString.length() == 1)
            hourString = "0" + hourString;
        formatted += hourString + ":";
        String minuteString = "" + minutes;
        if (minuteString.length() == 1)
            minuteString = "0" + minuteString;
        formatted += minuteString + ":";
        String secondString = "" + seconds;
        if (secondString.length() == 1)
            secondString = "0" + secondString;
        formatted += secondString;
        return formatted;
    }
    
    /**
     * Formats the specified amount of time in milliseconds to a readable format.
     *
     * @param ms The amount of time in milliseconds.
     * @return A formatted, readable copy of the time specified.
     */
    public String msToString(long ms) { return msToString(ms, false); }
    
    /**
     * Formats the specified amount of time in milliseconds to a readable format.
     *
     * @param ms   The amount of time in milliseconds.
     * @param caps True if the returned String should be printed in uppercase, false if it should be printed in lowercase.
     * @return A formatted, readable copy of the time specified.
     */
    public String msToString(long ms, boolean caps)
    {
        String msString = "";
        long seconds = ms / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        long days = hours / 24;
        hours %= 24;
        if (days >= 1)
            msString += days + (days > 1 ? " DAYS, " : " DAY, ");
        if (hours >= 1)
            msString += hours + (hours > 1 ? " HOURS, " : " HOUR, ");
        if (minutes >= 1)
            msString += minutes + (minutes > 1 ? " MINUTES, " : " MINUTE, ");
        msString += seconds + (seconds > 1 || seconds == 0 ? " SECONDS" : " SECOND");
        return caps ? msString : msString.toLowerCase();
    }
    
    /**
     * Inserts non-breaking spaces in place of breaking spaces in the specified RSN.
     *
     * @param rsn The RSN to fix.
     * @return The fixed RSN.
     */
    public String fix(String rsn)
    {
        return rsn != null ? rsn.replaceAll("" + (char) 160, "" + (char) 32) : null;
    }
    
    /**
     * Replaces all instances of all values in the specified {@code toReplaceValues array} in the specified {code host String} with the specified {@code replaceWith String}.
     * //TODO - Provide example.
     *
     * @param host            The host String that is having its values replaced.
     * @param replaceWith     The value that is to be the replacing value.
     * @param useRegex        True if the replacement search should be done using regular expressions, false otherwise.
     * @param toReplaceValues The array of Strings representing the values that are to be replaced with the specified {code replaceWith String}.
     * @return The specified {@code host String} with the specified values replaced.
     */
    public String replace(String host, String replaceWith, boolean useRegex, String... toReplaceValues)
    {
        ExceptionTools.nullCheck(host, "Host string cannot be null.");
        ExceptionTools.nullCheck(replaceWith, "To-Replace string cannot be null.");
        ExceptionTools.nullCheck(toReplaceValues, "Replace with array cannot be null.");
        for (String toReplace: toReplaceValues)
            if (toReplace != null)
                if (useRegex)
                    host = host.replaceAll(toReplace, replaceWith);
                else
                    host = host.replace(toReplace, replaceWith);
        return host;
    }
    
    /**
     * Finds how many times the specified String is contained within the host String.
     * <p>
     * This method is case sensitive.
     *
     * @param host The host String.
     * @param str  The String being counted.
     * @return How many times the specified String is contained within the host String.
     */
    public int getCount(String host, String str)
    {
        if (host == null)
            throw new NullPointerException("Host String cannot be null.");
        if (str == null)
            throw new NullPointerException("Searching String cannot be null.");
        int count = 0;
        while (!str.isEmpty() && host.contains(str))
        {
            host = host.replaceFirst(str, "");
            count++;
        }
        return count;
    }
    
    /**
     * Removes all instances of all of the specified Strings from the host String.
     *
     * @param host      The host String.
     * @param toRemoves The Strings being removed.
     * @return The host String with all instances of all of the specified Strings removed.
     */
    public String remove(String host, String... toRemoves)
    {
        if (host == null)
            throw new NullPointerException("Host String cannot be null.");
        String newHost = host;
        for (String toRemove: toRemoves)
            newHost = newHost.replace(toRemove, "");
        return newHost;
    }
    
    /**
     * Removes anything from the specified host char that does not equal one of the specified Strings.
     *
     * @param host    The host String.
     * @param toKeeps The chars being kept.
     * @return The host String with anything that does not equal one of the specified chars removed.
     */
    public String removeOther(String host, char... toKeeps)
    {
        if (host == null)
            throw new NullPointerException("Host String cannot be null.");
        ArrayList<Character> chars = new ArrayList<>();
        for (char c: host.toCharArray())
            if (ArrayTools.contains(c, toKeeps))
                chars.add(0, c);
        char[] charsArr = new char[chars.size()];
        for (int i = 0; i < chars.size(); i++)
            charsArr[i] = chars.get(i);
        return String.copyValueOf(charsArr);
    }
    
    /**
     * Returns the value that lies between the specified pre and post strings contained within the host string.
     *
     * @param host The host String.
     * @param pre  The pre String.
     * @param post The post String.
     * @return The value that lies between the specified pre and post strings contained within the host string.
     */
    public String getIntermediate(String host, String pre, String post)
    {
        host = host.substring(host.indexOf(pre) + pre.length());
        return host.substring(0, host.indexOf(post));
    }
    
    /**
     * Checks to see if the specified string is a number or not.
     *
     * @param val The string.
     * @return True if the specified string is a number, false otherwise.
     */
    public boolean isNumber(String val)
    {
        try
        {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(val);
            return true;
        }
        catch (NumberFormatException ignored) { }
        return false;
    }
    
    /**
     * Checks to see if the first string matchesEither the second string.
     * <p>
     * For the strings to match, the second string must be contained within the first string.
     * <p>
     * This method is not case sensitive.
     *
     * @param string1 The first string being compared.
     * @param string2 The second string being compared.
     * @return True if the first string matchesEither the second string, false otherwise. If either string is null, this method returns false.
     */
    public boolean matches(String string1, String string2)
    {
        return string1 != null && string2 != null && string1.toLowerCase().contains(string2.toLowerCase());
    }
    
    /**
     * Checks to see if the first string matchesEither the second string.
     * <p>
     * For the strings to match, only one of the strings must be contained within the other.
     * <p>
     * This method is not case sensitive.
     *
     * @param string1 The first string being compared.
     * @param string2 The second string being compared.
     * @return True if the first string matchesEither the second string, false otherwise. If either string is null, this method returns false.
     */
    public boolean matchesEither(String string1, String string2)
    {
        return string1 != null && string2 != null && (string1.toLowerCase().contains(string2.toLowerCase()) || string2.toLowerCase().contains(string1.toLowerCase()));
    }
    
    /**
     * Converts the specified string into the default HTML format.
     *
     * @param string        The string.
     * @param stripEditable True if the specified string should be made to be not editable, false if it should be left as-is.
     * @return The specified string converted into the default HTML format.
     */
    public String html(String string, boolean stripEditable)
    {
        if (string != null)
        {
            if (stripEditable)
                string = htmlStripEditable(string);
            return "<body><font face=\"Segoe UI\" size=\"2\">" + string + "</font></body>";
        }
        return null;
    }
    
    /**
     * Returns the specified array of text as an html list.
     * <p>
     * This method can never return null. If the specified array is null, an empty string is returned.
     *
     * @param alwaysList True if the data should always be shown in a list form, false if it should be shown plain if there is only 1 element.
     *                   <br>
     *                   If this value is set to false and there is only one element in the specified array, the returned text will be surrounded in break statements ({@code <br>})
     * @param components The text being converted into an html list.
     * @return The specified array of text as an html list.
     */
    public String htmlList(boolean alwaysList, String... components)
    {
        String text = "";
        if (components != null)
        {
            ArrayList<String> componentsList = new ArrayList<>(ArrayTools.removeNull(components));
            if (componentsList.size() > 1 || alwaysList)
            {
                text += "<ol>";
                for (String component: componentsList)
                    if (component != null)
                        text += "<li>" + capitalizeSentence(component) + "</li>";
                text += "</ol>";
            }
            else
                text += "<br>" + componentsList.get(0) + "<br>";
        }
        return text;
    }
    
    /**
     * Strips all HTML from the specified string that makes the string editable.
     *
     * @param string The string.
     * @return The specified string with all HTML that makes the string editable stripped from it.
     */
    public String htmlStripEditable(String string)
    {
        return string.replace(" contenteditable=\"true\"", "").replace("contenteditable=\"true\"", "");
    }
    
    /**
     * Returns the average length of the words in the specified sentence.
     *
     * @param sentence The sentence.
     * @return The average length of the words in the specified sentence.
     * @see #getAverageWordLength(String[])
     */
    public double getAverageWordLength(String sentence) { return getAverageWordLength(getWords(sentence)); }
    
    /**
     * Returns the average length of the words in the specified array of words.
     *
     * @param words The words.
     * @return The average length of the words in the specified array of words.
     * @see #getAverageWordLength(String)
     */
    public double getAverageWordLength(String[] words)
    {
        int totalCharacters = 0;
        for (String word: words)
            totalCharacters += word.length();
        return (double) totalCharacters / (double) words.length;
    }
    
    /**
     * Returns the specified sentence as an array of words.
     *
     * @param sentence The sentence.
     * @return The specified sentence as an array of words.
     */
    public String[] getWords(String sentence)
    {
        return sentence.split(" ");
    }
    
    /**
     * Makes a sentence out of the specified array of words, and then returns the result.
     *
     * @param words The words.
     * @return A sentence formed by the specified array of words.
     */
    public String sentence(String[] words)
    {
        String sentence = "";
        for (int i = 0; i < words.length; i++)
            sentence += words[i] + (i != words.length - 1 ? " " : "");
        return sentence;
    }
    
    /**
     * Returns the total character count of the strings in the specified array of words.
     *
     * @param words The words.
     * @return The total character count of the strings in the specified array of words.
     */
    public int getCharacterCount(String[] words)
    {
        int count = 0;
        for (String word: words)
            count += word.length();
        return count;
    }
    
    /**
     * Returns the substring of the specified getText based on the location of the specified subtext.
     *
     * @param text          The getText.
     * @param subText       The subtext.
     * @param substringType The type in which the method gets the substring.
     * @return The substring of the specified getText based on the location of the specified subtext.
     */
    public String substring(String text, String subText, SubstringType substringType)
    {
        if (text != null && subText != null && text.contains(subText))
            if (substringType == SubstringType.AFTER)
                return text.substring(text.indexOf(subText) + subText.length());
            else if (substringType == SubstringType.BEFORE)
                return text.substring(0, text.indexOf(subText));
        return null;
    }
    
    //
    
    /**
     * Deletes all elements contained within the specified {@code StringBuilder}.
     * <p>
     * Calling this method is equivalent to calling
     * <code>stringBuilder.{@link StringBuilder#delete(int, int) delete}(0, stringBuilder.{@link StringBuilder#capacity() capacity}().</code>
     *
     * @param stringBuilder The {@code StringBuilder} that is being cleared.
     * @return The recently cleared {@code StringBuilder}.
     * @see StringBuilder#delete(int, int)
     */
    public StringBuilder clearSB(StringBuilder stringBuilder)
    {
        stringBuilder.delete(0, stringBuilder.capacity());
        return stringBuilder;
    }
    
    public String replaceSeparator(String filePath)
    {
        String separator = "/";
        return StringTools.get().replace(filePath, separator, false, "/", "\\");
    }
    
    //
    
    /**
     * <p>Returns an appropriate toString for the specified {@link Object}.</p>
     * <ul>
     *     <li>If the {@code object} param is null, null is returned.</li>
     *     <li>
     *         If the {@code object} param is an {@link Array}, the class name of the array is returned.
     *         <br>
     *         ( See {@link Collections#singletonList(Object)} )
     *     </li>
     *     <li>If the {@code object} param is an instance of {@link Nameable}, the {@link Nameable#getName() name} of the {@code object} param is returned.</li>
     * </ul>
     *
     * @param obj The {@link Object} being toString'd.
     * @return An appropriate toString for the specified {@link Object}.
     */
    public String toString(Object obj)
    {
        if (obj == null)
            return null;
        else if (obj instanceof Array)
            return Collections.singletonList((Array) obj).toString();
        else if (obj instanceof Nameable)
            return ((Nameable) obj).getName();
        else
            return obj.toString();
        //		return GeneralTools.get().getSimpleName(obj.getClass());
    }
    
    //<editor-fold desc="Classes/Enums">
    
    /**
     * The format in which the substring method returns.
     */
    public enum SubstringType
    {
        /**
         * All of the characters <i>before</i> the specified substring are returned.
         */
        BEFORE,
        /**
         * All of the characters <i>after</i> the specified substring are returned.
         */
        AFTER
    }
    
    //</editor-fold>
    
    //
}