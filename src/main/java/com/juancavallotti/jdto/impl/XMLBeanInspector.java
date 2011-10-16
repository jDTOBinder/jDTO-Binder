package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.impl.xml.DTOElement;
import com.juancavallotti.jdto.impl.xml.DTOMappings;
import com.juancavallotti.jdto.impl.xml.DTOTargetField;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WARNING: THIS CLASS IS NOT PART OF JDTO PUBLIC API
 * 
 * XML Configuration reader.
 * @author juancavallotti
 */
public class XMLBeanInspector implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(XMLBeanInspector.class);

    /**
     * Get an instance of XMLBeanInspector which reads configuration over a 
     * package resource.
     * @param packageResource
     * @return 
     */
    public static XMLBeanInspector getInstance(String packageResource) {
        InputStream is = XMLBeanInspector.class.getResourceAsStream(packageResource);
        try {

            if (is == null) {
                logger.error("wrong package resource: " + packageResource);
                throw new IllegalArgumentException("Can't find the package resource.");
            }

            return new XMLBeanInspector(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    logger.error("Got exception while parsing xml file", ex);
                    throw new RuntimeException(ex);
                }
            }
        }
    }
    private final DTOMappings mappings;
    private final BeanInspector inspector;

    /**
     * Read the XML file which can be read by accessing the input stream taken 
     * as a parameter.
     * @param xmlStream 
     */
    public XMLBeanInspector(InputStream xmlStream) {
        if (xmlStream == null) {
            logger.error("Null xml input stream");
            throw new IllegalArgumentException("XML Stream cannot be null");
        }

        mappings = parseXML(xmlStream);
        inspector = new BeanInspector();
    }

    private DTOMappings parseXML(InputStream xmlStream) {
        try {
            JAXBContext context = JAXBContext.newInstance(DTOMappings.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            DTOMappings inputMappings = (DTOMappings) unmarshaller.unmarshal(xmlStream);

            return inputMappings;
        } catch (Exception ex) {
            logger.error("Got exception while parsing xml file", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method is not really part of the JDTO public API
     * DO NOT CALL IT
     * @return 
     */
    public DTOMappings getMappings() {
        return mappings;
    }

    /**
     * Build the bean metadata based on the information recovered on the xml 
     * parsing process
     * @return 
     */
    public synchronized HashMap<Class, BeanMetadata> buildMetadata() {
        HashMap<Class, BeanMetadata> ret = new HashMap<Class, BeanMetadata>();

        for (DTOElement dtoElement : mappings.getElements()) {
            Class dtoClass = XMLBeanMetadataReader.safeGetClass(dtoElement.getType());
            if (dtoClass == null) {
                logger.warn("could not find bean of type: " + dtoElement.getType() + " so i will ignore it!");
                continue;
            }

            BeanMetadata dtoMetadata = buildBeanMetadata(dtoClass, dtoElement);
            ret.put(dtoClass, dtoMetadata);
        }

        return ret;
    }

    /**
     * This is where things happen for real!
     * @param dtoClass
     * @param dtoElement
     * @return 
     */
    private BeanMetadata buildBeanMetadata(Class dtoClass, DTOElement dtoElement) {

        //use a standard inspector to read the field metadata.
        //then mapped values will override the default values and everyone is happy.
        //this may cause strange behavior when the targer bean is annotated.

        BeanMetadata ret = inspector.inspectBean(dtoClass);

        //the most easy part, the bean source names.
        ret.setDefaultBeanNames(XMLBeanMetadataReader.readDefaultBeanNames(dtoElement));

        //build the map with the field metadata
        HashMap<String, FieldMetadata> fieldsMetadata = ret.getFieldMetadata();

        ret.setFieldMetadata(fieldsMetadata);

        List<DTOTargetField> configuredFields = (dtoElement.getTargetFields() == null)
                ? Collections.EMPTY_LIST : dtoElement.getTargetFields();




        //go through the configured fields.
        for (DTOTargetField dtoTargetField : configuredFields) {

            //the name of the target property is mandatory.
            String targetFieldName = dtoTargetField.getFieldName();

            //if the field is transient then remove it
            if (dtoTargetField.isDtoTransient()) {
                fieldsMetadata.remove(targetFieldName);
                continue;
            }

            //build the metadata
            FieldMetadata fieldMetadata = XMLBeanMetadataReader.readFieldMetadata(dtoClass, dtoTargetField);
            
            if (fieldMetadata.isFieldTransient()) {
                continue;
            }
            
            fieldsMetadata.put(targetFieldName, fieldMetadata);
        }

        return ret;
    }
}
