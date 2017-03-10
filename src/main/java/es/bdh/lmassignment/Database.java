package es.bdh.lmassignment;

import java.math.BigDecimal;
import java.util.*;

/**
 * Persistence layer abstraction.
 *
 * Simple in-memory implementation suited only for the search flights use case.
 * All data in the system is created/added through this class methods.
 */
class Database {

    private final Map<String, Airline> airlines;
    private final Map<String, String> airports;
    private final Map<String, List<Flight>> flights;

    public Database() {
        airlines = new HashMap<>();
        airports = new HashMap<>();
        flights = new HashMap<>();

    }

    /**
     * Creates a new airline
     *
     * @param code IATA assigned code.
     * @param name Descriptive name.
     * @see Airline
     */
    public void addAirline(String code, String name) {
        if(code == null || name == null) { throw new IllegalArgumentException("All params are mandatory"); }
        Airline airline = new Airline(code, name);
        airlines.put(code, airline);
    }

    /**
     * Adds a new fixed price for an specific airline and passenger type
     *
     * @param airlineCode IATA code of the airline
     * @param type Passenger type
     * @param price Fixed price
     */
    public void addPassengerTypeFixedPrice(String airlineCode, PassengerType type, BigDecimal price) {
        if(airlineCode == null || type == null || price == null) { throw new IllegalArgumentException("All params are mandatory"); }
        Airline airline = airlines.get(airlineCode);
        if(airline == null) { throw new IllegalArgumentException(String.format("Airline %s not found", airlineCode)); }
        airline.setFixedPriceByPassengerType(type, price);
    }

    /**
     * Creates a new airport
     *
     * @param code IATA assigned code
     * @param name Descriptive name
     */
    public void addAirport(String code, String name) {
        if(code == null || name == null) { throw new IllegalArgumentException("All params are mandatory"); }
        airports.put(code, name);
    }

    /**
     * Creates a new flight
     *
     * @param originCode IATA code of the origin airport
     * @param destCode IATA code of the destination airport
     * @param flightCode IATA code of the flight. Assumes AAN* (AA -> airline, N* -> rest of identifier)
     * @param basePrice Base price for price rules
     */
    public void addFlight(String originCode, String destCode, String flightCode, BigDecimal basePrice) {
        if(originCode == null || destCode == null || flightCode == null || basePrice == null) { throw new IllegalArgumentException("All params are mandatory"); }
        // Flight creation
        String airlineCode = flightCode.substring(0,2);
        Airline airline = airlines.get(airlineCode);
        if(airline == null) { throw new IllegalArgumentException(String.format("Airline %s not found", airlineCode)); }
        Flight flight = new Flight(originCode, destCode, airline, flightCode, basePrice);

        // Add to route
        String routeCode = routeCode(originCode, destCode); // unique identifier for route
        List<Flight> flightsByRoute;
        if(flights.containsKey(routeCode)) {
            flightsByRoute = flights.get(routeCode);
        } else {
            flightsByRoute = new LinkedList<>();
            flights.put(routeCode, flightsByRoute);
        }
        flightsByRoute.add(flight);
    }

    /**
     * Retrieves the flights for a specific route
     * @param originCode IATA code of the origin airport
     * @param destCode IATA code of the destination airport
     * @return Flights that match that route (no order guaranteed)
     */
    public Collection<Flight> getFlights(String originCode, String destCode) {
        String routeCode = routeCode(originCode, destCode);
        Collection<Flight> routeFlights = flights.get(routeCode);
        return (routeFlights == null) ? Collections.<Flight>emptyList() : Collections.unmodifiableCollection(routeFlights);
    }

    private static String routeCode(String origin, String dest) {
        return origin+'-'+dest;
    }


}
