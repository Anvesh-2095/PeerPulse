package frontend.user;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel
{
	public HomePanel(ActionListener actionListener, String fetchedUsername, String fetchedName, int upvoteCount)
	{
		// Set layout manager for the main panel
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		// Create profile info panel with vertical BoxLayout
		JPanel profileInfoPanel = new JPanel();
		profileInfoPanel.setLayout(new BoxLayout(profileInfoPanel, BoxLayout.Y_AXIS));
		
		// Add profile information with proper spacing
		JLabel name = new JLabel("Name: " + fetchedName);
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(name);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JLabel username = new JLabel("Username: " + fetchedUsername);
		username.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(username);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JLabel upvotes = new JLabel("Upvotes: " + upvoteCount);
		upvotes.setAlignmentX(Component.LEFT_ALIGNMENT);
		profileInfoPanel.add(upvotes);
		profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		// Create button panel with vertical BoxLayout
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		// Add buttons with proper spacing
		JButton viewProfileButton = new JButton("View Profile");
		viewProfileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		viewProfileButton.setMaximumSize(new Dimension(150, 30));
		viewProfileButton.addActionListener(e -> actionListener.onActionPerformed("viewProfile", fetchedUsername));
		buttonPanel.add(viewProfileButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		// Add the new "View My Profile" button
//		JButton viewMyProfileButton = new JButton("View My Profile");
//		viewMyProfileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
//		viewMyProfileButton.setMaximumSize(new Dimension(150, 30));
//		viewMyProfileButton.addActionListener(e -> actionListener.onActionPerformed("viewMyProfile", fetchedUsername));
//		buttonPanel.add(viewMyProfileButton);
//		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton viewMyProfileButton = new JButton("View My Profile");
		viewMyProfileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		viewMyProfileButton.setMaximumSize(new Dimension(150, 30));
		viewMyProfileButton.addActionListener(e -> actionListener.onActionPerformed("viewMyProfile"));
		buttonPanel.add(viewMyProfileButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton upvoteButton = new JButton("Upvote");
		upvoteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		upvoteButton.setMaximumSize(new Dimension(150, 30));
		upvoteButton.addActionListener(e -> actionListener.onActionPerformed("upvote", fetchedUsername));
		buttonPanel.add(upvoteButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JButton downvoteButton = new JButton("Downvote");
		downvoteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		downvoteButton.setMaximumSize(new Dimension(150, 30));
		downvoteButton.addActionListener(e -> actionListener.onActionPerformed("downvote", fetchedUsername));
		buttonPanel.add(downvoteButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JButton nextButton = new JButton("Next");
		nextButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		nextButton.setMaximumSize(new Dimension(150, 30));
		nextButton.addActionListener(e -> actionListener.onActionPerformed("next"));
		buttonPanel.add(nextButton);

		// Add "Delete My Profile" button at the bottom
		JButton deleteProfileButton = new JButton("Delete My Profile");
		deleteProfileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		deleteProfileButton.setMaximumSize(new Dimension(150, 30));
		deleteProfileButton.setForeground(Color.RED);
		deleteProfileButton.addActionListener(e -> actionListener.onActionPerformed("deleteProfile"));
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add extra space
		buttonPanel.add(deleteProfileButton);


		// Create a container panel to hold both profileInfoPanel and buttonPanel
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
		containerPanel.add(profileInfoPanel);
		containerPanel.add(buttonPanel);
		
		// Add the container panel to the center of the main panel
		add(containerPanel, BorderLayout.CENTER);

		// Uncomment and fix the image code when you have images ready
		/*
		try
		{
			InputStream imageStream = getClass().getResourceAsStream("/Images/DemoFemale.png");
			assert imageStream != null;
			BufferedImage image = ImageIO.read(imageStream);
			JLabel profilePicture = new JLabel(new ImageIcon(image));
			profilePicture.setPreferredSize(new Dimension(100, 100));
			add(profilePicture, BorderLayout.WEST);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		*/
	}
}
