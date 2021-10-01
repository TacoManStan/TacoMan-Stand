package com.taco.suit_lady.util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class WebTools
{
    public static WebTools get()
    {
        return TB.web();
    }
    
    WebTools() { }
    
    public void browse(String rawUrl)
    {
        try {
            Desktop.getDesktop().browse(URI.create(rawUrl));
        } catch (IOException e) {
            throw ExceptionTools.ex(e);
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
