package com.esme.amazon.application.rappels.Infrastructure;

import com.esme.amazon.application.rappels.Domain.Amazon;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AmazonDao {

    private MarcheRepository marcheRepository;
    private ProductRepository productRepository;

    public AmazonDao(MarcheRepository marcheRepository) {
        this.marcheRepository = marcheRepository;
    }

    public List<Amazon> findAmazons() {
        return StreamSupport.stream(marcheRepository.findAll().spliterator(), false)
                .map(amazonEntitie -> buildAmazon(amazonEntitie))
                .collect(Collectors.toList());
    }

    public Amazon findAmazons(Long id) throws ChangeSetPersister.NotFoundException {
        return buildAmazon(marcheRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    public Amazon createAmazons(Amazon amazon) {
        return buildAmazon(marcheRepository.save(buildEntity(amazon)));
    }

    public void deleteAmazons(Long id) {
        marcheRepository.delete(marcheRepository.findById(id).get());
    }

    public void updateAmazon(Amazon amazon) {

        AmazonEntity amazonEntity = marcheRepository.save(buildEntity(amazon));

        amazon
                .getProducts()
                .stream()
                .forEach(product ->
                        productRepository.save(ProductEntity.builder()
                                .category(product.getCategory())
                                .frequency(product.getFrequency())
                                .quantity(product.getQuantity())
                                .amazonEntity(amazonEntity)
                                .build()));
    }

    public Amazon replaceAmazon(Amazon amazon) {
        return buildAmazon(marcheRepository.save(buildEntity(amazon)));
    }

    private AmazonEntity buildEntity(Amazon amazon) {
        return AmazonEntity
                .builder()
                .id(amazon.getId())
                .name(amazon.getName())
                .stock(amazon.getStock())
                .category(amazon.getCategory())
                .productEntities(
                        amazon
                                .getProducts()
                                .stream()
                                .map(product -> ProductEntity.builder()
                                        .category(product.getCategory())
                                        .frequency(product.getFrequency())
                                        .quantity(product.getQuantity())
                                        .build())
                                .collect(Collectors.toList()))
                .build();
    }

    private Amazon buildAmazon(AmazonEntity amazonEntity) {
        return Amazon.builder()
                .id(amazonEntity.getId())
                .name(amazonEntity.getName())
                .stock(amazonEntity.getStock())
                .category(amazonEntity.getCategory())
                .products(
                        amazonEntity
                                .getProductEntities()
                                .stream()
                                .map(productEntity -> Amazon.Product.builder()
                                        .id(productEntity.getId())
                                        .category(productEntity.getCategory())
                                        .frequency(productEntity.getFrequency())
                                        .quantity(productEntity.getQuantity())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }


}
