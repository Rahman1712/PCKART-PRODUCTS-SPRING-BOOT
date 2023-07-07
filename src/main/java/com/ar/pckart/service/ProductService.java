package com.ar.pckart.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ar.pckart.dto.ProductDTO;
import com.ar.pckart.dto.ProductDetails;
import com.ar.pckart.dto.ProductResponse;
import com.ar.pckart.model.Brand;
import com.ar.pckart.model.Category;
import com.ar.pckart.model.ImageModel;
import com.ar.pckart.model.ImagesData;
import com.ar.pckart.model.Product;
import com.ar.pckart.repo.ProductPageRepo;
import com.ar.pckart.repo.ProductRepo;
import com.ar.pckart.util.ImagesDataUtils;

@Service
public class ProductService {

	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private ProductPageRepo pageRepo;
	
	@Autowired
	private ImagesDataService imagesDataService;
	
	
	public ProductDetails save(Product product) {
		Product savedProd = productRepo.save(product);
		return ProductDetails.builder()
			.id(savedProd.getId())
			.name(savedProd.getName())
			.price(savedProd.getPrice())
			.quantity(savedProd.getQuantity())
			.discount(savedProd.getDiscount())
			.category(savedProd.getCategory().getName())
			.brand(savedProd.getBrand().getName())
			.color(savedProd.getColor())
			.build();
	}
	
	public List<ProductResponse> listOfProducts(){
		return productRepo.getAllProductDetailsWithSpecs();
	}
	
	public ProductResponse productResponseById(Long id){
		return productRepo.getProductDetailWithSpecsById(id);
	}
	 
	public List<ProductDTO<byte[], ImageModel>> listOfProductsWithImages() throws IOException{
		List<ProductDTO<byte[], ImageModel>> productsList = new ArrayList<>(); 
		for(ProductResponse pr : listOfProducts()) {
			ImagesData imageData = 	imagesDataService
					.findImageDataByParentId(pr.getProductId())
					.get();
			byte[] image = ImagesDataUtils.imageFromFolder(
					imageData.getProductMainImage().getFilePath(),
					imageData.getProductMainImage().getImgName());
			ProductDTO<byte[], ImageModel> pdto = new ProductDTO<>();
			pdto.setProductResponse(pr);
			pdto.setImgdata(image);
			pdto.setImgModel(imageData.getProductMainImage());
			productsList.add(pdto);
		}
		return productsList;
	}
	
	public ProductDTO<byte[], ImageModel> productDetailWithMainImageById(Long id) throws IOException{
		ProductDTO<byte[], ImageModel> productById = new ProductDTO<>(); 
		productById.setProductResponse(productResponseById(id));
		ImagesData imageData = 	imagesDataService
				.findImageDataByParentId(id)
				.get();
		
		byte[] image = ImagesDataUtils.imageFromFolder(
				imageData.getProductMainImage().getFilePath(),
				imageData.getProductMainImage().getImgName());
		productById.setImgdata(image);
		productById.setImgModel(imageData.getProductMainImage());
		
		return productById;
	}
	
	public ProductDTO<List<byte[]>, List<ImageModel>> productDetailsWithImagesById(Long id) throws IOException{
		ProductDTO<List<byte[]>, List<ImageModel>> productById = new ProductDTO<>(); 
		productById.setProductResponse(productResponseById(id));
		ImagesData imageData = 	imagesDataService
				.findImageDataByParentId(id)
				.get();
		
		List<byte[]> imagesList = new ArrayList<>();
		List<ImageModel> imagesModelList = new ArrayList<>();
		
		byte[] mainImage = ImagesDataUtils.imageFromFolder(
				imageData.getProductMainImage().getFilePath(),
				imageData.getProductMainImage().getImgName());
		
		imagesList.add(mainImage);
		imagesModelList.add(imageData.getProductMainImage());
		
		Set<ImageModel> productSubImages = imageData.getProductSubImages();
		for(ImageModel im: productSubImages) {
			byte[] image = ImagesDataUtils.imageFromFolder(
					im.getFilePath(),
					im.getImgName());
			imagesList.add(image);
			imagesModelList.add(im);
		}
//		File folder = new File(imageData.getFilePath());
//		File[] listOfFiles = folder.listFiles();
//		for(File f : listOfFiles) {
//			if(f.getName().startsWith(imageData.getImgName())) {
//				imagesList.add(ImagesDataUtils.imageFromFolder(imageData.getFilePath(), f.getName()));
//			}
//		}
		productById.setImgdata(imagesList);
		productById.setImgModel(imagesModelList);
		
		return productById;
	}
	
