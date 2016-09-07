/*
 *  Copyright (c) 2016 Nokia. All rights reserved.
 *
 *  Revision History:
 *
 *  DATE/AUTHOR          COMMENT
 *  ---------------------------------------------------------------------
 *  2016年9月5日/junldai                            
 */
package tool;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * TODO:Write class description
 * @author <a HREF="mailto:yourMail@nsn.com">Your Name</a>
 *
 */
public class MyTableCellRender extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                    int row, int column )
    {
        Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
//        if( column == 4 )
//        {
        if( Client.isValidateTable )
        {
            if( ( boolean ) table.getValueAt( row, 3 ) && Client.isNotEnough( ( String ) table.getValueAt( row, 1 ) ) )
            {
//            if( column == 1 )
//            {
                c.setBackground( Color.RED );
//            }
            }
            else
            {
                c.setBackground( Color.WHITE );
            }

        }

//        }

        return c;
    }
}
