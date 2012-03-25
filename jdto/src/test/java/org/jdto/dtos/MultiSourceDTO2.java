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

package org.jdto.dtos;

import org.jdto.annotation.Source;
import org.jdto.annotation.SourceNames;
import org.jdto.annotation.Sources;
import org.jdto.mergers.DateFormatMerger;
import org.jdto.mergers.StringFormatMerger;

/**
 * A second multi source dto
 * @author Juan Alberto Lopez Cavallotti
 */
@SourceNames({"bean1", "bean2"})
public class MultiSourceDTO2 {
    
    
    @Source("aString") //using bean1 as default
    private String string1;
    
    @Sources(value={@Source("anInt"), @Source(value = "theDate", sourceBean="bean2", merger=DateFormatMerger.class, mergerParam="dd/MM/yyyy")}, merger=StringFormatMerger.class, mergerParam="%02d %s")
    private String string2;
    
    
    @Source(value = "theCalendar", sourceBean="bean2", merger=DateFormatMerger.class, mergerParam="dd/MM/yyyy")
    private String string3;

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }
    
    
}
