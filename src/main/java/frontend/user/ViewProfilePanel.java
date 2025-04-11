package frontend.user;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ViewProfilePanel extends JPanel
{
	ViewProfilePanel(ActionListener actionListener, String username, String name, String university, Date dob, Date dateJoined, int likes, char sex)
	{
		int age = 0;
		// Calculate age from date of birth
		if (dob != null)
		{
			Date currentDate = new Date();
			age = currentDate.getYear() - dob.getYear();
			if (currentDate.getMonth() < dob.getMonth() || (currentDate.getMonth() == dob.getMonth() && currentDate.getDate() < dob.getDate()))
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
		profileInfoPanel.setBorder(BorderFactory.createTitledBorder("Profile Information"));

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
		JButton upvoteButton = new JButton("Upvote");
		upvoteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		upvoteButton.setMaximumSize(new Dimension(150, 30));
		upvoteButton.addActionListener(e -> actionListener.onActionPerformed("upvote", username));
		buttonPanel.add(upvoteButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton downvoteButton = new JButton("Downvote");
		downvoteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		downvoteButton.setMaximumSize(new Dimension(150, 30));
		downvoteButton.addActionListener(e -> actionListener.onActionPerformed("downvote", username));
		buttonPanel.add(downvoteButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton removeVoteButton = new JButton("Remove Vote");
		removeVoteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		removeVoteButton.setMaximumSize(new Dimension(150, 30));
		removeVoteButton.addActionListener(e -> actionListener.onActionPerformed("removeVote", username));
		buttonPanel.add(removeVoteButton);
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
