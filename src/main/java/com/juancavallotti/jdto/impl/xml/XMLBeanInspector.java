package com.juancavallotti.jdto.impl.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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

    DTOMappings getMappings() {
        return mappings;
    }
    
}
