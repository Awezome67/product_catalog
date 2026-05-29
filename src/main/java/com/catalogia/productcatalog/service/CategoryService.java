package com.catalogia.productcatalog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catalogia.productcatalog.dto.CategoryResponse;
import com.catalogia.productcatalog.entity.Category;
import com.catalogia.productcatalog.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<CategoryResponse> findAll() {
		return categoryRepository.findAll().stream()
				.map(CategoryResponse::from)
				.toList();
	}

	@Transactional
	public Category getById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new com.catalogia.productcatalog.exception.ResourceNotFoundException(
						"Category not found: " + id));
	}

}
