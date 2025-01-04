package javaHealth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Window_9 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea textArea;
    private JTextField doctorNameField;
    private JTextField appointmentIdField;
    private Window_8 parentWindow;  // Reference to the dashboard (Window_8)

    // Database connection object
    private Connection conn;

    /**
     * Create the frame.
     */
    public Window_9(Window_8 parent, Connection conn) {  // Constructor accepts a reference to the dashboard (Window_8) and the database connection
        this.parentWindow = parent;
        this.conn = conn;
        
        setTitle("Doctor Prescription");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        
        contentPane.setBackground(new Color(173, 216, 230)); // RGB values for baby blue
        setContentPane(contentPane);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10));
        contentPane.add(inputPanel, BorderLayout.NORTH);

        JLabel lblDoctorName = new JLabel("Doctor Name:");
        inputPanel.add(lblDoctorName);
        doctorNameField = new JTextField();
        inputPanel.add(doctorNameField);

        JLabel lblAppointmentId = new JLabel("Appointment ID:");
        inputPanel.add(lblAppointmentId);
        appointmentIdField = new JTextField();
        inputPanel.add(appointmentIdField);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        JButton btnSubmit = new JButton("Submit Prescription");
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitPrescription();
            }
        });
        buttonPanel.add(btnSubmit);

        // Add a Back button
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backToDashboard();  // Call method to return to the dashboard
            }
        });
        buttonPanel.add(btnBack);
    }

    // Method to handle prescription submission to the database
    private void submitPrescription() {
        String doctorName = doctorNameField.getText();
        String appointmentId = appointmentIdField.getText();
        String prescriptionText = textArea.getText();

        if (doctorName.isEmpty() || appointmentId.isEmpty() || prescriptionText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            // Database insertion
            try {
                String query = "INSERT INTO prescriptions (appointment_id, prescription_details) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(appointmentId)); // Cast appointmentId to int
                pstmt.setString(2, prescriptionText);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Prescription submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to submit prescription.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid appointment ID.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Method to clear input fields after submission
    private void clearFields() {
        doctorNameField.setText("");
        appointmentIdField.setText("");
        textArea.setText("");
    }

    // Method to navigate back to the previous window
    private void backToDashboard() {
        Window_9.this.setVisible(false);  // Hide this window
        parentWindow.setVisible(true);  // Show the previous window
    }
}
