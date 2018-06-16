package acme.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import com.github.lgooddatepicker.components.DateTimePicker;
import acme.pd.Courier;
import acme.pd.Ticket;
import acme.pd.User;

public class CompleteATicketUI extends AcmeBaseJPanel {
  Ticket ticketToComplete;
  DateTimePicker dateTimePickerDelivery;
  DateTimePicker dateTimePickerDeparture;
  DateTimePicker dateTimePickerPickup;
  

  public CompleteATicketUI(Ticket ticket) {
    super();
    this.ticketToComplete = ticket;
  }

  public void buildPanel() {
    // Initialize your panel here
    this.removeAll();
    this.setLayout(null);
    
    //Keeping with the color scheme
    Color lightBlue = new Color(93, 184, 202);
  
    //title label
    JLabel lblCompleteTicket = new JLabel("Complete Ticket");
    lblCompleteTicket.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
    lblCompleteTicket.setBounds(20, 20, 200, 27);
    add(lblCompleteTicket);
    
    //information section-------------------------------------------
    JLabel lblPackageId = new JLabel("Package ID");
    lblPackageId.setBounds(30, 59, 83, 16);
    add(lblPackageId);
    
    JLabel lblCourier = new JLabel("Courier");
    lblCourier.setBounds(30, 87, 83, 16);
    add(lblCourier);
    
    JLabel lblActPackageId = new JLabel(this.ticketToComplete.getId() + "");
    lblActPackageId.setBounds(125, 59, 150, 16);
    add(lblActPackageId);
    
    JLabel lblActCourier = new JLabel(this.ticketToComplete.getCourier().getName() + "");
    lblActCourier.setBounds(125, 87, 150, 16);
    add(lblActCourier);
    //--------------------------------------------------------------
    
    
    //times section-------------------------------------------------
    JLabel lblActualDepartureTime = new JLabel("Actual Departure time");
    lblActualDepartureTime.setBounds(30, 155, 145, 16);
    add(lblActualDepartureTime);
    
    this.dateTimePickerDeparture = new DateTimePicker();
    this.dateTimePickerDeparture.setBounds(200, 155, 400, 30);
    add(this.dateTimePickerDeparture);
    
    JLabel lblActualPickUpTime = new JLabel("Actual Pick Up time");
    lblActualPickUpTime.setBounds(30, 205, 135, 16);
    add(lblActualPickUpTime);
    
    this.dateTimePickerPickup = new DateTimePicker();
    this.dateTimePickerPickup.setBounds(200, 205, 400, 30);
    add(this.dateTimePickerPickup);
    
    JLabel lblActualDeliveryTime = new JLabel("Actual Delivery time");
    lblActualDeliveryTime.setBounds(30, 255, 145, 16);
    add(lblActualDeliveryTime);
    
    this.dateTimePickerDelivery = new DateTimePicker();
    this.dateTimePickerDelivery.setBounds(200, 255, 400, 30);
    add(this.dateTimePickerDelivery);
    //-------------------------------------------------------------
    
    //cancel and save buttons 
    JButton btnCancel = new JButton("Cancel");
    btnCancel.setBounds(364, 305, 117, 29);
    btnCancel.setBackground(lightBlue);
    btnCancel.setOpaque(true);
    btnCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clickedCancel();
      }
    });
    add(btnCancel);
    
    JButton btnSave = new JButton("Save");
    btnSave.setBounds(480, 305, 117, 29);
    btnSave.setBackground(lightBlue);
    btnSave.setOpaque(true);
    btnSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clickedSave();
      }
    });
    add(btnSave);
   
    this.revalidate();
    this.repaint();
  }
  
  //this method checks to make sure the times make sense in the basic sense of things and then 
  //completes the ticket
  public void clickedSave()
  {
    if(this.dateTimePickerDeparture.getDateTimeStrict() != null && this.dateTimePickerPickup.getDateTimeStrict() != null
        && this.dateTimePickerDelivery.getDateTimeStrict() != null)
    {
      if(this.dateTimePickerPickup.getDateTimeStrict().isBefore(this.dateTimePickerDeparture.getDateTimeStrict()) &&
          this.dateTimePickerDelivery.getDateTimeStrict().isBefore(this.dateTimePickerPickup.getDateTimeStrict()))
      {
        this.ticketToComplete.setDepartureTime(this.dateTimePickerDeparture.getDateTimeStrict());
        this.ticketToComplete.setPickupTime(this.dateTimePickerPickup.getDateTimeStrict());
        this.ticketToComplete.setDeliveryTime(this.dateTimePickerDelivery.getDateTimeStrict());
        
        //TO DO
        //go to TicketList Page
      }
      else
      {
        JOptionPane.showMessageDialog(null, "You must enter valid times.","Error",
            JOptionPane.ERROR_MESSAGE);
      }
      
    }
    else
    {
      JOptionPane.showMessageDialog(null, "You must enter valid times.","Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
  
  //This is just to go back
  public void clickedCancel()
  {
    //TO DO
    //go to TicketList Page
  }
  
  public static void main(String [] args) {
    AcmeUI acme = new AcmeUI();
    Ticket testTicket = new Ticket();
    Courier testCourier = new Courier();
    testCourier.setName("Dee Dee");
    testCourier.setCourierNumber(5);
    testTicket.setCourier(testCourier);
    testTicket.getId();
    acme.setPanel(new CompleteATicketUI(testTicket));
    acme.getCompany().setCurrentUser(new User());
  }
}
