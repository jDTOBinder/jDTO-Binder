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
import com.juancavallotti.jdto.entities.BillItem;
import com.juancavallotti.jdto.impl.CoreBeanModifier;
import java.util.Arrays;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test if the sum merger and the sum product merger are working fine.
 * @author juan
 */
public class TestExpressionMergers {
    
    private static ExpressionMerger merger;
    private static SumExpressionMerger sumMerger;
    
    @BeforeClass
    public static void globalInit() {
        CoreBeanModifier modifier = new CoreBeanModifier();
        merger = new ExpressionMerger();
        merger.setBeanModifier(modifier);
        sumMerger = new SumExpressionMerger();
        sumMerger.setBeanModifier(modifier);
    }
    
    
    @Test
    public void testExpressionMergerValue() {
        
        String[] expression = { "value * 20" };
        
        double result = merger.mergeObjects(10, expression);
        
        assertEquals(200, result, 0.00001);
    }

    @Test
    public void testExpressionMergerBean() {
        
        BillItem billItem = new BillItem();
        billItem.setAmount(5);
        billItem.setPrice(10);
        
        String[] expression = { "amount * price + (amount * price * 0.2)" };
        
        double result = merger.mergeObjects(billItem, expression);
        
        assertEquals(60, result, 0.00001);
    }
    
    
    @Test
    public void testAssociationSumProduct() {
        
        BillItem item1 = new BillItem();
        BillItem item2 = new BillItem();
        
        item1.setPrice(20);
        item2.setPrice(22);
        item1.setAmount(2);
        item2.setAmount(3);
        
        HashSet<BillItem> items  = new HashSet<BillItem>(Arrays.asList(item1, item2));
        
        String[] expression = {"price * amount"};
        
        double result = sumMerger.mergeObjects(items, expression);
        
        assertEquals(106, result, 0.00001);
    }
}
