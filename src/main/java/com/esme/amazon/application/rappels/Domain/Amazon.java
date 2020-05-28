package com.esme.amazon.application.rappels.Domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Amazon {

    private Long id;
    private String name;
    private int stock;
    private String category;
    private List<Product> products;

    @Getter
    @ToString
    @EqualsAndHashCode
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

        private Long id;
        private int frequency;
        private int quantity;
        private String category;

    }

}
