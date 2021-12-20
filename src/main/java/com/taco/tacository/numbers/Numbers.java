package com.taco.tacository.numbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Numbers
{
    public static boolean isNumber(String input, List<String> args)
    {
        if (args == null)
            throw new NullPointerException("Filter List cannot be null.");
        return isNumber(input, args.toArray(new String[0]));
    }
    
    public static boolean isNumber(String input, String... args)
    {
        final List<String> argsList = Arrays.asList(args);
        
        if (input == null)
            if (com.taco.tacository.numbers.Filter.THROW_NPE.matches(argsList))
                throw new NullPointerException("Input cannot be null.");
            else
                return false;
        
        try
        {
            final double parsedInput = Double.parseDouble(input);
            
            if (Double.isNaN(parsedInput) && !com.taco.tacository.numbers.Filter.ALLOW_NaN.matches(argsList))
                return false;
            else if (Double.isInfinite(parsedInput) && !com.taco.tacository.numbers.Filter.ALLOW_INFINITY.matches(argsList))
                return false;
            else if (decimals(parsedInput) != 0 && com.taco.tacository.numbers.Filter.INT_ONLY.matches(argsList))
                return false;
            
            return true;
        }
        catch (NumberFormatException e)
        {
            if (com.taco.tacository.numbers.Filter.THROW_NFE.matches(argsList))
                throw e;
            return false;
        }
    }
    
    //
    
    public static boolean isInteger(String input)
    {
        return isInteger(input, false, false);
    }
    
    public static boolean isInteger(String input, boolean throwNPE, boolean throwNFE)
    {
        final ArrayList<String> filters = new ArrayList<>();
        
        filters.add(com.taco.tacository.numbers.Filter.INT_ONLY.key());
        if (throwNPE)
            filters.add(com.taco.tacository.numbers.Filter.THROW_NPE.key());
        if (throwNFE)
            filters.add(Filter.THROW_NFE.key());
        
        return isNumber(input, filters);
    }
    
    public static double decimals(double input)
    {
        return input - ((int) input);
    }
    
    //
    
    public static boolean isEven(double input)
    {
        input = (int) input;
        return isInteger("" + (input / 2));
    }
    
    public static boolean isOdd(int input)
    {
        return !isEven(input);
    }
}
