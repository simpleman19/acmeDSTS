package acme.pd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.time.LocalDateTime;
import java.util.Collections;

public class Map {
    private MapIntersection[][] map;
    private MapIntersection homeBase;

    private static final String NORTH = "^";
    private static final String SOUTH = "V";
    private static final String EAST = ">";
    private static final String WEST = "<";
    private static final String N_BORDER = "nb";
    private static final String S_BORDER = "sb";
    private static final String E_BORDER = "eb";
    private static final String W_BORDER = "wb";
    private static final String CLOSED = "X";
    private static final String OPEN = "O";
    private static final String EW_BI = "-";
    private static final String NS_BI = "|";
    private static final String HOME = "H";
    private static final String CLOSURES = "IntersectionClosures";
    private String[][] closures;
    private String[][] intersections;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    public Map(File file) {
        importMap(file);
    }
    
    public void importMap(File file) {
        // this gives you a 2-dimensional array of strings
        List<List<String>> lines = new ArrayList<>();
        /*
         * This is where we'll store the intersection information and closures. The
         * intersections array is different form the map object.
         *
         * intersection[][] stores the imported road and intersection values
         * closures[][] stores the imported closure information with date ranges
         */
        intersections = new String[40][40];
        closures = new String[40][4];

        Scanner inputStream = null;
        // read in the file and put it into a 2d list
        try {
            inputStream = new Scanner(file);
            while (inputStream.hasNext()) {
                String line = inputStream.next();
                String[] values = line.split(",");
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(Arrays.asList(values));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*
         * Read in all the lines and store map info into the Intersections array and
         * closure info into the closures array
         */
        int lineNo = 0;
        int numRows = 0, numCols = 0;
        boolean closuresSection = false;
        for (List<String> line : lines) {
            int columnNo = 0;
            for (String value : line) {
                // Check for the intersection closure portion of the CSV
                if (value.equalsIgnoreCase(CLOSURES)) {
                    closuresSection = true;
                    columnNo = 0;
                    lineNo = 0;
                }
                // add the info into the closure array
                if (closuresSection == true) {
                    closures[lineNo][columnNo] = value;
                }
                // add the info to the map array
                else {
                    intersections[columnNo][lineNo] = value;
                }
                columnNo++;
            }
            lineNo++;
        }

        /*
         * Find out how many intersections we actually have so we can give our
         * MapIntersection array the necessary dimensions.
         */
        for (int i = 0; intersections[0][i] != null; i++) {
            numRows++;
            numCols = 0;
            for (int j = 0; intersections[j][0] != null; j++) {
                numCols++;
            }
        }

        // Instantiate the map with the number of rows/columns we counted prior
        map = new MapIntersection[(numRows + 1) / 2][(numCols + 1) / 2];
        // iterate through the list of intersections and set the road info
        for (int rows = 0; rows < numRows; rows++) {
            for (int cols = 0; cols < numCols; cols++) {
                // intersections fall on the odd indices of the csv file
                if ((cols % 2) == 1 && rows % 2 == 1) {
                    String nsRoadDir = "";
                    String ewRoadDir = "";
                    // create new roads and an intersection
                    MapIntersection tempIntersection = new MapIntersection();
                    Road nsRoad = new Road();
                    Road ewRoad = new Road();

                    // check to see if we hit the North/South borders
                    if (rows == 1) {
                        nsRoadDir = N_BORDER;
                    } else if (intersections[cols][rows + 1] == null) {
                        nsRoadDir = S_BORDER;
                    } else {
                        nsRoadDir = intersections[cols][rows + 1];
                    }

                    // check to see if we hit the East/West borders
                    if (cols == 1) {
                        ewRoadDir = W_BORDER;
                    } else if (intersections[cols + 1][rows] == null) {
                        ewRoadDir = E_BORDER;
                    } else {
                        ewRoadDir = intersections[cols + 1][rows];
                    }

                    /* handle the NS road */
                    // set the road name to to first row for this column
                    nsRoad.setName(intersections[cols][0]);

                    // Check for directional conditions
                    switch (nsRoadDir) {
                        case NORTH:
                            nsRoad.setDirection(Direction.NORTH);
                            break;
                        case SOUTH:
                            nsRoad.setDirection(Direction.SOUTH);
                            break;
                        // secure the borders!
                        case N_BORDER:
                            if (intersections[cols][rows + 1].equalsIgnoreCase(SOUTH)
                                    || intersections[cols][rows + 1].equalsIgnoreCase(NS_BI)) {
                                nsRoad.setDirection(Direction.SOUTH);
                            } else {
                                nsRoad.setDirection(null);
                            }
                            break;
                        case S_BORDER:
                            if (intersections[cols][rows - 1].equalsIgnoreCase(NORTH)
                                    || intersections[cols][rows - 1].equalsIgnoreCase(NS_BI)) {
                                nsRoad.setDirection(Direction.NORTH);
                            } else {
                                nsRoad.setDirection(null);
                            }
                            break;
                        default: // this must be a bidirectional if nothing else fits
                            nsRoad.setDirection(Direction.NORTH);
                            nsRoad.setBidirectional(true);
                    }
                    tempIntersection.setNSroad(nsRoad);

                    /* Handle EW road */
                    // set the road name to to first column for this row
                    ewRoad.setName(intersections[0][rows]);
                    // check directional conditions
                    switch (ewRoadDir) {
                        case WEST:
                            ewRoad.setDirection(Direction.WEST);
                            break;
                        case EAST:
                            ewRoad.setDirection(Direction.EAST);
                            break;
                        case E_BORDER:
                            if (intersections[cols - 1][rows].equalsIgnoreCase(WEST)
                                    || intersections[cols - 1][rows].equalsIgnoreCase(EW_BI)) {
                                ewRoad.setDirection(Direction.WEST);
                            } else {
                                ewRoad.setDirection(null);
                            }
                            break;
                        case W_BORDER:
                            if (intersections[cols + 1][rows].equalsIgnoreCase(EAST)
                                    || intersections[cols + 1][rows].equalsIgnoreCase(EW_BI)) {
                                ewRoad.setDirection(Direction.EAST);
                            } else {
                                ewRoad.setDirection(null);
                            }
                            break;
                        default:
                            ewRoad.setDirection(Direction.EAST);
                            ewRoad.setBidirectional(true);
                    }
                    tempIntersection.setEWroad(ewRoad);

                    // store the intersection into the proper location on the map
                    int mapRows = rows / 2, mapCols = cols / 2;
                    map[mapRows][mapCols] = tempIntersection;

                    // check for the intersection closure state
                    if (intersections[cols][rows].equalsIgnoreCase(CLOSED)) {
                        setClosures(mapRows, mapCols);
                    }
                    // check for home
                    else if(intersections[cols][rows].equalsIgnoreCase(HOME)) {
                        setHomeBase(mapRows, mapCols);
                    }

                }

            }
        }

        // set the homeBase
        // setHomeBase(3, 3);

    }

    public MapIntersection[][] getMap() {
        return map;
    }
    
    public Set<Road> getRoads() {
    	Set<Road> roads = new TreeSet<Road>();
    	for (int i = 0; i < map.length; i++) {
    		for (int j = 0; j < map[i].length; j++) {
    			roads.add(map[i][j].getEWroad());
    			roads.add(map[i][j].getNSroad());
    		}
    	}
    	return roads;
    }
    
    public Road getRoadByName(String roadName) {
		return getRoads().stream().filter(road -> road.getName().equalsIgnoreCase(roadName)).findFirst().get();
	}
    
    public Set<Road> getStreets() {
    	Set<Road> roads = new TreeSet<Road>();
    	for (int i = 0; i < map.length; i++) {
    		for (int j = 0; j < map[i].length; j++) {
    			roads.add(map[i][j].getEWroad());
    		}
    	}
    	return roads;
    }
    
    public Set<Road> getAvenues() {
    	Set<Road> roads = new TreeSet<Road>();
    	for (int i = 0; i < map.length; i++) {
    		for (int j = 0; j < map[i].length; j++) {
    			roads.add(map[i][j].getNSroad());
    		}
    	}
    	return roads;
    }

	public MapIntersection getIntersection(Road one, Road another) {
		if (one == null || another == null)
			System.out.println("Could not find road");
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				Road lookEW = map[i][j].getEWroad();
				Road lookNS = map[i][j].getNSroad();
				
				if ((map[i][j].getEWroad().equals(one) && map[i][j].getNSroad().equals(another)) ||
						(map[i][j].getEWroad().equals(another) && map[i][j].getNSroad().equals(one)))
					return map[i][j];
			}
		}
		return null;
	}
    
