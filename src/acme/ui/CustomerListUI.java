package acme.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;



import acme.pd.User;
import acme.pd.Customer;
import acme.pd.MapIntersection;
import acme.pd.Road;

public class CustomerListUI extends AcmeBaseJPanel {
  Customer [] customersArray;
  
  public CustomerListUI() {
    super();
  }
  
  public void buildPanel() {
  
    GroupLayout groupLayout = new GroupLayout(this);
    
    Color lightBlue = new Color(93, 184, 202);
    
    //title label
    JLabel lblCustomers = new JLabel("Customers");
    lblCustomers.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
    lblCustomers.setBounds(20, 20, 200, 27);
    add(lblCustomers);
    
    //my test data
    HashMap<UUID, Customer> testData = getCompany().getCustomer();
    Collection<Customer> values = testData.values();
    ArrayList<Customer> listOfValues = new ArrayList<Customer>(values);
    Set<UUID> keySet = testData.keySet();
    ArrayList<UUID> listOfKeys = new ArrayList<UUID>(keySet);
    customersArray = listOfValues.toArray(new Customer[listOfValues.size()]);
    Object[][] objects = getObjectsForTable(customersArray);
    String[] cols = new String [] {
        "Number", "Name", "Location", "Active", "Edit"
    };
    
    

    //scroll and table. I made this in netbeans, so it looks a little different
    JScrollPane jScrollPane1;
    JTable jTable1 = new JTable();

    //set up table
    jTable1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jTable1.setFont(new java.awt.Font("Lucida Grande", 0, 16)); 
    jTable1.setModel(new javax.swing.table.DefaultTableModel(objects, cols) {
        Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, javax.swing.JButton.class
        };
        boolean[] canEdit = new boolean [] {
            //need last one true so can click button
            false, false, false, false, true
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    
    //needed for buttons in table
    TableColumn editCol = jTable1.getColumnModel().getColumn(4);
    editCol.setCellRenderer(new ButtonRenderer());;
    editCol.setCellEditor(new ButtonEditor(new JTextField()));
  
    //more table properties
    jTable1.setColumnSelectionAllowed(false);
    jTable1.getTableHeader().setFont(new Font("Lucida Grande", 0, 18)); 
    jTable1.setRowHeight(24);
    jTable1.setRowMargin(2);
    jTable1.setSelectionBackground(new Color(93, 184, 202));
    jTable1.setSelectionForeground(new java.awt.Color(0, 0, 0));
    jTable1.setShowGrid(true);
    jScrollPane1 = new JScrollPane(jTable1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    
    //new customer button
    JButton btnNewCustomer = new JButton("New Customer");
    btnNewCustomer.setSize(120,40);
    btnNewCustomer.setBackground(lightBlue);
    btnNewCustomer.setOpaque(true);
    btnNewCustomer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        goToNewCustomerPage();
      }
    });
    
    //this is layout information and configuration
    //hard to understand personally
    //copy and past it into a design view to work with it
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup()
              .addGap(20)
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                  .addComponent(btnNewCustomer, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                .addComponent(lblCustomers, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
            .addGroup(groupLayout.createSequentialGroup()
              .addGap(20)
              .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, this.getSize().width-10)))
          .addContainerGap(0, Short.MAX_VALUE))
    );
    groupLayout.setVerticalGroup(
        groupLayout.createParallelGroup(Alignment.LEADING)
          .addGroup(groupLayout.createSequentialGroup()
            .addGap(20)
            .addComponent(lblCustomers, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
            .addGap(20)
            .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
            .addComponent(btnNewCustomer, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
            .addGap(175))
      );
    setLayout(groupLayout);
    
  }
  
  public static void main(String [] args) {
    AcmeUI acme = new AcmeUI();
    acme.setPanel(new CustomerListUI());
    acme.getCompany().setCurrentUser(new User());
}
  
//navigation methods-----------------------------
 public void goToEditPage(String customerName)
 { 
   customerName = customerName.substring(2);
   Customer customerToPass = null;
   for(int x = 0; x< this.customersArray.length; x++)
   {
     if(this.customersArray[x].getName().equals(customerName))
     {
       customerToPass = this.customersArray[x];
     }
   }
   this.getAcmeUI().customerAddUpdate(customerToPass);
 }
 
 public void goToNewCustomerPage()
 {
   //go to new customer page
   this.getAcmeUI().customerAddUpdate(null);
 }
//------------------------------------------------
 
 public Object[][] getObjectsForTable(Customer[] listOfCustomers)
 {
   
   Object[][] toReturn = new Object[listOfCustomers.length][5];
   for(int x = 0; x < listOfCustomers.length; x++)
   {
     try {
       toReturn[x][0] = listOfCustomers[x].getCustomerNumber();
       toReturn[x][1] = listOfCustomers[x].getName();
       toReturn[x][2] = listOfCustomers[x].getIntersection().getIntersectionName();
       toReturn[x][3] = listOfCustomers[x].isActive();
       toReturn[x][4] = '\u270E' + " "+ listOfCustomers[x].getName();
     }
     catch(NullPointerException e)
     {
       System.out.println("NullPointerException caught");
       System.out.println("Missing Customer data");
     }
   }
   
   
   return toReturn;
 }
 
//----------------------------------------------------------- 
//Tutorial From https://www.youtube.com/watch?v=3LiSHPqbuic
//Used to make buttons in the table. Creates them and handles the
//clicking of them.
//-----------------------------------------------------------
private class ButtonRenderer extends JButton implements  TableCellRenderer
{
  public ButtonRenderer() {
    setOpaque(true);
  }
  @Override
  public Component getTableCellRendererComponent(JTable table, Object obj,
      boolean selected, boolean focused, int row, int col) {

      //if there isn't any text then set the passed object to empty string
      //otherwise set it to the text
      setText((obj==null) ? "":obj.toString());

    //this is a Jbutton instance
    return this;
  }

}

//button editor class - handles what happens when the button is clicked
private class ButtonEditor extends DefaultCellEditor
{
  protected JButton btn;
   private String lbl;          //the text on the button
   private Boolean clicked;     //used to determine when the button has been clicked
  
   public ButtonEditor(JTextField txt) {
    super(txt);
  
    //creates button
    btn=new JButton();
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.setOpaque(true);
  
    //when the button is clicked
    btn.addActionListener(new ActionListener() {
  
      @Override
      public void actionPerformed(ActionEvent e) {
        //notifies all listeners 
        fireEditingStopped();
      }
    });
  }
  
   //have to override some methods
   @Override
  public Component getTableCellEditorComponent(JTable table, Object obj,
      boolean selected, int row, int col) {
  
     //set the label to the passed text if there is text, and then set the lbl to the button
     lbl=(obj==null) ? "":obj.toString();
     btn.setText(lbl);
     btn.setHorizontalAlignment(SwingConstants.LEFT);
     clicked=true;
     return btn;
  }
  
  //dealing with clicks
   @Override
  public Object getCellEditorValue() {
  
     //determine if button has been clicked
     if(clicked)
     {
       //go to edit page for that customer
       goToEditPage(lbl);
     }
    //since already clicked it is now false
    clicked=false;
    return new String(lbl);
  }
  
   // no longer clicking
   @Override
  public boolean stopCellEditing() {
    clicked=false;
    return super.stopCellEditing();
  }
  
   @Override
  protected void fireEditingStopped() {
    super.fireEditingStopped();
  }
}
//-------------------------------------------------------------------


//test data creation  
  public Customer[] getTestData()
  {
    Customer tempCustomer1 = new Customer();
    tempCustomer1.setActive(true);
    tempCustomer1.setAvenueName("5");
    tempCustomer1.setCustomerNumber(1);
    tempCustomer1.setName("Dee Dee Corp");
    tempCustomer1.setStreetName("A");
    MapIntersection tempIntersection1 = new MapIntersection();
    Road tempRoad1 = new Road();
    tempRoad1.setName("5");
    Road tempRoad11 = new Road();
    tempRoad11.setName("A");
    tempIntersection1.setEWroad(tempRoad1);
    tempIntersection1.setNSroad(tempRoad11);
    tempCustomer1.setIntersection(tempIntersection1);
    
    Customer tempCustomer2 = new Customer();
    tempCustomer2.setActive(true);
    tempCustomer2.setAvenueName("2");
    tempCustomer2.setCustomerNumber(2);
    tempCustomer2.setName("Paresa Corp");
    tempCustomer2.setStreetName("C");
    MapIntersection tempIntersection2 = new MapIntersection();
    Road tempRoad2 = new Road();
    tempRoad2.setName("2");
    Road tempRoad22 = new Road();
    tempRoad22.setName("C");
    tempIntersection2.setEWroad(tempRoad2);
    tempIntersection2.setNSroad(tempRoad22);
    tempCustomer2.setIntersection(tempIntersection2);
    
    Customer tempCustomer3 = new Customer();
    tempCustomer3.setActive(false);
    tempCustomer3.setAvenueName("3");
    tempCustomer3.setCustomerNumber(3);
    tempCustomer3.setName("Jacob Corp");
    tempCustomer3.setStreetName("G");
    MapIntersection tempIntersection3 = new MapIntersection();
    Road tempRoad3 = new Road();
    tempRoad3.setName("3");
    Road tempRoad33 = new Road();
    tempRoad33.setName("G");
    tempIntersection3.setEWroad(tempRoad3);
    tempIntersection3.setNSroad(tempRoad33);
    tempCustomer3.setIntersection(tempIntersection3);
    
    Customer tempCustomer4 = new Customer();
    tempCustomer4.setActive(true);
    tempCustomer4.setAvenueName("4");
    tempCustomer4.setCustomerNumber(4);
    tempCustomer4.setName("Emily Corp");
    tempCustomer4.setStreetName("D");
    MapIntersection tempIntersection4 = new MapIntersection();
    Road tempRoad4 = new Road();
    tempRoad4.setName("4");
    Road tempRoad44 = new Road();
    tempRoad44.setName("D");
    tempIntersection4.setEWroad(tempRoad4);
    tempIntersection4.setNSroad(tempRoad44);
    tempCustomer4.setIntersection(tempIntersection4);
    
    Customer tempCustomer5 = new Customer();
    tempCustomer5.setActive(true);
    tempCustomer5.setAvenueName("5");
    tempCustomer5.setCustomerNumber(5);
    tempCustomer5.setName("Chance Corp");
    tempCustomer5.setStreetName("B");
    MapIntersection tempIntersection5 = new MapIntersection();
    Road tempRoad5 = new Road();
    tempRoad4.setName("5");
    Road tempRoad55 = new Road();
    tempRoad55.setName("B");
    tempIntersection5.setEWroad(tempRoad5);
    tempIntersection5.setNSroad(tempRoad55);
    tempCustomer5.setIntersection(tempIntersection5);
    
    Customer[] data = new Customer[] {
      tempCustomer1,
      tempCustomer2,
      tempCustomer3,
      tempCustomer4,
      tempCustomer5,
    };
    
    return data;
    
  }
  
}
