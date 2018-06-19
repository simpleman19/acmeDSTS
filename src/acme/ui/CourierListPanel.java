package acme.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import acme.pd.Company;
import acme.pd.Courier;

public class CourierListPanel extends AcmeBaseJPanel {

    private static final long serialVersionUID = 1L;
    // Declarations
    Company company;
    JLabel mainLbl = new JLabel("Couriers");
    JTable listTbl = new JTable() {
        private static final long serialVersionUID = 1L;

        @Override
        public Class<?> getColumnClass(int column) {
            switch (column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Boolean.class;
            case 3:
                return JButton.class;
            case 4:
                return UUID.class;
            case 5:
                return JComboBox.class;
            default:
                return Boolean.class;
            }
        }
    };

    JScrollPane listScrll = new JScrollPane(listTbl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JButton addBtn = new JButton("New Courier");

    /* Constructor */
    public CourierListPanel() {

    }

    @Override
    public void buildPanel() {

        company = this.getCompany();
        listTbl.setFillsViewportHeight(true);
        initLayout();
        initDefaults();

    }

    private void initLayout() {

        mainLbl.setFont(new Font(mainLbl.getFont().toString(), Font.BOLD, 16));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 50, 50, 50, 50 };
        gridBagLayout.rowHeights = new int[] { 15, 100, 25 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
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
        GridBagConstraints gbc_addBtn = new GridBagConstraints();
        gbc_addBtn.fill = GridBagConstraints.HORIZONTAL;
        gbc_addBtn.anchor = GridBagConstraints.NORTH;
        gbc_addBtn.insets = new Insets(0, 65, 15, 5);
        gbc_addBtn.gridx = 0;
        gbc_addBtn.gridy = 2;
        add(addBtn, gbc_addBtn);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                getAcmeUI().courierAddUpdate(null);
            }
        });
    }

    private void initDefaults() {

        final int NAME_COL = 0;
        final int ACT_COL = 2;
        final int EDIT_COL = 3;
        final int ID_COL = 4;
        ImageIcon pen = new ImageIcon(
                new ImageIcon("resources/pen.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        listTbl.setAutoCreateRowSorter(true);
        listTbl.getTableHeader().setReorderingAllowed(false);
        listTbl.sizeColumnsToFit(JTable.AUTO_RESIZE_ALL_COLUMNS);
        String[] columnNames = { "Number", "Name", "Active", "Edit", null, "combo" };
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return (mColIndex == EDIT_COL || mColIndex == 5);
            }
        };
        listTbl.setModel(model);
        listTbl.getColumnModel().getColumn(ACT_COL).setMaxWidth(65);
        listTbl.getColumnModel().getColumn(EDIT_COL).setMaxWidth(65);
        listTbl.setRowHeight(35);

        /* Sort by active couriers, then by name */
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(listTbl.getModel());
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(ACT_COL, SortOrder.DESCENDING));
        sortKeys.add(new RowSorter.SortKey(NAME_COL, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        listTbl.setRowSorter(sorter);
        sorter.setSortable(EDIT_COL, false);
        sorter.sort();

        // hide the column containing the UUIDs
        TableColumn column = listTbl.getColumnModel().getColumn(ID_COL);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
        column.setPreferredWidth(0);

        /*
         * This button will search for the courier in the list and find the courier
         * associated with the ID so it can pass it to the add/update screen for use
         */
        Action editAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                // find the courier and go to the update screen
                for (Map.Entry<UUID, Courier> cour : company.getCouriers().entrySet()) {
                    if (cour.getKey().equals(listTbl.getModel().getValueAt(modelRow, ID_COL))) {
                        getAcmeUI().courierAddUpdate(cour.getValue());
                    }
                }
            }
        };

        
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("None");
        comboBox.addItem("Snowboarding");
        comboBox.addItem("Rowing");
        comboBox.addItem("Chasing toddlers");
        comboBox.addItem("Speed reading");
        comboBox.addItem("Teaching high school");
        
        comboBox.setSelectedIndex(1);
        listTbl.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboBox) {
            
            
        });
        /* Iterate through the couriers in the company and populate the table */
        for (Map.Entry<UUID, Courier> cour : company.getCouriers().entrySet()) {
            Vector<Object> row = new Vector<Object>();
            row.add(cour.getValue().getCourierNumber());
            row.add(cour.getValue().getName());
            row.add(cour.getValue().isActive());
            row.add(pen);
            row.add(cour.getKey());
            row.add(comboBox);
            model.addRow(row);
        }

        ButtonColumn button = new ButtonColumn(listTbl, editAction, EDIT_COL);
        button.setMnemonic(KeyEvent.VK_D);
    }
}
