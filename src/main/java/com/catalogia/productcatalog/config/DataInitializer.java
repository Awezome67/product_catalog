package com.catalogia.productcatalog.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.catalogia.productcatalog.entity.Category;
import com.catalogia.productcatalog.entity.Product;
import com.catalogia.productcatalog.repository.CategoryRepository;
import com.catalogia.productcatalog.repository.ProductRepository;

@Configuration
@Profile("dev")
public class DataInitializer {

	@Bean
	CommandLineRunner seedCatalog(CategoryRepository categoryRepository, ProductRepository productRepository) {
		return args -> {
			if (productRepository.count() > 0) {
				return;
			}

			Category category1 = categoryRepository.save(new Category("Category 1"));
			Category category2 = categoryRepository.save(new Category("Category 2"));
			Category category3 = categoryRepository.save(new Category("Category 3"));

			saveProduct(productRepository, "Wireless Headphones", new BigDecimal("79.99"),
					"Comfortable over-ear headphones with noise cancellation.", category1);
			saveProduct(productRepository, "Smart Watch", new BigDecimal("149.50"),
					"Track fitness, notifications, and daily activity.", category2);
			saveProduct(productRepository, "Desk Lamp", new BigDecimal("34.00"),
					"Adjustable LED lamp for your workspace.", category3);
			saveProduct(productRepository, "Notebook Set", new BigDecimal("12.99"),
					"Pack of three ruled notebooks for everyday notes.", category1);
			saveProduct(productRepository, "Water Bottle", new BigDecimal("18.75"),
					"Insulated stainless steel bottle, 750ml.", category2);
			saveProduct(productRepository, "Bluetooth Speaker", new BigDecimal("59.00"),
					"Portable speaker with rich bass and long battery life.", category3);
			saveProduct(productRepository, "Mechanical Keyboard", new BigDecimal("99.99"),
					"Compact keyboard with tactile switches.", category1);
			saveProduct(productRepository, "Canvas Tote Bag", new BigDecimal("24.50"),
					"Durable everyday tote with reinforced handles.", category2);
			saveProduct(productRepository, "Plant Pot", new BigDecimal("15.25"),
					"Minimal ceramic pot for indoor plants.", category3);
			saveProduct(productRepository, "USB-C Hub", new BigDecimal("42.00"),
					"Multi-port adapter for laptops and tablets.", category1);
		};
	}

	private void saveProduct(
			ProductRepository productRepository,
			String name,
			BigDecimal price,
			String description,
			Category category) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setDescription(description);
		product.setCategory(category);
		productRepository.save(product);
	}

}
