/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bedrock_and_Breakfast;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class SimpleDBManager {

    private static final String USER_NAME = "Bedrock"; // your DB username
    private static final String PASSWORD = "breakfast"; // your DB password
    private static final String URL = "jdbc:derby:BDB;create=true"; // embedded DB URL

    private Connection conn;

    public SimpleDBManager() {
        establishConnection();
        if (conn != null) {
            try {
                if (!databaseExists()) {
                    createDatabase();
                } else {
                    System.out.println("Database already exists.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SimpleDBManager dbManager = new SimpleDBManager();
        System.out.println(dbManager.getConnection());
    }

    public Connection getConnection() {
        return this.conn;
    }

    // Establish connection
    public void establishConnection() {
        conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                System.out.println("Connection established successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Failed to establish connection!");
            e.printStackTrace();
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
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return resultSet;
    }

    public void updateDB(String sql) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Check if database exists
    private boolean databaseExists() throws SQLException {
        DatabaseMetaData dbMeta = conn.getMetaData();
        ResultSet rs = dbMeta.getTables(null, null, null, new String[]{"TABLE"});
        boolean exists = false;
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            if (tableName.equalsIgnoreCase("CLIENTS")
                    || tableName.equalsIgnoreCase("USERS")
                    || tableName.equalsIgnoreCase("Type")
                    || tableName.equalsIgnoreCase("Room")
                    || tableName.equalsIgnoreCase("RESERVATIONS")) {
                exists = true;
                break;
            }
        }
        rs.close();
        return exists;
    }

    // Create database
    private void createDatabase() throws SQLException {
        try ( Statement stmt = conn.createStatement()) {
            String[] sqlStatements = {
                "CREATE TABLE CLIENTS ("
                + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "FIRSTNAME VARCHAR(50), "
                + "LASTNAME VARCHAR(50), "
                + "EMAIL VARCHAR(100), "
                + "PHONE VARCHAR(15)"
                + ")",
                "CREATE TABLE USERS ("
                + "ID INT PRIMARY KEY, "
                + "USERNAME VARCHAR(50) NOT NULL, "
                + "PASSWORD VARCHAR(50) NOT NULL, "
                + "CONSTRAINT FK_CLIENT_ID FOREIGN KEY (ID) REFERENCES CLIENTS(ID)"
                + ")",
                // Insert a client first
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('John', 'Doe', 'john.doe@example.com', '123-456-7890')",
                // Insert a user corresponding to the client
                "INSERT INTO USERS (ID, USERNAME, PASSWORD) VALUES (1, 'testuser', 'password')",
                "CREATE TABLE Type ("
                + "ID INT PRIMARY KEY, "
                + "Label VARCHAR(100), "
                + "Price VARCHAR(10)"
                + ")",
                "CREATE TABLE Room ("
                + "r_number INT PRIMARY KEY, "
                + "type INT, "
                + "phone VARCHAR(25), "
                + "reserved VARCHAR(25), "
                + "FOREIGN KEY (type) REFERENCES Type(ID)"
                + ")",
                "INSERT INTO Type (ID, Label, Price) VALUES (1, 'Queen', '150')",
                "INSERT INTO Type (ID, Label, Price) VALUES (2, 'Double', '300')",
                "INSERT INTO Type (ID, Label, Price) VALUES (3, 'Superior', '500')",
                "CREATE TABLE RESERVATIONS ("
                + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "CLIENT_ID INT, "
                + "ROOM_NUMBER INT, "
                + "DATE_IN DATE, "
                + "DATE_OUT DATE"
                + ")",
                
               
            };

            for (String sql : sqlStatements) {
                stmt.execute(sql);
            }

            System.out.println("Database created successfully.");
        }
    }

}
