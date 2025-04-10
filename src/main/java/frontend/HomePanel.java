package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import frontend.components.ModernButton;
import frontend.theme.ModernTheme;

public class HomePanel extends JPanel {
    public HomePanel(ActionListener actionListener, String fetchedUsername, String fetchedName, int upvoteCount) {
        setLayout(new BorderLayout(ModernTheme.SPACING, ModernTheme.SPACING));
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        // Profile Card
        JPanel profileCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ModernTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(),
                        ModernTheme.BORDER_RADIUS, ModernTheme.BORDER_RADIUS));
                g2.dispose();
            }
        };
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBackground(ModernTheme.SURFACE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(24, 24, 24, 24),
            BorderFactory.createLineBorder(ModernTheme.BORDER, 1, true)
        ));

        // Profile Header
        JLabel headerLabel = new JLabel("Profile Information");
        headerLabel.setFont(ModernTheme.HEADING);
        headerLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileCard.add(headerLabel);
        profileCard.add(Box.createRigidArea(new Dimension(0, ModernTheme.SPACING)));

        // Profile Details
        addProfileDetail(profileCard, "Name", fetchedName);
        addProfileDetail(profileCard, "Username", fetchedUsername);
        addProfileDetail(profileCard, "Upvotes", String.valueOf(upvoteCount));
        profileCard.add(Box.createRigidArea(new Dimension(0, ModernTheme.SPACING * 2)));

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // Action Buttons
  ModernButton viewProfileBtn = new ModernButton("View Profile", ModernTheme.PRIMARY, ModernTheme.PRIMARY_HOVER);
        viewProfileBtn.addActionListener(e -> actionListener.onActionPerformed("viewProfile", fetchedUsername));
        
        ModernButton upvoteBtn = new ModernButton("Upvote", ModernTheme.PRIMARY, ModernTheme.PRIMARY_HOVER);
        upvoteBtn.addActionListener(e -> actionListener.onActionPerformed("upvote", fetchedUsername));
        
        ModernButton downvoteBtn = new ModernButton("Downvote", ModernTheme.PRIMARY, ModernTheme.PRIMARY_HOVER);
        downvoteBtn.addActionListener(e -> actionListener.onActionPerformed("downvote", fetchedUsername));
        
        ModernButton nextBtn = new ModernButton("Next Profile", ModernTheme.PRIMARY, ModernTheme.PRIMARY_HOVER);
        nextBtn.addActionListener(e -> actionListener.onActionPerformed("next"));
        
        ModernButton deleteBtn = new ModernButton("Delete Profile", ModernTheme.DANGER, 
                new Color(ModernTheme.DANGER.getRed() - 20, ModernTheme.DANGER.getGreen() - 20, ModernTheme.DANGER.getBlue() - 20));

        // Add buttons with spacing
        buttonPanel.add(viewProfileBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, ModernTheme.SPACING)));
        buttonPanel.add(upvoteBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, ModernTheme.SPACING)));
        buttonPanel.add(downvoteBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, ModernTheme.SPACING)));
        buttonPanel.add(nextBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, ModernTheme.SPACING )));
        buttonPanel.add(deleteBtn);

        profileCard.add(buttonPanel);
        add(profileCard, BorderLayout.CENTER);
    }

    private void addProfileDetail(JPanel panel, String label, String value) {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setOpaque(false);
        detailPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(ModernTheme.BODY);
        labelText.setForeground(ModernTheme.TEXT_SECONDARY);
        
        JLabel valueText = new JLabel(value);
        valueText.setFont(ModernTheme.TITLE);
        valueText.setForeground(ModernTheme.TEXT_PRIMARY);

        detailPanel.add(labelText);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        detailPanel.add(valueText);
        detailPanel.add(Box.createRigidArea(new Dimension(0, ModernTheme.SPACING)));

        panel.add(detailPanel);
    }
}