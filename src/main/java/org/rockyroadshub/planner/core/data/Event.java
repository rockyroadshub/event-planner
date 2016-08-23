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

package org.rockyroadshub.planner.core.data;

import org.rockyroadshub.planner.core.database.Data;
import org.rockyroadshub.planner.gui.CalendarPane;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public class Event extends Data {
    private String event;
    private String description;
    private String location;
    private String date;
    private String year;
    private String month;
    private String day;
    private String start;
    private String end;
    
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    
    public Event() {
        super();
    }
    
    /**
     * Event Constructor
     * @param event title/name of the event
     * @param description brief description of the event
     * @param location where the event is going to take place
     * @param date date of the event
     * @param start time of start
     * @param end time of end
     */
    public Event(final String event, 
                 final String description, 
                 final String location,
                 final String date,
                 final String start,
                 final String end) 
    {
        this.event = event;
        this.description = description;
        this.location = location;
        this.date = date;
        this.start = start;
        this.end = end;
        
        breakDate(date);
        breakStartTime(start);
        breakEndTime(end);
    }
    
    /**
     * Gets the event parameter
     * @return event parameter
     */
    public String getEvent() {
        return event;
    }
    
    /**
     * Sets the event parameter
     * @param event title/name
     */
    public void setEvent(String event) {
        this.event = event;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
        breakDate(date);
    }   
    
    public String getStart() {
        return start;
    }
    
    public void setStart(String start) {
        this.start = start;
        breakStartTime(start);
    }
    
    public String getEnd() {
        return end;
    }
    
    public void setEnd(String end) {
        this.end = end;
        breakEndTime(end);
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
    
    public int getStartHour() {
        return startHour;
    }
    
    public int getStartMinute() {
        return startMinute;
    }
    
    public int getEndHour() {
        return endHour;
    }
    
    public int getEndMinute() {
        return endMinute;
    }
    
    /**
     * Splits the "String" date into year, month and day
     * @param date date of the event
     */
    private void breakDate(String date) {
        String[] dates = date.split("-");
        int i = Integer.parseInt(dates[1]);
        
        this.year = dates[0];
        this.month = CalendarPane.MONTHS[i-1];
        this.day = dates[2];        
    }
    
    /**
     * Splits the "String" start time into hours and minutes
     * @param start start of the event
     */
    private void breakStartTime(String start) {
        String[] times = start.split(":");
        this.startHour = Integer.parseInt(times[0]);
        this.startMinute = Integer.parseInt(times[1]);
    }
    
    /**
     * Splits the "String" end time into hours and minutes
     * @param end end of the event
     */
    private void breakEndTime(String end) {
        String[] times = end.split(":");
        this.endHour = Integer.parseInt(times[0]);
        this.endMinute = Integer.parseInt(times[1]);
    }
}
