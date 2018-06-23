package acme.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import acme.data.HibernateAdapter;
import acme.data.PersistableEntity;
import acme.pd.Ticket;
import acme.pd.User;
import acme.seed.SeedDatabase;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import acme.data.HibernateAdapter;
import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.Customer;
import acme.pd.Ticket;
import acme.pd.User;

public class ReportsPanel extends AcmeBaseJPanel {

    private static final long serialVersionUID = 1L;
    /* Declarations */
    Company company;
    // main label
    private JLabel reportsLbl = new JLabel("Reports");
    // preview table
    // TODO finish table models for different report types
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

    DateTimeFormatter databaseFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter reportDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter reportTime = DateTimeFormatter.ofPattern("hh:mm:ss a");

    /* Constructor */
    public ReportsPanel() {

    }

    @Override
    public void buildPanel() {
        company = this.getCompany();
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
                fromSettings.setDateRangeLimits(null, toDate.getDate());
                toSettings.setDateRangeLimits(null, today);
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
                String type = typeSel.getSelectedItem().toString();

                if (type.equals(COMPANY)) {
                    previewTbl.setModel(companyModel);
                } else if (type.equals(COURIER)) {
                    previewTbl.setModel(courierModel);
                } else {
                    previewTbl.setModel(customerModel);
                }
                ((DefaultTableModel) previewTbl.getModel()).setRowCount(0);
                generateReport(type, nameSel.getSelectedItem().toString(), fromDate.getDate(), toDate.getDate());
            }
        });

        // Print Button Listener
        printBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              String type = typeSel.getSelectedItem().toString();
                System.out.println("print report");
                if (type.equalsIgnoreCase(COMPANY)) {
                  printReport();
                }
                else if(type.equalsIgnoreCase(CUSTOMER)) {
                  printBillingReport();
                }

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
                    goBtn.setEnabled(true);
                } else {
                    nameSel.setVisible(true);
                    nameLbl.setVisible(true);
                    nameSel.setEnabled(true);
                    updateNameSel();
                }
                // trim it for the label
                nameLbl.setText(type.replaceAll(" Performance", ""));
            }
        });

    }

    /* Generate the report in the preview table */
    private void generateReport(String type, String name, LocalDate from, LocalDate to) {
        if (type.equals(COURIER)) {
            generateCourPerfReport(name, from, to);
        } else if (type.equals(COMPANY)) {
            generateCompPerformanceReport(from, to);
        }
        else if(type.equalsIgnoreCase(CUSTOMER)) {
          generateCustomerPerformanceReports(from, to, name);
        }
        previewTbl.repaint();
    }

    private void generateCourPerfReport(String name, LocalDate from, LocalDate to) {
        double bonus = 0;
        EntityManager em = HibernateAdapter.getEntityManager();
        Courier c = new Courier();
        List<Ticket> ticket = em.createQuery(
                "select t from TICKET as t where t.courier is not null AND t.deliveryTime is not null order by t.deliveryTime",
                Ticket.class).getResultList();
        int onTime = ticket.size();

        for (Map.Entry<UUID, Courier> courier : company.getCouriers().entrySet()) {
            if (name.equals(courier.getValue().getName())) {
                c = courier.getValue();
            }
        }

        //iterate through the tickets that the query returned
        for (Ticket t : ticket) {
            int margin = company.getLatenessMarginMinutes();
            LocalDateTime early = t.getDeliveryTime().minusMinutes(margin);
            LocalDateTime late = t.getDeliveryTime().plusMinutes(margin);
            // are we in the date range for this courier

            if (t.getCreationDateTime().isAfter(from.atStartOfDay())
                    && t.getDeliveryTime().isBefore(to.plusDays(1).atStartOfDay())
                    && t.getCourier().getId().equals(c.getId())) {
                // was it early or late
                boolean lateDelivery = t.getDeliveryTime().isBefore(late);
                bonus += t.getBonus().doubleValue();
                courierModel.addRow(new Object[] { reportDate.format(t.getDeliveryTime().toLocalDate()),
                        t.getPickupCustomer().getName(), reportTime.format(t.getPickupTime()),
                        t.getDeliveryCustomer().getName(), reportTime.format(t.getEstimatedDeliveryTime()),
                        reportTime.format(t.getDeliveryTime()), t.getBonus() });

                if (lateDelivery) onTime--;
            }
        }

        //append the summary row
        courierModel.addRow(new Object[] { "<html><b>" + "On-Time" + "<br>" + "Percentage: " + "</b>"
                + String.valueOf(NumberFormat.getPercentInstance().format((double) onTime / ticket.size())) + "</html>",
                "<html><b>" + "Bonus: $" + "</b>" + bonus + "</html>"

        });

        //reformat the cell sizes to fit the data
        updateCellSizes();
    }

    private void updateCellSizes() {

        previewTbl.getColumnModel().setColumnSelectionAllowed(false);
        for (int row = 0; row < previewTbl.getRowCount(); row++) {
            int rowHeight = previewTbl.getRowHeight();
            int columnWidth = previewTbl.getIntercellSpacing().width;

            for (int column = 0; column < previewTbl.getColumnCount(); column++) {
                Component comp = previewTbl.prepareRenderer(previewTbl.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height) + 1;
                previewTbl.getColumnModel().getColumn(column)
                        .setPreferredWidth(columnWidth = Math.max(columnWidth, comp.getPreferredSize().width) + 1);

            }
            previewTbl.setRowHeight(row, rowHeight);

        }
    }

    private void generateCompPerformanceReport(LocalDate from, LocalDate to) {
        DefaultTableModel model = (DefaultTableModel) previewTbl.getModel();
        HashMap<String, LocalDateTime> params = new HashMap<>();
        params.put("stDate", from.atStartOfDay());
        params.put("endDate", to.atStartOfDay().plusDays(1));
        EntityManager em = HibernateAdapter.getEntityManager();
        Query query = em.createQuery(
                "SELECT t from TICKET t WHERE t.creationDateTime BETWEEN :stDate AND :endDate AND t.deliveryTime IS NOT NULL ORDER BY t.pickupCustomer.name",
                Ticket.class);
        query.setParameter("stDate", from.atStartOfDay());
        query.setParameter("endDate", to.atStartOfDay().plusDays(1));
        List tickets = query.getResultList();
        Iterator i = tickets.iterator();
        int count = 0, onTime = 0, custCount = 0, custOntime = 0;
        Customer currentCust = null;
        Ticket t = null;
        while (i.hasNext()) {
            t = (Ticket) i.next();
            if (currentCust != null && t.getPickupCustomer() != currentCust && custCount != 0) {
                model.addRow(new Object[] { "<html><b>Customer</b></html>", "<html><b>Performance</b></html>", "" });
                model.addRow(new Object[] { "<html><b>" + currentCust.getName() + "</b></html>", "<html><b>"
                        + NumberFormat.getPercentInstance().format((double) custOntime / custCount) + "</b></html>",
                        "" });
                custCount = 0;
                custOntime = 0;
                currentCust = t.getPickupCustomer();
            } else if (currentCust == null) {
                currentCust = t.getPickupCustomer();
            }
            if (t.getEstimatedDeliveryTime().isAfter(t.getDeliveryTime())
                    || t.getEstimatedDeliveryTime().equals(t.getDeliveryTime())) {
                onTime++;
                custOntime++;
            }
            model.addRow(new Object[] { reportDate.format(t.getCreationDateTime()),
                    reportTime.format(t.getEstimatedDeliveryTime()), reportTime.format(t.getDeliveryTime()) });
            custCount++;
            count++;
        }
        if (custCount != 0) {
            model.addRow(new Object[] { "<html><b>Customer</b></html>", "<html><b>Performance</b></html>", "" });
            model.addRow(new Object[] {
                    "<html><b>" + currentCust.getName() + "</b></html>", "<html><b>"
                            + NumberFormat.getPercentInstance().format((double) custOntime / custCount) + "</b></html>",
                    "" });
        }

        updateCellSizes();
        fixCompPerfCols();
    }

    private void generateCustomerPerformanceReports(LocalDate from, LocalDate to, String customerName) {

      DefaultTableModel model = (DefaultTableModel) previewTbl.getModel();
      model.setRowCount(0);         //clears out table if run customer report multiple times in a row
      DateTimeFormatter databaseFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      DateTimeFormatter reportDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      DateTimeFormatter reportTime = DateTimeFormatter.ofPattern("hh:mm:ss a");
      HashMap<String, LocalDateTime> params = new HashMap<>();
      params.put("stDate", from.atStartOfDay());
      params.put("endDate", to.atStartOfDay().plusDays(1));
      EntityManager em = HibernateAdapter.getEntityManager();
      Query query = em.createQuery("SELECT t from TICKET t WHERE t.creationDateTime BETWEEN :stDate AND :endDate AND t.deliveryTime IS NOT NULL ORDER BY t.deliveryTime", Ticket.class);
      query.setParameter("stDate", from.atStartOfDay());
      query.setParameter("endDate", to.atStartOfDay().plusDays(1));
      List tickets = query.getResultList();
      Iterator i = tickets.iterator();
      int count = 0, onTime = 0;
      double totalPrice = 0;
      Ticket t = null;
      Stack<String> nameOfCustomers = new Stack();
      System.out.println(customerName);
      if(customerName.equals("(Select All)"))
      {
        nameOfCustomers.clear();
        for(Map.Entry<UUID, Customer> customer : this.getAcmeUI().getCompany().getCustomers().entrySet())
        {
          nameOfCustomers.push(customer.getValue().getName());
        }
      }
      else
      {
        nameOfCustomers.clear();
        nameOfCustomers.push(customerName);
      }
      while(!nameOfCustomers.isEmpty())
      {
        customerName = nameOfCustomers.pop();
        i = tickets.iterator();
        count = 0;
        onTime = 0;
        totalPrice = 0;
        while (i.hasNext()) {
            t = (Ticket) i.next();


            if(t.isBillToSender() && t.getPickupCustomer().getName().equals(customerName) ||
                !t.isBillToSender() && t.getDeliveryCustomer().getName().equals(customerName))
            {
              model.addRow(new Object[] {
                    reportDate.format(t.getCreationDateTime()),
                    reportTime.format(t.getCreationDateTime()),
                    t.getQuotedPrice(),
                    reportTime.format(t.getEstimatedDeliveryTime()),
                    reportTime.format(t.getDeliveryTime())
              });


              totalPrice = totalPrice + t.getQuotedPrice().doubleValue();

              if(t.getEstimatedDeliveryTime().isAfter(t.getDeliveryTime()) || t.getEstimatedDeliveryTime().equals(t.getDeliveryTime())) {
                  onTime++;
              }
              count++;
            }

        }
        if (count != 0) {
            model.addRow(new Object[] {
                    "<html><b>Customer Name</b></html>",
                    "<html><b>Total Price</b></html>",
                    "<html><b>On Time Percentage</b></html>",
                    ""
            });
            model.addRow(new Object[] {
                    "<html><b>" + customerName  + "</b></html>",
                    "<html><b>" + new BigDecimal(totalPrice)  + "</b></html>",
                    "<html><b>" +  NumberFormat.getPercentInstance().format((double) onTime / count) + "</b></html>",
                    ""
            });
        }
      }

      fixCustomerPerfCols();
  }



    private void fixCompPerfCols() {
        for (int i = 0; i < 3; i++) {
            previewTbl.getColumnModel().getColumn(i).setMinWidth(120);
        }
    }

    private void fixCustomerPerfCols() {
      for (int i = 0; i<  5; i++)
      {
        previewTbl.getColumnModel().getColumn(i).setMinWidth(120);
      }
    }

    /* Print the report to PDF */
    private void printReport() {
        // TODO print report
    }

    private void printBillingReport() {
      try {
      Document doc = new Document();
      PdfWriter.getInstance(doc, new FileOutputStream(System.getProperty("user.home")+"/Downloads/" +LocalDateTime.now() + ".pdf"));
      doc.open();
      PdfPTable pdfTable = new PdfPTable(this.previewTbl.getColumnCount());
      //adding table headers
      for (int i = 0; i < this.previewTbl.getColumnCount(); i++) {
          pdfTable.addCell(previewTbl.getColumnName(i));
      }
      //extracting data from the JTable and inserting it to PdfPTable
      int rows = 0;
      int cols = 0;
      for (rows = 0; rows < previewTbl.getRowCount(); rows++) {
          for (cols = 0; cols < previewTbl.getColumnCount(); cols++) {
              if(this.previewTbl.getModel().getValueAt(rows, cols) == null)
              {
                pdfTable.addCell(" ");
              }
              else {
              pdfTable.addCell(this.previewTbl.getModel().getValueAt(rows, cols).toString().replaceAll("\\<.*?>","") );
              }
          }
      }


      doc.add(pdfTable);
      doc.close();
      System.out.println("done");
      }
      catch (Exception e) {
        System.out.println("Error Printing");
      }
    }

    public void updateNameSel() {
        ArrayList<String> items = new ArrayList<>();
        // clear out the comboBox
        try {

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
                for (Map.Entry<UUID, Customer> customer : company.getCustomers().entrySet()) {
                    items.add(customer.getValue().getName());
                }
                if (items.size() > 1) {
                    // add a select all for customers
                    nameSel.addItem(SELECTALL);
                }
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

            goBtn.setEnabled(true);
            nameSel.setSelectedIndex(0);

        } catch (IllegalArgumentException arg0) {
            nameSel.addItem("No Items Found");
            nameSel.setEnabled(false);
            goBtn.setEnabled(false);
        }
    }

    private void initDefaults() {
        Font font = new Font(reportsLbl.getFont().toString(), Font.BOLD, 16);
        LocalDate today = LocalDate.now();
        reportsLbl.setFont(font);
        previewLbl.setFont(font);
        previewTbl.sizeColumnsToFit(JTable.AUTO_RESIZE_ALL_COLUMNS);
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
        fromSettings.setDateRangeLimits(null, today.minusDays(1));
        toSettings.setDateRangeLimits(null, today);

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
        gbc_goBtn.gridy = 3;
        northPane.add(goBtn, gbc_goBtn);

        // move to north
        GridBagConstraints gbc_nameLbl = new GridBagConstraints();
        gbc_nameLbl.anchor = GridBagConstraints.EAST;
        gbc_nameLbl.insets = new Insets(0, 0, 5, 5);
        gbc_nameLbl.gridx = 0;
        gbc_nameLbl.gridy = 3;
        northPane.add(nameLbl, gbc_nameLbl);
        GridBagConstraints gbc_nameSel = new GridBagConstraints();
        gbc_nameSel.fill = GridBagConstraints.BOTH;
        gbc_nameSel.insets = new Insets(0, 0, 5, 5);
        gbc_nameSel.gridx = 1;
        gbc_nameSel.gridy = 3;
        northPane.add(nameSel, gbc_nameSel);
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
        gbl_southPane.rowHeights = new int[] { 0, 0, 0, 0, 0 };
        gbl_southPane.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        gbl_southPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        southPane.setLayout(gbl_southPane);

        GridBagConstraints gbc_previewLbl = new GridBagConstraints();
        gbc_previewLbl.fill = GridBagConstraints.VERTICAL;
        gbc_previewLbl.anchor = GridBagConstraints.EAST;
        gbc_previewLbl.insets = new Insets(0, 0, 5, 5);
        gbc_previewLbl.gridx = 0;
        gbc_previewLbl.gridy = 1;
        southPane.add(previewLbl, gbc_previewLbl);
        previewTbl.setFillsViewportHeight(true);
        previewTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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

    public static void main(String [] args) {

      AcmeUI acme = new AcmeUI();
      acme.getCompany().setCurrentUser(new User());
      acme.setPanel(new ReportsPanel());
  }

}
