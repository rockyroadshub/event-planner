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
import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.rockyroadshub.planner.core.database.DatabaseControl;
import org.rockyroadshub.planner.core.database.Members;
import org.rockyroadshub.planner.core.database.Memory;
import org.rockyroadshub.planner.splash.SplashFrame;
import org.rockyroadshub.planner.utils.Utilities;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
public final class MemoryLoader extends AbstractLoader<Memory> {
    private final XMLConfiguration databaseProperties = new XMLConfiguration();
    
    private static final String MAPPER   = "[@mapper]";
    private static final String SCHEMA   = "[@schema]";
    private static final String NAME     = "[@name]";
    private static final String TYPE     = "[@type]";
    private static final String KEY      = "[@key]";
    private static final String INDEX    = "[@index]";
    private static final String ALT      = "[@alt]";
    
    
    private static final String TABLES   = "tables.table";
    private static final String COLUMNS  = "columns.column";
    private static final String DISPLAYS = "displays.display";
    
    private MemoryLoader() {}

    public static MemoryLoader getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final MemoryLoader INSTANCE = new MemoryLoader();
    }
    
    @Override
    public void load() {
        super.load();
        Task task = new Task(
                SplashFrame.getInstance(), 
                IconLoader.getInstance(),
                this);
        task.execute();
    }
    
    @Override
    public String getName() {
        return "memory";
    }
    
    private class Task extends AbstractTask<Void> {
        public Task(SplashFrame frame, AbstractLoader loader, AbstractLoader main) {
            super(frame, loader, main);
        }

        @LogExceptions
        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);
            publish("Loading database properties...");
            loadProperties();
            setProgress(50);
            publish("Database properties loaded.");
            publish("Checking memory...");
            List<HierarchicalConfiguration> tables 
                    = databaseProperties.configurationsAt(TABLES);
            for(int j = 0; j < tables.size(); j++) {     
                Members members = new Members();
                String mapper = tables.get(j).getString(MAPPER);
                String schema = tables.get(j).getString(SCHEMA);
                String name   = tables.get(j).getString(NAME);
                publish("Initializing Table: " + name + " at " + "Schema: " + schema);

                List<HierarchicalConfiguration> columns = 
                        tables.get(j).configurationsAt(COLUMNS);
                for(int i = 0; i < columns.size(); i++) {            
                    String column = columns.get(i).getString(NAME);
                    String type  = columns.get(i).getString(TYPE);
                    boolean key  = columns.get(i).getBoolean(KEY);
                    members.add(column, type, key);
                }

                List<HierarchicalConfiguration> displays = 
                        tables.get(j).configurationsAt(DISPLAYS);
                Integer[] disp = new Integer[displays.size()];
                for(int i = 0; i < displays.size(); i++) {
                    disp[i] = displays.get(i).getInt(INDEX);
                }

                for(int i = 0; i < displays.size(); i++) {
                    int index = displays.get(i).getInt(INDEX);
                    String alt = displays.get(i).getString(ALT);
                    members.setColumnAltText(index, alt);
                }

                members.setDisplayColumns(disp);
                members.pack();
                Memory memory = new Memory(members, schema, name);
                boolean isCreated = DatabaseControl.getInstance().create(memory);
                add(mapper, memory);
                publish("Memory: " + memory.getIdentifier() + 
                        ((isCreated) ? " created." : " loaded."));
                setProgress(Utilities.getPercent(j + 51, tables.size() + 50).intValue());
            }
            return null;
        }
        
        private void loadProperties() throws ConfigurationException {
            File file = FileLoader.getInstance().get("database");
            databaseProperties.setFile(file);
            databaseProperties.setDelimiterParsingDisabled(true);
            databaseProperties.load();
        }    
    }
 }
