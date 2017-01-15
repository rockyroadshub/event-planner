/*
 * Copyright 2017 Arnell Christoper D. Dalid.
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

package org.rockyroadshub.planner.data;

import com.jcabi.aspects.LogExceptions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import org.rockyroadshub.planner.core.database.Data;
import org.rockyroadshub.planner.core.database.DataMapper;
import org.rockyroadshub.planner.core.database.DatabaseConnection;
import org.rockyroadshub.planner.loader.MemoryLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
public final class AlarmMapper extends DataMapper {

    private AlarmMapper() {
        super(MemoryLoader.getInstance().get("AlarmMapper"));
    }
    
    private static final class Holder {
        private static final AlarmMapper INSTANCE = new AlarmMapper();
    }
    
    public static AlarmMapper getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Optional<Data> find(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @LogExceptions
    @Override
    public void insert(Data data) throws SQLException {
        Alarm alarm = (Alarm)data;
        Connection conn = DatabaseConnection.getConnection();
        
        try(PreparedStatement stmt = 
                conn.prepareStatement(getMembers().getInsertFormat())) 
        {
            stmt.setString(1, alarm.getAlarm());
            stmt.setString(2, alarm.getDescription());
            stmt.setString(3, alarm.getTime());
            stmt.setString(4, alarm.getDays());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Data data) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
