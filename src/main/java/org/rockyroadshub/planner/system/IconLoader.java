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

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import org.rockyroadshub.planner.core.gui.Frame;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.core.utils.Utilities;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class IconLoader implements PropertyChangeListener {

    private IconLoader() {
        initialize();
    }

    public static IconLoader getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final IconLoader INSTANCE = new IconLoader();
    }
    
    private final Map<String, Icon> ICONS = new HashMap<>();

    public Icon getIcon(String name) {
        return ICONS.get(name);
    }
    
    private void initialize() {
        Task task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("progress")) {
            System.out.println("IconLoader");
        }
    }
    
    private class Task extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            int count = 0;
            setProgress(count);
            
            File[] icons = new File(Globals.ICONS_PATH)
                    .listFiles((File dir, String filename) 
                            -> filename.endsWith(Globals.ICONS_EXTENSION));
            
            int numberOfFiles = icons.length;
            while(count < numberOfFiles) {
                try {
                    Image img = ImageIO.read(icons[count]);
                    int index = icons[count].getName().lastIndexOf('.');
                    String name = icons[count].getName().substring(0, index);
                    ICONS.put(name, new ImageIcon(img)); 
                }catch(IOException io) {}
                
                count++;
                setProgress(Utilities
                        .getPercent(count, numberOfFiles)
                        .intValue());
            }
            
            return null;
        }
        
        @Override
        protected void done() {
            Frame.getInstance().initialize();
        }   
    }
}
