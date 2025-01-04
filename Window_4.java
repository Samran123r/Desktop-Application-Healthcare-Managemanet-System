package javaHealth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Window_4 extends JFrame {
    private JTextArea feedbackTextArea;
    private int patientId;  // Store patient ID (since feedback is tied to patients)
    private int feedbackId; // Store feedback ID for deletion
    private Connection connection;

    public Window_4(Window_1 parent, int patientId) {  // Pass patientId instead of userId
        this.patientId = patientId;
        initialize();
        connectToDatabase();
        loadFeedback(); // Load existing feedback when the window is opened
    }

    private void initialize() {
        setTitle("View/Edit Feedback");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        feedbackTextArea = new JTextArea();
        feedbackTextArea.setBounds(20, 20, 540, 200);
        getContentPane().add(feedbackTextArea);

        JButton btnLoadFeedback = new JButton("Load Feedback");
        btnLoadFeedback.setBounds(20, 230, 150, 30);
        btnLoadFeedback.addActionListener(e -> loadFeedback());
        getContentPane().add(btnLoadFeedback);

        JButton btnUpdateFeedback = new JButton("Update Feedback");
        btnUpdateFeedback.setBounds(200, 230, 150, 30);
        btnUpdateFeedback.addActionListener(e -> updateFeedback());
        getContentPane().add(btnUpdateFeedback);

        JButton btnDeleteFeedback = new JButton("Delete Feedback");
        btnDeleteFeedback.setBounds(380, 230, 150, 30);
        btnDeleteFeedback.addActionListener(e -> deleteFeedback());
        getContentPane().add(btnDeleteFeedback);

        // Back Button to navigate to Window_1
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(20, 280, 150, 30);
        btnBack.addActionListener(e -> navigateBack());
        getContentPane().add(btnBack);
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/javahealth"; // Update with your database name
            String user = "root"; // MySQL username
            String password = ""; // MySQL password
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFeedback() {
        // Replace with logic to retrieve feedback based on patientId
        String query = "SELECT id, feedback_text FROM feedback WHERE patient_id = ? ORDER BY id DESC LIMIT 1"; // Gets the latest feedback
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, patientId);  // Use patient_id instead of user_id
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                feedbackId = rs.getInt("id"); // Store the feedback ID
                String feedback = rs.getString("feedback_text");
                feedbackTextArea.setText(feedback);
            } else {
                feedbackTextArea.setText("No feedback found.");
                feedbackId = -1; // No feedback ID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFeedback() {
        if (feedbackId != -1) {
            String updatedFeedback = feedbackTextArea.getText();
            String query = "UPDATE feedback SET feedback_text = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, updatedFeedback);
                pstmt.setInt(2, feedbackId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Feedback updated successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No feedback to update.");
        }
    }

    private void deleteFeedback() {
        if (feedbackId != -1) {
            String query = "DELETE FROM feedback WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, feedbackId);
                pstmt.executeUpdate();
                feedbackTextArea.setText(""); // Clear the text area
                feedbackId = -1; // Reset feedback ID
                JOptionPane.showMessageDialog(this, "Feedback deleted successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No feedback to delete.");
        }
    }

    // Navigate back to Window_1
    private void navigateBack() {
        Window_1 window1 = new Window_1(patientId); // Open Window_1 again with patientId
        window1.setVisible(true);
        dispose(); // Close the current window (Window_4)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Assuming patientId = 1 for testing. This value would be dynamic in the real app.
                Window_4 window = new Window_4(null, 1);
                window.setVisible(true);
            }
        });
    }
}
