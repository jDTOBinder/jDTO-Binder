package com.juancavallotti.jdto.mergers;

/**
 * Merge all the parameters on a collection or array by adding them one to
 * each other. All of the numbers on the collections will be treated as a double.
 * If the collection passed in as a parameter is not a number but rather a complex
 * type, then the extra parameter will decide which property will be read. <br />
 * 
 * This method resolves its results as a Double.
 * @author juan
 */
public class SumMerger extends AbstractCalulationCollectionMerger {

    @Override
    protected Double processCalculation(Iterable collection, String extraParam) {
        double sum = 0.0;

        for (Object value : collection) {

            //get the double.
            Double doubleValue = getActualValue(value, extraParam);
            sum += doubleValue;
        }

        return sum;
    }
}
