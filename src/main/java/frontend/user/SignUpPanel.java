package frontend.user;

import javax.swing.*;

public class SignUpPanel extends JPanel
{
	SignUpPanel(ActionListener actionListener)
	{
		setSize(800, 600);
		setVisible(true);

		// Username field
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(300, 100, 100, 30);
		add(usernameLabel);

		JTextField usernameField = new JTextField();
		usernameField.setBounds(400, 100, 200, 30);
		add(usernameField);

		// Name field
		JLabel nameLabel = new JLabel("Full Name:");
		nameLabel.setBounds(300, 140, 100, 30);
		add(nameLabel);

		JTextField nameField = new JTextField();
		nameField.setBounds(400, 140, 200, 30);
		add(nameField);

		// Date of birth field
		JLabel dobLabel = new JLabel("Date of Birth:");
		dobLabel.setBounds(300, 180, 100, 30);
		add(dobLabel);

		JTextField dobField = new JTextField("YYYY-MM-DD");
		dobField.setBounds(400, 180, 200, 30);
		add(dobField);

		// University field
		JLabel universityLabel = new JLabel("University:");
		universityLabel.setBounds(300, 220, 100, 30);
		add(universityLabel);

		JTextField universityField = new JTextField();
		universityField.setBounds(400, 220, 200, 30);
		add(universityField);

		// Sex field
		JLabel sexLabel = new JLabel("Sex:");
		sexLabel.setBounds(300, 260, 100, 30);
		add(sexLabel);

		String[] sexOptions = {"Male", "Female", "Other"};
		JComboBox<String> sexComboBox = new JComboBox<>(sexOptions);
		sexComboBox.setBounds(400, 260, 200, 30);
		add(sexComboBox);

		// Password field
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(300, 300, 100, 30);
		add(passwordLabel);

		JPasswordField passwordField = new JPasswordField();
		passwordField.setBounds(400, 300, 200, 30);
		add(passwordField);

		// Confirm password field
		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
		confirmPasswordLabel.setBounds(250, 340, 150, 30);
		add(confirmPasswordLabel);

		JPasswordField confirmPasswordField = new JPasswordField();
		confirmPasswordField.setBounds(400, 340, 200, 30);
		add(confirmPasswordField);

		// Security question field
		JLabel securityQuestionLabel = new JLabel("Security Question:");
		securityQuestionLabel.setBounds(250, 380, 150, 30);
		add(securityQuestionLabel);

		JTextField securityQuestionField = new JTextField(GeneralSavedData.securityQuestion);
		securityQuestionField.setBounds(400, 380, 200, 30);
		add(securityQuestionField);

		// Security answer field
		JLabel securityAnswerLabel = new JLabel("Security Answer:");
		securityAnswerLabel.setBounds(260, 420, 150, 30);
		add(securityAnswerLabel);

		JTextField securityAnswerField = new JTextField();
		securityAnswerField.setBounds(400, 420, 200, 30);
		add(securityAnswerField);

		// Buttons
		JButton signUpButton = new JButton("Sign Up");
		signUpButton.setBounds(300, 470, 100, 30);
		add(signUpButton);
		signUpButton.addActionListener((e) -> actionListener.onActionPerformed(
			"Sign Up", 
			usernameField.getText().trim(),
			new String(passwordField.getPassword()).trim(),
			new String(confirmPasswordField.getPassword()).trim(),
			securityQuestionField.getText().trim(),
			securityAnswerField.getText().trim(),
			nameField.getText().trim(),
			dobField.getText().trim(),
			universityField.getText().trim(),
			(String)sexComboBox.getSelectedItem()
		));

		JButton logInButton = new JButton("Log In");
		logInButton.setBounds(450, 470, 100, 30);
		add(logInButton);
		logInButton.addActionListener((e) -> actionListener.onActionPerformed("Navigate Log In"));
	}
}
