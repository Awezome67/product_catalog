package com.catalogia.productcatalog.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catalogia.productcatalog.dto.ProductRequest;
import com.catalogia.productcatalog.dto.ProductResponse;
import com.catalogia.productcatalog.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<ProductResponse> search(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) BigDecimal minPrice,
			@RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(required = false) Long categoryId) {
		return productService.search(search, minPrice, maxPrice, categoryId);
	}

	@GetMapping("/{id}")
	public ProductResponse findById(@PathVariable Long id) {
		return productService.findById(id);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse create(
			@Valid @RequestPart("product") ProductRequest request,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		return productService.create(request, image);
	}

	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProductResponse update(
			@PathVariable Long id,
			@Valid @RequestPart("product") ProductRequest request,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		return productService.update(id, request, image);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		productService.delete(id);
	}

}
