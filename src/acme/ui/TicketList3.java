package acme.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.Ticket;
import acme.pd.User;

public class TicketList3 extends AcmeBaseJPanel {
	
	
		
	private static final long serialVersionUID = 1L;
	private Company company;
	private User user;
	private boolean displayWithComplete;
	
	// Main Label for the panel.
	JLabel mainLbl = new JLabel("Ticket List");
	
	// Creating the check box for the panel.
	JCheckBox boxComp = new JCheckBox ("Show Completed");
	
	
	
	 
	JTable table = new JTable() {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int column) {
			                switch (column) {
			                case 0:
			                    return Integer.class;
			                case 1: //combo//
			                    return JComboBox.class; 
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
        

       
        public TicketList3 (boolean displayWithComplete) { 
        	
        	this.displayWithComplete = displayWithComplete;
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
            showComplete();
          }
        });
        add(boxComp);
		
	}
	
	
	
	
	
	
	
	// This is a method that can only be called from inside the class from which it is defined. 
	private void initDefaults() {   
        
		// creating variables for the column index.
		final int   DEPART_COL  = 2;
        final int 	CANCEL_COL 	= 4;
        final int 	COMP_COL 	= 5;
        final int 	PRINT_COL 	= 6;
        final int 	ID_COL 		= 7;
        final int   COMBO_COL   = 1;
        
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
        
        
        table.setRowHeight(35);

        /* Sort by active users, then by name */
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(DEPART_COL, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowSorter(sorter);
        sorter.setSortable(CANCEL_COL, false);
        sorter.setSortable(COMP_COL, false);
        sorter.setSortable(PRINT_COL, false);
        sorter.sort();
        
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
            	System.out.print("Print");
                //print 
            	
            }
        };
        
        /*
         * Use this method for your comp button
         */
        Action compAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                //Code here
            	int modelRow = Integer.valueOf(e.getActionCommand());
            	   for (Map.Entry<UUID, Ticket> ticket : company.getTickets().entrySet()) {
                       if (ticket.getValue().getId().equals(table.getModel().getValueAt(modelRow, ID_COL))) {
                           getAcmeUI().ticketComplete(ticket.getValue());
                       }
                   }
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
        
      
 
       addBtn.addActionListener(new ActionListener() {
    	     public void actionPerformed(ActionEvent e) {
    	    	goToTicketCreate();
    	     }
    	   });
       
       Courier tempCourier = new Courier();
       tempCourier.setCourierNumber(5);
       tempCourier.setName("ABC");
       Ticket tempTicket = new Ticket();
       tempTicket.setCourier(tempCourier);
       tempTicket.setDepartureTime(LocalDateTime.now());
       tempTicket.setDeliveryTime(LocalDateTime.now());
       company.getTickets().put(null, tempTicket);
       
     
       
//       combo1.setSelectedIndex(1);
//       
//       
//       TableColumn comboCol = table.getColumnModel().getColumn(COMBO_COL);
//       comboCol.setCellEditor(new DefaultCellEditor(combo1));
       
       JComboBox comboBox = new JComboBox();
       comboBox.addItem("None");
       comboBox.addItem("Snowboarding");
       comboBox.addItem("Rowing");
       comboBox.addItem("Chasing toddlers");
       comboBox.addItem("Speed reading");
       comboBox.addItem("Teaching high school");
       
       comboBox.setSelectedIndex(1);
       table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboBox) {
           
           
       });
       
       if ( displayWithComplete )
       {
    	   System.out.println("Displaying with completed");
	       /* Iterate through the tickets in the company and populate the table */
	       for (Map.Entry<UUID, Ticket> tickets : company.getTickets().entrySet()) {
	           Vector<Object> row = new Vector<Object>();
	           row.add(tickets.getValue().getPackageID());
	           row.add(comboBox);
	           row.add(tickets.getValue().getDepartureTime());
	           //Used delivery time
	           row.add(tickets.getValue().getDeliveryTime());
	           row.add(cancel);
	           row.add(comp);
	           row.add(print);
	           row.add(tickets.getValue().getId());
	           model.addRow(row);
	       }
       }
       else
       {
    	   System.out.println("Displaying with out completed");
    	   /* Iterate through the tickets in the company and populate the table */
	       for (Map.Entry<UUID, Ticket> tickets : company.getTickets().entrySet()) {
	    	   
	    	   if(tickets.getValue().getDeliveryTime() == null)
	    	   {
		           Vector<Object> row = new Vector<Object>();
		           row.add(tickets.getValue().getPackageID());
		           row.add(comboBox);
		           row.add(tickets.getValue().getDepartureTime());
		           //Used delivery time
		           row.add(tickets.getValue().getDeliveryTime());
		           row.add(cancel);
		           row.add(comp);
		           row.add(print);
		           row.add(tickets.getValue().getId());
		           model.addRow(row);
	    	   }
	       
	       }
       }
       
     //combobox
       

       
       
//       //Tool tip 
//       DefaultTableCellRenderer renderer = 
//       		new DefaultTableCellRenderer ();
//       	renderer.setToolTipText("Click for combo box");
//       comboCol.setCellRenderer(renderer);
//       
        ButtonColumn buttonPrint = new ButtonColumn(table, printAction, PRINT_COL);
        buttonPrint.setMnemonic(KeyEvent.VK_D);
        ButtonColumn buttonComp = new ButtonColumn(table, compAction, COMP_COL);
        buttonComp.setMnemonic(KeyEvent.VK_D);
        ButtonColumn buttonCancel = new ButtonColumn(table, cancelAction, CANCEL_COL);
        buttonCancel.setMnemonic(KeyEvent.VK_D);
    
	
	}
	
	public void showComplete()
	{
		this.getAcmeUI().ticketList(true);
	}
	
	public void goToTicketCreate()
	{
		 this.getAcmeUI().ticketCreate();
	}

    public static void main(String [] args) {
      AcmeUI acme = new AcmeUI();
      acme.setPanel(new TicketList3(false));
      acme.getCompany().setCurrentUser(new User());
  }
		
	

}

