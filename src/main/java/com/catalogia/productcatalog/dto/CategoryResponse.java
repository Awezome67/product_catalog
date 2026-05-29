package com.catalogia.productcatalog.dto;

import com.catalogia.productcatalog.entity.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {

	private Long id;
	private String name;

	public static CategoryResponse from(Category category) {
		CategoryResponse response = new CategoryResponse();
		response.setId(category.getId());
		response.setName(category.getName());
		return response;
	}

}
