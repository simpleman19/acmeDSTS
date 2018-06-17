package acme.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import acme.pd.Direction;
import acme.pd.Map;
import acme.pd.MapIntersection;
import acme.pd.User;

public class MapUI extends AcmeBaseJPanel {
  int name;

  
  public MapUI() {
    super();
    this.name = 0;
   
  }
  
  public void buildPanel() {
   
    //get the map  from company
    Map map = getCompany().getMap();
    
    
    this.removeAll();
    this.setLayout(null);
    
    //title label
    JLabel lblMap = new JLabel("Map");
    lblMap.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
    lblMap.setBounds(27, 34, 71, 27);
    add(lblMap);
    
    //these will hold the buttons (intersections) and labels for the map
    int size = map.getMap().length;
    JButton mapButtons[][] = new JButton[size][size];
    JLabel mapLablesNS[] = new JLabel[size];
    JLabel mapLablesEW[] = new JLabel[size];
    
    //used for positioning of the buttons and labels 
    int yPos = 100;
    int xPos = 35;
  
    
    //these are unicodes to make the arrows for the labels
    //to show which direction the road is 
    char downArrow = '\u2193';
    char upArrow = '\u2191';
    char leftArrow = '\u2190';
    char rightArrow = '\u2192';
    char biDirLRArrow = '\u2194';
    char biDirUDArrow = '\u2195';
    
    //this draws the labels for the east and west roads
    for(int x = 0; x < size; x++)
    {
      //figures out which arrow to use
      if((map.getMap()[x][0].getEWroad().isBidirectional()))
      {
        mapLablesEW[x]= new JLabel(map.getMap()[x][0].getEWroad().getName() + " " + biDirLRArrow); 
      }
      else if(map.getMap()[x][0].getEWroad().getDirection() == Direction.EAST)
      {
        mapLablesEW[x]= new JLabel(map.getMap()[x][0].getEWroad().getName() + " " + rightArrow); 
      }
      else
      {
        mapLablesEW[x]= new JLabel(map.getMap()[x][0].getEWroad().getName() + " " + leftArrow); 
      }
      
      mapLablesEW[x].setFont(new Font("Lucida Grande", Font.PLAIN, 16));
      mapLablesEW[x].setBounds(30, yPos, 40, 20);
      mapLablesEW[x].setHorizontalAlignment(SwingConstants.LEFT);
      add(mapLablesEW[x]);
      
      //keep track of positioning
      yPos=yPos+55;
    }
    
    //reset the y position for the rest of the map
    yPos=35;
   
    
    //this draws the buttons (intersections) and labels for the north and sout roads
    for(int x = 0; x < size; x++)
    {
      //configure positioning
      xPos=xPos+55;
      yPos = 35;
      
      //this is the drawing for the labels for the north and south road
      //it was convenient to just do it in this loop
      if(map.getMap()[0][x].getNSroad().isBidirectional())          //this is figuring out which arrow to use
      {
        mapLablesNS[x]= new JLabel("<html>" + map.getMap()[0][x].getNSroad().getName() + "<br>" + biDirUDArrow + "</html>");
      }
      else if(map.getMap()[0][x].getNSroad().getDirection() == Direction.SOUTH)
      {
        mapLablesNS[x]= new JLabel("<html>" + map.getMap()[0][x].getNSroad().getName() + "<br>" + downArrow + "</html>");
      }
      else
      {
        mapLablesNS[x]= new JLabel("<html>" + map.getMap()[0][x].getNSroad().getName() + "<br>" + upArrow + "</html>");
      }
      
      mapLablesNS[x].setFont(new Font("Lucida Grande", Font.PLAIN, 16));
      mapLablesNS[x].setBounds(xPos, yPos-10, 50, 50);
      mapLablesNS[x].setHorizontalAlignment(SwingConstants.CENTER);
      add(mapLablesNS[x]);
      

      
      for(int y = 0; y < size; y++)
      {
        yPos = yPos+55;
        
        //make an intersection 
        //you'll notice I flipped the x and the y here for the map.getMap()
        //this is drawing the map one col at a time
        mapButtons[x][y] = new JButton(map.getMap()[y][x].getIntersectionName());
        mapButtons[x][y].setBounds(xPos, yPos, 50, 50);
        
        //color code the intersections 
        if(map.getMap()[y][x].isClosed(LocalDate.now()))
        {
          mapButtons[x][y].setBackground(Color.RED);
          mapButtons[x][y].setOpaque(true);
        }
       
        if(map.getMap()[y][x].getIntersectionName().equals(map.getHomeBase().getIntersectionName()))
        {
          mapButtons[x][y].setBackground(Color.YELLOW);
          mapButtons[x][y].setOpaque(true);
        }
        
        //handle clicking of intersection 
        mapButtons[x][y].addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            intersectionClicked(((JButton) e.getSource()).getText());
          }
        });
        //add to the screen
        add(mapButtons[x][y]);
      }
    }
    
    //this is for athestic purposes 
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setBackground(new Color(93, 184, 202));
    scrollPane.setBounds(75, 75, size*59, size*59);
    add(scrollPane);
    
    //now draw the legends 
    xPos=xPos+95;
    yPos = 35;
    JLabel lblLegend = new JLabel("Legend");
    lblLegend.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
    lblLegend.setBounds(xPos, yPos, 71, 27);
    lblLegend.setHorizontalAlignment(SwingConstants.CENTER);
    add(lblLegend);
    xPos = xPos-10;
    yPos = yPos + 55;
    
    JButton closeButton = new JButton("Closed");
    closeButton.setBounds(xPos, yPos, 100, 50);
    closeButton.setBackground(Color.RED);
    closeButton.setOpaque(true);
    add(closeButton);
    yPos = yPos + 55;
    
    JButton openButton = new JButton("Open");
    openButton.setBounds(xPos, yPos, 100, 50);
    add(openButton);
    yPos = yPos + 55;
    
    JButton homeButton = new JButton("Home Office");
    homeButton.setBounds(xPos, yPos, 100, 50);
    homeButton.setBackground(Color.YELLOW);
    homeButton.setOpaque(true);
    add(homeButton);
    yPos = yPos + 55;
    
    
    this.revalidate();
    this.repaint();
  }
  
  public void intersectionClicked(String intersectionName)
  {
    //Will go to intersection page
  }
  
  public static void main(String [] args) {
    AcmeUI acme = new AcmeUI();
    acme.getCompany().setCurrentUser(new User());
    acme.setPanel(new MapUI());
    
    
}
  
}
