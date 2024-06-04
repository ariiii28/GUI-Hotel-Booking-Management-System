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

/**
 *
 * @author ariannemasinading
 */
public class RESERVATION {

    java.util.Date utilDate = new java.util.Date();
    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

    SimpleDBManager dbManager = new SimpleDBManager();

    public boolean addReservation(int client_id, int room_number, Date date_in, Date date_out) {

        PreparedStatement ps;
        ResultSet rs;
        String addQuery = "INSERT INTO RESERVATIONS (CLIENT_ID, ROOM_NUMBER, DATE_IN, DATE_OUT) VALUES (?,?,?,?)";

        try {
            ps = dbManager.getConnection().prepareStatement(addQuery);

            ps.setInt(1, client_id);
            ps.setInt(2, room_number);
            ps.setDate(3, date_in);
            ps.setDate(4, date_out);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean editReservation(int reservation_id, int client_id, int room_number, Date date_in, Date date_out) {

        PreparedStatement ps;
        ResultSet rs;
        String updateQuery = "UPDATE RESERVATIONS SET CLIENT_ID = ?, ROOM_NUMBER = ?, DATE_IN = ?, DATE_OUT = ? WHERE ID = ?";

        try {
            ps = dbManager.getConnection().prepareStatement(updateQuery);

            ps.setInt(1, reservation_id);
            ps.setInt(2, client_id);
            ps.setInt(3, room_number);
            ps.setDate(4, date_in);
            ps.setDate(5, date_out);

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

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
