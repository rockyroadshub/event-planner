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

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * 
 * @author Arnell Christoper D. Dalid
 * @since 0.1.0
 */
public class TextLimiter extends DocumentFilter {
    int max;
 
    public TextLimiter(int max) {
        this.max = max;
    }
 
    @Override
    public void insertString(
            FilterBypass fb, int offs,
            String str, AttributeSet a)
        throws BadLocationException 
    {

        if ((fb.getDocument().getLength() + str.length()) <= max) {
            super.insertString(fb, offs, str, a);
        }
        else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
     
    @Override
    public void replace(
            FilterBypass fb, int offs,
            int length, String str, AttributeSet a)
        throws BadLocationException 
    {

        if ((fb.getDocument().getLength() + str.length()
             - length) <= max) {
            super.replace(fb, offs, length, str, a);
        }
        else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}