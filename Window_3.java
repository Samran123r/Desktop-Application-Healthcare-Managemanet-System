package javaHealth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.SQLException;

public class Window_3 extends JFrame {
    private JTextArea txtFeedback;
    private int patientId;  // Store patient ID instead of general user ID

    public Window_3(JFrame parent, int patientId) {  // Pass patientId instead of userId
        this.patientId = patientId; // Store patient ID for later use
        setTitle("Submit Feedback");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 456);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(211, 211, 211));
        setContentPane(panel);

        JLabel lblFeedback = new JLabel("Feedback:");
        lblFeedback.setBounds(60, 24, 100, 25);
        panel.add(lblFeedback);

        txtFeedback = new JTextArea();
        txtFeedback.setBounds(60, 50, 676, 257);
        panel.add(txtFeedback);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(612, 332, 124, 30);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitFeedback();
            }
        });
        panel.add(btnSubmit);

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(60, 332, 124, 30); // Positioning the back button
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigateBack();
            }
        });
        panel.add(btnBack);
    }

    private void submitFeedback() {
        String feedbackText = txtFeedback.getText();

        // Insert feedback into the database, associating with patient_id
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javahealth", "root", "");
            String sql = "INSERT INTO feedback (patient_id, feedback_text, status) VALUES (?, ?, 'pending')";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, patientId);  // Set patient ID
            stmt.setString(2, feedbackText);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Feedback submitted successfully!");
            txtFeedback.setText(""); // Clear feedback text area
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to submit feedback. Please try again.");
        }
    }

    // Navigate back to Window_1
    private void navigateBack() {
        Window_1 window1 = new Window_1(patientId); // Open Window_1 again with patientId
        window1.setVisible(true);
        dispose(); // Close the current window (Window_3)
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Assuming patientId = 1 for testing. This value would be dynamic in the real app.
                Window_3 window = new Window_3(null, 1);
                window.setVisible(true);
            }
        });
    }
}
