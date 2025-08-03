package database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class FuelOrderPage extends JFrame {

    private String selectedStation;
    private double stationLat;
    private double stationLon;

    private JComboBox<String> fuelTypeCombo;
    private JTextField litreField;
    private JTextField userLocationField;
    private JLabel priceLabel;
    private JLabel totalAmountLabel;
    private JButton orderBtn;

    private HashMap<String, Double> productPrices;

    public FuelOrderPage(String selectedStation, double lat, double lon) {
        this.selectedStation = selectedStation;
        this.stationLat = lat;
        this.stationLon = lon;

        setTitle("Fuel Order Page - " + selectedStation);
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel titleLabel = new JLabel("Ordering from: " + selectedStation);
        titleLabel.setBounds(30, 20, 400, 30);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        JLabel locationLbl = new JLabel("Your Location (lat,lon):");
        locationLbl.setBounds(30, 60, 150, 30);
        locationLbl.setForeground(Color.WHITE);
        add(locationLbl);

        userLocationField = new JTextField();
        userLocationField.setBounds(200, 60, 150, 30);
        add(userLocationField);

        JLabel fuelTypeLbl = new JLabel("Fuel Type:");
        fuelTypeLbl.setBounds(30, 110, 150, 30);
        fuelTypeLbl.setForeground(Color.WHITE);
        add(fuelTypeLbl);

        fuelTypeCombo = new JComboBox<>();
        fuelTypeCombo.setBounds(200, 110, 150, 30);
        add(fuelTypeCombo);

        JLabel litreLbl = new JLabel("Litres:");
        litreLbl.setBounds(30, 160, 150, 30);
        litreLbl.setForeground(Color.WHITE);
        add(litreLbl);

        litreField = new JTextField();
        litreField.setBounds(200, 160, 150, 30);
        add(litreField);

        priceLabel = new JLabel("Price per litre: ₹0");
        priceLabel.setBounds(30, 210, 200, 30);
        priceLabel.setForeground(Color.WHITE);
        add(priceLabel);

        totalAmountLabel = new JLabel("Total: ₹0");
        totalAmountLabel.setBounds(30, 250, 200, 30);
        totalAmountLabel.setForeground(Color.WHITE);
        add(totalAmountLabel);

        orderBtn = new JButton("Place Order");
        orderBtn.setBounds(200, 300, 150, 30);
        add(orderBtn);

        // Styling background
        getContentPane().setBackground(new Color(36, 59, 85));

        fetchStationProducts();

        fuelTypeCombo.addActionListener(e -> updatePrice());
        litreField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateTotalAmount();
            }
        });

        orderBtn.addActionListener(e -> placeOrder());

        setVisible(true);
    }

    private void fetchStationProducts() {
        productPrices = new HashMap<>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/auto_fuel_assistance", "root", "");
             PreparedStatement ps = con.prepareStatement("SELECT product_name, price FROM station_products WHERE station_name = ?")) {

            ps.setString(1, selectedStation);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String product = rs.getString("product_name");
                double price = rs.getDouble("price");
                productPrices.put(product, price);
                fuelTypeCombo.addItem(product);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch products: " + e.getMessage());
        }
    }

    private void updatePrice() {
        String selectedProduct = (String) fuelTypeCombo.getSelectedItem();
        if (selectedProduct != null) {
            double price = productPrices.getOrDefault(selectedProduct, 0.0);
            priceLabel.setText("Price per litre: ₹" + price);
            updateTotalAmount();
        }
    }

    private void updateTotalAmount() {
        try {
            double litres = Double.parseDouble(litreField.getText());
            String selectedProduct = (String) fuelTypeCombo.getSelectedItem();
            double price = productPrices.getOrDefault(selectedProduct, 0.0);

            // Calculate distance
            String[] userLoc = userLocationField.getText().split(",");
            if (userLoc.length != 2) return;

            double userLat = Double.parseDouble(userLoc[0]);
            double userLon = Double.parseDouble(userLoc[1]);
            double distance = haversine(userLat, userLon, stationLat, stationLon);

            double deliveryCharge = distance * 5; // ₹5/km
            double total = litres * price + deliveryCharge;
            totalAmountLabel.setText("Total: ₹" + String.format("%.2f", total));
        } catch (Exception e) {
            totalAmountLabel.setText("Total: ₹0");
        }
    }

    private void placeOrder() {
        JOptionPane.showMessageDialog(this, "Order placed successfully!");
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
