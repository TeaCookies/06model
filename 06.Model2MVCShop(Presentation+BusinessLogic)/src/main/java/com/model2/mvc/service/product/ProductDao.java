package com.model2.mvc.service.product;

import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;

public interface ProductDao {
	
	//INSERT
	public void addProduct(Product product) throws Exception ;
	
	//SELECT 
	public Product getProduct(int prodNo) throws Exception ;
	
	//SELECT LIST
	public List<Product> getProductList(Search search) throws Exception ;
	
	//UPDATE
	public void updateProduct(Product product) throws Exception;
	
	// 게시판 Page 처리를 위한 전체Row(totalCount)  return
	public int getTotalCount(Search search) throws Exception ;
	

}
