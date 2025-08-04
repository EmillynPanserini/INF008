package br.edu.ifba.inf008.interfaces.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
@Data

public class BooksEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "isbn", nullable = false)
    private String isbn;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(name = "published_year", nullable = false)
    private Integer publishedYear;
    @Column(name = "copies_available", nullable = false)
    private Integer copiesAvailable;

}
