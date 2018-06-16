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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import acme.pd.Company;

public class ClerksListPanel extends AcmeBaseJPanel {

    private static final long serialVersionUID = 1L;
    // Declarations
    Company company;
    JLabel mainLbl = new JLabel("Clerks");
    JTable listTbl = new JTable() {
        @Override
        public Class getColumnClass(int column) {
            switch (column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Boolean.class;
            case 3:
                return JButton.class;
            default:
                return Boolean.class;
            }
        }
    };
    
    JScrollPane listScrll = new JScrollPane(listTbl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JButton addBtn = new JButton("New Clerk");

    /* Constructor */
    public ClerksListPanel() {
        listTbl.setFillsViewportHeight(true);

        // TODO remove from constructor
        
    }

    public void buildPanel() {

        company = this.getCompany();
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
        GridBagConstraints gbc_addBtn = new GridBagConstraints();
        gbc_addBtn.fill = GridBagConstraints.HORIZONTAL;
        gbc_addBtn.anchor = GridBagConstraints.NORTH;
        gbc_addBtn.insets = new Insets(0, 0, 0, 5);
        gbc_addBtn.gridx = 2;
        gbc_addBtn.gridy = 2;
        add(addBtn, gbc_addBtn);
    }

    private void initDefaults() {

        String[] columnNames = { "Name", "Username", "Active", "Edit" };
        Object[][] data = { { "Homer", "Simpson", false, "Edit" }, { "Madge", "Simpson", true, "Edit" },
                { "Bart", "Simpson", true, "Edit" }, { "Lisa", "Simpson", false, "Edit" }, };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        listTbl.setModel(model);

        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                System.out.println(((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
                
            }
        };

        String name = company.getCurrentUser().getName();
        
        String uName = company.getCurrentUser().getUsername();
        Boolean active = company.getCurrentUser().isActive();
        model.addRow(new Object[]{name, uName, active, "Edit"});
        ButtonColumn button = new ButtonColumn(listTbl, delete, 3);
        button.setMnemonic(KeyEvent.VK_D);
    }
}
