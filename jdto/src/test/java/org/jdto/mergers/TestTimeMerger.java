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

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Acceptance test for the time merger.
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestTimeMerger {
    
    private static TimeBetweenDatesMerger subject;
    private static Calendar refTime;
    
    @BeforeClass
    public static void init() {
        subject = new TimeBetweenDatesMerger();
        refTime = Calendar.getInstance();
    }
    
    @Test
    public void testSeconds() {
        
        int diff = 15;
        
        String[] units = {TimeBetweenDatesMerger.TIME_UNIT_SECONDS};
        Calendar other = (Calendar) refTime.clone();
        other.add(Calendar.SECOND, diff);
        
        Number result = subject.mergeObjects((List)Arrays.asList(refTime, other), units);
        
        assertEquals(diff, result.intValue());
    }
    @Test
    public void testMinutes() {
        
        int diff = 10;
        
        String[] units = {TimeBetweenDatesMerger.TIME_UNIT_MINUTES};
        Calendar other = (Calendar) refTime.clone();
        other.add(Calendar.MINUTE, diff);
        
        Number result = subject.mergeObjects((List)Arrays.asList(refTime, other), units);
        
        assertEquals(diff, result.intValue());
    }
    @Test
    public void testHours() {
        
        int diff = 2;
        
        String[] units = {TimeBetweenDatesMerger.TIME_UNIT_HOURS};
        Calendar other = (Calendar) refTime.clone();
        other.add(Calendar.HOUR, diff);
        
        Number result = subject.mergeObjects((List)Arrays.asList(refTime, other), units);
        
        assertEquals(diff, result.intValue());
    }
    @Test
    public void testDays() {
        
        int diff = 4;
        
        String[] units = {TimeBetweenDatesMerger.TIME_UNIT_DAYS};
        Calendar other = (Calendar) refTime.clone();
        other.add(Calendar.DAY_OF_MONTH, diff);
        
        Number result = subject.mergeObjects((List)Arrays.asList(refTime, other), units);
        
        assertEquals(diff, result.intValue());
    }
    @Test
    public void testWeeks() {
        
        int diff = 3;
        
        String[] units = {TimeBetweenDatesMerger.TIME_UNIT_WEEKS};
        Calendar other = (Calendar) refTime.clone();
        other.add(Calendar.WEEK_OF_YEAR, diff);
        
        Number result = subject.mergeObjects((List)Arrays.asList(refTime, other), units);
        
        assertEquals(diff, result.intValue());
    }
    
    
}
