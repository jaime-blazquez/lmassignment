package es.bdh.lmassignment;

/**
 * Utility class to group how many passengers of each type want to fly
 */
public class PassengersByType {

    private final PassengerType type;
    private final int number;

    /**
     * Creates a new type/number of passenger tuple
     * @param type Passenger type
     * @param number Number of passengers of that type that want to fly
     */
    public PassengersByType(PassengerType type, int number) {
        this.type = type;
        this.number = number;
    }

    /**
     * Retrieves the type of this passenger group
     * @return Passenger type
     */
    public PassengerType getType() {
        return type;
    }

    /**
     * Retrieves the number of passengers of this passenger group
     * @return Number of passengers
     */
    public int getNumber() {
        return number;
    }
}
