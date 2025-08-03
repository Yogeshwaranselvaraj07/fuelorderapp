package database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class HomePage extends JFrame {

    private JTextField locationField;
    private JButton confirmLocationBtn;

    private JComboBox<String> stationComboBox;
    private JButton orderFuelBtn;

    // Store station info
    private ArrayList<String> stationNames = new ArrayList<>();
    private ArrayList<Double> stationLatitudes = new ArrayList<>();
    private ArrayList<Double> stationLongitudes = new ArrayList<>();

    public HomePage() {
        setTitle("Home - Fuel Order App");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel locationLbl = new JLabel("Enter Your Location:");
        locationLbl.setForeground(Color.WHITE);
        locationLbl.setBounds(30, 30, 150, 30);
        add(locationLbl);

        locationField = new JTextField();
        locationField.setBounds(180, 30, 170, 30);
        add(locationField);

        confirmLocationBtn = new JButton("Confirm Location");
        confirmLocationBtn.setBounds(120, 70, 150, 30);
        add(confirmLocationBtn);

        JLabel nearbyStationLbl = new JLabel("Select Station:");
        nearbyStationLbl.setForeground(Color.WHITE);
        nearbyStationLbl.setBounds(30, 120, 150, 30);
        nearbyStationLbl.setVisible(false);
        add(nearbyStationLbl);

        stationComboBox = new JComboBox<>();
        stationComboBox.setBounds(180, 120, 170, 30);
        stationComboBox.setVisible(false);
        add(stationComboBox);

        orderFuelBtn = new JButton("Order Fuel");
        orderFuelBtn.setBounds(120, 170, 150, 30);
        orderFuelBtn.setVisible(false);
        add(orderFuelBtn);

        confirmLocationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = locationField.getText().trim();
                if (location.isEmpty()) {
                    JOptionPane.showMessageDialog(HomePage.this, "Please enter your location!");
                    return;
                }

                fetchStationsFromDatabase(); // 🔥 Fetch from DB
                if (stationNames.isEmpty()) {
                    JOptionPane.showMessageDialog(HomePage.this, "No stations found in database!");
                    return;
                }

                stationComboBox.removeAllItems();
                for (String station : stationNames) {
                    stationComboBox.addItem(station);
                }

                nearbyStationLbl.setVisible(true);
                stationComboBox.setVisible(true);
                orderFuelBtn.setVisible(true);

                JOptionPane.showMessageDialog(HomePage.this, "Location confirmed: " + location + "\nSelect a station.");
            }
        });

        orderFuelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = stationComboBox.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(HomePage.this, "Please select a station!");
                    return;
                }

                String selectedStation = stationNames.get(selectedIndex);
                double lat = stationLatitudes.get(selectedIndex);
                double lon = stationLongitudes.get(selectedIndex);

                new FuelOrderPage(selectedStation, lat, lon).setVisible(true);
                dispose();
            }
        });

        getContentPane().setBackground(new Color(36, 59, 85));
        setVisible(true);
    }

    private void fetchStationsFromDatabase() {
        stationNames.clear();
        stationLatitudes.clear();
        stationLongitudes.clear();

        String url = "jdbc:mysql://localhost:3306/auto_fuel_assistance";
        String username = "root";
        String password = ""; // Update if you have a password

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT station_name, latitude, longitude FROM stations";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                stationNames.add(rs.getString("station_name"));
                stationLatitudes.add(rs.getDouble("latitude"));
                stationLongitudes.add(rs.getDouble("longitude"));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePage::new);
    }
}
