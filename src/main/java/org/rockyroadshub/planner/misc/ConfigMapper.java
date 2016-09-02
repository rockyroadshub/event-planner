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

package org.rockyroadshub.planner.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.rockyroadshub.planner.core.database.Data;
import org.rockyroadshub.planner.core.database.DataMapper;
import org.rockyroadshub.planner.core.database.DataMapperException;
import org.rockyroadshub.planner.core.database.DatabaseConnection;
import org.rockyroadshub.planner.core.database.DatabaseControl;
import org.rockyroadshub.planner.core.database.Members;
import org.rockyroadshub.planner.core.database.Memory;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class ConfigMapper extends DataMapper {
    private static final Members MEMBERS = new Members()
            .add("CONFIG_ID", "INT NOT NULL PRIMARY KEY GENERATED "
                + "ALWAYS AS IDENTITY", true)
            .add("NAME"  , "VARCHAR(20)", false)
            .add("OBJECT", "BLOB(10K)"   , false)
            .pack();
    
    private static final Memory MEMORY = new Memory(MEMBERS,"PLANNER","CONFIG");
    
    private static class Holder {
        private static final ConfigMapper INSTANCE = new ConfigMapper();
    }
    
    private ConfigMapper() {
        super(null);
    }

    public static ConfigMapper getInstance() {
        return Holder.INSTANCE;
    }
    
    @Override
    public Optional<Data> find(int id) {
//        DatabaseControl control = DatabaseControl.getInstance();
//        String command = String.format(getMembers().getSelectFormat(), id);
//        try {
//            control.execute(command);
//        } catch (SQLException ex) {
//            return Optional.empty();
//        }
        return null;
    }

    @Override
    public void insert(Data data) throws DataMapperException {
        Config config = (Config)data;
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        byte[] dat;
        
        try(PreparedStatement ps = 
                conn.prepareStatement(getMembers().getInsertFormat())) 
        {
            try(ByteArrayOutputStream bos = 
                    new ByteArrayOutputStream())
            {
                try(ObjectOutputStream oos = 
                        new ObjectOutputStream(bos)) 
                {
                    oos.writeObject(config);
                    dat = bos.toByteArray();
                    
                    ps.setString(1, config.getConfigName());
                    ps.setObject(2, dat);
                    ps.executeUpdate();
                }catch (IOException ex) { ex.printStackTrace(System.out); }
            }catch (IOException ex) { ex.printStackTrace(System.out); }
        }catch (SQLException ex) { ex.printStackTrace(System.out); }
    }
    
    public Config getConfig(String name) {
        DatabaseConnection conn0 = DatabaseConnection.getInstance();
        Connection conn = conn0.getConnection();
        
         try(PreparedStatement ps = conn.prepareStatement
            ("SELECT * FROM PLANNER.CONFIG WHERE NAME = ?")) 
        {  
            ps.setString(1, name);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    try(ByteArrayInputStream bais = 
                            new ByteArrayInputStream(rs.getBytes("OBJECT"))) 
                    {
                        try(ObjectInputStream ins = 
                                new ObjectInputStream(bais)) 
                        {
                            return (Config)ins.readObject();        
                        }
                    }
                }
            }catch (IOException | ClassNotFoundException ex) { ex.printStackTrace(System.out); }   
        } catch (SQLException ex) { ex.printStackTrace(System.out); }
        return null;
    }

    @Override
    public void update(Data data) throws DataMapperException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int id) throws DataMapperException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}