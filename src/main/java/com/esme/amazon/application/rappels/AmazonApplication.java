package com.esme.amazon.application.rappels;

import com.esme.amazon.application.rappels.Domain.Amazon.Product;
import com.esme.amazon.application.rappels.Infrastructure.AmazonEntity;
import com.esme.amazon.application.rappels.Infrastructure.MarcheRepository;
import com.esme.amazon.application.rappels.Infrastructure.ProductEntity;
import com.esme.amazon.application.rappels.Infrastructure.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;


@Slf4j
@SpringBootApplication
public class AmazonApplication implements CommandLineRunner {

	//  @Autowired
	private MarcheRepository marcheRepository;
	//  @Autowired
	private ProductRepository productRepository;

	public AmazonApplication(MarcheRepository marcheRepository, ProductRepository productRepository) {
		this.marcheRepository = marcheRepository;
		this.productRepository = productRepository;
	}

	public static void main(String[] args) {

		SpringApplication.run(AmazonApplication.class, args);
		System.out.println("Hello SUDRIA !");
	}

	@Override
	public void run(String... args) {

		log.info("Data initilisation...");
		saveAmazon(1L, "Tablette", 5, "ELECTRONIQUE", Arrays.asList(Product.builder().frequency(2).category("electricité").build()));
		saveAmazon(2L, "Le petit prince", 1, "LIVRE", Arrays.asList(Product.builder().frequency(1).category("bois").build()));
	}

	@Transactional
	private void saveAmazon(long id, String name, int stock, String category, List<Product> products) {


		AmazonEntity amazonEntity = this.marcheRepository.save(
				AmazonEntity
						.builder()
						.id(id)
						.name(name)
						.stock(stock)
						.category(category)
						.build());

		products.stream()
				.forEach(product ->
						productRepository.save(
								ProductEntity
										.builder()
										.category(product.getCategory())
										.frequency(product.getFrequency())
										.quantity(product.getQuantity())
										.amazonEntity(amazonEntity)
										.build()
						));
	}

}
