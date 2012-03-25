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
package org.jdto.impl.xml;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a constructor arg readed from a XML configuration file.
 * @author juancavallotti
 */
@XmlRootElement(name = "arg")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DTOConstructorArg implements Serializable, DTOTargetConfig {

    private static final long serialVersionUID = 1L;
    private Integer order;
    private String type;
    private String merger;
    private String mergerParam;
    private String fieldType;
    private boolean cascade;
    private List<DTOSourceField> sources;

    public DTOConstructorArg() {
    }

    @XmlAttribute
    @Override
    public boolean isCascade() {
        return cascade;
    }

    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }

    @XmlAttribute(name = "cascadeType")
    @Override
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @XmlAttribute
    @Override
    public String getMerger() {
        return merger;
    }

    public void setMerger(String merger) {
        this.merger = merger;
    }

    @XmlAttribute
    @Override
    public String getMergerParam() {
        return mergerParam;
    }

    public void setMergerParam(String mergerParam) {
        this.mergerParam = mergerParam;
    }

    @XmlElement(name = "source")
    @Override
    public List<DTOSourceField> getSources() {
        return sources;
    }

    public void setSources(List<DTOSourceField> sources) {
        this.sources = sources;
    }

    @XmlAttribute(name = "order")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @XmlAttribute(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
