/*
 *    Copyright 2011 Juan Alberto López Cavallotti
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
