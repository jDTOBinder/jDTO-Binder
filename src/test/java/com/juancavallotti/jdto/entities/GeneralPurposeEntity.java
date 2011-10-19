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

package com.juancavallotti.jdto.entities;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * General purpose entity which has values of different types.
 * @author juancavallotti
 */
public class GeneralPurposeEntity {
    
    private String theString;
    private Date theDate;
    private Calendar theCalendar;
    private double theDouble;
    private BigDecimal theBigDecimal;
    private int theInt;

    public GeneralPurposeEntity() {
    }

    public GeneralPurposeEntity(String theString, Date theDate, Calendar theCalendar, double theDouble, BigDecimal theBigDecimal, int theInt) {
        this.theString = theString;
        this.theDate = theDate;
        this.theCalendar = theCalendar;
        this.theDouble = theDouble;
        this.theBigDecimal = theBigDecimal;
        this.theInt = theInt;
    }

    public BigDecimal getTheBigDecimal() {
        return theBigDecimal;
    }

    public void setTheBigDecimal(BigDecimal theBigDecimal) {
        this.theBigDecimal = theBigDecimal;
    }

    public Date getTheDate() {
        return theDate;
    }

    public void setTheDate(Date theDate) {
        this.theDate = theDate;
    }

    public double getTheDouble() {
        return theDouble;
    }

    public void setTheDouble(double theDouble) {
        this.theDouble = theDouble;
    }

    public int getTheInt() {
        return theInt;
    }

    public void setTheInt(int theInt) {
        this.theInt = theInt;
    }

    public String getTheString() {
        return theString;
    }

    public void setTheString(String theString) {
        this.theString = theString;
    }

    public Calendar getTheCalendar() {
        return theCalendar;
    }

    public void setTheCalendar(Calendar theCalendar) {
        this.theCalendar = theCalendar;
    }
    
}
