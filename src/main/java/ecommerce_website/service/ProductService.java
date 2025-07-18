package ecommerce_website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce_website.model.Product;
import ecommerce_website.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	@PostConstruct
	public void initData() {
	    for (int i = 1; i <= 200; i++) { // Insert 200 products
	        Product p = new Product();
	        p.setName("Product " + i);
	        p.setDescription("Demo product " + i);
	        p.setPrice(1000.0 + i);
	        p.setStock(10);
	        p.setCategory("Demo");
	        p.setImageUrl("/images/product" + i + ".jpeg");
	        productRepository.save(p);
	    }
	}



}
