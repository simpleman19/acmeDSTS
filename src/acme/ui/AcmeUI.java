package acme.ui;

import acme.pd.Company;

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
    }

    public void setPanel(AcmeJPanelBase panel) {
        this.getContentPane().add(panel);
        panel.buildPanel();
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