package ecommerce_website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce_website.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategory(String category);
    boolean existsByName(String name);
	List<Product> findBySellerEmail(String sellerEmail);
}
