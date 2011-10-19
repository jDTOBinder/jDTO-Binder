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

package com.juancavallotti.jdto.mergers;

import java.util.HashSet;
import java.util.List;
import com.juancavallotti.jdto.DTOBinder;
import com.juancavallotti.jdto.DTOBinderFactory;
import com.juancavallotti.jdto.dtos.BillDTO;
import com.juancavallotti.jdto.entities.Bill;
import com.juancavallotti.jdto.entities.BillItem;
import java.util.Arrays;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test if the sum merger and the sum product merger are working fine.
 * @author juan
 */
public class TestSumMerger {
    
    private static DTOBinder binder;
    
    @Test
    public void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }
    
    
    @Test
    public void testSumList() {
        int[] myInts = {2,4,6,8};
        
        SumMerger merger = new SumMerger();
        
        double result = merger.mergeObjects(myInts, "");
        
         assertEquals(20.0, result, 0.0001);
    }
    
    
    @Test
    public void testSumBinding() {
        
        Bill bill = new Bill();
        List<Double> prices = Arrays.asList(2.0, 4.0, 6.0, 8.0);
        bill.setItemPrices(prices);
        
        BillDTO dto = binder.bindFromBusinessObject(BillDTO.class, bill);
        
        assertEquals(20, dto.getTotal(), 0.00001);
    }
    
    @Test
    public void testAssociationSum() {
        
        Bill bill = new Bill();
        
        BillItem item1 = new BillItem();
        BillItem item2 = new BillItem();
        
        item1.setPrice(20);
        item2.setPrice(22);
        
        bill.setItems(new HashSet(Arrays.asList(item1, item2)));
        
        BillDTO dto = binder.bindFromBusinessObject(BillDTO.class, bill);
        
        assertEquals(42, dto.getTotal2(), 0.00001);
    }

    @Test
    public void testAssociationSumProduct() {
        
        Bill bill = new Bill();
        
        BillItem item1 = new BillItem();
        BillItem item2 = new BillItem();
        
        item1.setPrice(20);
        item2.setPrice(22);
        item1.setAmount(2);
        item2.setAmount(3);
        
        bill.setItems(new HashSet(Arrays.asList(item1, item2)));
        
        BillDTO dto = binder.bindFromBusinessObject(BillDTO.class, bill);
        
        assertEquals(106, dto.getTotalTotal(), 0.00001);
    }
}
