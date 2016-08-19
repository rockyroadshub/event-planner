/*
 * Copyright 2016 Arnell Christoper D. Dalid.
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
package org.rockyroadshub.planner.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.rockyroadshub.planner.gui.CalendarPane;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class Event {
    
    private String date;
    private String year;
    private String month;
    private String day;
    private String dateLabel;
                
    private Event(){
        initialize();
    }
    
    private void initialize() {} 
   
    private String formatDate(String m, String d, String y) {
         return String.format("%s %s, %s", m, d, y);
    }
    
    public void refresh(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date0 = calendar.getTime();

        this.date  = dateFormat.format(date0);       
        this.year  = String.valueOf(year);
        this.month = CalendarPane.MONTHS[month];
        this.day   = String.valueOf(day);
        
        dateLabel = formatDate(this.month, this.day, this.year);
    }
    
    public String getDate() {
        return date;
    }
    
    public String getYear() {
        return year;
    }
    
    public String getMonth() {
        return month;
    }
    
    public String getDay() {
        return day;
    }
    
    public String getDateLabel() {
        return dateLabel;
    }
    
    public static Event getInstance() {
        return Holder.INSTANCE;
    }
    
    private static final class Holder {
        private static final Event INSTANCE = new Event();
    }
}