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
import org.jdto.annotation.Sources;
import org.jdto.mergers.StringFormatMerger;

/**
 *
 * @author juancavallotti
 */
public class FormatDTO {
    
    @Source(value="aDouble", merger=StringFormatMerger.class, mergerParam="$ %.2f")
    private String price;
    
    @Sources(value={@Source("aDouble"), @Source("anInt")}, 
            merger=StringFormatMerger.class, mergerParam="%.2f %08d")
    private String compound;

    public String getCompound() {
        return compound;
    }

    public void setCompound(String compound) {
        this.compound = compound;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
}
