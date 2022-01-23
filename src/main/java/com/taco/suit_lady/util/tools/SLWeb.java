package com.taco.suit_lady.util.tools;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class SLWeb
{
    public static SLWeb get()
    {
        return TB.web();
    }
    
    SLWeb() { }
    
    public void browse(String rawUrl)
    {
        try {
            Desktop.getDesktop().browse(URI.create(rawUrl));
        } catch (IOException e) {
            throw SLExceptions.ex(e);
        }
    }
    
    public void browse(String page, boolean secure)
    {
        browse(page, "com", secure);
    }
    
    public void browse(String page, String suffix, boolean secure)
    {
        String prefix = "http" + (secure ? "s" : "") + "://www.";
        suffix = "." + suffix;
        String rawURL = prefix + page + suffix;
        browse(rawURL);
    }
}
