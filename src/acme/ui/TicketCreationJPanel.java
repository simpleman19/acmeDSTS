package acme.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;

import acme.pd.*;
import com.github.lgooddatepicker.components.TimePickerSettings;

public class TicketCreationJPanel extends AcmeBaseJPanel {

    Company c;
    Ticket ticket;
    // This courier is used to be able to not set courier at creation time
    Courier tbdCourier;

    public TicketCreationJPanel() {
        super();
        tbdCourier = new Courier();
        tbdCourier.setName("TBD");
    }

    @Override
    public void buildPanel() {
        c = this.getCompany();

        if (this.ticket == null) {
            this.ticket = new Ticket(c);
        }

        this.removeAll();

        label1 = new JLabel();
        pickupPanel = new JPanel();
        label2 = new JLabel();
        label3 = new JLabel();
        newCustomer = new JButton();
        label4 = new JLabel();
        pickupTimeLabel = new JLabel();
        billPickUp = new JRadioButton();
        dropOffPanel = new JPanel();
        label5 = new JLabel();
        label6 = new JLabel();
        newCustomer2 = new JButton();
        label7 = new JLabel();
        billDropOff = new JRadioButton();
        quotePanel = new JPanel();
        label8 = new JLabel();
        quoteLabel = new JLabel();
        label9 = new JLabel();
        packageIDLabel = new JLabel();
        courierPanel = new JPanel();
        label12 = new JLabel();
        label13 = new JLabel();
        label14 = new JLabel();
        label15 = new JLabel();
        notesPanel = new JPanel();
        label16 = new JLabel();
        scrollPane1 = new JScrollPane();
        notesTextArea = new JTextArea();
        buttonsPanel = new JPanel();
        cancelButton = new JButton();
        saveButton = new JButton();

        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        label1.setText("Tickets");
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 10f));
        this.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 6, 6), 0, 0));

        createPickupPanel();

        createDropOffPanel();

        createQuotePanel();

        createCourierPanel();

        createNotesPanel();

        buttonsPanel.setLayout(new GridBagLayout());
        ((GridBagLayout) buttonsPanel.getLayout()).rowHeights = new int[] {0, 0};
        ((GridBagLayout) buttonsPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 36));
        cancelButton.addActionListener((e) -> cancelButton());
        buttonsPanel.add(cancelButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 5), 0, 0));

        //---- saveButton ----
        saveButton.setText("Save");
        saveButton.setPreferredSize(new Dimension(100, 36));
        saveButton.addActionListener((e) -> saveButton());
        buttonsPanel.add(saveButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(buttonsPanel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        this.revalidate();
        this.repaint();
    }

    private void createPickupPanel() {
        // Pickup Info Panel
        pickupPanel.setBackground(new Color(204, 204, 204));
        pickupPanel.setForeground(Color.black);
        pickupPanel.setMinimumSize(new Dimension(400, 175));
        pickupPanel.setBorder(LineBorder.createBlackLineBorder());
        pickupPanel.setLayout(new GridBagLayout());
        ((GridBagLayout) pickupPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
        ((GridBagLayout) pickupPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        label2.setText("Pick Up");
        label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 2f));
        pickupPanel.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        label3.setText("Customer");
        pickupPanel.add(label3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        // Combo Box
        HashMap<UUID, Customer> customers = new HashMap<>(c.getCustomers());
        pickupCMB = new JComboBox(customers.values().toArray());
        // If set in ticket then use that customer
        if (ticket.getPickupCustomer() != null) pickupCMB.setSelectedItem(ticket.getPickupCustomer());
        pickupCMB.addActionListener((e) -> updateTicket());
        pickupPanel.add(pickupCMB, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        newCustomer.setText("New Customer");
        newCustomer.addActionListener((e) -> newCustomerAction());
        pickupPanel.add(newCustomer, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        label4.setText("Pickup Time");
        pickupPanel.add(label4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        LocalDateTime pickupTime = this.ticket.getEstimatedPickupTime();
        // This is using a date formatter in company to stay consistant
        pickupTimeLabel.setText(c.acmeDF.format(pickupTime));
        pickupPanel.add(pickupTimeLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        billPickUp.setText("Bill Pick Up");
        billPickUp.setSelected(this.ticket.isBillToSender());
        billPickUp.setBackground(new Color(204, 204, 204));
        billPickUp.addActionListener((e) -> {
            billDropOff.setSelected(!billPickUp.isSelected());
            updateTicket();
        });
        pickupPanel.add(billPickUp, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        this.add(pickupPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 6, 6), 0, 0));
    }

    private void createDropOffPanel() {
        dropOffPanel.setBackground(new Color(204, 204, 204));
        dropOffPanel.setForeground(Color.black);
        dropOffPanel.setMinimumSize(new Dimension(400, 175));
        dropOffPanel.setBorder(LineBorder.createBlackLineBorder());
        dropOffPanel.setLayout(new GridBagLayout());
        ((GridBagLayout) dropOffPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
        ((GridBagLayout) dropOffPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        label5.setText("Drop Off");
        label5.setFont(label5.getFont().deriveFont(label5.getFont().getSize() + 2f));
        dropOffPanel.add(label5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        label6.setText("Customer");
        dropOffPanel.add(label6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        // Combo Box
        HashMap<UUID, Customer> customers = new HashMap<>(c.getCustomers());
        customers.remove(((Customer) pickupCMB.getSelectedItem()).getId());
        deliveryCMB = new JComboBox(customers.values().toArray());
        // If set in ticket then use that customer
        if (ticket.getDeliveryCustomer() != null) deliveryCMB.setSelectedItem(ticket.getDeliveryCustomer());
        deliveryCMB.addActionListener((e) -> updateTicket());

        dropOffPanel.add(deliveryCMB, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        newCustomer2.setText("New Customer");
        newCustomer2.addActionListener((e) -> newCustomerAction());
        dropOffPanel.add(newCustomer2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        label7.setText("Time");
        dropOffPanel.add(label7, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        // Drop off date picker
        DatePickerSettings dateSettings = new DatePickerSettings();
        TimePickerSettings timeSettings = new TimePickerSettings();
        dateSettings.setAllowEmptyDates(false);
        timeSettings.setAllowEmptyTimes(false);
        dropOffPicker = new DateTimePicker(dateSettings, timeSettings);
        dropOffPicker.setDateTimePermissive(this.ticket.getDeliveryTime());
        dropOffPicker.addDateTimeChangeListener((e) -> updateTicket());
        dropOffPanel.add(dropOffPicker, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        billDropOff.setText("Bill Drop Off");
        billDropOff.setSelected(!this.ticket.isBillToSender());
        billDropOff.setBackground(new Color(204, 204, 204));
        billDropOff.addActionListener((e) -> {
            billPickUp.setSelected(!billDropOff.isSelected());
            updateTicket();
        });
        dropOffPanel.add(billDropOff, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        this.add(dropOffPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 6, 0), 0, 0));
    }

    private void createQuotePanel() {
        quotePanel.setLayout(new GridBagLayout());

        label8.setText("Quote");
        quotePanel.add(label8, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        BigDecimal quote = ticket.getQuotedPrice();
        String quoteStr = NumberFormat.getCurrencyInstance().format(quote);
        quoteLabel.setText(quoteStr);
        quoteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        quotePanel.add(quoteLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        label9.setText("Package ID: ");
        quotePanel.add(label9, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        packageIDLabel.setText(ticket.getPackageID());
        packageIDLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        quotePanel.add(packageIDLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        this.add(quotePanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 6, 0), 0, 0));
    }

    private void createCourierPanel() {
        // Courier Panel
        courierPanel.setBackground(new Color(204, 204, 204));
        courierPanel.setBorder(LineBorder.createBlackLineBorder());
        courierPanel.setLayout(new GridBagLayout());

        label12.setText("Courier");
        label12.setFont(label12.getFont().deriveFont(label12.getFont().getSize() + 2f));
        courierPanel.add(label12, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        label13.setText("Departure Time");
        courierPanel.add(label13, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        LocalDateTime departureTime = this.ticket.getEstimatedDepartureTime();
        label14.setText(c.acmeDF.format(departureTime));
        courierPanel.add(label14, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        label15.setText("Courier");
        courierPanel.add(label15, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        // Combo Box
        courierCMB = new JComboBox(c.getCouriers().values().toArray());
        courierCMB.addItem(tbdCourier);
        if (ticket.getCourier() == null){
            courierCMB.setSelectedItem(tbdCourier);
        } else {
            courierCMB.setSelectedItem(ticket.getCourier());
        }
        courierCMB.addActionListener((e) -> updateTicket());
        courierPanel.add(courierCMB, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        this.add(courierPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 6, 6), 0, 0));
    }

    private void createNotesPanel() {
        // Notes Panel
        notesPanel.setLayout(new GridBagLayout());

        label16.setText("Notes");
        label16.setVerticalAlignment(SwingConstants.TOP);
        notesPanel.add(label16, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        // Scroll Pane
        {
            //---- notesTextArea ----
            if (ticket.getNote().equals("")) {
                notesTextArea.setText("This is an area for notes");
            } else {
                notesTextArea.setText(ticket.getNote());
            }

            notesTextArea.setBackground(Color.white);
            notesTextArea.setFont(notesTextArea.getFont().deriveFont(notesTextArea.getFont().getSize() - 2f));
            notesTextArea.setMinimumSize(new Dimension(171, 120));
            notesTextArea.setPreferredSize(new Dimension(180, 120));
            notesTextArea.setLineWrap(true);
            notesTextArea.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                    ticket.setNote(notesTextArea.getText());
                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {

                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
            scrollPane1.setViewportView(notesTextArea);
        }
        notesPanel.add(scrollPane1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(notesPanel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 6, 0), 0, 0));


    }

    private void saveButton() {
        this.updateTicket();
        // TODO null pointer somewhere??
        //this.ticket.create();
        this.getAcmeUI().courierList();
    }

    private void cancelButton() {
        this.getAcmeUI().courierList();
    }

    private void newCustomerAction() {
        this.getAcmeUI().customerAddUpdate(null);
    }

    private void updateTicket() {
        // Update bill to
        this.ticket.setBillToSender(this.billPickUp.isSelected());

        // Update pickup customer
        this.ticket.setPickupCustomer((Customer) this.pickupCMB.getSelectedItem());

        // Update Drop Off Customer
        this.ticket.setDeliveryCustomer((Customer) this.deliveryCMB.getSelectedItem());

        // Only set courier if courier has been set
        if (courierCMB.getSelectedItem() != tbdCourier) {
            this.ticket.setCourier((Courier) courierCMB.getSelectedItem());
        }

        // Drop off date time
        this.ticket.setDeliveryTime(dropOffPicker.getDateTimeStrict());

        // Save Note Text
        this.ticket.setNote(notesTextArea.getText());

        this.buildPanel();
    }

    private JLabel label1;
    private JPanel pickupPanel;
    private JLabel label2;
    private JLabel label3;
    private JComboBox pickupCMB;
    private JButton newCustomer;
    private JLabel label4;
    private JLabel pickupTimeLabel;
    private JRadioButton billPickUp;
    private JPanel dropOffPanel;
    private JLabel label5;
    private JLabel label6;
    private JComboBox deliveryCMB;
    private JButton newCustomer2;
    private JLabel label7;
    private DateTimePicker dropOffPicker;
    private JRadioButton billDropOff;
    private JPanel quotePanel;
    private JLabel label8;
    private JLabel quoteLabel;
    private JLabel label9;
    private JLabel packageIDLabel;
    private JPanel courierPanel;
    private JLabel label12;
    private JLabel label13;
    private JLabel label14;
    private JLabel label15;
    private JComboBox courierCMB;
    private JPanel notesPanel;
    private JLabel label16;
    private JScrollPane scrollPane1;
    private JTextArea notesTextArea;
    private JPanel buttonsPanel;
    private JButton cancelButton;
    private JButton saveButton;


    public static void main(String [] args) {
        AcmeUI acme = new AcmeUI();
        acme.getCompany().setCurrentUser(new User());
        acme.setPanel(new TicketCreationJPanel());
    }
}
