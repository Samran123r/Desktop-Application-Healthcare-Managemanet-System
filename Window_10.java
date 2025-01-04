package javaHealth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Window_10 extends JFrame {
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> comboBoxRole;  // Role selection dropdown
    private JComboBox<String> comboBoxSpecialization; // Specialization dropdown
    private JPanel panel;

    public Window_10() {
        setTitle("User Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        panel = new JPanel();
        panel.setLayout(null);
        setContentPane(panel);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 30, 100, 25);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 30, 200, 25);
        panel.add(txtUsername);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 70, 100, 25);
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(150, 70, 200, 25);
        panel.add(txtEmail);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 110, 100, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 110, 200, 25);
        panel.add(txtPassword);

        // Role selection dropdown (Patient/Doctor/Admin)
        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(50, 150, 100, 25);
        panel.add(lblRole);

        comboBoxRole = new JComboBox<>();
        comboBoxRole.setModel(new DefaultComboBoxModel<>(new String[]{"Patient", "Doctor", "Admin"}));  // Added Admin role
        comboBoxRole.setBounds(150, 150, 200, 25);
        panel.add(comboBoxRole);

        // Specialization dropdown
        JLabel lblSpecialization = new JLabel("Specialization:");
        lblSpecialization.setBounds(50, 190, 100, 25);
        panel.add(lblSpecialization);

        comboBoxSpecialization = new JComboBox<>();
        comboBoxSpecialization.setModel(new DefaultComboBoxModel<>(new String[]{
                "Cardiologist", "Neurologist", "Orthopedist", "Pediatrician", "Surgeon","Orthopedics" ,"GeneralPhysician"
        }));
        comboBoxSpecialization.setBounds(150, 190, 200, 25);
        comboBoxSpecialization.setVisible(false); // Initially hidden
        panel.add(comboBoxSpecialization);

        // Show specialization dropdown if "Doctor" is selected
        comboBoxRole.addActionListener(e -> {
            if (comboBoxRole.getSelectedItem().equals("Doctor")) {
                comboBoxSpecialization.setVisible(true);
            } else {
                comboBoxSpecialization.setVisible(false);
            }
        });

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(150, 230, 100, 25);
        panel.add(btnRegister);

        btnRegister.addActionListener(e -> registerUser());
    }

    private void registerUser() {
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());
        String role = comboBoxRole.getSelectedItem().toString().toLowerCase();  // Get selected role and convert to lowercase

        // Determine which table to insert into based on the selected role
        String sql = "";
        if (role.equals("patient")) {
            sql = "INSERT INTO patients (username, email, password) VALUES (?, ?, ?)";
        } else if (role.equals("doctor")) {
            sql = "INSERT INTO doctors (username, email, password, specialization) VALUES (?, ?, ?, ?)";
        } else if (role.equals("admin")) {
            sql = "INSERT INTO admins (username, email, password) VALUES (?, ?, ?)";
        }

        try {
            // Establish the connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javahealth", "root", "");
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);

            if (role.equals("doctor")) {
                // Include specialization in the insert statement
                String specialization = comboBoxSpecialization.getSelectedItem().toString();
                stmt.setString(4, specialization);
            }

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!");
            clearFields();

            // Redirect to Window_5 after successful registration
            Window_5 loginWindow = new Window_5();
            loginWindow.setVisible(true);
            dispose();  // Close the current registration window

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
        }
    }

    private void clearFields() {
        txtUsername.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        comboBoxRole.setSelectedIndex(0);  // Reset role selection to "Patient"
        comboBoxSpecialization.setVisible(false); // Hide specialization dropdown
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Window_10 frame = new Window_10();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
