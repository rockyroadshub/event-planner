/*
 * The MIT License
 *
 * Copyright 2016 Arnell Christoper D. Dalid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Event Planner), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rockyroadshub.planner.lib;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class Event {
    
    private String event;
    private String description;
    private String location;
    private String date;
    private String year;
    private String month;
    private String day;
    private String start;
    private String end;
        
    private final Map<String, String> eventData = new HashMap<>();
    
    private Event(){
        initialize();
    }
    
    private void initialize() {}
    
    public void set(
        String title, String description, String location, String date,
        String year, String month, String day, String start, String end) 
    {
        this.event       = title;
        this.description = description;
        this.location    = location;
        this.date        = date;
        this.year        = year;
        this.month       = month;
        this.day         = day;
        this.start       = start;
        this.end         = end;
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