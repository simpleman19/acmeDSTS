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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import acme.pd.Company;
import acme.pd.User;

public class TicketUI extends AcmeBaseJPanel {
		
	private static final long serialVersionUID = 1L;
	private Company company;
	private User user;
	
	// Main Label for the panel.
	JLabel mainLbl = new JLabel("Ticket List");
	
	// Creating the combo box for the panel.
	JCheckBox boxComp = new JCheckBox ("Show Completed");
	
	
	
	JTable table = new JTable() {
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

        JScrollPane listScrll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	JButton addBtn = new JButton("New Ticket");      
        

       
        public TicketUI() {  
        	
        }
	
		
		@Override
		public void buildPanel() {
			
			company = this.getCompany();
			table.setFillsViewportHeight(true);
			this.initLayout();
			this.initDefaults();
			
		}
	
	private void initLayout() {
		// Adds "Ticket List" as the label for the panel.
		mainLbl.setFont(new Font(mainLbl.getFont().toString(), Font.BOLD, 16));
		
		GridBagLayout gridbl = new GridBagLayout();
		gridbl.columnWidths = new int[] {50, 50, 50, 50};
		gridbl.rowHeights = new int[] {15, 100, 25};
		gridbl.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
        gridbl.rowWeights = new double[] { 0.0, 1.0, 0.0 };
        setLayout(gridbl);
        
		GridBagConstraints gbc_main = new GridBagConstraints();
		gbc_main.fill = GridBagConstraints.BOTH;
		gbc_main.insets = new Insets(15, 65, 5, 0);
		gbc_main.gridx = 0;
        gbc_main.gridy = 0;
        add(mainLbl, gbc_main);
        
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
        
        boxComp.setBounds(0,0,10,10);
        boxComp.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            //code for check box here
            //probs a method call
          }
        });
        add(boxComp);
		
	}
	
	
	
	
	
	
	
	// This is a method that can only be called from inside the class from which it is defined. 
	private void initDefaults() {   
        
		// creating variables for the column index.
        final int 	CANCEL_COL 	= 4;
        final int 	COMP_COL 	= 5;
        final int 	PRINT_COL 	= 6;
        final int 	ID_COL 		= 7;
        
        // image icon
        ImageIcon cancel = new ImageIcon(
                new ImageIcon("resources/cancel.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        
        ImageIcon print = new ImageIcon(
                new ImageIcon("resources/printer.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        
        ImageIcon comp = new ImageIcon(
                new ImageIcon("resources/tick-mark.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.sizeColumnsToFit(JTable.AUTO_RESIZE_ALL_COLUMNS);
     
        
        // create a table model and set a Column Identifiers to this model 
        String[] columnNames = {"Package ID", "Courier", "Departure Time", "Return Time", "Cancel", "Complete",
    			"Print", null};

        // Creates a table with 0 rows and the number of columns found in the
        // columnNames array above. 
        
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            
        	private static final long serialVersionUID = 1L;

        		// No editable cell in our case
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return (mColIndex == PRINT_COL);
            }
        };
        
        table.setModel(model);
        table.getColumnModel().getColumn(CANCEL_COL).setMaxWidth(65);
        table.getColumnModel().getColumn(COMP_COL).setMaxWidth(65);
        table.getColumnModel().getColumn(PRINT_COL).setMaxWidth(65);
        //table.getColumnModel().getColumn(ID_COL).setMaxWidth(0);
        table.setRowHeight(35);
        
        TableColumn column = table.getColumnModel().getColumn(ID_COL);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
        column.setPreferredWidth(0);

//button methods---------------------------------------------------
        /*
         * Use this method for your print button
         */
        Action printAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                //Code here
            }
        };
        
        /*
         * Use this method for your comp button
         */
        Action compAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                //Code here
            }
        };
        
        /*
         * Use this method for your cancel button
         */
        Action cancelAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                //Code here
            }
        };
        
//-------------------------------------------------------------
        
     // Change A JTable Background Color, Font Size, Font Color, Row Height
	//        table.setBackground(Color.LIGHT_GRAY);
	//        table.setForeground(Color.black);
	//        Font font = new Font("",1,22);
	//        table.setFont(font);
	//        table.setRowHeight(30);

//        /* Sort by active users, then by name */
//        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
//        
//        table.setRowSorter(sorter);
//        sorter.setSortable(CANCEL_COL, false);
//        sorter.setSortable(COMP_COL, false);
//        sorter.setSortable(PRINT_COL, false);
//        sorter.sort();
        
     // button add row
//        addBtn.addActionListener(new ActionListener(){
//        	
//        	
//            @Override
//            public void actionPerformed(ActionEvent e) {

            	Vector<Object> row = new Vector<Object>();
                row.add("col1");
                row.add("col2");
                row.add("col3");
                row.add("col4");
                row.add(cancel);
                row.add(comp);
                row.add(print);
                model.addRow(row);
            
//            }
//        });

        
        
        
     



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
                
        //This is what is making your buttons visible
        //I had to add in the ButtonColumn class to make them work
        //I just noticed that Jacob had done that.
        //They take the parameters: 1. table(your table you want them in)
        //2.action(the method you want called when they are clicked)
        //3.and the colounm to place them in
        ButtonColumn buttonPrint = new ButtonColumn(table, printAction, PRINT_COL);
        buttonPrint.setMnemonic(KeyEvent.VK_D);
        ButtonColumn buttonComp = new ButtonColumn(table, compAction, COMP_COL);
        buttonComp.setMnemonic(KeyEvent.VK_D);
        ButtonColumn buttonCancel = new ButtonColumn(table, cancelAction, CANCEL_COL);
        buttonCancel.setMnemonic(KeyEvent.VK_D);
    
	
	}

	//So you can test it from here
    public static void main(String [] args) {
      AcmeUI acme = new AcmeUI();
      acme.setPanel(new TicketUI());
      acme.getCompany().setCurrentUser(new User());
  }
		
	

}
