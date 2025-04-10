package backend.DAO;

import backend.models.UserAuth;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAuthDAO
{
	private static final String URL = "jdbc:mysql://localhost:3306/peerpulse";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "1234";

	private Connection getConnection()
	{
		Connection connection = null;
		try
		{
			// TODO: check classpath for the MySQL JDBC driver
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return connection;
	}

	public void insert(UserAuth userAuth) throws SQLException
	{
		String sql = "INSERT INTO user_auth (username, password, security_question, security_answer) VALUES (?, ?, ?, ?)";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, userAuth.getUsername());
			stmt.setString(2, userAuth.getPassword());
			stmt.setString(3, userAuth.getSecurityQuestion());
			stmt.setString(4, userAuth.getSecurityAnswer());

			stmt.executeUpdate();
			System.out.println("User authentication inserted successfully");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void delete(String username) throws SQLException // not needed as already on delete cascade
	{
		String sql = "DELETE FROM user_auth WHERE username = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			stmt.executeUpdate();
			System.out.println("User authentication deleted successfully");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public UserAuth findByUsername(String username) throws SQLException
	{
		String sql = "SELECT * FROM user_auth WHERE username = ?";
		UserAuth userAuth = null;

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				userAuth = new UserAuth(rs.getString("username"), rs.getString("password"), rs.getString("security_question"), rs.getString("security_answer"));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return userAuth;
	}

	public void updatePassword(String username, String newPassword) throws SQLException
	{
		String sql = "UPDATE user_auth SET password = ? WHERE username = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, newPassword);
			stmt.setString(2, username);
			stmt.executeUpdate();
			System.out.println("User authentication updated successfully");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateSecurityQuestion(String username, String newSecurityQuestion, String newSecurityAnswer) throws SQLException
	{
		String sql = "UPDATE user_auth SET security_question = ?, security_answer = ? WHERE username = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, newSecurityQuestion);
			stmt.setString(2, newSecurityAnswer);
			stmt.setString(3, username);
			stmt.executeUpdate();
			System.out.println("User authentication updated successfully");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean validateCredentials(String username, String password) throws SQLException
	{
		String sql = "SELECT * FROM user_auth WHERE username = ? AND password = ?";
		boolean isValid = false;

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				isValid = true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return isValid;
	}

	public boolean validateSecurityQuestion(String username, String securityQuestion, String securityAnswer) throws SQLException
	{
		String sql = "SELECT * FROM user_auth WHERE username = ? AND security_question = ? AND security_answer = ?";
		boolean isValid = false;

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			stmt.setString(2, securityQuestion);
			stmt.setString(3, securityAnswer);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				isValid = true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return isValid;
	}

	public List<UserAuth> listAllUserAuths() throws SQLException
	{
		List<UserAuth> userAuths = new ArrayList<>();
		String sql = "SELECT * FROM user_auth";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
		{
			while (rs.next())
			{
				userAuths.add(new UserAuth(rs.getString("username"), rs.getString("password"), rs.getString("security_question"), rs.getString("security_answer")));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return userAuths;
	}
}
