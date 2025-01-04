package javaHealth;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class Window_6 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtAppointmentNo;
    private JTextField txtDoctorName;
    private JTextField txtPatientName;
    private JTextArea txtNotes; // Declare notes JTextArea
    private Connection connection;
    private JComboBox<String> comboBoxSpecialization; // Specialization JComboBox
    private int userId; // Store user ID

    public Window_6(int userId) {
        this.userId = userId; // Store user ID for later use
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500); // Increased width and height
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(255, 250, 250)); // Set baby blue background color
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Connect to the database
        connectToDatabase();

        // Doctor Details Label
        JLabel lblDoctorDetails = new JLabel("Doctor Details");
        lblDoctorDetails.setFont(new Font("Tahoma", Font.BOLD, 18)); // Increased font size
        lblDoctorDetails.setBounds(30, 20, 200, 30);
        contentPane.add(lblDoctorDetails);

        // Appointment No
        JLabel lblAppointmentNo = new JLabel("Appointment No:");
        lblAppointmentNo.setFont(new Font("Tahoma", Font.PLAIN, 14)); // Set font for labels
        lblAppointmentNo.setBounds(30, 70, 150, 25);
        contentPane.add(lblAppointmentNo);

        txtAppointmentNo = new JTextField();
        txtAppointmentNo.setBounds(180, 70, 180, 25);
        contentPane.add(txtAppointmentNo);
        txtAppointmentNo.setColumns(10);

        // Doctor Name
        JLabel lblDoctorName = new JLabel("Doctor Name:");
        lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblDoctorName.setBounds(30, 110, 150, 25);
        contentPane.add(lblDoctorName);

        txtDoctorName = new JTextField();
        txtDoctorName.setBounds(180, 110, 180, 25);
        contentPane.add(txtDoctorName);
        txtDoctorName.setColumns(10);

        // Specialization Dropdown
        JLabel lblSpecialization = new JLabel("Specialization:");
        lblSpecialization.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblSpecialization.setBounds(30, 150, 150, 25);
        contentPane.add(lblSpecialization);

        comboBoxSpecialization = new JComboBox<>();
        comboBoxSpecialization.setModel(new DefaultComboBoxModel<>(new String[]{"Cardiologist", "Neurologist", "Orthopedics", "Pediatrician", "Surgeon", "GeneralPhysician"}));
        comboBoxSpecialization.setBounds(180, 150, 220, 25);
        contentPane.add(comboBoxSpecialization);

        // Patient Details Label
        JLabel lblPatientDetails = new JLabel("Patient Details");
        lblPatientDetails.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblPatientDetails.setBounds(450, 20, 200, 30);
        contentPane.add(lblPatientDetails);

        // Patient Name
        JLabel lblPatientName = new JLabel("Name:");
        lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPatientName.setBounds(450, 70, 100, 25);
        contentPane.add(lblPatientName);

        txtPatientName = new JTextField();
        txtPatientName.setBounds(550, 70, 200, 25);
        contentPane.add(txtPatientName);
        txtPatientName.setColumns(10);

        // Patient Notes
        JLabel lblNotes = new JLabel("Notes:");
        lblNotes.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNotes.setBounds(450, 110, 100, 25);
        contentPane.add(lblNotes);

        txtNotes = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtNotes);
        scrollPane.setBounds(450, 140, 300, 150); // Adjust the size of the scroll pane
        contentPane.add(scrollPane);

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(30, 400, 100, 30);
        contentPane.add(btnBack);
        btnBack.addActionListener(e -> {
            Window_1 patientDashboard = new Window_1(userId); // Pass userId back to Window_1
            patientDashboard.setVisible(true);
            dispose(); // Close the current window
        });

        // Book Appointment Button
        JButton btnBookAppointment = new JButton("Book Appointment");
        btnBookAppointment.setBounds(150, 400, 150, 30);
        contentPane.add(btnBookAppointment);
        btnBookAppointment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to book appointment goes here
                String appointmentNo = txtAppointmentNo.getText();
                String doctorName = txtDoctorName.getText();
                String patientName = txtPatientName.getText();
                String notes = txtNotes.getText();
                String specialization = (String) comboBoxSpecialization.getSelectedItem();

                try {
                    // Fetch doctor_id based on doctorName and specialization
                    int doctorId = getDoctorId(doctorName, specialization);
                    if (doctorId == -1) {
                        JOptionPane.showMessageDialog(null, "Doctor not found. Please check the name and specialization.");
                        return;
                    }

                    String sql = "INSERT INTO appointments (id, patient_id, doctor_id, status, notes, specialization) VALUES (?, ?, ?, 'scheduled', ?, ?)";
                    PreparedStatement pst = connection.prepareStatement(sql);
                    pst.setString(1, appointmentNo); // Use the appointment number as the ID
                    pst.setInt(2, userId); // Assume userId is the patient ID
                    pst.setInt(3, doctorId); // Set doctor_id from the fetched doctor ID
                    pst.setString(4, notes);
                    pst.setString(5, specialization);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Appointment booked successfully!");

                    // Clear the input fields
                    txtAppointmentNo.setText("");
                    txtDoctorName.setText("");
                    txtPatientName.setText("");
                    txtNotes.setText("");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error booking appointment: " + ex.getMessage());
                }
            }
        });
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/javahealth", "root", "");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage());
        }
    }

    private int getDoctorId(String doctorName, String specialization) {
        int doctorId = -1;
        try {
            String sql = "SELECT id FROM doctors WHERE username = ? AND specialization = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, doctorName);
            pst.setString(2, specialization);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                doctorId = rs.getInt("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching doctor ID: " + e.getMessage());
        }
        return doctorId;
    }
}
