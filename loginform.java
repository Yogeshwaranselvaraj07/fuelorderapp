package database;

import javax.swing.*;
import java.awt.*;


public class loginform extends JFrame {

    public loginform() {
        setTitle("Welcome - Fuel Order App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 🌈 Gradient Background Panel
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 30, 48), getWidth(), getHeight(), new Color(36, 59, 85));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        setContentPane(panel);

        JLabel title = new JLabel("🚀 Fuel Order App");
        title.setForeground(new Color(0, 217, 255));
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(110, 30, 250, 30);
        panel.add(title);

        JButton userLoginBtn = new JButton("User Login");
        styleButton(userLoginBtn);
        userLoginBtn.setBounds(120, 90, 150, 35);
        panel.add(userLoginBtn);

        JButton ownerLoginBtn = new JButton("Station Owner Login");
        styleButton(ownerLoginBtn);
        ownerLoginBtn.setBounds(120, 145, 150, 35);
        panel.add(ownerLoginBtn);

        // 🔁 Actions
        userLoginBtn.addActionListener(e -> {
            new UserLogin();
            dispose();
        });

        ownerLoginBtn.addActionListener(e -> {
            new stationownerlogin();
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

    public static void main(String[] args) {
        new loginform();
    }
}
