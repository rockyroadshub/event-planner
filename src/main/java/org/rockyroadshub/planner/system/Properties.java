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

package org.rockyroadshub.planner.system;

import com.jcabi.aspects.LogExceptions;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.SwingWorker;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.rockyroadshub.planner.core.utils.Utilities;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class Properties implements PropertyChangeListener {
    private Properties() {
        initialize();
    }

    public static Properties getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final Properties INSTANCE = new Properties();
    }

    private static final String FILE_PATH = "prop/planner.properties";
    private static final String SOURCE_PATH = "/org/rockyroadshub/planner/src/cfg/planner.properties";
        
    public Color color_eventday;
    public Color color_currentday;
    
    public static final String COLOR_EVENTDAY = "calendar.color.eventday";
    public static final String COLOR_CURRENTDAY = "calendar.color.currentday";
    
    private PropertiesConfiguration properties;
    
    public Object getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public void setProperty(String key, Object value) {
        if(value instanceof Color) {
            Color color = (Color)value;
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            int a = color.getAlpha();
            String c = String.format("%d,%d,%d,%d", r,g,b,a);
            properties.setProperty(key, c);
        }
        else {
            properties.setProperty(key, value);
        }
        try {
            properties.save();
        } catch (ConfigurationException ex) {
        }
    }
    
    /**
     * Gets color elements as string array and converts it to
     * <code>Color</code> object.
     * 
     * @param key Configuration Key
     * 
     * @return color of the specified key
     */
    public Color getColor(String key) {
        String[] colors = properties.getStringArray(key);
        int[] cc  = new int[colors.length];
        for(int i = 0; i < colors.length; i++) {
            cc[i] = Integer.parseInt(colors[i]);
            if(cc[i] > 255)cc[i] = 255;
        }
        switch(colors.length) {
            case 4: return new Color(cc[0], cc[1], cc[2], cc[3]);
            case 3: return new Color(cc[0], cc[1], cc[2]);
            case 2: return new Color(cc[0], cc[1], 0);
            case 1: return new Color(cc[0], 0, 0);
            default: return Color.WHITE;
        }
    }
    
    public void load() {
        color_eventday   = getColor(COLOR_EVENTDAY);
        color_currentday = getColor(COLOR_CURRENTDAY);
    }
    
    private void initialize() {
        Task task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("progress")) {
            System.out.println("Properties");
        }
    }
    
    private class Task extends SwingWorker<Void, Void> {

        @LogExceptions
        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);
            properties = new PropertiesConfiguration();
            Utilities.checkFile(FILE_PATH, SOURCE_PATH);
            properties.setFile(new File(FILE_PATH));
            properties.setDelimiterParsingDisabled(false);
            properties.setListDelimiter(',');
            properties.load();
            load();
            setProgress(100);
            return null;
        }
        
        @Override
        protected void done() {
            IconLoader.getInstance();
        }       
    }
}