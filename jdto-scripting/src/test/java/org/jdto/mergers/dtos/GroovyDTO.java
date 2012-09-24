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
package org.jdto.mergers.dtos;

import java.io.Serializable;
import org.jdto.annotation.Source;
import org.jdto.annotation.SourceNames;
import org.jdto.annotation.Sources;
import org.jdto.mergers.GroovyMerger;
import org.jdto.mergers.MultiGroovyMerger;

/**
 * Integration object to test the groovy mergers.
 * @author Juan Alberto López Cavallotti
 */
@SourceNames({"bean1", "bean2"})
public class GroovyDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Source(value="myString", merger=GroovyMerger.class, 
            mergerParam="sourceValue == null ? 'is null' : 'is not null'")
    private String singleSource;
    
    @Sources(value = {@Source("myString"), @Source(value = "myString", sourceBean="bean2")},
            merger = MultiGroovyMerger.class, 
            mergerParam = "sourceValues[0] + ' and ' + sourceValues[1]")
    private String multipleSource;

    public String getMultipleSource() {
        return multipleSource;
    }

    public void setMultipleSource(String multipleSource) {
        this.multipleSource = multipleSource;
    }

    public String getSingleSource() {
        return singleSource;
    }

    public void setSingleSource(String singleSource) {
        this.singleSource = singleSource;
    }
    
}
