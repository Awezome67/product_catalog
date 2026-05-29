package com.catalogia.productcatalog.dto;

import java.math.BigDecimal;

import com.catalogia.productcatalog.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private String description;
	private Long categoryId;
	private String categoryName;
	private String imageUrl;

	public static ProductResponse from(Product product) {
		ProductResponse response = new ProductResponse();
		response.setId(product.getId());
		response.setName(product.getName());
		response.setPrice(product.getPrice());
		response.setDescription(product.getDescription());
		response.setCategoryId(product.getCategory().getId());
		response.setCategoryName(product.getCategory().getName());
		response.setImageUrl(product.getImageUrl());
		return response;
	}

}
