/*
 * Copyright 2013 Juan Alberto López Cavallotti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jdto.tools;

import java.io.Serializable;
import javax.lang.model.element.Element;

/**
 * Helper class which contains information not only about a configured property
 * but also which element carries this configuration.
 * @author Juan Alberto López Cavallotti
 */
final class SourceConfiguration implements Serializable {
    
    private final String propertyName;
    
    private final Element configuredElement;

    public SourceConfiguration(String propertyName, Element configuredElement) {
        this.propertyName = propertyName;
        this.configuredElement = configuredElement;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Element getConfiguredElement() {
        return configuredElement;
    }
    
}
