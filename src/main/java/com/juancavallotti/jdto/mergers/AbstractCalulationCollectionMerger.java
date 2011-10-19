package com.juancavallotti.jdto.mergers;

import com.juancavallotti.jdto.BeanModifier;
import com.juancavallotti.jdto.BeanModifierAware;
import com.juancavallotti.jdto.SinglePropertyValueMerger;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;

/**
 * Base class to implement a merger based on looping over a collection or array.
 * Which will produce a result based on a calculation.
 * <br />
 * 
 * This class will loop over the collection and let the imlementation decide
 * what to do with the resulting value which must be a Double.
 * @author juancavallotti
 */
public abstract class AbstractCalulationCollectionMerger implements SinglePropertyValueMerger<Double, Object>, BeanModifierAware {

    @Override
    public Double mergeObjects(Object input, String extraParam) {
        //the value could be null so check that.
        if (input == null) {
            return 0.0;
        }

        //convert it into an iterable instance.
        Iterable values = convertInput(input);

        return processCalculation(values, extraParam);
    }

    /**
     * Concrete implementation of the calculation processing cycle.
     * @param collection the input pre-processed as a collection so it can be iteraded on a foreach loop.
     * @param extraParam the merge extra-param.
     * @return the result of the calculation.
     */
    protected abstract Double processCalculation(Iterable collection, String extraParam);

    private Iterable convertInput(Object input) {
        if (input instanceof Iterable) {
            return (Iterable) input;
        }

        //if we found an array, then we better convert it to something we
        //can iterate withoth too much effort.
        //TODO - IMPLEMENT A BETTER SOLUTION
        if (input.getClass().isArray()) {
            ArrayList ret = new ArrayList();

            for (int i = 0; i < Array.getLength(input); i++) {
                ret.add(Array.get(input, i));
            }
            return ret;
        }

        //if everything else fails.
        return Collections.EMPTY_LIST;
    }
    
    /**
     * Get the actual number from a property. An empty or null extra param means
     * return the same object.
     * 
     * @param value
     * @param extraParam
     * @return 
     */
    protected Double getActualValue(Object value, String extraParam) {

        if (value == null) {
            return 0.0;
        }

        if (StringUtils.isEmpty(extraParam)) {
            return ((Number) value).doubleValue();
        } else {
            return ((Number) modifier.readPropertyValue(extraParam, value)).doubleValue();
        }
    }
    
    //BEAN MODIFIER AWARE CODE
    protected BeanModifier modifier;

    @Override
    public void setBeanModifier(BeanModifier modifier) {
        this.modifier = modifier;
    }
}
