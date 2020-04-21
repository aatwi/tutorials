# Faster JPA Repository Testing in Spring Applications

In this project, I will demonstrate how to test the repository layer in a Spring
Application without using SpringBootTest that runs the Spring Framework for each 
test class.

The main purpose of this approach is to make the tests in our Spring Application 
run faster; thus, having a faster feedback loop.

## Tutorial Description

For this tutorial, we will be using a Spring application that performs simple DB
operations on a book repository. There will be no need to write any Controller
or Service code. 

### Project Structure 
The project contains the following Java classes: 
1. [Book.java](src\main\java\dev\aatwi\sjt\model\Book.java): This class defines the Book entity class.
1. [BookRepository.java](src\main\java\dev\aatwi\sjt\repository\BookRepository.java): This interface extends the 'JpaRepository' interface. 
In addition to the inherited functionalities, we have a new method that finds books by 'Author Name'.
1. [SpringBookRepositoryTest.java](src\test\java\dev\aatwi\sjt\repository\SpringBookRepositoryTest.java): This is a SpringBootTest, thus, it starts a Spring instance before running the tests. 
1. [NoSpringBookRepositoryTest.java](src\test\java\dev\aatwi\sjt\repository\NoSpringBookRepositoryTest.java): This test suite does not require running Spring when running tests. 

Both test classes above contains the same 8 test cases that cover the methods 
1. save
1. findAll
1. findById
1. findAllByAuthor
1. existsById
1. count
1. delete
1. deleteById 

**Note:** In reality, we don't need to unit test all the JpaRepository methods 
like I did here, since we can assume it is fully tested by the Spring Framework.
Nevertheless, the same logic can be applied for tests that require initializing 
an instance of the repository classes.  

## Problem
Spring simplifies writing the repository layer for us. By extending the interface 
'JpaRepository', we can benefit from a wide range of functions (ex: findOne, 
findAll, exists, etc.) without writing any SQL or Java code. 

This is all provided thanks to the 'Dependency Injection' done by the Spring 
Framework. However, the price we pay for that is in the tests; because to run 
any test, we need to start Spring. Thus, losing a lot of precious time!

### Illustration  
Let's analyze the problem! 

To run the class [SpringBookRepositoryTest.java](src\test\java\dev\aatwi\sjt\repository\SpringBookRepositoryTest.java), 
we need to: 
1. Tag the test with the '@SpringBootTest'annotation. This will be used to run
Spring Boot server before running the test.
1. Tag the BookRepository variable with '@Autowired' annotation. This will be 
used by Spring to inject an instance of the BookRepository

Here is a code snippet of the test class initialization: 

```java
@SpringBootTest(classes = SpringJPATestingApplication.class)
class SpringBookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    ...
}
```

The above tests work perfect! 

But, let us have a look at the timing!

#### Test Cases Timings

The below table illustrates the time taken by each test: 

| Test Name                                   |  Run-1(ms) |  Run-2(ms) |  Run-3(ms) |
| ------------------------------------------- | ---------- | ---------- | ---------- |
| it_saves_the_books_to_the_database          |     11     |     10     |     12     |
| it_finds_all_the_books_in_the_database      |     11     |     10     |     10     |
| it_finds_a_book_by_an_id                    |     15     |     16     |     19     |
| it_finds_books_by_author_name               |     49     |     46     |     45     |
| it_returns_true_if_book_exists              |     280    |     254    |     270    |
| it_counts_the_number_of_books_in_repository |     13     |     12     |     14     |
| it_deletes_a_book                           |     14     |     15     |     18     |
| it_deletes_a_book_by_id                     |     16     |     20     |     19     |
| Test Class Initialization                   |    5359    |    4712    |    4819    |
| SpringBootTest Initialization               |     781    |     847    |     910    |
| **Total**                                   |  **7330**  |  **6789**  |  **7046**  |

Notice that most of the time is consumed by the last 2 rows of the above table. 
Those two rows are not part of our tests, they are context setup to run the 
SpringBoot tests. 

The question is do we need to have SpringBoot running when performing those tests?

