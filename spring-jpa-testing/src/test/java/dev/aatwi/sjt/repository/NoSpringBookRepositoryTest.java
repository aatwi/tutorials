package dev.aatwi.sjt.repository;

import dev.aatwi.sjt.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoSpringBookRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static BookRepository bookRepository;

    private Book cleanCode;
    private Book refactoring;
    private Book cleanArchitecture;

    @BeforeAll
    public static void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("InMemoryRepository");
        entityManager = entityManagerFactory.createEntityManager();
        JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        bookRepository = factory.getRepository(BookRepository.class);
    }

    @BeforeEach
    public void before() {
        entityManager.getTransaction().begin();
        cleanCode = new Book("978-0132350884", "Clean Code", "Robert Martin");
        refactoring = new Book("978-0201485677", "Refactoring: Improving the Design of Existing Code", "Martin Fowler");
        cleanArchitecture = new Book("978-0134494166", "Clean Architecture: A Craftsman's Guide to Software Structure and Design", "Robert Martin");

        cleanCode = bookRepository.save(cleanCode);
        refactoring = bookRepository.save(refactoring);
        cleanArchitecture = bookRepository.save(cleanArchitecture);
    }

    @Test
    public void itSavesTheBooksToTheDatabase() {
        assertTrue(cleanCode.getBookId() > 0);
        assertTrue(refactoring.getBookId() > 0);
        assertTrue(cleanArchitecture.getBookId() > 0);
    }

    @Test
    public void itFindsAllTheBooksInTheDatabase() {
        assertEquals(3, bookRepository.findAll().size());
    }

    @Test
    public void itFindsABookByAnId() {
        assertEquals(cleanCode, bookRepository.findById(cleanCode.getBookId()).get());
        assertEquals(refactoring, bookRepository.findById(refactoring.getBookId()).get());
        assertEquals(cleanArchitecture, bookRepository.findById(cleanArchitecture.getBookId()).get());
    }

    @Test
    public void
    itFindsBooksByAuthorName() {
        List<Book> robertMartin = bookRepository.findAllByAuthor("Robert Martin");
        List<Book> martinFowler = bookRepository.findAllByAuthor("Martin Fowler");

        assertEquals(Arrays.asList(cleanCode, cleanArchitecture), robertMartin);
        assertEquals(Arrays.asList(refactoring), martinFowler);
    }

    @Test
    public void
    itReturnsTrueIfBookExists() {
        assertTrue(bookRepository.existsById(cleanCode.getBookId()));
        assertTrue(bookRepository.existsById(refactoring.getBookId()));
        assertTrue(bookRepository.existsById(cleanArchitecture.getBookId()));
    }

    @Test
    public void
    itCountsTheNumberOfBooksInRepository() {
        assertEquals(3, bookRepository.count());
    }

    @Test
    public void
    itDeletesABook() {
        bookRepository.delete(cleanCode);
        bookRepository.delete(refactoring);
        bookRepository.delete(cleanArchitecture);

        assertFalse(bookRepository.existsById(cleanCode.getBookId()));
        assertFalse(bookRepository.existsById(refactoring.getBookId()));
        assertFalse(bookRepository.existsById(cleanArchitecture.getBookId()));
    }

    @Test
    public void
    itDeletesABookById() {
        bookRepository.deleteById(cleanCode.getBookId());
        bookRepository.deleteById(refactoring.getBookId());
        bookRepository.deleteById(cleanArchitecture.getBookId());

        assertFalse(bookRepository.existsById(cleanCode.getBookId()));
        assertFalse(bookRepository.existsById(refactoring.getBookId()));
        assertFalse(bookRepository.existsById(cleanArchitecture.getBookId()));
    }

    @AfterEach
    private void tearDown() {
        entityManager.getTransaction().rollback();
    }
}
