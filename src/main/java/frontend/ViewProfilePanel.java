package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ViewProfilePanel extends JPanel {
    // Custom colors
    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color PANEL_COLOR = new Color(255, 255, 255);
    private static final Color HEADER_COLOR = new Color(50, 120, 200);
    private static final Color TEXT_COLOR = new Color(40, 40, 40);
    private static final Color BUTTON_COLOR = new Color(60, 130, 210);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color HIGHLIGHT_COLOR = new Color(250, 90, 90);
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
    
    ViewProfilePanel(ActionListener actionListener, String username, String name, String university, 
                    Date dob, Date dateJoined, int likes, char sex) {
        // Calculate age from date of birth
        int age = calculateAge(dob);

        // Set up panel properties
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(BACKGROUND_COLOR);

        // Create header panel with username and votes
        JPanel headerPanel = createHeaderPanel(username, likes);
        
        // Create profile info panel
        JPanel profileInfoPanel = createProfileInfoPanel(name, username, university, dob, age, dateJoined, sex);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel(actionListener, username);

        // Add panels to main panel
        add(headerPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(profileInfoPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(buttonPanel);
    }
    
    private int calculateAge(Date dob) {
        if (dob == null) return 0;
        
        Date currentDate = new Date();
        int age = currentDate.getYear() - dob.getYear();
        if (currentDate.getMonth() < dob.getMonth() || 
            (currentDate.getMonth() == dob.getMonth() && currentDate.getDate() < dob.getDate())) {
            age--;
        }
        return age;
    }
    
    private JPanel createHeaderPanel(String username, int likes) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Username label with large font
        JLabel usernameLabel = new JLabel("@" + username);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        usernameLabel.setForeground(Color.WHITE);
        
        // Likes display with custom look
        JPanel likesPanel = new JPanel();
        likesPanel.setLayout(new BoxLayout(likesPanel, BoxLayout.Y_AXIS));
        likesPanel.setBackground(HEADER_COLOR);
        
        JLabel likesValueLabel = new JLabel(String.valueOf(likes));
        likesValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        likesValueLabel.setForeground(Color.WHITE);
        likesValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel likesTextLabel = new JLabel("LIKES");
        likesTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        likesTextLabel.setForeground(new Color(220, 220, 220));
        likesTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        likesPanel.add(likesValueLabel);
        likesPanel.add(likesTextLabel);
        
        headerPanel.add(usernameLabel, BorderLayout.WEST);
        headerPanel.add(likesPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createProfileInfoPanel(String name, String username, String university, 
                                        Date dob, int age, Date dateJoined, char sex) {
        JPanel profileInfoPanel = new JPanel();
        profileInfoPanel.setLayout(new BoxLayout(profileInfoPanel, BoxLayout.Y_AXIS));
        profileInfoPanel.setBackground(PANEL_COLOR);
        
        // Create a compound border with rounded corners and title
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Profile Information");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(HEADER_COLOR);
        
        Border emptyBorder = new EmptyBorder(15, 15, 15, 15);
        CompoundBorder compoundBorder = new CompoundBorder(titledBorder, emptyBorder);
        profileInfoPanel.setBorder(compoundBorder);
        
        // Add profile information with proper spacing and styling
        addProfileField(profileInfoPanel, "Name", name);
        addProfileField(profileInfoPanel, "Username", username);
        addProfileField(profileInfoPanel, "University", university);
        addProfileField(profileInfoPanel, "Date of Birth", dob != null ? dateFormat.format(dob) : "N/A");
        addProfileField(profileInfoPanel, "Age", String.valueOf(age));
        addProfileField(profileInfoPanel, "Date Joined", dateJoined != null ? dateFormat.format(dateJoined) : "N/A");
        addProfileField(profileInfoPanel, "Sex", String.valueOf(sex));
        
        return profileInfoPanel;
    }
    
    private void addProfileField(JPanel panel, String label, String value) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(PANEL_COLOR);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JLabel fieldLabel = new JLabel(label + ":");
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fieldLabel.setForeground(TEXT_COLOR);
        fieldLabel.setPreferredSize(new Dimension(120, 25));
        
        JLabel fieldValue = new JLabel(value);
        fieldValue.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldValue.setForeground(TEXT_COLOR);
        
        fieldPanel.add(fieldLabel, BorderLayout.WEST);
        fieldPanel.add(fieldValue, BorderLayout.CENTER);
        
        panel.add(fieldPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    private JPanel createButtonPanel(ActionListener actionListener, String username) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(PANEL_COLOR);
        
        // Create a compound border with rounded corners and title
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Actions");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(HEADER_COLOR);
        
        Border emptyBorder = new EmptyBorder(15, 15, 15, 15);
        CompoundBorder compoundBorder = new CompoundBorder(titledBorder, emptyBorder);
        buttonPanel.setBorder(compoundBorder);
        
        // Create styled buttons
        JButton upvoteButton = createStyledButton("Upvote", BUTTON_COLOR);
        upvoteButton.addActionListener(e -> actionListener.onActionPerformed("upvote", username));
        
        JButton downvoteButton = createStyledButton("Downvote", HIGHLIGHT_COLOR);
        downvoteButton.addActionListener(e -> actionListener.onActionPerformed("downvote", username));
        
        JButton removeVoteButton = createStyledButton("Remove Vote", new Color(150, 150, 150));
        removeVoteButton.addActionListener(e -> actionListener.onActionPerformed("removeVote", username));
        
        JButton backButton = createStyledButton("Back", new Color(100, 100, 100));
        backButton.addActionListener(e -> actionListener.onActionPerformed("back"));
        
        buttonPanel.add(upvoteButton);
        buttonPanel.add(downvoteButton);
        buttonPanel.add(removeVoteButton);
        buttonPanel.add(backButton);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    public void onActionPerformed(String action, String... params) {
        switch (action) {
            case "showHome":
                showHomePanel();
                break;
            // Add other cases as needed
        }
    }

    private void showHomePanel() {
        // Implementation for showing the home panel
    }
}