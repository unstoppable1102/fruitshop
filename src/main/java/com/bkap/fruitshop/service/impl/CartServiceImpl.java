package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.CartItemRequest;
import com.bkap.fruitshop.dto.request.CartRequest;
import com.bkap.fruitshop.dto.response.CartItemResponse;
import com.bkap.fruitshop.dto.response.CartResponse;
import com.bkap.fruitshop.entity.Cart;
import com.bkap.fruitshop.entity.CartItem;
import com.bkap.fruitshop.entity.Product;
import com.bkap.fruitshop.entity.User;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.CartItemRepository;
import com.bkap.fruitshop.repository.CartRepository;
import com.bkap.fruitshop.repository.ProductRepository;
import com.bkap.fruitshop.repository.UserRepository;
import com.bkap.fruitshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public CartResponse addToCart(CartRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        List<CartItem> cartItems = cart.getCartItems();
        for (CartItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                    .orElseGet(() -> {
                        CartItem newCartItem = new CartItem(cart, product, 0);
                        cartItems.add(newCartItem);
                        return newCartItem;
                    });
            cartItem.setQuantity(cartItem.getQuantity() + itemRequest.getQuantity());
            cartItemRepository.save(cartItem);
        }
        // Cập nhật tổng giá giỏ hàng
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        cart.setTotal(totalPrice);
        cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    @Override
    public List<CartResponse> getCartsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Cart> carts = cartRepository.findByUserId(userId)
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);

        return carts.stream().map(this::convertToCartResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CartResponse getCartById(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        return convertToCartResponse(cart);
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public int countCartItem(Long userId) {
        return cartItemRepository.countByCartUserId(userId);
    }

    @Override
    public double getTotalPrice(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
    }

    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse response = modelMapper.map(cart, CartResponse.class);

        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(item ->{
                    CartItemResponse itemResponse = new CartItemResponse();
                    itemResponse.setProductId(item.getProduct().getId());
                    itemResponse.setProductName(item.getProduct().getProductName());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setPrice(item.getProduct().getPrice()); // Cập nhật giá từng sản phẩm
                    return itemResponse;
                }).toList();
        response.setItems(items);
        response.setTotal(cart.getTotal());
        return response;
    }

    private CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemResponse.class);
    }

}
