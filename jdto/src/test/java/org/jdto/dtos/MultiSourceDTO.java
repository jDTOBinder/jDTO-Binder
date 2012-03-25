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

/**
 *
 * @author juancavallotti
 */
@SourceNames({"bean1", "bean2", "bean3"})
public class MultiSourceDTO {
    
    
    @Source(value="aString", sourceBean="bean1")
    private String source1;
    @Source(value="aString", sourceBean="bean2")
    private String source2;
    @Source(value="aString", sourceBean="bean3")
    private String source3;

    public String getSource1() {
        return source1;
    }

    public void setSource1(String source1) {
        this.source1 = source1;
    }

    public String getSource2() {
        return source2;
    }

    public void setSource2(String source2) {
        this.source2 = source2;
    }

    public String getSource3() {
        return source3;
    }

    public void setSource3(String source3) {
        this.source3 = source3;
    }
    
}
