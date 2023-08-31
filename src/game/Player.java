package game;

public class Player {
    private String name;

    public String getName() {
        return this.name;
    }
    private Field field;

    public void setField(Field field) {
        this.field = field;
        shipsLeft = 19;
    }
    public Field getField() {
        return this.field;
    }

    private int shipsLeft = 0;
    public int getShipsLeft() {
        return shipsLeft;
    }
    public void removeShip() {
        shipsLeft--;
    }

    public Player(String name) {
        this.name = name;
    }

}
