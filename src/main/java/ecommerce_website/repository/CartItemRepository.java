package ecommerce_website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ecommerce_website.model.CartItem;
import ecommerce_website.model.Product;
import ecommerce_website.model.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	 void deleteByUser(User user); 
    List<CartItem> findByUser(User user);

    // ✅ Add this method to get one cart item by user and product
    CartItem findByUserAndProduct(User user, Product product);

    // ✅ Add this method to remove or update by product ID (used in controller)
    CartItem findByUserAndProduct_Id(User user, Long productId);

    // ✅ Use JPQL for matching multiple product IDs
    @Query("SELECT c FROM CartItem c WHERE c.user = :user AND c.product.id IN :productIds")
    List<CartItem> findByUserAndProduct_IdIn(@Param("user") User user, @Param("productIds") List<Long> productIds);
}
