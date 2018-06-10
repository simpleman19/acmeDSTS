package acme.pd;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Map {
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
      for(MapIntersection temp : pathToBe)
      {
        System.out.println(temp.getIntersectionName());
      }
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
      //this is a local class for keeping track of the details during the calculations
      class MapIntersectionInfo
      {
        private MapIntersection intersection;
        private int distance;
        private MapIntersectionInfo intersectionBeforeIt;
        private int xVal;
        private int yVal;
        
        public MapIntersectionInfo(MapIntersection intersection, int distance, int xVal, int yVal)
        {
          this.intersection=intersection;
          this.distance = distance;
          this.intersectionBeforeIt = null;
          this.xVal = xVal;
          this.yVal = yVal;
        }
       
        public void setDistance(int distance)
        {
          this.distance = distance;
        }
        
        public int getDistance()
        {
          return this.distance;
        }
        
        public void setIntersection(MapIntersection intersection)
        {
          this.intersection = intersection;
        }
        
        public MapIntersection getIntersection()
        {
          return this.intersection;
        }
        
        public void setIntersectionBefore(MapIntersectionInfo intersectionBeforeIt)
        {
          this.intersectionBeforeIt = intersectionBeforeIt;
        }
        
        public MapIntersectionInfo getIntersectionBeforeIt()
        {
          return this.intersectionBeforeIt;
        }
        
        public void setXVal(int xVal)
        {
          this.xVal = xVal;
        }
        
        public int getXVal()
        {
          return this.xVal;
        }
        
        public void setYVal(int yVal)
        {
          this.yVal = yVal;
        }
        
        public int getYVal()
        {
          return this.yVal;
        }
        
      }
      
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
          intersections[x][y].getIntersection().setEWroad(map[x][y].getEWroad());
          intersections[x][y].getIntersection().setNSroad(map[x][y].getNSroad());
          notVisitedIntersections.add(intersections[x][y]);
          if(map[x][y].getIntersectionName().equals(pickUp.getIntersectionName()))
          {
            placeHolderX=x;
            placeHolderY=y;
            
          }
        }
      }
      
      intersections[placeHolderX][placeHolderY].setDistance(0);
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
            if(currentIntersection.getYVal() != compareValue &&
                currentIntersection.getIntersection().canTravelDirection(dir))
            {
              MapIntersectionInfo intersectionLookingAt = intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor];
             
              if(intersectionLookingAt.getIntersection().isClosedIndefinitely() != true
                  && intersectionLookingAt.getIntersection().isClosed(LocalDateTime.now()) != true)
              {
                adjCurrentDistance = intersectionLookingAt.getDistance();
                
                if((currentIntersection.getDistance() + 1) < adjCurrentDistance)
                {
                  intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor].
                  setDistance(currentIntersection.getDistance() +1);
                  intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor].setIntersectionBefore(currentIntersection);
                  
                  int removeIndex = 0;
                  int tempX = 0;
                  for(MapIntersectionInfo temp : notVisitedIntersections)
                  {
                    if(intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor].getIntersection().getIntersectionName().equals(temp.getIntersection().getIntersectionName()))
                    {
                      removeIndex = tempX;
                    }
                    tempX++;
                  }
                  notVisitedIntersections.remove(removeIndex);
                  notVisitedIntersections.add(intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor]);
                }
                
                listOfAdjacentIntersections.add(intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor]);
              }
            }
          }
          else if(i == 2 || i == 3) //looking at North or South Road
          {
            if(currentIntersection.getXVal() != compareValue &&
                currentIntersection.getIntersection().canTravelDirection(dir))
            {
              MapIntersectionInfo intersectionLookingAt = intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor];
             
              
              if(intersectionLookingAt.getIntersection().isClosedIndefinitely() !=true
                  && intersectionLookingAt.getIntersection().isClosed(LocalDateTime.now()) != true)
              {
                adjCurrentDistance = intersectionLookingAt.getDistance();
                
                if((currentIntersection.getDistance() + 1) < adjCurrentDistance)
                {
                  intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor].
                  setDistance(currentIntersection.getDistance() +1);
                  intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor].setIntersectionBefore(currentIntersection);
                  int removeIndex = 0;
                  int tempX = 0;
                  for(MapIntersectionInfo temp : notVisitedIntersections)
                  {
                    if(intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor].getIntersection().getIntersectionName().equals(temp.getIntersection().getIntersectionName()))
                    {
                      removeIndex = tempX;
                    }
                    tempX++;
                  }
                  notVisitedIntersections.remove(removeIndex);
                  notVisitedIntersections.add(intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor]);
                }
                
                listOfAdjacentIntersections.add(intersections[currentIntersection.getXVal() + xFactor][currentIntersection.getYVal() + yFactor]);
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
            if(temp.getIntersection().getIntersectionName().equals(currentIntersection.getIntersection().getIntersectionName()))
            {
              removeIndex=tempX;
            }
            tempX++;
          }
          notVisitedIntersections.remove(removeIndex);
          
          
          //check if destination is in list of adjacent intersections
          for(MapIntersectionInfo temp : listOfAdjacentIntersections)
          {
            if(temp.getIntersection().getIntersectionName().equals(dropOff.getIntersectionName()))
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
              if(temp.getDistance() < leastDistance)
              { leastDistance = temp.getDistance();
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
        totalBlocks = currentIntersection.getDistance() + totalBlocks;
        pathToBe.add(currentIntersection.getIntersection());
        currentIntersection = currentIntersection.getIntersectionBeforeIt();
      }
      Collections.reverse(pathToBe);
      shortestPath.setPath(pathToBe);
      shortestPath.setBlocksBetweenPickupandDropoff(totalBlocks);
      
      return shortestPath;
    }
    
}
