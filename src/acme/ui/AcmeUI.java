package acme.ui;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collections;

import javax.persistence.NoResultException;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import acme.data.HibernateAdapter;
import acme.data.PersistableEntity;
import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.Customer;
import acme.pd.Ticket;
import acme.pd.User;

public class AcmeUI extends JFrame {

    private Company company;

    public AcmeUI() {
        super("Acme Delivery Software");
        HibernateAdapter.startUp();
        
        try {
        	this.company = PersistableEntity.querySingle(Company.class, "select c from COMPANY c", Collections.EMPTY_MAP);
        } catch(NoResultException e) {
        	this.company = Company.getDefaultAcme();
        }

        this.company = Company.getDefaultAcme();
        this.buildMenu();

        setVisible(true);
        setSize(550, 550);
        setLocationRelativeTo(null);
        addWindowListener(new ShutdownListener());

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
    public void customerAddUpdate(Customer customer) {
        // This will be called with null to create
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
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
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
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
            dispose(); //Destroy the JFrame object
        }
        public void windowOpened(WindowEvent event) {}
        public void windowClosed(WindowEvent event) {
        	HibernateAdapter.shutDown();
        }
        public void windowIconified(WindowEvent event) {}
        public void windowDeiconified(WindowEvent event) {}
        public void windowActivated(WindowEvent event) {}
        public void windowDeactivated(WindowEvent event) {}
}}

