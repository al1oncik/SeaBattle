package exceptions.ShipExceptions;

public class InvalidShipLength extends Exception {
    public InvalidShipLength() {
        super();
    }
    public InvalidShipLength(String message) {
        super(message);
    }
    public InvalidShipLength(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidShipLength(Throwable cause) {
        super(cause);
    }


}
