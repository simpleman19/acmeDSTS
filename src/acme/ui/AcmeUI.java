package acme.ui;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collections;
import java.util.function.Consumer;

import javax.persistence.NoResultException;
import javax.swing.*;

import acme.data.HibernateAdapter;
import acme.data.PersistableEntity;
import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.Customer;
import acme.pd.Ticket;
import acme.pd.User;
import acme.seed.SeedDatabase;
import org.postgresql.util.PSQLException;

public class AcmeUI extends JFrame {

    private Company company;
	protected AcmeBaseJPanel storedPanel;

    public AcmeUI() {
        super("Acme Delivery Software");
        HibernateAdapter.startUp();
        
        addWindowListener(new ShutdownListener());

        this.company = Company.loadCompanyFromDB();
        if (this.company == null) {
            System.out.println("Could Not Load Company from Database");
            Object[] options = {"Yes, create a company",
                    "No, Shut down the app"};
            int n = JOptionPane.showOptionDialog(this,
                    "Would you like me to setup the database?",
                    "Database Error",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[0]); //default button title
            if (n == JOptionPane.YES_OPTION) {
                SeedDatabase.seedDB();
                this.company = Company.loadCompanyFromDB();
            } else {
                System.out.println("Shutting down application due to no company");
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                return;
            }
        }

        this.buildMenu();

        setVisible(true);
        setSize(550, 550);
        setLocationRelativeTo(null);


        loginScreen();
    }

    private void buildMenu() {
        this.setJMenuBar(new JMenuBar());
        if (company.getCurrentUser() != null) {
            boolean admin = company.getCurrentUser().isAdmin();
            JMenuBar menuBar = new JMenuBar();

            // Ticket Button
            JMenuItem ticketList = new JMenuItem("Tickets");
            ticketList.addActionListener((event) -> ticketList());
            menuBar.add(ticketList);

            // Map Button
            JMenuItem map = new JMenuItem("Map");
            map.setPreferredSize(new Dimension(50, map.getPreferredSize().height));
            map.addActionListener((event) -> mapView());
            menuBar.add(map);

            // Maintenance Menu
            JMenu maintenanceMenu = new JMenu("Maintenance");
            maintenanceMenu.setPreferredSize(new Dimension(110, maintenanceMenu.getPreferredSize().height));
            menuBar.add(maintenanceMenu);

            JMenuItem customerItem = new JMenuItem("Customers");
            customerItem.addActionListener((event) -> customerList());
            maintenanceMenu.add(customerItem);

            JMenuItem courierItem = new JMenuItem("Couriers");
            courierItem.addActionListener((event) -> courierList());
            maintenanceMenu.add(courierItem);

            JMenuItem importItem = new JMenuItem("Import");
            importItem.addActionListener((event) -> importIntoCompany());
            maintenanceMenu.add(importItem);

            // Admin only
            if (admin) {
                JMenuItem clerkItem = new JMenuItem("Clerks");
                // TODO should this be clerks or user???
                clerkItem.addActionListener((event) -> userList());
                maintenanceMenu.add(clerkItem);

                JMenuItem companyItem = new JMenuItem("Company");
                companyItem.addActionListener((event) -> companyEdit());
                maintenanceMenu.add(companyItem);
            }

            // Reports Button
            if (admin) {
                JMenuItem report = new JMenuItem("Reports");
                report.setPreferredSize(new Dimension(50, report.getPreferredSize().height));
                report.addActionListener((event) -> reports());
                menuBar.add(report);
            }

            // Logout Button
            menuBar.add(Box.createHorizontalGlue());
            JMenuItem logout = new JMenuItem("Logout");
            logout.setPreferredSize(new Dimension(50, logout.getPreferredSize().height));
            logout.setHorizontalAlignment(SwingConstants.RIGHT);
            logout.addActionListener((event) -> logoutUser());
            menuBar.add(logout);

            this.setJMenuBar(menuBar);
        }
    }

    public void setPanel(AcmeBaseJPanel panel) {
        this.getContentPane().removeAll();
        this.buildMenu();
        this.getContentPane().add(panel);
        panel.buildPanel();
        this.revalidate();
        this.repaint();
    }
    
    public void setStoredPanel(AcmeBaseJPanel panel) {
    	this.storedPanel = panel;
    }
    
    public AcmeBaseJPanel getStoredPanel() {
    	return this.storedPanel;
    }

    public void logoutUser() {
        company.setCurrentUser(null);
        this.loginScreen();
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void loginScreen() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void ticketList() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void ticketCreate() {
        // This will be called with null to create a ticket
        TicketCreationJPanel jp = new TicketCreationJPanel();
        this.setPanel(jp);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void ticketComplete(Ticket ticket) {
        CompleteATicketUI ticketCompleteUI = new CompleteATicketUI(ticket);
        this.setPanel(ticketCompleteUI);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void customerList() {
        CustomerListUI customerListUI = new CustomerListUI();
        this.setPanel(customerListUI);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void customerAddUpdate(Customer customer, Consumer<Customer> onSaveFunc) {
        // This will be called with null to create
        CustomerCreatePanel panel = new CustomerCreatePanel(onSaveFunc);
        this.setPanel(panel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void userList() {
        ClerksListPanel clp = new ClerksListPanel();
        this.setPanel(clp);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void userAddUpdate(User user) {
        // This will be called with null to create
        ClerksUpdatePanel cp = new ClerksUpdatePanel(user);
        this.setPanel(cp);
    }

      // Display the list of customers
    public void courierList() {
        CourierListPanel clp = new CourierListPanel();
        this.setPanel(clp);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void courierAddUpdate(Courier courier) {
        // This will be called with null to create
    	CourierCreatePanel panel = new CourierCreatePanel();
        this.setPanel(panel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void importIntoCompany() {
        ImportsPanel ip = new ImportsPanel();
        this.setPanel(ip);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void companyEdit() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }
  
    public void mapView() {
        MapUI mapUI = new MapUI();
        this.setPanel(mapUI);
    }

    // Generate Reports Panel
    public void reports() {
        ReportsPanel rp = new ReportsPanel();
        this.setPanel(rp);
    }

    public Company getCompany() {
        return company;
    }
    class ShutdownListener implements WindowListener {
        public void windowClosing(WindowEvent event) {
        HibernateAdapter.shutDown();
        setVisible(false);
        company.exportMap();
            dispose(); //Destroy the JFrame object
        }
        public void windowOpened(WindowEvent event) {}
        public void windowClosed(WindowEvent event) {}
        public void windowIconified(WindowEvent event) {}
        public void windowDeiconified(WindowEvent event) {}
        public void windowActivated(WindowEvent event) {}
        public void windowDeactivated(WindowEvent event) {}
}}

