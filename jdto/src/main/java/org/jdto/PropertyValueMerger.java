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
 * Marker interface to make easier work with mergers. <br />
 * This interface is not intended to be implemented by users but to provide a 
 * single type to deal with the different kinds of property value mergers. <br />
 * 
 * If you wish to implement your own merger, please take a look at:
 * <ul>
 * <li>{@link SinglePropertyValueMerger SinglePropertyValueMerger}.</li>
 * <li>{@link MultiPropertyValueMerger MultiPropertyValueMerger}.</li>
 * </ul>
 * @since 1.2
 * @author Juan Alberto Lopez Cavallotti
 */
public interface PropertyValueMerger {

}
