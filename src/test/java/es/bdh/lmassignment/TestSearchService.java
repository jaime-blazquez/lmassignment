package es.bdh.lmassignment;

import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.bdh.lmassignment.PassengerType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

/**
 *  Tests the whole service. Includes the example data & tests.
 */
public class TestSearchService {

    private Database db;
    private PriceService priceService;

    public TestSearchService() {
        db = new Database();
        db.addAirport("MAD","Madrid");
        db.addAirport("BCN","Barcelona");
        db.addAirport("LHR","London");
        db.addAirport("CDG","Paris");
        db.addAirport("FRA","Frakfurt");
        db.addAirport("IST","Istanbul");
        db.addAirport("AMS","Amsterdam");
        db.addAirport("FCO","Rome");
        db.addAirport("CPH","Copenhagen");

        db.addAirline("IB", "Iberia");
        db.addPassengerTypeFixedPrice("IB", INFANT, new BigDecimal("10.00"));
        db.addAirline("BA", "British Airways");
        db.addPassengerTypeFixedPrice("BA", INFANT, new BigDecimal("15.00"));
        db.addAirline("LH", "Lufthansa");
        db.addPassengerTypeFixedPrice("LH", INFANT, new BigDecimal("7.00"));
        db.addAirline("FR", "Ryanair");
        db.addPassengerTypeFixedPrice("FR", INFANT, new BigDecimal("20.00"));
        db.addAirline("VY", "Vueling");
        db.addPassengerTypeFixedPrice("VY", INFANT, new BigDecimal("10.00"));
        db.addAirline("TK", "Turkish Airlines");
        db.addPassengerTypeFixedPrice("TK", INFANT, new BigDecimal("5.00"));
        db.addAirline("U2", "Easyjet");
        db.addPassengerTypeFixedPrice("U2", INFANT, new BigDecimal("19.90"));

        db.addFlight("CPH","FRA","IB2818", new BigDecimal("186.00"));
        db.addFlight("CPH","LHR","U23631", new BigDecimal("152.00"));
        db.addFlight("CDG","MAD","IB8482", new BigDecimal("295.00"));
        db.addFlight("BCN","FRA","FR7521", new BigDecimal("150.00"));
        db.addFlight("CPH","FCO","TK4667", new BigDecimal("137.00"));
        db.addFlight("CPH","FCO","U24631", new BigDecimal("268.00"));
        db.addFlight("FCO","CDG","VY4335", new BigDecimal("158.00"));
        db.addFlight("LHR","IST","TK8891", new BigDecimal("250.00"));
        db.addFlight("FRA","AMS","U24107", new BigDecimal("237.00"));
        db.addFlight("CPH","BCN","U22593", new BigDecimal("218.00"));
        db.addFlight("BCN","IST","VY9890", new BigDecimal("178.00"));
        db.addFlight("AMS","CPH","TK4927", new BigDecimal("290.00"));
        db.addFlight("FCO","MAD","BA1164", new BigDecimal("118.00"));
        db.addFlight("CPH","LHR","BA7710", new BigDecimal("138.00"));
        db.addFlight("BCN","AMS","U24985", new BigDecimal("191.00"));
        db.addFlight("MAD","CDG","IB9961", new BigDecimal("128.00"));
        db.addFlight("LHR","FRA","LH2118", new BigDecimal("165.00"));
        db.addFlight("IST","FRA","IB8911", new BigDecimal("180.00"));
        db.addFlight("AMS","FRA","TK2372", new BigDecimal("197.00"));
        db.addFlight("FRA","IST","LH4145", new BigDecimal("169.00"));
        db.addFlight("MAD","CDG","IB6112", new BigDecimal("112.00"));
        db.addFlight("CPH","FRA","LH1678", new BigDecimal("298.00"));
        db.addFlight("LHR","CPH","LH6620", new BigDecimal("217.00"));
        db.addFlight("MAD","LHR","TK4199", new BigDecimal("186.00"));
        db.addFlight("MAD","CDG","IB7403", new BigDecimal("253.00"));
        db.addFlight("FRA","CPH","BA4369", new BigDecimal("109.00"));
        db.addFlight("BCN","MAD","IB2171", new BigDecimal("259.00"));
        db.addFlight("IST","LHR","LH6412", new BigDecimal("197.00"));
        db.addFlight("IST","MAD","LH1115", new BigDecimal("160.00"));
        db.addFlight("LHR","LHR","VY8162", new BigDecimal("285.00"));
        db.addFlight("FRA","LHR","BA8162", new BigDecimal("205.00"));
        db.addFlight("AMS","FCO","BA7610", new BigDecimal("168.00"));
        db.addFlight("LHR","IST","LH1085", new BigDecimal("148.00"));
        db.addFlight("FCO","FRA","U21423", new BigDecimal("274.00"));
        db.addFlight("CPH","MAD","U23282", new BigDecimal("113.00"));
        db.addFlight("CDG","CPH","LH5778", new BigDecimal("263.00"));
        db.addFlight("CPH","CDG","BA2777", new BigDecimal("284.00"));
        db.addFlight("BCN","LHR","TK4375", new BigDecimal("208.00"));
        db.addFlight("MAD","FCO","LH8408", new BigDecimal("149.00"));
        db.addFlight("AMS","IST","IB4563", new BigDecimal("109.00"));
        db.addFlight("LHR","FCO","LH5174", new BigDecimal("251.00"));
        db.addFlight("MAD","BCN","BA9569", new BigDecimal("232.00"));
        db.addFlight("AMS","FRA","TK2659", new BigDecimal("248.00"));
        db.addFlight("LHR","CDG","IB2771", new BigDecimal("289.00"));
        db.addFlight("IST","MAD","IB8688", new BigDecimal("150.00"));
        db.addFlight("CPH","AMS","TK8355", new BigDecimal("137.00"));
        db.addFlight("FCO","CDG","VY2974", new BigDecimal("111.00"));
        db.addFlight("AMS","FRA","LH5909", new BigDecimal("113.00"));
        db.addFlight("CPH","BCN","FR7949", new BigDecimal("176.00"));
        db.addFlight("BCN","CPH","U27858", new BigDecimal("237.00"));
        db.addFlight("FRA","AMS","LH2320", new BigDecimal("288.00"));
        db.addFlight("LHR","BCN","VY4633", new BigDecimal("149.00"));
        db.addFlight("AMS","IST","IB7289", new BigDecimal("163.00"));
        db.addFlight("FRA","LHR","IB9443", new BigDecimal("254.00"));
        db.addFlight("IST","FCO","LH4948", new BigDecimal("176.00"));
        db.addFlight("IST","BCN","TK5558", new BigDecimal("211.00"));
        db.addFlight("BCN","BCN","BA9409", new BigDecimal("215.00"));
        db.addFlight("IST","AMS","FR9261", new BigDecimal("267.00"));
        db.addFlight("CDG","IST","IB7181", new BigDecimal("227.00"));
        db.addFlight("LHR","BCN","TK1446", new BigDecimal("217.00"));
        db.addFlight("FCO","FRA","TK2793", new BigDecimal("175.00"));
        db.addFlight("AMS","CPH","FR1491", new BigDecimal("284.00"));
        db.addFlight("IST","BCN","IB9219", new BigDecimal("279.00"));
        db.addFlight("MAD","AMS","TK7871", new BigDecimal("159.00"));
        db.addFlight("FCO","AMS","VY4840", new BigDecimal("260.00"));
        db.addFlight("MAD","FRA","BA8982", new BigDecimal("171.00"));
        db.addFlight("IST","LHR","U23526", new BigDecimal("254.00"));
        db.addFlight("FRA","MAD","BA6773", new BigDecimal("157.00"));
        db.addFlight("CDG","CPH","IB5257", new BigDecimal("299.00"));
        db.addFlight("CPH","CDG","LH8545", new BigDecimal("230.00"));
        db.addFlight("LHR","AMS","IB4737", new BigDecimal("110.00"));
        db.addFlight("BCN","MAD","LH5496", new BigDecimal("293.00"));
        db.addFlight("CDG","LHR","U29718", new BigDecimal("103.00"));
        db.addFlight("LHR","AMS","BA9561", new BigDecimal("253.00"));
        db.addFlight("FRA","LHR","TK3167", new BigDecimal("118.00"));
        db.addFlight("IST","FRA","FR4727", new BigDecimal("108.00"));
        db.addFlight("CPH","IST","LH6320", new BigDecimal("115.00"));
        db.addFlight("LHR","AMS","BA6657", new BigDecimal("122.00"));
        db.addFlight("LHR","FRA","TK5342", new BigDecimal("295.00"));
        db.addFlight("IST","LHR","IB4938", new BigDecimal("226.00"));
        db.addFlight("CDG","BCN","VY9791", new BigDecimal("289.00"));
        db.addFlight("MAD","LHR","IB4124", new BigDecimal("272.00"));
        db.addFlight("FRA","MAD","BA7842", new BigDecimal("121.00"));
        db.addFlight("AMS","FCO","VY5092", new BigDecimal("178.00"));
        db.addFlight("CDG","LHR","BA9813", new BigDecimal("171.00"));
        db.addFlight("FRA","IST","BA2421", new BigDecimal("226.00"));
        db.addFlight("IST","CPH","U28059", new BigDecimal("262.00"));
        db.addFlight("MAD","AMS","LH7260", new BigDecimal("191.00"));
        db.addFlight("CDG","CPH","TK2044", new BigDecimal("186.00"));

        priceService = new PriceService();
        priceService.addDaysToDepartureRule(0, 2, new BigDecimal("1.50"));
        priceService.addDaysToDepartureRule(3, 15, new BigDecimal("1.20"));
        priceService.addDaysToDepartureRule(16, 30, new BigDecimal("1.00"));
        priceService.addDaysToDepartureRule(31, Integer.MAX_VALUE, new BigDecimal("0.80"));

        priceService.addPassengerTypeRule(CHILD, new BigDecimal("0.67"));
    }

