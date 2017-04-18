# BenchRestClient
Transaction rest client

--------------------------------------------------------------------------------------
### Requirements:
JVM 8

Maven

--------------------------------------------------------------------------------------
## Run instructions
### Running the application:
1. Pull the GitHub project.
2. On your system's terminal, navigate to the directory containing "pom.xml".
3. Issue `mvn package` to build the jar.
4. Navigate to the "target" folder with `cd target`.
4. Issue `java -jar BenchRestClient-1.0.0.jar`.

### Running JUnit tests:
1. Pull the GitHub project.
2. On your system's terminal, navigate to the directory containing "pom.xml".
3. Issue `mvn test`.

--------------------------------------------------------------------------------------
## Design considerations
### Overall considerations
- Use Java 8 to utilize the syntax simplicity. e.g. lambdas.
- Break up functionality into objects. Demonstrate OOP.
- Use Java native packages where possible, avoid third-party libraries if possible. 
This simplifies dependency management and binary size.
### Transaction.java
- This class models a transaction object. Each transaction object contains a 
transaction date and a transaction amount.
- This class holds 2 static variables (`s_transactionByDateCache` and `s_totalBalance`) 
used to keep track of the daily balance and the total balance. *Another option
could of been for other classes to keep track of all the transaction objects.
I didn't go that route because I felt it added more unnecessary code and complexity.
All parsed transaction need to be updated to the daily and total balance, therefore 
it made sense for the Transaction object to maintain this information.*
- `s_transactionByDateCache` uses a LinkedHashMap because ordering is required, and
O(1) access is required. TreeMap doesn't work because it provides O(logN) access.
HashMap doesn't work because sorting is not possible.
### TransactionHandler.java
- Google's json.simple library was used to parse JSON responses. There aren't
any native Java packages for parsing JSON, a third-party library was necessary.
As I'm writing this, I realized I should of chose GSON after further reading. 
It would of been the most appropriate considering the size of the transaction data. 
However I don't think it's a big concern. This is a good post in case you're interested: 
[http://blog.takipi.com/the-ultimate-json-library-json-simple-vs-gson-vs-jackson-vs-json/]
### TransactionRequest.java
- This class represents a HTTP request for retrieving transaction information.
- `HttpURLConnection` was used because it's a native Java package. *Apache's `HttpClient` 
was the other option I considered. It did contain more built in methods, but none of 
which were concern to me. It didn't justify me including `HttpClient` as a dependency.*

--------------------------------------------------------------------------------------
## Attempts
### Multithreading
I considered the possibility of large number of transactions broken into thousands
JSON pages, a bottleneck could occur at the network level. I wanted to implement the
application in a way to allow for multithreading, with each thread being responsible
for parsing a single HTTP request and response. I implemented this with Java Future
and Callable framework. However, I encountered synchronization issues. I was receiving
in consistent run results. After an hour of debugging, I decided the effort was not 
worth the performance gain. The code attempt can be seen on the __"supportThreads"__ branch.
### Transaction cache
I considered that in the case of large number of transactions, it's not wise to hold 
everything in memory. A cyclical cache of a size determined at run time (scale based on 
the number of system core for example) made sense. However, transaction records were
parsed in date descending, while daily balance is accumulated in date ascending. This
means reference to the first parsed record will need to be kept until the last
transaction was parsed. This would of defeated the justification for a cache. The
code attempt can be seen on the __"supportThreads"__ branch. *An option which would of 
made the cache idea possible would be to:*
*1. Scan how may JSON response pages there are.*
*2. Parse the response in reverse: From the bottom of the last response page, to the
top of the first response page.*
*This way both parsing and daily balance flows in an ascending order. Considering the
size of the data and the complexity of the assignment, I didn't feel I had enough
justification to implement this idea. I did think about the detail implementations
of this cache though, feel free to ask me if you're interested.*

--------------------------------------------------------------------------------------
## Limitations
- Possible OutOfMemoryException if the number of transactions is too large.

--------------------------------------------------------------------------------------
## Time breakdown
- Design, research, and initial POC: ~1.5H
- _"supportThread"_ branch implementation: ~2H
- _"supportThread"_ branch debugging: ~1H
- Final single threaded design implementation: ~1H
- JUnit testing: ~1H
- pom.xml ~0.5H
- Documentation: ~1H
- Git was not being friendly...: ~0.5H

Total: ~8.5H




