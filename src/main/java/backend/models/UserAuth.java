package backend.models;

public class UserAuth
{
	private String username;
	private String password;
	private String securityQuestion;
	private String securityAnswer;

	public UserAuth(String username, String password, String securityQuestion, String securityAnswer)
	{
		this.username = username;
		this.password = password;
		this.securityQuestion = securityQuestion;
		this.securityAnswer = securityAnswer;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getSecurityQuestion()
	{
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion)
	{
		this.securityQuestion = securityQuestion;
	}
	public String getSecurityAnswer()
	{
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer)
	{
		this.securityAnswer = securityAnswer;
	}

	@Override
	public String toString()
	{
		return "UserAuth{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", securityQuestion='" + securityQuestion + '\'' +
				", securityAnswer='" + securityAnswer + '\'' +
				'}';
	}
}