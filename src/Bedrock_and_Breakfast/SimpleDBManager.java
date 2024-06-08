package Bedrock_and_Breakfast;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class SimpleDBManager {

    private static final String URL = "jdbc:derby:Bed;create=true"; // embedded DB URL
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
                    || tableName.equalsIgnoreCase("\"Type\"")
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
                + "EMAIL VARCHAR(100) UNIQUE NOT NULL, "
                + "PHONE VARCHAR(15)"
                + ")",
                "CREATE TABLE USERS ("
                + "CLIENT_ID INT,"
                + "EMAIL_CLIENT VARCHAR(100) NOT NULL, "
                + "PASSWORD VARCHAR(50) NOT NULL, "
                + "CONSTRAINT FK_CLIENT_ID FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS(ID), "
                + "CONSTRAINT FK_EMAIL FOREIGN KEY (EMAIL_CLIENT) REFERENCES CLIENTS(EMAIL)"
                + ")",
                "CREATE TABLE \"Type\" ("
                + "ID INT PRIMARY KEY, "
                + "Label VARCHAR(100), "
                + "Price VARCHAR(10)"
                + ")",
                "CREATE TABLE Room ("
                + "r_number INT PRIMARY KEY, "
                + "type INT, "
                + "phone VARCHAR(25), "
                + "reserved VARCHAR(25), "
                + "FOREIGN KEY (type) REFERENCES \"Type\"(ID)"
                + ")",
                "CREATE TABLE RESERVATIONS ("
                + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "CLIENT_ID INT, "
                + "ROOM_NUMBER INT, "
                + "DATE_IN DATE, "
                + "DATE_OUT DATE, "
                + "FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS(ID) ON DELETE CASCADE, "
                + "FOREIGN KEY (ROOM_NUMBER) REFERENCES Room(r_number) ON DELETE CASCADE"
                + ")",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Admin', 'Admin', 'admin@bedrock.com', '696-696-6969')",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Elon', 'Musk', 'elonmusk@gmail.com', '123-456-7890')",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Jeff', 'Bezos', 'amazon@gmail.com', '555-555-5555')",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Bernard', 'Arnault', 'louisvuitton@gmail.com', '111-111-1111')",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Mark', 'Zuckerberg', 'meta@facebook.com', '222-222-2222')",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Kim', 'Kardashian', 'northwest@skims.com', '444-444-4444')",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Giovanni', 'Ferrero', 'rocher@ferrero.com', '101-101-1010')",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Taylor', 'Swift', 'ttpd@taylornation.com', '131-131-1313')",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Oprah', 'Winfrey', 'yougetacar@gmail.com', '362-345-2457')",
                "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('Shiv', 'Nadar', 'shiv@software.com', '666-666-6666')",
                "INSERT INTO USERS (EMAIL_CLIENT, PASSWORD) VALUES ('admin@bedrock.com', 'password')",
                "INSERT INTO USERS (EMAIL_CLIENT, PASSWORD) VALUES ('elonmusk@gmail.com', 'password')",
                "INSERT INTO \"Type\" (ID, Label, Price) VALUES (1, 'Queen', '150')",
                "INSERT INTO \"Type\" (ID, Label, Price) VALUES (2, 'Double', '300')",
                "INSERT INTO \"Type\" (ID, Label, Price) VALUES (3, 'Superior', '500')"
            };

            for (String sql : sqlStatements) {
                stmt.execute(sql);
            }

            System.out.println("Database created successfully.");
        }
    }
}
