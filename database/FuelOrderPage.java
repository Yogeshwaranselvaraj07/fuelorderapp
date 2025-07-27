package database;

import javax.swing.*;
import java.awt.*;

public class FuelOrderPage extends JFrame {
    private JComboBox<String> fuelTypeBox;
    private JTextField litresField;
    private JTextArea resultArea;
    private JLabel successLabel;

    private final double petrolRate = 104;
    private final double dieselRate = 94;

    private final double userLat = 12.9000;
    private final double userLon = 80.1000;

    public FuelOrderPage(String stationName, double stationLat, double stationLon) {
        setTitle("Fuel Order App - " + stationName);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(36, 59, 85));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Fuel Order");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(new Color(0, 217, 255));
        heading.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(heading, gbc);
        gbc.gridwidth = 1;

        // Fuel Type
        JLabel fuelLabel = new JLabel("Fuel Type:");
        fuelLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(fuelLabel, gbc);

        fuelTypeBox = new JComboBox<>(new String[]{"Petrol - ₹104", "Diesel - ₹94"});
        gbc.gridx = 1;
        add(fuelTypeBox, gbc);

        // Litres
        JLabel litresLabel = new JLabel("Litres:");
        litresLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(litresLabel, gbc);

        litresField = new JTextField();
        gbc.gridx = 1;
        add(litresField, gbc);

        // Calculate Button
        JButton calcBtn = new JButton("Calculate Total");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        calcBtn.setBackground(new Color(0, 217, 255));
        calcBtn.setForeground(Color.BLACK);
        calcBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(calcBtn, gbc);

        // Result Area
        resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(46, 46, 46));
        resultArea.setForeground(Color.WHITE);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridy = 4;
        add(resultArea, gbc);

        // Order Button
        JButton orderBtn = new JButton("Order Now");
        gbc.gridy = 5;
        orderBtn.setBackground(new Color(0, 217, 255));
        orderBtn.setForeground(Color.BLACK);
        orderBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(orderBtn, gbc);

        // Success Message
        successLabel = new JLabel("✅ Order placed successfully!");
        successLabel.setForeground(new Color(40, 167, 69));
        successLabel.setHorizontalAlignment(JLabel.CENTER);
        successLabel.setVisible(false);
        gbc.gridy = 6;
        add(successLabel, gbc);

        // Actions
        calcBtn.addActionListener(e -> calculateTotal(stationLat, stationLon));
        orderBtn.addActionListener(e -> {
            if (!resultArea.getText().isEmpty()) {
                successLabel.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please calculate the total before placing the order.");
            }
        });
    }

    private void calculateTotal(double stationLat, double stationLon) {
        try {
            double litres = Double.parseDouble(litresField.getText());
            if (litres <= 0) {
                resultArea.setText("⚠️ Enter a valid litre amount");
                return;
            }

            String fuelType = (String) fuelTypeBox.getSelectedItem();
            double rate = fuelType.contains("Petrol") ? petrolRate : dieselRate;

            double distance = haversine(userLat, userLon, stationLat, stationLon);
            double deliveryCharge = distance * 5;
            double fuelCost = litres * rate;
            double total = fuelCost + deliveryCharge;

            resultArea.setText(String.format(
                    "📍 Distance to Station: %.2f km\n⛽ Fuel Cost: ₹%.2f\n🚚 Delivery Charge: ₹%.2f\n\n💰 Total: ₹%.2f",
                    distance, fuelCost, deliveryCharge, total
            ));
            successLabel.setVisible(false);
        } catch (NumberFormatException ex) {
            resultArea.setText("⚠️ Enter a valid litre amount");
        }
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Earth radius
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new FuelOrderPage("Station 1", 12.9260, 80.1171).setVisible(true));
    }
}
