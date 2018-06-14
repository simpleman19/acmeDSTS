package acme.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ClerksListPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	// Declarations
	// Company company;
	JLabel mainLbl = new JLabel("Clerks");
	JTable listTbl = new JTable();
	JScrollPane listScrll = new JScrollPane(listTbl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	JButton addBtn = new JButton("Add");
	JButton saveBtn = new JButton("Save");
	JButton cnclBtn = new JButton("Cancel");

	/* Constructor */
	public ClerksListPanel() {
		listTbl.setFillsViewportHeight(true);

		// TODO remove from constructor
		buildPanel();
	}

	public void buildPanel() {

		// company = this.getCompany();
		initLayout();
		initDefaults();

	}

	private void initLayout() {

		mainLbl.setFont(new Font(mainLbl.getFont().toString(), Font.BOLD, 16));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 50, 50, 50, 50 };
		gridBagLayout.rowHeights = new int[] { 15, 100, 25 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0 };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_mainLbl = new GridBagConstraints();
		gbc_mainLbl.fill = GridBagConstraints.BOTH;
		gbc_mainLbl.insets = new Insets(15, 65, 5, 0);
		gbc_mainLbl.gridx = 0;
		gbc_mainLbl.gridy = 0;
		add(mainLbl, gbc_mainLbl);
		GridBagConstraints gbc_listScrll = new GridBagConstraints();
		gbc_listScrll.fill = GridBagConstraints.BOTH;
		gbc_listScrll.insets = new Insets(0, 65, 5, 0);
		gbc_listScrll.gridwidth = 3;
		gbc_listScrll.gridx = 0;
		gbc_listScrll.gridy = 1;
		add(listScrll, gbc_listScrll);
		GridBagConstraints gbc_cnclBtn = new GridBagConstraints();
		gbc_cnclBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_cnclBtn.anchor = GridBagConstraints.NORTH;
		gbc_cnclBtn.insets = new Insets(0, 0, 0, 5);
		gbc_cnclBtn.gridx = 1;
		gbc_cnclBtn.gridy = 2;
		add(cnclBtn, gbc_cnclBtn);
		GridBagConstraints gbc_addBtn = new GridBagConstraints();
		gbc_addBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_addBtn.anchor = GridBagConstraints.NORTH;
		gbc_addBtn.insets = new Insets(0, 0, 0, 5);
		gbc_addBtn.gridx = 2;
		gbc_addBtn.gridy = 2;
		add(addBtn, gbc_addBtn);
		GridBagConstraints gbc_saveBtn = new GridBagConstraints();
		gbc_saveBtn.insets = new Insets(0, 0, 15, 15);
		gbc_saveBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_saveBtn.anchor = GridBagConstraints.NORTH;
		gbc_saveBtn.gridx = 3;
		gbc_saveBtn.gridy = 2;
		add(saveBtn, gbc_saveBtn);
	}

	private void initDefaults() {

		String[] columnNames = { "First Name", "Last Name", "" };
		Object[][] data = { { "Homer", "Simpson", "delete Homer" }, { "Madge", "Simpson", "delete Madge" },
				{ "Bart", "Simpson", "delete Bart" }, { "Lisa", "Simpson", "delete Lisa" }, };

		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		listTbl.setModel(model);

		Action delete = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int modelRow = Integer.valueOf(e.getActionCommand());
				((DefaultTableModel) table.getModel()).removeRow(modelRow);
			}
		};

		ButtonColumn button = new ButtonColumn(listTbl, delete, 2);
		ButtonColumn buttonColumn = new ButtonColumn(listTbl, delete, 2);
		button.setMnemonic(KeyEvent.VK_D);
	}
}