    public MapIntersection getHomeBase() {
        return homeBase;
    }

    public MapIntersection getIntersection(Customer customer) {
        return this.getIntersection(customer.getAvenueName(), customer.getStreetName());
    }

    public MapIntersection getIntersection(String road, String road2) {
        MapIntersection intersection = findIntersection(road, road2);
        if (intersection == null) {
            intersection = findIntersection(road2, road);
        }
        return intersection;
    }

    private MapIntersection findIntersection(String nsRoad, String ewRoad) {
        for (int i = 0; i < map.length; i++) {
            if (map[0][i].getNSroad().getName().equalsIgnoreCase(nsRoad)) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[j][i].getEWroad().getName().equalsIgnoreCase(ewRoad)) {
                        return map[j][i];
                    }
                }
            }
        }
        return null;
    }

    public Direction getTravelDirection(MapIntersection start, MapIntersection end) {
        int [] startLoc = getLocation(start);
        int [] endLoc = getLocation(end);

        if (startLoc != null && endLoc != null) {
            if (startLoc[0] == endLoc[0]) {
                if (start.getEWroad().isBidirectional()) {
                    if (startLoc[1] > endLoc[1]) {
                        return Direction.WEST;
                    } else {
                        return Direction.EAST;
                    }
                } else {
                    return start.getEWroad().getDirection();
                }
            } else if (startLoc[1] == endLoc[1]) {
                if (start.getNSroad().isBidirectional()) {
                    if (startLoc[1] > endLoc[1]) {
                        return Direction.SOUTH;
                    } else {
                        return Direction.NORTH;
                    }
                } else {
                    return start.getNSroad().getDirection();
                }
            }
        }
        return null;
    }

    public int [] getLocation(MapIntersection inter) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (inter.getEWroad().getName().equalsIgnoreCase(map[i][j].getEWroad().getName())
                        && inter.getNSroad().getName().equalsIgnoreCase(map[i][j].getNSroad().getName())) {
                    return new int [] {i, j};
                }
            }
        }
        return null;
    }

    private void setHomeBase(int NS, int EW) {
        this.homeBase = map[NS][EW];
    }

    private void setClosures(int mapRow, int mapCol) {
        // set the intersection to closed
        map[mapRow][mapCol].setClosedIndefinitely(true);
        // Check for closure dates
        for (int row = 0; row < closures[0].length; row++) {
            for (int col = 0; col < 4; col++) {
                if (closures[row][col] != null) {
                    if (map[mapRow][mapCol].getEWroad().getName().equals(closures[row][0])) {
                        if (map[mapRow][mapCol].getNSroad().getName().equals(closures[row][1])) {
                            // assign the closure dates
                            map[mapRow][mapCol].setClosedFrom(LocalDate.parse(closures[row][2], formatter));
                            map[mapRow][mapCol].setClosedTo(LocalDate.parse(closures[row][3], formatter));
                            // This intersection is not closed indefinitely
                            map[mapRow][mapCol].setClosedIndefinitely(false);
                            break;
                        } // end if NS road
                    } // end if EW road
                } // end if closures is not null
            } // end for columns
        } // end for rows
    }

    void exportMap(File file) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            StringBuilder sb = new StringBuilder();
            // handle the map information
            for (int i = 0; i < intersections.length; i++) {
                for (int j = 0; j < intersections.length; j++) {
                    if (intersections[j][i] != null) {
                        // if we are on an intersection
                        if (j % 2 == 1 && i % 2 == 1) {
                            // is it closed?
                            if (map[(i / 2)][(j / 2)].isClosedIndefinitely()
                                    || !map[i / 2][j / 2].isClosed(LocalDate.MAX)) {
                                sb.append(CLOSED);
                            }
                            // check for home base
                            else if (map[i/2][j/2].equals(homeBase)){
                                sb.append(HOME);
                            }
                            // it must be open
                            else {
                                sb.append(OPEN);
                            }
                        }
                        // we aren't on an intersection, assume it is a label or direction
                        else {
                            sb.append(intersections[j][i]);
                        }
                        sb.append(",");
                    }
                }

                // remove trailing commas
                sb.setLength(sb.length() - 1);
                // go to next line if we are still parsing map info
                sb.append("\n");
                if (intersections[0][i] == null) {
                    break;
                }

            }

            // handle the closure information
            sb.append(CLOSURES);
            sb.append("\n");
            for (int i = 0; i < map[0].length; i++) {
                for (int j = 0; j < map.length; j++) {
                    if (!map[i][j].isClosed(LocalDate.MAX)) {
                        sb.append(map[i][j].getEWroad().getName());
                        sb.append(",");
                        sb.append(map[i][j].getNSroad().getName());
                        sb.append(",");
                        sb.append(map[i][j].getClosedFrom().format(formatter));
                        sb.append(",");
                        sb.append(map[i][j].getClosedTo().format(formatter));
                        sb.append("\n");
                    }
                }
            }
            br.write(sb.toString());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        pathDir.setBlocksBetweenDropoffandHome(pathDir3.getBlocksBetweenPickupandDropoff());
        pathDir.setPath(pathToBe);

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
                        && intersectionLookingAt.intersection.isClosed(LocalDate.now()) != true)
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
                        && intersectionLookingAt.intersection.isClosed(LocalDate.now()) != true)
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

    public static void main(String [] args) {
        Company company = new Company();

        Ticket ticket = new Ticket(company);

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
        ticket.setEstimatedDeliveryTime(LocalDateTime.parse(date, format));


        System.out.println("Date and Time Wanted For Delivery: " + date);
        System.out.println("");
        System.out.println("Delivery Location");
        System.out.println(tempDropOff.getIntersectionName());
        System.out.println(tempDropOff.getEWroad().getDirection());
        System.out.println(tempDropOff.getNSroad().getDirection());
        System.out.println("");

        Path testPath = company.getMap().getPath(tempPickUp,tempDropOff );

        ticket.setPath(testPath);
        System.out.println("Total Blocks: " + testPath.getBlocksBetweenHomeandDropoff());
        System.out.println("Home Office to Pickup Blocks: " + testPath.getBlocksBetweenHomeandPickup());
        System.out.println("Pickup to Delivery Blocks : " + testPath.getBlocksBetweenPickupandDropoff());
        System.out.println("Quote: " + ticket.getQuotedPrice().shortValue());
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
        ticket.setEstimatedDeliveryTime(LocalDateTime.parse(date, format));


        System.out.println("Date and Time Wanted For Pickup: " + date);
        System.out.println("");
        System.out.println("Delivery Location");
        System.out.println(tempDropOff.getIntersectionName());
        System.out.println(tempDropOff.getEWroad().getDirection());
        System.out.println(tempDropOff.getNSroad().getDirection());
        System.out.println("");

        testPath = company.getMap().getPath(tempPickUp,tempDropOff );

        ticket.setPath(testPath);
        System.out.println("Total Blocks: " + testPath.getBlocksBetweenHomeandDropoff());
        System.out.println("Home Office to Pickup Blocks: " + testPath.getBlocksBetweenHomeandPickup());
        System.out.println("Pickup to Delivery Blocks : " + testPath.getBlocksBetweenPickupandDropoff());
        System.out.println("Quote: " + ticket.getQuotedPrice().shortValue());
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
        ticket.setEstimatedDeliveryTime(LocalDateTime.parse(date, format));


        System.out.println("Date and Time Wanted For Delivery: " + date);
        System.out.println("");
        System.out.println("Delivery Location");
        System.out.println(tempDropOff.getIntersectionName());
        System.out.println(tempDropOff.getEWroad().getDirection());
        System.out.println(tempDropOff.getNSroad().getDirection());
        System.out.println("");

        testPath = company.getMap().getPath(tempPickUp,tempDropOff );

        ticket.setPath(testPath);
        System.out.println("Total Blocks: " + testPath.getBlocksBetweenHomeandDropoff());
        System.out.println("Home Office to Pickup Blocks: " + testPath.getBlocksBetweenHomeandPickup());
        System.out.println("Pickup to Delivery Blocks : " + testPath.getBlocksBetweenPickupandDropoff());
        System.out.println("Quote: " + ticket.getQuotedPrice().shortValue());
        System.out.println("Estimated Departure Time: " + ticket.getEstimatedDepartureTime());
        System.out.println("Estimated Pickup Time: " + ticket.getEstimatedPickupTime());
        System.out.println("Estimated Delivery Time: " + ticket.getEstimatedDeliveryTime());


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
