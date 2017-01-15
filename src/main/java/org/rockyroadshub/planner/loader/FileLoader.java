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
import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.rockyroadshub.planner.splash.SplashFrame;
import org.rockyroadshub.planner.utils.Utilities;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
public final class FileLoader extends AbstractLoader<File> {     
    private FileLoader() {}

    public static FileLoader getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final FileLoader INSTANCE = new FileLoader();
    }
    
    private enum Files {
        PLANNER("prop/planner.properties","/org/rockyroadshub/planner/src/cfg/planner.properties"),
        DATABASE("prop/database.xml","/org/rockyroadshub/planner/src/cfg/database.xml");
        
        private final String path;
        private final String source;
        
        Files(String path, String source) {
            this.path = path;
            this.source = source;
        }
    }
       
    @Override
    public void load() {
        super.load();
        Task task = new Task(
                SplashFrame.getInstance(), 
                PropertyLoader.getInstance(),
                this);
        task.execute();
    }
    
    @Override
    public String getName() {
        return "file";
    }
    
    private class Task extends AbstractTask<Void> {
    
        private Task(SplashFrame frame, AbstractLoader loader, AbstractLoader main) {
            super(frame, loader, main);
        }
        
        @LogExceptions
        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);
            publish("Checking system files...");
            int max = Files.values().length;
            int count = 0;
            for(Files f : Files.values()) {
                String path = f.path;
                String source = f.source;
                boolean isNew = Utilities.checkFile(path, source);
                count++;
                File file = new File(path);
                add(FilenameUtils.removeExtension(file.getName()), file);
                publish((isNew) ? path + " created." : path + " loaded.");
                setProgress(Utilities.getPercent(count, max).intValue()); 
            }
            return null;
        }
    }
 }
