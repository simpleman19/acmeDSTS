package acme.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import acme.pd.Company;
import acme.pd.User;
public class CompanyUI extends AcmeBaseJPanel {

private static final long serialVersionUID = 1L;
private JPanel panel;
private JPanel panelForm;
private JPanel buttonPanel;
private JButton saveButton;
private JButton exitButton;
private GridBagConstraints grid;

private JTextField company_name;
private JTextField bonus_amount;
private JTextField courier_miles;
private JTextField blocks_per_mile;
private JTextField lateness_margin;
private JTextField flat_billing;
private JTextField block_rate_name;
private Company comp;

//////////////////////////
	public CompanyUI()
    {
        super();
    }


	public void buildPanel() {
    	buildForm();
    	buildButtonPanel();
    }

	
    public void buildForm () 
    {
        panel = new JPanel();
        this.add(panel);

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

        company_name = new JTextField (10);
        panelForm.add(company_name, grid);
        ++grid.gridy;
        
        company_name.setText(getCompany().getName());
        
        
        bonus_amount = new JTextField (10);
        panelForm.add(bonus_amount, grid);
        ++grid.gridy;
        
        bonus_amount.setText(getCompany().getBonus().toString());
        

        courier_miles = new JTextField (10);
        panelForm.add(courier_miles, grid);
        ++grid.gridy;
        
        courier_miles.setText(Double.toString(getCompany().getCourierMilesPerHour()));
        
        
        blocks_per_mile = new JTextField (10);
        panelForm.add(blocks_per_mile, grid);
        ++grid.gridy;
        blocks_per_mile.setText(Double.toString(getCompany().getBlocksPerMile()));
        
        
        lateness_margin = new JTextField (10);
        panelForm.add(lateness_margin, grid);
        ++grid.gridy;
        lateness_margin.setText(Integer.toString(getCompany().getLatenessMarginMinutes()));

        flat_billing = new JTextField (10);
        panelForm.add(flat_billing, grid);
        ++grid.gridy;
        flat_billing.setText(getCompany().getFlatBillingRate().toString());
        
        block_rate_name = new JTextField (10);
        panelForm.add(block_rate_name, grid);
        ++grid.gridy;
        block_rate_name.setText(Double.toString(getCompany().getBlocksPerMile()));
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
            getCompany().update();
            getAcmeUI().ticketList();
        }
    }


       public static void main(String [] args) {
    	   AcmeUI acme = new AcmeUI();
    	   acme.getCompany().setCurrentUser(new User());
    	   acme.setPanel(new CompanyUI());
	}
       }
