package database;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Mainframe extends JFrame {

    JTextField emailField;
    JPasswordField passwordField;
    JButton loginBtn, signupBtn;
    JTextField nameField, vehicleNoField, locationField;


    String url = "jdbc:mysql://localhost:3306/auto_fuel_assistance";
    String user = "root";
    String password = "Yogesh0756@";

    public Mainframe() {
            setTitle("🔐 Login");
            setSize(400, 280);
            setLayout(null);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            // Name
            JLabel nameLbl = new JLabel("Name:");
            nameLbl.setBounds(30, 10, 100, 30);
            add(nameLbl);

            nameField = new JTextField();
            nameField.setBounds(130, 10, 200, 30);
            add(nameField);

            // Email
            JLabel emailLbl = new JLabel("Email:");
            emailLbl.setBounds(30, 50, 100, 30);
            add(emailLbl);

            emailField = new JTextField();
            emailField.setBounds(130, 50, 200, 30);
            add(emailField);

            // Password
            JLabel passLbl = new JLabel("Password:");
            passLbl.setBounds(30, 90, 100, 30);
            add(passLbl);

            passwordField = new JPasswordField();
            passwordField.setBounds(130, 90, 200, 30);
            add(passwordField);

            // Vehicle No
            JLabel vehicleLbl = new JLabel("Vehicle No:");
            vehicleLbl.setBounds(30, 130, 100, 30);
            add(vehicleLbl);

            vehicleNoField = new JTextField();
            vehicleNoField.setBounds(130, 130, 200, 30);
            add(vehicleNoField);

            // Location
            JLabel locationLbl = new JLabel("Location:");
            locationLbl.setBounds(30, 170, 100, 30);
            add(locationLbl);

            locationField = new JTextField();
            locationField.setBounds(130, 170, 200, 30);
            add(locationField);

            // Buttons
            loginBtn = new JButton("Login");
            loginBtn.setBounds(50, 210, 100, 30);
            add(loginBtn);

            signupBtn = new JButton("Sign Up");
            signupBtn.setBounds(200, 210, 100, 30);
            add(signupBtn);

            // Action Listeners
            loginBtn.addActionListener(e -> login());
            signupBtn.addActionListener(e -> signup());

            setVisible(true);
        }



    void login() {
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM userdetails WHERE email=? AND password=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Successful login, get user details
                String name = rs.getString("name");
                String vehicleNo = rs.getString("vehicle_no");
                String location = rs.getString("location");

                // Show success message
                JOptionPane.showMessageDialog(this, "✅ Login Successful!");

                // Fill the UI fields with user info
                nameField.setText(name);
                vehicleNoField.setText(vehicleNo);
                locationField.setText(location);

                // Optionally, keep email/password filled
                emailField.setText(email);
                passwordField.setText(pass);

                // Optional: disable editing if you want user to only view details
                // nameField.setEditable(false);
                // vehicleNoField.setEditable(false);
                // locationField.setEditable(false);

            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid credentials!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    void signup() {

            String name = nameField.getText().trim();
            String vehicleNo = vehicleNoField.getText().trim();
            String location = locationField.getText().trim();
            String email = emailField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (name.isEmpty() || vehicleNo.isEmpty() || location.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                String checkQuery = "SELECT * FROM userdetails WHERE email=?";
                PreparedStatement checkStmt = con.prepareStatement(checkQuery);
                checkStmt.setString(1, email);
                ResultSet checkRs = checkStmt.executeQuery();

                if (checkRs.next()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Email already registered.");
                    return;
                }

                String insertQuery = "INSERT INTO userdetails (name, vehicle_no, location, email, password) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(insertQuery);
                pst.setString(1, name);
                pst.setString(2, vehicleNo);
                pst.setString(3, location);
                pst.setString(4, email);
                pst.setString(5, pass);

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "🎉 Signup successful! Now login.");
                    // Clear fields after signup
                    nameField.setText("");
                    vehicleNoField.setText("");
                    locationField.setText("");
                    emailField.setText("");
                    passwordField.setText("");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    public static void main(String[] args) {
        new Mainframe();
    }
}
