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
    
    public void fillRoomsJTable(JTable table) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT * FROM ROOM";

        try {
            ps = dbManager.getConnection().prepareStatement(selectQuery);
            rs = ps.executeQuery();

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);

            Object[] row;

            while (rs.next()) {
                row = new Object[4];

                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
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

    //create a function to add a new room
    public boolean addRoom(int number, int type, String phone) {

        PreparedStatement ps;
        ResultSet rs;
        String addQuery = "INSERT INTO ROOM (R_NUMBER, TYPE, PHONE, RESERVED) VALUES (?,?,?,?)";

        try {
            ps = dbManager.getConnection().prepareStatement(addQuery);

            ps.setInt(1, number);
            ps.setInt(2, type);
            ps.setString(3, phone);

            //when we add a new room
            //the reserved column would be set to no
            //the reserved coulumn mean if this room is free or not
            ps.setString(4, "No");

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean editRoom(int number, int type, String phone, String isReserved) {

        PreparedStatement ps;
        ResultSet rs;
        String updateQuery = "UPDATE ROOM SET TYPE = ?, PHONE = ?, RESERVED = ? WHERE R_NUMBER = ?";

        try {
            ps = dbManager.getConnection().prepareStatement(updateQuery);

            ps.setInt(1, type);
            ps.setString(2, phone);
            ps.setString(3, isReserved);
            ps.setInt(4, number);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean removeRoom(int roomNumber) {
        PreparedStatement ps;
        ResultSet rs;
        String deleteQuery = "DELETE FROM ROOM WHERE r_number = ?";

        try {
            ps = dbManager.getConnection().prepareStatement(deleteQuery);

            ps.setInt(1, roomNumber);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    // room type id, the price and the name of the room
    public String[] getTypeInfo(int clientId) {
    PreparedStatement ps;
    ResultSet rs;
    String selectQuery = "SELECT TYPE.ID, TYPE.PRICE, TYPE.LABEL " +
                         "FROM RESERVATIONS " +
                         "JOIN ROOM ON RESERVATIONS.ROOM_NUMBER = ROOM.R_NUMBER " +
                         "JOIN TYPE ON ROOM.TYPE = TYPE.ID " +
                         "WHERE RESERVATIONS.CLIENT_ID = ?";
    String[] roomTypeIdPriceAndLabel = new String[3];

    try {
        ps = dbManager.getConnection().prepareStatement(selectQuery);
        ps.setInt(1, clientId);
        rs = ps.executeQuery();

        if (rs.next()) {
            roomTypeIdPriceAndLabel[0] = rs.getString("ID");
            roomTypeIdPriceAndLabel[1] = rs.getString("PRICE");
            roomTypeIdPriceAndLabel[2] = rs.getString("LABEL");
        }
    } catch (SQLException ex) {
        Logger.getLogger(ROOMS.class.getName()).log(Level.SEVERE, null, ex);
    }
    return roomTypeIdPriceAndLabel;
    }

}