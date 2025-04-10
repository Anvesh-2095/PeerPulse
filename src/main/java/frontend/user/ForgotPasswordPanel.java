package frontend.user;

import javax.swing.*;

public class ForgotPasswordPanel extends JPanel
{
	ForgotPasswordPanel(ActionListener actionListener)
	{
		setSize(800, 600);
		setVisible(true);

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(300, 200, 100, 30);
		add(usernameLabel);

		JTextField usernameField = new JTextField();
		usernameField.setBounds(400, 200, 100, 30);
		usernameField.setSize(200, 30);
		add(usernameField);

		JLabel securityQuestionLabel = new JLabel("Security Question:");
		securityQuestionLabel.setBounds(250, 250, 150, 30);
		add(securityQuestionLabel);

		JLabel securityQuestionField = new JLabel(GeneralSavedData.securityQuestion);
		securityQuestionField.setBounds(400, 250, 100, 30);
		securityQuestionField.setSize(200, 30);
		add(securityQuestionField);

		JLabel securityAnswerLabel = new JLabel("Security Answer:");
		securityAnswerLabel.setBounds(260, 300, 150, 30);
		add(securityAnswerLabel);

		JTextField securityAnswerField = new JTextField();
		securityAnswerField.setBounds(400, 300, 100, 30);
		securityAnswerField.setSize(200, 30);
		add(securityAnswerField);

		JLabel newPasswordLabel = new JLabel("New Password:");
		newPasswordLabel.setBounds(250, 350, 150, 30);
		add(newPasswordLabel);

		JPasswordField newPasswordField = new JPasswordField();
		newPasswordField.setBounds(400, 350, 100, 30);
		newPasswordField.setSize(200, 30);
		add(newPasswordField);

		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
		confirmPasswordLabel.setBounds(250, 400, 150, 30);
		add(confirmPasswordLabel);

		JPasswordField confirmPasswordField = new JPasswordField();
		confirmPasswordField.setBounds(400, 400, 100, 30);
		confirmPasswordField.setSize(200, 30);
		add(confirmPasswordField);

		JButton submitButton = new JButton("Submit");
		submitButton.setBounds(300, 450, 100, 30);
		add(submitButton);
		submitButton.addActionListener((e) -> actionListener.onActionPerformed("Forgot Passowrd Submit", usernameField.getText().trim(), securityAnswerField.getText().trim(), new String(newPasswordField.getPassword()).trim(), new String(confirmPasswordField.getPassword()).trim()));
	}
}
