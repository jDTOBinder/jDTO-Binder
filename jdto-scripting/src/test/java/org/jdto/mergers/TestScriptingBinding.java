/*
 * Copyright 2014 juancavallotti.
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

package org.jdto.mergers;

import static org.hamcrest.Matchers.*;
import org.jdto.DTOBinder;
import org.jdto.DTOBinderFactory;
import org.jdto.mergers.dtos.GroovyBean;
import org.jdto.mergers.dtos.ScriptingDTO;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author juancavallotti
 */
public class TestScriptingBinding {

    private static final Logger logger = LoggerFactory.getLogger(TestGroovyBinding.class);
    
    @Test
    public void testBinding() {
        
        //get the binder
        DTOBinder binder = DTOBinderFactory.getBinder();
        
        //build some test objects
        GroovyBean bean1 = new GroovyBean("Adão");
        
        ScriptingDTO dto = binder.bindFromBusinessObject(ScriptingDTO.class, bean1);
        
        assertNotNull("DTO Should not be null", dto);
        
        Assert.assertThat("Data on the dto should be correct", dto, allOf(
                hasProperty("text", equalToIgnoringCase(bean1.getMyString())),
                hasProperty("compoundText", equalTo(bean1.getMyString() + " and " + bean1.getMyString()))
        ));
    }
    
}
