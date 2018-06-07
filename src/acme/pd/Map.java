package acme.pd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Map {
	private MapIntersection[][] map;
	private MapIntersection homeBase;

	Map(File file) {
		// TODO load in map file
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

		// this gives you a 2-dimensional array of strings
		List<List<String>> lines = new ArrayList<>();
		String[][] maps = new String[40][40];
		String[][] closures = new String[40][40];
		int rows = 0;
		int cols = 0;

		Scanner inputStream = null;
		try {
			inputStream = new Scanner(file);

			while (inputStream.hasNext()) {
				String line = inputStream.next();
				String[] values = line.split(",");
				// this adds the currently parsed line to the 2-dimensional string array
				lines.add(Arrays.asList(values));
			}
			if (!lines.isEmpty()) {
				rows = lines.size();
				cols = lines.get(0).size();
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// the following code lets you iterate through the 2-dimensional array
		int lineNo = 0;
		boolean closuresSection = false;
		for (List<String> line : lines) {
			int columnNo = 0;
			for (String value : line) {
				if (value.equals("IntersectionClosures")) {
					closuresSection = true;
					columnNo = 1;
					lineNo = 1;
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
					System.out.print(maps[j][i] + "\t");
				}
			}
			System.out.print("\n");
		}
		
		System.out.print("Closure\n");
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (closures[j][i] != null) {
					System.out.print(closures[j][i] + "\t");
				}
			}
			System.out.print("\n");
		}

		map = new MapIntersection[8][8];
		IntStream.range(0, 8).forEach(i -> {
			IntStream.range(0, 8).forEach(j -> {
				MapIntersection temp = new MapIntersection();
				Road tempRoad = new Road();
				tempRoad.setName(String.valueOf(j + 1));
				tempRoad.setBidirectional(true);
				tempRoad.setDirection(Direction.NORTH);
				temp.setNSroad(tempRoad);

				tempRoad = new Road();
				tempRoad.setName(Character.toString(alphabet[i]));
				tempRoad.setBidirectional(true);
				tempRoad.setDirection(Direction.EAST);
				temp.setEWroad(tempRoad);
				map[i][j] = temp;
			});
		});
		this.homeBase = map[3][3];
	}

	public MapIntersection[][] getMap() {
		return map;
	}

	public MapIntersection getHomeBase() {
		return homeBase;
	}
}
