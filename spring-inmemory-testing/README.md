# In-Memory Repository Testing in Spring Applications

In this project, I will show how to write tests for the repository layer in a 
Spring Application without running the server.

The main purpose of this approach is to make the tests run faster; thus, having
a faster feedback loop.

## Tutorial Description

For this tutorial, we will be using a Spring application that performs simple DB
operations on a book repository. There will be no need to write any Controller
or Service code. 

### Project Structure 
The project contains the following Java classes: 
1. [Book.java](src\main\java\dev\aatwi\imrt\model\Book.java): This class defines the Book entity class.
1. [BookRepository.java](src\main\java\dev\aatwi\imrt\repository\BookRepository.java): This interface extends the 'JpaRepository' interface. 
In addition to the inherited functionalities, we have a new method that finds books by Author name.
1. [SpringBookRepositoryTest.java](src\test\java\dev\aatwi\imrt\repository\SpringBookRepositoryTest.java): This is a SpringBootTest, thus, it starts a Spring instance before running the tests. 
1. [NoSpringBookRepositoryTest.java](src\test\java\dev\aatwi\imrt\repository\NoSpringBookRepositoryTest.java): This test suite does not require running Spring when running tests. 
 
**Note:** In reality, we don't need to unit test all the Repository methods like
I did here, since we can assume it fully tested by the Spring Framework. 
Nevertheless, the same logic can be applied for tests that require initializing 
an instance of the repository.  
 
## Problem
Spring simplifies writing the repository layer for us. By extending the interface 
'JpaRepository', we can benefit from a wide range of functions (ex: findOne, 
findAll, exists, etc.) without writing SQL or Java code. 

This is all provided thanks to the Dependency Injection done by the Spring Framework. 
However, the price we pay for that is in the tests; because to run any test, we 
need to start Spring. Thus, losing a lot of precious time!

### Illustration  
Let's analyze the problem! 

In the test class [SpringBookRepositoryTest.java](src\test\java\dev\aatwi\imrt\repository\SpringBookRepositoryTest.java), 
we have 8 test cases to cover 8 different methods of the BookRepository interface. 
To run those tests properly we need to: 
1. Tag the test with the '@SpringBootTest'annotation. This will be used to run
Spring before running the test.
1. Tag the BookRepository variable with '@Autowired' annotation. This will be 
used by Spring to inject an instance of the BookRepository

Here is a code snippet of the class initialization: 

```java
@SpringBootTest(classes = InMemoryRepositoryTestingApplication.class)
class SpringBookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    ...
}
```

The above tests work perfect! But, let us have a look at the timing!

#### Test Cases Timings

The below table illustrates the time taken by each test: 

|                  Test Name                  |  Run 1(ms) |  Run 2(ms) |  Run 3(ms) |
| ------------------------------------------- | ---------- | ---------- | ---------- |
| it_saves_the_books_to_the_database          |     11     |     10     |     12     |
| it_finds_all_the_books_in_the_database      |     11     |     10     |     10     |
| it_finds_a_book_by_an_id                    |     15     |     16     |     19     |
| it_finds_books_by_author_name               |     49     |     46     |     45     |
| it_returns_true_if_book_exists              |     280    |     254    |     270    |
| it_counts_the_number_of_books_in_repository |     13     |     12     |     14     |
| it_deletes_a_book                           |     14     |     15     |     18     |
| it_deletes_a_book_by_id                     |     16     |     20     |     19     |
| SpringBookRepositoryTest                    |    5359    |    4712    |    4819    |
| InMemoryRepositoryTestingApplicationTests   |     781    |     847    |     910    |
| **Total**                                   |    7330    |    6789    |    7046    |

Notice that most of the time is consumed by the last 2 rows of the above table. 
Those two rows are not part of our tests, they are context setup to run the 
SpringBoot tests. 
                    

## Solution 

### Illustration  

#### Test Cases Timings

The below table illustrates the time taken by each test: 

|                  Test Name                  |  Run 1(ms) |  Run 2(ms) |  Run 3(ms) | 
| ------------------------------------------- | ---------- | ---------- | ---------- | 
| it_saves_the_books_to_the_database          |     2      |     2      |     2      | 
| it_finds_all_the_books_in_the_database      |     5      |     6      |     10     | 
| it_finds_a_book_by_an_id                    |     3      |     4      |     4      | 
| it_finds_books_by_author_name               |     5      |     6      |     6      | 
| it_returns_true_if_book_exists              |     12     |     19     |     14     | 
| it_counts_the_number_of_books_in_repository |     5      |     5      |     6      | 
| it_deletes_a_book                           |     7      |     11     |     8      | 
| it_deletes_a_book_by_id                     |     8      |     8      |     15     | 

## Resources 

