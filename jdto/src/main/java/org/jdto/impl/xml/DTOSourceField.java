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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The DTO source field mappings.
 * @author Juan Alberto Lopez Cavallotti
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
