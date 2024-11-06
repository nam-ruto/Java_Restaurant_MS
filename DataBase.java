/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {
    
    public static Connection connectDb() {
        Connection connection = null;
        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Connect to the database
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/restaurant", "postgres", "1234");

            // Check if the connection is successful
            if (connection != null) {
                System.out.println("Successful");
                return connection;
            } else {
                System.out.println("Fail");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }
    
    

//    public static void main(String[] args) {
//        Connection conn = connectDb();
//        PreparedStatement prepare;
//        ResultSet result;
//        String sql = "SELECT * FROM admin_db WHERE username = 'admin_1' AND password = '1234'";
//        try {
//            prepare = conn.prepareStatement(sql);
//            result = prepare.executeQuery();
//            System.out.println(result);
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }

}