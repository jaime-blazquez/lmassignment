package es.bdh.lmassignment;

import org.hamcrest.Matchers;
import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests the database
 */
public class TestDatabase {

    // No result: it returns not null and empty
    @Test
    public void getFlightsNoResult() {
        Database db = new Database();
        Collection<Flight> flights = db.getFlights("CPH", "FRA");
        assertThat(flights, notNullValue());
        assertThat(flights, Matchers.hasSize(0));
    }

    // One result, some additional tests on result to see info it's well constructed
    @Test
    public void getFlightsSingle() {

        Database db = new Database();
        db.addAirline("IB", "Iberia");
        db.addAirline("U2", "EasyJet");
        db.addPassengerTypeFixedPrice("IB", PassengerType.INFANT, new BigDecimal("15.00"));
        db.addFlight("CPH","FRA","IB2818", new BigDecimal("186.00"));
        db.addFlight("CPH","LHR","U23631", new BigDecimal("152.00")); // Should not be returned

        Collection<Flight> flights = db.getFlights("CPH", "FRA");
        assertThat(flights.size(), equalTo(1));
        Flight flight = flights.iterator().next();
        assertThat(flight.getFlightCode(), equalTo("IB2818"));
        assertThat(flight.getBasePrice(), equalTo(new BigDecimal("186.00")));
        assertThat(flight.getAirline(), new HasPropertyWithValue<Airline>("code", is("IB")));
        assertThat(flight.getAirline().getFixedPricesByPassengerType().get(PassengerType.INFANT), equalTo(new BigDecimal("15.00")));
    }

    @Test
    public void getFlightsMultiple() {

        Database db = new Database();
        db.addAirline("U2", "EasyJet");
        db.addAirline("TK", "Turkish Airlines");
        db.addFlight("CPH","FCO","TK4667", new BigDecimal("137.00"));
        db.addFlight("CPH","FCO","U24631", new BigDecimal("268.00"));
        db.addFlight("CPH","BCN","U22593", new BigDecimal("218.00")); // Should not be returned

        Collection<Flight> flights = db.getFlights("CPH", "FCO");
        assertThat(flights.size(), equalTo(2));

        assertThat(flights, hasItem(new HasPropertyWithValue<Flight>("flightCode", is("TK4667"))));
        assertThat(flights, hasItem(new HasPropertyWithValue<Flight>("flightCode", is("U24631"))));
    }

}
