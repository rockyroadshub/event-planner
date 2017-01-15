/*
 * Copyright 2017 Arnell Christoper D. Dalid.
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

package org.rockyroadshub.planner.data;

import org.rockyroadshub.planner.core.database.Data;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
@SuppressWarnings("serial")
public final class Alarm extends Data {
    
    private String alarm;
    private String description;
    private String time;
    private String days;
    
    private int timeHour;
    private int timeMinute;
    
    public Alarm() {
        super();
    }
    
    public Alarm(final String alarm, 
                 final String description, 
                 final String time,
                 final String days) 
    {
        this.alarm = alarm;
        this.description = description;
        this.time = time;
        this.days = days;
        
        parseTime(time);
    }
    
    public String getAlarm() {
        return alarm;
    }
    
    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
        parseTime(time);
    }
    
    public String getDays() {
        return days;
    }
    
    public void setDays(String days) {
        this.days = days;
    }
    
    private void parseTime(String start) {
        String[] times = start.split(":");
        this.timeHour = Integer.parseInt(times[0]);
        this.timeMinute = Integer.parseInt(times[1]);
    }
}
