/*
 *    Copyright 2012 Juan Alberto López Cavallotti
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
package org.jdto;

/**
 * Handles the instantiation of property value mergers in a platform-dependant way.
 * 
 * @since 1.2
 * @author Juan Alberto Lopez Cavallotti
 */
public interface PropertyValueMergerInstanceManager {
    
    /**
     * Get an instance of a @{@link PropertyValueMerger}. Implementations may
     * use an instance pool or a dependency injection context. <br /><br />
     * 
     * The returned value merger will have its dependencies injected by using setter
     * dependency injection if it requires components of the framework and also
     * other platform dependant dependencies may be injected by other means. <br /><br />
     * 
     * @param <T> the type of the merger to return.
     * @param mergerClass the class of the merger.
     * @return an instance of a property value merger with its dependencies set.
     */
    <T extends PropertyValueMerger> T getPropertyValueMerger(Class<T> mergerClass);
}
