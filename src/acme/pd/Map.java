package acme.pd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Map {
	private MapIntersection[][] map;
	private MapIntersection homeBase;

	Map(File file) {
		// this gives you a 2-dimensional array of strings
		List<List<String>> lines = new ArrayList<>();
		String[][] maps = new String[40][40];
		String[][] closures = new String[40][40];

		Scanner inputStream = null;
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

		final int rows = lines.size();
		final int cols = lines.get(0).size();
		// the following code lets you iterate through the 2-dimensional array
		int lineNo = 0;
		boolean closuresSection = false;
		for (List<String> line : lines) {
			int columnNo = 0;
			for (String value : line) {
				if (value.equals("IntersectionClosures")) {
					closuresSection = true;
					columnNo = 0;
					lineNo = 0;
				}
				if (closuresSection == true) {
					closures[columnNo][lineNo] = value;
				} else {
					maps[columnNo][lineNo] = value;
				}
				columnNo++;
			}
			lineNo++;
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (maps[j][i] != null) {
					//System.out.print(maps[j][i] + "\t");
				}
			}
			//System.out.print("\n");
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (closures[j][i] != null) {
					//System.out.print(closures[j][i] + "\t");
				}
			}
			//System.out.print("\n");
		}


		map = new MapIntersection[8][8];
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < cols; j++) {
				if ((j % 2) == 1 && i % 2 == 1) {
					String NS = "";
					String EW = "";

					// System.out.println("col=" + j + " row=" + i);
					NS = maps[j][i + 1];
					EW = maps[j + 1][i];

					if (NS == null) {
						NS = "";
					}
					if (EW == null) {
						EW = "";
					}

					MapIntersection temp = new MapIntersection();
					Road nRoad = new Road();
					Road eRoad = new Road();
					/* handle the NS road */
					// set the road name to to first row for this column
					nRoad.setName(maps[j][0]);
					// check which direction it is
					// if it is neither, it is bidirectional
					switch (NS) {
					case "^":
						nRoad.setDirection(Direction.NORTH);
						break;
					case "V":
						nRoad.setDirection(Direction.SOUTH);
						break;
					default:
						nRoad.setDirection(Direction.NORTH);
						nRoad.setBidirectional(true);
					}
					temp.setNSroad(nRoad);
					

					/* Handle EW road */
					// set the road name to to first column for this row
					eRoad.setName(maps[0][i]);
					// check which direction it is
					// if it is neither, it is bidirectional
					switch (EW) {
					case "<":
						eRoad.setDirection(Direction.WEST);
						break;
					case ">":
						eRoad.setDirection(Direction.EAST);
						break;
					default:
						eRoad.setDirection(Direction.EAST);
						eRoad.setBidirectional(true);
					}
					temp.setEWroad(eRoad);

					// set the intersection in our map
					map[i/2][j/2] = temp;
				}
			}
		}

		this.homeBase = map[3][3];
		
		System.out.println(map[6][6].getNSroad().getName());
		System.out.println(map[6][6].getNSroad().getDirection());
		System.out.println(map[6][6].getEWroad().getName());
		System.out.println(map[6][6].getEWroad().getDirection());
		System.out.println(map[6][6].getIntersectionName());
		System.out.println(map[6][6].canTravelDirection(Direction.NORTH));
		System.out.println(map[6][6].canTravelDirection(Direction.SOUTH));
		System.out.println(map[6][6].canTravelDirection(Direction.EAST));
		System.out.println(map[6][6].canTravelDirection(Direction.WEST));
		System.out.println("\n"+this.getHomeBase().getNSroad().getName());
		System.out.println(this.getHomeBase().getIntersectionName());

	}

	public MapIntersection[][] getMap() {
		return map;
	}

	public MapIntersection getHomeBase() {
		return homeBase;
	}
}
