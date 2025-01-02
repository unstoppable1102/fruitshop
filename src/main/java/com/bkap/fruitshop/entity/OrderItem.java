package com.bkap.fruitshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    private int quantity;
    private double price;
}
