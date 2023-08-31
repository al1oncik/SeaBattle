package game;

import java.util.List;
import java.util.logging.Logger;
import exceptions.ShipExceptions.InvalidShipLength;

public class Ship {
    private Logger log = Logger.getLogger("game.Ship");
    private static final String emoji = "\uD83D\uDEA2";

    public static String getEmoji() {
        return emoji;
    }

    private int length;
    public int getLength() {
        return length;
    }
    List<List<Integer>> coordinates;

    public List<List<Integer>> getCoordinates() {
        return coordinates;
    }

    public Ship(List<List<Integer>> coordinates, int length) throws InvalidShipLength {
        this.coordinates = coordinates;
        if (!(length > 0 & length < 5)) {
            throw new InvalidShipLength("Invalid Ship Length");
        }
        this.length = length;
    }
}
