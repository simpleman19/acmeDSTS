package acme.ui;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.MapIntersection;
import acme.pd.Path;
import acme.pd.Road;
import acme.pd.Ticket;
import acme.pd.User;

public class TicketListUI extends AcmeBaseJPanel {
    
    
        
    private static final long serialVersionUID = 1L;
    private Company company;
    private boolean displayWithComplete;
    
    public static final String RESULT
    = System.getProperty("user.home")+"/Downloads/temp.pdf";
    
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
        

       
        public TicketListUI (boolean displayWithComplete) { 
            
            this.displayWithComplete = displayWithComplete;
            
        }
    
        
        @Override
        public void buildPanel() {
            
            company = this.getCompany();
            table.setFillsViewportHeight(true);
            
            
            if (this.displayWithComplete == true)
            {
            	boxComp.setSelected(true);
            }
            
            
            this.initLayout();
            this.initDefaults();
            
            this.revalidate();
            this.repaint();
            
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
        final int   CANCEL_COL  = 4;
        final int   COMP_COL    = 5;
        final int   PRINT_COL   = 6;
        final int   ID_COL      = 7;
        final int   COMBO_COL   = 1;
        
        
        
        // image icons
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

                //only have buttons and combo box columns be editable
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if(mColIndex == COMBO_COL || mColIndex == PRINT_COL || 
                    mColIndex == COMP_COL || mColIndex == CANCEL_COL)
                {
                  return true;
                }
                return false;
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
        sortKeys.add(new RowSorter.SortKey(DEPART_COL, SortOrder.ASCENDING));
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

        Action printAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                System.out.println("Print");
                int modelRow = Integer.valueOf(e.getActionCommand());
                for (Map.Entry<UUID, Ticket> ticket : company.getTickets().entrySet()) {
                    if (ticket.getValue().getId().equals(table.getModel().getValueAt(modelRow, ID_COL))) {
                      //print 
                      try {
                            Document document = new Document();
                            PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.home")+"/Downloads/" + ticket.getValue().getPackageID() +".pdf"));
                            document.open();
                            String directions="";
                            for(String temp : ticket.getValue().getDeliveryInstructions())
                            {
                              directions = directions + "\n" + temp;
                            }
                            System.out.println("here");
                            document.add(new Paragraph(directions));
                            document.close();
                      }
                      catch(Exception printError)
                      {
                        System.out.println("Error printing");
                      }
                      }  
                }
                
                
            }
        };
        
        Action compAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                System.out.println("Compelete");
                Ticket ticket = company.getTickets().get(table.getModel().getValueAt(modelRow, ID_COL));
                System.out.println(ticket.getCourier().getName());
                getAcmeUI().ticketComplete(company.getTickets().get(table.getModel().getValueAt(modelRow, ID_COL)));
            }
        };
        
        Action cancelAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
            	int modelRow = Integer.valueOf(e.getActionCommand());

                System.out.print("Cancel");
                Ticket ticket = company.getTickets().get(table.getModel().getValueAt(modelRow, ID_COL));
                company.getTickets().remove(table.getModel().getValueAt(modelRow, ID_COL));
                company.update();
                ticket.delete();
                refreshPage();
            }
        };
        
      
       addBtn.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                goToTicketCreate();
             }
           });

      //-------------------------------------------- 
     
       //populate combo box
       JComboBox comboBox = new JComboBox();
       comboBox.addItem("None");
       Map<String, Integer> courierMap = new HashMap<String, Integer>();
       Map<String, Courier> courierMapForTickets = new HashMap<String, Courier>();
       Map<String, Integer> courierMapForCreatingCouriers = new HashMap<String, Integer>();
       int count = 1;
       for(Map.Entry<UUID, Courier> courier: company.getCouriers().entrySet())
       {
         String courierName = courier.getValue().getName();
         comboBox.addItem(courierName);
         courierMap.put(courierName, count);
         courierMapForTickets.put(courierName,courier.getValue());
         courierMapForCreatingCouriers.put(courierName, courier.getValue().getCourierNumber());
         count++;
       }
       
       //display combo box
       table.getColumnModel().getColumn(COMBO_COL).setCellEditor(new DefaultCellEditor(comboBox) {

       });
       
       //used to set courier for tickets when value in combo box changes
       model.addTableModelListener(new TableModelListener() {
         @Override
         public void tableChanged(TableModelEvent e) {
             int type = e.getType();
             switch (type) {
                 case TableModelEvent.UPDATE:
                     if (e.getFirstRow() - e.getLastRow() == 0) {
                         TableModel model = (TableModel) e.getSource();
                         int row = e.getFirstRow();
                         int col = e.getColumn();
                         
                         if(col == 1)
                         {
                             System.out.println("Update " + row + "x" + col + " = " + model.getValueAt(row, col));
    
      
                             if(company.getTickets().get(table.getModel().getValueAt(row, ID_COL)).getCourier() != null) {
                               company.getTickets().get(table.getModel().getValueAt(row, ID_COL)).setCourier(courierMapForTickets.get(model.getValueAt(row, col)));   
                             }
                             else {
                               if(!model.getValueAt(row, col).equals("None"))
                               {
                                 Courier temp = new Courier();
                                 temp.setActive(courierMapForTickets.get(model.getValueAt(row, col)).isActive());
                                 temp.setCourierNumber(courierMapForTickets.get(model.getValueAt(row, col)).getCourierNumber());
                                 temp.setName(courierMapForTickets.get(model.getValueAt(row, col)).getName());
                                 company.getTickets().get(table.getModel().getValueAt(row, ID_COL)).setCourier(temp);
                                 temp.create();
                               }
                             }
                             company.getTickets().get(table.getModel().getValueAt(row, ID_COL)).update();
                             company.update();
                             
                             System.out.println("Success");
                         }
                     }
                     break;
             }
         }
     });
       
       //To print pretty dates and times
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd hh:mm a");

       /* Iterate through the tickets in the company and populate the table */
       for (Map.Entry<UUID, Ticket> tickets : company.getTickets().entrySet()) {

           if(tickets.getValue().getDeliveryTime() == null || displayWithComplete)
           {
               Courier rowCourier = tickets.getValue().getCourier();
               if(rowCourier != null)
               {
                 comboBox.setSelectedIndex(courierMap.get(rowCourier.getName()));
               } else {
                   comboBox.setSelectedIndex(0);
               }

               Vector<Object> row = new Vector<Object>();
               row.add(tickets.getValue().getPackageID());
               row.add(comboBox.getItemAt(comboBox.getSelectedIndex()));
               row.add(tickets.getValue().getEstimatedDepartureTime().format(formatter));
               row.add(tickets.getValue().getEstimatedReturnTime().format(formatter));
               row.add(cancel);
               row.add(comp);
               row.add(print);
               row.add(tickets.getValue().getId());
               model.addRow(row);
           }

       }
       

        //display buttons
        ButtonColumn buttonPrint = new ButtonColumn(table, printAction, PRINT_COL);
        buttonPrint.setMnemonic(KeyEvent.VK_D);
        ButtonColumn buttonComp = new ButtonColumn(table, compAction, COMP_COL);
        buttonComp.setMnemonic(KeyEvent.VK_D);
        ButtonColumn buttonCancel = new ButtonColumn(table, cancelAction, CANCEL_COL);
        buttonCancel.setMnemonic(KeyEvent.VK_D);
    }
    
    public void refreshPage()
    {
      getAcmeUI().ticketList(this.displayWithComplete);
    }
    
    //method for checkbox
    //toggles back and forth between the two views
    public void showComplete()
    {
      if(this.displayWithComplete == false)
      {
        this.getAcmeUI().ticketList(true);
      }
      else
      {
        this.getAcmeUI().ticketList(false);
      }
    }
    
    public void goToTicketCreate()
    {
        this.getAcmeUI().ticketCreate();
    }

    public static void main(String [] args) {
      AcmeUI acme = new AcmeUI();
      acme.setPanel(new TicketListUI(false));
      acme.getCompany().setCurrentUser(new User());
  }
       
}


