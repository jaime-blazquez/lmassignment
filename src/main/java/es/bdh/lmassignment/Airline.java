package es.bdh.lmassignment;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity class for an Airline
 */
public class Airline {

    private final String code;
    private final String name;
    private final Map<PassengerType, BigDecimal> fixedPrices;

    /**
     * Creates a new airline
     *
     * @param code IATA assigned code
     * @param name Commercial or descriptive name
     */

    public Airline(String code, String name) {
        this.code = code;
        this.name = name;
        this.fixedPrices = new HashMap<>();
    }

    /**
     * Retrieves the IATA assigned code
     * @return IATA code
     */
    public String getCode() {
        return code;
    }

    /**
     * Retrieves the commercial, brand or descriptive name
     * @return Descriptive name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a fixed price for a passenger type.
     *
     * When a customer buys a ticket for a passenger of this type, the price is not route-dependent, but fixed.
     * @param type Passenger type
     * @param price Fixed price
     */
    public void setFixedPriceByPassengerType(PassengerType type, BigDecimal price) {
        fixedPrices.put(type, price);
    }

    /**
     * Retrieves a copy of those prices fixed for a passenger type
     * @return Map of fixed prices indexed by passenger type. Not modifiable.
     */
    public Map<PassengerType, BigDecimal> getFixedPricesByPassengerType() {
        return Collections.unmodifiableMap(fixedPrices);
    }

}
