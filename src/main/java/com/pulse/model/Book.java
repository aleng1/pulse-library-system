package com.pulse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    private String publisher;
    
    @Enumerated(EnumType.STRING)
    private BookCategory category;
    
    private int publicationYear;
    private int copies;
    private int availableCopies;
    
    @Enumerated(EnumType.STRING)
    private BookLocation location;
    private String status;
} 