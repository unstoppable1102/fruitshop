package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.WishlistRequest;
import com.bkap.fruitshop.dto.response.WishlistResponse;
import com.bkap.fruitshop.entity.Product;
import com.bkap.fruitshop.entity.User;
import com.bkap.fruitshop.entity.Wishlist;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.ProductRepository;
import com.bkap.fruitshop.repository.UserRepository;
import com.bkap.fruitshop.repository.WishlistRepository;
import com.bkap.fruitshop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<WishlistResponse> findWishlistsByUserId(long userId) {
        return wishlistRepository.findWishlistsByUserId(userId).stream()
                .map((element) -> modelMapper.map(element, WishlistResponse.class))
                .collect(Collectors.toList());
    }


    @Override
    public WishlistResponse save(WishlistRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));


        Wishlist exists = wishlistRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId());
        if (exists != null) {
            return modelMapper.map(exists, WishlistResponse.class);

        }else {
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user);
            wishlist.setProduct(product);
            wishlist.setPrice(product.getPrice());
            return modelMapper.map(wishlistRepository.save(wishlist), WishlistResponse.class);
        }

    }

    @Override
    public void delete(long id) {
        if (!wishlistRepository.existsById(id)) {
            throw new AppException(ErrorCode.WISHLIST_NOT_FOUND);
        }
        wishlistRepository.deleteById(id);
    }
}
