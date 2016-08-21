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

package org.rockyroadshub.planner.core.dtb;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class Members {
    
    private final StringBuilder createFormat0 = new StringBuilder("CREATE TABLE %s (");
    
    public List<String> columns = new ArrayList<>();
    
    private String createFormat;
    private String insertFormat;
    private String updateFormat;
    private String deleteFormat;
    private String mainKey;
    
    private boolean keyExists = false;
    
    public Members() {}
    
    public Members add(String column, String type, boolean isMainKey) {   
        createFormat0.append(column)
                     .append(" ")
                     .append(type)
                     .append(",");
        
        if(!isMainKey) {
            columns.add(column);
        }
        else if(!keyExists) {
            mainKey = column;
            keyExists = true;
        }
        
        return this;
    }
    
    public Members pack() {
        createFormat0.replace(createFormat0.length()-1, createFormat0.length(), ")");
        createFormat = createFormat0.toString();
        
        StringBuilder insertColumn  = new StringBuilder("INSERT INTO %s (");
        StringBuilder insertValue   = new StringBuilder("VALUES (");
        StringBuilder updateFormat0 = new StringBuilder("UPDATE %s");
        String deleteFormat0 = "DELETE FROM %s WHERE " + mainKey + " = %d";
        
        columns.stream().map((column) -> {
            insertColumn.append(column).append(",");
            return column;
        }).forEach((column) -> {
            insertValue.append("'%s',");
            updateFormat0.append(" SET ")
                         .append(column)
                         .append(" = '%s',");
        });
        
        insertColumn.replace(insertColumn.length()-1, insertColumn.length(), ") ");
        insertValue.replace(insertValue.length()-1, insertValue.length(), ")"); 
        updateFormat0.replace(updateFormat0.length()-1, updateFormat0.length(), " ")
                     .append("WHERE ").append(mainKey).append(" = %d"); 
        
        
        insertFormat = insertColumn.toString() + insertValue.toString();
        updateFormat = updateFormat0.toString();
        deleteFormat = deleteFormat0;
        
        return this;
    }
    
    public String getCreateFormat() {
        return createFormat;
    }
    
    public void setCreateFormat(String createFormat) {
        this.createFormat = createFormat;
    }
    
    public String getInsertFormat() {
        return insertFormat;
    }
    
    public void setInsertFormat(String insertFormat) {
        this.insertFormat = insertFormat;
    }
    
    public String getUpdateFormat() {
        return updateFormat;
    }
    
    public void setUpdateFormat(String updateFormat) {
        this.updateFormat = updateFormat;
    }
    
    public String getDeleteFormat() {
        return deleteFormat;
    }
    
    public void setDeleteFormat(String deleteFormat) {
        this.deleteFormat = deleteFormat;
    }
    
    public int getTotalColumns() {
        return columns.size();
    }
}
