package com.juancavallotti.jdto.mergers;

import org.apache.commons.lang.StringUtils;

/**
 * Covers some typical case when you wish to make a sum of the result of a product. <br />
 * 
 * If you could picture this as if was an SQL query, it will look like the following expression: <br />
 * 
 * <pre>
 *     select sum(a * b * c) from mytable as collection. 
 * </pre>
 * 
 * if it is configured like the following way: <br />
 * 
 * <pre>
 * 
 * &#64;Source(value="collection", merger=SumProductMerger.class, mergerParam="a,b,c"
 * private double resultHolder;
 * 
 * </pre>
 * 
 * This is a really typical case specially when dealing with bills which can
 * contain a price, an amount and a tax factor: <br />
 * 
 * <ul><li>
 * sum(itemPrice * itemAmount * taxFactor) <br />
 * </li></ul>
 * But it can be applied on other situations as well.
 * 
 * @author juan
 */
public class SumProductMerger extends AbstractCalulationCollectionMerger {

    @Override
    protected Double processCalculation(Iterable collection, String extraParam) {

        String[] factorNames = StringUtils.split(extraParam, ",");

        double sum = 0.0;

        for (Object value : collection) {

            double[] factors = getActualFactors(value, factorNames);

            double doubleValue = factors.length == 0 ? 0.0 : 1.0;

            for (double factor : factors) {
                doubleValue *= factor;
            }

            //get the double.

            sum += doubleValue;
        }

        return sum;
    }

    private double[] getActualFactors(Object value, String[] factorNames) {

        double[] ret = new double[factorNames.length];

        for (int i = 0; i < factorNames.length; i++) {
            String string = factorNames[i];
            ret[i] = getActualValue(value, string);
        }

        return ret;
    }
}
