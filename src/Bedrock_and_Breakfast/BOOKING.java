/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bedrock_and_Breakfast;

import static java.lang.Math.random;
import static java.lang.StrictMath.random;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.Random;

public class BOOKING {

    SimpleDBManager dbManager = new SimpleDBManager();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Random random = new Random();

    public BOOKING() {
    }

    public boolean addClient(String fname, String lname, String phone, String email) {

        // Check if the client already exists
        int existingClientId = getClientIdByEmail(email);
        if (existingClientId != -1) {
            // Client already exists
            return true;
        }

        String insertQuery = "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES (?, ?, ?, ?)";
        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(insertQuery)) {
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

    public boolean addBooking(int clientId, int roomNumber, String dateIn, String dateOut) {
        String insertQuery = "INSERT INTO RESERVATIONS (CLIENT_ID, ROOM_NUMBER, DATE_IN, DATE_OUT) VALUES (?, ?, ?, ?)";
        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(insertQuery)) {
            ps.setInt(1, clientId);
            ps.setInt(2, roomNumber);
            ps.setString(3, dateIn);
            ps.setString(4, dateOut);
            boolean bookingSuccess = ps.executeUpdate() > 0;
            if (bookingSuccess) {
                markRoomAsReserved(roomNumber);
            }
            return bookingSuccess;
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean markRoomAsReserved(int roomNumber) {
        String updateQuery = "UPDATE Room SET reserved = 'Yes' WHERE r_number = ?";
        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(updateQuery)) {
            ps.setInt(1, roomNumber);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public int getAvailableRoomNumber(int roomType) {
        int roomNumber;
        boolean isRoomNumberUnique;
        do {
            roomNumber = generateRandomRoomNumber();
            isRoomNumberUnique = isRoomNumberUnique(roomNumber);
        } while (!isRoomNumberUnique);

        return roomNumber;
    }

    private int generateRandomRoomNumber() {
        // Generate a random room number between 100 and 999 (you can adjust the range as needed)
        return random.nextInt(900) + 100;
    }

    private boolean isRoomNumberUnique(int roomNumber) {
        String query = "SELECT r_number FROM Room WHERE r_number = ?";
        try ( PreparedStatement ps = dbManager.getConnection().prepareStatement(query)) {
            ps.setInt(1, roomNumber);
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean addRoom(int roomNumber, int roomType, String phone) {
        String insertQuery = "INSERT INTO Room (r_number, type, phone, reserved) VALUES (?, ?, ?, 'Yes')";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(insertQuery)) {
            ps.setInt(1, roomNumber);
            ps.setInt(2, roomType);
            ps.setString(3, phone);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
