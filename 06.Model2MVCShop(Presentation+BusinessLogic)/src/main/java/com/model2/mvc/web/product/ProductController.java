package com.model2.mvc.web.product;


import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.UploadFile;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


//==> �ǸŰ��� Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "uploadPath")
	private String uploadPath;

	//setter Method ���� ����
	
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	
	
	@RequestMapping("/addProductView.do")
	public ModelAndView addProductView() throws Exception {

		System.out.println("/addProductView.do");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/product/addProductView.jsp");
		
		return modelAndView;
	}
	
	
	
	@RequestMapping("/addProduct.do")
	public ModelAndView addUser( @ModelAttribute("product") Product product, MultipartHttpServletRequest mtfRequest )throws Exception {

		System.out.println("/addProduct.do");
		//Business Logic
		
		product.setFileName(UploadFile.saveFile(mtfRequest.getFile("file"),uploadPath));
		productService.addProduct(product);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/product/readProduct.jsp");
		
		return modelAndView;
	}
	

	
	@RequestMapping("/getProduct.do")
	public ModelAndView getProduct( @RequestParam("prodNo") int prodNo , 
								@RequestParam("menu") String menu,
								@CookieValue(value="history", required=false)  Cookie cookie ,
								HttpServletResponse response) throws Exception {
		
		System.out.println("/getProduct.do");
		
		//Business Logic
		Product product = productService.getProduct(prodNo);
		System.out.println("�������      ���� �̸� Ȯ��      �����������"+product.getFileName());
		System.out.println("�������      ���� �̸� Ȯ��      �����������");
		
		// Model �� View ����
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("product", product);	
		
		
		if ( menu.equals("manage") ) {
			modelAndView.setViewName("/updateProductView.do");
			return modelAndView;
			
		} else {
			if (cookie != null ) {
				if (!(cookie.getValue().contains(Integer.toString(prodNo)))) {
					cookie.setValue(cookie.getValue()+","+Integer.toString(prodNo));
					response.addCookie(cookie);
				}
			}else {
				response.addCookie(new Cookie("history", Integer.toString(prodNo)));
			}
			
			modelAndView.setViewName("/product/getProduct.jsp");
			return modelAndView;
		}
	}
	
	
	
	
	
	@RequestMapping("/updateProductView.do")
	public ModelAndView updateProductView( @RequestParam("prodNo") int prodNo ) throws Exception{

		System.out.println("/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(prodNo);

		// Model �� View ����
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/product/updateProduct.jsp");
		modelAndView.addObject("product", product);
		
		return modelAndView;
	}
	
	
	
	
	@RequestMapping("/updateProduct.do")
	public ModelAndView updateProduct( @ModelAttribute("product") Product product) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		productService.updateProduct(product);
		product = productService.getProduct(product.getProdNo());
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/product/getProduct.jsp?prodNo="+product.getProdNo());
		modelAndView.addObject("product", product);
		
		return modelAndView;
	}
	
	
	
	
	@RequestMapping("/listProduct.do")
	public ModelAndView listProduct( @ModelAttribute("search") Search search ) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		
		// Business logic ����
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/product/listProduct.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		
		return modelAndView;
	}
	
	
	
}