package database; 

import java.sql.Connection;
import java.sql.SQLException;

import businessLogic.entity.User;

import java.sql.PreparedStatement; //used to run parameterized SQL quesries: uses placeholders(?), used for login 
import java.sql.ResultSet; //used to store rows retuned from SELECT queries

public class UserDAO {
    public static String authenticateUser(String email, String password){
        String sql = "SELECT * FROM users WHERE username = ? AND user_password = ?"; //parameterized query to prevent SQL injection
        
        try (Connection conn = DatabaseConnection.connect(); //establish connection
            PreparedStatement pstmt = conn.prepareStatement(sql)){ //create a PreparedStatement object to execute the query

            pstmt.setString(1, email); //set the first placeholder to email
            pstmt.setString(2, password); //set the second placeholder to password

            ResultSet rs = pstmt.executeQuery();//execute the query and store the result in ResultSet

            if (rs.next()){ //behaves as a cursor that moves through the returned rows
                System.out.println("Login Successful (" + rs.getString("role") + ")"); //if a row is returned, login is successful
                return new User{
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("user_password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getInt("receive_promotions")
                    rs.getTimestamp("created_at").toLocalDateTime()
                };
            } else {
                return "Login Failed"; //no rows returned, login failed
            }
            
        } catch (SQLException e){ //handle any SQL exceptions
            e.printStackTrace(); //print stack trace for debugging
            return "Database Error"; //return database error message 
        } 
    }
}