The answer is no! But, what can it be replaced with? 

## Solution 

To solve the above problem, we need to provide an implementation of the BookRepository
interface. There are two ways to do so: 
1. Write our own implementation, but this is complex, faulty and time wasting.
1. Find a way to inject an Implementation of BookRepository similar to what Spring does.  

### Illustration  

To remove the dependency on SpringBoot, we need to do the following: 
1. Use the EntityManager class to load the InMemory database configuration (H2 in our case)
1. Use JpaRepositoryFactory class to inject an implementation of the BookRepository
interface.  

The code in our test file will look like this: 

```java
class NoSpringBookRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static BookRepository bookRepository;

    ...

    @BeforeAll
    public static void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("InMemoryRepository");
        entityManager = entityManagerFactory.createEntityManager();
        JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        bookRepository = factory.getRepository(BookRepository.class);
    }

    ...
```

Let's see how the timings are now! 

#### Test Cases Timings

The below table compares the timing between the two test classes:

|                  Test Name                  |  Run-1 (SBT) | Run-1 (JRF) | Run-2 (SBT) | Run-2 (JRF) | Run-3 (SBT) | Run-3 (JRF) | 
| ------------------------------------------- | -----------  | ----------- | ----------- | ----------- | ----------  | ----------- | 
| it_saves_the_books_to_the_database          |      11      |      2      |      10     |      2      |      12     |      2      | 
| it_finds_all_the_books_in_the_database      |      11      |      5      |      10     |      6      |      10     |      10     | 
| it_finds_a_book_by_an_id                    |      15      |      3      |      16     |      4      |      19     |      4      | 
| it_finds_books_by_author_name               |      49      |      5      |      46     |      6      |      45     |      6      | 
| it_returns_true_if_book_exists              |      280     |      12     |      254    |      19     |      270    |      14     | 
| it_counts_the_number_of_books_in_repository |      13      |      5      |      12     |      5      |      14     |      6      | 
| it_deletes_a_book                           |      14      |      7      |      15     |      11     |      18     |      8      | 
| it_deletes_a_book_by_id                     |      16      |      8      |      20     |      8      |      19     |      15     |
| Test Class Initialization                   |     5359     |     275     |     4712    |     298     |     4819    |     296     |
| SpringBootTest Initialization               |      781     |      0      |      847    |      0      |      910    |      0      |
| **Total**                                   |   **7330**   |   **322**   |   **6789**  |   **359**   |   **7046**  |   **361**   |

**SBT = using SpringBootTest*

**JRF = using JpaRepositoryFactory* 
                             
The above table clearly shows that we gained on average **6,700 ms** for each test class!

## Conclusion

As developers, we always aim at making our tests run faster. Simply because that 
fastens the feedback loop and thus allowing us to run the tests more often! 

As a result, the confidence in the code increases! 

By following the approach defined in this tutorial, we can gain time in some of 
the tests. Of course, this can't be applied for all the tests because it is required
to have Spring up and running for certain tests in the Spring application. 

## Try It

To try this tutorial on your machine, run this maven command:  
```
mvn clean install
``` 
The 'JUnit Insights' configuration defined in the 'maven-surefire-plugin' will 
generate the test reports under a 'reports' folder.    

                                                                                                                     
## Keep In Mind

Keep in mind the following points when referencing this project: 
1. **H2 InMemory Database**: Here I am using 'H2 InMemory' as the in-memory 
database. Of course, the logic in this application works with any other DB 
instance. You just need to modify the configuration accordingly.   
1. **Configuration Files**: Loading H2 and starting Spring for the tests require
the following configuration files:  
    1. [persistence.xml](src\test\resources\META-INF\persistence.xml)
    1. [application.properties](src\test\resources\application.properties)
    1. [create_tables.sql](src\test\resources\create_tables.sql)
 

## Resources 

1. [JpaRepositoryFactory - Spring Documentation](https://docs.spring.io/spring-data/data-jpa/docs/current/api/org/springframework/data/jpa/repository/support/JpaRepositoryFactory.html)
1. [JUnit Insights](https://github.com/adessoAG/junit-insights)


