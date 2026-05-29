package com.catalogia.productcatalog.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.catalogia.productcatalog.dto.ProductRequest;
import com.catalogia.productcatalog.dto.ProductResponse;
import com.catalogia.productcatalog.entity.Category;
import com.catalogia.productcatalog.entity.Product;
import com.catalogia.productcatalog.exception.ResourceNotFoundException;
import com.catalogia.productcatalog.repository.ProductRepository;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryService categoryService;
	private final FileStorageService fileStorageService;

	public ProductService(
			ProductRepository productRepository,
			CategoryService categoryService,
			FileStorageService fileStorageService) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.fileStorageService = fileStorageService;
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> search(String search, BigDecimal minPrice, BigDecimal maxPrice, Long categoryId) {
		Specification<Product> specification = buildSpecification(search, minPrice, maxPrice, categoryId);
		return productRepository.findAll(specification).stream()
				.map(ProductResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		return ProductResponse.from(getProduct(id));
	}

	public ProductResponse create(ProductRequest request, MultipartFile image) {
		Category category = categoryService.getById(request.getCategoryId());
		Product product = new Product();
		applyRequest(product, request, category);
		product.setImageUrl(fileStorageService.store(image));
		return ProductResponse.from(productRepository.save(product));
	}

	public ProductResponse update(Long id, ProductRequest request, MultipartFile image) {
		Product product = getProduct(id);
		Category category = categoryService.getById(request.getCategoryId());
		applyRequest(product, request, category);

		if (image != null && !image.isEmpty()) {
			fileStorageService.deleteIfPresent(product.getImageUrl());
			product.setImageUrl(fileStorageService.store(image));
		}

		return ProductResponse.from(productRepository.save(product));
	}

	public void delete(Long id) {
		Product product = getProduct(id);
		fileStorageService.deleteIfPresent(product.getImageUrl());
		productRepository.delete(product);
	}

	private Product getProduct(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
	}

	private void applyRequest(Product product, ProductRequest request, Category category) {
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setDescription(request.getDescription());
		product.setCategory(category);
	}

	private Specification<Product> buildSpecification(
			String search,
			BigDecimal minPrice,
			BigDecimal maxPrice,
			Long categoryId) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (search != null && !search.isBlank()) {
				String pattern = "%" + search.trim().toLowerCase() + "%";
				predicates.add(criteriaBuilder.or(
						criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)));
			}

			if (minPrice != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
			}

			if (maxPrice != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
			}

			if (categoryId != null) {
				predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
			}

			if (predicates.isEmpty()) {
				return criteriaBuilder.conjunction();
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}

}