	public ProductDetails update(Long id,Product product) {
		product.setId(id);
		Product updatedProduct = productRepo.save(product);
		return ProductDetails.builder()
				.id(updatedProduct.getId())
				.name(updatedProduct.getName())
				.price(updatedProduct.getPrice())
				.quantity(updatedProduct.getQuantity())
				.discount(updatedProduct.getDiscount())
				.brand(updatedProduct.getCategory().getName())
				.category(updatedProduct.getBrand().getName())
				.color(updatedProduct.getColor())
				.build();
	}
	
	public void deleteById(Long id) {
		productRepo.deleteById(id);
	}
	
	public Page<ProductResponse> listAll(int currentPage,String sortField,String sortDir){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(currentPage - 1, 5,sort);   //10 5 20
		return pageRepo.getAllProductDetailsWithSpecs(pageable);
	}

	public Map<String, Object> listAllWithimage(int currentPage,String sortField,String sortDir) throws IOException{
		List<ProductDTO<byte[], ImageModel>> productsList = new ArrayList<>();
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(currentPage - 1, 5,sort);   //10 5 20
		Page<ProductResponse> page = pageRepo.getAllProductDetailsWithSpecs(pageable);
		
		for(ProductResponse pr : page.getContent()) {
			ImagesData imageData = 	imagesDataService
					.findImageDataByParentId(pr.getProductId())
					.get();
			byte[] image = ImagesDataUtils.imageFromFolder(
					imageData.getProductMainImage().getFilePath(),
					imageData.getProductMainImage().getImgName());
			ProductDTO<byte[], ImageModel> pdto = new ProductDTO<>();
			pdto.setProductResponse(pr);
			pdto.setImgdata(image);
			pdto.setImgModel(imageData.getProductMainImage());
			productsList.add(pdto);
		}
		
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		
		Map<String, Object> map = new HashMap<>();
		map.put("currentPage", currentPage);
		map.put("totalItems", totalItems);
		map.put("totalPages", totalPages);
		map.put("listProducts", productsList);
		map.put("sortField", sortField);
		map.put("sortDir", sortDir);
		
		return map;
	}

	public Object getSpecsById(Long id) {
		return productRepo.getProductSpecsById(id);
	}

    public Long countProductsByCategoryName(String categoryName) {
    	System.err.println("LONG : "+productRepo.countByCategoryName(categoryName));
        return productRepo.countByCategoryName(categoryName);
    }

    public Product getProductById(Long id) {
		ProductResponse response = productResponseById(id);

		
		Product product = Product.builder()
			.id(response.getProductId())
			.name(response.getProductName())
			.price(response.getProductPrice())
			.quantity(response.getProductQuantity())
			.discount(response.getProductDiscount())
			.color(response.getProductColor())
			.description(response.getProductDescription())
			.specs(toMapFromList(response.getProductSpecs()))
			.brand(
					Brand.builder()
					.id(response.getBrandId())
					.name(response.getBrandName())
					.build()
			)
			.category(
					Category.builder()
					.id(response.getCategoryId())
					.name(response.getCategoryName())
					.build()
			)
			.added_at(response.getAdded_at())
			.build();
			
		return product;
	}
    
    private static Map<String,String> toMapFromList(List<Map<String,String>> mapList){
    	List<String> keyAndValues = mapList.stream().flatMap(map -> 
			map.values().stream()).collect(Collectors.toList());
    	Map<String, String> map = new HashMap<>();
    	for(int i=0; i<keyAndValues.size(); i=i+2) {
    		map.put(keyAndValues.get(i), keyAndValues.get(i+1));
    	}
		return map;
    }
}


/*
public Map<String,String> getSpecsById(Long id) {
	return productRepo.getProductSpecsById(id);
}
*/