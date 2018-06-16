package acme.ui;

import acme.pd.*;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AcmeUI extends JFrame {

    private Company company;

    public AcmeUI() {
        super("Acme Delivery Software");

        this.company = new Company();
        this.buildMenu();

        setVisible(true);
        setSize(550, 550);
        setLocationRelativeTo(null);
        addWindowListener(new ShutdownListener());

        loginScreen();
    }

    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu ticketMenu = new JMenu("Tickets");
        menuBar.add(ticketMenu);

        // TODO actually implement.  This is temporary to get a base for everyone
        ticketMenu.add(new JMenuItem("Create"));
        ticketMenu.add(new JMenuItem("List"));

        this.setJMenuBar(menuBar);
    }

    public void setPanel(AcmeBaseJPanel panel) {
        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        panel.buildPanel();
        this.revalidate();
        this.repaint();
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
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void ticketComplete(Ticket ticket) {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void importCustomers() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void customerList() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void customerAddUpdate(Customer customer) {
        // This will be called with null to create
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // List of clerks in the system that can be added to or updated
    public void userList() {
        ClerksListPanel cp = new ClerksListPanel();
        this.setPanel(cp);
    }

    // Update or add clerks into the system. Accessed by the userList page
    public void userAddUpdate(User user) {
        // This will be called with null to create
        ClerksUpdatePanel cu = new ClerksUpdatePanel(user);
        this.setPanel(cu);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void importCouriers() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void courierList() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void courierAddUpdate(Courier courier) {
        // This will be called with null to create
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void mapView() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Generate Reports Panel
    public void reports() {
        ReportsPanel rp = new ReportsPanel();
        this.setPanel(rp);
    }

    public Company getCompany() {
        return company;
    }
}

class ShutdownListener implements WindowListener {
    public void windowClosing(WindowEvent event) {

    }
    public void windowOpened(WindowEvent event) {}
    public void windowClosed(WindowEvent event) {}
    public void windowIconified(WindowEvent event) {}
    public void windowDeiconified(WindowEvent event) {}
    public void windowActivated(WindowEvent event) {}
    public void windowDeactivated(WindowEvent event) {}
}