package database;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
public class DatabaseMain {
    public static void main(String[] args) {
        Scanner nn = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/auto_fuel_assistance";
        String user = "root";
        String password = "Yogesh0756@";

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to Database!");

            System.out.println("\nChoose an option:\n1. Insert Data\n2. View Users\n3. Delete User");
            int choice = nn.nextInt();
            nn.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = nn.nextLine();

                    System.out.print("Enter email: ");
                    String email = nn.nextLine();

                    String insertQuery = "INSERT INTO userdetails (name, email) VALUES (?, ?)";
                    PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                    insertStmt.setString(1, name);
                    insertStmt.setString(2, email);
                    int rowsInserted = insertStmt.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("✅ Data inserted successfully!");
                    }
                    break;

                case 2:
                    String selectQuery = "SELECT * FROM userdetails";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(selectQuery);

                    System.out.println("\n📋 User List:");
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("user_id")
                                + ", Name: " + rs.getString("name")
                                + ", Email: " + rs.getString("email"));
                    }
                    break;

                case 3:
                    System.out.print("Enter user ID to delete: ");
                    int deleteId = nn.nextInt();

                    String deleteQuery = "DELETE FROM users WHERE user_id = ?";
                    PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
                    deleteStmt.setInt(1, deleteId);
                    int rowsDeleted = deleteStmt.executeUpdate();

                    if (rowsDeleted > 0) {
                        System.out.println("🗑️ User deleted successfully!");
                    } else {
                        System.out.println("⚠️ No user found with that ID.");
                    }
                    break;

                default:
                    System.out.println("❌ Invalid choice.");
            }

            con.close();
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
