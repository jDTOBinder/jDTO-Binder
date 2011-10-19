package com.juancavallotti.jdto.entities;

import java.util.List;
import java.util.Set;

/**
 *
 * @author juan
 */
public class Bill {
    private List<Double> itemPrices;
    private Set<BillItem> items;
    
    public List<Double> getItemPrices() {
        return itemPrices;
    }

    public void setItemPrices(List<Double> itemPrices) {
        this.itemPrices = itemPrices;
    }

    public Set<BillItem> getItems() {
        return items;
    }

    public void setItems(Set<BillItem> items) {
        this.items = items;
    }
    
}
