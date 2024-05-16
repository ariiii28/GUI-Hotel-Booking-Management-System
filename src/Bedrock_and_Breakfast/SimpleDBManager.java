/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bedrock_and_Breakfast;

/**
 *
 * @author diyatopiwala
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class SimpleDBManager {

    private static final String USER_NAME = "ADMIN1"; //your DB username
    private static final String PASSWORD = "Admin123"; //your DB password
    private static final String URL = "jdbc:derby://localhost:1527/Bedrock and Breakfast";  //url of the DB host

    Connection conn;

    public SimpleDBManager() {
        establishConnection();
    }

    public static void main(String[] args) {
        SimpleDBManager dbManager = new SimpleDBManager();
        System.out.println(dbManager.getConnection());
    }

    public Connection getConnection() {
        return this.conn;
    }

    //Establish connection
    public void establishConnection() {
        conn = null;

        try {
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            if (conn != null) {
                System.out.println("Connection established successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Failed to establish connection!");
            e.printStackTrace();
        } finally {
            // Closing the connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection!");
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public ResultSet queryDB(String sql) {

        Connection connection = this.conn;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return resultSet;
    }

    public void updateDB(String sql) {

        Connection connection = this.conn;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
