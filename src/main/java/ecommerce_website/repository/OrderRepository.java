package ecommerce_website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce_website.model.Order;
import ecommerce_website.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Fetch orders by user
    List<Order> findByUser(User user);
    
    // Optional: fetch most recent orders or order by date
    List<Order> findByUserOrderByOrderDateDesc(User user);
}
