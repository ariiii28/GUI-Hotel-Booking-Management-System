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

public class BOOKING {
    
    SimpleDBManager dbManager = new SimpleDBManager();
    
    public BOOKING() {
        this.dbManager = dbManager;
    }

    public boolean addClient(String fname, String lname, String phone, String email) {
        PreparedStatement ps;
        String addQuery = "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES (?,?,?,?)";

        try {
            ps = dbManager.getConnection().prepareStatement(addQuery);
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
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT ID FROM CLIENTS WHERE EMAIL = ?";
        int clientId = -1;

        try {
            ps = dbManager.getConnection().prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                clientId = rs.getInt("ID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
        }

        return clientId;
    }

    public boolean addReservation(int client_id, int room_number, String date_in, String date_out) {
        PreparedStatement ps;
        String addQuery = "INSERT INTO RESERVATIONS (CLIENT_ID, ROOM_NUMBER, DATE_IN, DATE_OUT) VALUES (?,?,?,?)";

        try {
            ps = dbManager.getConnection().prepareStatement(addQuery);
            ps.setInt(1, client_id);
            ps.setInt(2, room_number);
            ps.setString(3, date_in);
            ps.setString(4, date_out);

            if (ps.executeUpdate() > 0) {
                markRoomAsReserved(room_number);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void markRoomAsReserved(int room_number) {
        PreparedStatement ps;
        String updateQuery = "UPDATE ROOMS SET RESERVED = 'Yes' WHERE ROOM_NUMBER = ?";

        try {
            ps = dbManager.getConnection().prepareStatement(updateQuery);
            ps.setInt(1, room_number);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BOOKING.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}