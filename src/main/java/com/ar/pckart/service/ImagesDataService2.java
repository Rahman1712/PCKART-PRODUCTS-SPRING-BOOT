package com.ar.pckart.service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ar.pckart.model.ImageModel;
import com.ar.pckart.model.ImagesData;
import com.ar.pckart.repo.ImageModelRepo;
import com.ar.pckart.repo.ImagesDataRepo;
import com.ar.pckart.util.ImagesDataUtils;

//@Service
public class ImagesDataService2 {

	@Value("${images.folder.path}")
	private String FOLDER_PATH;
	
	@Autowired private ImagesDataRepo imageDataRepo;
	@Autowired private ImageModelRepo imageModelRepo;

	public ImagesData saveImages(MultipartFile imageFile, 
			MultipartFile[] subImagesFiles ,
			Long productId) throws IOException {
		
		ImagesData imgData = new ImagesData();
		imgData.setProductId(productId);
		
        File directory = new File(FOLDER_PATH+productId);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = FOLDER_PATH+productId+"\\";
        if(imageFile != null) {
        	ImagesDataUtils.imageToFolder(filePath, imageFile, imageFile.getOriginalFilename());
        	ImageModel mainImgMode = ImageModel.builder()
        		.filePath(filePath)
        		.imgName(imageFile.getOriginalFilename())
        		.imgType(imageFile.getContentType())
        		.build();
        	imgData.setProductMainImage(mainImgMode);
        }
        
        Set<ImageModel> subImagesModelSet = new HashSet<>(); 
        for(int i=0; i< subImagesFiles.length; i++) {
        	if(subImagesFiles[i] != null) {
        		ImagesDataUtils.imageToFolder(filePath, subImagesFiles[i], subImagesFiles[i].getOriginalFilename());
        		ImageModel subImageModel = ImageModel.builder()
                		.filePath(filePath)
                		.imgName(subImagesFiles[i].getOriginalFilename())
                		.imgType(subImagesFiles[i].getContentType())
                		.build();
        		subImagesModelSet.add(subImageModel);
        	}
        }
        imgData.setProductSubImages(subImagesModelSet);
        
		return imageDataRepo.save(imgData);
	}

	public Optional<ImagesData> findImageDataByParentId(Long parentId){
		return imageDataRepo.findByProductId(parentId);
	}
	
	public ImagesData updateImages(MultipartFile imageFile,
			MultipartFile[] subImagesFiles,
			Long productId) throws IOException {
		
		ImagesData imgData = findImageDataByParentId(productId).get();
        String filePath = FOLDER_PATH+productId+"\\";
        if(imageFile != null) {
        	ImagesDataUtils.imageToFolder(filePath, imageFile, imageFile.getOriginalFilename());
        	ImageModel mainImgMode = ImageModel.builder()
            		.filePath(filePath)
            		.imgName(imageFile.getOriginalFilename())
            		.imgType(imageFile.getContentType())
            		.build();
            imgData.setProductMainImage(mainImgMode);
        }
        
        Set<ImageModel> subImagesModelSet = new HashSet<>();
        for(int i=0; i< subImagesFiles.length; i++) {
        	if(subImagesFiles[i] != null) {
        		ImagesDataUtils.imageToFolder(filePath, subImagesFiles[i], subImagesFiles[i].getOriginalFilename());
        		ImageModel subImageModel = ImageModel.builder()
                		.filePath(filePath)
                		.imgName(subImagesFiles[i].getOriginalFilename())
                		.imgType(imageFile.getContentType())
                		.build();
        		subImagesModelSet.add(subImageModel);
        	}
        }
        imgData.setProductSubImages(subImagesModelSet);
        
		return imageDataRepo.save(imgData);
	}

//	public void deleteImageDataAndFolder(Long productid) {
//		imageDataRepo.deleteImageData(productid);
//		File directory = new File(FOLDER_PATH+productid);
//		for(String fileName : directory.list()) { // delete all inside files
//			File currentFile = new File(directory.getPath(),fileName);
//			currentFile.delete();
//		}
//		directory.delete();
//	}
	
	public void deleteImageDataAndFolder(Long productid) {
	    Optional<ImagesData> imageDataOptional = imageDataRepo.findByProductId(productid);
	    if (imageDataOptional.isPresent()) {
	        ImagesData imageData = imageDataOptional.get();
	        // Delete the associated productMainImage
	        if (imageData.getProductMainImage() != null) {
	            ImageModel mainImage = imageData.getProductMainImage();
	            imageData.setProductMainImage(null); // Remove the association
	            imageDataRepo.save(imageData); // Save the updated ImagesData entity
	            imageModelRepo.delete(mainImage); // Delete the associated ImageModel
	        }
	        // Delete the associated productSubImages
	        Set<ImageModel> subImages = imageData.getProductSubImages();
	        if (subImages != null && !subImages.isEmpty()) {
	            imageData.setProductSubImages(null); // Remove the association
	            imageDataRepo.save(imageData); // Save the updated ImagesData entity
	            imageModelRepo.deleteAll(subImages); // Delete the associated ImageModels
	        }
	        // Delete the ImagesData entity
	        imageDataRepo.deleteById(imageData.getImageid());
	        
	        //Delete from folder
			File directory = new File(FOLDER_PATH+productid);
			for(String fileName : directory.list()) { // delete all inside files
				File currentFile = new File(directory.getPath(),fileName);
				currentFile.delete();
			}
			directory.delete();
	    }
	} 


	
}

//	private final String FOLDER_PATH = "path\\images\\";