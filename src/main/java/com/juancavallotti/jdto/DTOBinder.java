package com.juancavallotti.jdto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO binder takes care of the deep copy operation between business objects, 
 * which normally may be decorated with different types of proxies (for example)
 * lazy loading proxies. <br />
 * 
 * By design a binder should take care of all the boilerplate required to populate
 * DTOs from business objects and the other way around (whenever is possible). <br /><br />
 * 
 * In order to get an instance of a DTOBinder you should call
 * {@link DTOBinderFactory#buildBinder() } or any of its overloads.
 * 
 * @author juancavallotti
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
     * TODO - IMPROVE DOCUMENTATION ON HOW OBJECTS WILL BE BOUND.
     * 
     * @param <T> Indicates the type of the DTO returned to avoid downcasts.
     * @param dtoClass The class of the resulting DTO.
     * @param businessObjects a list of Business Objects to extract fresh data.
     * The data will be extracted always by executing getter methods and never by
     * direct field access.
     * 
     * @return an instance of the DTO populated with the data specified on the metadata. 
     * @throws some subclass of {@link RuntimeException} on various error conditions.
     */
    public <T> T bindFromBusinessObject(Class<T> dtoClass, Object... businessObjects);
    
    
    /**
     * Create a list of DTOs by performing multiple calls to 
     * {@link DTOBinder#bindFromBusinessObject(java.lang.Class, java.lang.Object[]) } 
     * you can supply more than one {@link List } but they should have the same size
     * and same indexes should contain related data.
     * @param <T> The type of the DTOs list which will be returned.
     * @param dtoClass the class of the resulting DTOs
     * @param businessObjectsLists lists of business objects.
     * @return A list of DTO's populated with the provided business objects.
     * 
     * @throws some subclass of {@link RuntimeException} on various error conditions.
     */
    public <T> List<T> bindFromBusinessObjectList(Class<T> dtoClass, List... businessObjectsLists);
    
    
    /**
     * Makes the best effort to extract data from a dto to a business object. <br />
     * 
     * In order to perform this operation, the user can provide reverse metadata
     * but also the extraction metadata can be used for non-composite attributes. <br /> 
     * 
     * TODO - IMPROVE DOCUMENTATION ON HOW OBJECTS WILL BE BOUND.
     * 
     * @param <T> Indicates the type of the business object to avoid downcasts.
     * @param businessObjectClass the class of the resulting business object.
     * @param dto the source from which the data will be extracted.
     * @return a new instance of the business object populated on a best-effort
     * from the dto.
     */
    public <T> T extractFromDto(Class<T> businessObjectClass, Object dto);
}
