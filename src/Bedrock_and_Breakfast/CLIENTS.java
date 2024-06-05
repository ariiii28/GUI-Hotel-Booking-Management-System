/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bedrock_and_Breakfast;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ariannemasinading
 */
public class CLIENTS {

    SimpleDBManager dbManager = new SimpleDBManager();

    //create a function to add a client
    public boolean addClient(String fname, String lname, String phone, String email) {

        PreparedStatement ps;
        ResultSet rs;
        String addQuery = "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES (?,?,?,?)";

        try {
            ps = dbManager.getConnection().prepareStatement(addQuery);

            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, phone);
            ps.setString(4, email);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    //create a function to edit the selected client
    public boolean editClient(int id, String fname, String lname, String phone, String email) {

        PreparedStatement ps;
        ResultSet rs;
        String updateQuery = "UPDATE CLIENTS SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, PHONE = ? WHERE ID = ?";

        try {
            ps = dbManager.getConnection().prepareStatement(updateQuery);

            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setInt(5, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    //create a function to remove the selected client
    public boolean removeClient(int id) {
        PreparedStatement ps;
        ResultSet rs;
        String deleteQuery = "DELETE FROM CLIENTS WHERE ID = ?";

        try {
            ps = dbManager.getConnection().prepareStatement(deleteQuery);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //create a function to populate the jtable with all the clients in the database
    public void fillClientJTable(JTable table) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT * FROM CLIENTS";

        try {
            ps = dbManager.getConnection().prepareStatement(selectQuery);
            rs = ps.executeQuery();

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);

            Object[] row;

            while (rs.next()) {
                row = new Object[5];
                // Assuming column indices start from 1
                row[0] = rs.getInt(1); // ID
                row[1] = rs.getString(2); // First Name
                row[2] = rs.getString(3); // Last Name
                row[3] = rs.getString(4); // Phone Number
                row[4] = rs.getString(5); // Email
                tableModel.addRow(row);
            }
            tableModel.fireTableDataChanged();
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //create a function to get client details by ID
    public String[] getClientDetails(int id) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT * FROM CLIENTS WHERE ID = ?";
        String[] clientDetails = new String[5];

        try {
            ps = dbManager.getConnection().prepareStatement(selectQuery);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                clientDetails[0] = rs.getString(2); // First Name
                clientDetails[1] = rs.getString(3); // Last Name
                clientDetails[2] = rs.getString(4); // Phone Number
                clientDetails[3] = rs.getString(5); // Email
                clientDetails[4] = rs.getInt(1) + ""; // ID as String
            }

        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return clientDetails;
    }
}
