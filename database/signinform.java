package database;

import javax.swing.*;
import java.sql.*;

public class signinform extends JFrame {

    JTextField nameField, vehicleNoField, locationField, emailField;
    JPasswordField passwordField;
    JButton signupBtn;

    String url = "jdbc:mysql://localhost:3306/auto_fuel_assistance";
    String user = "root";
    String password = "Yogesh0756@";

    public signinform() {
        setTitle("Sign Up");
        setSize(400, 320);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel nameLbl = new JLabel("Name:");
        nameLbl.setBounds(30, 20, 100, 30);
        add(nameLbl);

        nameField = new JTextField();
        nameField.setBounds(140, 20, 200, 30);
        add(nameField);

        JLabel vehicleLbl = new JLabel("Vehicle No:");
        vehicleLbl.setBounds(30, 60, 100, 30);
        add(vehicleLbl);

        vehicleNoField = new JTextField();
        vehicleNoField.setBounds(140, 60, 200, 30);
        add(vehicleNoField);

        JLabel locationLbl = new JLabel("Location:");
        locationLbl.setBounds(30, 100, 100, 30);
        add(locationLbl);

        locationField = new JTextField();
        locationField.setBounds(140, 100, 200, 30);
        add(locationField);

        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setBounds(30, 140, 100, 30);
        add(emailLbl);

        emailField = new JTextField();
        emailField.setBounds(140, 140, 200, 30);
        add(emailField);

        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(30, 180, 100, 30);
        add(passLbl);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 180, 200, 30);
        add(passwordField);

        signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(140, 230, 100, 30);
        add(signupBtn);

        signupBtn.addActionListener(e -> signup());

        setVisible(true);
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
                JOptionPane.showMessageDialog(this, "🎉 Signup successful! Please login now.");
                this.dispose();
                new loginform();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
