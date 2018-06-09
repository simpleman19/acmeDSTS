package acme.pd;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
	private static final String OPEN = "O";
	private static final String CLOSED = "X";
	private static final String EW_BI = "-";
	private static final String NS_BI = "|";
	private String[][] closures;
	private String[][] intersection;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

	Map(File file) {
		importMap(file);
		// TODO export map
	}

	public void importMap(File file) {
		// this gives you a 2-dimensional array of strings
		List<List<String>> lines = new ArrayList<>();
		/*
		 * This is where we'll store the intersection information and closures. There
		 * will be a max of 40x40 for each (40 was an arbitrarily selected number but
		 * there is no requirement for 40)
		 */
		intersection = new String[40][40];
		closures = new String[40][40];

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
			// TODO add a popup for when there is no file
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
				if (value.equalsIgnoreCase("IntersectionClosures")) {
					closuresSection = true;
					columnNo = 0;
					lineNo = 0;
				}
				if (closuresSection == true) {
					closures[lineNo][columnNo] = value;
				} else { // add the info to the map array
					intersection[columnNo][lineNo] = value;
				}
				columnNo++;
			}
			lineNo++;
		}

		/*
		 * Find out how many intersections we actually have so we can give our
		 * MapIntersection array the necessary dimensions.
		 */
		for (int i = 0; intersection[0][i] != null; i++) {
			numRows++;
			numCols = 0;
			for (int j = 0; intersection[j][0] != null; j++) {
				numCols++;
			}
		}

		// Instantiate the map with the number of rows/columns we counted eariler
		map = new MapIntersection[(numRows + 1) / 2][(numCols + 1) / 2];
		// iterate through the list of intersections and set the road info
		for (int rows = 0; rows < numRows; rows++) {
			for (int cols = 0; cols < numCols; cols++) {
				if ((cols % 2) == 1 && rows % 2 == 1) {
					String nsRoadDir = "";
					String ewRoadDir = "";

					// check to see if we hit the North/South borders
					if (rows == 1) {
						nsRoadDir = N_BORDER;
					} else if (intersection[cols][rows + 1] == null) {
						nsRoadDir = S_BORDER;
					} else {
						nsRoadDir = intersection[cols][rows + 1];
					}

					// check to see if we hit the East/West borders
					if (cols == 1) {
						ewRoadDir = W_BORDER;
					} else if (intersection[cols + 1][rows] == null) {
						ewRoadDir = E_BORDER;
					} else {
						ewRoadDir = intersection[cols + 1][rows];
					}

					MapIntersection temp = new MapIntersection();
					Road nsRoad = new Road();
					Road ewRoad = new Road();
					/* handle the NS road */
					// set the road name to to first row for this column
					nsRoad.setName(intersection[cols][0]);
					// check which direction it is
					// if it is neither, it is bidirectional
					switch (nsRoadDir) {
					case NORTH:
						nsRoad.setDirection(Direction.NORTH);
						break;
					case SOUTH:
						nsRoad.setDirection(Direction.SOUTH);
						break;
					// secure the borders!
					case N_BORDER:
						if (intersection[cols][rows + 1].equalsIgnoreCase(SOUTH)
								|| intersection[cols][rows + 1].equalsIgnoreCase(NS_BI)) {
							nsRoad.setDirection(Direction.SOUTH);
						} else {
							nsRoad.setDirection(null);
						}
						break;
					case S_BORDER:
						if (intersection[cols][rows - 1].equalsIgnoreCase(NORTH)
								|| intersection[cols][rows - 1].equalsIgnoreCase(NS_BI)) {
							nsRoad.setDirection(Direction.NORTH);
						} else {
							nsRoad.setDirection(null);
						}
						break;
					default: // this must be a bidirectional if nothing else fits
						nsRoad.setDirection(Direction.NORTH);
						nsRoad.setBidirectional(true);
					}
					temp.setNSroad(nsRoad);

					/* Handle EW road */
					// set the road name to to first column for this row
					ewRoad.setName(intersection[0][rows]);
					// check which direction it is
					// if it is neither, it is bidirectional
					switch (ewRoadDir) {
					case WEST:
						ewRoad.setDirection(Direction.WEST);
						break;
					case EAST:
						ewRoad.setDirection(Direction.EAST);
						break;
					case E_BORDER:
						if (intersection[cols - 1][rows].equalsIgnoreCase(WEST)
								|| intersection[cols - 1][rows].equalsIgnoreCase(EW_BI)) {
							ewRoad.setDirection(Direction.WEST);
						} else {
							ewRoad.setDirection(null);
						}
						break;
					case W_BORDER:
						if (intersection[cols + 1][rows].equalsIgnoreCase(EAST)
								|| intersection[cols + 1][rows].equalsIgnoreCase(EW_BI)) {
							ewRoad.setDirection(Direction.EAST);
						} else {
							ewRoad.setDirection(null);
						}
						break;
					default:
						ewRoad.setDirection(Direction.EAST);
						ewRoad.setBidirectional(true);
					}
					temp.setEWroad(ewRoad);

					int mapRows = rows / 2, mapCols = cols / 2;
					// set the intersection in our map
					map[mapRows][mapCols] = temp;

					// check for the intersection closure state
					if (intersection[cols][rows].equalsIgnoreCase(CLOSED)) {
						// if the closure is in the closures array, set the date
						map[mapRows][mapCols].setClosedIndefinitely(true);
						// check to see if it is in the closures list
						for (int row = 0; row < closures[0].length; row++) {
							for (int col = 0; col < 4; col++) {
								if (closures[row][col] != null) {
									if (ewRoad.getName().equals(closures[row][0])) {
										if (nsRoad.getName().equals(closures[row][1])) {
											// assign the closure dates
											map[mapRows][mapCols]
													.setClosedFrom(LocalDate.parse(closures[row][2], formatter));
											map[mapRows][mapCols]
													.setClosedTo(LocalDate.parse(closures[row][3], formatter));
											// don't close it indefinitely
											map[mapRows][mapCols].setClosedIndefinitely(false);
											break;
										}
									}
								}
							}
						}
					}
				}

			}
		}

		// set the homebase
		setHomeBase(3, 3);

		// test all the directions
		// testDirection();

		// test closures
		testClosure();
	}

	private void setHomeBase(int NS, int EW) {
		this.homeBase = map[NS][EW];
	}

	public MapIntersection[][] getMap() {
		return map;
	}

	public MapIntersection getHomeBase() {
		return homeBase;
	}

	public void testClosure() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				System.out.println(map[i][j].getIntersectionName());
				System.out.println((map[i][j].isClosedIndefinitely()) );
				if (map[i][j].getClosedFrom()!= null) {System.out.println(map[i][j].getClosedFrom() + " " + map[i][j].getClosedTo());}
				System.out.print("\n");
			}
			System.out.print("\n");
		}
	}

	public void testDirection() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				/*
				 * System.out.println(map[i][j].getNSroad().getName());
				 * System.out.println(map[i][j].getNSroad().getDirection());
				 * System.out.println(map[i][j].getEWroad().getName());
				 * System.out.println(map[i][j].getEWroad().getDirection());
				 */
				System.out.println(map[i][j].getIntersectionName());
				System.out.println(map[i][j].canTravelDirection(Direction.NORTH));
				System.out.println(map[i][j].canTravelDirection(Direction.SOUTH));
				System.out.println(map[i][j].canTravelDirection(Direction.EAST));
				System.out.println(map[i][j].canTravelDirection(Direction.WEST));

				System.out.print("\n");
			}
			System.out.print("\n");
		}
	}
}
