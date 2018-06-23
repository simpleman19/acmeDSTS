package acme.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import acme.pd.User;

public class UserUI extends AcmeBaseJPanel {

    private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JPanel panelForm;
    private JPanel buttonPanel;
    private JButton LoginButton;
    private GridBagConstraints grid;
    private JTextField txt;
    private JPasswordField txt2;

    public UserUI() {

        super();

    }

    public void buildForm() {
        
        try {
            BufferedImage myPicture = ImageIO.read(new File("resources/AcmeLogo.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            add(picLabel, new GridBagConstraints());
        } catch (IOException e) {
            System.out.println("Couldn't find company logo");
        }
        
        panel = new JPanel();
        this.add(panel);

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
    
    
    public void buildPanel() {
    	buildForm();
    	buildButtonPanel();
    }

    
    
     
    private class LoginButtonListner implements ActionListener {
    	    
		@Override
        public void actionPerformed(ActionEvent e) {
            // IMPLEMENT
            String pass = new String (txt2.getPassword());
            String username = new String(txt.getText());
            loginUser(username, pass);
		}
    }
		
		private void loginUser(String username, String password) {
            User user = User.authenticate(username, password);
            if (user != null) {
                this.getCompany().setCurrentUser(user);
                this.getAcmeUI().ticketList(false);
            }
        }


		public static void main(String [] args) {
			AcmeUI acme = new AcmeUI();
			acme.getCompany().setCurrentUser(new User());
			acme.setPanel(new UserUI());
		}

	}
