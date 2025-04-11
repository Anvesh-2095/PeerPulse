package frontend.user;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ViewMyProfile extends JPanel
{
	ViewMyProfile(ActionListener actionListener, String username, String name, String university,
				  Date dob, Date dateJoined, int likes, char sex)
	{
		int age = 0;
		// Calculate age from date of birth
		if (dob != null)
		{
			Date currentDate = new Date();
			age = currentDate.getYear() - dob.getYear();
			if (currentDate.getMonth() < dob.getMonth() ||
						(currentDate.getMonth() == dob.getMonth() && currentDate.getDate() < dob.getDate()))
			{
				age--;
			}
		}

		// Set layout manager for the main panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Create profile info panel with vertical BoxLayout
		JPanel profileInfoPanel = new JPanel();
		profileInfoPanel.setLayout(new BoxLayout(profileInfoPanel, BoxLayout.Y_AXIS));
		profileInfoPanel.setBorder(BorderFactory.createTitledBorder("My Profile Information"));

		// Add profile information with proper spacing
		JLabel nameLabel = new JLabel("Name: " + name);
		nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(nameLabel);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel usernameLabel = new JLabel("Username: " + username);
		usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(usernameLabel);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel universityLabel = new JLabel("University: " + university);
		universityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(universityLabel);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel dobLabel = new JLabel("Date of Birth: " + (dob != null ? dob.toString() : "N/A"));
		dobLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(dobLabel);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel ageLabel = new JLabel("Age: " + age);
		ageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(ageLabel);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel dateJoinedLabel = new JLabel("Date Joined: " + (dateJoined != null ? dateJoined.toString() : "N/A"));
		dateJoinedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(dateJoinedLabel);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel likesLabel = new JLabel("Likes: " + likes);
		likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(likesLabel);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel sexLabel = new JLabel("Sex: " + sex);
		sexLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(sexLabel);

		// Create button panel with vertical BoxLayout
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

		// Add buttons with proper spacing
		JButton deleteProfileButton = new JButton("Delete My Profile");
		deleteProfileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		deleteProfileButton.setMaximumSize(new Dimension(150, 30));
		deleteProfileButton.addActionListener(e -> {
			// First confirm the user wants to delete
			int option = JOptionPane.showConfirmDialog(
					this,
					"Are you sure you want to delete your profile? This action cannot be undone.",
					"Confirm Deletion",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
			);

			if (option == JOptionPane.YES_OPTION) {
				// Then ask for password
				JPasswordField passwordField = new JPasswordField();
				int passwordOption = JOptionPane.showConfirmDialog(
						this,
						passwordField,
						"Enter your password to confirm account deletion:",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE
				);

				if (passwordOption == JOptionPane.OK_OPTION) {
					String password = new String(passwordField.getPassword());
					actionListener.onActionPerformed("deleteProfile", username, password);
				}
			}
		});
		buttonPanel.add(deleteProfileButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton viewLogsButton = new JButton("View Activity Logs");
		viewLogsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		viewLogsButton.setMaximumSize(new Dimension(150, 30));
		viewLogsButton.addActionListener(e -> actionListener.onActionPerformed("viewLogs"));
		buttonPanel.add(viewLogsButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton backButton = new JButton("Back");
		backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		backButton.setMaximumSize(new Dimension(150, 30));
		backButton.addActionListener(e -> actionListener.onActionPerformed("back"));
		buttonPanel.add(backButton);

		// Add both panels to this panel
		add(profileInfoPanel);
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(buttonPanel);
	}
}