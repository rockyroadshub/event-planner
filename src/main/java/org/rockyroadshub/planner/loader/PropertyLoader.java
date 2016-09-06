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

import com.jcabi.aspects.LogExceptions;
import java.awt.Color;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.rockyroadshub.planner.splash.SplashFrame;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
public final class PropertyLoader extends AbstractLoader<Object> {
    private final PropertiesConfiguration plannerProperties = new PropertiesConfiguration();
    
    private PropertyLoader() {}

    public static PropertyLoader getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PropertyLoader INSTANCE = new PropertyLoader();
    }
    
    public void setProperty(Property property, Object value) {
        String val = null;
        if(value instanceof Color) {
            Color color = (Color)value;
            val = String.format("#%08x", color.getRGB());
        }
        plannerProperties.setProperty(property.toString(), val);
    }
    
    public Color getColor(Property property) {        
        int c = Long.decode(plannerProperties
                    .getString(property
                    .toString()))
                    .intValue();
        
        int r = (c >> 16) & 0xFF;
        int g = (c >> 8)  & 0xFF;
        int b = (c & 0xFF);
        int a = (c >> 24) & 0xFF;
        return new Color(r,g,b,a);
    }
    
    public String getString(Property property) {
        return plannerProperties.getString(property.toString());
    }
    
    public void commit() {
        try {
            save();
        } 
        catch (ConfigurationException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    @LogExceptions
    private void save() throws ConfigurationException {
        plannerProperties.save();
    }
    
    @Override
    public void load() {
        super.load();
        Task task = new Task(
                SplashFrame.getInstance(),
                MemoryLoader.getInstance());
        task.execute();
    }
    
    private class Task extends AbstractTask<Void> {

        public Task(SplashFrame frame, AbstractLoader loader) {
            super(frame, loader);
        }

        @LogExceptions
        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);
            publish("Loading planner properties...");
            loadProperties();
            setProgress(50);
            publish("Planner properties loaded.");
            
            publish("Loading configurations...");
            refresh();
            publish("All configurations loaded.");
            setProgress(100);
            return null;
        }
        
        private void loadProperties() throws ConfigurationException {
            File file = FileLoader.getInstance().get("planner");
            plannerProperties.setFile(file);
            plannerProperties.setDelimiterParsingDisabled(false);
            plannerProperties.setListDelimiter(',');
            plannerProperties.load();
        }    
    }
    
    public Color calendar_color_eventday;
    public Color calendar_color_currentday;
    public Color calendar_color_weekdays;
    public Color calendar_color_defaultday;
    public Color calendar_color_foreground;
    
    public String calendar_icon_theme;
    
    public void refresh() {
        calendar_color_eventday   = getColor(Property.CALENDAR_COLOR_EVENTDAY);
        calendar_color_currentday = getColor(Property.CALENDAR_COLOR_CURRENTDAY);
        calendar_color_weekdays   = getColor(Property.CALENDAR_COLOR_WEEKDAYS);
        calendar_color_defaultday = getColor(Property.CALENDAR_COLOR_DEFAULTDAY);
        calendar_color_foreground = getColor(Property.CALENDAR_COLOR_FOREGROUND);  
        
        calendar_icon_theme       = getString(Property.CALENDAR_ICON_THEME);
    }
 }