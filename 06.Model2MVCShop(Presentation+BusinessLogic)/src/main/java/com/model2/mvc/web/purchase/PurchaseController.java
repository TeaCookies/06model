package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;


//==> 구매관리 Controller
@Controller
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	
	
	@RequestMapping("/addPurchaseView.do")
	public String addPurchaseView(@ModelAttribute("purchase") Purchase purchase, Model model, HttpServletRequest request ) throws Exception {

		System.out.println("/addPurchaseView.do");

		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchase.setBuyer((User)request.getSession().getAttribute("user"));
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	
	
	
	@RequestMapping("/addPurchase.do")
	public String addPurchase( @ModelAttribute("purchase") Purchase purchase, HttpServletRequest request  ) throws Exception {

		System.out.println("/addPurchase.do");
		
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchase.setBuyer((User)request.getSession().getAttribute("user"));
		
		//Business Logic
		purchaseService.addPurchase(purchase);
		
		return "forward:/purchase/readPurchase.jsp";
	}
	
	
	
	
	@RequestMapping("/getPurchase.do")
	public String getPurchase( @RequestParam("tranNo") int tranNo , Model model, HttpServletRequest request ) throws Exception {
		
		System.out.println("/getPurchase.do");
		
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	

	
	@RequestMapping("/updatePurchaseView.do")
	public String updatePurchaseView( @RequestParam("tranNo") int tranNo , Model model ) throws Exception{

		System.out.println("/updatePurchaseView.do");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchase.jsp?tranNo="+tranNo;
	}
	
	
	
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchase( @ModelAttribute("purchase") Purchase purchase , Model model) throws Exception{

		System.out.println("/updatePurchase.do");
		//Business Logic
		purchaseService.updatePurchase(purchase);

		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp?tranNo="+purchase.getTranNo();
	}
	
	
	
	@RequestMapping("/updateTranCode.do")
	public String updateTranCode( @ModelAttribute("purchase") Purchase purchase , Model model, HttpServletRequest request) throws Exception{

		System.out.println("/updateTranCode.do");
		
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		
		Product product = productService.getProduct(prodNo);
		purchase = purchaseService.getPurchase2(prodNo);
		
		System.out.println("###############################"+purchase);
	
		if (request.getParameter("tranCode").trim().equals("1")) {
			product.setProTranCode("2");
		} else if (request.getParameter("tranCode").trim().equals("2")) {
			product.setProTranCode("3");
		}	
		System.out.println("@@@@@@@@확인용@@@@@@@@@"+product.getProTranCode());
		purchase.setTranCode(product.getProTranCode());
		System.out.println("===============================================");
		//Business Logic
		purchaseService.updateTranCode(purchase);

		model.addAttribute("purchase", purchase);
		
		if (request.getParameter("tranCode").trim().equals("1")) {
			return "forward:/listProduct.do?menu="+request.getParameter("menu");
		}else {	
			return "forward:/listPurchase.do";
		}
	}
	
	
	
	
	@RequestMapping("/listPurchase.do")
	public String listPurchase( @ModelAttribute("search") Search search, Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listPurchase.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		HttpSession session = request.getSession();
		String buyerId = ((User)session.getAttribute("user")).getUserId();
		
		// Business logic 수행
		Map<String , Object> map=purchaseService.getPurchaseList(search, buyerId);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/listPurchase.jsp";
	}
}