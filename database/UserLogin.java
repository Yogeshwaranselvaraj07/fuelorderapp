package database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserLogin extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;

    public UserLogin() {
        setTitle("User Login");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🌈 Gradient Background Panel
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(50, 65, 90), getWidth(), getHeight(), new Color(30, 40, 60));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        setContentPane(panel);

        JLabel title = new JLabel("🚹 User Login");
        title.setForeground(new Color(0, 217, 255));
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(130, 20, 200, 30);
        panel.add(title);

        JLabel emailLabel = new JLabel("Email:");
        styleLabel(emailLabel);
        emailLabel.setBounds(60, 70, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        styleField(emailField);
        emailField.setBounds(150, 70, 180, 28);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        styleLabel(passwordLabel);
        passwordLabel.setBounds(60, 120, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        styleField(passwordField);
        passwordField.setBounds(150, 120, 180, 28);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.setBounds(60, 180, 120, 35);
        panel.add(loginButton);

        signupButton = new JButton("Sign In");
        styleButton(signupButton);
        signupButton.setBounds(200, 180, 120, 35);
        panel.add(signupButton);

        // 🔐 Login logic
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❗ Please enter email and password");
                return;
            }

            try {
                Connection con = Databaseconnection.getConnection();
                String query = "SELECT * FROM userdetails WHERE email=? AND password=?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, email);
                pst.setString(2, password);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "✅ Login Successful!");
                    dispose();
                    new HomePage();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Invalid email or password!");
                }
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        // ⏳ Show sign-up page when button clicked
        signupButton.addActionListener(e -> {
            new UserLogin(); // Assuming you have a separate signup screen
            dispose();
        });

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 217, 255));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    public static void main(String[] args) {
        new UserLogin();
    }
}
