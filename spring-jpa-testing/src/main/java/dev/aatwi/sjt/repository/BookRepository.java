package dev.aatwi.sjt.repository;

import dev.aatwi.sjt.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select book1 from Book book1 where book1.author=?1")
    List<Book> findAllByAuthor(String author);
}
