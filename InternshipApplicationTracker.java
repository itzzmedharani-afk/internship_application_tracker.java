    import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;

public class InternshipApplicationTracker extends JFrame {

    JTextField companyField, roleField, dateField;
    JTextField referralField, interviewField, actionField;
    JComboBox<String> statusBox;
    JTable table;
    DefaultTableModel model;
    JLabel totalLabel;

    Color lightBlue = new Color(220, 240, 255);
    Color darkBlue = new Color(40, 110, 180);

    InternshipApplicationTracker() {

        setTitle("Internship Application Tracker");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(lightBlue);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(lightBlue);

        JLabel title = new JLabel(
            "  INTERNSHIP APPLICATION TRACKER");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(darkBlue);

        JLabel subtitle = new JLabel(
            "  Stay organized. Track progress. Land your next opportunity.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));

        header.add(title);
        header.add(subtitle);
        mainPanel.add(header, BorderLayout.NORTH);

        // Input form
        JPanel form = new JPanel(new GridLayout(0, 4, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(darkBlue, 2),
            "Add New Application",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            darkBlue
        ));

        companyField = new JTextField();
        roleField = new JTextField();
        dateField = new JTextField();
        referralField = new JTextField();
        interviewField = new JTextField();
        actionField = new JTextField();

        statusBox = new JComboBox<>(new String[]{
            "Applied", "Interviewing", "Offer",
            "Follow Up", "Rejected"
        });

        addField(form, "Company:", companyField);
        addField(form, "Role:", roleField);
        addField(form, "Date Applied:", dateField);
        addField(form, "Status:", statusBox);
        addField(form, "Referral:", referralField);
        addField(form, "Interview:", interviewField);
        addField(form, "Next Action:", actionField);

        JButton submitButton = createButton("SUBMIT");
        JButton deleteButton = createButton("DELETE SELECTED");
        JButton clearButton = createButton("CLEAR FIELDS");

        form.add(submitButton);
        form.add(deleteButton);
        form.add(clearButton);

        // Table
        String[] columns = {
            "Company", "Role", "Date Applied", "Status",
            "Referral", "Interview", "Next Action"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Sample applications
        model.addRow(new Object[]{
            "Amazon", "Software Engineering Intern",
            "May 20, 2026", "Applied",
            "Jane Smith", "Not Scheduled", "Follow up"
        });

        model.addRow(new Object[]{
            "Microsoft", "Product Management Intern",
            "May 18, 2026", "Interviewing",
            "LinkedIn", "Phone Screen", "Prepare"
        });

        model.addRow(new Object[]{
            "Google", "Data Science Intern",
            "May 15, 2026", "Interviewing",
            "Campus Event", "Online Interview", "Review notes"
        });

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setSelectionBackground(new Color(180, 220, 255));
        table.setGridColor(new Color(190, 215, 235));
        table.setShowGrid(true);

        table.getTableHeader().setBackground(darkBlue);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setPreferredSize(
            new Dimension(0, 38));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(
            darkBlue, 2));

        // Center section
        JPanel centerPanel = new JPanel(new BorderLayout(10, 15));
        centerPanel.setBackground(lightBlue);
        centerPanel.add(form, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(lightBlue);

        totalLabel = new JLabel();
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(darkBlue);

        JButton exitButton = createButton("EXIT");

        footer.add(totalLabel, BorderLayout.WEST);
        footer.add(exitButton, BorderLayout.EAST);

        mainPanel.add(footer, BorderLayout.SOUTH);

        // Submit action
        submitButton.addActionListener(e -> {
            String company = companyField.getText().trim();
            String role = roleField.getText().trim();
            String date = dateField.getText().trim();

            if (company.isEmpty() || role.isEmpty()
                    || date.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter Company, Role and Date Applied.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            model.addRow(new Object[]{
                company,
                role,
                date,
                statusBox.getSelectedItem(),
                referralField.getText().trim().isEmpty()
                    ? "-" : referralField.getText().trim(),
                interviewField.getText().trim().isEmpty()
                    ? "-" : interviewField.getText().trim(),
                actionField.getText().trim().isEmpty()
                    ? "-" : actionField.getText().trim()
            });

            updateTotal();
            clearFields();

            JOptionPane.showMessageDialog(
                this, "Application added successfully!");
        });

        // Delete action
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow >= 0) {
                model.removeRow(selectedRow);
                updateTotal();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Please select a row to delete.");
            }
        });

        // Clear action
        clearButton.addActionListener(e -> clearFields());

        // Exit action
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Application",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        updateTotal();
        setVisible(true);
    }

    void addField(JPanel panel, String label, JComponent field) {
        JLabel text = new JLabel(label);
        text.setFont(new Font("Arial", Font.BOLD, 12));
        text.setForeground(darkBlue);

        field.setPreferredSize(new Dimension(150, 28));
        panel.add(text);
        panel.add(field);
    }

    JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(darkBlue);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(darkBlue, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    void clearFields() {
        companyField.setText("");
        roleField.setText("");
        dateField.setText("");
        referralField.setText("");
        interviewField.setText("");
        actionField.setText("");
        statusBox.setSelectedIndex(0);
        companyField.requestFocus();
    }

    void updateTotal() {
        totalLabel.setText(
            "Total Applications: " + model.getRowCount());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new InternshipApplicationTracker());
    }
}