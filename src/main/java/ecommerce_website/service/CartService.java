package ecommerce_website.service;

import ecommerce_website.model.CartItem;
import ecommerce_website.model.Product;
import ecommerce_website.model.User;
import ecommerce_website.repository.CartItemRepository;
import ecommerce_website.repository.ProductRepository;
import ecommerce_website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public boolean addProductToCart(Long productId, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Product product = productRepository.findById(productId).orElse(null);

        if (user == null || product == null) {
            return false;
        }

        addToCart(user, product);
        return true;
    }


    public void addToCart(User user, Product product) {
        List<CartItem> userItems = cartItemRepository.findByUser(user);

        CartItem existingItem = userItems.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setProductName(product.getName());
            newItem.setProductPrice(product.getPrice());
            newItem.setImageUrl(product.getImageUrl());
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
        }
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void updateQuantity(User user, Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .filter(i -> i.getUser().getId().equals(user.getId()))
                .orElse(null);
        if (item != null) {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    public void removeFromCart(User user, Long itemId) {
        cartItemRepository.findById(itemId)
                .filter(item -> item.getUser().getId().equals(user.getId()))
                .ifPresent(cartItemRepository::delete);
    }

    public double getTotal(User user) {
        return getCartItems(user).stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}
