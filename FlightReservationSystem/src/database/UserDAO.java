package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import businessLogic.entity.*;

public class UserDAO {

    public static User authenticateUser(String email, String password) {

        String sql = "SELECT * FROM users WHERE email = ? AND user_password = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login Successful (" + rs.getString("role") + ")");

                String role = rs.getString("role");
                
                // Shared fields
                int id = rs.getInt("user_id");
                String dbEmail = rs.getString("email");
                String dbPass = rs.getString("user_password");
                String fName = rs.getString("first_name");
                String lName = rs.getString("last_name");
                String addr = rs.getString("address");
                String phone = rs.getString("phone");
                boolean promo = rs.getInt("receive_promotions") == 1;
                var createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                // Return correct child class
                switch (role) {
                    case "Admin":
                        return new Admin(id, dbEmail, dbPass, fName, lName, addr, phone, promo, createdAt);

                    case "Agent":
                        return new Agent(id, dbEmail, dbPass, fName, lName, addr, phone, promo, createdAt);

                    default: // Customer
                        return new Customer(id, dbEmail, dbPass, fName, lName, addr, phone, promo, createdAt);
                }
            }

            return null; // login failed

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
