package org.rockyroadshub.planner.core;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TextLimiter extends DocumentFilter {
    int max;
 
    public TextLimiter(int max) {
        this.max = max;
    }
 
    @Override
    public void insertString(FilterBypass fb, int offs,
                             String str, AttributeSet a)
        throws BadLocationException {

        if ((fb.getDocument().getLength() + str.length()) <= max)
            super.insertString(fb, offs, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }
     
    @Override
    public void replace(FilterBypass fb, int offs,
                        int length, 
                        String str, AttributeSet a)
        throws BadLocationException {

        if ((fb.getDocument().getLength() + str.length()
             - length) <= max)
            super.replace(fb, offs, length, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }
}