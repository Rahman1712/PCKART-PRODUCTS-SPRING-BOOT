package com.ar.pckart.repo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ar.pckart.dto.ProductResponse;
import com.ar.pckart.model.Product;

import jakarta.transaction.Transactional;

public interface ProductRepo extends JpaRepository<Product, Long>{

	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "ORDER BY p.added_at DESC")
    List<ProductResponse> getAllProductsDetails();

	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.id = ?1 "
			+ "ORDER BY p.added_at DESC")
	ProductResponse getProductDetailById(Long id);
	
	@Query(nativeQuery = true, value = "SELECT specs_key, specs FROM product_specs WHERE spec_id = :id")
	List<Map<String, String>> getProductSpecsById(@Param("id") Long id);

	default List<ProductResponse> getAllProductDetailsWithSpecs() {
	    List<ProductResponse> productResponses = getAllProductsDetails();

	    for (ProductResponse response : productResponses) {
	        Long productId = response.getProductId();
	        List<Map<String, String>> specs = getProductSpecsById(productId);
	        response.setProductSpecs(specs);
	    }

	    return productResponses;
	}
	
	default ProductResponse getProductDetailWithSpecsById(Long id) {
	    ProductResponse response = getProductDetailById(id);
	    response.setProductSpecs(getProductSpecsById(response.getProductId()));

	    return response;
	}

	
	@Modifying
	@Transactional
	@Query("update Product u set u.name = :name where u.id = :id")
	void updateName(@Param(value = "id") Long id, @Param(value = "name") String name);
	
	@Query("SELECT p.quantity FROM Product p WHERE p.id = :id")
	public int getQuantityById(@Param("id") Long id);
	@Query("SELECT p.quantity FROM Product p WHERE p.name = :name")
	public int getQuantityByName(@Param("name") String name);
	
	@Query("SELECT DISTINCT(p.brand.name) from Product p")
	List<String> listOfDistinctBrands();
	
	@Query("SELECT p.brand.name,p.brand.id from Product p GROUP BY p.brand.name")
	List<String> listOfDistinctBrandsImgs();
	
	@Query("SELECT count(*) from Product p WHERE p.category.name = :categoryName")
	Long countByCategoryName(String categoryName);
	
} 
