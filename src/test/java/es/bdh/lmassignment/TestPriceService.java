package es.bdh.lmassignment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.*;

import org.junit.Test;

import static es.bdh.lmassignment.PassengerType.ADULT;
import static es.bdh.lmassignment.PassengerType.CHILD;
import static es.bdh.lmassignment.PassengerType.INFANT;
import static org.junit.Assert.fail;


/**
 * Tests the price service
 */
public class TestPriceService {

    private static final BigDecimal BASE_PRICE_100 = new BigDecimal("100.00");

    // NO RULES APPLIED

    @Test
    public void noRuleReturnsBasePrice() {
        PriceService engine = new PriceService();

        Set<PassengersByType> passengers = new HashSet<>();
        passengers.add(new PassengersByType(ADULT, 1));
        BigDecimal price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 1, passengers);

        assertThat(price, equalTo(BASE_PRICE_100));
    }

    @Test
    public void noRuleUsesNumberOfPassengers() {
        PriceService engine = new PriceService();

        Set<PassengersByType> passengers = new HashSet<>();
        passengers.add(new PassengersByType(ADULT, 2));
        passengers.add(new PassengersByType(CHILD, 3));
        BigDecimal price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 1, passengers);

        assertEquals(new BigDecimal("500.00"), price); // 100€ * 2 adult + 100€ * 3 child
    }

    // RULES APPLIED INDIVIDUALLY

    @Test
    public void daysToDeparture() {
        PriceService engine = new PriceService();
        engine.addDaysToDepartureRule(0, 2, new BigDecimal("1.50"));
        engine.addDaysToDepartureRule(3, 15, new BigDecimal("1.20"));
        engine.addDaysToDepartureRule(16, 30, new BigDecimal("1.00"));
        engine.addDaysToDepartureRule(31, Integer.MAX_VALUE, new BigDecimal("0.80"));

        Set<PassengersByType> passengers = new HashSet<>();
        passengers.add(new PassengersByType(ADULT, 1));

        BigDecimal price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 2, passengers);
        assertThat(price, equalTo(new BigDecimal("150.00")));

        price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 3, passengers);
        assertEquals(new BigDecimal("120.00"), price);

        price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 16, passengers);
        assertEquals(new BigDecimal("100.00"), price);

        price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 31, passengers);
        assertEquals(new BigDecimal("80.00"), price);
    }

    @Test
    public void passengerTypeDiscount() {
        PriceService engine = new PriceService();
        engine.addPassengerTypeRule(CHILD, new BigDecimal("0.67"));

        // NO DISCOUNT
        Set<PassengersByType> passengers = new HashSet<>();
        passengers.add(new PassengersByType(ADULT, 1));

        BigDecimal price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 1, passengers);
        assertThat(price, equalTo(new BigDecimal("100.00")));

        // DISCOUNT
        passengers = new HashSet<>();
        passengers.add(new PassengersByType(CHILD, 1));

        price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 1, passengers);
        assertThat(price, equalTo(new BigDecimal("67.00")));
    }

    @Test
    public void passengerTypeFixed() {
        PriceService engine = new PriceService();

        // NO DISCOUNT
        Set<PassengersByType> passengers = new HashSet<>();
        passengers.add(new PassengersByType(ADULT, 1));

        BigDecimal price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 1, passengers);
        assertThat(price, equalTo(new BigDecimal("100.00")));

        // FIXED
        passengers = new HashSet<>();
        passengers.add(new PassengersByType(INFANT, 1));
        Map<PassengerType, BigDecimal> fixedPrices = new HashMap<>();
        fixedPrices.put(INFANT, new BigDecimal("15.00"));

        price = engine.calculatePrice(BASE_PRICE_100, fixedPrices, 1, passengers);
        assertThat(price, equalTo(new BigDecimal("15.00")));
    }

    // RULES COMBINED

    @Test
    public void combinedDaysToDepartureAndPassengerType() {
        PriceService engine = new PriceService();
        engine.addDaysToDepartureRule(3, 15, new BigDecimal("1.20"));
        engine.addPassengerTypeRule(CHILD, new BigDecimal("0.67"));

        Set<PassengersByType> passengers = new HashSet<>();
        passengers.add(new PassengersByType(CHILD, 1));

        BigDecimal price = engine.calculatePrice(BASE_PRICE_100, Collections.EMPTY_MAP, 4, passengers);
        assertThat(price, equalTo(new BigDecimal("80.40")));
    }

    @Test
    public void combinedFixedOverridesDaysToDeparture() {
        PriceService engine = new PriceService();
        engine.addDaysToDepartureRule(3, 15, new BigDecimal("1.20"));

        Map<PassengerType, BigDecimal> fixedPrices = new HashMap<>();
        fixedPrices.put(INFANT, new BigDecimal("15.00"));

        Set<PassengersByType> passengers = new HashSet<>();
        passengers.add(new PassengersByType(INFANT, 1));

        BigDecimal price = engine.calculatePrice(BASE_PRICE_100, fixedPrices, 4, passengers);
        assertThat(price, equalTo(new BigDecimal("15.00")));
    }

    @Test
    public void combinedAll() {
        PriceService engine = new PriceService();
        engine.addDaysToDepartureRule(3, 15, new BigDecimal("1.20"));
        engine.addPassengerTypeRule(CHILD, new BigDecimal("0.67"));

        Map<PassengerType, BigDecimal> fixedPrices = new HashMap<>();
        fixedPrices.put(INFANT, new BigDecimal("15.00"));

        Set<PassengersByType> passengers = new HashSet<>();
        passengers.add(new PassengersByType(ADULT, 2));
        passengers.add(new PassengersByType(CHILD, 3));
        passengers.add(new PassengersByType(INFANT, 1));


        // Base price: 100 * 1.20 = 120.00
        // Adults  : 2 * 120.00          = 240.00
        // Children: 3 * (120.00 * 0.67) = 241.20
        // Infant  : 1 * 15              =  15.00
        //                                 ------
        //                                 496.20
        BigDecimal price = engine.calculatePrice(BASE_PRICE_100, fixedPrices, 4, passengers);
        assertThat(price, equalTo(new BigDecimal("496.20")));

        // Base price: 245 * 1 = 120.00
        // Adults  : 2 * 245.00          = 490.00
        // Children: 3 * (245.00 * 0.67) = 492.45
        // Infant  : 1 * 15              =  15.00
        //                                 ------
        //                                 997.45
        price = engine.calculatePrice(new BigDecimal("245.00"), fixedPrices, 17, passengers);
        assertThat(price, equalTo(new BigDecimal("997.45")));
    }

    // INVALID INPUTS

    @Test
    public void invalidDaysToDeparture() {
        PriceService engine = new PriceService();
        try {
            engine.addDaysToDepartureRule(4,3, BigDecimal.ONE);
            fail("Shouldn't allow min > max");
        } catch(IllegalArgumentException ex) {
            // NOP
        }

        try {
            engine.addDaysToDepartureRule(-1,3, BigDecimal.ONE);
            fail("Shouldn't allow negative days to departure");
        } catch(IllegalArgumentException ex) {
            // NOP
        }

        try {
            engine.addDaysToDepartureRule(1,3, null);
            fail("Shouldn't allow null percentages");
        } catch(IllegalArgumentException ex) {
            // NOP
        }

        try {
            engine.addDaysToDepartureRule(1,3, new BigDecimal("-1.00"));
            fail("Shouldn't allow negative percentages");
        } catch(IllegalArgumentException ex) {
            // NOP
        }

    }

    @Test
    public void invalidPassengerTypeDiscount() {
        PriceService engine = new PriceService();
        try {
            engine.addPassengerTypeRule(null, BigDecimal.ONE);
            fail("Shoudn't allow null passenger types");
        } catch(IllegalArgumentException ex) {
            // NOP
        }

        try {
            engine.addPassengerTypeRule(CHILD, null);
            fail("Shouldn't allow null percentages");
        } catch(IllegalArgumentException ex) {
            // NOP
        }

        try {
            engine.addPassengerTypeRule(CHILD, new BigDecimal("-1.00"));
            fail("Shoudn't allow negative percentages");
        } catch(IllegalArgumentException ex) {
            // NOP
        }

    }

    // Runtime (calculatePrice) arguments are not tested as they should be validated on user input (date and number of passengers)
    //  or at airline or flight input (base price and fixed (infant) prices
}
