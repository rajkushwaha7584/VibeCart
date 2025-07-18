package ecommerce_website.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    private String productName;
    private double productPrice;
    private int quantity;
    private String imageUrl;

    public double getTotalPrice() {
        return productPrice * quantity;
    }

    public Long getProductId() {
        return product.getId();
    }
}
