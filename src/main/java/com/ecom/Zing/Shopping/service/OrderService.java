package com.ecom.Zing.Shopping.service;

import com.ecom.Zing.Shopping.model.OrderRequest;
import com.ecom.Zing.Shopping.model.ProductOrder;

import java.util.List;

public interface OrderService {
    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception;

    public List<ProductOrder> getOrdersByUser(Integer userId);

    public ProductOrder updateOrderStatus(Integer id, String status);
    List<ProductOrder> getAllOrders();
}
