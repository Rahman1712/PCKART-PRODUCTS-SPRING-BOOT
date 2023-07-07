package com.ar.pckart.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@SqlResultSetMapping(
	    name = "ProductResponseMapping",
	    entities = @EntityResult(
	        entityClass = ProductResponse2.class,
	        fields = {
	            @FieldResult(name = "productId", column = "productId"),
	            @FieldResult(name = "productName", column = "productName"),
	            @FieldResult(name = "productPrice", column = "productPrice"),
	            @FieldResult(name = "productQuantity", column = "productQuantity"),
	            @FieldResult(name = "productDiscount", column = "productDiscount"),
	            @FieldResult(name = "productColor", column = "productColor"),
	            @FieldResult(name = "productDescription", column = "productDescription"),
	            @FieldResult(name = "productSpecs", column = "productSpecs"),
	            @FieldResult(name = "brandId", column = "brandId"),
	            @FieldResult(name = "brandName", column = "brandName"),
	            @FieldResult(name = "categoryId", column = "categoryId"),
	            @FieldResult(name = "categoryName", column = "categoryName")
	        }
	    )
	)
public class ProductResponse2 {

	private Long productId;
	private String productName;
	private double productPrice;
	private int productQuantity; 
	private float productDiscount;
	private String productColor;
	
	private String productDescription;
	private Map<String,String> productSpecs = new HashMap<>();
	
	private Long brandId;
	private String brandName;
	
	private Long categoryId;
	private String categoryName;
	
	
	
	public ProductResponse2(Long productId, String productName,
			double productPrice, int productQuantity,
			float productDiscount, String productColor,
			Long brandId, String brandName, Long categoryId,
			String categoryName) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productQuantity = productQuantity;
		this.productDiscount = productDiscount;
		this.productColor = productColor;
		this.brandId = brandId;
		this.brandName = brandName;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}



	public ProductResponse2(Long productId, String productName, double productPrice, int productQuantity,
			float productDiscount, String productColor, String productDescription, Long brandId, String brandName,
			Long categoryId, String categoryName) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productQuantity = productQuantity;
		this.productDiscount = productDiscount;
		this.productColor = productColor;
		this.productDescription = productDescription;
		this.brandId = brandId;
		this.brandName = brandName;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}



//	public ProductResponse(Long productId, String productName, double productPrice, int productQuantity,
//			float productDiscount, String productColor, String productDescription, Object productSpecs,
//			Long brandId, String brandName, Long categoryId, String categoryName) {
//		this.productId = productId;
//		this.productName = productName;
//		this.productPrice = productPrice;
//		this.productQuantity = productQuantity;
//		this.productDiscount = productDiscount;
//		this.productColor = productColor;
//		this.productDescription = productDescription;
//		this.productSpecs = productSpecs;
//		this.brandId = brandId;
//		this.brandName = brandName;
//		this.categoryId = categoryId;
//		this.categoryName = categoryName;
//	}


	

}
