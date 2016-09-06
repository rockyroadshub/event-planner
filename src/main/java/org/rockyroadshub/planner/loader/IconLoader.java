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
import java.awt.Image;
import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.rockyroadshub.planner.core.gui.MainFrame;
import org.rockyroadshub.planner.splash.SplashFrame;
import org.rockyroadshub.planner.core.utils.Utilities;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
public final class IconLoader extends AbstractLoader<Icon>{
    private static final String[] EXTENSIONS = {"png"};
    private static final String   DIRECTORY  = "src/images/icons";
    private static final String   MAIN_PATH  = DIRECTORY + "/%s";
    
    private final Collection<File> iconThemes = new TreeSet<>();
    
    private IconLoader() {}

    public static IconLoader getInstance() {
        return Holder.INSTANCE;
    }  

    private static class Holder {
        private static final IconLoader INSTANCE = new IconLoader();
    }

    @Override
    public void load() {
        super.load();
        Task task = new Task(
                SplashFrame.getInstance(), 
                MemoryLoader.getInstance());
        task.execute();
    }
    
    public Collection<File> getIconThemes() {
        return iconThemes;
    }
    
    private class Task extends AbstractTask<Void> {
        
        private Task(SplashFrame frame, AbstractLoader loader) {
            super(frame, loader);
        }

        @LogExceptions
        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);       
            publish("Loading icons...");
            int count = 0;
            Collections.addAll(iconThemes, new File(DIRECTORY)
                    .listFiles((FileFilter)DirectoryFileFilter.DIRECTORY));
            
            String iconThemeName = PropertyLoader.getInstance().calendar_icon_theme;
            String iconThemePath = String.format(MAIN_PATH, iconThemeName);
            File iconTheme = new File(iconThemePath);
            
            if(!iconThemes.contains(iconTheme)) {
                iconThemePath = String.format(MAIN_PATH, "default");
                iconTheme = new File(iconThemePath);
            }
            
            Collection<File> icons = FileUtils.listFiles(iconTheme, EXTENSIONS, false);
            int numberOfFiles = icons.size();
            for(File icon : icons) {
                Image img = ImageIO.read(icon);
                String name = FilenameUtils.removeExtension(icon.getName());
                count++;
                add(name, new ImageIcon(img));
                publish(iconThemePath + "/" + icon.getName() + " loaded.");
                setProgress(Utilities.getPercent(count, numberOfFiles).intValue());
            }     
            return null;
        }
        
        @Override
        protected void done() {
            publish("Event Planner is ready.");
            MainFrame.getInstance();
            frame.dispose();
        }
    }
 }
