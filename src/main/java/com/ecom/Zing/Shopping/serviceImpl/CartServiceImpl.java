package com.ecom.Zing.Shopping.serviceImpl;

import com.ecom.Zing.Shopping.model.Cart;
import com.ecom.Zing.Shopping.model.Product;
import com.ecom.Zing.Shopping.model.User;
import com.ecom.Zing.Shopping.repository.CartRepository;
import com.ecom.Zing.Shopping.repository.ProductRepository;
import com.ecom.Zing.Shopping.repository.UserRepository;
import com.ecom.Zing.Shopping.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer productId, Integer userId) {
        User user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();
        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);
        Cart cart = null;
        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getDiscountPrice());

        } else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());

        }
        Cart saveCart = cartRepository.save(cart);
        return saveCart;
    }


    @Override
    public List<Cart> getCartByUser(Integer userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        Double totalOrderPrice = 0.0;
        List<Cart> updateCarts = new ArrayList<>();
        for (Cart c : carts) {
            Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
            c.setTotalPrice(totalPrice);
            totalOrderPrice = totalOrderPrice + totalPrice;
            c.setTotalOrderPrice(totalOrderPrice);
            updateCarts.add(c);
        }

        return updateCarts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        Integer userById = cartRepository.countByUserId(userId);
        return userById;
    }

    @Override
    public void updateQuantity(String sy, Integer cid) {
        Cart cart = cartRepository.findById(cid).get();
        int updateQuantity;
        if (sy.equalsIgnoreCase("de")) {
            updateQuantity = cart.getQuantity() - 1;
            if (updateQuantity <= 0) {
                cartRepository.delete(cart);
            }else{
                cart.setQuantity(updateQuantity);
                cartRepository.save(cart);
            }
        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            cartRepository.save(cart);

        }

    }
}
