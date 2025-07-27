package database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {

    private JTextField locationField;
    private JButton confirmLocationBtn;

    private JComboBox<String> stationComboBox;
    private JButton orderFuelBtn;

    // Dummy stations data with their coordinates
    private String[] allStations = {"Station A", "Station B", "Station C"};
    private double[][] stationCoords = {
            {12.9260, 80.1171},  // Station A
            {12.9350, 80.1200},  // Station B
            {12.9200, 80.1100}   // Station C
    };

    public HomePage() {
        setTitle("Home - Fuel Order App");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Label + input for customer location
        JLabel locationLbl = new JLabel("Enter Your Location:");
        locationLbl.setBounds(30, 30, 150, 30);
        add(locationLbl);

        locationField = new JTextField();
        locationField.setBounds(180, 30, 170, 30);
        add(locationField);

        confirmLocationBtn = new JButton("Confirm Location");
        confirmLocationBtn.setBounds(120, 70, 150, 30);
        add(confirmLocationBtn);

        // Nearby station selector (hidden initially)
        JLabel nearbyStationLbl = new JLabel("Select Nearby Station:");
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

        // Confirm location button action
        confirmLocationBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String location = locationField.getText().trim();
                if (location.isEmpty()) {
                    JOptionPane.showMessageDialog(HomePage.this, "Please enter your location!");
                    return;
                }
                // Show dummy stations for now, can filter based on location later
                stationComboBox.removeAllItems();
                for (String station : allStations) {
                    stationComboBox.addItem(station);
                }

                // Show stations dropdown and order button
                nearbyStationLbl.setVisible(true);
                stationComboBox.setVisible(true);
                orderFuelBtn.setVisible(true);

                JOptionPane.showMessageDialog(HomePage.this, "Location confirmed: " + location + "\nSelect a nearby station.");
            }
        });

        // Order fuel button action
        orderFuelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = stationComboBox.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(HomePage.this, "Please select a station!");
                    return;
                }

                String selectedStation = allStations[selectedIndex];
                double lat = stationCoords[selectedIndex][0];
                double lon = stationCoords[selectedIndex][1];

                // Open FuelOrderPage with selected station info
                new FuelOrderPage(selectedStation, lat, lon).setVisible(true);
                dispose(); // close this window
            }
        });

        getContentPane().setBackground(new Color(36, 59, 85));
        setVisible(true);
    }

    public static void main(String[] args) {
        new HomePage();
    }
}
