package es.bdh.lmassignment;

import java.math.BigDecimal;
import java.util.*;

/**
 * Utility that keeps price rules and calculates flight prices according to them.
 *
 * Currently it applies the following kind of rules:
 * - Fixed prices (external) -> Fixes the price for a passenger if it's type has a fixed price
 * - Days to departure (internal) -> Applies a coefficient if the days to departure is inside a given range
 * - Passenger type (internal) -> Applies a coefficient if the passenger type matches
 */
class PriceService {

    private class DaysRule {
        private final int min;
        private final int max;
        private final BigDecimal coefficient;

        DaysRule(int min, int max, BigDecimal coefficient) {
            this.min = min;
            this.max = max;
            this.coefficient = coefficient;
        }
    }

    private final List<DaysRule> daysRules;
    private final Map<PassengerType, BigDecimal> typeRules;

    public PriceService() {
        daysRules = new LinkedList<>();
        typeRules = new HashMap<>();
    }

    /**
     * Adds a 'days to departure' rule.
     *
     * If the days to departure is between min and max (both included), applies the coefficient.
     *
     * @param min Minimum for the day range
     * @param max Maximum for the day range
     * @param coefficient Coefficient to be applied
     */
    public void addDaysToDepartureRule(int min, int max, BigDecimal coefficient) {
        if(min > max) { throw new IllegalArgumentException("in must be <= max"); }
        if(min < 0) { throw new IllegalArgumentException("Negative days to departure not allowed"); }
        if(coefficient == null) { throw new IllegalArgumentException("Percentage is mandatory"); }
        if(coefficient.compareTo(BigDecimal.ZERO) < 0) { throw new IllegalArgumentException("Negative percentages not allowed"); }

        DaysRule rule = new DaysRule(min, max, coefficient);
        daysRules.add(rule);
    }

    /**
     * Adds a 'passenger type' rule.
     *
     * If a passenger type is of the supplied type, applies the coefficient.
     *
     * @param type Passenger type
     * @param coefficient Coefficient to be applied
     */
    public void addPassengerTypeRule(PassengerType type, BigDecimal coefficient) {
        if(type == null) { throw new IllegalArgumentException("Passenger type is mandatory"); }
        if(coefficient == null) { throw new IllegalArgumentException("Percentage is mandatory"); }
        if(coefficient.compareTo(BigDecimal.ZERO) < 0) { throw new IllegalArgumentException("Negative percentages not allowed"); }

        typeRules.put(type, coefficient);
    }

    /**
     * Calculates a flight price based on price rules and flight data.
     *
     * It uses two sets of data:
     * - Price rules: With system scope, configured in this object
     * - Flight data: With flight scope, passed as parameter
     *
     * Precedence is the following (for each passenger group):
     * - First, looks for fixed prices and uses them
     * - Then, applies the matching 'days to departure' rule (if any)
     * - Then, applies the matching 'passenger type' rule (if any)
     * - Finally multiplies the resulting price by the number of passengers.
     *
     * @param basePrice Price used as base to apply the rules.
     * @param fixedPrices Map of fixed prices indexed by passenger type.
     * @param daysToDeparture Number of days to departure (used to match the 'days to departure' rules)
     * @param passengers Collection of [Type, Num. of passengers] tuples describing the people flying.
     * @return Price for all passengers
     */
    public BigDecimal calculatePrice(BigDecimal basePrice, Map<PassengerType, BigDecimal> fixedPrices,
                                     int daysToDeparture, Collection<PassengersByType> passengers) {

        BigDecimal result = BigDecimal.ZERO;

        for(PassengersByType passengersByType : passengers) {

            PassengerType type = passengersByType.getType();
            BigDecimal price;
            if(fixedPrices.containsKey(type)) {
                // Fixed prices override other calculations
                price = fixedPrices.get(type);
            } else {
                // Apply rules
                price = basePrice;
                for(DaysRule daysRule : daysRules) {
                    if(daysToDeparture >= daysRule.min && daysToDeparture <= daysRule.max) {
                        price = price.multiply(daysRule.coefficient);
                        break;
                    }
                }
                if(typeRules.containsKey(type)) {
                    price = price.multiply(typeRules.get(type));
                }
            }
            // Multiplied price by
            price = price.multiply(BigDecimal.valueOf(passengersByType.getNumber()));
            result = result.add(price);
        }
        return result.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

}
