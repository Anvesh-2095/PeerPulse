package frontend.user;

public interface ActionListener
{
	void onActionPerformed(String action);

	void onActionPerformed(String action, String username, String password);

	void onActionPerformed(String action, String username);

	void onActionPerformed(String action, String username, String password, String confirmPassword, String securityQuestion, String securityAnswer, String name, String dob, String university, String sex);

	void onActionPerformed(String action, String username, String securityAnswer, String password, String confirmPassword);
}
