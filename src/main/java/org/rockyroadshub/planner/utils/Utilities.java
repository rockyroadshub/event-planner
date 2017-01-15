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

package org.rockyroadshub.planner.utils;

import com.jcabi.aspects.LogExceptions;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.rockyroadshub.planner.gui.panes.calendar.CalendarPane;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
public final class Utilities {
    private Utilities(){}
    
    public static final Pattern DATE_FORMAT = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");

    public static String formatDate(int year, int month, int day) {
        return String.format("%d-%02d-%02d", year, month, day);
    }
    
    public static String formatDate(String month, int day, int year) {
        return String.format("%s %d, %d", month, day, year);
    }
    
    public static String formatDate(String date) {
        Matcher m = DATE_FORMAT.matcher(date);
        if(m.matches()) {
            int month = Integer.valueOf(m.group(2));
            int day   = Integer.valueOf(m.group(3));
            int year  = Integer.valueOf(m.group(1));
            return formatDate(Utilities.formatDate(CalendarPane.MONTHS[month-1],day,year));
        }
        return "";
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
    
    /**
     * Gets an image as {@code BufferedImage} object from the jar file.
     * @param jarPath path of the image inside the jar file(e.g. 
     *                /org/rockyrodshub/planner/src/img/Frame.png)
     * @return buffered image based on the specified jar path
     * @throws IOException 
     */
    @LogExceptions
    public static BufferedImage getBufferedImage(String jarPath) 
            throws IOException 
    {
        try(InputStream in = Utilities.class.getResourceAsStream(jarPath)) {
            return ImageIO.read(in);
        }
    }
    
    /**
     * 
     * @param jarPath
     * @return
     * @throws IOException
     * @throws FontFormatException 
     */
    @LogExceptions
    public static Font getFont(String jarPath) throws IOException, FontFormatException {
        try (InputStream in = Utilities.class.getResourceAsStream(jarPath))
        {
            return Font.createFont(Font.TRUETYPE_FONT, in);
        } 
    }
}
