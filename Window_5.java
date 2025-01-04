package javaHealth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Window_5 extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public Window_5() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(211, 211, 211));
        setContentPane(panel);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 50, 100, 25);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 50, 200, 25);
        panel.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 100, 100, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 100, 200, 25);
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 150, 100, 30);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
        panel.add(btnLogin);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(260, 150, 100, 30);
        btnRegister.addActionListener(e -> {
            Window_10 registrationWindow = new Window_10();
            registrationWindow.setVisible(true);
            setVisible(false);
        });
        panel.add(btnRegister);
    }

    private void loginUser() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javahealth", "root", "");

            // First, check if the user is a patient
            if (checkUserRole(connection, "patients", username, password)) {
                int userId = getUserId(connection, "patients", username);
                Window_1 patientDashboard = new Window_1(userId);
                patientDashboard.setVisible(true);
            }
            // Next, check if the user is a doctor
            else if (checkUserRole(connection, "doctors", username, password)) {
                int userId = getUserId(connection, "doctors", username);
                Window_1 doctorDashboard = new Window_1(userId);
                doctorDashboard.setVisible(true);
            }
            // Finally, check if the user is an admin
            else if (checkUserRole(connection, "admins", username, password)) {
                Window_2 adminDashboard = new Window_2();
                adminDashboard.getFrame().setVisible(true);
            } 
            // If no match found, show error
            else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
            }

            dispose(); // Close the login window after successful login
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login failed. Please try again.");
        }
    }

    // Check if the user exists in the specified table with the given username and password
    private boolean checkUserRole(Connection connection, String tableName, String username, String password) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE username=? AND password=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    // Get the user ID from the specified table based on the username
    private int getUserId(Connection connection, String tableName, String username) throws SQLException {
        String sql = "SELECT id FROM " + tableName + " WHERE username=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            throw new SQLException("User not found.");
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Window_5 window = new Window_5();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
