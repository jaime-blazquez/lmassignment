package es.bdh.lmassignment;

import java.math.BigDecimal;

/**
 * Entity class for a flight
 */
public class Flight {

    private final String     originAirport;
    private final String     destAirport;
    private final Airline    airline;
    private final String     flightCode;
    private final BigDecimal basePrice;

    /**
     * Creates a new Flight object
     *
     * @param originAirport IATA flightCode of the origin/source airport
     * @param destAirport IATA flightCode of the destination airport
     * @param airline Airline that operates the flight
     * @param flightCode IATA flightCode of the flight
     * @param basePrice Base price for price rules
     */
    public Flight(String originAirport, String destAirport, Airline airline, String flightCode, BigDecimal basePrice) {
        this.originAirport = originAirport;
        this.destAirport = destAirport;
        this.airline = airline;
        this.flightCode = flightCode;
        this.basePrice = basePrice;
    }

    /**
     * Gets the origin airport flightCode
     * @return IATA flightCode of the origin airport
     */
    public String getOriginAirport() {
        return originAirport;
    }

    /**
     * Gets the destination airport flightCode
     * @return IATA flightCode of the destination airport
     */
    public String getDestAirport() {
        return destAirport;
    }

    /**
     * Gets the airline that operates this flight
     * @return Airline object
     */
    public Airline getAirline() {
        return airline;
    }

    /**
     * Gets the IATA code of the flight
     * @return IATA code
     */
    public String getFlightCode() {
        return flightCode;
    }

    /**
     * Gets the base price to which price rules are applied
     * @return Base price
     */
    public BigDecimal getBasePrice() {
        return basePrice;
    }

}
