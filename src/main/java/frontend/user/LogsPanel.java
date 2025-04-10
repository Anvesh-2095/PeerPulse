package frontend.user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LogsPanel extends JPanel
{
	private JTable logsTable;
	private DefaultTableModel tableModel;
	private ActionListener actionListener;

	public LogsPanel(ActionListener actionListener, List<String[]> logs)
	{
		this.actionListener = actionListener;

		// Set layout manager
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Create title
		JLabel titleLabel = new JLabel("Activity Logs");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(titleLabel, BorderLayout.NORTH);

		// Create table model with columns
		String[] columnNames = {"Date", "Action"};
		tableModel = new DefaultTableModel(columnNames, 0);

		// Populate table with logs
		if (logs != null && !logs.isEmpty())
		{
			for (String[] logEntry : logs)
			{
				tableModel.addRow(logEntry);
			}
		}
		else
		{
			// Add a placeholder row if no logs
			tableModel.addRow(new String[]{"", "No activity logs found"});
		}

		// Create table
		logsTable = new JTable(tableModel);
		logsTable.setRowHeight(25);
		logsTable.getTableHeader().setReorderingAllowed(false);

		// Add table to scroll pane
		JScrollPane scrollPane = new JScrollPane(logsTable);
		scrollPane.setPreferredSize(new Dimension(600, 400));
		add(scrollPane, BorderLayout.CENTER);

		// Add back button
		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> actionListener.onActionPerformed("back"));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(backButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
}