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

package org.jdto.dtos;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import org.jdto.annotation.Source;

/**
 * DTO Designed to test the compatibility logic feature.
 * @author Juan Alberto Lopez Cavallotti
 */
public class CompatibilityDTO {
    private String strRep;
    private UsefulEnum usefulEnum;
    private Calendar theDate;
    private Date theCalendar;
    private Double theBigDecimal;
    
    @Source("theInt")
    public String getStrRep() {
        return strRep;
    }

    public void setStrRep(String strRep) {
        this.strRep = strRep;
    }

    @Source("theString")
    public UsefulEnum getUsefulEnum() {
        return usefulEnum;
    }

    public void setUsefulEnum(UsefulEnum usefulEnum) {
        this.usefulEnum = usefulEnum;
    }
    
    public Calendar getTheDate() {
        return theDate;
    }

    public void setTheDate(Calendar theDate) {
        this.theDate = theDate;
    }

    public Date getTheCalendar() {
        return theCalendar;
    }

    public void setTheCalendar(Date theCalendar) {
        this.theCalendar = theCalendar;
    }

    public Double getTheBigDecimal() {
        return theBigDecimal;
    }

    public void setTheBigDecimal(Double theBigDecimal) {
        this.theBigDecimal = theBigDecimal;
    }
    
}
