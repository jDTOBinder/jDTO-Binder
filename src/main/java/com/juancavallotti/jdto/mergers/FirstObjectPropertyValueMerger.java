package com.juancavallotti.jdto.mergers;

import com.juancavallotti.jdto.MultiPropertyValueMerger;
import java.util.List;

/**
 * This is the default property value merger. <br />
 * It returns the first non null value of the list.
 * 
 * @author juancavallotti
 */
public class FirstObjectPropertyValueMerger implements MultiPropertyValueMerger<Object> {

    /**
     * Return the first non-null value of the list or null if none found.
     * @param values
     * @return 
     */
    public Object mergeObjects(List<Object> values, String extraParam) {
        for (Object object : values) {
            if (object != null) {
                return object;
            }
        }
        return null;
    }
    
}
