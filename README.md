Assignment Solution
===================

The solution implements barely the described use case and it's tests.

It makes the following assumptions and choices:

- As 'only the Java language and no libraries except for testing', I've not implemented
  anything Java EE nor equivalent. It's only POJOs, an informal on-memory database, and 
  a class with a simple interface, suited for the use case.
    
- I've implemented the following classes:

    - *Airline* and *Flight* are entity types that only store the data.
      I've not created an *Airport* entity, as it's not used at all in the assignment.
      Both contain some fields that are not used, as the IATA code of airline or the
      origin and destination on the airport (they are used to index, but not in the
      entity itself). These fields are left because they fit naturally and would be
      very probably used in related use cases (i.e.: a detailed listing of the search).
      
    - *PassengerType* it's an Enum with the age-related types described in the assignment.
      If additional reporting was done, or automatic classifying from age, the Enum could
      have additional properties, but they were not needed for the assignment. It also can
      be substituted with having type-specific fields in other classes, but that solution
      wasn't simpler and it was less flexible.
      
    - *PassengersByType* is a simple object to group how many passengers of each type
      they are. Again, it could have been substituted with additional parameters for each
      type, but it wasn't simpler and it's less flexible.
      
    - *Database* is the entry point for the persistence (although of the persistence layer
      in this assignment is read-only except for initialization). To make implementation
      simpler, it stores the flights grouped by source-destination pairs, but it's only
      an implementation decision, that could be changed when requisites grow. The only query
      funcionality is ask for the flights for a given route (source-destination).
      
    - *PriceService* is an engine that stores the rules that modify the base prices.
      I think it's better suited apart from the SearchService because of functionality and
      testing. I didn't made it more flexible (applying any kind of rule) because that would
      have required at least three more classes (for each kind of rule), more interface and
      probably some kind of order/precedence management.
      
    - *SearchService* is the entry point for the assignment. It takes the search parameters,
      retrieves the flights from the database and calls the price service for each flight.
    
- The "infant uses fixed price" rule is implemented as a "some types of passengers may
  have a fixed price" applied in the example only to infants. It supports changes to the
  types and some airlines have that policy or not. I believe that the complexity was similar
  to have a "infantPrice" field in the airline, given the on-memory storage. If storage
  would have been a relational-database, the field would have been easier, however.
  
- Language & API level used has been Java 1.7. Java 1.8 would have a small impact (mostly on
  search methods), so I preferred a broader target.

- Tests have been implemented with JUnit & Hamcrest.
