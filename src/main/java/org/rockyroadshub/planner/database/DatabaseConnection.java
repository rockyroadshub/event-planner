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
import java.sql.DriverManager;
import java.sql.SQLException;
import org.rockyroadshub.planner.core.Globals;
import org.rockyroadshub.planner.core.Initializable;
import org.rockyroadshub.planner.main.PlannerSystem;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class DatabaseConnection implements Initializable {
    private static final String PROTOCOL = "jdbc:derby:%s;%s=true";
    private Connection connection;

    private DatabaseConnection() {}
                 
    @Override
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
                    setProtocol(Globals.DATABASE_ROOT, "create"));
    }   
        
    private String setProtocol(String path, String cmd) {
        return String.format(PROTOCOL, path, cmd);
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }
    
    private static class Holder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
        
        static {
            PlannerSystem.addToQueue(0, INSTANCE);
        }
    }
}
