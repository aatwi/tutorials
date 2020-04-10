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
 
**Note:** In real projects, we don't need to unit test all the Repository methods
like I did here, since we can assume it fully tested by the Spring Framework. 
Nevertheless, the same logic can apply for tests that require initializing an 
instance of the repository.  
 
## Problem
Spring simplifies writing the repository layer for us. By extending the interface 
'JpaRepository', we can benefit from a wide range of functions (ex: findOne, 
findAll, exists, etc.) without writing SQL or Java code. 

This is all provided thanks to the Dependency Injection done by the Spring Framework. 
However, the price we pay for that is in the tests; because to run any test, we 
need to start Spring. Thus, losing a lot of precious time!

### Illustration  


#### Test Cases Timings

|                  Test Name                  | Setup | Execution | TearDown |
| ------------------------------------------- | ----- | --------- | -------- |
| it_saves_the_books_to_the_database          |   4   |     0     |    4     |
| it_finds_all_the_books_in_the_database      |   4   |     2     |    3     |
| it_finds_a_book_by_an_id                    |   4   |     6     |    4     |
| it_finds_books_by_author_name               |   4   |     27    |    3     |
| it_returns_true_if_book_exists              |  150  |     8     |    11    |
| it_counts_the_number_of_books_in_repository |   4   |     3     |    4     |
| it_deletes_a_book                           |   5   |     6     |    3     |
| it_deletes_a_book_by_id                     |   3   |     11    |    2     |

#### Spring Context Setup 

|                Test Class                 | Time | 
| ----------------------------------------- | ---- |
|            BookRepositoryTest             | 2639 |
| InMemoryRepositoryTestingApplicationTests | 556  |
                    

## Solution 

## Resources 

