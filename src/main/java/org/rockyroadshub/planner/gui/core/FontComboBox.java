package org.rockyroadshub.planner.gui.core;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

@SuppressWarnings("serial")
public class FontComboBox extends JComboBox {
    public static final GraphicsEnvironment GE = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
    public static final String[] FNT_FAMILY = 
            GE.getAvailableFontFamilyNames();
    
    private final ComboRenderer fontRenderer;
    
    public FontComboBox() {
        super(FNT_FAMILY);
        fontRenderer = new ComboRenderer(this);
        setRenderer(fontRenderer);
        addItemListener(fontListener);
    }
    
    private final ItemListener fontListener = (ItemEvent e) -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String fontName = getSelectedItem().toString();
            setFont(new Font(fontName, Font.PLAIN, 16));
        }
    };
    
    protected class ComboRenderer extends BasicComboBoxRenderer {
        protected JComboBox comboBox;
        final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
        private int row;

        public ComboRenderer(JComboBox fontsBox) {
            comboBox = fontsBox;
        }

        private void manItemInCombo() {
            if (comboBox.getItemCount() > 0) {
                Object comp = comboBox.getUI().getAccessibleChild(comboBox, 0);
                if ((comp instanceof JPopupMenu)) {
                    JList list = new JList(comboBox.getModel());
                    JPopupMenu popup = (JPopupMenu) comp;
                    JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
                    JViewport viewport = scrollPane.getViewport();
                    Rectangle rect = popup.getVisibleRect();
                    Point pt = viewport.getViewPosition();
                    row = list.locationToIndex(pt);
                    }
                }
            }

        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
        {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (list.getModel().getSize() > 0) {
                manItemInCombo();
            }
            JLabel renderer = (JLabel) defaultRenderer.
                    getListCellRendererComponent(list, value, row, isSelected, cellHasFocus);
            Object fntObj = value;
            String fontFamilyName = (String) fntObj;
            setFont(new Font(fontFamilyName, Font.PLAIN, 16));
            return this;
        }
    }
}