    @Test
    public void assignmentExamples() throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date today = df.parse("20170101");

        SearchService srv = new SearchService(db, priceService);
        {
            /*
                1 adult, 31 days to the departure date, flying AMS -> FRA
                flights:
                    * TK2372, 157.6 €
                    * TK2659, 198.4 €
                    * LH5909, 90.4 €
            */
            List<PassengersByType> passengers = new LinkedList<>();
            passengers.add(new PassengersByType(ADULT, 1));
            Collection<SearchService.SearchResult> flights = srv.search("AMS", "FRA", today, df.parse("20170201"), passengers);
            assertThat(flights, hasSize(3));
            assertThat(flights, hasItem(allOf(
                    new HasPropertyWithValue<SearchService.SearchResult>("flightCode", is("TK2372")),
                    new HasPropertyWithValue<SearchService.SearchResult>("price", is(new BigDecimal("157.60")))
            )));
            assertThat(flights, hasItem(allOf(
                    new HasPropertyWithValue<SearchService.SearchResult>("flightCode", is("TK2659")),
                    new HasPropertyWithValue<SearchService.SearchResult>("price", is(new BigDecimal("198.40")))
            )));
            assertThat(flights, hasItem(allOf(
                    new HasPropertyWithValue<SearchService.SearchResult>("flightCode", is("LH5909")),
                    new HasPropertyWithValue<SearchService.SearchResult>("price", is(new BigDecimal("90.40")))
            )));
        }
        {
            /*
                2 adults, 1 child, 1 infant, 15 days to the departure date, flying LHR -> IST
                flights:
                    * TK8891, 806 € (2 * (120% of 250) + 67% of (120% of 250) + 5)
                    * LH1085, 481.19 € (2 * (120% of 148) + 67% of (120% of 148) + 7)
            */
            List<PassengersByType> passengers = new LinkedList<>();
            passengers.add(new PassengersByType(ADULT, 2));
            passengers.add(new PassengersByType(CHILD, 1));
            passengers.add(new PassengersByType(INFANT, 1));
            Collection<SearchService.SearchResult> flights = srv.search("LHR", "IST", today, df.parse("20170116"), passengers);
            assertThat(flights, hasSize(2));
            assertThat(flights, hasItem(allOf(
                    new HasPropertyWithValue<SearchService.SearchResult>("flightCode", is("TK8891")),
                    new HasPropertyWithValue<SearchService.SearchResult>("price", is(new BigDecimal("806.00")))
            )));
            assertThat(flights, hasItem(allOf(
                    new HasPropertyWithValue<SearchService.SearchResult>("flightCode", is("LH1085")),
                    new HasPropertyWithValue<SearchService.SearchResult>("price", is(new BigDecimal("481.19")))
            )));

        }
        {
            /*
                1 adult, 2 children, 2 days to the departure date, flying BCN -> MAD
                flights:
                    * IB2171, 909.09 € (150% of 259 + 2 * 67% of (150% of 259))
                    * LH5496, 1028.43 € (150% of 293 + 2 * 67% of (150% of 293))
             */
            List<PassengersByType> passengers = new LinkedList<>();
            passengers.add(new PassengersByType(ADULT, 1));
            passengers.add(new PassengersByType(CHILD, 2));
            Collection<SearchService.SearchResult> flights = srv.search("BCN", "MAD", today, df.parse("20170103"), passengers);
            assertThat(flights, hasSize(2));
            assertThat(flights, hasItem(allOf(
                    new HasPropertyWithValue<SearchService.SearchResult>("flightCode", is("IB2171")),
                    new HasPropertyWithValue<SearchService.SearchResult>("price", is(new BigDecimal("909.09")))
            )));
            assertThat(flights, hasItem(allOf(
                    new HasPropertyWithValue<SearchService.SearchResult>("flightCode", is("LH5496")),
                    new HasPropertyWithValue<SearchService.SearchResult>("price", is(new BigDecimal("1028.43")))
            )));

        }
        {
            /* CDG -> FRA
               no flights available
            */
            List<PassengersByType> passengers = new LinkedList<>();
            passengers.add(new PassengersByType(ADULT, 1));
            Collection<SearchService.SearchResult> result = srv.search("CDG", "FRA", today, df.parse("20170115"), passengers);
            assertTrue(result.isEmpty());
        }
    }

}
