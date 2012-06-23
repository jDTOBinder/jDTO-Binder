/*
 *    Copyright 2012 Juan Alberto López Cavallotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jdto.mergers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * Test the date format merger and the restore process.
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestDateFormat {
    
    private static DateFormatMerger subject;
    
    @BeforeClass
    public static void init() {
        subject = new DateFormatMerger();
    }
    
    @Test
    public void testDateFormatMerger() {
        
        final String format = "yyyy-MM-dd";
        
        Date d = new Date();
        
        String result = subject.mergeObjects(d, new String[] {format});
        
        assertEquals(new SimpleDateFormat(format).format(d), result);
    }
    
    @Test
    public void testUnmergeDate() {
        
        String input = "2012-06-23";
        
        String[] params = {"yyyy-MM-dd"};
        assertTrue(subject.isRestoreSupported(params));
        
        Date result = (Date) subject.restoreObject(input, params);
        Calendar cal = Calendar.getInstance();
        cal.setTime(result);
        
        assertEquals(23, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(05, cal.get(Calendar.MONTH));
        assertEquals(2012, cal.get(Calendar.YEAR));
    }
}
