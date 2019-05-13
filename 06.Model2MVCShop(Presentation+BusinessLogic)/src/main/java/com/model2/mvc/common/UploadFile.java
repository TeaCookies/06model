package com.model2.mvc.common;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public class UploadFile {
	
	private static String UPLOAD_PATH = "C:\\Users\\bitcamp\\git\\06model\\06.Model2MVCShop(Presentation+BusinessLogic)\\WebContent\\images\\uploadFiles\\";
	

	public static String saveFile(MultipartFile file) throws Exception{
	    // 파일 이름 변경
	    String saveName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	
	    // 저장할 File 객체를 생성(껍데기 파일)
	    File saveFile = new File(UPLOAD_PATH,saveName); // 저장할 폴더 이름, 저장할 파일 이름
	    file.transferTo(saveFile); // 업로드 파일에 saveFile이라는 껍데기 입힘
	    return saveName;
	} 
}
