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
        String addQuery = "INSERT INTO CLIENTS (FIRST_NAME, LAST_NAME, PHONE, EMAIL) VALUES (?,?,?,?)";
        
        try {
            ps = dbManager.getConnection().prepareStatement(addQuery);
            
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, phone);
            ps.setString(4, email);
            
            if (ps.executeUpdate() > 0) {
                return true;
            } else {
                return false; 
            }
        } catch (SQLException ex) {
            Logger.getLogger(CLIENTS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
   
    }
    
    //create a function to edit the selected client
    
    //create a function to remove the selected client
    
    //create a function to return a list of all clients
    
}
