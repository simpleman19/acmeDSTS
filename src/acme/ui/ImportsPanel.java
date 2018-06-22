package acme.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.Customer;
import acme.pd.MapIntersection;

public class ImportsPanel extends AcmeBaseJPanel {

    private static final long serialVersionUID = 1L;
    /* Declarations */
    Company company;
    Customer cus;
    JLabel mainLbl = new JLabel("Import");
    String current = System.getProperty("user.dir");
    static final String COURIER = "Courier", CUSTOMER = "Customer";
    JComboBox<String> typeSel = new JComboBox<String>();
    JFileChooser chooser = new JFileChooser();
    JButton selectBtn = new JButton("Select File");
    JButton saveBtn = new JButton("Save");
    Map<UUID, Courier> newCouriers;
    Map<UUID, Customer> newCustomers;

    JTextArea rsltText = new JTextArea();
    JScrollPane rsltScrl = new JScrollPane(rsltText);

    File importFile;

    /* Constructor */
    public ImportsPanel() {
        
    }

    @Override
    public void buildPanel() {

        company = this.getCompany();
        
        initLayout();
        initDefaults();
        initListeners();
        
    }

    /* Listener initialization to handle all buttons */
    private void initListeners() {
        final String SAVE = "save", SELECT = "select", CHANGE = "change";
        selectBtn.setActionCommand(SELECT);
        saveBtn.setActionCommand(SAVE);
        typeSel.setActionCommand(CHANGE);

        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                case SAVE:
                    saveImport(typeSel.getSelectedItem().toString());

                    break;
                case SELECT:
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma Separated Value Files", "csv");
                    chooser.setFileFilter(filter);
                    File workingDirectory = new File(System.getProperty("user.dir") + "/resources/");
                    chooser.setCurrentDirectory(workingDirectory);
                    int returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        addText("You chose to open this file: " + chooser.getSelectedFile().getName());
                        importFile = chooser.getSelectedFile();
                        try {
                            if (typeSel.getSelectedItem().toString().equals(COURIER)) {
                                parseCourier();
                            } else {
                                parseCustomer();
                            }
                        } catch (NumberFormatException f) {
                            f.printStackTrace();
                        }
                    }
                    break;
                default:
                    saveBtn.setVisible(false);
                    break;
                }
            }

        };

        selectBtn.addActionListener(listener);
        saveBtn.addActionListener(listener);
        typeSel.addActionListener(listener);

    }

    /* Setups some of the basics */
    private void initDefaults() {
        saveBtn.setVisible(false);
        typeSel.addItem(COURIER);
        typeSel.addItem(CUSTOMER);
        rsltText.setTabSize(4);
    }

    /* Parse the courier file import and prepare for savings */
    private void parseCourier() {

        // remove all couriers that have been imported but not saved
        newCouriers = new HashMap<UUID, Courier>();

        // open file input stream
        BufferedReader reader = null;

        // read file line by line
        String line = null;
        Scanner scanner = null;
        int index = 0;
        addText("\nCourier Results");
        try {
            reader = new BufferedReader(new FileReader(importFile));
            // skip the first three lines
            for (int i = 0; i < 3; i++) {
                reader.readLine();
            }
            while ((line = reader.readLine()) != null) {
                Courier c1 = new Courier();
                scanner = new Scanner(line);
                scanner.useDelimiter(",");
                saveBtn.setVisible(true);
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index == 0)
                        c1.setCourierNumber(Integer.parseInt(data));
                    else if (index == 1)
                        c1.setName(data);
                    else {
                        addText("invalid data::" + data);
                        saveBtn.setVisible(false);
                    }

                    index++;
                }
                c1.setActive(true);
                addText("ID: " + c1.getCourierNumber() + "\tName: " + c1.getName());

                // check to see if the courier exists
                UUID id = UUID.randomUUID();

                for (Map.Entry<UUID, Courier> courier : company.getCouriers().entrySet()) {
                    if (courier.getValue().getName().equals(c1.getName())) {
                        addText("Courier Already Exists: " + courier.getValue().getName());
                        id = courier.getKey();
                    }
                }
                newCouriers.put(id, c1);
                index = 0;

            }
            // close reader
            reader.close();
        } catch (

        NumberFormatException e) {
            JOptionPane.showMessageDialog(null,  "Bad Input Format");
            saveBtn.setVisible(false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,  "Unable to Read File");
            saveBtn.setVisible(false);
        }

    }

    /* Parse the customer file input and prepare for saving */
    private void parseCustomer() {
        // remove all couriers that have been imported but not saved
        newCustomers = new HashMap<UUID, Customer>();
        MapIntersection[][] map;

        // open file input stream
        BufferedReader reader = null;

        // read file line by line
        String line = null;
        Scanner scanner = null;
        int index = 0;
        addText("\nCustomer Results");
        try {
            reader = new BufferedReader(new FileReader(importFile));
            // skip the first two lines
            for (int i = 0; i < 2; i++) {
                reader.readLine();
            }
            while ((line = reader.readLine()) != null) {
                Customer c2 = new Customer();
                line = line.replaceAll(" Ave and ", ",");
                line = line.replaceAll("th|nd|rd|st|Street", "");
                scanner = new Scanner(line);
                // use any comma that is not inside double quotes
                scanner.useDelimiter("(?!\\B\"[^\"]*),(?![^\"]*\"\\B)");
                saveBtn.setVisible(true);
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index == 0)
                        c2.setCustomerNumber(Integer.parseInt(data));
                    else if (index == 1) {
                        c2.setName(data);
                    } else if (index == 2) {
                        c2.setAvenueName(data.replaceAll("\\s",  ""));
                    } else if (index == 3) {
                        c2.setStreetName(data.replaceAll("\\s",  ""));
                    } else {
                        addText("invalid data::" + data);
                        saveBtn.setVisible(false);
                    }

                    index++;
                }
                
                c2.setActive(true);
                addText("ID: " + c2.getCustomerNumber() + "\tName: " + c2.getName());
                addText("\tIntersection: " + c2.getIntersection(company.getMap()).getIntersectionName()+"\n");

                // check to see if the courier exists
                UUID id = UUID.randomUUID();

                for (Map.Entry<UUID, Customer> customer : company.getCustomers().entrySet()) {
                    if (customer.getValue().getName().equals(c2.getName())) {
                        addText("Customer Already Exists: " + customer.getValue().getName());
                        id = customer.getKey();
                    }
                }
                newCustomers.put(id, c2);
                index = 0;

            }
            // close reader
            reader.close();
        } catch (

        NumberFormatException e) {
            JOptionPane.showMessageDialog(null,  "Bad Input Format");
            saveBtn.setVisible(false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,  "Unable to Read File");
            saveBtn.setVisible(false);
        }

    }

    /*
     * Fulfills the save button functionality and stores data into the correct Map
     */
    private void saveImport(String type) {
        if (type.equals(COURIER)) {
            // check to see if the courier exists and update him
            for (Map.Entry<UUID, Courier> courier : newCouriers.entrySet()) {
                company.getCouriers().put(courier.getKey(), courier.getValue());
                addText("Saving Courier: " + courier.getValue().getName());
            }
        } else if (type.equals(CUSTOMER)) {
            for (Map.Entry<UUID, Customer> customer : newCustomers.entrySet()) {
                company.getCustomers().put(customer.getKey(), customer.getValue());
                addText("Saving Customer: " + customer.getValue().getName());
            }
        } else {
            addText("Unable to save Import");
        }
        company.update();
    }

    /* Handles all of the layout junk */
    private void initLayout() {
        mainLbl.setFont(new Font(mainLbl.getFont().toString(), Font.BOLD, 16));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 62, 168, 28, 81, 57, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 23, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);
        GridBagConstraints gbc_mainLbl = new GridBagConstraints();
        gbc_mainLbl.anchor = GridBagConstraints.WEST;
        gbc_mainLbl.insets = new Insets(25, 15, 10, 10);
        gbc_mainLbl.gridx = 0;
        gbc_mainLbl.gridy = 0;
        add(mainLbl, gbc_mainLbl);
        GridBagConstraints gbc_typeSel = new GridBagConstraints();
        gbc_typeSel.fill = GridBagConstraints.HORIZONTAL;
        gbc_typeSel.insets = new Insets(0, 0, 5, 5);
        gbc_typeSel.gridx = 1;
        gbc_typeSel.gridy = 1;
        add(typeSel, gbc_typeSel);
        GridBagConstraints gbc_selectBtn = new GridBagConstraints();
        gbc_selectBtn.anchor = GridBagConstraints.NORTHWEST;
        gbc_selectBtn.insets = new Insets(0, 0, 5, 5);
        gbc_selectBtn.gridx = 3;
        gbc_selectBtn.gridy = 1;
        add(selectBtn, gbc_selectBtn);
        GridBagConstraints gbc_saveBtn = new GridBagConstraints();
        gbc_saveBtn.insets = new Insets(0, 0, 5, 0);
        gbc_saveBtn.anchor = GridBagConstraints.NORTHWEST;
        gbc_saveBtn.gridx = 4;
        gbc_saveBtn.gridy = 1;
        add(saveBtn, gbc_saveBtn);
        GridBagConstraints gbc_rslt = new GridBagConstraints();
        gbc_rslt.gridwidth = 4;
        gbc_rslt.insets = new Insets(0, 0, 50, 65);
        gbc_rslt.gridx = 1;
        gbc_rslt.gridy = 3;
        gbc_rslt.fill = GridBagConstraints.BOTH;
        add(rsltScrl, gbc_rslt);

    }

    // formats text for the textarea on the display
    private void addText(String message) {
        String old = rsltText.getText();
        rsltText.setText(old + message + "\n");
    }

}
