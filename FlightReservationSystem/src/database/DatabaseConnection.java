package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:DatabaseSQLite.db";
   
    public static Connection connect(){ 
    //must be public so DAO classes can use it 

        Connection conn = null; //initialize connection object
        try {
            conn = DriverManager.getConnection(URL); //establish connection to the database
        } catch (SQLException e) {
            System.out.println(e.getMessage());//print any SQL exceptions
        }
        return conn; //return the connection object
    
    }
}
