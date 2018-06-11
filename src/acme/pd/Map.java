package acme.pd;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import javafx.util.converter.LocalTimeStringConverter;

public class Map {
  
  public static void main(String [] args) {
    Company company = new Company();
    
    Ticket ticket = new Ticket();
    
    MapIntersection tempMap[][] = company.getMap().getMap();
     
    for(int x = 0; x < tempMap.length; x++)
    {
      System.out.println("");
      for(int y = 0; y < tempMap.length; y++)
      {
        System.out.print(tempMap[x][y].getIntersectionName() + "    ");
        System.out.print(tempMap[x][y].getEWroad().getDirection() + "    ");
        System.out.print(tempMap[x][y].getNSroad().getDirection() + "    ");
        
      }
    }
    //Test 1-----------------------------------------------------------
    MapIntersection tempPickUp = new MapIntersection();
    Road tempPickUpRoad = new Road();
    tempPickUpRoad.setName("3");
    tempPickUpRoad.setBidirectional(false);
    tempPickUpRoad.setDirection(Direction.NORTH);
    tempPickUp.setNSroad(tempPickUpRoad);
    tempPickUpRoad = new Road();
    tempPickUpRoad.setName("E");
    tempPickUpRoad.setBidirectional(false);
    tempPickUpRoad.setDirection(Direction.WEST);
    tempPickUp.setEWroad(tempPickUpRoad);
    
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("Pick Up Location");
    System.out.println(tempPickUp.getIntersectionName());
    System.out.println(tempPickUp.getEWroad().getDirection());
    System.out.println(tempPickUp.getNSroad().getDirection());
    
    MapIntersection tempDropOff = new MapIntersection();
    Road tempDropOffRoad = new Road();
    tempDropOffRoad.setName("5");
    tempDropOffRoad.setBidirectional(false);
    tempDropOffRoad.setDirection(Direction.NORTH);
    tempDropOff.setNSroad(tempDropOffRoad);
    tempDropOffRoad = new Road();
    tempDropOffRoad.setName("C");
    tempDropOffRoad.setBidirectional(false);
    tempDropOffRoad.setDirection(Direction.WEST);
    tempDropOff.setEWroad(tempDropOffRoad);
    
    String date = "2018-06-11 06:30";
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    ticket.setDeliveryTime(LocalDateTime.parse(date, format));
    
    
    System.out.println("Date and Time Wanted For Delivery: " + date);
    System.out.println("");
    System.out.println("Delivery Location");
    System.out.println(tempDropOff.getIntersectionName());
    System.out.println(tempDropOff.getEWroad().getDirection());
    System.out.println(tempDropOff.getNSroad().getDirection());
    System.out.println("");
    
    Path testPath = company.getMap().getPath(tempPickUp,tempDropOff );
    
    ticket.setPath(testPath);
    ticket.setCompany(company);
    System.out.println("Total Blocks: " + testPath.getBlocksBetweenHomeandDropoff());
    System.out.println("Home Office to Pickup Blocks: " + testPath.getBlocksBetweenHomeandPickup());
    System.out.println("Pickup to Delivery Blocks : " + testPath.getBlocksBetweenPickupandDropoff());
    System.out.println("Quote: " + ticket.calcQuote().shortValue());
    System.out.println("Estimated Departure Time: " + ticket.getEstimatedDepartureTime());
    System.out.println("Estimated Pickup Time: " + ticket.getEstimatedPickupTime());
    System.out.println("Estimated Delivery Time: " + ticket.getEstimatedDeliveryTime());
    //Test 1-----------------------------------------------------------
    
    //Test 2-----------------------------------------------------------
    tempPickUp = new MapIntersection();
    tempPickUpRoad = new Road();
    tempPickUpRoad.setName("2");
    tempPickUpRoad.setBidirectional(true);
    tempPickUpRoad.setDirection(Direction.NORTH);
    tempPickUp.setNSroad(tempPickUpRoad);
    tempPickUpRoad = new Road();
    tempPickUpRoad.setName("G");
    tempPickUpRoad.setBidirectional(true);
    tempPickUpRoad.setDirection(Direction.WEST);
    tempPickUp.setEWroad(tempPickUpRoad);
    
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("Pick Up Location");
    System.out.println(tempPickUp.getIntersectionName());
    System.out.println(tempPickUp.getEWroad().getDirection());
    System.out.println(tempPickUp.getNSroad().getDirection());
    
    tempDropOff = new MapIntersection();
    tempDropOffRoad = new Road();
    tempDropOffRoad.setName("5");
    tempDropOffRoad.setBidirectional(false);
    tempDropOffRoad.setDirection(Direction.NORTH);
    tempDropOff.setNSroad(tempDropOffRoad);
    tempDropOffRoad = new Road();
    tempDropOffRoad.setName("A");
    tempDropOffRoad.setBidirectional(false);
    tempDropOffRoad.setDirection(Direction.WEST);
    tempDropOff.setEWroad(tempDropOffRoad);
    
    date = "2018-06-11 06:30";
    format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    ticket.setDeliveryTime(LocalDateTime.parse(date, format));
    
    
    System.out.println("Date and Time Wanted For Pickup: " + date);
    System.out.println("");
    System.out.println("Delivery Location");
    System.out.println(tempDropOff.getIntersectionName());
    System.out.println(tempDropOff.getEWroad().getDirection());
    System.out.println(tempDropOff.getNSroad().getDirection());
    System.out.println("");
    
    testPath = company.getMap().getPath(tempPickUp,tempDropOff );
    
    ticket.setPath(testPath);
    ticket.setCompany(company);
    System.out.println("Total Blocks: " + testPath.getBlocksBetweenHomeandDropoff());
    System.out.println("Home Office to Pickup Blocks: " + testPath.getBlocksBetweenHomeandPickup());
    System.out.println("Pickup to Delivery Blocks : " + testPath.getBlocksBetweenPickupandDropoff());
    System.out.println("Quote: " + ticket.calcQuote().shortValue());
    System.out.println("Estimated Departure Time: " + ticket.getEstimatedDepartureTime());
    System.out.println("Estimated Pickup Time: " + ticket.getEstimatedPickupTime());
    System.out.println("Estimated Delivery Time: " + ticket.getEstimatedDeliveryTime());
    
  //Test 3-----------------------------------------------------------
    tempPickUp = new MapIntersection();
    tempPickUpRoad = new Road();
    tempPickUpRoad.setName("1");
    tempPickUpRoad.setBidirectional(false);
    tempPickUpRoad.setDirection(Direction.NORTH);
    tempPickUp.setNSroad(tempPickUpRoad);
    tempPickUpRoad = new Road();
    tempPickUpRoad.setName("A");
    tempPickUpRoad.setBidirectional(false);
    tempPickUpRoad.setDirection(Direction.WEST);
    tempPickUp.setEWroad(tempPickUpRoad);
    
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("Pick Up Location");
    System.out.println(tempPickUp.getIntersectionName());
    System.out.println(tempPickUp.getEWroad().getDirection());
    System.out.println(tempPickUp.getNSroad().getDirection());
    
    tempDropOff = new MapIntersection();
    tempDropOffRoad = new Road();
    tempDropOffRoad.setName("7");
    tempDropOffRoad.setBidirectional(false);
    tempDropOffRoad.setDirection(Direction.SOUTH);
    tempDropOff.setNSroad(tempDropOffRoad);
    tempDropOffRoad = new Road();
    tempDropOffRoad.setName("G");
    tempDropOffRoad.setBidirectional(true);
    tempDropOffRoad.setDirection(Direction.WEST);
    tempDropOff.setEWroad(tempDropOffRoad);
    
    date = "2018-06-11 06:30";
    format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    ticket.setDeliveryTime(LocalDateTime.parse(date, format));
    
    
    System.out.println("Date and Time Wanted For Delivery: " + date);
    System.out.println("");
    System.out.println("Delivery Location");
    System.out.println(tempDropOff.getIntersectionName());
    System.out.println(tempDropOff.getEWroad().getDirection());
    System.out.println(tempDropOff.getNSroad().getDirection());
    System.out.println("");
    
    testPath = company.getMap().getPath(tempPickUp,tempDropOff );
    
    ticket.setPath(testPath);
    ticket.setCompany(company);
    System.out.println("Total Blocks: " + testPath.getBlocksBetweenHomeandDropoff());
    System.out.println("Home Office to Pickup Blocks: " + testPath.getBlocksBetweenHomeandPickup());
    System.out.println("Pickup to Delivery Blocks : " + testPath.getBlocksBetweenPickupandDropoff());
    System.out.println("Quote: " + ticket.calcQuote().shortValue());
    System.out.println("Estimated Departure Time: " + ticket.getEstimatedDepartureTime());
    System.out.println("Estimated Pickup Time: " + ticket.getEstimatedPickupTime());
    System.out.println("Estimated Delivery Time: " + ticket.getEstimatedDeliveryTime());
    
    
  }
    private MapIntersection[][] map;
    private MapIntersection homeBase;

