package frontend;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * A modern, visually appealing login panel with improved user experience.
 */
public class LogInPanel extends JPanel {
    // Colors
    private static final Color PRIMARY_COLOR = new Color(66, 133, 244);
    private static final Color SECONDARY_COLOR = new Color(52, 168, 83);
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color TEXT_COLOR = new Color(60, 64, 67);
    private static final Color BORDER_COLOR = new Color(218, 220, 224);
    
    // Fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    // Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    /**
     * Creates a modern login panel
     * @param actionListener The listener to handle login actions
     */
    public LogInPanel(ActionListener actionListener) {
        // Set up panel properties
        setLayout(null);
        setBackground(BACKGROUND_COLOR);
        setSize(800, 600);
        
        // Create a container panel for better layout control
        JPanel loginContainer = new JPanel();
        loginContainer.setLayout(null);
        loginContainer.setBounds(200, 100, 400, 400);
        loginContainer.setBackground(Color.WHITE);
        loginContainer.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        // Welcome title
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBounds(30, 20, 340, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginContainer.add(titleLabel);
        
        // Username field with label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setBounds(30, 80, 340, 20);
        loginContainer.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setBounds(30, 105, 340, 35);
        usernameField.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        usernameField.setFont(LABEL_FONT);
        loginContainer.add(usernameField);
        
        // Password field with label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setBounds(30, 155, 340, 20);
        loginContainer.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(30, 180, 340, 35);
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setFont(LABEL_FONT);
        loginContainer.add(passwordField);
        
        // Log In button
        JButton logInButton = createStyledButton("Log In", PRIMARY_COLOR, Color.WHITE);
        logInButton.setBounds(30, 240, 340, 40);
        logInButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            actionListener.onActionPerformed("Log In", username, password);
        });
        loginContainer.add(logInButton);
        
        // Sign Up button
        JButton signUpButton = createStyledButton("Create Account", Color.WHITE, PRIMARY_COLOR);
        signUpButton.setBounds(30, 295, 160, 40);
        signUpButton.addActionListener(e -> 
            actionListener.onActionPerformed("Navigate Sign Up"));
        loginContainer.add(signUpButton);
        
        // Forgot Password button
        JButton forgotPasswordButton = createStyledButton("Forgot Password?", Color.WHITE, SECONDARY_COLOR);
        forgotPasswordButton.setBounds(210, 295, 160, 40);
        forgotPasswordButton.addActionListener(e -> 
            actionListener.onActionPerformed("Navigate Forgot Password"));
        loginContainer.add(forgotPasswordButton);
        
        add(loginContainer);
        setVisible(true);
    }
    
    /**
     * Creates a styled button with the specified colors
     */
    private JButton createStyledButton(String text, Color backgroundColor, Color foregroundColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(foregroundColor);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(backgroundColor == Color.WHITE);
        button.setBorder(new LineBorder(foregroundColor, 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (backgroundColor == Color.WHITE) {
                    button.setBackground(new Color(240, 240, 240));
                } else {
                    button.setBackground(backgroundColor.darker());
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    /**
     * Gets the entered username
     * @return username text
     */
    public String getUsername() {
        return usernameField.getText().trim();
    }
    
    /**
     * Gets the entered password
     * @return password text
     */
    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }
    
    /**
     * Clears both username and password fields
     */
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }
}