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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.rockyroadshub.planner.core.Initializable;
import org.rockyroadshub.planner.main.PlannerSystem;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class DatabaseControl implements Initializable {
    private static final String CREATE = "CREATE TABLE %s (%s)";
    private static final String INSERT = "INSERT INTO %s (%s) VALUES (%s)";
        
    private Connection connection;
    private String columns;
    private String columnsN;
    private String columnsN0;
    private String tableName;
    private String keyName;
    private List<String> columnsNList = new ArrayList<>();
    private final List<String> dayList = new ArrayList<>();
    private String[] dataList;
    
    private String insertFormat;
    private String createFormat;
    private String updateFormat;
    private String deleteFormat;
    private String selectFormat0;  
    private String selectFormat1;
    private String selectFormat2;
    
    private DatabaseControl() {}
    
    @Override
    public final void initialize() {
        DatabaseConfig config = DatabaseConfig.getInstance();
        connection    = DatabaseConnection.getInstance().getConnection();
        columns       = config.getColumns();
        columnsN      = config.getColumnsN();
        columnsN0     = config.getColumnsN0();
        columnsNList  = config.getColumnsNAsList();
        tableName     = config.getTableName();
        keyName       = config.getKeyName();
        dataList      = new String[columnsNList.size()];
        
        createFormat  = String.format(CREATE, tableName, columns);
        insertFormat  = String.format(INSERT, tableName, columnsN, columnsN0);
        updateFormat  = prepare("UPDATE ", " SET %s = '%s' WHERE ", " = %d");  
        deleteFormat  = prepare("DELETE FROM ", " WHERE ", " = %d");
        selectFormat0 = prepare("SELECT * FROM ", " WHERE ", " = %d");
        selectFormat1 = prepare("SELECT * FROM ", " WHERE ", "%s ", "= '%s'");
        selectFormat2 = prepare("SELECT * FROM ", " WHERE ", "%s = '%s' %s ", "%s = '%s'");
        
        try {
            build();
        }catch (SQLException ex) {}
    }
    
    @LogExceptions
    private void build() throws SQLException {
        if(!exists()) {
            create();
        }
    }
    
    @LogExceptions
    private void create() throws SQLException {
        try(Statement command = connection.createStatement()) {
            command.executeUpdate(createFormat);
        }
    }
    
    @LogExceptions
    private boolean exists() throws SQLException {
        DatabaseMetaData dbmd = connection.getMetaData();
        try(ResultSet rs = dbmd.getTables(null, null, tableName, null)) {
            return rs.next();
        }
    }
    
    /**
     * 
     * @param a EVENT
     * @param b DESCRIPTION
     * @param c LOCATION
     * @param d EVENT_DATE
     * @param e EVENT_YEAR
     * @param f EVENT_MONTH
     * @param g EVENT_DAY
     * @param h EVENT_START
     * @param i EVENT_END
     * @throws SQLException 
     */
    @LogExceptions
    public void insert(
         String a, String b, String c, String d, String e, 
         String f, String g, String h, String i) 
                throws SQLException 
    {
        try(Statement stmt = connection.createStatement()) {
            String statement = 
                    String.format(insertFormat,a,b,c,d,e,f,g,h,i);
            stmt.executeUpdate(statement);
        }
    }
    
    @LogExceptions
    public void update(int id, Object... args) throws SQLException {
        int k = 0;
        for(String col : columnsNList) {
            try(Statement stmt = connection.createStatement()) {
                String j = String.valueOf(args[k]);
                String statement = String.format(updateFormat,col,j,id);
                stmt.executeUpdate(statement);
                k++;
            }
        }
    }
    
    /**
     * 
     * @param id EVENT_ID
     * @throws SQLException 
     */
    @LogExceptions
    public void delete(int id) throws SQLException {
        try(Statement stmt = connection.createStatement()) {
            String statement = String.format(deleteFormat, id);
            stmt.executeUpdate(statement);
        }
    }
    
    /**
     * 
     * @param id EVENT_ID
     * @return 
     * @throws SQLException 
     */
    @LogExceptions
    public Object[] select(int id) throws SQLException {
        try(Statement stmt = connection.createStatement()) {
            String statement = String.format(selectFormat0,id);
            try(ResultSet rs = stmt.executeQuery(statement)) {
                while(rs.next()) {
                    for(int i = 0; i < columnsNList.size(); i++) {
                        dataList[i] = rs.getString(columnsNList.get(i));
                    }
                }
            }
        }
        return dataList;
    }

    /**
     * 
     * @param c0 Column 1
     * @param v0 Value 1
     * @param c1 Column 2
     * @param v1 Value 2
     * @param o Operator
     * @param d Data (column label of the data)
     * @return List of Data
     * @throws SQLException 
     */
    @LogExceptions
    public List<String> select(String c0, String v0, String c1, 
                               String v1, String o , String d) 
            throws SQLException 
    {
        dayList.clear();
        try(Statement stmt = connection.createStatement()) {
            String statement = String.format(selectFormat2,c0,v0,o,c1,v1);
            try(ResultSet rs = stmt.executeQuery(statement)) {
                while(rs.next()) {
                    dayList.add(rs.getString(d));
                }
            }
        }
        finally {
            return dayList;
        }
    }
    
    private String prepare(String a, String b, String d) {
        return prepare(a,b,keyName,d);
    }
    
    private String prepare(String a, String b, String c, String d) {
        StringBuilder bld = new StringBuilder(a);
            bld.append(tableName).append(b)
                   .append(c).append(d);
        return bld.toString();
    }
    
    public static DatabaseControl getInstance() {
        return Holder.INSTANCE;
    }
    
    private static final class Holder {
        private static final DatabaseControl INSTANCE = new DatabaseControl();
        
        static {
            PlannerSystem.addToQueue(2, INSTANCE);
        }
    }
}
