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

package org.rockyroadshub.planner.core.utils;

import com.jcabi.aspects.LogExceptions;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class Utilities {
    private Utilities(){}

    public static String formatDate(int year, int month, int day) {
        return String.format("%d-%02d-%02d", year, month, day);
    }
    
    public static String formatDate(String month, int day, int year) {
        return String.format("%s %d, %d", month, day, year);
    }
    
    public static String stamp(int limit) {
        StringBuilder bld = new StringBuilder("(%0");
        int digits = (int)(Math.log10(limit)+1);
        bld.append(digits).append("d/").append(limit).append(")");
        return bld.toString();
    }
          
    public static Double getPercent(int numerator, int denominator) {
        float n = numerator;
        float d = denominator;
        return new Double((n/d) * 100);
    }
    
    /**
     * Checks file if it exists or not.
     * <p>
     * If the checked file does not exist, this method will create a new copy from
     * the specified source path and saves it in the specified file path.
     * </p>
     * @param path file path
     * @param defaultPath source path
     * @return returns true if the file is newly created; returns false if
     *         otherwise
     * @throws IOException
     * @throws ConfigurationException
     * @throws URISyntaxException 
     */
    @LogExceptions
    public static boolean checkFile(String path, String defaultPath) 
            throws IOException, ConfigurationException, URISyntaxException 
    {
        File file = new File(path);
        if(!file.exists()) {
            FileUtils.copyInputStreamToFile(
                    Utilities.class.getResourceAsStream(defaultPath), file);
            return true;
        } 
        return false;
    }
}
