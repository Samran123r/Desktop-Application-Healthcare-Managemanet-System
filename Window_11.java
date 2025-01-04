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

public class Window_11 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Window_2 parentWindow;  // Reference to Window_2 (admin dashboard)
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
    public Window_11(Window_2 parent) {
        this.parentWindow = parent;  // Initialize reference to parent window
        
        setTitle("Prescriptions Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 400);  // Adjusted window size for better table view
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        // Table to display prescriptions
        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        table = new JTable();
        scrollPane.setViewportView(table);

        // Load prescriptions from the database when the dashboard is opened
        loadPrescriptions();

        // Panel for buttons at the bottom
        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        // Button to edit prescription details
        JButton btnEditPrescription = new JButton("Edit Prescription");
        btnEditPrescription.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editPrescription();  // Call method to edit prescription details
            }
        });
        buttonPanel.add(btnEditPrescription);

        // Button to delete the last submitted prescription
        JButton btnDeletePrescription = new JButton("Delete Last Prescription");
        btnDeletePrescription.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteLastPrescription();  // Call method to delete the last prescription
            }
        });
        buttonPanel.add(btnDeletePrescription);

        // Add a Back button to navigate back to the admin dashboard (Window_2)
        JButton btnBack = new JButton("Back to Admin Dashboard");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (parentWindow != null) {
                    Window_11.this.setVisible(false);  // Hide this window
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

    // Method to load prescriptions from the database
    private void loadPrescriptions() {
        model = new DefaultTableModel(new String[]{
            "Prescription ID", "Appointment ID", "Prescription Details"
        }, 0);

        // Connect to the database and retrieve prescription data
        try (Connection connection = createConnection()) {
            String sql = "SELECT p.id, p.appointment_id, p.prescription_details " +
                         "FROM prescriptions p";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Populate the table with data from the prescriptions
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("appointment_id"),
                    rs.getString("prescription_details")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setModel(model);
    }

    // Method to edit prescription details
    private void editPrescription() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String newDetails = JOptionPane.showInputDialog(this, "Enter new prescription details:", model.getValueAt(selectedRow, 2));
            if (newDetails != null) {
                int prescriptionId = (int) model.getValueAt(selectedRow, 0); // Get prescription ID
                updatePrescriptionInDatabase(prescriptionId, newDetails); // Update prescription in the database
                model.setValueAt(newDetails, selectedRow, 2); // Update prescription in the table
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a prescription to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Method to update prescription in the database
    private void updatePrescriptionInDatabase(int prescriptionId, String newDetails) {
        try (Connection connection = createConnection()) {
            String sql = "UPDATE prescriptions SET prescription_details = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, newDetails);
            stmt.setInt(2, prescriptionId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Prescription updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating prescription.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete the last submitted prescription
    private void deleteLastPrescription() {
        if (model.getRowCount() > 0) {
            int lastRowIndex = model.getRowCount() - 1;
            int prescriptionId = (int) model.getValueAt(lastRowIndex, 0); // Get last prescription ID
            
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the last prescription?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deletePrescriptionFromDatabase(prescriptionId); // Delete prescription from database
                model.removeRow(lastRowIndex); // Remove row from table
                JOptionPane.showMessageDialog(this, "Prescription deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No prescriptions available to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Method to delete prescription from the database
    private void deletePrescriptionFromDatabase(int prescriptionId) {
        try (Connection connection = createConnection()) {
            String sql = "DELETE FROM prescriptions WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, prescriptionId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting prescription.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
