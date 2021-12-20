package com.taco.tacository.quick;

import com.taco.tacository.Util;
import com.taco.tacository.numbers.Numbers;

public class ConsoleBB
{
    public static final ConsoleBB CONSOLE;
    
    static
    {
        CONSOLE = new ConsoleBB();
    }
    
    private int consoleWidth;
    private String dividerChar;
    
    private ConsoleBB()
    {
        this.restoreDefaults();
    }
    
    public void restoreDefaults()
    {
        this.consoleWidth = 20;
        this.dividerChar = "*";
        
        this.restoreHeaderDefaults();
    }
    
    //
    
    public String dividerChar()
    {
        return dividerChar != null ? dividerChar : "";
    }
    
    public void setDividerChar(String dvChar)
    {
        this.dividerChar = dvChar.substring(0, 1);
    }
    
    //
    
    public String divider()
    {
        return divider(consoleWidth, "-");
    }
    
    public String divider(int width, String dvChar)
    {
        return dvChar.repeat(width);
    }
    
    //<editor-fold desc="--- Header ---">
    
    // --- Header Vars --- //
    
    private String titleEnclosureStringL;
    private String titleEnclosureStringR;
    
    private int titleSpacing;
    private String titleBorderCharL;
    private String titleBorderCharR;
    private int titleBorderCharCount;
    
    private void restoreHeaderDefaults()
    {
        this.titleEnclosureStringL = "<<  ";
        this.titleEnclosureStringR = "  >>";
        
        this.titleBorderCharCount = 1;
        this.titleBorderCharL = "<";
        this.titleBorderCharR = ">";
        
        this.titleSpacing = 4;
    }
    
    // --- Header Logic --- //
    
    public String[] header(Object obj)
    {
        final String dvChar = dividerChar();
        final String text = titleEnclosureStringL + Util.toText(obj) + titleEnclosureStringR;
        
        int divWidth = Math.max((((text.length() / 2) + (titleBorderCharCount + titleSpacing)) * 2), (consoleWidth * 2));
        String div = divider(divWidth, dvChar);
        
        if (Numbers.isOdd(text.length()))
            div += dvChar;
        
        int fTextSpaces = (((divWidth / 2) - titleBorderCharCount) - (text.length() / 2));
        
        String lfText = titleBorderCharL.repeat(titleBorderCharCount) + " ".repeat(fTextSpaces);
        String rfText = " ".repeat(fTextSpaces) + titleBorderCharR.repeat(titleBorderCharCount);
        String fText = lfText + text + rfText;
        
        return new String[]{div, fText, div};
    }
    
    //</editor-fold>
    
    //
    
    // Currently, identical to standard print(...) operation
    // Used to identify messages that should eventually be excluded from output when out of dev mode
    public void dev(String... sequence)
    {
        print(sequence);
    }
    
    public void print(String... sequence)
    {
        print(PrintType.PLAIN, sequence);
    }
    
    public void print(PrintType printType, String... sequence)
    {
        if (printType == null)
            throw new NullPointerException("PrintType param cannot be null — use PrintType.PLAIN for standard text printout if desired.");
        
        switch (printType)
        {
            case PLAIN -> {
                for (String line: sequence)
                    System.out.println(line);
            }
            case HEADER -> {
                if (sequence.length == 0)
                    throw new UnsupportedOperationException("No header content provided.");
                if (sequence.length > 1)
                    throw new UnsupportedOperationException("Only one header can be printed at a time.");
                
                print(PrintType.PLAIN, header(sequence[0]));
            }
            case WARNING -> {
                if (sequence.length == 0)
                    throw new UnsupportedOperationException("No warning content provided.");
                if (sequence.length > 1)
                    throw new UnsupportedOperationException("Only one warning can be printed at a time.");
                
                print(PrintType.PLAIN, "WARNING: " + sequence[0]);
            }
        }
    }
    
    public enum PrintType
    {
        PLAIN, HEADER, WARNING
    }
}