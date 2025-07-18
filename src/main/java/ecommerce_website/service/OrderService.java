package ecommerce_website.service;

import ecommerce_website.model.*;
import ecommerce_website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Order placeOrder(User user, List<Long> cartItemIds, String address) {
        List<CartItem> selectedCartItems = cartItemRepository.findAllById(cartItemIds);

        if (selectedCartItems.isEmpty()) {
            throw new IllegalArgumentException("No items selected for order.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(address);
        order.setOrderDate(LocalDateTime.now());

        double totalAmount = selectedCartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = selectedCartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setTotalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        // Clear selected cart items
        cartItemRepository.deleteAll(selectedCartItems);

        return savedOrder;
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }
}
