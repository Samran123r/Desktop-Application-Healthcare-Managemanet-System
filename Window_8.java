package javaHealth;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Window_8 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Window_2 parentWindow;  // Reference to Window_2 (admin dashboard)
    private JButton btnEditNotes;    // Button for editing notes
    private JButton btnDeleteAppointment; // Button for deleting the appointment
    private DefaultTableModel model;  // Model for the table

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Window_2 adminWindow = new Window_2();  // Create an instance of Window_2
                    adminWindow.getFrame().setVisible(true); // Show the admin window
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Constructor that accepts a reference to the admin dashboard (Window_2).
     */
    public Window_8(Window_2 parent) {
        this.parentWindow = parent;  // Initialize reference to parent window
        
        setTitle("Appointments Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 400);  // Adjusted window size for better table view
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        // Table to display appointments
        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        table = new JTable();
        scrollPane.setViewportView(table);

        // Load appointments from the database when the dashboard is opened
        loadAppointments();

        // Panel for buttons at the bottom
        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        // Button to make a prescription
        JButton btnMakePrescription = new JButton("Make Prescription");
        btnMakePrescription.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Connection connection = createConnection();  // Create the connection here
                if (connection != null) {
                    Window_9 prescriptionWindow = new Window_9(Window_8.this, connection);  // Pass connection
                    prescriptionWindow.setVisible(true);
                    Window_8.this.setVisible(false);  // Hide the current dashboard
                } else {
                    JOptionPane.showMessageDialog(Window_8.this, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(btnMakePrescription);
        
        // Button to cancel all appointments
        JButton btnCancelAppointment = new JButton("Cancel All Appointments");
        btnCancelAppointment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelAllAppointments();  // Call method to delete all appointments
            }
        });
        buttonPanel.add(btnCancelAppointment);

        // Button to edit notes of the selected appointment
        btnEditNotes = new JButton("Edit Notes");
        btnEditNotes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editNotes();  // Call method to edit notes
            }
        });
        buttonPanel.add(btnEditNotes);

        // Button to delete the last submitted appointment
        btnDeleteAppointment = new JButton("Delete Last Appointment");
        btnDeleteAppointment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteLastAppointment();  // Call method to delete the last appointment
            }
        });
        buttonPanel.add(btnDeleteAppointment);

        // Add a Back button to navigate back to the admin dashboard (Window_2)
        JButton btnBack = new JButton("Back to Admin Dashboard");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (parentWindow != null) {
                    Window_8.this.setVisible(false);  // Hide this window
                    parentWindow.getFrame().setVisible(true);  // Show the admin dashboard
                }
            }
        });
        buttonPanel.add(btnBack);
    }

    // Method to create a database connection
    private Connection createConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/javahealth", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to load appointments from the database
    private void loadAppointments() {
        model = new DefaultTableModel(new String[]{
            "Appointment No", "Doctor Name", "Specialization", "Patient Name", "Notes"
        }, 0);

        // Connect to the database and retrieve appointment data
        try (Connection connection = createConnection()) {
            String sql = "SELECT a.id, d.username AS doctor_name, d.specialization, p.username AS patient_name, a.notes " +
                         "FROM appointments a " +
                         "JOIN doctors d ON a.doctor_id = d.id " +
                         "JOIN patients p ON a.patient_id = p.id";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Populate the table with data from the appointments
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("doctor_name"),
                    rs.getString("specialization"),
                    rs.getString("patient_name"),
                    rs.getString("notes")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setModel(model);
    }

    // Method to edit notes of the selected appointment
    private void editNotes() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String newNotes = JOptionPane.showInputDialog(this, "Enter new notes:", model.getValueAt(selectedRow, 4));
            if (newNotes != null) {
                int appointmentId = (int) model.getValueAt(selectedRow, 0); // Get appointment ID
                updateNotesInDatabase(appointmentId, newNotes); // Update notes in the database
                model.setValueAt(newNotes, selectedRow, 4); // Update notes in the table
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an appointment to edit notes.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Method to update notes in the database
    private void updateNotesInDatabase(int appointmentId, String newNotes) {
        try (Connection connection = createConnection()) {
            String sql = "UPDATE appointments SET notes = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, newNotes);
            stmt.setInt(2, appointmentId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Notes updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating notes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete the last submitted appointment
    private void deleteLastAppointment() {
        if (model.getRowCount() > 0) {
            int lastRowIndex = model.getRowCount() - 1;
            int appointmentId = (int) model.getValueAt(lastRowIndex, 0); // Get last appointment ID
            
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the last appointment?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteAppointmentFromDatabase(appointmentId); // Delete appointment from database
                model.removeRow(lastRowIndex); // Remove row from table
                JOptionPane.showMessageDialog(this, "Appointment deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No appointments available to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Method to delete appointment from the database
    private void deleteAppointmentFromDatabase(int appointmentId) {
        try (Connection connection = createConnection()) {
            String sql = "DELETE FROM appointments WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting appointment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to "cancel" all appointments by clearing the table
    private void cancelAllAppointments() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);  // Clear all rows
    }
}
