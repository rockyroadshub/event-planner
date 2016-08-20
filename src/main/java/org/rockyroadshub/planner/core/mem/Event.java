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

package org.rockyroadshub.planner.core.mem;

import java.io.Serializable;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public final class Event extends Data implements Serializable {
    
    private String event;
    private String description;
    private String location;
    private String date;
    private String year;
    private String month;
    private String day;
    private String start;
    private String end;
    
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
        
    }
    
    public String getEvent() {
        return event;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getLocation() {
        return location;
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
    
    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
    
    @Override
    public Data getData() {
        return this;
    }
}