    Map(File file) {
        // TODO load in map file
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        map = new MapIntersection[7][7];
        for(int x = 0; x < 7; x++)
        {
          for(int y = 0; y < 7; y++)
          {
            MapIntersection temp = new MapIntersection();
            Road tempRoad = new Road();
            tempRoad.setName(7-y+"");
            if(y==6 || y==4 || y==2)
            {
              tempRoad.setBidirectional(false);
              tempRoad.setDirection(Direction.NORTH); 
            }
            else if(y==0 || y==3)
            {
              tempRoad.setBidirectional(false);
              tempRoad.setDirection(Direction.SOUTH); 
            }
            else
            {
              tempRoad.setBidirectional(true);
              tempRoad.setDirection(Direction.NORTH); 
            }
            
            temp.setNSroad(tempRoad);
            tempRoad = new Road();
            tempRoad.setName(Character.toString(alphabet[x]));
            
            if(x==0 || x==2 || x==4)
            {
              tempRoad.setBidirectional(false);
              tempRoad.setDirection(Direction.WEST);
            }
            else if(x==1 || x==5)
            {
              tempRoad.setBidirectional(false);
              tempRoad.setDirection(Direction.EAST);
            }
            else
            {
              tempRoad.setBidirectional(true);
              tempRoad.setDirection(Direction.EAST); 
            }
            
            temp.setEWroad(tempRoad);
            map[x][y] = temp;
            
          }
        }
        this.homeBase = map[3][3];
    }

