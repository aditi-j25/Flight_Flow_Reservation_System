package database; 

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement; 
//used to run parameterized SQL quesries: uses placeholders(?), used for login 
import java.sql.ResultSet;
//used to store rows retuned from SELECT queries

public class UserDAO {
    public static String login(){
        String sql = "SELECT * FROM users WHERE username = ? AND user_password = ?";
        
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){ //behaves as a cursor that moves through the returned rows
                return "Login Successful (" + rs.getString("role") + ")";
            } else {
                return "Login Failed";
            }
            
        } catch (SQLException e){
            e.printStackTrace();
            return "Database Error"; 
        } 
    }
}
