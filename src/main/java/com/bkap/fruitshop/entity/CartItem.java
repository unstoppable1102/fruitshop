package com.bkap.fruitshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items")
public class CartItem extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product product;

    private int quantity;
    private double price;
    @Transient
    private int countCartItem;

    public CartItem(Cart cart, Product product, int quantity, double price) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
