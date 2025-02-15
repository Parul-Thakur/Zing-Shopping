package com.ecom.Zing.Shopping.repository;

import java.util.List;

import com.ecom.Zing.Shopping.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    List<ProductOrder> findByUserId(Integer userId);

}