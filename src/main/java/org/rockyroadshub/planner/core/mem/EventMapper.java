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

package org.rockyroadshub.planner.core.mem;

import org.rockyroadshub.planner.core.dtb.Data;
import org.rockyroadshub.planner.core.dtb.DataMapperException;
import org.rockyroadshub.planner.core.dtb.DataMapper;
import java.sql.SQLException;
import org.rockyroadshub.planner.core.dtb.DatabaseControl;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public final class EventMapper implements DataMapper {
    private EventMapper(){}
    
    private static class Holder {
        private static final EventMapper INSTANCE = new EventMapper();
    }
    
    public static EventMapper getInstance() {
        return Holder.INSTANCE;
    }
    
    private static final String CREATE =
        "CREATE TABLE " + Event.SCHEMA_NAME + "." + Event.TABLE_NAME + " ("
            + "EVENT_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
            + "EVENT VARCHAR(50),"
            + "DESCRIPTION VARCHAR(200),"
            + "LOCATION VARCHAR(200),"
            + "EVENT_DATE DATE,"
            + "EVENT_YEAR VARCHAR(4),"
            + "EVENT_MONTH VARCHAR(9),"
            + "EVENT_DAY VARCHAR(2),"
            + "EVENT_START TIME,"
            + "EVENT_END TIME)";
    
    private static final String INSERT =
        "INSERT INTO " + Event.SCHEMA_NAME + "." + Event.TABLE_NAME + " ("
            + "EVENT,"
            + "DESCRIPTION,"
            + "LOCATION,"
            + "EVENT_DATE,"
            + "EVENT_YEAR,"
            + "EVENT_MONTH,"
            + "EVENT_DAY,"
            + "EVENT_START,"
            + "EVENT_END) "
            + "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')";
    
    private static final String UPDATE_ONE = 
        "UPDATE " + Event.SCHEMA_NAME + "." + Event.TABLE_NAME 
            + " SET %s = '%s' WHERE EVENT_ID = %d";
    
    private static final String UPDATE_ALL = 
        "UPDATE " + Event.SCHEMA_NAME + "." + Event.TABLE_NAME 
            + " SET EVENT = '%s',"
            + " SET DESCRIPTION = '%s',"
            + " SET LOCATION = '%s',"
            + " SET EVENT_DATE = '%s',"
            + " SET EVENT_YEAR = '%s',"
            + " SET EVENT_MONTH = '%s',"
            + " SET EVENT_DAY = '%s',"
            + " SET EVENT_START = '%s',"
            + " SET EVENT_END = '%s'"
            + " WHERE EVENT_ID = %d";
    
    private static final String DELETE =
        "DELETE FROM " + Event.SCHEMA_NAME + "." + Event.TABLE_NAME
            + " WHERE EVENT_ID = %d";
    
    private static final String FIND =
        "SELECT * FROM " + Event.SCHEMA_NAME + "." + Event.TABLE_NAME
            + " WHERE EVENT_ID = %d";
    
    private static final String SELECT0 =
        "SELECT * FROM " + Event.SCHEMA_NAME + "." + Event.TABLE_NAME
            + " WHERE %s = '%s'";
    
    private static final String SELECT1 =
        "SELECT * FROM " + Event.SCHEMA_NAME + "." + Event.TABLE_NAME
            + " WHERE %s = '%s' %s %s = '%s'";
    
    @Override
    public void create(Data data) throws DataMapperException {
        try {
            DatabaseControl control = DatabaseControl.getInstance();
            control.create(data, CREATE);
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }    
    
    @Override
    public void insert(Data data) throws DataMapperException {
        Event event = (Event)data;
        DatabaseControl control = DatabaseControl.getInstance();
        String command = String.format(INSERT,
                event.getEvent(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                event.getYear(),
                event.getMonth(),
                event.getDay(),
                event.getStart(),
                event.getEnd());
        
        try {
            control.execute(command);
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }

    @Override
    public void update(int id, String column, String value) 
            throws DataMapperException 
    {
        DatabaseControl control = DatabaseControl.getInstance();
        String command = String.format(UPDATE_ONE,
                column, value, id);
        
        try {
            control.execute(command);
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }

    @Override
    public void update(int id, Data data) {
        Event event = (Event)data;
        DatabaseControl control = DatabaseControl.getInstance();
        String command = String.format(UPDATE_ALL,
                event.getEvent(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                event.getYear(),
                event.getMonth(),
                event.getDay(),
                event.getStart(),
                event.getEnd(),
                id);
        
        try {
            control.execute(command);
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }
    
    @Override
    public void delete(int id) throws DataMapperException {
        DatabaseControl control = DatabaseControl.getInstance();
        String command = String.format(DELETE, id);
        
        try {
            control.execute(command);
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }
    
    @Override
    public Event find(int id) throws DataMapperException {
        DatabaseControl control = DatabaseControl.getInstance();
        String command = String.format(FIND, id);
        
        try {
            Event event = new Event();
            String[] data = control.find(command, event)
                    .split(DatabaseControl.SEPARATOR);
            
            event.setEvent(data[0]);
            event.setDescription(data[1]);
            event.setLocation(data[2]);
            event.setDate(data[3]);
            event.setStart(data[7]);
            event.setEnd(data[8]);
            
            return event;
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }

    @Override
    public void select(String c, String v) 
            throws DataMapperException 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void select(
            String c1, String v1, 
            String c2, String v2, 
            String o) 
            
                throws DataMapperException 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
