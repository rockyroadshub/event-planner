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
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.rockyroadshub.planner.core.gui.splash.SplashFrame;
import org.rockyroadshub.planner.core.utils.Utilities;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.1.2
 */
public final class IconLoader extends AbstractLoader<Icon>{
    private static final String[] EXTENSIONS = {"png"};
    private static final String   PATH       = "src/images/icons/default";
    
    private IconLoader() {}

    public static IconLoader getInstance() {
        return Holder.INSTANCE;
    }  

    private static class Holder {
        private static final IconLoader INSTANCE = new IconLoader();
    }

    @Override
    public void load() {
        Task task = new Task(SplashFrame.getInstance(), MemoryLoader.getInstance());
        task.execute();
    }
    
    private class Task extends AbstractTask<Void> {
        
        private Task(SplashFrame frame, AbstractLoader loader) {
            super(frame, loader);
        }

        @LogExceptions
        @Override
        protected Void doInBackground() throws Exception {
            publish("Loading icons...");
            int count = 0;
            setProgress(0);       
            Collection<File> icons = 
                    FileUtils.listFiles(new File(PATH), EXTENSIONS, false);
            int numberOfFiles = icons.size();
            for(File icon : icons) {
                Image img = ImageIO.read(icon);
                String name = FilenameUtils.removeExtension(icon.getName());
                count++;
                add(name, new ImageIcon(img));
                publish(PATH + "/" + icon.getName() + " loaded.");
                setProgress(Utilities.getPercent(count, numberOfFiles).intValue());
                debug();
            }     
            return null;
        }
    }
 }
