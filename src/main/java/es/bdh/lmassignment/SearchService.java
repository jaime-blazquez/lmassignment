package es.bdh.lmassignment;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Flight search facade
 */
public class SearchService {

    private final Database db;
    private final PriceService priceService;

    public class SearchResult {

        private final String flightCode;
        private final BigDecimal price;

        public SearchResult(String flightCode, BigDecimal price) {
            this.flightCode = flightCode;
            this.price = price;
        }

        public String getFlightCode() {
            return flightCode;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }

    /**
     * Creates an instance of the service
     *
     * @param db Database with flights, airlines, airports...
     * @param priceService Price calculating engine
     */
    public SearchService(Database db, PriceService priceService) {
        this.db = db;
        this.priceService = priceService;
    }

    /**
     * Search flights for a give route, day and passenger set.
     *
     * @param originCode IATA code for origin airport
     * @param destCode IATA code for destination airport
     * @param departure Departure date
     * @param passengers Collection of [Type, Num. of passengers] tuples describing the people flying.
     * @return Collection of SearchResult (Flight code + price)
     */
    public Collection<SearchResult> search(String originCode, String destCode, Date departure, Collection<PassengersByType> passengers) {
        return search(originCode, destCode, new Date(), departure, passengers);
    }

    /**
     * Search flights for a give route, day and passenger set.
     *
     * This method adds a 'today' parameter to be used as today date for calculations.
     * It is not expected to be the interface, but it's provided with package visibily to make testing easier.
     *
     * @param originCode IATA code for origin airport
     * @param destCode IATA code for destination airport
     * @param today Date used as today date
     * @param departure Departure date
     * @param passengers Collection of [Type, Num. of passengers] tuples describing the people flying.
     * @return Collection of SearchResult (Flight code + price)
     */
    Collection<SearchResult> search(String originCode, String destCode, Date today, Date departure, Collection<PassengersByType> passengers) {
        List<SearchResult> result = new LinkedList<>();
        Collection<Flight> flights = db.getFlights(originCode, destCode);

        int daysToDeparture = calcDaysBetweenDates(today, departure);

        if(flights != null) {
            for(Flight flight : flights) {
                BigDecimal price = priceService.calculatePrice(flight.getBasePrice(), flight.getAirline().getFixedPricesByPassengerType(), daysToDeparture, passengers);
                result.add(new SearchResult(flight.getFlightCode(), price));
            }
        }
        return result;
    }

    private int calcDaysBetweenDates(Date today, Date departure) {
        Date todayNoon = startOfDay(today);
        long diff = departure.getTime() - todayNoon.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private Date startOfDay(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
