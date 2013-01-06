/*
 * Copyright 2013 Juan Alberto López Cavallotti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jdto;

import java.util.List;
import org.jdto.impl.CascadeType;

/**
 * API that abstracts the information needed for binding a specific field of a DTO.
 * @author Juan Alberto López Cavallotti
 * @since 1.4
 */
public interface DTOFieldMetadata {

    /**
     * If the field is cascaded, get the type which this field should have after
     * cascading.
     * @return the class of the cascade result.
     */
    Class getCascadeTargetClass();

    /**
     * If the field is cascaded, return the type of the cascade that should be 
     * made to this DTO.
     * @return an enum constant with the cascade type.
     */
    CascadeType getCascadeType();
    
    /**
     * A list of merger parameters specified by configuration.
     * @return a list of strings or null.
     */
    String[] getMergerParameter();
    
    /**
     * The multi property value merger used for merging this field.
     * @return 
     */
    Class<? extends MultiPropertyValueMerger> getPropertyValueMerger();
    
    /**
     * The source bean names, this overrides the configuration made on 
     * {@link DTOMetadata#getDefaultBeanNames() }.
     * @return an array of strings with the bean names.
     */
    String[] getSourceBeanNames();
    
    /**
     * The source bean names specified by configuration.
     * @return an array of strings with the bean names.
     */
    String[] getSourceBeans();
    
    /**
     * The list of fields from which the target value is derived.
     * @return a list of fields on the source bean.
     */
    List<String> getSourceFields();
    
    /**
     * An array representing the mergers to be applied to the source fields.
     * @return an array consisting on the types of the mergers to be applied.
     */
    Class[] getSourceMergers();
    
    /**
     * An array of the source merger params specified by each merger, this is 
     * matched by index with the result of {@link #getSourceMergers() } method.
     * @return an array of String arrays.
     */
    String[][] getSourceMergersParams();

    /**
     * The type of the target field.
     * @return a class object representing the type of the target field.
     */
    Class getTargetType();
    
    /**
     * Check if the cascade configuration is present.
     * @return true if this field is configured for cascade, false otherwise.
     */
    boolean isCascadePresent();
    
    /**
     * Check if the transient configuration is present.
     * @return true if the field is configured as transient, false otherwise.
     */
    boolean isFieldTransient();

}
