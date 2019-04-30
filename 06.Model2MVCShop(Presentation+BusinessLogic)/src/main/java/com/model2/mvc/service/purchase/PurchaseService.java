package com.model2.mvc.service.purchase;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;


public interface PurchaseService {

	//상품 구매
	public void addPurchase(Purchase purchase) throws Exception;
	
	//구매한 상품 tranNo로 확인
	public Purchase getPurchase(int tranNo) throws Exception;
	
	//구매한 상품 prodNo로 확인
	public Purchase getPurchase2(int prodNo) throws Exception;
	
	//구매목록 리스트
	public Map<String, Object> getPurchaseList(Search search,String buyerId) throws Exception;
	
	//구매한 상품 정보 수정
	public void updatePurchase(Purchase purchase) throws Exception;
	
	//구매한 상품 tranCode 수정
	public void updateTranCode(Purchase purchase) throws Exception;
	
}