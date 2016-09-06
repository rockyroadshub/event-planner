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

package org.rockyroadshub.planner.loader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
public enum Property {
    CALENDAR_COLOR_EVENTDAY("calendar.color.eventday"),
    CALENDAR_COLOR_CURRENTDAY("calendar.color.currentday"),
    CALENDAR_COLOR_WEEKDAYS("calendar.color.weekdays"),
    CALENDAR_COLOR_DEFAULTDAY("calendar.color.defaultday"),
    CALENDAR_COLOR_FOREGROUND("calendar.color.foreground"),
    CALENDAR_ICON_THEME("calendar.icon.theme");
        
    private final String key; 
    
    Property(String key) {
        this.key = key;
    }
    
    @Override
    public String toString() {
        return key;
    }
}
