package com.juancavallotti.jdto.dtos;

import com.juancavallotti.jdto.annotation.Source;
import com.juancavallotti.jdto.mergers.SumMerger;
import com.juancavallotti.jdto.mergers.SumProductMerger;

/**
 *
 * @author juan
 */
public class BillDTO {

    @Source(value = "itemPrices", merger = SumMerger.class)
    private double total;
    @Source(value = "items", merger = SumMerger.class, mergerParam = "price")
    private double total2;
    
    @Source(value = "items", merger = SumProductMerger.class, mergerParam = "price,amount")
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
