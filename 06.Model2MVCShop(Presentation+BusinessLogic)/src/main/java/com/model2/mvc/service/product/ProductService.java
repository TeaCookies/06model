package com.model2.mvc.service.product;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;


public interface ProductService {
	
	//상품추가
	public void addProduct(Product productVO) throws Exception;

	//상품정보확인
	public Product getProduct(int prodNo) throws Exception;

	//상품 리스트
	public Map<String,Object> getProductList(Search searchVO) throws Exception;

	//상품 정보 수정
	public void updateProduct(Product productVO) throws Exception;
	
}