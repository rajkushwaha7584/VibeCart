package ecommerce_website.repository;

import ecommerce_website.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
