package com.catalogia.productcatalog.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

	@NotBlank
	@Size(max = 255)
	private String name;

	@NotNull
	@DecimalMin(value = "0.0", inclusive = false)
	private BigDecimal price;

	@Size(max = 2000)
	private String description;

	@NotNull
	private Long categoryId;

}
