package org.jdto.util;

import org.apache.commons.lang3.exception.CloneFailedException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/**
 * This class has been extracted from the ObjectUtils provided on the Apache
 * Commons-Lang library, the methods present in this class were copied here so
 * the dependency with that library could be relaxed to a lower version.
 *
 * Please note that this class may be removed in the future.
 * 
 * @author Juan Alberto López Cavallotti
 */
public class ObjectUtils {

    /**
     * Clone an object.
     *
     * @param o the object to clone
     * @return the clone if the object implements {@link Cloneable} otherwise
     * <code>null</code>
     * @throws CloneFailedException if the object is cloneable and the clone
     * operation fails
     * @since 2.6
     */
    public static Object clone(final Object o) {
        if (o instanceof Cloneable) {
            final Object result;
            if (o.getClass().isArray()) {
                final Class componentType = o.getClass().getComponentType();
                if (!componentType.isPrimitive()) {
                    result = ((Object[]) o).clone();
                } else {
                    int length = Array.getLength(o);
                    result = Array.newInstance(componentType, length);
                    while (length-- > 0) {
                        Array.set(result, length, Array.get(o, length));
                    }
                }
            } else {
                try {
                    result = org.jdto.util.MethodUtils.invokeMethod(o, "clone", null);
                } catch (final NoSuchMethodException e) {
                    throw new CloneFailedException("Cloneable type "
                            + o.getClass().getName()
                            + " has no clone method", e);
                } catch (final IllegalAccessException e) {
                    throw new CloneFailedException("Cannot clone Cloneable type "
                            + o.getClass().getName(), e);
                } catch (final InvocationTargetException e) {
                    throw new CloneFailedException("Exception cloning Cloneable type "
                            + o.getClass().getName(), e.getTargetException());
                }
            }
            return result;
        }

        return null;
    }
}
