package acme.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import acme.pd.Company;
import acme.pd.Customer;
import acme.pd.MapIntersection;
import acme.pd.Road;
import acme.pd.User;

public class CustomerListUI extends AcmeBaseJPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Customer[] customersArray;

    public CustomerListUI() {
        super();
        // buildPanel();
    }

    public void buildPanel() {

        // my test data
        Map<UUID, Customer> testData = getCompany().getCustomers();
        Collection<Customer> values = testData.values();
        ArrayList<Customer> listOfValues = new ArrayList<Customer>(values);
        customersArray = listOfValues.toArray(new Customer[listOfValues.size()]);
        Object[][] objects = getObjectsForTable(customersArray);
        String[] cols = new String[] { "Number", "Name", "Location", "Active", "Edit" };

        // scroll and table. I made this in netbeans, so it looks a little different
        // I've updated this so that the layout matches the other list pages -- Jacob
        // Collins
        JScrollPane jScrollPane1;
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 159, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 27, 200, 0, 40, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        // new customer button
        JButton btnNewCustomer = new JButton("New Customer");
        btnNewCustomer.setSize(120, 40);
        // btnNewCustomer.setBackground(lightBlue);
        btnNewCustomer.setOpaque(true);
        btnNewCustomer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goToNewCustomerPage();
            }
        });

        // title label
        JLabel lblCustomers = new JLabel("Customers");
        lblCustomers.setFont(new Font(lblCustomers.getFont().toString(), Font.BOLD, 16));
        lblCustomers.setBounds(20, 20, 200, 27);
        add(lblCustomers);
        GridBagConstraints gbc_lblCustomers = new GridBagConstraints();
        gbc_lblCustomers.fill = GridBagConstraints.BOTH;
        gbc_lblCustomers.insets = new Insets(15, 65, 5, 5);
        gbc_lblCustomers.gridx = 0;
        gbc_lblCustomers.gridy = 0;
        add(lblCustomers, gbc_lblCustomers);
        JTable jTable1 = new JTable();
        jTable1.setFillsViewportHeight(true);

        // set up table
        jTable1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTable1.setModel(new javax.swing.table.DefaultTableModel(objects, cols) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;
            Class<?>[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.Boolean.class, javax.swing.JButton.class };
            boolean[] canEdit = new boolean[] {
                    // need last one true so can click button
                    false, false, false, false, true };

            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        // needed for buttons in table
        TableColumn editCol = jTable1.getColumnModel().getColumn(4);

        editCol.setCellRenderer(new ButtonRenderer());
        editCol.setCellEditor(new ButtonEditor(new JTextField("resources/pen.jpg")));

        // more table properties
        jTable1.setColumnSelectionAllowed(false);
        jTable1.setRowHeight(35);
        jTable1.setRowMargin(2);
        jTable1.setSelectionBackground(new Color(93, 184, 202));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable1.setShowGrid(true);
        jTable1.setAutoCreateRowSorter(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1 = new JScrollPane(jTable1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        GridBagConstraints gbc_jScrollPane1 = new GridBagConstraints();
        gbc_jScrollPane1.gridheight = 2;
        gbc_jScrollPane1.gridwidth = 2;
        gbc_jScrollPane1.fill = GridBagConstraints.BOTH;
        gbc_jScrollPane1.insets = new Insets(0, 65, 5, 50);
        gbc_jScrollPane1.gridx = 0;
        gbc_jScrollPane1.gridy = 1;
        add(jScrollPane1, gbc_jScrollPane1);
        GridBagConstraints gbc_btnNewCustomer = new GridBagConstraints();
        gbc_btnNewCustomer.insets = new Insets(0, 65, 15, 5);
        gbc_btnNewCustomer.anchor = GridBagConstraints.WEST;
        gbc_btnNewCustomer.fill = GridBagConstraints.VERTICAL;
        gbc_btnNewCustomer.gridx = 0;
        gbc_btnNewCustomer.gridy = 3;
        add(btnNewCustomer, gbc_btnNewCustomer);

    }

    public static void main(String[] args) {
        AcmeUI acme = new AcmeUI();
        acme.setPanel(new CustomerListUI());
        acme.getCompany().setCurrentUser(new User());
    }

    // navigation methods-----------------------------
    public void goToEditPage(String customerName) {
        customerName = customerName.substring(2);
        Customer customerToPass = null;
        for (int x = 0; x < this.customersArray.length; x++) {
            if (this.customersArray[x].getName().equals(customerName)) {
                customerToPass = this.customersArray[x];
            }
        }
        this.getAcmeUI().customerAddUpdate(customerToPass);
    }

    public void goToNewCustomerPage() {
        // go to new customer page
        this.getAcmeUI().customerAddUpdate(null);
    }
    // ------------------------------------------------

    public Object[][] getObjectsForTable(Customer[] listOfCustomers) {
    	Company company = this.getCompany();
        ImageIcon pen = new ImageIcon(
                new ImageIcon("resources/pen.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        Object[][] toReturn = new Object[listOfCustomers.length][5];
        for (int x = 0; x < listOfCustomers.length; x++) {
            try {
                toReturn[x][0] = listOfCustomers[x].getCustomerNumber();
                toReturn[x][1] = listOfCustomers[x].getName();
                toReturn[x][2] = listOfCustomers[x].getIntersection(company.getMap()).getIntersectionName();
                toReturn[x][3] = listOfCustomers[x].isActive();
                toReturn[x][4] = pen;
            } catch (NullPointerException e) {
                System.out.println("NullPointerException caught");
                System.out.println("Missing Customer data");
            }
        }

        return toReturn;
    }

    // -----------------------------------------------------------
    // Tutorial From https://www.youtube.com/watch?v=3LiSHPqbuic
    // Used to make buttons in the table. Creates them and handles the
    // clicking of them.
    // -----------------------------------------------------------
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object obj, boolean selected, boolean focused,
                int row, int col) {

            // if there isn't any text then set the passed object to empty string
            // otherwise set it to the text
            if (obj == null) {
                setText("");
            } else if (obj.getClass().toGenericString().contains("ImageIcon")) {
                setIcon((Icon) obj);
            } else {
                setText(obj.toString());
            }
            // this is a Jbutton instance
            return this;
        }

    }

    // button editor class - handles what happens when the button is clicked
    private class ButtonEditor extends DefaultCellEditor {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        protected JButton btn;
        private String lbl; // the text on the button
        private Boolean clicked; // used to determine when the button has been clicked

        public ButtonEditor(JTextField txt) {
            super(txt);

            // creates button
            btn = new JButton();
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setOpaque(true);

            // when the button is clicked
            btn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // notifies all listeners
                    fireEditingStopped();
                }
            });
        }

        // have to override some methods
        @Override
        public Component getTableCellEditorComponent(JTable table, Object obj, boolean selected, int row, int col) {

            // set the label to the customer or to a blank
            lbl = (obj == null) ? "" : table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            clicked = true;
            return btn;
        }

        // dealing with clicks
        @Override
        public Object getCellEditorValue() {

            // determine if button has been clicked
            if (clicked) {
                // go to edit page for that customer
                goToEditPage(lbl);
            }
            // since already clicked it is now false
            clicked = false;
            return new String(lbl);
        }

        // no longer clicking
        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    // -------------------------------------------------------------------

    // test data creation
    public Customer[] getTestData() {
        Customer tempCustomer1 = new Customer();
        tempCustomer1.setActive(true);
        tempCustomer1.setAvenueName("5");
        tempCustomer1.setCustomerNumber(1);
        tempCustomer1.setName("Dee Dee Corp");
        tempCustomer1.setStreetName("A");

        Customer tempCustomer2 = new Customer();
        tempCustomer2.setActive(true);
        tempCustomer2.setAvenueName("2");
        tempCustomer2.setCustomerNumber(2);
        tempCustomer2.setName("Paresa Corp");
        tempCustomer2.setStreetName("C");

        Customer tempCustomer3 = new Customer();
        tempCustomer3.setActive(false);
        tempCustomer3.setAvenueName("3");
        tempCustomer3.setCustomerNumber(3);
        tempCustomer3.setName("Jacob Corp");
        tempCustomer3.setStreetName("G");

        Customer tempCustomer4 = new Customer();
        tempCustomer4.setActive(true);
        tempCustomer4.setAvenueName("4");
        tempCustomer4.setCustomerNumber(4);
        tempCustomer4.setName("Emily Corp");
        tempCustomer4.setStreetName("D");

        Customer tempCustomer5 = new Customer();
        tempCustomer5.setActive(true);
        tempCustomer5.setAvenueName("5");
        tempCustomer5.setCustomerNumber(5);
        tempCustomer5.setName("Chance Corp");
        tempCustomer5.setStreetName("B");

        Customer[] data = new Customer[] { tempCustomer1, tempCustomer2, tempCustomer3, tempCustomer4, tempCustomer5, };

        return data;

    }

}
