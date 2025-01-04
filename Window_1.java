package javaHealth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window_1 extends JFrame {
    private JPanel contentPane;
    private int userId;

    public Window_1(int userId) {
        this.userId = userId; // Store user ID for later use
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 456);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // User Image
        JLabel userImage = new JLabel();
        userImage.setIcon(new ImageIcon("path_to_user_image.jpg")); // Update with the actual path to the user image
        userImage.setBounds(624, 55, 150, 150); // Adjust size and position as necessary
        contentPane.add(userImage);

        // Navigation Bar Panel
        JPanel navPanel = new JPanel();
        navPanel.setBounds(10, 11, 764, 33);
        navPanel.setBackground(new Color(192, 192, 192));
        contentPane.add(navPanel);
        navPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Submit Feedback Button
        JButton btnFeedback = new JButton("Submit Feedback");
        btnFeedback.addActionListener(e -> {
            Window_3 feedbackSubmissionWindow = new Window_3(this, userId);
            feedbackSubmissionWindow.setVisible(true);
            setVisible(false);
        });
        navPanel.add(btnFeedback);

        // View/Edit Feedback Button
        JButton btnViewFeedback = new JButton("View/Edit Feedback");
        btnViewFeedback.addActionListener(e -> {
            Window_4 feedbackViewWindow = new Window_4(this, userId);
            feedbackViewWindow.setVisible(true);
            setVisible(false);
        });
        navPanel.add(btnViewFeedback);

        // Profile Button
        JButton btnProfile = new JButton("Profile");
        btnProfile.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Profile feature is under development.");
        });
        navPanel.add(btnProfile);

        // Appointments Button
        JButton btnAppointments = new JButton("Appointments");
        btnAppointments.addActionListener(e -> {
            Window_6 appointmentWindow = new Window_6(userId); // Pass userId to Window_6
            appointmentWindow.setVisible(true);
            setVisible(false);
        });
        navPanel.add(btnAppointments);

        // Logout Button
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            Window_5 loginWindow = new Window_5();
            loginWindow.setVisible(true);
            dispose();
        });
        navPanel.add(btnLogout);
    }
}
