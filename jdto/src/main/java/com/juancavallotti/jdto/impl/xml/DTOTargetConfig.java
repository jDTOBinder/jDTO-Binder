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

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Common configuration for DTO targets over XML
 * @author juancavallotti
 */
public interface DTOTargetConfig {

    @XmlAttribute(name = "cascadeType")
    String getFieldType();

    @XmlAttribute
    String getMerger();

    @XmlAttribute
    String getMergerParam();

    @XmlElement(name = "source")
    List<DTOSourceField> getSources();

    @XmlAttribute
    boolean isCascade();
    
}
