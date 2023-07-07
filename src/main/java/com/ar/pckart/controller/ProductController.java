package com.ar.pckart.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.pckart.dto.ProductDTO;
import com.ar.pckart.dto.ProductDetails;
import com.ar.pckart.dto.ProductResponse;
import com.ar.pckart.model.ImageModel;
import com.ar.pckart.model.Product;
import com.ar.pckart.service.ImagesDataService;
import com.ar.pckart.service.ProductService;


@RestController
@RequestMapping("/pckart/api/v1/products")
public class ProductController {
	
	@Autowired private ProductService productService;
	@Autowired private ImagesDataService imagesService;

	@PostMapping("/auth/add-product-imgs")
	public ProductDetails saveWithMultipleImage(
			@RequestParam(name = "file") MultipartFile file,
			@RequestParam(name = "files") MultipartFile[] files,
			@RequestPart Product product
			) throws IOException {
		
		ProductDetails savedProduct = productService.save(product);
		imagesService.saveImages(file, files, savedProduct.getId());
		
		return savedProduct; 
	}
	
	@GetMapping("/get/all-products")
	public ResponseEntity<List<ProductResponse>> allProducts(){
		return ResponseEntity.ok(productService.listOfProducts());
	}
	
	@GetMapping("/get/all-product-imgs")
	public ResponseEntity<?> listOfProducts() throws IOException {
		List<ProductDTO<byte[], ImageModel>> listOfProds = productService.listOfProductsWithImages();
		return ResponseEntity.status(HttpStatus.OK).body(listOfProds);	
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getProductId(@PathVariable("id") Long id){
		return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(id));
	}
	
	@GetMapping("/get/product/{id}")
	public ResponseEntity<?> getProductDetailsMainImageById(@PathVariable Long id) throws IOException {
		ProductDTO<byte[], ImageModel> productInfo = productService.productDetailWithMainImageById(id);
		return ResponseEntity.status(HttpStatus.OK).body(productInfo);	
	}
	
	@GetMapping("/get/product-imgs/{id}")
	public ResponseEntity<?> getProductDetailsImagesById(@PathVariable Long id) throws IOException {
		ProductDTO<List<byte[]>, List<ImageModel>> productInfo = productService.productDetailsWithImagesById(id);
		return ResponseEntity.status(HttpStatus.OK).body(productInfo);	
	}
	
	@GetMapping("/get/product-res/{id}")
	public ResponseEntity<ProductResponse> getProductResponseById(
			@PathVariable("id") Long id){
		return ResponseEntity.ok(productService.productResponseById(id));
	}
	
	@PutMapping("/auth/update-product/{id}")
	public ProductDetails updateImageInfo(
			@PathVariable("id") Long id,
			@RequestParam(name = "file") MultipartFile file,
			@RequestParam(name = "files") MultipartFile[] files,
			@RequestPart Product product
			) throws IOException {
		
		ProductDetails updatedProduct = productService.update(id,product);
		imagesService.updateImages(file, files, updatedProduct.getId());
		
		return updatedProduct;
	}
	
	@DeleteMapping("/auth/delete-product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
		productService.deleteById(id);
		imagesService.deleteImageDataAndFolder(id);
		return ResponseEntity.ok("Product deleted");
	}
	

	@GetMapping("/get/page/{pageNumber}" )
	public Map<String, Object> listByPage(@PathVariable("pageNumber") int currentPage ,
			@Param("sortField") String sortField , @Param("sortDir") String sortDir ) {

		Page<ProductResponse> page = productService.listAll(currentPage,sortField,sortDir );
		
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		List<ProductResponse> prodsList = page.getContent();
		
		Map<String, Object> map = new HashMap<>();
		map.put("currentPage", currentPage);
		map.put("totalItems", totalItems);
		map.put("totalPages", totalPages);
		map.put("listProducts", prodsList);
		map.put("sortField", sortField);
		map.put("sortDir", sortDir); 
		
		String reverseSortDir =  sortDir.equals("asc") ? "desc" : "asc" ;
		map.put("reverseSortDir", reverseSortDir);
		
		return map;
	}
	
	@GetMapping("/get/page-imgs/{pageNumber}" )
	public Map<String, Object> listAllWithimage(@PathVariable("pageNumber") int currentPage ,
			@Param("sortField") String sortField , @Param("sortDir") String sortDir ) throws IOException {
		
		Map<String, Object> map = productService.listAllWithimage(currentPage,sortField,sortDir );
		String reverseSortDir =  sortDir.equals("asc") ? "desc" : "asc" ;
		map.put("reverseSortDir", reverseSortDir);
		
		return map;
	}
	
	@GetMapping
	public String work() {
		return "products";
	}
	
	@GetMapping("/get/product-count/by-category-name/{categoryName}" )
	public ResponseEntity<String> countProductsByCategoryName(@PathVariable("categoryName") String categoryName) {
		Long count = productService.countProductsByCategoryName(categoryName);
        return ResponseEntity.ok(String.valueOf(count));
    }
	
}

/*
 	@PostMapping("/add-product")
	public ProductDetails save(@RequestBody Product product) {
		return productService.save(product);
	}
*/

//	@GetMapping("/specs/{id}")
//	public ResponseEntity<?> getSpecsById(@PathVariable("id") Long id){
//		return ResponseEntity.ok(productService.getSpecsById(id));
//	}