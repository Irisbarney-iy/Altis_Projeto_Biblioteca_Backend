package com.altis.library.books.models.entities;

import com.altis.library.publishers.models.entities.PublisherEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(nullable = false)
    private Integer anoLancamento;

    @Column(nullable = false)
    private Integer quantidadeTotal;

    @Column(nullable = false)
    private Integer quantidadeEmUso = 0;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private PublisherEntity publisher;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

}
