package acme.ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TicketUI extends JFrame {

	private static final long serialVersionUID = 1L;
	

	public TicketUI() {

		// create JFrame and JTable
        super("Acme Ticketing System");
        JTable table = new JTable(); 
        JCheckBox boxComp = new JCheckBox ("Show Completed");
        
        // create a table model and set a Column Identifiers to this model 
        Object[] columns = {"Package ID", "Courier", "Departure Time", "Return Time", "Cancel", "Complete",
    			"Print"};
        
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        
        // set the model to the table
        table.setModel(model);
        
        // Change A JTable Background Color, Font Size, Font Color, Row Height
        table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.black);
        Font font = new Font("",1,22);
        table.setFont(font);
        table.setRowHeight(30);
        
        // create JButton
        JButton btnAdd = new JButton("New Ticket");
        
        btnAdd.setBounds(10, 320, 100, 30);
        boxComp.setBounds(255, 85, 250, 25);
  
        // create JScrollPane
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(10, 120, 880, 200);
        
        setLayout(null);
        
        add(pane);
        
        add(btnAdd);
        add(boxComp);        
        
        // create an array of objects to set the row data
        Object[] row = new Object[7];
        
        
        // button add row
        btnAdd.addActionListener(new ActionListener(){
        	
        	
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	// cancel button; I don't know how to add it in the table
            	JButton exitButton = new JButton ("X");
            	exitButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
						
					}          		
            	});
            	///////////// end of cancel button//////////////////////////
            	
            	
                row[0] = "col 1";
                row[1] = "combo"; //I don't know how to add it in the table
                row[2] = "col 3";
                row[3] = "col 4";
                row[4] = exitButton; //// cancel button position
                row[5] = "checkbox"; //I don't know how to add it in the table
                row[6] = "col 7";
                
                
                // add row to the model
                model.addRow(row);
             
            }
        });
        
        setSize(900,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
    }

	}
