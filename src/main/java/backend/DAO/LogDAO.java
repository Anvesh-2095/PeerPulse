package backend.DAO;

import backend.models.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LogDAO
{
	private static final String URL = "jdbc:mysql://localhost:3306/peerpulse";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "1234";

	public Connection getConnection()
	{
		Connection connection = null;
		try
		{
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return connection;
	}

	public void insert(Log log)
	{
		String sql = "INSERT INTO log (username, action, date) VALUES (?, ?, ?)";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, log.getUsername());
			stmt.setString(2, log.getAction());
			stmt.setDate(3, log.getDate());

			stmt.executeUpdate();
			System.out.println("Log inserted successfully");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void delete(String username)
	{
		String sql = "DELETE FROM log WHERE username = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			stmt.executeUpdate();
			System.out.println("Log deleted successfully");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public List<Log> findByUsername(String username)
	{
		String sql = "SELECT * FROM log WHERE username = ? ORDER BY date DESC LIMIT 10";
		List<Log> logs = new ArrayList<>();

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			while (rs.next())
			{
				Log log = new Log(rs.getString("username"), rs.getString("action"), rs.getDate("date"));
				logs.add(log);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return logs;
	}
}
