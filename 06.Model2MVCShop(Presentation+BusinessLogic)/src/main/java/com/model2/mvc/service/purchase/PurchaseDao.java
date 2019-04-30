package com.model2.mvc.service.purchase;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseDao {
	
	//INSERT
	public void addPurchase(Purchase purchase) throws Exception ;
	
	//SELECT tranNO
	public Purchase getPurchase(int tranNo) throws Exception ;
	
	//SELECT prodNo
	public Purchase getPurchase2(int prodNo) throws Exception ;
	
	//SELCT LIST
	public Map<String , Object> getPurchaseList(Search search, String buyerId) throws Exception ;
	
	//UPDATE
	public void updatePurchase(Purchase purchase) throws Exception ;
	
	//UPDATE tranCode
	public void updateTranCode(Purchase purchase) throws Exception ;
	
	// 게시판 Page 처리를 위한 전체Row(totalCount)  return
	public int getTotalCount(String buyerId) throws Exception ;

}
