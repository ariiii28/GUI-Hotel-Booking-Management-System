///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
// */

package Bedrock_and_Breakfast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author diyatopiwala
 */
public class SimpleDBManagerTest {
    
    private SimpleDBManager dbManager;
    
    public SimpleDBManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        dbManager = new SimpleDBManager();
    }
    
    @After
    public void tearDown() {
        dbManager.closeConnections();
    }

    /**
     * Test of main method, of class SimpleDBManager.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        SimpleDBManager.main(args);
        assertNotNull("Connection should not be null", dbManager.getConnection());
    }

    /**
     * Test of getConnection method, of class SimpleDBManager.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        Connection conn = dbManager.getConnection();
        assertNotNull("Connection should not be null", conn);
    }

    /**
     * Test of establishConnection method, of class SimpleDBManager.
     */
    @Test
    public void testEstablishConnection() {
        System.out.println("establishConnection");
        dbManager.establishConnection();
        Connection conn = dbManager.getConnection();
        assertNotNull("Connection should be established", conn);
    }

    /**
     * Test of queryDB method, of class SimpleDBManager.
     */
    @Test
    public void testQueryDB() {
        System.out.println("queryDB");
        String sql = "SELECT * FROM CLIENTS";
        ResultSet resultSet = dbManager.queryDB(sql);
        assertNotNull("ResultSet should not be null", resultSet);
    }

    /**
     * Test of updateDB method, of class SimpleDBManager.
     */
    @Test
    public void testUpdateDB() {
        System.out.println("updateDB");
        String sql = "INSERT INTO CLIENTS (FIRSTNAME, LASTNAME, EMAIL, PHONE) VALUES ('John', 'Doe', 'john.doe@example.com', '123-456-7890')";
        dbManager.updateDB(sql);
        
        // Verify if the data was inserted correctly
        String querySql = "SELECT COUNT(*) AS total FROM CLIENTS";
        ResultSet resultSet = dbManager.queryDB(querySql);
        int count = 0;
        try {
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals("Number of clients should be 11", 11, count);
    }
}
