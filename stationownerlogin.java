package database;

import pages.StationOwnerHomepage;

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

        // Login Logic
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    Connection con = Databaseconnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM fuel_stations WHERE email=? AND password=?");
                    ps.setString(1, email);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login Successful!");
                        new StationOwnerHomepage(email); // Open homepage
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Email or Password");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database Error");
                }
            }
        });

        // Sign Up Logic
        signupBtn.addActionListener(e -> {
            JTextField stationName = new JTextField();
            JTextField location = new JTextField();
            JTextField contact = new JTextField();
            JTextField products = new JTextField();
            JTextField email = new JTextField();
            JPasswordField pass = new JPasswordField();

            Object[] msg = {
                    "Station Name:", stationName,
                    "Location:", location,
                    "Contact No:", contact,
                    "Products:", products,
                    "Email:", email,
                    "Password:", pass
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
                    ps.setString(5, email.getText());
                    ps.setString(6, new String(pass.getPassword()));
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Sign Up Successful. You can now log in.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error while signing up");
                }
            }
        });

        setVisible(true);
    }
}
