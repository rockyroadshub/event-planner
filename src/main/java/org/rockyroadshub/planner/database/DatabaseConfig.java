/*
 * The MIT License
 *
 * Copyright 2016 Arnell Christoper D. Dalid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Event Planner), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
import org.rockyroadshub.planner.lib.Initializable;
import org.rockyroadshub.planner.main.PlannerSystem;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class DatabaseConfig implements Initializable {
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
    
    @Override
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
        
        static {
            PlannerSystem.addToQueue(1, INSTANCE);
        }
    }
}
