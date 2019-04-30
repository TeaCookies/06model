package com.model2.mvc.service.purchase.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


/*
 *	FileName :  UserServiceTest.java
 * ㅇ JUnit4 (Test Framework) 과 Spring Framework 통합 Test( Unit Test)
 * ㅇ Spring 은 JUnit 4를 위한 지원 클래스를 통해 스프링 기반 통합 테스트 코드를 작성 할 수 있다.
 * ㅇ @RunWith : Meta-data 를 통한 wiring(생성,DI) 할 객체 구현체 지정
 * ㅇ @ContextConfiguration : Meta-data location 지정
 * ㅇ @Test : 테스트 실행 소스 지정
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/context-common.xml" ,
									"classpath:config/context-aspect.xml",
									"classpath:config/context-mybatis.xml",
									"classpath:config/context-transaction.xml"})
public class PurchaseServiceTest {

	//==>@RunWith,@ContextConfiguration 이용 Wiring, Test 할 instance DI
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;

	//@Test	//==>확인 
	public void testAddPurchase() throws Exception {
		
		User user =  new User();
		Product product = new Product();
		Purchase purchase = new Purchase();
		
		user.setUserId("user18");
		product.setProdNo(10080);
		purchase.setPurchaseProd(product);
		purchase.setBuyer(user);
		purchase.setPaymentOption("1");
		purchase.setReceiverName("받는사람");
		purchase.setReceiverPhone("101010");
		purchase.setDivyAddr("받는사람주소");
		purchase.setDivyRequest("경비실");
		purchase.setTranCode("1");
		purchase.setDivyDate("20190423");
		
		purchaseService.addPurchase(purchase);
		
		purchase = purchaseService.getPurchase(10061);

		//==> console 확인
		System.out.println("\n :: console 확인 :: "+purchase);
		
		//==> API 확인
		Assert.assertEquals("다시확인", purchase.getReceiverName());
		Assert.assertEquals(10061, purchase.getPurchaseProd().getProdNo());
		Assert.assertEquals("user18", purchase.getBuyer().getUserId());
		Assert.assertEquals("19-04-23", purchase.getDivyDate());
		Assert.assertEquals("1", purchase.getTranCode());
	}
	
	@Test	//==>확인
	public void testGetPurchase() throws Exception {
		
		Purchase purchase = new Purchase();
		
		purchase = purchaseService.getPurchase(10000);

		//==> console 확인
		System.out.println("\n :: console 확인 :: "+purchase);
		
		//==> API 확인
		Assert.assertEquals("SCOTT", purchase.getReceiverName());
		Assert.assertEquals(10001, purchase.getPurchaseProd().getProdNo());
		Assert.assertEquals("19-04-26", purchase.getDivyDate());
		Assert.assertEquals("2019-04-26", purchase.getOrderDate().toString());
		
		
		Assert.assertNotNull(purchaseService.getPurchase(10000));
	}
	
	
	//@Test	//==>확인
	public void testGetPurchase2() throws Exception {
		
		Purchase purchase = new Purchase();
		
		purchase = purchaseService.getPurchase2(10061);

		//==> console 확인
		System.out.println("\n :: console 확인 :: "+purchase);
		
		//==> API 확인
		Assert.assertEquals("다시확인", purchase.getReceiverName());
		Assert.assertEquals(10061, purchase.getPurchaseProd().getProdNo());

		Assert.assertNotNull(purchaseService.getPurchase2(10061));
		Assert.assertNotNull(purchaseService.getPurchase2(10062));
	}
	
	// @Test	//==>확인
	 public void testUpdatePurchase() throws Exception{
		 
		Purchase purchase = purchaseService.getPurchase(10061);
		Assert.assertNotNull(purchase);
		
		Assert.assertEquals("다시확인", purchase.getReceiverName());
		Assert.assertEquals(10061, purchase.getPurchaseProd().getProdNo());

		purchase.setPaymentOption("1");
		purchase.setTranCode("2");
		purchase.setReceiverName("확인");
		purchase.setDivyDate("20170707");
		purchaseService.updatePurchase(purchase);
		
		purchase = purchaseService.getPurchase(10061);
		Assert.assertNotNull(purchase);
		
		//==> console 확인
		System.out.println("\n :: console 확인 :: "+purchase);
			
		//==> API 확인
		Assert.assertEquals("1", purchase.getPaymentOption());
		Assert.assertEquals("2", purchase.getTranCode());
		//Assert.assertEquals("19/04/23", purchase.getDivyDate());
		Assert.assertEquals("확인", purchase.getReceiverName());
	 }
	 
	 
	 //@Test	//==>확인
	 public void testUpdateTranCode() throws Exception{
		 
		Purchase purchase = purchaseService.getPurchase(10061);
		Assert.assertNotNull(purchase);
		
		Assert.assertEquals("1", purchase.getTranCode());

		purchase.setTranCode("2");
		
		purchaseService.updateTranCode(purchase);
		
		purchase = purchaseService.getPurchase(10061);
		Assert.assertNotNull(purchase);
		
		//==> console 확인
		System.out.println("\n :: console 확인 :: "+purchase);
			
		//==> API 확인
		Assert.assertEquals("2", purchase.getTranCode());
	 }
	

	 //@Test	//==>확인
	 public void testGetPurchaseListById() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);	 	
	 	Map<String,Object> map = purchaseService.getPurchaseList(search, "user18");
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console 확인
	 	System.out.println("\n :: console 확인 :: "+list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	map = purchaseService.getPurchaseList(search, "user18");
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
	 	//==> console 확인
	 	System.out.println("\n :: console 확인 :: "+list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	
}