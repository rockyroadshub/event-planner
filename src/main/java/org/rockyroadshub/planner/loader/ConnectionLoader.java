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
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ExecutionException;
import org.rockyroadshub.planner.core.database.DatabaseConnection;
import org.rockyroadshub.planner.splash.SplashFrame;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
public final class ConnectionLoader extends AbstractLoader<Void> {
    private static final String PROTOCOL = "jdbc:derby:bin/mem;create=true";
    
    private ConnectionLoader() {}
    
    private static final class Holder {
        private static final ConnectionLoader INSTANCE = new ConnectionLoader();
    }

    public static ConnectionLoader getInstance() {  
        return Holder.INSTANCE;
    }  
    
    @Override
    public void load() {
        super.load();
        Task task = new Task(
                SplashFrame.getInstance(), 
                FileLoader.getInstance(),
                this);
        task.execute();
    }
    
    @Override
    public String getName() {
        return "connection";
    }
    
    private void setupDriver() throws 
            ClassNotFoundException, InstantiationException, 
            IllegalAccessException 
    {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    }
    
    private class Task extends AbstractTask<Connection> {

        private Task(SplashFrame frame, AbstractLoader loader, AbstractLoader main) {
            super(frame, loader, main);
        }

        @LogExceptions
        @Override
        protected Connection doInBackground() throws Exception {
            Connection connection;
            
            setProgress(0);
            publish("Setting up database driver...");
            setupDriver();
            publish("Database driver has been loaded.");
            setProgress(50);
            
            publish("Checking database...");
            boolean isNew = !(new File("bin/mem").exists());
            publish("Database is" + ((isNew) ? " not " : " ") + "present.");
            publish(((isNew) ? "Creating " : "Loading ") + "database...");
            connection = DriverManager.getConnection(PROTOCOL);
            publish("Database has been" + ((isNew) ? " created." : " loaded."));
            setProgress(100);
            return connection;
        }
        
        @Override
        protected void done() {
            try {
                DatabaseConnection.setConnection(get());
            } 
            catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace(System.out);
            }
            super.done();
        }
    }
}
