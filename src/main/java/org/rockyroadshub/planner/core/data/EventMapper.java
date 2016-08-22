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

import java.sql.SQLException;
import java.util.Optional;
import org.rockyroadshub.planner.core.dtb.DatabaseControl;
import org.rockyroadshub.planner.core.dtb.Data;
import org.rockyroadshub.planner.core.dtb.DataMapperException;
import org.rockyroadshub.planner.core.dtb.DataMapper;
import org.rockyroadshub.planner.core.dtb.Members;
import org.rockyroadshub.planner.core.dtb.Memory;

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
            .setDisplayColumns(1,2,5,8,9)
            .pack();
    
    private static final Memory MEMORY = new Memory(MEMBERS, "EVENTS");
    
    private EventMapper() {
        this.memory = MEMORY;
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
                    memory.getMembers().getSelectFormat(), 
                    id);
            
            String[] data = control.find(command, 
                    memory.getMembers().getTotalColumns())
                    .split(DatabaseControl.SEPARATOR);
            
            event.setID(id);
            event.setEvent(data[0]);
            event.setDescription(data[1]);
            event.setLocation(data[2]);
            event.setDate(data[3]);
            event.setStart(data[7]);
            event.setEnd(data[8]);
            
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
                memory.getMembers().getInsertFormat(),
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
                memory.getMembers().getUpdateFormat(),
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
                memory.getMembers().getDeleteFormat(), id);
        
        try {
            control.execute(command);
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }
}
