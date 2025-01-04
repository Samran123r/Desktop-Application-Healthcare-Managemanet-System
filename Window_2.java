package javaHealth;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class Window_2 {
    private JFrame frame;

    /**
     * Create the application.
     */
    public Window_2() {
        initialize();
    }

    /**
     * Getter for the frame.
     */
    public JFrame getFrame() {
        return this.frame;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(211, 211, 211));
        frame.getContentPane().setLayout(null);

        // Navigation panel (Left side - navy blue)
        JPanel panel = new JPanel();
        panel.setBackground(new Color(100, 149, 237));
        panel.setBounds(0, 0, 197, 461); // Adjusted to start at y=0
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JButton btnAppointments = new JButton("Appointments");
        btnAppointments.setForeground(new Color(100, 149, 237));
        btnAppointments.setBackground(new Color(255, 250, 250));
        btnAppointments.setBounds(37, 262, 130, 28);
        panel.add(btnAppointments);

        // Action listener to navigate to Window_8 when the appointment button is clicked
        btnAppointments.addActionListener(e -> {
            frame.setVisible(false);  // Hide the current frame
            Window_8 appointmentWindow = new Window_8(this);  // Create a new instance of Window_8 and pass reference to this frame
            appointmentWindow.setVisible(true);  // Show Window_8 (Appointments window)
        });

        // Change the button name to "Prescriptions"
        JButton btnPrescriptions = new JButton("Prescriptions");
        btnPrescriptions.setForeground(new Color(0, 0, 128));
        btnPrescriptions.setBackground(new Color(255, 250, 250));
        btnPrescriptions.setBounds(37, 216, 130, 28);
        panel.add(btnPrescriptions);

        // Action listener to navigate to Window_11 when the prescriptions button is clicked
        btnPrescriptions.addActionListener(e -> {
            frame.setVisible(false);  // Hide the current frame
            Window_11 prescriptionWindow = new Window_11(this);  // Create a new instance of Window_11 and pass reference to this frame
            prescriptionWindow.setVisible(true);  // Show Window_11 (Prescriptions window)
        });

        // Other buttons (disabled)
        JButton btnManagePatients = new JButton("Manage Patients");
        btnManagePatients.setEnabled(false);
        btnManagePatients.setForeground(new Color(0, 0, 128));
        btnManagePatients.setBackground(new Color(255, 250, 250));
        btnManagePatients.setBounds(37, 168, 130, 28);
        panel.add(btnManagePatients);

        JButton btnManageDoctors = new JButton("Manage Doctors");
        btnManageDoctors.setEnabled(false);
        btnManageDoctors.setForeground(new Color(0, 0, 128));
        btnManageDoctors.setBackground(new Color(255, 250, 250));
        btnManageDoctors.setBounds(37, 123, 130, 28);
        panel.add(btnManageDoctors);

        JButton btnProfile = new JButton("Profile");
        btnProfile.setEnabled(false);
        btnProfile.setBackground(new Color(255, 250, 250));
        btnProfile.setForeground(new Color(100, 149, 237));
        btnProfile.setBounds(37, 75, 128, 28);
        panel.add(btnProfile);

        JButton btnSettings = new JButton("Settings");
        btnSettings.setEnabled(false);
        btnSettings.setForeground(new Color(0, 0, 128));
        btnSettings.setBackground(new Color(255, 250, 250));
        btnSettings.setBounds(37, 307, 130, 28);
        panel.add(btnSettings);

        // Right side - Profile panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(128, 128, 128));
        rightPanel.setBounds(200, 0, 580, 461);
        rightPanel.setLayout(null);
        frame.getContentPane().add(rightPanel);

        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(new Color(255, 250, 250));
        profilePanel.setBounds(21, 11, 538, 119);
        profilePanel.setLayout(null);
        profilePanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 2), "", TitledBorder.LEFT, TitledBorder.TOP));
        rightPanel.add(profilePanel);

        JPanel userPanel = new JPanel();
        userPanel.setBackground(new Color(255, 250, 250));
        userPanel.setBounds(360, 0, 220, 119);
        userPanel.setLayout(null);
        userPanel.setBorder(new LineBorder(Color.GRAY, 2));
        profilePanel.add(userPanel);

        JLabel profilePicLabel = new JLabel("");
        ImageIcon profileIcon = new ImageIcon("C:\\Users\\vsdia\\eclipse-workspace\\javaHealth\\src\\images\\profile.JPEG");
        ImageIcon resizedIcon = new ImageIcon(profileIcon.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH));
        profilePicLabel.setIcon(resizedIcon);
        profilePicLabel.setBounds(86, 25, 80, 80);
        userPanel.add(profilePicLabel);

        JLabel userNameLabel = new JLabel("Sophia");
        userNameLabel.setForeground(new Color(128, 128, 128));
        userNameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        userNameLabel.setBounds(10, 84, 104, 20);
        userPanel.add(userNameLabel);

        JLabel UserName = new JLabel("A365");
        UserName.setForeground(new Color(128, 128, 128));
        UserName.setFont(new Font("Tahoma", Font.BOLD, 14));
        UserName.setBounds(10, 65, 46, 14);
        userPanel.add(UserName);

        JLabel welcomeLabel = new JLabel("Welcome Admin!");
        welcomeLabel.setForeground(new Color(100, 149, 237));
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        welcomeLabel.setBounds(10, 11, 182, 22);
        profilePanel.add(welcomeLabel);

        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Main method to run the application (for testing)
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Window_2 window = new Window_2();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
