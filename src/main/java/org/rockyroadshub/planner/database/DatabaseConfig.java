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
package org.rockyroadshub.planner.database;

import com.jcabi.aspects.LogExceptions;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.rockyroadshub.planner.lib.Globals;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class DatabaseConfig {
    private final XMLConfiguration config = new XMLConfiguration();
    
    private static final String NAME    = "name";
    private static final String TYPE    = "type";
    private static final String COLUMNS = "tables.table.columns.column(%d)[@%s]";
    
    public static final String TITLE       = "event";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION    = "location";
    
    private int totalColumns;
    
    private String columns;
    private String columnsN;
    private String columnsN0;
    private String tableName;
    private String databaseID;
    
    private final List<String> columnsNList = new ArrayList<>();
    private final List<String> tableColumns = new ArrayList<>();
    private final Map<String, String> columnMap = new HashMap<>();
    
    private DatabaseConfig() {}
    
    public final void initialize() {
        try {
            setup();
        }
        catch (ConfigurationException ex) {}
        
        totalColumns = config.configurationsAt("tables.table.columns.column").size();
        tableName    = config.configurationAt("tables.table").getString("name");
        columns      = initColumns();
        columnsN     = initColumnsN();
        columnsN0    = initColumnsN0();
        
        initTableColumns();
    }
    
    @LogExceptions
    private void setup() throws ConfigurationException {
        File file = new File(getClass().getResource(String.
                format(Globals.JAR_ROOT, "src/database.xml")).getFile());
        config.setFile(file);
        config.setDelimiterParsingDisabled(true);
        config.load();
    }
    
    private String initColumns() {
        StringBuilder bld = new StringBuilder();
        for(int i = 0; i < totalColumns; i++) {
            if(i == 0) databaseID = getAttribute(i, NAME);
            bld.append(getAttribute(i, NAME)).append(" ")
               .append(getAttribute(i, TYPE)).append(",");
        }
        bld.replace(bld.length()-1, bld.length(), "");
        return bld.toString();
    }
    
    private String initColumnsN() {
        StringBuilder bld = new StringBuilder();
        for(int i = 1; i < totalColumns; i++) {
            String name = getAttribute(i, NAME);
            bld.append(name).append(",");
            columnsNList.add(name);
        }
        bld.replace(bld.length()-1, bld.length(), "");
        return bld.toString();
    }
    
    private String initColumnsN0() {
        StringBuilder bld = new StringBuilder();
        for(int i = 1; i < totalColumns; i++) {
            bld.append("'%s',");
        }
        bld.replace(bld.length()-1, bld.length(), "");
        return bld.toString();
    }
    
    private void initTableColumns() {
        int size = config.configurationsAt("columns.column").size();
        for(int k = 0; k < size; k++) {
            String name = config.getString(format("columns.column(%d)[@%s]",k,"name"));
            String attr = config.getString(format("columns.column(%d)[@%s]",k,"attr"));
            columnMap.put(name, attr);
            tableColumns.add(name);
        }
//        EventsDisplay.getInstance().setupColumn(size);
    }
    
    private String getAttribute(int index, String attribute) {
        return config.getString(format(index, attribute));
    }
    
    private String format(int index, String attribute) {
        return String.format(COLUMNS, index, attribute);
    }   
       
    private String format(String exp, int index, String attribute) {
        return String.format(exp, index, attribute);
    }
    
    public String getDatabaseID() {
        return databaseID;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public String getColumns() {
        return columns;
    }
    
    public String getColumnsN() {
        return columnsN;
    }
    
    public String getColumnsN0() {
        return columnsN0;
    }
    
    public List<String> getColumnsNAsList() {
        return columnsNList;
    }
    
    public List<String> getTableColumns() {
        return tableColumns;
    }
    
    public Map<String, String> getColumnMap() {
        return columnMap;
    }
    
    public int getLength(String s) {
        return Integer.valueOf(config.configurationAt("properties.length").getString(s));
    }
    
    public static DatabaseConfig getInstance() {
        return Holder.INSTANCE;
    }
    
    private static final class Holder {
        private static final DatabaseConfig INSTANCE = new DatabaseConfig();
    }
}
