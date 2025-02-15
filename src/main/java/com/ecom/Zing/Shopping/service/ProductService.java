package com.ecom.Zing.Shopping.service;

import com.ecom.Zing.Shopping.model.Category;
import com.ecom.Zing.Shopping.model.Product;

import java.util.List;

public interface ProductService {
    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(int id);

    public Product getProductById(int id);
    public List<Product> getAllActiveProducts(String category);
}
