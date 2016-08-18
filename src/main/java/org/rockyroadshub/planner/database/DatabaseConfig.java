/*
 * The MIT License
 *
 * Copyright 2016 Arnell Christoper D. Dalid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Event Planner"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Event Planner.
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
    
    private static final String DATABASE_PROPERTIES = "src/database.xml";
            
    private static final String NAME  = "name";
    private static final String TYPE  = "type";
    private static final String ATTR  = "attr";
    private static final String MAIN  = "tables.table";
    private static final String PROP  = "tables.table.properties.length";
    private static final String CLMN0 = "tables.table.columns.column";
    private static final String CLMN1 = "tables.table.columns.column(%d)[@%s]";
    private static final String DSPL0 = "tables.table.displays.display";
    private static final String DSPL1 = "tables.table.displays.display(%d)[@%s]";
    
    public static final String TITLE       = "event";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION    = "location";
    
    private int totalColumns;
    
    private String columns;
    private String columnsN;
    private String columnsN0;
    private String tableName;
    private String keyName;
    
    private final List<String> columnsNList         = new ArrayList<>();
    private final List<String> displayColumns       = new ArrayList<>();
    private final Map<String, String> displayColMap = new HashMap<>();
    
    private DatabaseConfig() {}
    
    @Override
    public final void initialize() {
        try {
            setup();
        }
        catch (ConfigurationException ex) {}
        
        totalColumns = config.configurationsAt(CLMN0).size();
        tableName    = config.configurationAt(MAIN).getString(NAME);
        
        initColumns();
        initColumnsN();
        initColumnsN0();     
        initDisplayColumns();
    }
    
    @LogExceptions
    private void setup() throws ConfigurationException {
        File file = new File(getClass().getResource(String.
                format(Globals.JAR_ROOT, DATABASE_PROPERTIES)).getFile());
        config.setFile(file);
        config.setDelimiterParsingDisabled(true);
        config.load();
    }
    
    private void initColumns() {
        StringBuilder bld = new StringBuilder();
        for(int i = 0; i < totalColumns; i++) {
            String name = getAttribute(CLMN1,i,NAME);
            String type = getAttribute(CLMN1,i,TYPE);
            if(i == 0) { keyName = name; }
            bld.append(name).append(" ")
               .append(type).append(",");
        }
        bld.replace(bld.length()-1, bld.length(), "");
        columns = bld.toString();
    }
    
    private void initColumnsN() {
        StringBuilder bld = new StringBuilder();
        for(int i = 1; i < totalColumns; i++) {
            String name = getAttribute(CLMN1,i,NAME);
            bld.append(name).append(",");
            columnsNList.add(name);
        }
        bld.replace(bld.length()-1, bld.length(), "");
        columnsN =  bld.toString();
    }
    
    private void initColumnsN0() {
        StringBuilder bld = new StringBuilder();
        for(int i = 1; i < totalColumns; i++) {
            bld.append("'%s',");
        }
        bld.replace(bld.length()-1, bld.length(), "");
        columnsN0 = bld.toString();
    }
    
    private void initDisplayColumns() {
        int size = config.configurationsAt(DSPL0).size();
        for(int i = 0; i < size; i++) {
            String name = getAttribute(DSPL1,i,NAME);
            String attr = getAttribute(DSPL1,i,ATTR);
            displayColMap.put(name, attr);
            displayColumns.add(name);
        }
    }
    
    private String getAttribute(String exp, int idx, String attr) {
        String format = String.format(exp, idx, attr);
        return config.getString(format);
    }
    
    public String getKeyName() {
        return keyName;
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
    
    public List<String> getDisplayColumns() {
        return displayColumns;
    }
    
    public Map<String, String> getDisplayColMap() {
        return displayColMap;
    }
    
    public int getSize(String size) {
        return Integer.valueOf(
                config.configurationAt(PROP).getString(size));
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
