package com.juancavallotti.jdto.impl.xml;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The DTO source field mappings.
 * @author juancavallotti
 */
@XmlRootElement(name = "source")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DTOSourceField implements Serializable {
    private static final long serialVersionUID = 1L;

    public DTOSourceField() {
    }
    
    private String name;
    private String merger;
    private String mergerParam;
    private String sourceBean;
    
    @XmlAttribute(name="merger")
    public String getMerger() {
        return merger;
    }

    public void setMerger(String merger) {
        this.merger = merger;
    }

    @XmlAttribute(name="mergerParam")
    public String getMergerParam() {
        return mergerParam;
    }

    public void setMergerParam(String mergerParam) {
        this.mergerParam = mergerParam;
    }
    
    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @XmlAttribute(name="bean")
    public String getSourceBean() {
        return sourceBean;
    }

    public void setSourceBean(String sourceBean) {
        this.sourceBean = sourceBean;
    }
    
}
