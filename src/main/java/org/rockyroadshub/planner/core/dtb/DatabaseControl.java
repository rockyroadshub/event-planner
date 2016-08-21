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

package org.rockyroadshub.planner.core.dtb;

import com.jcabi.aspects.LogExceptions;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.rockyroadshub.planner.database.DatabaseConnection;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public final class DatabaseControl {
    public static final String SEPARATOR = ";";
    
    private DatabaseControl() {}
    
    private static final class Holder {
        private static final DatabaseControl INSTANCE = new DatabaseControl();
    }

    public static DatabaseControl getInstance() {  
        return Holder.INSTANCE;
    }  
    
    @LogExceptions
    public void create(Memory memory) throws SQLException {
        String schemaPattern = memory.getSchemaPattern();
        String tableNamePattern = memory.getTableNamePattern();
        DatabaseConnection connection = DatabaseConnection.getInstance();
        Connection connection0 = connection.getConnection();
        
        if(!exists(connection0, schemaPattern, tableNamePattern)) {
            try(Statement command = connection0.createStatement()) {
                command.executeUpdate(memory.getMembers().getCreateFormat());
            }
        }
    }
    
    @LogExceptions
    private boolean exists(Connection connection, 
            String schemaPattern,
            String tableNamePattern) 
            
                throws SQLException 
    {
        DatabaseMetaData dbmd = connection.getMetaData();
        try(ResultSet rs = dbmd.getTables(
                null, schemaPattern, tableNamePattern, null)) 
        {
            return rs.next();
        }
    }
    
    @LogExceptions
    public void execute(String SQLCommand) throws SQLException {
        DatabaseConnection connection = DatabaseConnection.getInstance();
        Connection connection0 = connection.getConnection();
        try(Statement stmt = connection0.createStatement()) {
            stmt.executeUpdate(SQLCommand);
        }
    }
    
    @LogExceptions
    public String find(String SQLCommand, int totalColumns) 
            throws SQLException 
    {
        DatabaseConnection connection = DatabaseConnection.getInstance();
        Connection connection0 = connection.getConnection();
        StringBuilder builder = new StringBuilder();
        
        try(Statement stmt = connection0.createStatement()) {
            try(ResultSet rs = stmt.executeQuery(SQLCommand)) {
                if(rs.next()) {
                    for(int i = 1; i < totalColumns + 1; i++) {
                        builder.append(rs.getString(i)).append(SEPARATOR);
                    }
                    builder.replace(builder.length()-1, builder.length(), "");
                }           
            }
        }
        return builder.toString();
    }
    
    @LogExceptions
    public int getRowCount(String SQLCommand) throws SQLException {
        int i = 0;
        DatabaseConnection connection = DatabaseConnection.getInstance();
        Connection connection0 = connection.getConnection();
        try(Statement stmt = connection0.createStatement()) {
            try(ResultSet rs = stmt.executeQuery(SQLCommand)) {
                while(rs.next()) {
                    i++;
                }
            }
        }
        return i;
    }
}