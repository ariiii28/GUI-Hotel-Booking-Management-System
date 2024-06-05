/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bedrock_and_Breakfast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ariannemasinading
 */
public class RESERVATION {

    java.util.Date utilDate = new java.util.Date();
    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

    SimpleDBManager dbManager = new SimpleDBManager();
    ROOMS room = new ROOMS();

    public void fillReservationsJTable(JTable table) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT * FROM RESERVATIONS";

        try {
            ps = dbManager.getConnection().prepareStatement(selectQuery);
            rs = ps.executeQuery();

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);

            Object[] row;

            while (rs.next()) {

                row = new Object[5];

                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getInt(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                tableModel.addRow(row);
            }
            tableModel.fireTableDataChanged();
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean addReservation(int client_id, int room_number, String date_in, String date_out) {

        PreparedStatement ps;
        ResultSet rs;
        String addQuery = "INSERT INTO RESERVATIONS (CLIENT_ID, ROOM_NUMBER, DATE_IN, DATE_OUT) VALUES (?,?,?,?)";

        try {
            ps = dbManager.getConnection().prepareStatement(addQuery);

            ps.setInt(1, client_id);
            ps.setInt(2, room_number);
            ps.setString(3, date_in);
            ps.setString(4, date_out);

            if (ps.executeUpdate() > 0) {
                room.setRoomToReserve(room_number, "Yes");
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean editReservation(int reservation_id, int client_id, int room_number, String date_in, String date_out) {

        PreparedStatement ps;
        ResultSet rs;
        String updateQuery = "UPDATE RESERVATIONS SET CLIENT_ID = ?, ROOM_NUMBER = ?, DATE_IN = ?, DATE_OUT = ? WHERE ID = ?";

        try {
            ps = dbManager.getConnection().prepareStatement(updateQuery);

            ps.setInt(1, client_id);
            ps.setInt(2, room_number);
            ps.setString(3, date_in);
            ps.setString(4, date_out);
            ps.setInt(5, reservation_id);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean removeReservation(int reservation_id) {
        PreparedStatement ps;
        ResultSet rs;
        String deleteQuery = "DELETE FROM RESERVATIONS WHERE ID = ?";

        try {
            ps = dbManager.getConnection().prepareStatement(deleteQuery);

            ps.setInt(1, reservation_id);

            if (ps.executeUpdate() > 0) {
                room.setRoomToReserve(getRoomNumberFromReservation(reservation_id), "No");
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Date getClientCheckInDate(int client_id) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT DATE_IN FROM RESERVATIONS WHERE CLIENT_ID = ?";
        Date checkInDate = null;

        try {
            ps = dbManager.getConnection().prepareStatement(selectQuery);
            ps.setInt(1, client_id);
            rs = ps.executeQuery();

            if (rs.next()) {
                checkInDate = rs.getDate("DATE_IN");
            }

        } catch (SQLException ex) {
            Logger.getLogger(RESERVATION.class.getName()).log(Level.SEVERE, null, ex);
        }

        return checkInDate;
    }

    public Date getClientCheckOutDate(int client_id) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT DATE_OUT FROM RESERVATIONS WHERE CLIENT_ID = ?";
        Date checkOutDate = null;

        try {
            ps = dbManager.getConnection().prepareStatement(selectQuery);
            ps.setInt(1, client_id);
            rs = ps.executeQuery();

            if (rs.next()) {
                checkOutDate = rs.getDate("DATE_OUT");
            }

        } catch (SQLException ex) {
            Logger.getLogger(RESERVATION.class.getName()).log(Level.SEVERE, null, ex);
        }

        return checkOutDate;
    }
    
    public int getRoomNumberFromReservation(int reservationID) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT ROOM_NUMBER FROM RESERVATIONS WHERE IF = ?";

        try {
            ps = dbManager.getConnection().prepareStatement(selectQuery);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(0);
            } else {
                return 0;
            }
         
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
