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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a DTOElement of an XML file.
 * @author Juan Alberto Lopez Cavallotti
 */
@XmlRootElement(name = "dto")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DTOElement implements Serializable {

    private static final long serialVersionUID = 1L;
    private String type;
    private List<String> beanNames;
    private List<DTOTargetField> targetFields;
    private List<DTOConstructorArg> constructorArgs;
    
    public DTOElement() {
    }
    
    @XmlAttribute(name="type", required = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @XmlElementWrapper(name="sourceNames")
    @XmlElement(name = "beanName")
    public List<String> getBeanNames() {
        return beanNames;
    }

    public void setBeanNames(List<String> beanNames) {
        this.beanNames = beanNames;
    }
    
    @XmlElement(name = "field")
    public List<DTOTargetField> getTargetFields() {
        return targetFields;
    }

    public void setTargetFields(List<DTOTargetField> targetFields) {
        this.targetFields = targetFields;
    }
    
    @XmlElementWrapper(name = "immutableConstructor")
    @XmlElement(name = "arg")
    public List<DTOConstructorArg> getConstructorArgs() {
        return constructorArgs;
    }

    public void setConstructorArgs(List<DTOConstructorArg> constructorArgs) {
        this.constructorArgs = constructorArgs;
    }
    
}
