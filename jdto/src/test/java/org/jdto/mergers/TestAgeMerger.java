/*
 *    Copyright 2011 Juan Alberto López Cavallotti
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

import org.jdto.mergers.AgeMerger;
import java.util.Calendar;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Acceptance test for the age merger.
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestAgeMerger {
    
    @Test
    public void testAgeMergerNull() {
        AgeMerger merger = new AgeMerger();
        
        Number expected = merger.mergeObjects(null, null);
        
        assertEquals("should be 0", 0, expected.intValue());
    }
    
    @Test
    public void testAgeMergerDate() {
        AgeMerger merger = new AgeMerger();
        
        Number expected = 20.0;
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1* expected.intValue());
        
        Number result = merger.mergeObjects(cal.getTime(), null);
        
        assertEquals("should be the same age", expected.intValue(), result.intValue());
    }
    
    @Test
    public void testAgeMergerCalendar() {
        AgeMerger merger = new AgeMerger();
        
        Number expected = 20.0;
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1* expected.intValue());
        
        Number result = merger.mergeObjects(cal, new String[] { AgeMerger.YEARS });
        
        assertEquals("should be the same age", expected.intValue(), result.intValue());
    }
    
    @Test
    public void testAgeMergerWeeks() {
        
        AgeMerger merger = new AgeMerger();
        
        Number expected = 20.0;
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -1* expected.intValue());
        
        Number result = merger.mergeObjects(cal, new String[] { AgeMerger.WEEKS });
        
        assertEquals("should be the same age in weeks", expected.intValue(), result.intValue());
        
    }

    @Test
    public void testAgeMergerDays() {
        
        AgeMerger merger = new AgeMerger();
        
        Number expected = 20.0;
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1* expected.intValue());
        
        Number result = merger.mergeObjects(cal, new String[] { AgeMerger.DAYS });
        
        assertEquals("should be the same age in weeks", expected.intValue(), result.intValue());
        
    }
}
