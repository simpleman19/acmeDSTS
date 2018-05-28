package acme.pd;

import java.io.File;
import java.util.stream.IntStream;

public class Map {
    private MapIntersection[][] map;
    private MapIntersection homeBase;

    Map(File file) {
        // TODO load in map file
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        map = new MapIntersection[8][8];
        IntStream.range(0, 8).forEach(i -> {
                    IntStream.range(0, 8).forEach(j ->
                            {
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
                            }
                    );
                }
        );
        this.homeBase = map[3][3];
    }

    public MapIntersection[][] getMap() {
        return map;
    }

    public MapIntersection getHomeBase() {
        return homeBase;
    }
}
