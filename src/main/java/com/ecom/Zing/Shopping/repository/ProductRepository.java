package com.ecom.Zing.Shopping.repository;

import com.ecom.Zing.Shopping.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer > {
    List<Product> findByIsActiveTrue();

    List<Product> findByCategory(String category);
}
