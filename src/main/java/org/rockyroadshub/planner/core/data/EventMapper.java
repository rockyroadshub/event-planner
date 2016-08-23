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

package org.rockyroadshub.planner.core.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.rockyroadshub.planner.core.database.DatabaseControl;
import org.rockyroadshub.planner.core.database.Data;
import org.rockyroadshub.planner.core.database.DataMapperException;
import org.rockyroadshub.planner.core.database.DataMapper;
import org.rockyroadshub.planner.core.database.Members;
import org.rockyroadshub.planner.core.database.Memory;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class EventMapper extends DataMapper {
    private static final Members MEMBERS = new Members()
            .add("EVENT_ID", "INT NOT NULL PRIMARY KEY GENERATED "
                + "ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)", true)
            .add("EVENT"      ,"VARCHAR(50)" ,false)
            .add("DESCRIPTION","VARCHAR(500)",false)
            .add("LOCATION"   ,"VARCHAR(250)",false)
            .add("EVENT_DATE" ,"DATE"        ,false)
            .add("EVENT_YEAR" ,"VARCHAR(10)" ,false)
            .add("EVENT_MONTH","VARCHAR(20)" ,false)
            .add("EVENT_DAY"  ,"VARCHAR(10)" ,false)
            .add("EVENT_START","TIME"        ,false)
            .add("EVENT_END"  ,"TIME"        ,false)
            .setDisplayColumns(0,1,4,8,9)
            .pack();
    
    private static final String[] ALTTEXTS = {"ID", "Title", "Date", "Start", "End"};  
    private static final Memory MEMORY = new Memory(MEMBERS, "EVENTS");
    
    private final List<Integer> dayList = new ArrayList<>();
    private final List<Event> evtList = new ArrayList<>();
    
    private EventMapper() {
        setMemory(MEMORY);
        setDisplayAltTexts(ALTTEXTS);
    }
    
    private static final class Holder {
        private static final EventMapper INSTANCE = new EventMapper();
    }
    
    public static EventMapper getInstance() {
        return Holder.INSTANCE;
    }
    
    @Override
    public Optional<Data> find(int id) {
        try {
            DatabaseControl control = DatabaseControl.getInstance();
            Event event = new Event();
            String command = String.format(
                    getMembers().getSelectFormat(), 
                    id);
            
            String[] data = control.find(command, 
                    getMembers().getTotalColumns());
            
            event.setID(Integer.parseInt(data[0]));
            event.setEvent(data[1]);
            event.setDescription(data[2]);
            event.setLocation(data[3]);
            event.setDate(data[4]);
            event.setStart(data[8]);
            event.setEnd(data[9]);
            
            return Optional.of(event);
            
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }

    /**
     * Inserts a new event data into memory
     * @param data data to be inserted
     * @throws DataMapperException 
     */
    @Override
    public void insert(Data data) throws DataMapperException {
        Event event = (Event)data;
        DatabaseControl control = DatabaseControl.getInstance();
        String command = String.format(
                getMembers().getInsertFormat(),
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

    /**
     * Updates the selected event from the memory
     * @param data data to be used for updating an event
     * @throws DataMapperException 
     */
    @Override
    public void update(Data data) throws DataMapperException {
        Event event = (Event)data;
        DatabaseControl control = DatabaseControl.getInstance();
        String command = String.format(
                getMembers().getUpdateFormat(),
                event.getEvent(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                event.getYear(),
                event.getMonth(),
                event.getDay(),
                event.getStart(),
                event.getEnd(),
                event.getID());
        
        try {
            control.execute(command);
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }

    /**
     * Deletes an event data
     * @param id primary/unique key of the data
     * @throws DataMapperException 
     */
    @Override
    public void delete(int id) throws DataMapperException {
        DatabaseControl control = DatabaseControl.getInstance();
        String command = String.format(
                getMembers().getDeleteFormat(), id);
        
        try {
            control.execute(command);
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }
    
    
    /**
     * Gets all events registered in a given date
     * @param date date of the event
     * @return list of dates
     */
    public List<Event> getEvents(String date) {
        org.rockyroadshub.planner.database.DatabaseConnection conn0 = 
        org.rockyroadshub.planner.database.DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        
        try(PreparedStatement stmt = 
            conn.prepareStatement("SELECT * FROM EVENTS WHERE EVENT_DATE = ?"))
        {
            stmt.setString(1, date);
            evtList.clear();
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    Event event = new Event();
                    event.setID(rs.getInt("EVENT_ID"));
                    event.setDescription(rs.getString("DESCRIPTION"));
                    event.setLocation(rs.getString("LOCATION"));
                    event.setDate(rs.getString("EVENT_DATE"));
                    event.setStart(rs.getString("EVENT_START"));
                    event.setEnd(rs.getString("EVENT_END"));
                    evtList.add(event);
                }
            }
            catch (SQLException ex) {
                throw new DataMapperException(ex);
            }
            
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
        
        return evtList;
    }
    
    /**
     * Gets all the dates with a registered event with a given month and date
     * @param month month of the event
     * @param year year of the event
     * @return registered days with an event on a list
     */
    public List<Integer> getRegisteredDays(String month, String year) {
        org.rockyroadshub.planner.database.DatabaseConnection conn0 = 
        org.rockyroadshub.planner.database.DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        try(PreparedStatement stmt = 
            conn.prepareStatement("SELECT * FROM EVENTS WHERE EVENT_MONTH = ? AND EVENT_YEAR = ?"))
        {
            stmt.setString(1, month);
            stmt.setString(2, year); 
            dayList.clear();
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    dayList.add(rs.getInt("EVENT_DAY"));
                }
            }
            catch (SQLException ex) {
                throw new DataMapperException(ex);
            }
            
        }
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
        
        return dayList;
    }
    
    /**
     * Deletes all the registered events in a given month and year
     * @param month month of the event
     * @param year year of the event
     */
    public void deleteAll(String month, String year) {
        org.rockyroadshub.planner.database.DatabaseConnection conn0 = 
        org.rockyroadshub.planner.database.DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        try(PreparedStatement stmt = 
            conn.prepareStatement("SELECT * FROM EVENTS WHERE EVENT_MONTH = ? AND EVENT_YEAR = ?"))
        {
            stmt.setString(1, month);
            stmt.setString(2, year); 
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    delete(rs.getInt("EVENT_ID"));
                }
            }
            catch (SQLException ex) {
                throw new DataMapperException(ex);
            }
            
        }
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }
}
