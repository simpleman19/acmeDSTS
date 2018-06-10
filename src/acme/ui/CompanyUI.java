package acme.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CompanyUI extends JFrame {
////////////GUI/////////////////
private static final long serialVersionUID = 1L;
private JPanel panel;
private JPanel panelForm;
private JPanel buttonPanel;
private JButton saveButton;
private JButton exitButton;
private GridBagConstraints grid;
private JTextField field1;
private JTextField field2;
private JTextField field3;
private JTextField field4;
private JTextField field5;
private JTextField field6;
private JTextField field7;
//////////////////////////
	public CompanyUI()
    {

        super("Acme Ticketing System");

        buildForm();

        buildButtonPanel();

        setVisible(true);
        setSize(550, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
//        // TODO initialize company
//        File file = new File("null");
//        this.map = new Map(file);
//
//        couriers = new HashMap<UUID, Courier>();
//        tickets = new HashMap<UUID, Ticket>();
//        customer = new HashMap<UUID, Customer>();
//        currentUser = new User();
    }
	
	
    public void buildForm () 
    {
        panel = new JPanel();
        getContentPane().add(panel);

        panelForm = new JPanel(new GridBagLayout());
        panel.add(panelForm);

        grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.anchor = GridBagConstraints.LINE_END;

        // JLabel label1 = new JLabel("Company Name");
        // label1.setLayout(grid);
        panelForm.add(new JLabel("Company Name "), grid);
        ++grid.gridy;

        panelForm.add(new JLabel("Bonus Amount "), grid);
        ++grid.gridy;

        panelForm.add(new JLabel("Courier Miles/Hour "), grid);
        ++grid.gridy;

        panelForm.add(new JLabel("Blocks Per Mile "), grid);
        ++grid.gridy;

        panelForm.add(new JLabel("Lateness Margin (Minutes) "), grid);
        ++grid.gridy;

        panelForm.add(new JLabel("Flat Billing Rate "), grid);
        ++grid.gridy;

        panelForm.add(new JLabel("$/Block Rate Name "), grid);
        ++grid.gridy;

        grid.gridx = 1;
        grid.gridy = 0;
        grid.anchor = GridBagConstraints.LINE_START;

        field1 = new JTextField (10);
        panelForm.add(field1, grid);
        ++grid.gridy;

        field2 = new JTextField (10);
        panelForm.add(field2, grid);
        ++grid.gridy;

        field3 = new JTextField (10);
        panelForm.add(field3, grid);
        ++grid.gridy;

        field4 = new JTextField (10);
        panelForm.add(field4, grid);
        ++grid.gridy;

        field5 = new JTextField (10);
        panelForm.add(field5, grid);
        ++grid.gridy;

        field6 = new JTextField (10);
        panelForm.add(field6, grid);
        ++grid.gridy;
        
        field7 = new JTextField (10);
        panelForm.add(field7, grid);
        ++grid.gridy;
    }

    private void buildButtonPanel()
    {

        buttonPanel = new JPanel();
        Color lightBlue = new Color(93, 184, 202);
        buttonPanel.setBackground(lightBlue);

        saveButton = new JButton("Save");
        exitButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Register the action listeners
        ExitButtonListner exit = new ExitButtonListner();
        exitButton.addActionListener(exit);

        SaveButtonListner save = new SaveButtonListner();
        saveButton.addActionListener(save);

    }

    private class ExitButtonListner implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);

        }

    }

    private class SaveButtonListner implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            // IMPLEMENT
            
            
//            System.out.println(field1.getText());
//            Integer.parseInt(field2.getText());
            
            
        }
    }
}
