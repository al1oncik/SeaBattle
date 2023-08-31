package game;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import exceptions.ShipExceptions.InvalidShipLength;

public class Main {
    private static Logger log = Logger.getLogger("game.Main");
    private static final Pattern pattern = Pattern.compile("^\\d,\\d(;\\d,\\d){0,3}$");
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.print("Player1 name: ");
        Player player1 = new Player(scanner.nextLine());

        System.out.print("Player2 name: ");
        Player player2 = new Player(scanner.nextLine());

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Collections.shuffle(players);

        players.get(0).setField(getField(players.get(0).getName()));
        players.get(1).setField(getField(players.get(1).getName()));

        boolean gameEnd = false;
        while (!gameEnd) {
            for (int i = 0,j = 1; i < 2; i++, j--) {
                while (true) {
                    System.out.println("Player " + players.get(i).getName() + "! Now is your turn to attack!");
                    players.get(j).getField().drawForEnemy();
                    int[] coordinates = getAttackingCoordinates();
                    String attackingResult = attack(players.get(i), players.get(j), coordinates);
                    if (attackingResult.equals("invalid")) {
                        continue;
                    }
                    else if (attackingResult.equals("beaten")) {
                        System.out.println("Nice! You beat the enemy ship!");
                        System.out.println("You can attack again");
                        continue;
                    }
                    else if (attackingResult.equals("missed")) {
                        System.out.println("You missed :(");
                        break;
                    }
                    break;
                }
                for (int enter = 0; enter < 100; enter++) {
                    System.out.println();
                }

            }
        }

    }

    private static Ship getShip(int length) throws InvalidShipLength, Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Print the coordinates of the ship with length " + length);
        String coordinates = scanner.nextLine();

        Matcher matcher = pattern.matcher(coordinates);
        if (!matcher.matches()) {
            System.err.println("Invalid Input!");
            return getShip(length);
        }
        else if (coordinates.length() != length*4-1) {
            System.err.println("Incorrect length of ship coordinates!");
            return getShip(length);
        }
        else {

            List<Integer> x = new ArrayList<>();
            List<Integer> y = new ArrayList<>();
            String[] coordinatesArr = coordinates.split(";");
            boolean allXMatching = true;
            boolean allYMatching = true;
            for (String i : coordinatesArr) {
                x.add(Integer.parseInt(i.split(",")[0]));
                y.add(Integer.parseInt(i.split(",")[1]));

                if (coordinates.length() > 3)  {
                    if (!(i.split(",")[0].equals(coordinatesArr[0].split(",")[0]))) {
                        allXMatching = false;
                    }
                    if (!(i.split(",")[1].equals(coordinatesArr[0].split(",")[1]))) {
                        allYMatching = false;
                    }
                } else {
                    allXMatching = false;
                }
            }
            if (allXMatching ^ allYMatching) {
                List<List<Integer>> xy = new ArrayList<>();
                for (int i = 0; i < x.size(); i++) {
                    xy.add(List.of(x.get(i),y.get(i)));
                }

                for (int i = 0; i < xy.size()-1; i ++) {
                    double distance = Math.sqrt(
                            Math.pow(xy.get(i).get(0)-xy.get(i+1).get(0), 2) +Math.pow(xy.get(i).get(1)-xy.get(i+1).get(1), 2)
                    );
                    if (distance != 1.0) {
                        System.err.println("Invalid ship coordinates!");
                        return getShip(length);
                    }
                }

                return new Ship(xy,length);
            }
            System.err.println("Ships must be located horizontally or vertically only!");
            return getShip(length);
        }
    }

    private static Field getField(String username) {
        System.out.println("Player "+username+"! Now is your turn to set your battle ships!");
        Field field = new Field();
        try {
            field.draw();
            for (int shipLength = 1; shipLength < 5; shipLength++) {
                for (int shipCount = 0; shipCount < 5-shipLength; shipCount++){
                    while (!(field.addShip(getShip(shipLength)))) {
                        System.err.println("Ships cannot be too close to each other!");
                    };
                    field.draw();
                }
            }
        } catch (InvalidShipLength isl) {
            System.err.println(isl.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        field.draw();
        for (int i = 0; i < 1000; i++) {
            System.out.println();
        }
        System.out.println("Player" +username+ " completed filling the battle ships");
        return field;
    }

    private static int[] getAttackingCoordinates() {
        System.out.print("Print the coordinates where you want to attack: ");
        String input = scanner.nextLine();
        if (input.equals("exit")) {
            System.exit(0);
        }
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches() & input.length() == 3) {
            int x = Integer.parseInt(input.split(",")[0]);
            int y = Integer.parseInt(input.split(",")[1]);
            return new int[] {x,y};
        }
        System.err.println("Invalid Input!");
        return getAttackingCoordinates();

    }

    private static String attack(Player p1, Player p2, int[] coordinates) {
        if (coordinates[0] > 9 || coordinates[0] < 0 || coordinates[1] > 9 || coordinates[1] < 0) {
            return "invalid";
        }
        if (p2.getField().getArea()[ coordinates[0] ][ coordinates[1] ].equals( Ship.getEmoji() )) {
            p2.getField().changeArea(coordinates, Field.beatenShipEmoji);
            checkShipsAround(p2, p1.getName(),coordinates);
            p2.removeShip();
            return "beaten";
        }
        else if (
                p2.getField().getArea()[ coordinates[0] ][ coordinates[1] ].equals( Field.missedTargetEmoji ) ||
                p2.getField().getArea()[ coordinates[0] ][ coordinates[1] ].equals( Field.beatenShipEmoji )
        ) {
            System.err.println("You cannot attack the same place twice!");
            return "invalid";
        } else {
            p2.getField().changeArea(coordinates, Field.missedTargetEmoji);
            return "You missed :(";
        }
    }

    private static void checkShipsAround(Player p2, String p1Name,int[] coordinates) {
        if (p2.getShipsLeft() == 0) {
            System.out.println("\n\n" + p1Name + " WIN!!!!!\n\n");
            System.exit(0);
        }
        System.out.println(p2.getShipsLeft());
        List<List<Integer>> spaceAround = new ArrayList<>();
        List<int[]> shipsCoordinates = new ArrayList<>();
        shipsCoordinates.add(coordinates);
        for (int i = 0; i < shipsCoordinates.size(); i++) {
            int x = shipsCoordinates.get(i)[0];
            int y = shipsCoordinates.get(i)[1];
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int yOffset = -1; yOffset <= 1; yOffset++) {
                    int newX = x + xOffset;
                    int newY = y + yOffset;
                    if (newX < 0 || newY < 0 || newX > 9 || newY > 9) {
                        continue;
                    }
                    if (p2.getField().getArea()[newX][newY].equals(Ship.getEmoji())) {
                        return;
                    }
                    else if (p2.getField().getArea()[newX][newY].equals(Field.beatenShipEmoji)) {
                        boolean contains = false;
                        for (int[] j : shipsCoordinates) {
                            if (j[0] == newX && j[1] == newY) {
                                contains = true;
                            }
                        }
                        if (!contains) {
                            shipsCoordinates.add(new int[]{newX, newY});
                        }
                    } else {
                        List<Integer> cord = new ArrayList<>();
                        cord.add(newX);
                        cord.add(newY);
                        spaceAround.add(cord);
                    }
                }
            }
        }
        System.out.println("Congratulations!!! You beat the hole ship! :D");
        for (int i = 0; i < spaceAround.size(); i++) {
            int x = spaceAround.get(i).get(0);
            int y = spaceAround.get(i).get(1);
            if (p2.getField().getArea()[x][y].equals(Field.emptySpaceEmoji)) {
                p2.getField().changeArea(new int[] {x,y}, Field.missedTargetEmoji);
            }
        }
    }
}












