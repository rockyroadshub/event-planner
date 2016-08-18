/*
 * The MIT License
 *
 * Copyright 2016 Arnell Christoper D. Dalid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Event Planner"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Event Planner.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rockyroadshub.planner.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    
    private final Integer[] param = new Integer[3];
        
    private final Map<String, String> eventData = new HashMap<>();
    
    private Event(){
        initialize();
    }
    
    private void initialize() {}
    
    public void refresh(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date0 = calendar.getTime();
        
        param[0] = year;
        param[1] = month;
        param[2] = day;

        int j = calendar.get(Calendar.MONTH);
        this.date  = dateFormat.format(date0);       
        this.year  = String.valueOf(calendar.get(Calendar.YEAR));
        this.month = CalendarPane.MONTHS[j];
        this.day   = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
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
    
    public Map<String, String> getData() {
        return eventData;
    }
    
    public static Event getInstance() {
        return Holder.INSTANCE;
    }
    
    private static final class Holder {
        private static final Event INSTANCE = new Event();
    }
}