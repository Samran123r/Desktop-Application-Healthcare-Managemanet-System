package javaHealth;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.YearMonth;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Window_7 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField nameOnCardField;
    private JTextField cardNumberField;
    private JTextField expiryDateField;
    private JTextField cvvField;
    private JLabel totalLabel;
    private int appointmentId;  // Added for appointment association

    // Constructor with appointment ID passed for associating payment with an appointment
    public Window_7(int appointmentId) {
        this.appointmentId = appointmentId;
        initialize();
    }

    public Window_7() {
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Window_7 frame = new Window_7();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Initialize the frame.
     */
    private void initialize() {
        setTitle("Payment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 801, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Set background color to gray
        contentPane.setBackground(new Color(128, 128, 128));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel formPanel = new JPanel();
        formPanel.setBounds(93, 36, 608, 393);
        formPanel.setBackground(new Color(255, 250, 250));
        formPanel.setLayout(null);

        JLabel lblPayment = new JLabel("Payment");
        lblPayment.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblPayment.setBounds(254, 27, 135, 40);
        formPanel.add(lblPayment);

        JLabel label = new JLabel("Name on Card:");
        label.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label.setBounds(63, 79, 201, 46);
        formPanel.add(label);
        nameOnCardField = new JTextField();
        nameOnCardField.setBounds(324, 94, 218, 28);
        formPanel.add(nameOnCardField);

        JLabel label_1 = new JLabel("Card Number (16 digits):");
        label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_1.setBounds(63, 125, 201, 46);
        formPanel.add(label_1);
        cardNumberField = new JTextField();
        cardNumberField.setBounds(324, 140, 218, 28);
        formPanel.add(cardNumberField);

        JLabel label_2 = new JLabel("Expiry Date (MM/YY):");
        label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_2.setBounds(63, 174, 201, 46);
        formPanel.add(label_2);
        expiryDateField = new JTextField();
        expiryDateField.setBounds(324, 189, 218, 28);
        formPanel.add(expiryDateField);

        JLabel label_3 = new JLabel("CVV:");
        label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_3.setBounds(63, 221, 201, 46);
        formPanel.add(label_3);
        cvvField = new JTextField();
        cvvField.setBounds(324, 239, 218, 28);
        formPanel.add(cvvField);

        JLabel label_4 = new JLabel("Total Amount:");
        label_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_4.setBounds(63, 270, 201, 46);
        formPanel.add(label_4);
        totalLabel = new JLabel("                                      2500 LKR");
        totalLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        totalLabel.setBounds(324, 278, 218, 34);
        formPanel.add(totalLabel);

        JButton btnSubmit = new JButton("Submit Payment");
        btnSubmit.setBounds(399, 340, 142, 34);
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitPayment();
            }
        });
        formPanel.add(btnSubmit);

        contentPane.add(formPanel);
    }

    // Method to handle payment submission
    private void submitPayment() {
        String nameOnCard = nameOnCardField.getText();
        String cardNumber = cardNumberField.getText();
        String expiryDate = expiryDateField.getText();
        String cvv = cvvField.getText();

        // Validate input
        if (nameOnCard.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (cardNumber.length() != 16) {
            JOptionPane.showMessageDialog(this, "Card number must be 16 digits.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {  
            JOptionPane.showMessageDialog(this, "Please enter a valid expiry date (MM/YY).", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isDateValid(expiryDate)) {
            JOptionPane.showMessageDialog(this, "The card expiry date is in the past.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (cvv.length() != 3) {
            JOptionPane.showMessageDialog(this, "CVV must be 3 digits.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Process the payment and update the database
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javahealth", "root", "")) {
                String sql = "UPDATE appointments SET notes = ? WHERE id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, "Payment of 2500 LKR processed for appointment " + appointmentId);
                stmt.setInt(2, appointmentId);
                
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Payment submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update payment status.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                // Clear fields after submission
                nameOnCardField.setText("");
                cardNumberField.setText("");
                expiryDateField.setText("");
                cvvField.setText("");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Payment submission failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to check if the expiry date is valid and not in the past
    private boolean isDateValid(String expiryDate) {
        String[] parts = expiryDate.split("/"); // Split the date into MM and YY
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt("20" + parts[1]); 

        // Get the current year and month
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth cardYearMonth = YearMonth.of(year, month);

        // Check if the card expiry date is in the future or the current month
        return !cardYearMonth.isBefore(currentYearMonth);
    }
}
