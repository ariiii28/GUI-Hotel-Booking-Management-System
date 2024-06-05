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

/**
 *
 * @author ariannemasinading
 */
public class BOOKING {
    
    SimpleDBManager dbManager = new SimpleDBManager();

    public BOOKING() {
       // dbManager = new DBManager();
    }

    public boolean addClient(String fname, String lname, String phone, String email) {
        String insertQuery = "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(insertQuery)) {
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, email);
            ps.setString(4, phone);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public int getClientIdByEmail(String email) {
        String selectQuery = "SELECT ID FROM CLIENTS WHERE EMAIL = ?";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(selectQuery)) {
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

    public boolean addBooking(int clientId, int roomNumber, String dateIn, String dateOut) {
        String insertQuery = "INSERT INTO RESERVATIONS (CLIENT_ID, ROOM_NUMBER, DATE_IN, DATE_OUT) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(insertQuery)) {
            ps.setInt(1, clientId);
            ps.setInt(2, roomNumber);
            ps.setString(3, dateIn);
            ps.setString(4, dateOut);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean markRoomAsReserved(int roomNumber) {
        String updateQuery = "UPDATE ROOM SET reserved = 'Yes' WHERE r_number = ?";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(updateQuery)) {
            ps.setInt(1, roomNumber);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
