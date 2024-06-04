/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bedrock_and_Breakfast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ariannemasinading
 */
public class ROOMS {
    
    SimpleDBManager dbManager = new SimpleDBManager();
    
    //create a function to display all room types in jtable
    public void fillRooms_TYPE_JTable(JTable table) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT * FROM TYPE";

        try {
            ps = dbManager.getConnection().prepareStatement(selectQuery);
            rs = ps.executeQuery();

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);

            Object[] row;

            while (rs.next()) {
                row = new Object[3];
               
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                tableModel.addRow(row);
            }
            tableModel.fireTableDataChanged();
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //create a function to fill a combo box with the rooms type id
    
    public void fillRooms_TYPE_JComboBox(JComboBox combobox) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT * FROM TYPE";

        try {
            ps = dbManager.getConnection().prepareStatement(selectQuery);
            rs = ps.executeQuery();
           
            while (rs.next()) {
              
                combobox.addItem(rs.getInt(1));
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
