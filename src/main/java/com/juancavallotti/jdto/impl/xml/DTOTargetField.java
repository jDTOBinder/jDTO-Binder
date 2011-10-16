package com.juancavallotti.jdto.impl.xml;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * the DTO target field XML element
 * @author juancavallotti
 */
@XmlRootElement(name = "field")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DTOTargetField implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String fieldName;
    private String merger;
    private String mergerParam;
    private String fieldType;
    private boolean cascade;
    private boolean dtoTransient;
    private List<DTOSourceField> sources;
    
    public DTOTargetField() {
    }
    
    @XmlAttribute
    public boolean isCascade() {
        return cascade;
    }

    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }
    
    @XmlAttribute(name="transient")
    public boolean isDtoTransient() {
        return dtoTransient;
    }

    public void setDtoTransient(boolean dtoTransient) {
        this.dtoTransient = dtoTransient;
    }
    
    @XmlAttribute(name="name")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    @XmlAttribute(name="cascadeType")
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    
    @XmlAttribute
    public String getMerger() {
        return merger;
    }

    public void setMerger(String merger) {
        this.merger = merger;
    }

    @XmlAttribute
    public String getMergerParam() {
        return mergerParam;
    }

    public void setMergerParam(String mergerParam) {
        this.mergerParam = mergerParam;
    }
    
    @XmlElement(name="source")
    public List<DTOSourceField> getSources() {
        return sources;
    }

    public void setSources(List<DTOSourceField> sources) {
        this.sources = sources;
    }
    
    
}
