/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bedrock_and_Breakfast;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ariannemasinading
 */

public class RESERVATION {

    SimpleDBManager dbManager = new SimpleDBManager();
    ROOMS room = new ROOMS();

    public void fillReservationsJTable(JTable table) {
        String selectQuery = "SELECT * FROM RESERVATIONS";

        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(selectQuery);  ResultSet rs = ps.executeQuery()) {

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = new Object[5];
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
        String addQuery = "INSERT INTO RESERVATIONS (CLIENT_ID, ROOM_NUMBER, DATE_IN, DATE_OUT) VALUES (?,?,?,?)";

        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(addQuery)) {
            ps.setInt(1, client_id);
            ps.setInt(2, room_number);
            ps.setString(3, date_in);
            ps.setString(4, date_out);

            // Ensure consistent case for comparison
            if (room.isRoomReserved(room_number).equalsIgnoreCase("No")) {
                if (ps.executeUpdate() > 0) {
                    room.setRoomToReserve(room_number, "Yes");
                    return true;
                } else {
                    return false;
                }

            } else {
                JOptionPane.showMessageDialog(null, "This Room is Reserved", "Room Reserved", JOptionPane.WARNING_MESSAGE);
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean editReservation(int reservation_id, int client_id, int room_number, String date_in, String date_out) {
        String updateQuery = "UPDATE RESERVATIONS SET CLIENT_ID = ?, ROOM_NUMBER = ?, DATE_IN = ?, DATE_OUT = ? WHERE ID = ?";

        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(updateQuery)) {
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
        String deleteQuery = "DELETE FROM RESERVATIONS WHERE ID = ?";

        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(deleteQuery)) {
            ps.setInt(1, reservation_id);

            //int room_number = getRoomNumberFromReservation(reservation_id);
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

    public int getClientIdByEmail(String email) {
        String selectQuery = "SELECT ID FROM CLIENTS WHERE EMAIL = ?";
        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(selectQuery)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public java.sql.Date getClientCheckInDatebyEmail(String email) {
        int client_id = getClientIdByEmail(email);
        if (client_id == -1) {
            System.out.println("No client found with email: " + email);
            return null;
        }
        return getClientCheckInDate(client_id);
    }

    public java.sql.Date getClientCheckInDate(int client_id) {
        String selectQuery = "SELECT DATE_IN FROM RESERVATIONS WHERE CLIENT_ID = ?";
        java.sql.Date checkInDate = null;

        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(selectQuery)) {
            ps.setInt(1, client_id);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    checkInDate = rs.getDate("DATE_IN");
                    System.out.println("Check-in date found: " + checkInDate);
                } else {
                    System.out.println("No check-in date found for client_id: " + client_id);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESERVATION.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkInDate;
    }

    public java.sql.Date getClientCheckOutDateByEmail(String email) {
        int client_id = getClientIdByEmail(email);
        if (client_id == -1) {
            System.out.println("No client found with email: " + email);
            return null;
        }
        return getClientCheckOutDate(client_id);
    }

    public java.sql.Date getClientCheckOutDate(int client_id) {
        String selectQuery = "SELECT DATE_OUT FROM RESERVATIONS WHERE CLIENT_ID = ?";
        java.sql.Date checkOutDate = null;
        
        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(selectQuery)) {
            ps.setInt(1, client_id);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    checkOutDate = rs.getDate("DATE_OUT");
                    System.out.println("Check-out date found: " + checkOutDate);
                } else {
                    System.out.println("No check-out date found for client_id: " + client_id);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESERVATION.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkOutDate;
    }

    public int getRoomNumberFromReservation(int reservationID) {
        String selectQuery = "SELECT ROOM_NUMBER FROM RESERVATIONS WHERE ID = ?";

        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(selectQuery)) {
            ps.setInt(1, reservationID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
