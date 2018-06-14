package acme.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.Customer;

public class ReportsPanel extends AcmeBaseJPanel {

	/* Declarations */
	Company company;
	// main label
	private JLabel reportsLbl = new JLabel("Reports");
	// preview table
	DefaultTableModel courierModel = new DefaultTableModel(null, new String[] { "Date", "Pick Up", "Pick Up Time",
			"Delivery", "Estimate Delivery Time", "Actual Delivery Time", "Bonus" });
	DefaultTableModel customerModel = new DefaultTableModel(null, new String[] { "Date", "Time of Order",
			"Price of Order", "Estimate Delivery Time", "Actual Delivery Time" });
	DefaultTableModel companyModel = new DefaultTableModel(null,
			new String[] { "Date of Order", "Estimate Delivery Time", "Actual Delivery Time" });
	private JTable previewTbl = new JTable(courierModel);
	private JLabel previewLbl = new JLabel("Preview");
	private JScrollPane previewScrll = new JScrollPane(previewTbl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	// buttons
	private JButton goBtn = new JButton("Go");
	private JButton printBtn = new JButton("Print");
	// report type selection box
	private JComboBox<String> typeSel = new JComboBox<String>();
	// courier or customer name selection
	private JComboBox<String> nameSel = new JComboBox<String>();
	private JLabel nameLbl = new JLabel("Courier");
	// date range selectors
	private DatePicker fromDate = new DatePicker();
	private JLabel fromLbl = new JLabel("From");
	private DatePicker toDate = new DatePicker();
	private JLabel toLbl = new JLabel("To");
	private DatePickerSettings toSettings = new DatePickerSettings();
	private DatePickerSettings fromSettings = new DatePickerSettings();
	// main panels for display
	JPanel northPane = new JPanel();
	JPanel southPane = new JPanel();
	// constants
	static final String COURIER = "Courier Performance";
	static final String CUSTOMER = "Customer Performance";
	static final String COMPANY = "Company Performance";
	static final String SELECTALL = "(Select All)";

	/* Constructor */
	public ReportsPanel(Company comp) {
		company = comp;
		initNorthPane();
		initSouthPane();
		initDefaults();
		initListeners();
	}

	/* Provide action listeners to the necessary components */
	private void initListeners() {

		// date picker Listeners
		DateChangeListener dateListen = new DateChangeListener() {
			LocalDate today = LocalDate.now();

			@Override
			public void dateChanged(DateChangeEvent e) {
				fromSettings.setDateRangeLimits(today.minusYears(5), toDate.getDate());
				toSettings.setDateRangeLimits(today.minusYears(5), today);
				if (fromDate.getDate().isAfter(toDate.getDate())) {
					fromDate.setDate(toDate.getDate().minusDays(7));
				}
			}
		};
		toDate.addDateChangeListener(dateListen);
		fromDate.addDateChangeListener(dateListen);

		// Go Button Listener
		goBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate report
				String type = typeSel.getSelectedItem().toString();

				System.out.println("generate report");
				if (type.equals(COMPANY)) {
					previewTbl.setModel(companyModel);
				} else if (type.equals(COURIER)) {
					previewTbl.setModel(courierModel);
				} else {
					previewTbl.setModel(customerModel);
				}

				generateReport(type, nameSel.getSelectedItem().toString(), fromDate.getDate(), toDate.getDate());
			}
		});

		// Print Button Listener
		printBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO print report
				System.out.println("print report");
			}

		});

		// ComboBox Listeners
		// report type
		typeSel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String type = typeSel.getSelectedItem().toString();
				// hide the combobox if it is for the company performance
				if (type.equals(COMPANY)) {
					nameSel.setVisible(false);
					nameLbl.setVisible(false);
				} else {
					nameSel.setVisible(true);
					nameLbl.setVisible(true);
					try {
						updateNameSel();
						nameSel.setSelectedIndex(0);
					} catch (IllegalArgumentException arg0) {
						nameSel.addItem("No Items Found");
					}
				}
				// trim it for the label
				nameLbl.setText(type.replaceAll(" Performance", ""));
			}
		});

		// name of courier or customer changed
		nameSel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO customer or courier name changed

				System.out.println("name changed");

			}

		});

	}

	/* Generate the report in th preview table */
	private void generateReport(String type, String name, LocalDate from, LocalDate to) {
		// TODO make preview table
		previewTbl.repaint();

	}

	public void updateNameSel() {
		ArrayList<String> items = new ArrayList<>();
		// clear out the comboBox
		nameSel.removeAllItems();

		switch (typeSel.getSelectedItem().toString()) {
		case COURIER:
			// get the list of couriers
			for (Map.Entry<UUID, Courier> courier : company.getCouriers().entrySet()) {
				items.add(courier.getValue().getName());
			}
			break;
		case CUSTOMER:
			// get the list of customers
			for (Map.Entry<UUID, Customer> customer : company.getCustomer().entrySet()) {
				items.add(customer.getValue().getName());
			}
			// add a select all for customers
			nameSel.addItem(SELECTALL);
			break;
		default:
			break;
		}

		// sort the items
		Collections.sort(items);
		// put items in the comboBox
		for (String values : items) {
			nameSel.addItem(values);
		}
	}

	private void initDefaults() {
		Font font = new Font(reportsLbl.getFont().toString(), Font.BOLD, 16);
		LocalDate today = LocalDate.now();
		reportsLbl.setFont(font);
		previewLbl.setFont(font);
		// date defaults
		toSettings.setVisibleTodayButton(true);
		toSettings.setFormatForDatesCommonEra("MM/dd/yy");
		toSettings.setFontInvalidDate(new Font(toDate.getFont().toString(), Font.PLAIN, 16));
		toSettings.setVisibleClearButton(false);
		toDate.setDateToToday();
		fromSettings.setVisibleTodayButton(true);
		fromSettings.setFormatForDatesCommonEra("MM/dd/yy");
		fromSettings.setVisibleClearButton(false);
		fromDate.setDate(LocalDate.now().minusDays(7));
		fromDate.setSettings(fromSettings);
		toDate.setSettings(toSettings);
		fromSettings.setDateRangeLimits(today.minusYears(5), today.minusDays(1));
		toSettings.setDateRangeLimits(today.minusYears(5), today);

		// set report type defaults
		typeSel.addItem(COURIER);
		typeSel.addItem(CUSTOMER);
		typeSel.addItem(COMPANY);

		typeSel.setSelectedItem(COURIER);
		updateNameSel();

	}

	private void initNorthPane() {

		northPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 465, 0 };
		gridBagLayout.rowHeights = new int[] { 120 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0 };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_northPane = new GridBagConstraints();
		gbc_northPane.insets = new Insets(0, 0, 10, 0);
		gbc_northPane.anchor = GridBagConstraints.NORTH;
		gbc_northPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_northPane.gridx = 0;
		gbc_northPane.gridy = 0;
		add(northPane, gbc_northPane);
		GridBagLayout gbl_northPane = new GridBagLayout();
		gbl_northPane.columnWidths = new int[] { 75, 200, 12, 50, 0 };
		gbl_northPane.rowHeights = new int[] { 0, 20, 23, 0, 0 };
		gbl_northPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_northPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		northPane.setLayout(gbl_northPane);

		GridBagConstraints gbc_reportsLbl = new GridBagConstraints();
		gbc_reportsLbl.anchor = GridBagConstraints.EAST;
		gbc_reportsLbl.fill = GridBagConstraints.VERTICAL;
		gbc_reportsLbl.insets = new Insets(0, 0, 5, 5);
		gbc_reportsLbl.gridx = 0;
		gbc_reportsLbl.gridy = 0;
		northPane.add(reportsLbl, gbc_reportsLbl);
		GridBagConstraints gbc_typeSel = new GridBagConstraints();
		gbc_typeSel.fill = GridBagConstraints.BOTH;
		gbc_typeSel.anchor = GridBagConstraints.WEST;
		gbc_typeSel.insets = new Insets(10, 5, 25, 5);
		gbc_typeSel.gridx = 1;
		gbc_typeSel.gridy = 0;
		northPane.add(typeSel, gbc_typeSel);
		GridBagConstraints gbc_fromLbl = new GridBagConstraints();
		gbc_fromLbl.anchor = GridBagConstraints.EAST;
		gbc_fromLbl.fill = GridBagConstraints.VERTICAL;
		gbc_fromLbl.insets = new Insets(0, 0, 5, 5);
		gbc_fromLbl.gridx = 0;
		gbc_fromLbl.gridy = 1;
		northPane.add(fromLbl, gbc_fromLbl);
		GridBagConstraints gbc_fromDate = new GridBagConstraints();
		gbc_fromDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_fromDate.anchor = GridBagConstraints.NORTH;
		gbc_fromDate.insets = new Insets(0, 0, 5, 5);
		gbc_fromDate.gridx = 1;
		gbc_fromDate.gridy = 1;
		northPane.add(fromDate, gbc_fromDate);
		GridBagConstraints gbc_toLbl = new GridBagConstraints();
		gbc_toLbl.anchor = GridBagConstraints.EAST;
		gbc_toLbl.fill = GridBagConstraints.VERTICAL;
		gbc_toLbl.insets = new Insets(0, 0, 5, 5);
		gbc_toLbl.gridx = 0;
		gbc_toLbl.gridy = 2;
		northPane.add(toLbl, gbc_toLbl);
		GridBagConstraints gbc_toDate = new GridBagConstraints();
		gbc_toDate.fill = GridBagConstraints.BOTH;
		gbc_toDate.insets = new Insets(0, 0, 5, 5);
		gbc_toDate.gridx = 1;
		gbc_toDate.gridy = 2;
		northPane.add(toDate, gbc_toDate);
		GridBagConstraints gbc_goBtn = new GridBagConstraints();
		gbc_goBtn.insets = new Insets(0, 0, 5, 10);
		gbc_goBtn.fill = GridBagConstraints.VERTICAL;
		gbc_goBtn.anchor = GridBagConstraints.WEST;
		gbc_goBtn.gridx = 2;
		gbc_goBtn.gridy = 2;
		northPane.add(goBtn, gbc_goBtn);
	}

	private void initSouthPane() {
		GridBagConstraints gbc_southPane = new GridBagConstraints();
		gbc_southPane.fill = GridBagConstraints.BOTH;
		gbc_southPane.insets = new Insets(0, 0, 10, 0);
		gbc_southPane.gridx = 0;
		gbc_southPane.gridy = 1;
		add(southPane, gbc_southPane);
		GridBagLayout gbl_southPane = new GridBagLayout();
		gbl_southPane.columnWidths = new int[] { 75, 200, 150, 0, 0 };
		gbl_southPane.rowHeights = new int[] { 33, 0, 0, 0, 0, 0 };
		gbl_southPane.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_southPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		southPane.setLayout(gbl_southPane);

		GridBagConstraints gbc_nameLbl = new GridBagConstraints();
		gbc_nameLbl.anchor = GridBagConstraints.EAST;
		gbc_nameLbl.insets = new Insets(0, 0, 5, 5);
		gbc_nameLbl.gridx = 0;
		gbc_nameLbl.gridy = 0;
		southPane.add(nameLbl, gbc_nameLbl);
		GridBagConstraints gbc_nameSel = new GridBagConstraints();
		gbc_nameSel.fill = GridBagConstraints.BOTH;
		gbc_nameSel.insets = new Insets(0, 0, 5, 5);
		gbc_nameSel.gridx = 1;
		gbc_nameSel.gridy = 0;
		southPane.add(nameSel, gbc_nameSel);
		GridBagConstraints gbc_previewLbl = new GridBagConstraints();
		gbc_previewLbl.fill = GridBagConstraints.VERTICAL;
		gbc_previewLbl.anchor = GridBagConstraints.EAST;
		gbc_previewLbl.insets = new Insets(0, 0, 5, 5);
		gbc_previewLbl.gridx = 0;
		gbc_previewLbl.gridy = 1;
		southPane.add(previewLbl, gbc_previewLbl);
		previewTbl.setFillsViewportHeight(true);
		previewTbl.setEnabled(true);
		previewTbl.setVisible(true);
		previewScrll.setViewportView(previewTbl);
		GridBagConstraints gbc_previewScrll = new GridBagConstraints();
		gbc_previewScrll.fill = GridBagConstraints.BOTH;
		gbc_previewScrll.gridwidth = 2;
		gbc_previewScrll.insets = new Insets(0, 0, 10, 5);
		gbc_previewScrll.gridheight = 5;
		gbc_previewScrll.gridx = 1;
		gbc_previewScrll.gridy = 1;
		southPane.add(previewScrll, gbc_previewScrll);
		GridBagConstraints gbc_printBtn = new GridBagConstraints();
		gbc_printBtn.anchor = GridBagConstraints.WEST;
		gbc_printBtn.insets = new Insets(0, 0, 10, 10);
		gbc_printBtn.fill = GridBagConstraints.VERTICAL;
		gbc_printBtn.gridx = 3;
		gbc_printBtn.gridy = 1;
		southPane.add(printBtn, gbc_printBtn);
	}

}
