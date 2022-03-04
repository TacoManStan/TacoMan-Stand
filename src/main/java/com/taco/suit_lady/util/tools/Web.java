package com.taco.suit_lady.util.tools;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public final class Web {
    private Web() { } //No Instance
    
    public static void browse(String rawUrl) {
        try {
            Desktop.getDesktop().browse(URI.create(rawUrl));
        } catch (IOException e) {
            throw Exc.ex(e);
        }
    }
    
    public static void browse(String page, boolean secure) {
        browse(page, "com", secure);
    }
    
    public static void browse(String page, String suffix, boolean secure) {
        String prefix = "http" + (secure ? "s" : "") + "://www.";
        suffix = "." + suffix;
        String rawURL = prefix + page + suffix;
        browse(rawURL);
    }
}
