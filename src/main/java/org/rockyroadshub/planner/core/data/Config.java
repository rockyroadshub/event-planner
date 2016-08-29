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

import java.awt.Color;
import java.io.Serializable;
import org.rockyroadshub.planner.core.database.Data;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class Config extends Data implements Serializable{

    private static final long serialVersionUID = -5322086761689546386L;
    
    private String configName = "DEFAULT";
    private Color  dayColor   = Color.YELLOW;
    private Color  eventColor = new Color(50, 130, 180);

    public Config() {}
    
    public Config(String configName,
                  Color dayColor,
                  Color eventColor) 
    {
        this.configName = configName;
        this.dayColor   = dayColor;
        this.eventColor = eventColor;
    }
    
    public String getConfigName() {
        return configName;
    }
    
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    
    public Color getDayColor() {
        return dayColor;
    }
    
    public void setDayColor(Color dayColor) {
        this.dayColor = dayColor;
    }
    
    public Color getEventColor() {
        return eventColor;
    }
    
    public void setEventColor(Color eventColor) {
        this.eventColor = eventColor;
    }
}