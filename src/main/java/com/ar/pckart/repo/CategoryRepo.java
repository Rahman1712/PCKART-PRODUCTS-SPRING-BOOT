package com.ar.pckart.repo;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ar.pckart.dto.CategoryResponse;
import com.ar.pckart.model.Category;

import jakarta.transaction.Transactional;

public interface CategoryRepo extends JpaRepository<Category, Long> {

	@Modifying
	@Transactional
	@Query(value = "update Category set name = :name, image = :image, imageName = :imageName, imageType = :imageType , parent = :parent WHERE id = :id")
	public void updateCategory(@Param("id") Long id, 
			@Param("name") String name, 
			@Param("image") byte[] image, 
			@Param("imageName") String imageName, 
			@Param("imageType") String imageType, 
			@Param("parent") Category parent);


	@Query("SELECT new com.ar.pckart.dto.CategoryResponse("
			+ "c.id, c.name, c.parent.id) "
			+ "FROM Category c "
			+ "WHERE c.id = ?1")
	Optional<CategoryResponse> findByIdCategoryResponse(Long id);
	
	
	@Query("SELECT new com.ar.pckart.dto.CategoryResponse("
			+ "c.id, c.name, c.parent.id) "
			+ "FROM Category c ")
	public List<CategoryResponse> allCategoriesIdAndName();
	
	
	@Query("SELECT c FROM Category c")
	public List<Category> allCategoriesQuery();
	
	@Query("SELECT c FROM Category c WHERE c.parent = NULL")
	public List<Category> allParentCategories();
	
	@Query("SELECT c FROM Category c WHERE c.parent.name = :parentCategory")
	public List<Category> allCategoriesByParentName(
			@Param("parentCategory") String parentCategory);
} 
