package acme.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TicketUI extends JFrame {
	

private static final long serialVersionUID = 1L;
private JTable table;
private JCheckBox boxComp = new JCheckBox ("Show Completed");
private JButton btnAdd = new JButton("New Ticket");
	

	public TicketUI() {

		// create JFrame and JTable
		super();
	
	
      //new
        table = new JTable() {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                case 0:
                    return Integer.class;
                case 1: //combo//
                    return String.class; 
                case 2:
                    return String.class;
                case 3:
                    return String.class;
                case 4:
                    return JButton.class;
                case 5:
                    return JButton.class;
                case 6:
                    return JButton.class;
                case 7:
                    return UUID.class;
                default:
                    return Boolean.class;
                }
            }
        }; 
        // 
        
        
        btnAdd.setBounds(10, 320, 100, 30);
        boxComp.setBounds(255, 85, 250, 25);
  
        // create JScrollPane
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(10, 120, 880, 200);
        
        setLayout(null);
        
        add(pane);
        
        add(btnAdd);
        add(boxComp);        
        
        // create an array of objects to set the row data
        
        
        initDefaults();
        
        
        setSize(900,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        
    }
	
	private void initDefaults() {

//        final int NAME_COL = 0;
//        final int ACT_COL = 2;
//        final int EDIT_COL = 3;
//        final int ID_COL = 4;
        
        final int CANCEL_COL = 4;
        final int COMP_COL = 5;
        final int PRINT_COL = 6;
        // image icon
        ImageIcon cancel = new ImageIcon(
                new ImageIcon("resources/cancel.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        ImageIcon print = new ImageIcon(
                new ImageIcon("resources/print.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        ImageIcon comp = new ImageIcon(
                new ImageIcon("resources/comp.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        
        //
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.sizeColumnsToFit(JTable.AUTO_RESIZE_ALL_COLUMNS);
     // create a table model and set a Column Identifiers to this model 
        String[] columnNames = {"Package ID", "Courier", "Departure Time", "Return Time", "Cancel", "Complete",
    			"Print"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames) ;
//        {
//            private static final long serialVersionUID = 1L;
//
//            public boolean isCellEditable(int rowIndex, int mColIndex) {
//                return (mColIndex == EDIT_COL);
//            }
//        };
        table.setModel(model);
        table.getColumnModel().getColumn(CANCEL_COL).setMaxWidth(65);
        table.getColumnModel().getColumn(COMP_COL).setMaxWidth(65);
        table.getColumnModel().getColumn(PRINT_COL).setMaxWidth(65);
        table.setRowHeight(35);
        
     // Change A JTable Background Color, Font Size, Font Color, Row Height
        table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.black);
        Font font = new Font("",1,22);
        table.setFont(font);
        table.setRowHeight(30);

        /* Sort by active users, then by name */
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        
        table.setRowSorter(sorter);
        sorter.setSortable(CANCEL_COL, false);
        sorter.setSortable(COMP_COL, false);
        sorter.setSortable(PRINT_COL, false);
        sorter.sort();
        
     // button add row
        btnAdd.addActionListener(new ActionListener(){
        	
        	
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	
            	
            	Vector<Object> row = new Vector<Object>();
                row.add("dfsf");
                row.add("fsfs");
                row.add("dfsf");
                row.add("fsfs");
                row.add(cancel);
                row.add(comp);
                row.add(print);
                model.addRow(row);
             
            }
        });

        // hide the column containing the UUIDs
        
        
        /////////////????????????????????///////////////
//        TableColumn column = table.getColumnModel().getColumn(ID_COL);
//        column.setMinWidth(0);
//        column.setMaxWidth(0);
//        column.setWidth(0);
//        column.setPreferredWidth(0);
/////////////????????????????????///////////////

        /*
         * This button will search for the user in the list and find the User associated
         * with the ID so it can pass it to the add/update screen for use
         */
//        Action editAction = new AbstractAction() {
//            private static final long serialVersionUID = 1L;
//
//            public void actionPerformed(ActionEvent e) {
//                int modelRow = Integer.valueOf(e.getActionCommand());
//                // find the user and go to the update screen
//                for (Map.Entry<UUID, User> users : company.getUsers().entrySet()) {
//                    if (users.getKey().equals(listTbl.getModel().getValueAt(modelRow, ID_COL))) {
//                        getAcmeUI().userAddUpdate(users.getValue());
//                    }
//                }
//            }
//        };

        /* Iterate through the users in the company and populate the table */
//        for (Map.Entry<UUID, User> users : company.getUsers().entrySet()) {
//            Vector<Object> row = new Vector<Object>();
//            row.add(users.getValue().getName());
//            row.add(users.getValue().getUsername());
//            row.add(users.getValue().isActive());
//            row.add(cancel);
//            row.add(users.getKey());
//            model.addRow(row);
//        }
//
//        ButtonColumn button = new ButtonColumn(listTbl, editAction, EDIT_COL);
//        button.setMnemonic(KeyEvent.VK_D);
    }

		
		
	

}
