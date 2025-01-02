package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.CartItemRequest;
import com.bkap.fruitshop.dto.response.CartItemResponse;
import com.bkap.fruitshop.entity.CartItem;
import com.bkap.fruitshop.entity.Product;
import com.bkap.fruitshop.entity.User;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.CartItemRepository;
import com.bkap.fruitshop.repository.ProductRepository;
import com.bkap.fruitshop.repository.UserRepository;
import com.bkap.fruitshop.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    @Override
    public List<CartItemResponse> findByUserId(long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItems.stream()
                .map((element) -> modelMapper.map(element, CartItemResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemResponse addCartItem(CartItemRequest request) {
        //Get user information
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        //get product information
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingCartItem);
            return modelMapper.map(existingCartItem, CartItemResponse.class);
        }else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(cartItem);
            return modelMapper.map(cartItem, CartItemResponse.class);
        }

    }

    @Override
    public long countItemsInCart(long userId) {
        return cartItemRepository.countCartItemsByUserId(userId);
    }

    @Override
    public Double calculateTotalPrice(long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        return items.stream().mapToDouble(item ->{
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            return product.getPrice() * item.getQuantity();
        }).sum();
    }

    @Override
    public void updateCartItems(long userId, Map<String, String> quantities) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        for (CartItem cartItem : cartItems) {
            String quantityStr = quantities.get(cartItem.getProduct().getId().toString());
            if (quantityStr != null) {
                cartItem.setQuantity(Integer.parseInt(quantityStr));
                cartItemRepository.save(cartItem);
            }
        }

    }

    @Override
    public void removeCartItem(long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        cartItemRepository.deleteAll(cartItems);
    }
}
