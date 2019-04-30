package com.model2.mvc.web.product;

import java.awt.image.RescaleOp;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


//==> 판매관리 Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
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
	
	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception {

		System.out.println("/addProductView.do");
		
		return "forward:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addUser( @ModelAttribute("product") Product product ) throws Exception {

		System.out.println("/addProduct.do");
		//Business Logic
		productService.addProduct(product);
		
		return "forward:/product/readProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct( @RequestParam("prodNo") int prodNo , Model model, @CookieValue(value="history") Cookie cookie ,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("/getProduct.do");
		
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);

		if (cookie != null) {
			if (!(cookie.getValue().contains(Integer.toString(prodNo)))) {
				cookie.setValue(cookie.getValue()+","+Integer.toString(prodNo));
				response.addCookie(cookie);
			}
		}else {
			response.addCookie(new Cookie("history", Integer.toString(prodNo)));
		}
		
//		Cookie[] cookies = request.getCookies();
//		
//		
//		if (cookies != null && cookies.length > 0 ){
//			for(int i=0; i<cookies.length; i++ ) {
//				Cookie cookie = cookies[i];
//				if (cookie.getName().equals("history")) {
//					cookie.setValue(cookie.getValue()+","+Integer.toString(prodNo));
//					response.addCookie(cookie);
//				}//안쪽에 else if문 생성하고 조건을 쿠키 네임이 history가 아닐경우로 설정하면 : history 외의 쿠키(j세션)일때 히스토리가 없는 것으로 간주하고 히스토리 쿠키를 또 다시 생성해 덮어씌움
//			}
//		}
//		if (cook.equals("")){
//			Cookie cookie = new Cookie("history", request.getParameter("prodNo"));
//			cookie.setMaxAge(-1);
//			response.addCookie(cookie);
//		}
		
		

		
		
		if ( request.getParameter("menu").equals("manage") ) {
			return "forward:/updateProductView.do";
		} else {
			return "forward:/product/getProduct.jsp";
		}
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct( @ModelAttribute("product") Product product , Model model) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		productService.updateProduct(product);

		model.addAttribute("product", product);
		System.out.println("디버깅1@@@@@@@@@@"+product.getRegDate());
		System.out.println("디버깅2@@@@@@@@@@"+product.getManuDate());
		
		return "forward:/product/getProduct.jsp?prodNo="+product.getProdNo();
	}
	
	
	@RequestMapping("/listProduct.do")
	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/product/listProduct.jsp";
	}
}