    public MapIntersection[][] getMap() {
        return map;
    }

    public MapIntersection getHomeBase() {
        return homeBase;
    }
    
    public Path getPath(MapIntersection pickUp, MapIntersection dropOff)
    {
      Path pathDir1 = shortestPath(this.homeBase, pickUp);
      Path pathDir2 = shortestPath(pickUp, dropOff);
      Path pathDir3 = shortestPath(dropOff, this.homeBase);
      
      Path pathDir = new Path();
      ArrayList<MapIntersection> pathToBe = new ArrayList<MapIntersection>();
      int totalBlocks = 0;
      pathToBe.addAll(pathDir1.getPath());
      totalBlocks = totalBlocks + pathDir1.getBlocksBetweenPickupandDropoff();
      pathDir.setBlocksBetweenHomeandPickup(pathDir1.getBlocksBetweenPickupandDropoff());
      pathToBe.addAll(pathDir2.getPath());
      totalBlocks = totalBlocks + pathDir2.getBlocksBetweenPickupandDropoff();
      pathDir.setBlocksBetweenPickupandDropoff(pathDir2.getBlocksBetweenPickupandDropoff());
      pathToBe.addAll(pathDir3.getPath());
      totalBlocks = totalBlocks + pathDir3.getBlocksBetweenPickupandDropoff();
      pathDir.setBlocksBetweenHomeandDropoff(totalBlocks);
      pathDir.setPath(pathToBe);
     
      System.out.println("----Directions-----");
      
      for(MapIntersection temp : pathDir.getPath())
      {
        System.out.println(temp.getIntersectionName());
      }
      return pathDir;
    }
    
