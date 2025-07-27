package database;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class stationownerlogin extends JFrame {
    JTextField emailField;
    JPasswordField passwordField;

    public stationownerlogin() {
        setTitle("Station Owner Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 30, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 30, 180, 25);
        add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 70, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 70, 180, 25);
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(50, 120, 120, 30);
        add(loginBtn);

        JButton signupBtn = new JButton("Sign In");
        signupBtn.setBounds(200, 120, 120, 30);
        add(signupBtn);

        // 🔒 Login Logic
        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String pass = new String(passwordField.getPassword());

            try {
                Connection con = Databaseconnection.getConnection();
                String query = "SELECT * FROM fuel_stations WHERE email=? AND password=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Station Owner Login Successful");
                    // forward to station dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Email or Password");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ✨ Sign In Logic
        signupBtn.addActionListener(e -> {
            JTextField stationName = new JTextField();
            JTextField location = new JTextField();
            JTextField contact = new JTextField();
            JTextField products = new JTextField();

            Object[] msg = {
                    "Station Name:", stationName,
                    "Location:", location,
                    "Contact No:", contact,
                    "Products:", products,
                    "Email:", emailField,
                    "Password:", passwordField
            };

            int option = JOptionPane.showConfirmDialog(this, msg, "Station Owner Sign Up", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    Connection con = Databaseconnection.getConnection();
                    String insertQuery = "INSERT INTO fuel_stations (station_name, station_location, contact_no, products, email, password) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(insertQuery);
                    ps.setString(1, stationName.getText());
                    ps.setString(2, location.getText());
                    ps.setString(3, contact.getText());
                    ps.setString(4, products.getText());
                    ps.setString(5, emailField.getText());
                    ps.setString(6, new String(passwordField.getPassword()));
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Sign Up Successful");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }
}
