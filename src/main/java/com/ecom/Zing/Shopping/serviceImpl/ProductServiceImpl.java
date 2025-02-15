package com.ecom.Zing.Shopping.serviceImpl;

import com.ecom.Zing.Shopping.model.Category;
import com.ecom.Zing.Shopping.model.Product;
import com.ecom.Zing.Shopping.repository.ProductRepository;
import com.ecom.Zing.Shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProduct(int id) {
        Product product = productRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(product)) {
            productRepository.delete(product);
            return true;
        }

        return false;
    }

    @Override
    public Product getProductById(int id) {
       return  productRepository.findById(id).orElse(null);

    }


    @Override
    public List<Product> getAllActiveProducts(String category) {
        List<Product> products = null;
        if (ObjectUtils.isEmpty(category)) {
            products = productRepository.findByIsActiveTrue();
        } else {
            products = productRepository.findByCategory(category);
        }

        return products;
    }
}
