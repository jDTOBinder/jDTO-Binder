package com.juancavallotti.jdto.impl.xml;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juancavallotti
 */
@XmlRootElement(name = "dto-mapping")
@XmlAccessorType(XmlAccessType.PROPERTY)
class DTOMappings implements Serializable {

    private static final long serialVersionUID = 1L;

    public DTOMappings() {
    }
    private List<DTOElement> elements;

    @XmlElement(name = "dto")
    public List<DTOElement> getElements() {
        return elements;
    }

    public void setElements(List<DTOElement> elements) {
        this.elements = elements;
    }
}
