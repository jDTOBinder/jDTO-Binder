/*
 * Copyright 2014 Juan Alberto López Cavallotti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jdto.mergers.dtos;

import org.jdto.annotation.Source;
import org.jdto.annotation.Sources;
import org.jdto.mergers.ScriptMerger;

/**
 *
 * @author Juan Alberto López Cavallotti
 */
public class ScriptingDTO {
    
    @Source(value = "myString", merger = ScriptMerger.class, mergerParam = "sourceValue.toLowerCase()")
    private String text;
    
    @Sources(value = {@Source("myString"), @Source("myString")},
            merger = ScriptMerger.class, mergerParam = { "sourceValues[0] + ' and ' + sourceValues[1] ", "groovy" })
    private String compoundText;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCompoundText() {
        return compoundText;
    }

    public void setCompoundText(String compoundText) {
        this.compoundText = compoundText;
    }
    
}
