package com.catalogia.productcatalog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalogia.productcatalog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByNameIgnoreCase(String name);

}
