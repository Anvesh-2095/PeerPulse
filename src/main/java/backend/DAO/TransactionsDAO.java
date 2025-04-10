package backend.DAO;

import backend.models.Transactions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionsDAO
{
	private static final String URL = "jdbc:mysql://localhost:3306/peerpulse";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "1234";

	public Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	public void insert(Transactions transaction)
	{
		String sql = "INSERT INTO transactions (sender, receiver, amount, date) VALUES (?, ?, ?, ?)";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, transaction.getSender());
			stmt.setString(2, transaction.getReceiver());
			stmt.setInt(3, transaction.getAmount());
			stmt.setDate(4, new java.sql.Date(transaction.getDate().getTime()));

			stmt.executeUpdate();
			System.out.println("Transaction inserted successfully");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void delete(String sender, String receiver)
	{
		String sql = "DELETE FROM transactions WHERE sender = ? AND receiver = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, sender);
			stmt.setString(2, receiver);
			stmt.executeUpdate();
			System.out.println("Transaction deleted successfully");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void update(Transactions transaction)
	{
		String sql = "UPDATE transactions SET amount = ? WHERE sender = ? AND receiver = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, transaction.getAmount());
			stmt.setString(2, transaction.getSender());
			stmt.setString(3, transaction.getReceiver());
			stmt.executeUpdate();
			System.out.println("Transaction updated successfully");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public Transactions findTransaction(String sender, String receiver)
	{
		String sql = "SELECT * FROM transactions WHERE sender = ? AND receiver = ?";
		Transactions transaction = null;

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, sender);
			stmt.setString(2, receiver);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				transaction = new Transactions(
						rs.getString("sender"),
						rs.getString("receiver"),
						rs.getInt("amount"),
						rs.getDate("date")
				);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return transaction;
	}

	public boolean hasTransaction(String sender, String receiver)
	{
		return findTransaction(sender, receiver) != null;
	}

	public List<Transactions> findTransactionsBySender(String sender)
	{
		String sql = "SELECT * FROM transactions WHERE sender = ?";
		List<Transactions> transactions = new ArrayList<>();

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, sender);
			ResultSet rs = stmt.executeQuery();

			while (rs.next())
			{
				transactions.add(new Transactions(
						rs.getString("sender"),
						rs.getString("receiver"),
						rs.getInt("amount"),
						rs.getDate("date")
				));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return transactions;
	}

	public void deleteAllTransactionsBySender(String sender)
	{
		String sql = "DELETE FROM transactions WHERE sender = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, sender);
			stmt.executeUpdate();
			System.out.println("All transactions by sender " + sender + " deleted successfully");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void deleteAllTransactionsByUser(String username)
	{
		String sql = "DELETE FROM transactions WHERE sender = ? OR receiver = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			stmt.setString(2, username);
			stmt.executeUpdate();
			System.out.println("All transactions involving user " + username + " deleted successfully");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
