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
import org.rockyroadshub.planner.core.gui.calendar.CalendarPane;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
@SuppressWarnings("serial")
public final class Event extends Data {
    private String event;
    private String description;
    private String location;
    private String date;
    private String year;
    private String month;
    private String day;
    private String start;
    private String end;
    
    /**
     * Start variable - hour
     */
    private int startHour;
    
    /**
     * Start variable - minute
     */
    private int startMinute;
    
    /**
     * End variable - hour
     */
    private int endHour;
    
    /**
     * End variable - minute
     */
    private int endMinute;
    
    /**
     * Event's constructor
     */
    public Event() {
        super();
    }
    
    /**
     * Event's Constructor
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
    
    /**
     * Gets the description of an event
     * @return description parameter
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description of an event
     * @param description description parameter
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the location of an event
     * @return location parameter
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Sets the location of an event
     * @param location location parameter
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * Gets the date of an event
     * @return date parameter
     */
    public String getDate() {
        return date;
    }
    
    /**
     * Sets the date of an event
     * @param date date parameter
     */
    public void setDate(String date) {
        this.date = date;
        breakDate(date);
    }   
    
    /**
     * Gets the time when the event will start
     * @return start time parameter
     */
    public String getStart() {
        return start;
    }
    
    /**
     * Sets the time when the event will start
     * @param start start time parameter
     */
    public void setStart(String start) {
        this.start = start;
        breakStartTime(start);
    }
    
    /**
     * Gets the time when the event will end
     * @return end time parameter
     */
    public String getEnd() {
        return end;
    }
    
    /**
     * Sets the time when the event will end
     * @param end end time parameter
     */
    public void setEnd(String end) {
        this.end = end;
        breakEndTime(end);
    }  
     
    /**
     * 
     * @return year of the event
     */
    public String getYear() {
        return year;
    }
    
    /**
     * 
     * @return month of the event
     */
    public String getMonth() {
        return month;
    }
    
    /**
     * 
     * @return day of the event
     */
    public String getDay() {
        return day;
    }
    
    /**
     * 
     * @return start of the event (hour parameter)
     */
    public int getStartHour() {
        return startHour;
    }
    
    /**
     * 
     * @return start of the event (minute parameter)
     */
    public int getStartMinute() {
        return startMinute;
    }
    
    /**
     * 
     * @return end of the event (hour parameter)
     */
    public int getEndHour() {
        return endHour;
    }
    
    /**
     * 
     * @return end of the event (minute parameter)
     */
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
