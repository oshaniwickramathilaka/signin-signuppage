import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Calendar;

public class SignInSignUpApp extends JFrame {
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton signInButton, signUpButton;
    private JPanel mainPanel;

    private Connection conn;

    public SignInSignUpApp() {
        setTitle("Java Authentication Application");
        setSize(500, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Setting up the signup form section
        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new GridLayout(6, 2, 10, 10));

        // Name field
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));  
        signUpPanel.add(nameLabel);
        JTextField nameField = new JTextField();
        signUpPanel.add(nameField);

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));  
        signUpPanel.add(emailLabel);
        JTextField emailField = new JTextField();
        signUpPanel.add(emailField);

        // Gender field
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Arial", Font.BOLD, 16));  
        signUpPanel.add(genderLabel);
        JPanel genderPanel = new JPanel();
        JRadioButton maleButton = new JRadioButton("Male");
        JRadioButton femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        signUpPanel.add(genderPanel);

        // Birthday field
        JLabel birthdayLabel = new JLabel("Birthday");
        birthdayLabel.setFont(new Font("Arial", Font.BOLD, 16));  
        signUpPanel.add(birthdayLabel);
        JPanel birthdayPanel = new JPanel();

        // days dropdown list
        JComboBox<String> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(String.valueOf(i));
        }

        // months dropdown list
        JComboBox<String> monthComboBox = new JComboBox<>();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            monthComboBox.addItem(month);
        }

        // years dropdown list
        JComboBox<String> yearComboBox = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1900; i--) {
            yearComboBox.addItem(String.valueOf(i));
        }
        birthdayPanel.add(dayComboBox);
        birthdayPanel.add(monthComboBox);
        birthdayPanel.add(yearComboBox);
        signUpPanel.add(birthdayPanel);

        // password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));  
        signUpPanel.add(passwordLabel);
        JTextField signUpPasswordField = new JTextField();
        signUpPanel.add(signUpPasswordField);

        // robot check
        JCheckBox robotCheckBox = new JCheckBox("I am not a robot");
        signUpPanel.add(robotCheckBox);

        // signup button
        JButton signUpSubmitButton = new JButton("Sign Up");
        signUpSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpSubmitButton.setBackground(Color.BLUE);
        signUpSubmitButton.setForeground(Color.WHITE);
        signUpSubmitButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpSubmitButton.setFocusPainted(false);
        signUpSubmitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, signUpSubmitButton.getPreferredSize().height)); // Full width

        // signup action call on submit button click
        signUpSubmitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String gender = maleButton.isSelected() ? "Male" : "Female";
            String birthday = dayComboBox.getSelectedItem() + "-" + monthComboBox.getSelectedItem() + "-" + yearComboBox.getSelectedItem();
            String password = signUpPasswordField.getText();

            // validate the fields
            if (name.isEmpty() || email.isEmpty() || gender.isEmpty() || birthday.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required. Please fill them in.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // validate robot checkbox
            if (!robotCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please confirm you are not a robot.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            signUpUser(name, email, gender, birthday, password);
        });

        JLabel signUpTitle = new JLabel("Sign Up");
        signUpTitle.setFont(new Font("Arial", Font.BOLD, 18));
        signUpTitle.setForeground(Color.BLUE);
        signUpTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // Add padding for spacing
        mainPanel.add(signUpTitle);
        mainPanel.add(signUpPanel);
        mainPanel.add(signUpSubmitButton);

        // sign in section
        JPanel signInPanel = new JPanel();
        signInPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel signInTitle = new JLabel("Sign In");
        signInTitle.setForeground(Color.BLUE);
        signInTitle.setFont(new Font("Arial", Font.BOLD, 18));
        signInTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));  // Add padding for spacing
        mainPanel.add(signInTitle);

        JLabel signInEmailLabel = new JLabel("Email");
        signInEmailLabel.setFont(new Font("Arial", Font.BOLD, 16));  
        signInPanel.add(signInEmailLabel);
        usernameField = new JTextField();
        signInPanel.add(usernameField);

        JLabel signInPasswordLabel = new JLabel("Password");
        signInPasswordLabel.setFont(new Font("Arial", Font.BOLD, 16));  
        signInPanel.add(signInPasswordLabel);
        passwordField = new JTextField();
        signInPanel.add(passwordField);

        signInButton = new JButton("Sign In");
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setBackground(Color.BLUE);
        signInButton.setForeground(Color.WHITE);
        signInButton.setFont(new Font("Arial", Font.BOLD, 16));
        signInButton.setFocusPainted(false);

        signInButton.addActionListener(new SignInActionListener());
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, signInButton.getPreferredSize().height));

        mainPanel.add(signInPanel);
        mainPanel.add(signInButton);

        add(mainPanel, BorderLayout.CENTER);

        // connecting to the database
        connectToDatabase();
    }

    // sign in action
    private class SignInActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = usernameField.getText();
            String password = passwordField.getText();

            // validate the fields
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(SignInSignUpApp.this, "Both email and password are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            signInUser(email, password);
        }
    }

    // for connecting to the database
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/UserDB";
            String user = "root";
            String password = "1234";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // sign in handler
    private void signInUser(String email, String password) {
        try {
            String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Sign In Success!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred during login.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // signup handler
    private void signUpUser(String name, String email, String gender, String birthday, String password) {
        try {
            String query = "INSERT INTO Users (name, email, gender, birthday, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, gender);
            stmt.setString(4, birthday);
            stmt.setString(5, password);
            int result = stmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Sign Up Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error during sign up.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred during sign up.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignInSignUpApp app = new SignInSignUpApp();
            app.setVisible(true);
        });
    }
}
