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
	private static final String CLOSURES = "IntersectionClosures";
	private String[][] closures;
	private String[][] intersections;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

	Map(File file) {
		importMap(file);
		// TODO call export function on shutdown
		// exportMap(file);
		// TODO export map
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
				}

			}
		}

		// set the homeBase
		setHomeBase(3, 3);

	}

	public MapIntersection[][] getMap() {
		return map;
	}

	public MapIntersection getHomeBase() {
		return homeBase;
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

	private void exportMap(File file) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(file));
			StringBuilder sb = new StringBuilder();
			// handle the map information
			for (int i = 0; i < intersections.length; i++) {
				for (int j = 0; j < intersections.length; j++) {
					if (intersections[j][i] != null) {
						// if we are on an intersection
						if (j % 2 == 1 && i % 2 == 1) {
							// is it closed
							if (map[(i / 2)][(j / 2)].isClosedIndefinitely()
									|| !map[i / 2][j / 2].isClosed(LocalDate.MAX)) {
								sb.append(CLOSED);
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
			// TODO can't access map file for export
			e.printStackTrace();
		}
	}

}
