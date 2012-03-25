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
import org.jdto.mergers.SumMerger;
import org.jdto.mergers.SumProductMerger;

/**
 *
 * @author Juan Alberto Lopez Cavallotti
 */
public class BillDTO {

    @Source(value = "itemPrices", merger = SumMerger.class)
    private double total;
    @Source(value = "items", merger = SumMerger.class, mergerParam = "price")
    private double total2;
    
    @Source(value = "items", merger = SumProductMerger.class, mergerParam = {"price","amount"})
    private double totalTotal;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal2() {
        return total2;
    }

    public void setTotal2(double total2) {
        this.total2 = total2;
    }

    public double getTotalTotal() {
        return totalTotal;
    }

    public void setTotalTotal(double totalTotal) {
        this.totalTotal = totalTotal;
    }
    
}