    private Path shortestPath(MapIntersection pickUp, MapIntersection dropOff)
    {
      //path that will be set to shortest path and returned
      Path shortestPath = new Path();
      
      //used for looping
      boolean foundDropOff = false;
      
     //this represents the map
      MapIntersectionInfo intersections[][] = new MapIntersectionInfo[map.length][map.length];   
      
      List<MapIntersectionInfo> visitedIntersections = new ArrayList<MapIntersectionInfo>();
      List<MapIntersectionInfo> notVisitedIntersections = new ArrayList<MapIntersectionInfo>();
      
      //set up the map representation
      //set all intersections' distances to infinity
      int placeHolderX = 0;
      int placeHolderY = 0;
      for(int x = 0; x <map.length; x++)
      {
        for(int y = 0; y < map.length; y++)
        {
          intersections[x][y] = new MapIntersectionInfo(new MapIntersection(), Integer.MAX_VALUE, x, y);
          intersections[x][y].intersection.setEWroad(map[x][y].getEWroad());
          intersections[x][y].intersection.setNSroad(map[x][y].getNSroad());
          notVisitedIntersections.add(intersections[x][y]);
          if(map[x][y].getIntersectionName().equals(pickUp.getIntersectionName()))
          {
            placeHolderX=x;
            placeHolderY=y;
            
          }
        }
      }
      
      intersections[placeHolderX][placeHolderY].distance=0;
      MapIntersectionInfo currentIntersection =  intersections[placeHolderX][placeHolderY];
      
      
      while(!foundDropOff)
      {
        visitedIntersections.add(currentIntersection);
        
        //-----------------------------------------------------------------------------------------------------------------
        //calculate distances from current intersection to adjacent intersections------------------------------------------
        //-----------------------------------------------------------------------------------------------------------------
        List<MapIntersectionInfo> listOfAdjacentIntersections = new ArrayList<MapIntersectionInfo>();
        int adjCurrentDistance;
        
        for(int i = 0; i < 4; i++)
        {
          //set up values
          Direction dir;
          int xFactor;
          int yFactor;
          int compareValue;
          if(i == 0)     //West
          {
             dir = Direction.WEST;
             xFactor =0;
             yFactor = -1;
             compareValue =  0;
          }
          else if(i == 1)   //East
          {
            dir = Direction.EAST;
            xFactor = 0;
            yFactor = 1;
            compareValue = map.length-1;
          }
          else if(i == 2)   //North
          { 
            dir = Direction.NORTH;
            xFactor = -1;
            yFactor = 0;
            compareValue =  0;
          }
          else            //South
          {
            dir = Direction.SOUTH;
            xFactor = 1;
            yFactor = 0;
            compareValue = map.length-1;
          }
   
        
          if(i == 0 || i == 1)  //looking at East or West road
          {
            if(currentIntersection.yVal != compareValue &&
                currentIntersection.intersection.canTravelDirection(dir))
            {
              MapIntersectionInfo intersectionLookingAt = intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor];
             
              if(intersectionLookingAt.intersection.isClosedIndefinitely() != true
                  && intersectionLookingAt.intersection.isClosed(LocalDateTime.now()) != true)
              {
                adjCurrentDistance = intersectionLookingAt.distance;
                
                if((currentIntersection.distance + 1) < adjCurrentDistance)
                {
                  intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor].distance = currentIntersection.distance +1;
                  intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor].intersectionBeforeIt = currentIntersection;
                  
                  int removeIndex = 0;
                  int tempX = 0;
                  for(MapIntersectionInfo temp : notVisitedIntersections)
                  {
                    if(intersections[currentIntersection.xVal+ xFactor][currentIntersection.yVal + yFactor].intersection.getIntersectionName().equals(temp.intersection.getIntersectionName()))
                    {
                      removeIndex = tempX;
                    }
                    tempX++;
                  }
                  notVisitedIntersections.remove(removeIndex);
                  notVisitedIntersections.add(intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor]);
                }
                
                listOfAdjacentIntersections.add(intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor]);
              }
            }
          }
          else if(i == 2 || i == 3) //looking at North or South Road
          {
            if(currentIntersection.xVal != compareValue &&
                currentIntersection.intersection.canTravelDirection(dir))
            {
              MapIntersectionInfo intersectionLookingAt = intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor];
             
              
              if(intersectionLookingAt.intersection.isClosedIndefinitely() !=true
                  && intersectionLookingAt.intersection.isClosed(LocalDateTime.now()) != true)
              {
                adjCurrentDistance = intersectionLookingAt.distance;
                
                if((currentIntersection.distance + 1) < adjCurrentDistance)
                {
                  intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor].distance = currentIntersection.distance +1;
                  intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor].intersectionBeforeIt = currentIntersection;
                  int removeIndex = 0;
                  int tempX = 0;
                  for(MapIntersectionInfo temp : notVisitedIntersections)
                  {
                    if(intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor].intersection.getIntersectionName().equals(temp.intersection.getIntersectionName()))
                    {
                      removeIndex = tempX;
                    }
                    tempX++;
                  }
                  notVisitedIntersections.remove(removeIndex);
                  notVisitedIntersections.add(intersections[currentIntersection.xVal+ xFactor][currentIntersection.yVal + yFactor]);
                }
                
                listOfAdjacentIntersections.add(intersections[currentIntersection.xVal + xFactor][currentIntersection.yVal + yFactor]);
              }
            }
          }
        }
        
        //-----------------------------------------------------------------------------------------------------------------
        //end of calculate distances from current intersection to adjacent intersections-----------------------------------
        //-----------------------------------------------------------------------------------------------------------------
          
          int removeIndex = 0;
          int tempX = 0;
          for(MapIntersectionInfo temp : notVisitedIntersections)
          {
            if(temp.intersection.getIntersectionName().equals(currentIntersection.intersection.getIntersectionName()))
            {
              removeIndex=tempX;
            }
            tempX++;
          }
          notVisitedIntersections.remove(removeIndex);
          
          
          //check if destination is in list of adjacent intersections
          for(MapIntersectionInfo temp : listOfAdjacentIntersections)
          {
            if(temp.intersection.getIntersectionName().equals(dropOff.getIntersectionName()))
            {
              foundDropOff = true;
              currentIntersection = temp;
            }
          }
          
          //if in case already found drop out
          if(!foundDropOff)
          {
            listOfAdjacentIntersections.clear();
            
            int leastDistance = Integer.MAX_VALUE;
            //get new current intersection
            for(MapIntersectionInfo temp : notVisitedIntersections)
            {
              if(temp.distance < leastDistance)
              { leastDistance = temp.distance;
                currentIntersection = temp;
              }
            }
          }
        }
      
      //set up path to return
      ArrayList<MapIntersection> pathToBe = new ArrayList<MapIntersection>();
      int totalBlocks = 0;
      while(currentIntersection != null)
      {
        totalBlocks = 1 + totalBlocks;
        pathToBe.add(currentIntersection.intersection);
        currentIntersection = currentIntersection.intersectionBeforeIt;
      }
      Collections.reverse(pathToBe);
      shortestPath.setPath(pathToBe);
      shortestPath.setBlocksBetweenPickupandDropoff(totalBlocks);
      
      return shortestPath;
    }
    
  //this is a local class for keeping track of the details during the calculations
    class MapIntersectionInfo
    {
      public MapIntersection intersection;
      public int distance;
      public MapIntersectionInfo intersectionBeforeIt;
      public int xVal;
      public int yVal;
      
      public MapIntersectionInfo(MapIntersection intersection, int distance, int xVal, int yVal)
      {
        this.intersection=intersection;
        this.distance = distance;
        this.intersectionBeforeIt = null;
        this.xVal = xVal;
        this.yVal = yVal;
      }
      
    }
    
}
