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
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rockyroadshub.planner.lib.Globals;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class DatabaseConnection {
    private static final String PROTOCOL = "jdbc:derby:%s;%s=true";
    private Connection connection;

    private DatabaseConnection() {}
    
    private String getProtocol(String path, String cmd) {
        return String.format(PROTOCOL, path, cmd);
    }
                 
    public final void initialize() {
        try {
            setupDriver();
            setupConnection();
        } 
        catch (ClassNotFoundException | 
               InstantiationException | 
               IllegalAccessException | 
               SQLException ex) 
        {
            throw new RuntimeException(ex);
        }
    }
    
    @LogExceptions
    private void setupDriver() throws 
            ClassNotFoundException, InstantiationException, 
            IllegalAccessException 
    {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    }
   
    @LogExceptions
    private void setupConnection() throws SQLException {
        connection = DriverManager.getConnection(
                    getProtocol(Globals.DATABASE_ROOT, "create"));
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }
    
    private static class Holder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }
}
