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

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rockyroadshub.planner.core.dtb.DatabaseControl;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public final class EventMapper implements DataMapper {
    private static final String SCHEMA_NAME = "PLANNER";
    private static final String TABLE_NAME = "EVENTS";
    
    private static final String CREATE =
        "CREATE TABLE " + SCHEMA_NAME + "." + TABLE_NAME + " ("
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
        "INSERT INTO " + SCHEMA_NAME + "." + TABLE_NAME + " ("
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
    
    
    @Override
    public void create() throws DataMapperException {
        try {
            DatabaseControl control = DatabaseControl.getInstance();
            control.create(SCHEMA_NAME, TABLE_NAME, CREATE);
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
            control.insert(command);
        } 
        catch (SQLException ex) {
            throw new DataMapperException(ex);
        }
    }

    @Override
    public void update(Data data) throws DataMapperException {
        Event event = (Event)data;
    }

    @Override
    public void delete(Data data) throws DataMapperException {
        Event event = (Event)data;
    }
}
