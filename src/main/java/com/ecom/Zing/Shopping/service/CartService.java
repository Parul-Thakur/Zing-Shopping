package com.ecom.Zing.Shopping.service;

import com.ecom.Zing.Shopping.model.Cart;

import java.util.List;

public interface CartService {
    public Cart saveCart(Integer productId, Integer userId);
    public List<Cart> getCartByUser(Integer userId);
    public Integer getCountCart(Integer userId);

    void updateQuantity(String sy, Integer cid);
}
