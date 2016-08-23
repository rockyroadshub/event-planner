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

package org.rockyroadshub.planner.core.database;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class Memory {

    private String schemaPattern;
    private String tableNamePattern;
    
    private final Members members;  
    private final String identifier;
    
    public Memory(Members members, String tableNamePattern) {
        this(members, "null", tableNamePattern);
    }
    
    public Memory(Members members, String schemaPattern, String tableNamePattern) {
        this.members = members;
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
        
        if(schemaPattern.equals("null")) {
            identifier = tableNamePattern;
        }
        else {
            identifier = schemaPattern + "." + tableNamePattern;
        }
        
        initializeFormats();
    }
    
    private void initializeFormats() {
        String create = members.getCreateFormat();
        String insert = members.getInsertFormat();
        String update = members.getUpdateFormat();
        String delete = members.getDeleteFormat();
        String select = members.getSelectFormat();
        
        create = create.replaceAll("@", identifier);
        insert = insert.replaceAll("@", identifier);
        update = update.replaceAll("@", identifier);
        delete = delete.replaceAll("@", identifier);
        select = select.replaceAll("@", identifier);
        
        members.setCreateFormat(create);
        members.setInsertFormat(insert);
        members.setUpdateFormat(update);
        members.setDeleteFormat(delete);
        members.setSelectFormat(select);
    }
    
    public Members getMembers() {
        return members;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public String getSchemaPattern() {
        return schemaPattern;
    }
    
    public String getTableNamePattern() {
        return tableNamePattern;
    }
    
    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }
    
    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }
}