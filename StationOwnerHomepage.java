package pages;

import database.Databaseconnection;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class StationOwnerHomepage extends JFrame {
    private JTextField productField, priceField;
    private String ownerEmail;

    public StationOwnerHomepage(String email) {
        this.ownerEmail = email;

        setTitle("Station Owner Dashboard");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("Fuel Station - Product Update");
        title.setBounds(80, 20, 300, 30);
        add(title);

        JLabel productLabel = new JLabel("Product Type:");
        productLabel.setBounds(50, 70, 100, 25);
        add(productLabel);

        productField = new JTextField();
        productField.setBounds(160, 70, 150, 25);
        add(productField);

        JLabel priceLabel = new JLabel("Price per Litre:");
        priceLabel.setBounds(50, 110, 100, 25);
        add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(160, 110, 150, 25);
        add(priceField);

        JButton updateBtn = new JButton("Update Product");
        updateBtn.setBounds(110, 170, 150, 30);
        add(updateBtn);

        // Load existing product details
        loadProductDetails();

        updateBtn.addActionListener(e -> {
            try {
                Connection con = Databaseconnection.getConnection();
                String query = "UPDATE fuel_stations SET products=?, price_per_litre=? WHERE email=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, productField.getText());
                ps.setDouble(2, Double.parseDouble(priceField.getText()));
                ps.setString(3, ownerEmail);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating product.");
            }
        });

        setVisible(true);
    }

    private void loadProductDetails() {
        try {
            Connection con = Databaseconnection.getConnection();
            String query = "SELECT products, price_per_litre FROM fuel_stations WHERE email=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, ownerEmail);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                productField.setText(rs.getString("products"));
                priceField.setText(rs.getString("price_per_litre"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
