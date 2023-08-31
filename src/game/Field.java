package game;

import java.util.List;
import java.util.Arrays;
import java.util.logging.Logger;

public class Field {
    private Logger log = Logger.getLogger("game.Field");
    private String[][] area = new String[10][10];
    private Ship[] ships;
    private final String numericEmoji = "\uFE0F";
    public static final String emptySpaceEmoji = "⬜";
    public static final String missedTargetEmoji = "❌";
    public static final String beatenShipEmoji = "\uD83E\uDEA6";

    public String[][] getArea() {
        return area;
    }

    public void changeArea(int[] coordinates, String emoji) {
        area[ coordinates[0] ][ coordinates[1] ] = emoji;
    }

    public Field() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                area[i][j] = emptySpaceEmoji;
            }
        }
    }

    public void draw() {
        System.out.print("\uD83D\uDFE6");
        for (int i = 0; i < 10; i++) {
            System.out.print(i+numericEmoji);
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print(i+"\uFE0F");
            for (int j = 0; j < 10; j++) {
                System.out.print(area[j][i]);
            }
            System.out.println();
        }
    }

    public void drawForEnemy() {
        System.out.print("\uD83D\uDFE6");
        for (int i = 0; i < 10; i++) {
            System.out.print(i+numericEmoji);
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print(i+"\uFE0F");
            for (int j = 0; j < 10; j++) {
                if (area[j][i].equals(Ship.getEmoji()) || area[j][i].equals(emptySpaceEmoji)) {
                  System.out.print(emptySpaceEmoji);
                } else {
                    System.out.print(area[j][i]);
                }
            }
            System.out.println();
        }
    }

    public boolean addShip(Ship ship)  {
        if (checkCoordinates(ship.getCoordinates(), ship.getLength())) {
            for (int i = 0; i < ship.getLength(); i++) {
                int x = ship.getCoordinates().get(i).get(0);
                int y = ship.getCoordinates().get(i).get(1);
                area[x][y] = ship.getEmoji();
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean checkCoordinates(List<List<Integer>> coordinates, int length) {
        int[][] spaceAroundShip = new int[length*9][2];
        int index = 0;
        for (int i = 0; i < length; i++) {
            int x = coordinates.get(i).get(0);
            int y = coordinates.get(i).get(1);
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int yOffset = -1; yOffset <= 1; yOffset++) {
                    int newX = x + xOffset;
                    int newY = y + yOffset;
                    spaceAroundShip[index++] = new int[]{newX, newY};
                }
            }
        }
        for (int[] block : spaceAroundShip) {
            if (block[0] < 0 || block[0] > 9 || block[1] < 0 || block[1] > 9) {
                continue;
            }
            if (coordinates.contains(Arrays.asList(block))) {
                continue;
            }
            if (area[block[0]][block[1]].equals("\uD83D\uDEA2")) {
                return false;
            }
        }
        return true;
    }
}
