package com.outhdemo.demo.Models;

import java.util.HashMap;
import java.util.Map;

public class globals {
    private Map<String, String> GlobalAttributes;
    private static volatile globals accessTokenCache;

    private globals(){
        GlobalAttributes = new HashMap<>();
    }
    public static globals getAttributes(){
        if (accessTokenCache == null){
            synchronized (globals.class){
                if (accessTokenCache == null) {
                    accessTokenCache = new globals();
                }
            }

        }
        return accessTokenCache;
    }
    public void addAttribute(String key, String value){
        GlobalAttributes.put(key, value);
    }

    public String getAttribute(String key){
        return GlobalAttributes.get(key);
    }
}
