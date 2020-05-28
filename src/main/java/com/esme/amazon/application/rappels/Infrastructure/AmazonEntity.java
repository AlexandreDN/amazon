package com.esme.amazon.application.rappels.Infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AmazonEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "NAME", length = 50, nullable = false)
    private String name;
    @Column(name = "AGE", nullable = false)
    private int stock;
    @Column(name = "CATEGORY", length = 50, nullable = false)
    private String category;

    @OneToMany(mappedBy = "amazonEntity", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private List<ProductEntity> productEntities;

}

