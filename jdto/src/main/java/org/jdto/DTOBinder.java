/*
 *    Copyright 2012 Juan Alberto López Cavallotti
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

package org.jdto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A DTO binder takes care of the shallow copy operation between business objects, 
 * which normally are decorated with different types of proxies (for example)
 * lazy loading proxies. <br />
 * 
 * By design a binder will take care of all the boilerplate required to populate
 * DTOs from business objects and the other way around (whenever is possible). <br /><br />
 * 
 * In order to get an instance of a DTOBinder you should call
 * {@link DTOBinderFactory#buildBinder() } or any of its overloads.
 * 
 * @author Juan Alberto Lopez Cavallotti
 * @since 1.0
 */
public interface DTOBinder extends Serializable{
    
    /**
     * Create a new DTO instance filled with data extracted from one or more 
     * business objects. <br />
     * 
     * The business objects (if more than one) should be named by the means of
     * metadata and binding expressions with the attributes, or constructor parameters
     * of the DTO.
     * 
     * <br />
     * 
     * You may configure the DTO objects to declaratively indicate where the values
     * for the fields should be read.
     * 
     * Normally DTOs can be configured via a XML file or Annotations, to know
     * more about the framewokr functionality and the types of configuration
     * please refer tho the manual. <br />
     * 
     * @param <T> Indicates the type of the DTO returned to avoid downcasts.
     * @param dtoClass The class of the resulting DTO.
     * @param businessObjects a list of Business Objects to extract fresh data.
     * The data will be extracted always by executing getter methods and never by
     * direct field access.
     * 
     * @return an instance of the DTO populated with the data specified on the metadata. 
     * @throws some subclass of {@link RuntimeException} on various error conditions.
     * @since 1.0
     */
    public <T> T bindFromBusinessObject(Class<T> dtoClass, Object... businessObjects);
    
    
    /**
     * Create a list of DTOs by performing multiple calls to 
     * {@link DTOBinder#bindFromBusinessObject(java.lang.Class, java.lang.Object[]) } 
     * you can supply more than one {@link List } but they should have the same size
     * and same indexes should contain related data. <br />
     * 
     * @param <T> The type of the DTOs list which will be returned.
     * @param dtoClass the class of the resulting DTOs
     * @param businessObjectsLists lists of business objects.
     * @return A list of DTO's populated with the provided business objects.
     * 
     * @throws some subclass of {@link RuntimeException} on various error conditions.
     */
    public <T> List<T> bindFromBusinessObjectList(Class<T> dtoClass, List... businessObjectsLists);
    
    
    /**
     * Create a collection of DTOs by performing multiple calls to 
     * {@link DTOBinder#bindFromBusinessObject(java.lang.Class, java.lang.Object[]) }.<br />
     * 
     * The returned iterable will be of the <u>same type</u> of the collection
     * passed as a parameter. In the cases of instantiating the given collection is not 
     * possible, then the following rules apply: <br />
     * 
     * <ul>
     *      <li>If the collection is a Set, then the returned object will be {@link java.util.HashSet}</li>
     *      <li>If the collection is a List, then the returned object will be {@link java.util.LinkedList}</li>
     * </ul>
     * 
     * @param <T> The type of the DTOs to be bound.
     * @param dtoClass the class of the
     * @param businessObjects the list of source business objects.
     * @since 1.1
     * @return the collection of the same type of the original collection but filled with DTOs
     * 
     * @throws some subclass of {@link RuntimeException} on various error conditions.
     */
    public <T,R extends Collection> R bindFromBusinessObjectCollection(Class<T> dtoClass, R businessObjects);
    
    /**
     * Makes the best effort to extract data from a dto to a business object. <br />
     * 
     * In order to perform this operation, the user can provide reverse metadata
     * but also the extraction metadata can be used for non-composite attributes. <br /> 
     * 
     * jDTO Binder takes seriously the approach of flexibility on populating DTO
     * objects against perfect inverse binding so many data conversion operations
     * may not be reversible and therefore extracting data from a DTO can be kind
     * of limited unless you provide reverse annotations and custom data deconversion. <br />
     * 
     * @param <T> Indicates the type of the business object to avoid downcasts.
     * @param businessObjectClass the class of the resulting business object.
     * @param dto the source from which the data will be extracted.
     * @return a new instance of the business object populated on a best-effort
     * from the dto.
     */
    public <T> T extractFromDto(Class<T> businessObjectClass, Object dto);
}
