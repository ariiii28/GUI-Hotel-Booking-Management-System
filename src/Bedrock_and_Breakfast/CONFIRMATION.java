/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bedrock_and_Breakfast;

/**
 *
 * @author ariannemasinading
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class CONFIRMATION {

    private SimpleDBManager dbManager;

    public CONFIRMATION() {
        dbManager = new SimpleDBManager();
    }

    public void checkAndCreateUser(String email) {
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Check if the email exists in the CLIENTS table
            if (!emailExistsInClients(email)) {
                JOptionPane.showMessageDialog(null, "Email does not exist in clients", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if the email already exists in the USERS table
            if (!emailExistsInUsers(email)) {
                // Email does not exist in USERS, create a password and add the user
                String password = generatePassword();
                addUser(email, password);
            } else {
                JOptionPane.showMessageDialog(null, "Email already has a user account", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfirmationForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean emailExistsInClients(String email) throws SQLException {
        String query = "SELECT 1 FROM CLIENTS WHERE EMAIL = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean emailExistsInUsers(String email) throws SQLException {
        String query = "SELECT 1 FROM USERS WHERE USERNAME = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void addUser(String email, String password) throws SQLException {
        String query = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "User created with password: " + password, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String generatePassword() {
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

}
