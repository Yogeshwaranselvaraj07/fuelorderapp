package database;

import java.sql.*;

public class Databaseconnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/auto_fuel_assistance", // Make sure port and DB name are correct
                    "root", // 🔐 Your new MySQL username
                    "yogesh0756v"  // 🔐 Your new MySQL password
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
