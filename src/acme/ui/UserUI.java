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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UserUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JPanel panelForm;
    private JPanel buttonPanel;
    private JButton LoginButton;
    private GridBagConstraints grid;
    private JTextField txt;
    private JPasswordField txt2;

    public UserUI() {

        super("Acme Ticketing System");

        buildForm(); 

        buildButtonPanel();

        setVisible(true);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildForm();

        buildButtonPanel();
    }

    public void buildForm() {
        panel = new JPanel();
        getContentPane().add(panel);

        panelForm = new JPanel(new GridBagLayout());
        panel.add(panelForm);

        grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;

        grid.anchor = GridBagConstraints.LINE_END;

        panelForm.add(new JLabel("Username "), grid);
        ++grid.gridy;

        panelForm.add(new JLabel("Password "), grid);
        ++grid.gridy;

        grid.gridx = 1;
        grid.gridy = 0;
        grid.anchor = GridBagConstraints.LINE_START;

        txt = new JTextField(10);
        // field1.add(lb1);
        panelForm.add(txt, grid);
        ++grid.gridy;

        txt2 = new JPasswordField(10);
        // field2.add(lb2);
        panelForm.add(txt2, grid);
        ++grid.gridy;

    }

    private void buildButtonPanel() {

        buttonPanel = new JPanel();
        Color lightBlue = new Color(93, 184, 202);
        buttonPanel.setBackground(lightBlue);

        LoginButton = new JButton("Log in");

        buttonPanel.add(LoginButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Register the action listeners

        LoginButtonListner save = new LoginButtonListner();
        LoginButton.addActionListener(save);

    }

    private class LoginButtonListner implements ActionListener {
   	
//    		private boolean checkIfUserExists(String username) {
//    			// TODO check database
//    	    return true;
//    		}
    		
    		private boolean loginUser(String username) {
    			// TODO check database
    			this.getAcmeUI().loginUser();
    			
    		}
    		



		@Override
        public void actionPerformed(ActionEvent e) {
            // IMPLEMENT
            String pass = new String (txt2.getPassword());

            if ((txt.getText().equalsIgnoreCase("admin") && pass.equalsIgnoreCase("admin")) ||
                 (txt.getText().equalsIgnoreCase("owner") && pass.equalsIgnoreCase("owner")) ||
                 (txt.getText().equalsIgnoreCase("courier") && pass.equalsIgnoreCase("courier"))) {

                System.out.println("true");

            }

            else {
                System.out.println("NOT ALLOWED");
                txt.setText("");
                txt2.setText("");
            }

        }
    }

}
