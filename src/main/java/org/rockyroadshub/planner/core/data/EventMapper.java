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
import org.rockyroadshub.planner.core.database.DatabaseConnection;
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
    
    private static final String SELECT_EVENT_DATE = 
            "SELECT * FROM EVENTS WHERE EVENT_DATE = ?";
    
    private static final String SELECT_EVENT_MONTH_YEAR = 
            "SELECT * FROM EVENTS WHERE EVENT_MONTH = ? AND EVENT_YEAR = ?";
    
    private EventMapper() {
        initialize();
    }
    
    private static final class Holder {
        private static final EventMapper INSTANCE = new EventMapper();
    }
    
    public static EventMapper getInstance() {
        return Holder.INSTANCE;
    }
    
    private void initialize() {
        try {
            DatabaseControl.getInstance().create(MEMORY);
        } 
        catch (SQLException ex) {}
        setMemory(MEMORY);
        setDisplayAltTexts(ALTTEXTS);
    }
    
    /**
     * Finds an event from a given id
     * @param id primary/main key of the event
     * @return returns an event as in "Optional" container
     * @see java.util.Optional
     */
    @Override
    public Optional<Data> find(int id) {
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        
        try(PreparedStatement stmt = 
                conn.prepareStatement(getMembers().getSelectFormat())) 
        {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    Event event = new Event();
                    event.setID(rs.getInt(1));
                    event.setEvent(rs.getString(2));
                    event.setDescription(rs.getString(3));
                    event.setLocation(rs.getString(4));
                    event.setDate(rs.getString(5));
                    event.setStart(rs.getString(9));
                    event.setEnd(rs.getString(10));
                    return Optional.of(event);
                }
                else 
                    return Optional.empty();
            }
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
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        
        try(PreparedStatement stmt = 
                conn.prepareStatement(getMembers().getInsertFormat())) 
        {
            stmt.setString(1, event.getEvent());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getLocation());
            stmt.setString(4, event.getDate());
            stmt.setString(5, event.getYear());
            stmt.setString(6, event.getMonth());
            stmt.setString(7, event.getDay());
            stmt.setString(8, event.getStart());
            stmt.setString(9, event.getEnd());
            stmt.executeUpdate();
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
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        
        try(PreparedStatement stmt = 
                conn.prepareStatement(getMembers().getUpdateFormat())) 
        {
            stmt.setString(1, event.getEvent());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getLocation());
            stmt.setString(4, event.getDate());
            stmt.setString(5, event.getYear());
            stmt.setString(6, event.getMonth());
            stmt.setString(7, event.getDay());
            stmt.setString(8, event.getStart());
            stmt.setString(9, event.getEnd());
            stmt.setInt(10, event.getID());
            stmt.executeUpdate();
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
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        
        try(PreparedStatement stmt = 
                conn.prepareStatement(getMembers().getDeleteFormat())) 
        {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }
    
    /**
     * Gets the number of events in a given date
     * @param date date parameter
     * @return number of events
     */
    public int getNumberOfEvents(String date) {
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        int i = 0;
        try(PreparedStatement stmt = 
                conn.prepareStatement(SELECT_EVENT_DATE)) 
        {
            stmt.setString(1, date);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    i++;
                }
            }
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
        return i;
    }
    
    /**
     * Gets the number of events in a given month and year
     * @param month month parameter
     * @param year year parameter
     * @return number of events
     */
    public int getNumberOfEvents(String month, String year) {
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        int i = 0;
        try(PreparedStatement stmt = 
                conn.prepareStatement(SELECT_EVENT_MONTH_YEAR)) 
        {
            stmt.setString(1, month);
            stmt.setString(2, year);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    i++;
                }
            }
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
        return i;
    }
    
    /**
     * Gets all events registered in a given date
     * @param date date of the event
     * @return list of dates
     */
    public List<Event> getEvents(String date) {
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        
        try(PreparedStatement stmt = 
            conn.prepareStatement(SELECT_EVENT_DATE))
        {
            stmt.setString(1, date);
            evtList.clear();
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    Event event = new Event();
                    event.setID(rs.getInt(1));
                    event.setEvent(rs.getString(2));
                    event.setDescription(rs.getString(3));
                    event.setLocation(rs.getString(4));
                    event.setDate(rs.getString(5));
                    event.setStart(rs.getString(9));
                    event.setEnd(rs.getString(10));
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
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        try(PreparedStatement stmt = 
            conn.prepareStatement(SELECT_EVENT_MONTH_YEAR))
        {
            stmt.setString(1, month);
            stmt.setString(2, year); 
            dayList.clear();
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    dayList.add(rs.getInt(8));
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
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        try(PreparedStatement stmt = 
            conn.prepareStatement(SELECT_EVENT_MONTH_YEAR))
        {
            stmt.setString(1, month);
            stmt.setString(2, year); 
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    delete(rs.getInt(1));
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
