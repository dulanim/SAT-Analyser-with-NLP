/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project.traceability.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author K.Kamalan
 */
public class DefaultWords {
    public static List<String> defaultWords = new ArrayList<String>();
    public static List<String> getDefaultWords(){
        defaultWords.add("is");
        defaultWords.add("has");
        defaultWords.add("details");
        defaultWords.add("detail");
        
        return defaultWords;
    }
    
}
